
package com.hframework.smartsql

import org.apache.spark.sql.catalyst.analysis.Analyzer
import org.apache.spark.sql.catalyst.catalog.SessionCatalog
import org.apache.spark.sql.catalyst.expressions.UnsafeRow
import org.apache.spark.sql.catalyst.optimizer.Optimizer
import org.apache.spark.sql.catalyst.parser.CatalystSqlParser
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, ReturnAnswer}
import org.apache.spark.sql.catalyst.rules.Rule
import org.apache.spark.sql.execution.Strategy
import org.apache.spark.sql.execution.smartsql.{SparkPlan, SparkPlanner}
import org.apache.spark.sql.internal.SQLConf

/**
 * Created by zhangquanhong on 2017/8/1.
 */

object SmartSqlManager{
  def execute(sqlText: String): SparkPlan ={
    new SmartSqlManager(sqlText).executedPlan
  }

  def parse(sqlText: String): LogicalPlan ={
    new SmartSqlManager(sqlText).parsedPlan
  }

  def getManger(sqlText: String): SmartPlanManager ={
    new SmartSqlManager(sqlText).planManager
  }
}

class SmartSqlManager(sqlText: String) {

  lazy val parsedPlan: LogicalPlan = {
    CatalystSqlParser.parsePlan(sqlText)
  }
  lazy val planManager: SmartPlanManager = new SmartPlanManager(parsedPlan)
  lazy val executedPlan: SparkPlan = planManager.executedPlan
}


class SmartPlanManager(plan: LogicalPlan) {
  val parsedPlan = plan
  val catalog: SessionCatalog = SmartSchemaManager.defaultManager.catalog
  val conf = SmartSchemaManager.defaultManager.conf

  lazy val analyzer = new Analyzer(catalog, conf)
  lazy val analyzedPlan = analyzer.execute(parsedPlan)

  lazy val optimizer: Optimizer = new SmartOptimizer(catalog, conf)
  lazy val optimizedPlan = optimizer.execute(analyzedPlan)

  lazy val planner = new SparkPlanner(null, conf) {
          override def extraPlanningStrategies: Seq[Strategy] =
            super.extraPlanningStrategies ++ Seq(
              CatalogRelationStrategy
            )
  }

  lazy val sparkPlan: SparkPlan = {
    val sparkPlan: SparkPlan = planner.plan(ReturnAnswer(optimizedPlan)).next()
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


class SmartOptimizer(sessionCatalog: SessionCatalog, conf: SQLConf)
  extends Optimizer(sessionCatalog: SessionCatalog, conf: SQLConf)