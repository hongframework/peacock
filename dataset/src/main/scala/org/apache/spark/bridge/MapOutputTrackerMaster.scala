package org.apache.spark.bridge

import org.apache.spark.{MapOutputTracker, SparkConf}
import org.apache.spark.broadcast.BroadcastManager

/**
 * Created by zhangquanhong on 2017/8/24.
 */
class MapOutputTrackerMaster(conf: SparkConf,
                             broadcastManager: BroadcastManager, isLocal: Boolean)
  extends org.apache.spark.MapOutputTrackerMaster(conf,broadcastManager,isLocal) {

}
