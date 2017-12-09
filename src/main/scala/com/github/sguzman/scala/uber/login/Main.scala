package com.github.sguzman.scala.uber.login

import org.feijoas.mango.common.base.Preconditions

import scala.util.{Failure, Success}
import scalaj.http.Http

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

      println(csrf)
    }) match {
      case Success(_) => println("Done")
      case Failure(e) => Console.err.println(e)
    }
  }
}
