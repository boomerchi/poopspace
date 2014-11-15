name := "poopspace"

version := "1.0.0-SNAPSHOT"

libraryDependencies ++= Seq(
	"com.typesafe.play" %% "play-slick" % "0.8.0",
	"com.h2database" % "h2" % "1.3.167",
	"org.scalatest" % "scalatest_2.10" % "2.2.1" % "test"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
