import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import question3.person.livethelife


object question3 extends App{
  object bankaccount{
    case class deposite(amount: Int)
    case class withdraw(amount: Int)
    case object Statement

    case class TransactionFailure(message: String)
    case class TransactionSuccess(reason: String)
  }

  class bankaccount extends Actor{
    import bankaccount._
    var money=0

    override def receive: Receive = {
      case deposite(amount) =>
        if(amount<0) sender() ! TransactionFailure("invalid deposite amount")
        else
          {
            money += amount
            sender() ! TransactionSuccess(s"successfully deposited $amount")
          }
      case withdraw(amount) =>
        if(amount<0) sender() ! TransactionFailure("invalid withdraw amount")
        else if(amount>money) sender() ! TransactionFailure("insufficient funds")
        else{
          money -=amount
          sender() ! TransactionSuccess(s"successfully withdraw $amount")
        }

      case Statement => sender() ! s"your balance is $money"

    }
  }

  object person{
    case class livethelife(account: ActorRef)
  }

  class person extends Actor{
    import person._
    import bankaccount._

    override def receive: Receive = {
      case livethelife(account) =>
        account ! deposite(100)
        account ! withdraw(200)
        account ! withdraw(50)
        account ! Statement
      case message => println(message.toString)
    }
  }
  val System = ActorSystem("BankAccount")
  val account = System.actorOf(Props[bankaccount],"bankaccount")
  val Person = System.actorOf(Props[person],"billion")
  Person ! livethelife(account)

}
