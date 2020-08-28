/* Author:          Fazal Khan
   Github:          acse-fk4517
   Github Repo:     https://github.com/acse-2019/irp-acse-fk4517
*/

import com.jsuereth.sbtpgp.PgpKeys.gpgCommand
import sbt.url

name := "picta"

organization := "org.carbonateresearch"

version := "0.1.1"

//scalaVersion := "2.13.2"

// cross scala version with 2.12; give sequence of scala version
crossScalaVersions := Seq("2.12.12", "2.13.3")

// documentation output
target in Compile in doc := baseDirectory.value.getParentFile / "api"

// conus library
libraryDependencies += "org.carbonateresearch" %% "conus" % "0.2.3"

// Get scaladoc to add rootdoc.txt content to index.html
scalacOptions in (Compile, doc) ++= Seq("-doc-root-content", "rootdoc.txt")

// unit testing
libraryDependencies += "org.scalactic" %% "scalactic" % "3.1.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.2" % "test"

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

val username = "cedricmjohn"
val repo     = "picta"
//name := "picta"

inThisBuild(
  List(
    organization := "org.carbonateresearch",
    homepage := Some(url(s"https://github.com/$username/$repo")),
    licenses := List("GNU General Public License" -> url(s"https://github.com/$username/$repo/LICENSE")),
    scmInfo := Some(ScmInfo(url(s"https://github.com/$username/$repo-site"), s"scm:git:git@github.com:$username/$repo.git")),
    developers := List(
      Developer(
        id = "acse-fk4517",
        name = "Fazal Khan",
        email = "fazal.khan19@imperial.ac.uk",
        url = new URL(s"http://github.com/acse-fk4517")
      ),
      Developer(
        id = username,
        name = "Cedric John",
        email = "cedric.john@gmail.com",
        url = new URL(s"http://github.com/${username}")
      )
    )
  )
)

//Global / gpgCommand := (baseDirectory.value / "gpg.sh").getAbsolutePath

ThisBuild / description := "Picta: the interactive scientific plotting library for Scala."
ThisBuild / licenses := List("GNU General Public License" -> new URL("http://www.gnu.org/licenses/"))
ThisBuild / homepage := Some(url("https://github.com/cedricmjohn/picta"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true

publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)


/*
credentials += Credentials(
  "GnuPG Key ID",
  "gpg",
  "C2B1493A584970F254B26AC1174AF7302D6AD8DB", // key identifier
  "ignored" // this field is ignored; passwords are supplied by pinentry
)*/

publishTo := sonatypePublishToBundle.value


