import akka.actor._
import scala.concurrent.duration._

sealed trait PiMessage
case object Calculate extends PiMessage
case class Work(start: Int, nrOfElements: Int) extends PiMessage
case class Result(value: Double) extends PiMessage
case class PiApproximation(pi: Double, duration: Duration)

object Pi extends App {
 
  calculate(nrOfWorkers = 4, nrOfElements = 10000, nrOfMessages = 10000)
 
  // actors and messages ...
 
  def calculate (nrOfWorkers: Int, nrOfElements: Int, nrOfMessages: Int) : Unit = {
    // Create an Akka system
    val system = ActorSystem("PiSystem")
 
    // create the result listener, which will print the result and shutdown the system
    val listener = system.actorOf(Props[Listener], name = "listener")
 
    // create the master
    val master = system.actorOf(Props(new Master(
      nrOfWorkers, nrOfMessages, nrOfElements, listener)),
      name = "master")
 
    // start the calculation
    master ! Calculate
 
  }
}