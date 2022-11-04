import Question4Spec.{bankaccount, deposite, withdraw}
import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.TestActor.NullMessage.sender
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike
//import .bankaccount
//import .bankaccount.{Statement, deposite, withdraw}

class Question4Spec extends TestKit(ActorSystem("Test"))
  with ImplicitSender
  with AnyWordSpecLike
  with BeforeAndAfterAll {
  override def afterAll(): Unit = {

    TestKit.shutdownActorSystem(system)
  }


  "A person" should {
    "deposite money" in {
      val system = ActorSystem("BankActor")
      val BankActor = system.actorOf(Props[bankaccount], "bankActor")
      BankActor ! deposite(100)
      expectMsg("successfully deposited 100")

      //val Person = system.actorOf(Props[person], "billion")

    }
  }

  "A person" should {
    "withdraw money" in {
      val system = ActorSystem("BankActor")
      val BankActor = system.actorOf(Props[bankaccount], "bankActor")
      BankActor ! withdraw(200)
      expectMsg("insufficient funds")

      //val Person = system.actorOf(Props[person], "billion")

    }
  }


}


object Question4Spec{

  case class deposite(amount: Int)

  case class withdraw(amount: Int)

  case object Statement

  class bankaccount extends Actor {

    var money = 0

    override def receive: Receive = {
      case deposite(amount) =>
        if (amount < 0) sender() ! "invalid deposite amount"
        else {
          money += amount
          sender() ! s"successfully deposited $amount"
        }
      case withdraw(amount) =>
        if (amount < 0) sender() ! "invalid withdraw amount"
        else if (amount > money) sender() ! "insufficient funds"
        else {
          money -= amount
          sender() ! s"successfully withdraw $amount"
        }

      case Statement => sender() ! s"your balance is $money"

    }
  }


}
