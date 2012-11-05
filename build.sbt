name := "GoL"

libraryDependencies ++= Seq("org.specs2" %% "specs2" % "1.7.1" % "test",
      "org.scala-lang" % "scala-swing" % "2.9.1",
      "org.easytesting" % "fest-swing" % "1.2.1")

resolvers ++= Seq("releases"  at "http://oss.sonatype.org/content/repositories/releases")

fork in run := true

fork in test := true