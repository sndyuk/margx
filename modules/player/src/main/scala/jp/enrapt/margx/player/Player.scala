package jp.enrapt.margx.player

import akka.actor._
import scala.util.{Success, Failure}
import scala.concurrent._
import java.util.concurrent.{ Executors, ExecutorService, Executor }

/**
 * プレイヤー
 */
object Player {

  val es = Executors.newFixedThreadPool(2)
  implicit val ec = ExecutionContext.fromExecutorService(es)
}

class Player extends Actor {

  import Player._

  def expensiveCalculation(): Int = {
    println("expensiveCalculation()")
    1
  }

  def expensiveCalculation(sender: ActorRef): Int = {
    println("expensiveCalculation()" + sender)
    2
  }

  def doSomethingOnSuccess(i: Int) {

  }

  def doSomethingOnFailure(err: Throwable) {

  }

  def receive = {

    case _ =>
      // Completely safe, "self" is OK to close over
      // and it’s an ActorRef, which is thread-safe
      Future { expensiveCalculation() }  onComplete {
        case Success(result)  ⇒ doSomethingOnSuccess(result)
        case Failure(failure) ⇒ doSomethingOnFailure(failure)
      }

      // Completely safe, we close over a fixed value
      // and it’s an ActorRef, which is thread-safe
      val currentSender = sender
      Future { expensiveCalculation(currentSender) }

  }

}
