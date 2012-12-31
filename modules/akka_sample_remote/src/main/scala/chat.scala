package jp.enrapt

import akka.actor._
import com.typesafe.config.{ConfigValue, ConfigFactory}

/**
 * Created with IntelliJ IDEA.
 * User: yutaka
 * Date: 2012/12/31
 * Time: 17:39
 * To change this template use File | Settings | File Templates.
 */
object chat extends App {

  case class ConnectMsg(msg: String) extends Serializable

  case class DisconnectMsg(msg: String) extends Serializable

  case class SendMsg(msg: String) extends Serializable

  case class ResponseMsg(msg: String) extends Serializable

  class ChatActor extends Actor with ActorLogging {
    def receive: ChatActor#Receive = {
      case (actor: ActorRef, op) ⇒ {
        println(actor, op);
        actor ! op
      }
      case ConnectMsg(msg) => {
        log.info("ConnectMsg msg[%s]".format(msg))
        sender ! ResponseMsg("connected")
      }
      case DisconnectMsg(msg) => {
        log.info("DisconnectMsg msg[%s]".format(msg))
      }
      case SendMsg(msg) => {
        log.info("SendMsg msg[%s]".format(msg))
        sender ! ResponseMsg(msg)
        self ! ResponseMsg(msg)
      }
      case ResponseMsg(msg) => {
        log.info("> msg[%s]".format(msg))
      }
      case o: Object => {
        log.warning("not treated[%s]".format(o))
      }
    }
  }

  val host_system = ActorSystem("chatApplication",
    ConfigFactory.parseString( """{
      akka.actor.provider="akka.remote.RemoteActorRefProvider"
      akka.remote.netty.port=2552
                              }""")
  )
  val hostActor = host_system.actorOf(Props[ChatActor], "chat")
  //hostActor ! SendMsg("send from host actor")

  val client_system = ActorSystem("chatApplication",
    ConfigFactory.parseString( """{
      akka.actor.provider="akka.remote.RemoteActorRefProvider"
      akka.remote.netty.port=2553
                              }""")
  )
  val client = client_system.actorOf(Props[ChatActor], "lookupActor")
  val host_ref = client_system.actorFor("akka://chatApplication@192.168.1.10:2552/user/chat")

  host_ref ! ConnectMsg("send from client actor")
  client !(host_ref, SendMsg("send from client actor"))

  Thread.sleep(1000)

  client_system.shutdown()
  host_system.shutdown()

}
