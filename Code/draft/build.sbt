name := "conusviz"

version := "0.1"

scalaVersion := "2.13.2"

// unit testing
libraryDependencies += "org.scalactic" %% "scalactic" % "3.1.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.2" % "test"

// html generation
libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.9.1"

val circeVersion = "0.12.3"

// JSON encoding / decoding
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-literal"
).map(_ % circeVersion)