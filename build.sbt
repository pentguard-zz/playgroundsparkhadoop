name := "PlayGroundSparkHadoop"
version := "0.1"
scalaVersion := "2.11.12"

val sparkVersion = "2.4.3"
val hbaseVersion = "2.2.0"
val hadoopVersion = "3.2.0"
val sftpVersion = "1.1.3"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core"    % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql"     % sparkVersion % "provided",
  "org.apache.spark" %% "spark-hive"    % sparkVersion % "provided",
  "com.springml" % "spark-sftp_2.11"    % sftpVersion,
  "org.apache.hadoop" % "hadoop-common" % hadoopVersion % "provided",
  "org.apache.hbase" % "hbase-client"   % hbaseVersion % "provided",
  "org.apache.hbase" % "hbase-server"   % hbaseVersion % "provided",
  "org.apache.hbase" % "hbase-protocol" % hbaseVersion % "provided",
  "org.apache.hbase" % "hbase-common"   % hbaseVersion % "provided"
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) =>
    xs map {_.toLowerCase} match {
      case "manifest.mf" :: Nil | "index.list" :: Nil | "dependencies" :: Nil =>
        MergeStrategy.discard
      case ps @ x :: xs if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
        MergeStrategy.discard
      case "services" :: _ =>  MergeStrategy.filterDistinctLines
      case _ => MergeStrategy.first
    }
  case _ => MergeStrategy.first
}

resolvers ++= Seq(
  "spark-core" at "https://mvnrepository.com/artifact/org.apache.spark/spark-core",
  "spark-sql" at "https://mvnrepository.com/artifact/org.apache.spark/spark-sql",
  "spark-sftp" at "https://mvnrepository.com/artifact/com.springml/spark-sftp",
  "hbase-client" at "https://mvnrepository.com/artifact/org.apache.hbase/hbase-client",
  "hbase-server" at "https://mvnrepository.com/artifact/org.apache.hbase/hbase-server",
  "hbase-protocol" at "https://mvnrepository.com/artifact/org.apache.hbase/hbase-protocol",
  "hbase-common" at "https://mvnrepository.com/artifact/org.apache.hbase/hbase-common"
)