#!/bin/bash
set -x

export BASEDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

HADOOP_CLASSPATH=$(hadoop classpath)
HBASE_CLASSPATH=$(hbase classpath)
HIVE_CLASSPATH=/opt/hive/lib/*

export SPARK_DIST_CLASSPATH=$HIVE_CLASSPATH:$HBASE_CLASSPATH:$HADOOP_CLASSPATH

time spark-submit \
  --driver-memory 2G \
  --executor-memory 1G \
  --executor-cores 1 \
  --driver-class-path "/opt/hbase/conf" \
  --conf spark.sql.hive.metastore.version=2.3 \
  --conf spark.sql.hive.metastore.jars=$HIVE_HOME"/lib/*" \
  --class br.com.company.playgroundsparkhadoop.service.PlayGroundSparkHadoopImportData PlayGroundSparkHadoop-assembly-0.1.jar \

retCode=$?

exit $retCode
