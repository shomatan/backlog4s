package com.nulabinc.backlog4s

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import cats.implicits._
import com.nulabinc.backlog4s.apis.UserApi
import com.nulabinc.backlog4s.datas.UserT
import dsl._

import scala.util.{Failure, Success}

object App {

  implicit val system = ActorSystem("test")
  implicit val mat = ActorMaterializer()
  implicit val exc = system.dispatcher

  def main(args: Array[String]): Unit = {
    val httpInterpret = new AkkaHttpInterpret(
      "https://nulab.backlog.jp/api/v2/", AccessKey(Key.accessKey)
    )

    val interpreter = httpInterpret

    val prg = for {
      file <- UserApi.downloadIcon(UserT.id(0))
      user <- UserApi.getById(UserT.myself)
      users <- UserApi.getAll(0, 1000)
    } yield Seq(file, user, users)

    prg.foldMap(interpreter).onComplete { result =>
      result match {
        case Success(data) => data.foreach(println)
        case Failure(ex) => ex.printStackTrace()
      }
      system.terminate()
    }
  }
}
