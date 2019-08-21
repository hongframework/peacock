package org.apache.spark.sql.execution.smartsql

import java.util.Random

import org.apache.spark._
import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.internal.Logging
import org.apache.spark.rdd._
import org.apache.spark.bridge.{Utils => collectionUtils, BoundedPriorityQueue}
import scala.reflect._

object ResultSet{


  /** Build the union of a list of RDDs passed as variable-length arguments. */
  def union[T: ClassTag](first: ResultSet[T], rest: ResultSet[T]*): ResultSet[T] = {
    union(Seq(first) ++ rest)
  }
  /** Build the union of a list of ResultSets. */
  def union[T: ClassTag](rdds: Seq[ResultSet[T]]): ResultSet[T] =  {

    new ResultSet(rdds.foldLeft(Iterator[T]())((iter,rdd) => {
      iter ++ rdd.data
    }))
  }
}

/**
 * Created by zhangquanhong on 2017/8/9.
 */
class ResultSet[T: ClassTag](
      val data : Iterator[T] = Iterator(),
       @transient private var deps: Seq[ResultSet[_]] = Nil
       ) extends Serializable with Logging {

  private[spark] def elementClassTag: ClassTag[T] = classTag[T]

  if (classOf[ResultSet[_]].isAssignableFrom(elementClassTag.runtimeClass)) {
    // This is a warning instead of an exception in order to avoid breaking user programs that
    // might have defined nested RDDs without running jobs with them.
    logWarning("Spark does not support nested RDDs (see SPARK-5063)")
  }



  /**
   * :: DeveloperApi ::
   * Implemented by subclasses to compute a given partition.
   */
  @DeveloperApi
  def compute(partitions : Int): Iterator[T] = {
    data
  }


  /**
   * Implemented by subclasses to return how this RDD depends on parent RDDs. This method will only
   * be called once, so it is safe to implement a time-consuming computation in it.
   */
  protected def getDependencies: Seq[ResultSet[_]] = deps


  private[spark] def mapPartitionsInternal[U: ClassTag](
       f: Iterator[T] => Iterator[U],
       preservesPartitioning: Boolean = false): ResultSet[U] =  {
    new ResultSet(f(this.compute(1)))
  }

  def collect(): Array[T] = {
    val results = this.compute(1).toArray
//    Array.concat(results: _*)
    results
  }

  def toLocalIterator: Iterator[T] =  {
    val results = this.compute(1)
    //    Array.concat(results: _*)
    results
  }

  final def partitions: Array[Partition] = {
    getPartitions
  }
  /**
   * Implemented by subclasses to return the set of partitions in this RDD. This method will only
   * be called once, so it is safe to implement a time-consuming computation in it.
   *
   * The partitions in this array must satisfy the following property:
   *   `rdd.partitions.zipWithIndex.forall { case (partition, index) => partition.index == index }`
   */
  protected def getPartitions: Array[Partition] = {
    Array(SmartPartition(1))
  }

  case class SmartPartition(index : Int) extends Partition{

  }

  /**
   * Return a new RDD by applying a function to each partition of this RDD, while tracking the index
   * of the original partition.
   *
   * `preservesPartitioning` indicates whether the input function preserves the partitioner, which
   * should be `false` unless this is a pair RDD and the input function doesn't modify the keys.
   */
  def mapPartitionsWithIndex[U: ClassTag](
                                           f: (Int, Iterator[T]) => Iterator[U],
                                           preservesPartitioning: Boolean = false): ResultSet[U] = {
//    new ResultSet(getPartitions.map(part => {
//      f(part.index,data)
//    }).toIterator).asInstanceOf[ResultSet[U]]

    new ResultSet(f(1,data))
  }

  /**
   * Return a new RDD that is reduced into `numPartitions` partitions.
   *
   * This results in a narrow dependency, e.g. if you go from 1000 partitions
   * to 100 partitions, there will not be a shuffle, instead each of the 100
   * new partitions will claim 10 of the current partitions. If a larger number
   * of partitions is requested, it will stay at the current number of partitions.
   *
   * However, if you're doing a drastic coalesce, e.g. to numPartitions = 1,
   * this may result in your computation taking place on fewer nodes than
   * you like (e.g. one node in the case of numPartitions = 1). To avoid this,
   * you can pass shuffle = true. This will add a shuffle step, but means the
   * current upstream partitions will be executed in parallel (per whatever
   * the current partitioning is).
   *
   * @note With shuffle = true, you can actually coalesce to a larger number
   * of partitions. This is useful if you have a small number of partitions,
   * say 100, potentially with a few partitions being abnormally large. Calling
   * coalesce(1000, shuffle = true) will result in 1000 partitions with the
   * data distributed using a hash partitioner. The optional partition coalescer
   * passed in must be serializable.
   */
  def coalesce(numPartitions: Int, shuffle: Boolean = false,
               partitionCoalescer: Option[PartitionCoalescer] = Option.empty)
              (implicit ord: Ordering[T] = null)
  : ResultSet[T] =  {
    require(numPartitions > 0, s"Number of partitions ($numPartitions) must be positive.")
//    if (shuffle) {
//      /** Distributes elements evenly across output partitions, starting from a random partition. */
//      val distributePartition = (index: Int, items: Iterator[T]) => {
//        var position = (new Random(index)).nextInt(numPartitions)
//        items.map { t =>
//          // Note that the hash code of the key will just be the key itself. The HashPartitioner
//          // will mod it with the number of total partitions.
//          position = position + 1
//          (position, t)
//        }
//      } : Iterator[(Int, T)]
//
//      // include a shuffle step so that our upstream tasks are still distributed
//      new CoalescedRDD(
//        new ShuffledRDD[Int, T, T](mapPartitionsWithIndex(distributePartition),
//          new HashPartitioner(numPartitions)),
//        numPartitions,
//        partitionCoalescer).values
//    } else {
//      new CoalescedRDD(this, numPartitions, partitionCoalescer)
//    }
    this
  }


  /**
   * [performance] Spark's internal mapPartitionsWithIndex method that skips closure cleaning.
   * It is a performance API to be used carefully only if we are sure that the RDD elements are
   * serializable and don't require closure cleaning.
   *
   * @param preservesPartitioning indicates whether the input function preserves the partitioner,
   * which should be `false` unless this is a pair RDD and the input function doesn't modify
   * the keys.
   */
  private[spark] def mapPartitionsWithIndexInternal[U: ClassTag](
                                                                  f: (Int, Iterator[T]) => Iterator[U],
                                                                  preservesPartitioning: Boolean = false): ResultSet[U] =  {
//    new ResultSet(getPartitions.map(part => {
//      f(part.index,data)
//    }).toIterator).asInstanceOf[ResultSet[U]]
    new ResultSet(f(1,data))
  }

  def mapPartitions[U: ClassTag](
                                  f: Iterator[T] => Iterator[U],
                                  preservesPartitioning: Boolean = false): ResultSet[U] = {
//    new ResultSet(getPartitions.map(part => {
//      f(data)
//    }).toIterator).asInstanceOf[ResultSet[U]]

    new ResultSet(f(data))
  }

  def map[U: ClassTag](f: T => U): ResultSet[U] = {
    new ResultSet(data.map(f(_)))
  }

  /**
   * Returns the first k (smallest) elements from this RDD as defined by the specified
   * implicit Ordering[T] and maintains the ordering. This does the opposite of [[]].
   * For example:
   * {{{
   *   sc.parallelize(Seq(10, 4, 2, 12, 3)).takeOrdered(1)
   *   // returns Array(2)
   *
   *   sc.parallelize(Seq(2, 3, 4, 5, 6)).takeOrdered(2)
   *   // returns Array(2, 3)
   * }}}
   *
   * @note This method should only be used if the resulting array is expected to be small, as
   * all the data is loaded into the driver's memory.
   *
   * @param num k, the number of elements to return
   * @param ord the implicit ordering for T
   * @return an array of top elements
   */
  def takeOrdered(num: Int)(implicit ord: Ordering[T]): Array[T] = {
    if (num == 0) {
      Array.empty
    } else {
      val mapRDDs = mapPartitions { items =>
        // Priority keeps the largest elements, so let's reverse the ordering.
        val queue = new BoundedPriorityQueue[T](num)(ord.reverse)
        queue ++= collectionUtils.takeOrdered(items, num)(ord)
        Iterator.single(queue)
      }
      if (mapRDDs.partitions.length == 0) {
        Array.empty
      } else {
        mapRDDs.reduce { (queue1, queue2) =>
          queue1 ++= queue2
          queue1
        }.toArray.sorted(ord)
      }
    }
  }

  /**
   * Reduces the elements of this RDD using the specified commutative and
   * associative binary operator.
   */
  def reduce(f: (T, T) => T): T =  {
//    val reducePartition: Iterator[T] => Option[T] = iter => {
//      if (iter.hasNext) {
//        Some(iter.reduceLeft(f))
//      } else {
//        None
//      }
//    }
//    var jobResult: Option[T] = None
//    val mergeResult = (index: Int, taskResult: Option[T]) => {
//      if (taskResult.isDefined) {
//        jobResult = jobResult match {
//          case Some(value) => Some(f(value, taskResult.get))
//          case None => taskResult
//        }
//      }
//    }
//    sc.runJob(this, reducePartition, mergeResult)
//    // Get the final result out of our Option, or throw an exception if the RDD was empty
//    jobResult.getOrElse(throw new UnsupportedOperationException("empty collection"))
    data.reduce(f)
  }

  /**
   * Zip this RDD's partitions with one (or more) RDD(s) and return a new RDD by
   * applying a function to the zipped partitions. Assumes that all the RDDs have the
   * *same number of partitions*, but does *not* require them to have the same number
   * of elements in each partition.
   */
  def zipPartitions[B: ClassTag, V: ClassTag]
  (rdd2: ResultSet[B], preservesPartitioning: Boolean = false)
  (f: (Iterator[T], Iterator[B]) => Iterator[V]): ResultSet[V] =  {
    new ResultSet(f(this.data, rdd2.data))
  }

  /**
   * Aggregate the elements of each partition, and then the results for all the partitions, using a
   * given associative function and a neutral "zero value". The function
   * op(t1, t2) is allowed to modify t1 and return it as its result value to avoid object
   * allocation; however, it should not modify t2.
   *
   * This behaves somewhat differently from fold operations implemented for non-distributed
   * collections in functional languages like Scala. This fold operation may be applied to
   * partitions individually, and then fold those results into the final result, rather than
   * apply the fold to each element sequentially in some defined ordering. For functions
   * that are not commutative, the result may differ from that of a fold applied to a
   * non-distributed collection.
   *
   * @param zeroValue the initial value for the accumulated result of each partition for the `op`
   *                  operator, and also the initial value for the combine results from different
   *                  partitions for the `op` operator - this will typically be the neutral
   *                  element (e.g. `Nil` for list concatenation or `0` for summation)
   * @param op an operator used to both accumulate results within a partition and combine results
   *                  from different partitions
   */
  def fold(zeroValue: T)(op: (T, T) => T): T =  {
    data.fold(zeroValue)(op)
  }
}

