libraryDependencies += "io.netty" % "netty" % "3.4.1.Final"

libraryDependencies += "org.scalatest" % "scalatest_2.9.0" % "1.6.1"

resolvers ++= Seq(
  "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases"  at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.9" % "test"
)

seq(Revolver.settings: _*)

javaOptions in run += "-server"

javaOptions in Revolver.reStart ++= Seq("-server")
