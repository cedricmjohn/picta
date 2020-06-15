name := "conusviz"

organization := "org.carbonateresearch"

version := "0.1"

scalaVersion := "2.13.2"

// unit testing
libraryDependencies += "org.scalactic" %% "scalactic" % "3.1.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.2" % "test"

// conus library
libraryDependencies  += "org.carbonateresearch" %% "conus" % "0.0.1b"

// html generation
libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.9.1"

// plotly library
libraryDependencies += "org.plotly-scala" %% "plotly-core" % "0.7.6"
libraryDependencies += "org.plotly-scala" %% "plotly-render" % "0.7.6"

// JSON encoding / decoding
libraryDependencies += "com.lihaoyi" %% "upickle" % "1.1.0"
libraryDependencies += "com.lihaoyi" %% "ujson-circe" % "1.1.0"