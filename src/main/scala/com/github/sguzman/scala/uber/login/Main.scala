package com.github.sguzman.scala.uber.login

import org.feijoas.mango.common.base.Preconditions

import scala.util.{Failure, Success}

object Main {
  def main(args: Array[String]): Unit = {
    util.Try({
      val user = System.getenv("USERNAME")
      val pass = System.getenv("PASSWORD")

      Preconditions.checkNotNull(user)
      Preconditions.checkNotNull(pass)

      println("hello world")
    }) match {
      case Success(_) => println("Done")
      case Failure(e) => Console.err.println(e)
    }
  }
}
