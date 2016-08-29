resolvers ++= Seq(
  "Typesafe"           at "http://repo.typesafe.com/typesafe/releases/",
  "Oncue Bintray Repo" at "http://dl.bintray.com/oncue/releases"
)

addSbtPlugin("org.brianmckenna" % "sbt-wartremover" % "0.14")
