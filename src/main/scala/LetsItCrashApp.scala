import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
object LetsItCrashApp extends ActorSystemApp {
	val supervisor=system.actorOf(Props(classOf[Supervisor]),"supervisor")
	(supervisor ? Props(classOf[Child])).mapTo[ActorRef].foreach(child=>{
		child ! new ArithmeticException()
		child ! new NullPointerException()
		child ! new IllegalArgumentException()

	})



}
class Supervisor extends Actor with ActorLogging{
	import akka.actor.OneForOneStrategy
	import akka.actor.SupervisorStrategy._
	import scala.concurrent.duration._

	override val supervisorStrategy =
		OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
			case _: ArithmeticException      ⇒
				log.info("Resume")
				Resume
			case _: NullPointerException     ⇒
				log.info("Restart")
				Restart
			case _: IllegalArgumentException ⇒
				log.info("Stop")
				Stop
			case _: Exception                ⇒
				log.info("Escalate")
				Escalate
		}

	def receive = {
		case p: Props ⇒ sender() ! context.actorOf(p)
	}
}
class Child extends Actor {
	var state = 0
	def receive = {
		case ex: Exception ⇒ throw ex
		case x: Int        ⇒ state = x
		case "get"         ⇒ sender() ! state
	}
}
