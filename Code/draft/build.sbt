name := "untitled"

version := "0.1"

scalaVersion := "2.13.2"

libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.9.1"

val circeVersion = "0.12.3"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-literal"
).map(_ % circeVersion)