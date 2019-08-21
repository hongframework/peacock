package com.hframework.smartsql

import java.net.URI

import org.apache.spark.bridge.Utils
import org.apache.spark.sql.catalyst.analysis.{Analyzer, FunctionRegistry}
import org.apache.spark.sql.catalyst.catalog._
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.optimizer.Optimizer
import org.apache.spark.sql.catalyst.parser.CatalystSqlParser
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, ReturnAnswer}
import org.apache.spark.sql.catalyst.rules.Rule
import org.apache.spark.sql.catalyst.{FunctionIdentifier, TableIdentifier}
import org.apache.spark.sql.execution.smartsql.{SparkPlan, _}
import org.apache.spark.sql.internal.SQLConf
import org.apache.spark.sql.types.StructType

/**
 * Created by zhangquanhong on 2017/7/28.
 */
object Demo  extends  PredicateHelper{
  def main(args: Array[String]) {
    var plan = CatalystSqlParser.parsePlan("" +
      "select * from ( SELECT name, max(age),999 as index, count(age) FRoM (select  name, (age+1) as age from people) WHERE age BETWEEN 13 AND 19 group by name " +
//      "SELECT name, age,999, age FRoM (select  name, (age+1) as age from people) WHERE age BETWEEN 13 AND 19 group by name " +
      "union (select p.name, p.age, 1000 as index, p.six from report r left join people p on r.col1 = p.col1 where id < 10 )) order by name limit 1")
    print(plan)

    plan = CatalogBuilder.build().execute(plan)
    print(plan)
    plan = CatalogBuilder.optimizer.execute(plan)
    print(plan)
    val sparkPlan = CatalogBuilder.planner.plan(ReturnAnswer(plan))
    print(sparkPlan)
  }
}

class QueryPlanManager(plan: LogicalPlan){
  lazy val parsedPlan = CatalogBuilder.build().execute(plan)
  lazy val optimizedPlan = CatalogBuilder.optimizer.execute(parsedPlan)
  lazy val sparkPlan: SparkPlan = {
    print(parsedPlan)
    print(optimizedPlan)
    val sparkPlan: SparkPlan = CatalogBuilder.planner.plan(ReturnAnswer(optimizedPlan)).next()
    sparkPlan
  }
  lazy val executedPlan: SparkPlan = prepareForExecution(sparkPlan)

  /**
   * Prepares a planned [[SparkPlan]] for execution by inserting shuffle operations and internal
   * row format conversions as needed.
   */
  protected def prepareForExecution(plan: SparkPlan): SparkPlan = {
    preparations.foldLeft(plan) { case (sp, rule) => rule.apply(sp) }
  }

  /** A sequence of rules that will be applied in order to the physical plan before execution. */
  protected def preparations: Seq[Rule[SparkPlan]] = Seq(
//    python.ExtractPythonUDFs,
//    PlanSubqueries(sparkSession),
//    EnsureRequirements(sparkSession.sessionState.conf),
//    CollapseCodegenStages(sparkSession.sessionState.conf),
//    ReuseExchange(sparkSession.sessionState.conf),
//    ReuseSubquery(sparkSession.sessionState.conf)
  )
}

object CatalogBuilder{
  val tableInputFormat: String = "org.apache.park.SequenceFileInputFormat"
  val tableOutputFormat: String = "org.apache.park.SequenceFileOutputFormat"
  val defaultProvider: String = "parquet"
  val funcClass = "org.apache.spark.myFunc"

  val optimizer: Optimizer = {
    val conf = new SQLConf()
    val catalog = new SessionCatalog(newBasicCatalog(), FunctionRegistry.builtin, conf)
    new CommonOptimizer(catalog, conf)
  }

  class CommonOptimizer(sessionCatalog: SessionCatalog, conf: SQLConf)
    extends Optimizer(sessionCatalog: SessionCatalog, conf: SQLConf)

  lazy val storageFormat = CatalogStorageFormat(
    locationUri = None,
    inputFormat = Some(tableInputFormat),
    outputFormat = Some(tableOutputFormat),
    serde = None,
    compressed = false,
    properties = Map.empty)


  lazy val part1 = CatalogTablePartition(Map("a" -> "1", "b" -> "2"), storageFormat)
  lazy val part2 = CatalogTablePartition(Map("a" -> "3", "b" -> "4"), storageFormat)

  def newEmptyCatalog(): ExternalCatalog = new InMemoryCatalog

