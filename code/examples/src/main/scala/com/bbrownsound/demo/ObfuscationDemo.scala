package com.bbrownsound.demo

import com.bbrownsound.macros._

object ObfuscationDemo extends App {
  @ToStringObfuscateV2("password", "pinCode")
  case class SampleUser(name: String, username: String, password: String, pinCode: Long)

  val su = SampleUser(name = "brandon", username = "brbrown", password = "hunter2", pinCode = 12345)

  println(s"user: ${su}")
  println(s"${su.name}-${su.username}-${su.password}-${su.pinCode}")
}
