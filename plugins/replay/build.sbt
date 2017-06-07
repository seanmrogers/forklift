organization := "com.github.dcshock"

name := "forklift-replay"

version := "2.0"

javacOptions ++= Seq("-source", "1.8")

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "1.8")
    sys.error("Java 8 is required for this project.")
}

resolvers ++= Seq(
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "Maven Central" at "http://repo1.maven.org/maven2",
    "Fuse Snapshots" at "http://repo.fusesource.com/nexus/content/repositories/snapshots",
    "Fuse" at "http://repo.fusesource.com/nexus/content/groups/public"
)

libraryDependencies ++= Seq(
  "com.github.dcshock" % "forklift" % "2.0",
  "io.searchbox" % "jest" % "2.4.0",
  "org.elasticsearch" % "elasticsearch" % "2.4.1",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.3",
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.7.3"
)

lazy val testDependencies = Seq(
  "com.novocode" % "junit-interface" % "0.11",
  "org.mockito" % "mockito-all" % "1.9.5"
)

libraryDependencies ++= testDependencies.map(_ % "test")

// Remove scala dependency for pure Java libraries
autoScalaLibrary := false

// Remove the scala version from the generated/published artifact
crossPaths := false

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

// Remove scala dependency for pure Java libraries
autoScalaLibrary := false

// Remove the scala version from the generated/published artifact
crossPaths := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/dcshock/forklift</url>
  <licenses>
    <license>
      <name>BSD-style</name>
      <url>http://www.opensource.org/licenses/bsd-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:dcshock/forklift.git</url>
    <connection>scm:git:git@github.com:dcshock/forklift.git</connection>
  </scm>
  <developers>
    <developer>
      <id>dcshock</id>
      <name>Matt Conroy</name>
      <url>http://www.mattconroy.com</url>
    </developer>
  </developers>)
