package org.apache.spark.bridge

import org.apache.spark.SparkConf

/**
 * Created by zhangquanhong on 2017/8/24.
 */
class SparkContext(config: SparkConf) extends org.apache.spark.SparkContext(config){
  override def conf: SparkConf = super.conf
}
