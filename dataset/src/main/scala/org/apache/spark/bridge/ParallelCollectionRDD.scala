package org.apache.spark.bridge

import scala.collection.Map
import scala.reflect.ClassTag

/**
 * Created by zhangquanhong on 2017/8/24.
 */
class ParallelCollectionRDD[T: ClassTag](
                                          sc: SparkContext,
                                          @transient private val data: Seq[T],
                                          numSlices: Int,
                                          locationPrefs: Map[Int, Seq[String]])
  extends org.apache.spark.rdd.ParallelCollectionRDD[T](sc, data, numSlices, locationPrefs){

}
