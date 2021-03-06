/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.execution.smartsql

import org.apache.spark.bridge.ParallelCollectionRDD
import org.apache.spark.rdd.{ RDD}
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.{Attribute, UnsafeProjection}

import scala.reflect.ClassTag


/**
 * Physical plan node for scanning data from a local collection.
 */
case class LocalTableScanExec(
    output: Seq[Attribute],
    rows: Seq[InternalRow]) extends LeafExecNode {

//  override lazy val metrics = Map(
//    "numOutputRows" -> SQLMetrics.createMetric(sparkContext, "number of output rows"))

  private lazy val unsafeRows: Array[InternalRow] = {
    if (rows.isEmpty) {
      Array.empty
    } else {
      val proj = UnsafeProjection.create(output, output)
      rows.map(r => proj(r).copy()).toArray
    }
  }

  private lazy val numParallelism: Int = 2//math.min(math.max(unsafeRows.length, 1),
//    sqlContext.sparkContext.defaultParallelism)
//
  private lazy val rdd = parallelize(unsafeRows, numParallelism)

  def parallelize[T: ClassTag](
                                seq: Seq[T],
                                numSlices: Int ): RDD[T] =  {
    new ParallelCollectionRDD[T](null, seq, numSlices, Map[Int, Seq[String]]())
  }

  protected override def doExecute(parameters : Map[String, Any] = Map.empty[String, Any]): ResultSet[InternalRow] = {
    val numOutputRows = longMetric("numOutputRows")
    rdd.map { r =>
      numOutputRows += 1
      r
    }
    new ResultSet()
  }

  override protected def stringArgs: Iterator[Any] = {
    if (rows.isEmpty) {
      Iterator("<empty>", output)
    } else {
      Iterator(output)
    }
  }

  override def executeCollect(parameters : Map[String, Any] = Map.empty[String, Any]): Array[InternalRow] = {
    longMetric("numOutputRows").add(unsafeRows.size)
    unsafeRows
  }

  override def executeTake(limit: Int): Array[InternalRow] = {
    val taken = unsafeRows.take(limit)
    longMetric("numOutputRows").add(taken.size)
    taken
  }
}
