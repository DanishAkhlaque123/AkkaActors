import akka.actor.SupervisorStrategy.Resume
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

import java.io._

object question extends App{


  class ActorWithWarn extends Actor with ActorLogging{
    override def receive: Receive = {
      case message => {

        val writer = new BufferedWriter(new FileWriter(new File("warn.log")))
        val towrite = log.info(message.toString)
        writer.write("MessageWarn")
        writer.close()
      }
    }
  }

  class ActorWithInfo extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => {
        val writer = new BufferedWriter(new FileWriter(new File("info.log")))

        val towrite = log.info(message.toString)
        writer.write("MessageInfo")
        writer.close()
      }
    }
  }



  val part1 = ConfigFactory.load("Application.conf")
  println(s"separate config log level: ${part1.getString("akka.loglevel")}")

  val part2 = ConfigFactory.load("internalresource/Application1.conf")
  println(s"separate config log level: ${part2.getString("akka.loglevel")}")


}
