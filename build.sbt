import com.banno.license.Licenses._
import com.banno.license.Plugin.LicenseKeys._
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

val scalaV = "2.11.8"

organization  := "com.github.arkadius"
name := s"flink-influxdb-reporter"

licenseSettings
license := apache2("Copyright 2015 the original author or authors.")
licenses :=  Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
removeExistingHeaderBlock := true

scalaVersion  := scalaV
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

val flinkV = "1.1.2"

libraryDependencies ++= {
  Seq(
    "org.apache.flink"  % "flink-metrics-core"       % flinkV,
    "org.apache.flink"  % "flink-metrics-dropwizard" % flinkV,
    "com.github.davidb" % "metrics-influxdb"         % "0.8.2"
  )
}

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  setNextVersion,
  commitNextVersion,
  pushChanges
)
