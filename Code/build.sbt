name := "picta"

organization := "org.carbonateresearch"

version := "0.1"

scalaVersion := "2.13.2"

// documentation output
target in Compile in doc := baseDirectory.value.getParentFile / "docs"

// Get scaladoc to add rootdoc.txt content to index.html
scalacOptions in (Compile,doc) ++= Seq("-doc-root-content", "rootdoc.txt")

// unit testing
libraryDependencies += "org.scalactic" %% "scalactic" % "3.1.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.2" % "test"

// conus library
libraryDependencies  += "org.carbonateresearch" %% "conus" % "0.2.0"

// JSON encoding / decoding
libraryDependencies += "com.lihaoyi" %% "upickle" % "1.1.0"

// Jupter Kernels
resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies += "com.github.jupyter" % "jvm-repr" % "0.4.0"
libraryDependencies += "sh.almond" %% "interpreter-api" % "0.10.0"
libraryDependencies += "sh.almond" %% "jupyter-api" % "0.10.0"

// OS interface
libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.7.0"

// CSV file
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.6"