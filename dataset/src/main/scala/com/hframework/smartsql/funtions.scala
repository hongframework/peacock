package com.hframework.smartsql

import java.security.{MessageDigest, NoSuchAlgorithmException}

/**
 * Created by zhangquanhong on 2017/8/14.
 */

trait Func{
  def invoke(vars: Any*): String
}

object SHA256Func extends Func{
  override def invoke(vars: Any*): String = sha256Encode(String.valueOf(vars.head))

  def sha256Encode(v: String): String = {
    var md: MessageDigest = null
    try {
      md = MessageDigest.getInstance("SHA-256")
    }
    catch {
      case e: NoSuchAlgorithmException => {
        return null
      }
    }
    md.update(v.getBytes)
    return byteToHex(md.digest)
  }

  def byteToHex(hash: Array[Byte]): String = {
    try {
      val formatter: java.util.Formatter = new java.util.Formatter
//      for (b <- hash) formatter.format("%02x", b)
      return formatter.toString
    }
    catch {
      case e: Exception => {
        return ""
      }
    }
  }
}