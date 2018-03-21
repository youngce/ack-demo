name := "ack-demo"

version := "0.1"

scalaVersion := "2.12.4"
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.11"
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-persistence
libraryDependencies += "com.typesafe.akka" %% "akka-persistence" % "2.5.11"
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-persistence-cassandra
libraryDependencies += "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.83"
