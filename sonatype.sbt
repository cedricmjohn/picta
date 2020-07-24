import xerial.sbt.Sonatype._

publishMavenStyle := true

sonatypeProfileName := "org.carbonateresearch"
sonatypeProjectHosting := Some(GitHubHosting(user="cedricmjohn", repository="picta", email="cedric.john@gmail.com"))
developers := List(
  Developer(
    id = "acse-fk4517",
    name = "Fazal Khan",
    email = "fazal.khan19@imperial.ac.uk",
    url = new URL(s"http://github.com/acse-fk4517")
  ),
  Developer(id = "cedric", name = "Cedric M. John", email = "cedric.john@gmail.com", url = url("http://www.carbonateresearch.org"))
)
licenses := Seq("GNU GPL 3.0" -> url("https://www.gnu.org/licenses/gpl-3.0.en.html"))

publishTo := sonatypePublishToBundle.value
