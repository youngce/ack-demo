import akka.actor.{Actor, ActorLogging, ActorPath, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import scala.concurrent.duration._

object MessageDrivenApp extends App {
	val system=ActorSystem()



	var ping=system.actorOf(Props(classOf[PingActor]),"PingActor")
	var pong=system.actorOf(Props(classOf[PongActor]),"PongActor")
	//tell
	ping ! Message.Pong
	//ask
	implicit val timeout= akka.util.Timeout(3.seconds)
	implicit val ec=scala.concurrent.ExecutionContext.global
	(ping ? Message.Pong)
		.foreach(msg=>println(s"msg:$msg received !!"))

  //	start ping pong
	ping ! Message.Start







}
object Message{
	case object Ping
	case object Pong
	case object Start
}
class PingActor extends Actor with ActorLogging{
	var times=0
	override def receive: Receive = {
		case Message.Pong=>
			log.info(s"$times. ${self.path.name} receive Pong")
			if (times<10){
				sender() ! Message.Ping
			}

			times+=1
		case Message.Start=>
			context.actorSelection("../PongActor") ! Message.Ping

	}
}
class PongActor extends Actor with ActorLogging {

	override def receive: Receive = {

		case Message.Ping=>
			log.info(s"${self.path.name} receive Ping")
			sender() ! Message.Pong
	}
}
