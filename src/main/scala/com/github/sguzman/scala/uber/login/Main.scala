package com.github.sguzman.scala.uber.login

import com.github.sguzman.scala.uber.login.typesafe.email.input.{Answer, Email, UserIdentifier}
import com.github.sguzman.scala.uber.login.typesafe.email.output.EmailResponse
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser.decode
import org.feijoas.mango.common.base.Preconditions

import scala.util.{Failure, Success}
import scalaj.http.{Http, HttpResponse}

object Main {
  def main(args: Array[String]): Unit = {
    util.Try({
      val (user, pass) = getCreds
      val responseLoginPage = getLoginPage

      val responsePostEmail = postEmail(responseLoginPage, user)
      println(responsePostEmail.body)
      println(decode[EmailResponse](responsePostEmail.body))
    }) match {
      case Success(_) => println("Done")
      case Failure(e) => Console.err.println(e)
    }
  }

  def getLoginPage: HttpResponse[String] =
    Http("https://auth.uber.com/login/?next_url=https%3A%2F%2Fpartners.uber.com")
      .asString

  def getCreds: (String, String) = {
    val user = System.getenv("USERNAME")
    val pass = System.getenv("PASSWORD")

    Preconditions.checkNotNull(user)
    Preconditions.checkNotNull(pass)
    (user, pass)
  }

  def postEmail(response: HttpResponse[String], user: String): HttpResponse[String] = {
    val postURL = "https://auth.uber.com/login/handleanswer"
    val payload = Email(Answer(`type` = "VERIFY_INPUT_USERNAME", UserIdentifier(user)),  init = true)

    val emailBody = payload.asJson.toString
    val requestEmail = Http(postURL)
      .postData(emailBody)
      .header("Cookie", response.cookies.mkString("; "))
      .header("x-csrf-token", response.header("x-csrf-token").get)
      .header("Content-Type", "application/json")
    val responseEmail =  requestEmail.asString
    responseEmail
  }
}
