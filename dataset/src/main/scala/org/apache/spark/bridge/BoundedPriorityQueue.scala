package org.apache.spark.bridge

/**
 * Created by zhangquanhong on 2017/8/24.
 */
class BoundedPriorityQueue [A](maxSize: Int)(implicit ord: Ordering[A])
  extends org.apache.spark.util.BoundedPriorityQueue[A](maxSize)(ord){

}
