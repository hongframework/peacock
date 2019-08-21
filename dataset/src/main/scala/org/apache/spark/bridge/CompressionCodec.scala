package org.apache.spark.bridge

import org.apache.spark.SparkConf
import org.apache.spark.io.CompressionCodec

/**
 * Created by zhangquanhong on 2017/8/24.
 */
object CompressionCodec {
  def createCodec(conf: SparkConf): CompressionCodec = org.apache.spark.io.CompressionCodec.createCodec(conf)

  def createCodec(conf: SparkConf, codecName: String): CompressionCodec = org.apache.spark.io.CompressionCodec.createCodec(conf, codecName)
}
