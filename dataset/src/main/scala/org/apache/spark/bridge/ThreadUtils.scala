package org.apache.spark.bridge

import java.util.concurrent.{Executors, ThreadPoolExecutor}

/**
 * Created by zhangquanhong on 2017/8/24.
 */
object ThreadUtils {
  def newDaemonCachedThreadPool(prefix: String): ThreadPoolExecutor = org.apache.spark.util.ThreadUtils.newDaemonCachedThreadPool(prefix)

  /**
   * Create a cached thread pool whose max number of threads is `maxThreadNumber`. Thread names
   * are formatted as prefix-ID, where ID is a unique, sequentially assigned integer.
   */
  def newDaemonCachedThreadPool(
                                 prefix: String, maxThreadNumber: Int, keepAliveSeconds: Int = 60): ThreadPoolExecutor = org.apache.spark.util.ThreadUtils.newDaemonCachedThreadPool(prefix, maxThreadNumber, keepAliveSeconds)
}
