package com.github.sguzman.scala.uber.login

import scala.util.{Failure, Success}

object Main {
  def main(args: Array[String]): Unit = {
    util.Try({
      println("Hello world")
    }) match {
      case Success(_) => println("Done")
      case Failure(e) => Console.err.println(e)
    }
  }
}
