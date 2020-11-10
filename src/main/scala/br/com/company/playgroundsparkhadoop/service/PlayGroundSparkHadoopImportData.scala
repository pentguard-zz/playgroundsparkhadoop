package br.com.company.playgroundsparkhadoop.service

import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.storage.StorageLevel

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

    //datasetEmployee
      //.cache
      //.persist(StorageLevel.MEMORY_AND_DISK_2)

    datasetEmployee.show(10, false)

    val datasetEmployeeHB = spark.sql(" select * from company.employee")

    //datasetEmployeeHB
      //.persist(StorageLevel.MEMORY_AND_DISK_2)

    println("Quantidade de Empregados pela Tabela HBase: " + datasetEmployeeHB.count)

    datasetEmployeeHB.show(10, false)

    val datasetSalaries = spark.sql("select emp_no, salary, from_date, to_date, substr(from_date,1,4) ano, substr(from_date,6,2) mes from company.salaries")

    //datasetSalaries
      //.persist(StorageLevel.MEMORY_AND_DISK_2)

    println("Quantidade Salarios pela Tabela do Hive: " + datasetSalaries.count)

    //Todos os codigos de funcionarios utilizados
    val dfFilterEmployee = datasetSalaries.select("emp_no").distinct

    //datasetEmployeeHB.filter(col("emp_no").isin(dfFilterEmployee.select("emp_no")))
    //100GB/2000=X >2GB particao

    val datasetTitles = spark.sql("select * from company.titles")

    println("Quantidade Cargos dos Funcionarios pela Tabela do Hive: " + datasetTitles.count)

    val datasetFinal = datasetEmployeeHB
      .join(datasetSalaries,
        datasetEmployeeHB.col("emp_no").equalTo(datasetSalaries.col("emp_no")),joinType = "left")
      .join(datasetTitles,
        datasetEmployeeHB.col("emp_no").equalTo(datasetTitles.col("emp_no")),joinType = "left")
      .select(
        datasetEmployeeHB.col("emp_no"),
        datasetTitles.col("title"),
        datasetSalaries.col("salary"),
        datasetSalaries.col("to_date"),
        datasetSalaries.col("ano"),
        datasetSalaries.col("mes")
      )
      .filter(datasetEmployeeHB.col("emp_no").isNotNull)

    println("Quantidade Salarios pela Tabela do Hive: " + datasetFinal.count)

    datasetFinal.show(50,false)

    datasetFinal.write.mode(SaveMode.Overwrite).format("hive").partitionBy("ano", "mes").saveAsTable("company.salariesByYearMonth")

    //datasetFinal.write.mode(SaveMode.Append).format("hive").saveAsTable("company.analise_employee_titles")
/*
    println("Iniciando a gravacao no hive")

    datasetEmployee.write.mode(SaveMode.Append).format("hive").saveAsTable("company.employee_gravacao")

    println("Gerando o arquivo")

    datasetEmployee
      .coalesce(1)
      .write
      .format("csv")
      .option("sep", "|")
      .option("header","true")
      .mode("overwrite")
      .save("/user/hadoop/")
*/
    spark.stop()
  }
}
