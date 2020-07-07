package com.example.kedro

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source

import scala.concurrent.Await
import scala.language.postfixOps

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{ Failure, Success }

object KClient {
  def main(args: Array[String]): Unit = {
    // Boot akka
    implicit val sys: ActorSystem = ActorSystem("KClient")
    implicit val mat = ActorMaterializer()
    implicit val ec = sys.dispatcher

    // Or via application.conf:
    val clientSettings = GrpcClientSettings.fromConfig("kedro.KedroService")

    // Create a client-side stub for the service
    val client = KedroClient(clientSettings)

    def getPipelines: PipelineSummary = {
      sys.log.info("Listing pipelines")
      val reply = client.listPipelines(PipelineParams())
      Await.result(reply, 2 seconds)
    }

    def streamingStatus(runId: String): Unit = {
      sys.log.info("Listing pipelines")
      val responseStream: Source[RunStatus, NotUsed] = client.status(RunId(runId))
      val done: Future[Done] = responseStream.runForeach(reply => println(reply.events))
      done.onComplete {
        case Success(_) =>
          println("Finished")
        case Failure(e) =>
          println(e)
      }

    }

    def createRun() = {
      val reply = client.run(RunParams())
      Await.result(reply, 2 seconds)
    }
    val pipelines = getPipelines
    println(pipelines.pipeline)
    println(streamingStatus(""))
    val run = createRun()
    println(run.runId)
    println(streamingStatus(run.runId))

  }
}

