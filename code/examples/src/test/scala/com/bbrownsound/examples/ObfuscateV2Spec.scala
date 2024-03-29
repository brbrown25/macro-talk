package com.bbrownsound.examples

import com.bbrownsound.macros.ToStringObfuscateV2Impl
import java.util.UUID
import org.scalatest._
import scala.util.Random

class ObfuscateV2Spec extends WordSpec with Matchers {

  def generateUUID = UUID.randomUUID().toString

  "`toString` method on TestObfuscatePassword case class obfuscates password and pinCode fields" in {
    val name = generateUUID
    val username = generateUUID
    val password = generateUUID
    val pinCode = generateUUID

    val obfuscatedInstance = Obfuscated.TestPassword(
      name = name,
      username = username,
      password = password,
      pinCode = pinCode
    )

    val nonObfuscatedInstance = NonObfuscated.TestPassword(
      name = name,
      username = username,
      password = ToStringObfuscateV2Impl.obfuscateValue(password),
      pinCode = ToStringObfuscateV2Impl.obfuscateValue(pinCode)
    )

    val obfuscatedToString = obfuscatedInstance.toString
    val nonObfuscatedToString = nonObfuscatedInstance.toString
    obfuscatedToString shouldEqual nonObfuscatedToString
  }

  "`toString` method on TestObfuscateCreditCard case class obfuscates the cardNumber field" in {
    val cardNumber = generateUUID
    val cvv = Random.nextInt(1000)
    val endDate = generateUUID

    val obfuscatedInstance = Obfuscated.TestCreditCard(
      cardNumber = cardNumber,
      cvv = cvv,
      endDate = endDate
    )

    val nonObfuscatedInstance = NonObfuscated.TestCreditCard(
      cardNumber = ToStringObfuscateV2Impl.obfuscateValue(cardNumber),
      cvv = cvv,
      endDate = endDate
    )

    val obfuscatedToString = obfuscatedInstance.toString
    val nonObfuscatedToString = nonObfuscatedInstance.toString
    obfuscatedToString shouldEqual nonObfuscatedToString
  }

  "`toString` method in identical case classes are different when one of them obfuscate one of his fields" in {
    val username = generateUUID
    val password = generateUUID

    val obfuscatedInstance = Obfuscated.UserPassword(
      username = username,
      password = password
    )

    val nonObfuscatedInstance = NonObfuscated.UserPassword(
      username = username,
      password = password
    )
    (obfuscatedInstance.toString should not).equal(nonObfuscatedInstance.toString)
  }

  "`toString` method should obfuscate non string fields" in {
    val username = generateUUID
    val password = generateUUID
    val obfuscatedInstance = Obfuscated.TestUser(
      username = username,
      password = password,
      pinCode = 1234
    )

    val nonObfuscatedInstance = NonObfuscated.TestUser(
      username = username,
      password = password,
      pinCode = 1234
    )

    obfuscatedInstance.toString shouldEqual s"TestUser(${username},${ToStringObfuscateV2Impl.obfuscateValue(password)},****)"
    (obfuscatedInstance.toString should not).equal(nonObfuscatedInstance.toString)
  }

  "`toString` should work with nested case classes" in {
    val username = generateUUID
    val password = generateUUID
    val obfuscatedInstance = Obfuscated.NestedExample(
      Obfuscated.TestUser(
        username = username,
        password = password,
        pinCode = 1234
      )
    )

    val nonObfuscatedInstance = NonObfuscated.NestedExample(
      NonObfuscated.TestUser(
        username = username,
        password = password,
        pinCode = 1234
      )
    )

    obfuscatedInstance.toString shouldEqual s"NestedExample(TestUser(${username},${ToStringObfuscateV2Impl.obfuscateValue(password)},****))"
    (obfuscatedInstance.toString should not).equal(nonObfuscatedInstance.toString)
  }
}
