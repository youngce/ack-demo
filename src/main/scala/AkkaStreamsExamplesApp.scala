import akka.actor.Actor
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Sink, Source}

object AkkaStreamsExamplesApp extends ActorSystemApp {

	Source.single(1)
		.map(_.toString)
		.runWith({
			println("Single Source -> Sink.head")
			Sink.head
		})
		.foreach(println)


	Source(1 to 10)
		.filter(_%2==0)
		.map(_.toString)
		.runWith({
			println("Seq Source -> Sink.seq")
			Sink.seq
		})
		.foreach(println)

	val actor=Source.actorRef[Int](Int.MaxValue,OverflowStrategy.dropHead)
		.map(_ * 10)
		.to({
			println("Actor Source -> Sink.seq")
			Sink.foreach(println)	}
		)
		.run()

	(1 to 5).foreach(actor ! _)
}


