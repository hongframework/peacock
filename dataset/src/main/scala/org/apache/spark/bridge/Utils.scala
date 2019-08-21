package org.apache.spark.bridge

import java.io.File

import org.apache.spark.internal.Logging

/**
 * Created by zhangquanhong on 2017/8/24.
 */
object Utils extends Logging{
  def createTempDir(
                     root: String = System.getProperty("java.io.tmpdir"),
                     namePrefix: String = "spark"): File = org.apache.spark.util.Utils.createTempDir(root, namePrefix)


  def takeOrdered[T](input: Iterator[T], num: Int)(implicit ord: Ordering[T]): Iterator[T] =org.apache.spark.util.collection.Utils.takeOrdered(input,num)(ord)
}
