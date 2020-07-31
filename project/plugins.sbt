val SONATYPE_VERSION = sys.env.getOrElse("SONATYPE_VERSION", "3.9.2")
addSbtPlugin("com.github.gseitz" % "sbt-release"  % "1.0.13")
addSbtPlugin("org.xerial.sbt"    % "sbt-sonatype" % SONATYPE_VERSION)
addSbtPlugin("com.jsuereth"      % "sbt-pgp"      % "2.0.1")
addSbtPlugin("org.scalameta"     % "sbt-scalafmt" % "2.4.0")

libraryDependencies += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value

resolvers += Resolver.sonatypeRepo("releases")