package br.com.company.playgroundsparkhadoop.service

import org.apache.spark.sql.SparkSession

object PlayGroundSparkHadoopImportData {

  def main(args : Array[String]) {
    val spark : SparkSession = SparkSession
      .builder()
      .appName(s"play-ground-spark-hadoop")
      .config("hive.exec.dynamic.partition.mode","nonstrict")
      .config("hive.exec.dynamic.partition","true")
      .enableHiveSupport()
      .getOrCreate()

    val applicationId : String = spark.sparkContext.applicationId
    val applicationName : String = spark.sparkContext.appName

    println(s"**********************************************************************************")
    println(s"*** Application ID: $applicationId ")
    println(s"*** Application Name: $applicationName ")
    println(s"**********************************************************************************")

    val datasetEmployee = spark.sql("select * from company.employee_hive")

    println("Quantidade de Empregados pela Tabela Hive: " + datasetEmployee.count)

    datasetEmployee.show(10, false)

    val datasetEmployeeHB = spark.sql(" select * from company.employee")

    println("Quantidade de Empregados pela Tabela HBase: " + datasetEmployeeHB.count)

    datasetEmployeeHB.show(10, false)

    spark.stop()
  }
}
