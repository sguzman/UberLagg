package com.github.sguzman.scala.uber.login

import com.github.sguzman.scala.uber.login.typesafe.email.{Answer, Email, UserIdentifier}
import org.feijoas.mango.common.base.Preconditions

import scala.util.{Failure, Success}
import scalaj.http.Http

import io.circe.syntax._
import io.circe.generic.auto._

object Main {
  def main(args: Array[String]): Unit = {
    util.Try({
      val user = System.getenv("USERNAME")
      val pass = System.getenv("PASSWORD")

      Preconditions.checkNotNull(user)
      Preconditions.checkNotNull(pass)

      val loginPageURL = "https://auth.uber.com/login/?next_url=https%3A%2F%2Fpartners.uber.com"
      val request = Http(loginPageURL)
      val response = request.asString
      val csrf = response.header("x-csrf-token").get

      val postURL = "https://auth.uber.com/login/handleanswer"
      val payload = Email(Answer(`type` = "VERIFY_INPUT_USERNAME", UserIdentifier(email = user)),  init = true)

      val emailBody = payload.asJson.toString
      val requestEmail = Http(postURL)
        .postData(emailBody)
        .header("x-csrf-token", csrf)
        .header("Content-Type", "application/json")
        .header("Content-Type", response.headers("Set-Cookie").mkString("; "))
      val responseEmail =  requestEmail.asString
      val body = responseEmail.body
      println(body)
    }) match {
      case Success(_) => println("Done")
      case Failure(e) => Console.err.println(e)
    }
  }
}