  def newBasicCatalog(): ExternalCatalog = {
    val catalog = newEmptyCatalog()
    // When testing against a real catalog, the default database may already exist
    catalog.createDatabase(newDb("default"), ignoreIfExists = true)
    catalog.createDatabase(newDb("db1"), ignoreIfExists = false)
    catalog.createDatabase(newDb("db2"), ignoreIfExists = false)
    catalog.createDatabase(newDb("db3"), ignoreIfExists = false)
    catalog.createTable(newTable("tbl1", "db2"), ignoreIfExists = false)
    catalog.createTable(newTable("tbl2", "db2"), ignoreIfExists = false)
//    catalog.createTable(newTable("people", "default"), ignoreIfExists = false)
    catalog.createTable(newTable("report", "default"), ignoreIfExists = false)
    catalog.createTable(newView("view1", Some("db3")), ignoreIfExists = false)
    catalog.createPartitions("db2", "tbl2", Seq(part1, part2), ignoreIfExists = false)
    catalog.createFunction("db2", newFunc("func1", Some("db2")))
//    catalog.createFunction("default", newFunc("max", Some("default")))
//    catalog.createFunction("default", newFunc("count", Some("default")))
    catalog
  }

    def planner: SparkPlanner = {
      val conf = new SQLConf()
      val catalog = new SessionCatalog(newBasicCatalog(), FunctionRegistry.builtin, conf)
      new SparkPlanner(null, conf) {
  //      override def extraPlanningStrategies: Seq[Strategy] =
  //        super.extraPlanningStrategies ++ customPlanningStrategies
      }
    }

  def build(): Analyzer ={
    val conf = new SQLConf()
        val catalog = new SessionCatalog(newBasicCatalog(), FunctionRegistry.builtin, conf)
//    val catalog = new SessionCatalog(newEmptyCatalog(), new SimpleFunctionRegistry, conf)
    val analyzer = new Analyzer(catalog, conf)
    val people = CatalogTable(
      identifier = TableIdentifier("people", Some("default")),
      tableType = CatalogTableType.EXTERNAL,
      storage = storageFormat.copy(locationUri = Some(Utils.createTempDir().toURI)),
      schema = new StructType()
        .add("id", "int")
        .add("six", "int")
        .add("name", "string")
        .add("age", "int")
        .add("col1", "int")
        .add("col2", "string")
        .add("a", "int")
        .add("b", "string"),
      provider = Some(defaultProvider),
      partitionColumnNames = Seq("a", "b"),
      bucketSpec = Some(BucketSpec(4, Seq("col1"), Nil)))


    catalog.createTable(people, ignoreIfExists = false)
    catalog.setCurrentDatabase("default")
    analyzer
  }

  def newDb(name: String): CatalogDatabase = {
    CatalogDatabase(name, name + " description", newUriForDatabase(), Map.empty)
  }

  def newTable(name: String, db: String): CatalogTable = newTable(name, Some(db))

  def newTable(name: String, database: Option[String] = None): CatalogTable = {
    CatalogTable(
      identifier = TableIdentifier(name, database),
      tableType = CatalogTableType.EXTERNAL,
      storage = storageFormat.copy(locationUri = Some(Utils.createTempDir().toURI)),
      schema = new StructType()
        .add("col1", "int")
        .add("col2", "string")
        .add("a", "int")
        .add("b", "string"),
      provider = Some(defaultProvider),
      partitionColumnNames = Seq("a", "b"),
      bucketSpec = Some(BucketSpec(4, Seq("col1"), Nil)))
  }

  def newView(
               name: String,
               database: Option[String] = None): CatalogTable = {
    val viewDefaultDatabase = database.getOrElse("default")
    CatalogTable(
      identifier = TableIdentifier(name, database),
      tableType = CatalogTableType.VIEW,
      storage = CatalogStorageFormat.empty,
      schema = new StructType()
        .add("col1", "int")
        .add("col2", "string")
        .add("a", "int")
        .add("b", "string"),
      viewText = Some("SELECT * FROM tbl1"),
      properties = Map(CatalogTable.VIEW_DEFAULT_DATABASE -> viewDefaultDatabase))
  }

  def newFunc(name: String, database: Option[String] = None): CatalogFunction = {
    CatalogFunction(FunctionIdentifier(name, database), funcClass, Seq.empty[FunctionResource])
  }

  def newUriForDatabase(): URI = new URI(Utils.createTempDir().toURI.toString.stripSuffix("/"))
}
