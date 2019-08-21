package com.hframework.smartsql

import org.apache.spark.SparkConf
import org.apache.spark.bridge.{ParallelCollectionRDD, SparkContext}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.catalyst.InternalRow

import scala.reflect.ClassTag

/**
 * Created by zhangquanhong on 2017/8/8.
 */

object SmartRDD{
  val conf = {
    val _conf = new SparkConf()
    _conf.set("spark.master","1")
    _conf.set("spark.app.name","1")
    _conf
  }
  val sc = {
    new SparkContext(conf)
  }
  def apply(data: Seq[InternalRow]) : SmartRDD[InternalRow] = {
    new SmartRDD(data, sc)
  }
}

class SmartRDD[T: ClassTag]  (@transient private val data: Seq[T], _sc: SparkContext = null
) extends ParallelCollectionRDD[T](_sc, data, 1, Map.empty) with Logging{
  private def sc: SparkContext = {
    null
  }
}
