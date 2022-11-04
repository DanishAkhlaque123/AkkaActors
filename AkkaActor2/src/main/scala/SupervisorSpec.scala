import akka.actor.SupervisorStrategy.Resume
import akka.actor.{Actor, ActorIdentity, ActorLogging, ActorRef, ActorSystem, Identify, OneForOneStrategy, Props, Terminated}
import com.typesafe.config.ConfigFactory

import java.io.{BufferedWriter, File, FileWriter, PrintWriter}

object SupervisorSpec extends App{
  case class CreateChild(value: String)
  case class tellchild(message: String)
  class Supervisor extends Actor with ActorLogging {

    override val supervisorStrategy =
      OneForOneStrategy() {
        case _: NullPointerException => Resume
        case _: Exception => Resume
      }


    override def receive: Receive = {

      case CreateChild(name: String) => {

        val ChildRef = context.actorOf(Props[ActorWithWarn2])

        ChildRef ! "I am a warning log"
      }
    }

  }


  class ActorWithWarn2 extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => {
        val pw = new PrintWriter(new File("warn1.txt"))
        val towrite = log.info(message.toString)
        pw.write("MessageWarn2 is the best")
        pw.close
        throw new NullPointerException("I am from Actor with warning")
      }
    }
  }

  class ActorWithInfo2 extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => {
        val pw = new PrintWriter(new File("info1.txt"))
        val towrite = log.info(message.toString)
        pw.write("MessageInfo1")
        pw.close
      }
    }
  }

  val system = ActorSystem("Demo")
  val parent = system.actorOf(Props[Supervisor])
  parent ! CreateChild("kid")

}
