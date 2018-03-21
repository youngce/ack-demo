import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.duration._
trait ActorSystemApp extends App{

	implicit val system=ActorSystem()
	implicit val timeout= akka.util.Timeout(3.seconds)
	implicit val ec=scala.concurrent.ExecutionContext.global
	implicit val mat=ActorMaterializer()
}
