name := "conusviz"

organization := "org.carbonateresearch"

version := "0.1"

scalaVersion := "2.13.2"

// unit testing
libraryDependencies += "org.scalactic" %% "scalactic" % "3.1.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.2" % "test"

// conus library
libraryDependencies  += "org.carbonateresearch" %% "conus" % "0.0.1b"

// JSON encoding / decoding
libraryDependencies += "com.lihaoyi" %% "upickle" % "1.1.0"
libraryDependencies += "com.lihaoyi" %% "ujson-circe" % "1.1.0"