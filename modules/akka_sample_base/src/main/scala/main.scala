package jp.enrapt

import akka.actor.{Props, ActorSystem, Actor, ActorLogging}

/**
 * Created with IntelliJ IDEA.
 * User: yutaka
 * Date: 2012/12/31
 * Time: 17:39
 * To change this template use File | Settings | File Templates.
 */
object main extends App {

  case class TestMsg(msg: String) extends Serializable

  class TestActor extends Actor with ActorLogging {
    def receive: TestActor#Receive = {
      case TestMsg(msg) => log.info("recieving msg[%s]".format(msg))
      case o: Object => {
        log.warning("not treated[%s]".format(o))
      }
    }
  }

  val system = ActorSystem("MySystem")
  val tester = system.actorOf(Props[TestActor])

  tester ! TestMsg("test msg Hello!")
  tester ! "sample msg"

  system.shutdown()

}
