resolvers ++= Seq(
  "Typesafe"           at "http://repo.typesafe.com/typesafe/releases/",
  "Oncue Bintray Repo" at "http://dl.bintray.com/oncue/releases"
)

addSbtPlugin("org.brianmckenna" % "sbt-wartremover" % "0.14")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")
addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.4.0")
