import Pig._
import akka.actor.{ActorLogging, Props}
import akka.persistence.PersistentActor

import scala.concurrent.duration._

object EventSourcingApp extends ActorSystemApp {
	def createPig=system.actorOf(Props(classOf[Pig]))
	val pig=createPig

//	pig ! Eat(10)
//	pig ! Eat(20)
//	pig ! Exercise(1.hours)
//	system.stop(pig)
//	Thread.sleep(1000)
//	val restart =



}
object Pig{
	trait Command
	case class Eat(weightOfFood:Int) extends Command
	case class Exercise(duration: Duration) extends Command
	trait Event
	case class Ate(weightOfFood:Int) extends Event
	case class Exercised(lossWeight:Int) extends Event

}
class Pig extends PersistentActor with ActorLogging{
	var weight:Int=0
	def handleEvent(evt:Event)={
		evt match {
			case Ate(weightOfFood)=>
				weight+=weightOfFood
			case Exercised(lossWeight)=>
				weight-=lossWeight
		}
		log.info(s"weight: $weight is changed by $evt")
	}
	override def receiveRecover: Receive = {
		case evt:Event=>
			log.info(s"Recovering by $evt")
			handleEvent(evt)
	}

	override def receiveCommand: Receive = {
		case Eat(weightOfFood)=>
			persist(Ate(weightOfFood))(evt=>handleEvent(evt))
		case Exercise(duration)=>
			val lossWeight=duration.toMinutes.toInt/60
			if (lossWeight<weight)
				persist(Exercised(lossWeight))(evt=>handleEvent(evt))
	}

	override def persistenceId: String = "pig"
}
