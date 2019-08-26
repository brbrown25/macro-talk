//package com.bbrownsound.demo
//
//import com.bbrownsound.macros._
//
//object FailToCompile extends App {
//  def testMethod[String]: Double = {
//    val x = 2.0 + 2.0
//    Math.pow(x, x)
//  }
//
//  @ProcessTimer
//  testMethod
//  @ProcessTimer
//  case class FooBar(iterations: Long) {
//    @ProcessTimer
//    def testing: Double = {
//      {for (i <- 0 until iterations.toInt) yield Math.pow(i, i)}.sum
//    }
//  }
//
//  FooBar(100).testing
//}
