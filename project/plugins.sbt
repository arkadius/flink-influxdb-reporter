resolvers += Resolver.url(
  "bintray-sbt-plugin-releases",
  url("http://dl.bintray.com/banno/oss"))(
    Resolver.ivyStylePatterns)

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")

addSbtPlugin("com.banno" % "sbt-license-plugin" % "0.1.5")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.3")
