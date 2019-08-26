package com.bbrownsound.demo

import com.bbrownsound.macros._

object ProcessTimerDemo extends App {
  @ProcessTimer
  def testMethod[String]: Double = {
//  def testMethod[String] = {
    val x = 2.0 + 2.0
    Math.pow(x, x)
  }

  @ProcessTimer
  def methodWithArguments(a: Double, b: Double): Boolean = {
//  def methodWithArguments(a: Double, b: Double) = {
    val c = Math.pow(a, b)
    c > a + b
  }

  @ProcessTimer
  def longRunning(n: Int): Seq[Double] = {
//  def longRunning(n: Int) = {
    for (i <- 0 until n) yield Math.pow(i, i)
  }

  testMethod
  methodWithArguments(1.0, 5.0)
  longRunning(10)
  longRunning(100)
  longRunning(1000)
}
