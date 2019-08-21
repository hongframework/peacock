package org.apache.spark.bridge

import java.io.{DataOutputStream, DataInputStream}

import org.apache.spark.api.r.JVMObjectTracker

/**
 * Created by zhangquanhong on 2017/8/24.
 */
object SerDe{
  def readInt(in: DataInputStream): Int = org.apache.spark.api.r.SerDe.readInt(in)
  def readObject(dis: DataInputStream): Object =  org.apache.spark.api.r.SerDe.readObject(dis,null)
  def writeObject(dos: DataOutputStream, obj: Object): Unit =  org.apache.spark.api.r.SerDe.writeObject(dos,obj, null)
}
