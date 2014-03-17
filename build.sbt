name := "insta-stijl"

version := "0.1"

scalaVersion := "2.10.2"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  "org.dupontmanual" %% "dm-image" % "0.1-SNAPSHOT"
)

scalacOptions ++= Seq("-deprecation", "-feature")
