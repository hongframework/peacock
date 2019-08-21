package com.hframework.smartsql

import org.apache.spark.sql.catalyst.expressions.UnsafeRow
import org.apache.spark.sql.types.{IntegerType, StringType, LongType}

/**
 * Created by zhangquanhong on 2017/8/31.
 */
object SmartSqlClient{

  val defaultManager = SmartSchemaManager.defaultManager

  {
    defaultManager.registerDatabase("mysql", new Mysql("127.0.0.1", 3306, "peacock", "peacock","peacock"))
    defaultManager.registerDatabase("redis", Redis("127.0.0.1",7011,"beetlefe@password"))
    defaultManager.registerDatabase("hbase", HBase("zqh,wzk,zzy",2181))

    defaultManager.registerTable("mysql", new Table("test_user",
      Column("id",LongType)
        ::Column("name",StringType)
        ::Column("age",IntegerType)
        ::Column("six",IntegerType)
        :: Nil))

    defaultManager.registerTable("redis", RTable("lcs_hour_all",
      RDb(Seq(2)),
      Key().prechars("lcs_hour_all").column("node").dateTimeYYYYMMDD("datetime").joinwith("_"),
      (List.tabulate(23)(hour => RedisMapItem(String.valueOf(hour + 1), StringType)))
        ::: List.tabulate(23)(hour => RedisMapItem("org_" + String.valueOf(hour + 1), StringType))))

    defaultManager.registerTable("hbase",HTable("hamster_statistics_second",
      Key().column("node").underline().column("specail").underline().dateTime("datetime"),
      List.tabulate(59)(second => HBaseQualifier("items", String.valueOf(second),StringType))))

    defaultManager.registerTable("hbase",HTable("hbase_data_profile_user",
      Key().sha256("userId").column("userId").joinwith("_"),
      HBaseQualifier("data", "maxDayInvestingAmountOfSixMonth", LongType) :: HBaseQualifier("data", "value-0", LongType) :: List.tabulate(10)(value => HBaseQualifier("data", "value-" + String.valueOf(value+1),StringType))))
    println(defaultManager.catalog)
  }

  var cacheSqlPlan = Map[String, SmartPlanManager]()

  def queryList(sql: String, parameter: Map[String, Any]): Array[Seq[Any]] = {
    val cacheKey = sql.replaceAll("\\w+","")
    val plan = if(cacheSqlPlan.contains(cacheKey)) {
      cacheSqlPlan.get(cacheKey).get.executedPlan
    }else {
      val planManager = SmartSqlManager.getManger(sql)
      print(s"parsedPlan => ${planManager.parsedPlan}")
      print(s"analyzedPlan => ${planManager.analyzedPlan}")
      print(s"optimizedPlan => ${planManager.optimizedPlan}")
      print(s"sparkPlan => ${planManager.sparkPlan}")
      print(s"executedPlan => ${planManager.executedPlan}")
      cacheSqlPlan += (cacheKey -> planManager)
      planManager.executedPlan
    }
    val unsafeResult = plan executeCollect(parameter)
    unsafeResult.map( row => {
      row.asInstanceOf[UnsafeRow].toSeq(plan.output.map(_.dataType))
    })
  }

  def main(args: Array[String]) {
    val sql = """SELECT *
                FROM (
                	SELECT name, max(age), 999 AS index, count(age) as countAge
                	FROM (SELECT name, age + 1 AS age
                	FROM test_user
                 union
                SELECT name, age - 1 AS age
                                	FROM test_user
                		)
                	WHERE age BETWEEN 13 AND 21
                GROUP BY name
                	UNION
                	SELECT p.name, p.age, 1000 AS index, u.`maxDayInvestingAmountOfSixMonth`
                	FROM lcs_hour_all r
                		LEFT JOIN test_user p ON r.node = p.id
                   LEFT JOIN hbase_data_profile_user u on p.id=u.userId

                	WHERE r.datetime > input("start_datetime","date") and r.datetime < input("end_datetime","date") and u.userId < input("userId","int")
                )
                ORDER BY name
                LIMIT 100"""

    val result = SmartSqlClient.queryList(sql,Map("userId" -> 5637,"start_datetime" -> "2017-08-13","end_datetime" -> "2017-08-23"))
    println(result.mkString("\n"))
  }
}
