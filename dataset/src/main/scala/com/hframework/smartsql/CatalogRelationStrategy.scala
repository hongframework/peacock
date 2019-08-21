package com.hframework.smartsql

import java.text.SimpleDateFormat
import java.util
import java.util.{Calendar, Date}

import com.hframework.common.util.RegexUtils
import com.hframework.smartsql.ParameterInput
import com.hframework.smartsql.client.{DBClient, HBaseClient}
import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.catalog.CatalogRelation
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.Strategy
import org.apache.spark.sql.execution.smartsql._
import org.apache.spark.sql.types.{DataType, IntegerType, LongType, StringType}
import com.hframework.common.Logging
import com.hframework.smartsql.client2.RedisClient

import scala.util.control.Breaks

/**
 * Created by zhangquanhong on 2017/8/4.
 */
object CatalogRelationStrategy extends Strategy{
  override def apply(plan: LogicalPlan): Seq[SparkPlan] = {

  val dbEntitys: Seq[DBEntity] = LogicalPlanHelper.distinctDBEntity(plan)
  if(dbEntitys.length == 1) {
    dbEntitys.head match {
              case mysql : Mysql => Seq(new MysqlScanExec(mysql, plan))
              case hbase: HBase => {
                if(LogicalPlanHelper.distinctTableNames(plan).length == 1 && LogicalPlanHelper.onlyEasyPlan(plan)) {
                  Seq(new HBaseScanExec(hbase, plan))
                }else {
                  Nil
                }
              }
              case redis: Redis => {
                if(LogicalPlanHelper.distinctTableNames(plan).length == 1 && LogicalPlanHelper.onlyEasyPlan(plan)) {
                  Seq(new RedisScanExec(redis, plan))
                }else {
                  Nil
                }
              }
              case err @ _ => throw new RuntimeException(s"database $err unsupported")
    }
  }else {
    Nil
  }
  }


}
case class MysqlScanExec(mysql: Mysql,plan: LogicalPlan)
  extends LeafExecNode{

  lazy val keyOrdering = newNaturalAscendingOrdering(_project.map(_.dataType))

  lazy val _project:scala.Seq[org.apache.spark.sql.catalyst.expressions.NamedExpression] = {
    val project = LogicalPlanHelper.getProject(plan)
    if(project.isDefined)
      project.get.projectList
    else Seq.empty
  }

  def wrapValue(value: Any, dataType: DataType) : Any = {
    dataType match {
      case StringType => org.apache.spark.unsafe.types.UTF8String.fromString(value.toString)
      case LongType if(!value.isInstanceOf[Long]) => value.toString.toLong
      case IntegerType if(!value.isInstanceOf[Integer]) => value.toString.toInt
      case _ => value
    }
  }

  lazy val _mysql =  LogicalPlanHelper.distinctDBEntity(plan).head.asInstanceOf[Mysql]

  lazy val mysqlId =  _mysql.asInstanceOf[DBEntity].id
  lazy val _sql = {
//    println(s"[PLAN] ==> $plan")
    val sql = sqlConcat(plan)
    if(sql.startsWith("(") && sql.endsWith(")")){
      sql.substring(1, sql.length-1)
    }else {
      sql
    }
  }


  def sqlConcat(plan: LogicalPlan, tableNames: Set[String] = Set()): String = plan match {
    case agg : Aggregate => {
      val _tableNames = agg.aggregateExpressions.filter(_.qualifier.isDefined).map(_.qualifier.get).toSet
      val childSql = {
        val sql = sqlConcat(agg.child, _tableNames)
        if(sql.startsWith("(") && sql.endsWith(")") && _tableNames.size == 1){
          sql + " AS " + _tableNames.head
        }else {
          sql
        }
      }
        s"""(SELECT ${agg.aggregateExpressions.map(_.sql).mkString(", ")}
             FROM $childSql
             GROUP BY ${agg.groupingExpressions.map(_.sql).mkString(", ")})"""
    }
    case union: Union => {
      "(" + union.children.map("" + sqlConcat(_) + "").mkString("\n UNION \n") + ")"
    }
    case proj: Project => {
      val projectSql = {
        var _projectSql = proj.projectList.map(_.sql).mkString(", ")
        RegexUtils.find(_projectSql, "\\d+L").map(longValue => {
          _projectSql = _projectSql.replace(longValue, longValue.substring(0, longValue.length - 1))
        })
        _projectSql
      }
      s"SELECT $projectSql ${sqlConcat(proj.child) }"
    }
    case filter: Filter => {
      val filterSql = {
        var _filterSql = filter.condition.sql
        _filterSql = if(_filterSql.startsWith("(") && _filterSql.endsWith(")") ){
          _filterSql.substring(1, _filterSql.length - 1)
        }else {
          _filterSql
        }
        _filterSql = if(filter.child.isInstanceOf[CatalogRelation]) {
          _filterSql.replaceAll("[a-zA-Z0-9_]+\\.`", "`")
        }else {
          _filterSql
        }
        RegexUtils.find(_filterSql, "\\d+L").map(longValue => {
          _filterSql = _filterSql.replace(longValue, longValue.substring(0, longValue.length - 1))
        })
        _filterSql
      }

      s"${sqlConcat(filter.child) } WHERE ${filterSql}"
    }
    case CatalogRelation(tableMeta,_,_) => {
//      val dbEntity = SmartSchemaManager.defaultManager.getDatabase(tableMeta.database)
      s" FROM ${tableMeta.identifier.table}"
    }
    case other : LogicalPlan => {
      s" [TODO $other ]"
    }

  }

  /**
   * Overridden by concrete implementations of SparkPlan.
   * Produces the result of the query as an ResultSet[InternalRow]
   */
  override protected def doExecute(parameters : Map[String, Any] = Map.empty[String, Any]): ResultSet[InternalRow] = {
    println(s"[MYSQL] ==> $mysqlId , ${_mysql}")


    var sql = _sql
    ParameterInput.placeholderAndParameters.foreach{placeholderAndParameter => {
      if(parameters.get(placeholderAndParameter._2).isDefined) {
        val value = parameters.get(placeholderAndParameter._2).get
        sql = sql.replaceAll(placeholderAndParameter._1.toString, value.toString)
      }
    }}
//        logDebug(s"[SQL]  ==> ${_sql}")
    println("[MYSQL] ==>" , sql)

    val data = client2.DBClient executeQueryList(mysqlId, sql, null, false)
    println("[MYSQL RESULT]  ==>" , data)


    import scala.collection.JavaConverters._
//    val result = List()
//    for(row <- data.asScala) {
//      result :+ proj(InternalRow.fromSeq(row.asScala))
//    }
    val result = data.asScala.map(row =>{
      val proj = UnsafeProjection.create(output, output)
      proj(
        InternalRow.fromSeq(
          row.asScala.zipWithIndex.map(cell => {
            wrapValue(cell._1, outputList(cell._2).dataType)
          })
        )
      )
    })



//    val row = InternalRow.fromSeq(output.map(_.dataType match{
//      case StringType => org.apache.spark.unsafe.types.UTF8String.fromString("张三")
//      case LongType => 1l
//      case _ => 1
//    }))

    new ResultSet(result.sorted(keyOrdering).toIterator)
  }

  override def output: Seq[Attribute] = LogicalPlanHelper.getAttr(plan)

  lazy val outputList: List[Attribute] = output.toList
}

case class KeyPosition(index: Int, joinChar : String, dataType : DataType, columnName : String)
case class ColumnPosition(columnName : String, dataType : DataType)

abstract class NoSqlExec(plan: LogicalPlan) extends LeafExecNode{

  /**
   * 按照输出列进行排序
   */
  lazy val keyOrdering = newNaturalAscendingOrdering(_project.map(_.dataType))

  def wrapValue(value: Any, dataType: DataType) : Any = {
    dataType match {
      case _ if(value ==null)  => null
      case StringType => org.apache.spark.unsafe.types.UTF8String.fromString(value.toString)
      case LongType if(!value.isInstanceOf[Long]) => value.toString.toLong
      case IntegerType if(!value.isInstanceOf[Integer]) => value.toString.toInt
      case _ => value
    }
  }

  /**
   * 获取输出列信息
   */
  lazy val _project:scala.Seq[org.apache.spark.sql.catalyst.expressions.NamedExpression] = {
    val project = LogicalPlanHelper.getProject(plan)
    if(project.isDefined)
      project.get.projectList
    else Seq.empty
  }

  /**
   * 获取输出列再数据中的索引信息
   */
  lazy val _projectPosition = {
    _project.map(express => {
      val columnName = express.name
      val dataType = express.dataType
      val columnKeyParts = keyDescriptor.keyParts.filter(keyPart => {
        keyPart match {
          case value : Value if(value.varName.eq(columnName)) =>
            true
          case datetime: DateTime if(datetime.varName.eq(columnName)) =>
            true
          case _ => false
        }
      })


      if(columnKeyParts.isDefinedAt(0)){
        val loop = new Breaks
        var count = 0
        loop.breakable(
          for((keyPart, index) <- keyDescriptor.keyParts.zipWithIndex) {
            keyPart match {
              case value : Value if(value.varName.eq(columnName)) =>
                count += index
                loop.break()
              case datetime: DateTime if(datetime.varName.eq(columnName)) =>
                count += index
                loop.break()
              case Character(char : String) if(char.contains(keyDescriptor.joinChart)) =>
                count += char.split(keyDescriptor.joinChart).length - 1
              case _ => false
            }
          }
        )
        KeyPosition(count, keyDescriptor.joinChart, dataType,columnName)
      } else {
        ColumnPosition(columnName, dataType)
      }
    })
  }

  /**
   * 打印取值范围信息
   */
  lazy val _scopePrinter = {

    _scope.asInstanceOf[List[Map[String, (String, Char, String, String, Char)]]].map(andMap => {
      andMap.mapValues(value => {
        val leftOperatorChar = value._2
        val leftValue = value._3
        val rightValue = value._4
        val rightOperatorChar = value._5

        val reallyLeftValue = if(ParameterInput.placeholderAndParameters.get(leftValue).isDefined){
          "#{" + ParameterInput.placeholderAndParameters.get(leftValue).get + "}"
        }else leftValue
        val reallyRightValue =if(ParameterInput.placeholderAndParameters.get(rightValue).isDefined){
          "#{" + ParameterInput.placeholderAndParameters.get(rightValue).get + "}"
        }else rightValue
        if(leftOperatorChar ==  '='){
          reallyLeftValue
        }else  {
          var str = ""
          if(leftOperatorChar != Char.MinValue){
            str += (leftOperatorChar + reallyLeftValue)
          }else {
            str += "(-∞"
          }

          str += ", "
          if(rightOperatorChar != Char.MinValue){
            str += (reallyRightValue + rightOperatorChar)
          }else {
            str += "∞)"
          }
          str
        }
      }).mkString(",")
    }).mkString(";\n")
  }

  /**
   * 获取Rowkey中的列信息
   */
  lazy val _keyColumnInfo = keyDescriptor.keyParts.filter(!_.isInstanceOf[Character]).map(_ match {
    case sha: SHA256 =>
      sha.varName -> sha
    case value : Value =>
      value.varName -> value
    case datetime: DateTime =>
      datetime.varName -> datetime
    case _ =>
      throw new RuntimeException(s" this key type unsupported")
  }).toMap


  lazy val _filter = {
    val filter = LogicalPlanHelper.getFilter(plan)
    if(filter.isDefined)
      filter.get.condition
  }

  /**
   * 取值上下文信息
   */
  lazy val _scope = {
    if(_filter != null) {
      LogicalPlanHelper.getConditions(_filter.asInstanceOf[Expression])
    }else {
      List[Map[String, (String, Char, String, String, Char)]]()
    }
  }

  /**
   * 取值上下文信息-固定值
   */
  lazy val _valScope = _scope.zipWithIndex.map(aGroup => {
    aGroup._2 -> aGroup._1.filter(aCondition => {
      val leftValue = aCondition._2._3
      val rightValue = aCondition._2._4
      ParameterInput.placeholderAndParameters.get(leftValue).isEmpty  && ParameterInput.placeholderAndParameters.get(rightValue).isEmpty
    })
  })

  /**
   * 取值上下文信息-变量
   */
  lazy val _varScope: List[(Int, Map[String, (String, Char, String, String, Char)])] = _scope.zipWithIndex.map(aGroup => {
    aGroup._2 -> aGroup._1.filter(aCondition => {
      val leftValue = aCondition._2._3
      val rightValue = aCondition._2._4
      ParameterInput.placeholderAndParameters.get(leftValue).isDefined  || ParameterInput.placeholderAndParameters.get(rightValue).isDefined
    })
  })


  val keyDescriptor: Key

  /**
   * 常量值对应的值列表
   */
  lazy val _keyColumnValuesFromValScope: Map[String, Any] = _valScope.map(valScope => getKeyColumnValuesFromScope(valScope._2)).reduce(_ ++ _)
  /**
   * 常量值对应的值列表
   */
  lazy val _keyColumnFixValuesFromValScope: Map[String, Any] = _keyColumnValuesFromValScope.filter(item => (item._2.isInstanceOf[String] || item._2.isInstanceOf[List[String]]))

  /**
   * 常量值对应的Key组合对
   */
  lazy val _defaultFixKeyPairs: List[Map[String, String]] = getKeyPairs(_keyColumnFixValuesFromValScope)

  def getRuntimeVarScope(parameter: Map[String, Any]): List[(Int, Map[String, (String, Char, String, String, Char)])] = {
    _varScope.map(aGroup => {
      aGroup._1 -> aGroup._2.map(aCondition => {
        val leftValue = aCondition._2._3
        val rightValue = aCondition._2._4
        val reallyLeftValue = if(ParameterInput.placeholderAndParameters.get(leftValue).isDefined){
          val parameterName = ParameterInput.placeholderAndParameters.get(leftValue).get
          if(parameter.contains(parameterName)) parameter.get(parameterName).get.toString
          else leftValue
        }else leftValue
        val reallyRightValue = if(ParameterInput.placeholderAndParameters.get(rightValue).isDefined){
          val parameterName = ParameterInput.placeholderAndParameters.get(rightValue).get
          if(parameter.contains(parameterName)) parameter.get(parameterName).get.toString
          else rightValue
        }else rightValue

        aCondition._1 -> aCondition._2.copy(_3 = reallyLeftValue, _4 = reallyRightValue)
      })
    }).asInstanceOf[List[(Int, Map[String, (String, Char, String, String, Char)])]]
  }

  def getRuntimeKeyColumnValues(parameter: Map[String, Any] = Map.empty): Map[String, Any] = {
    getRuntimeKeyColumnVarValues(parameter) ++ _keyColumnValuesFromValScope
  }

  def getRuntimeKeyColumnCompareValues(parameter: Map[String, Any] = Map.empty): Map[String, (Char, String)] = {
    getRuntimeKeyColumnValues(parameter).filter(item => (!item._2.isInstanceOf[String] && !item._2.isInstanceOf[List[String]])).asInstanceOf[Map[String, (Char, String)]]
  }

  def getRuntimeKeyColumnFixValues(parameter: Map[String, Any] = Map.empty): Map[String, Any] = {
    getRuntimeKeyColumnVarValues(parameter).filter(item => (item._2.isInstanceOf[String] || item._2.isInstanceOf[List[String]])) ++ _keyColumnFixValuesFromValScope
  }

  def getRuntimeKeyPairs(parameter: Map[String, Any]): List[Map[String, String]] = {
//    val reallyVarScope = getReallyVarScope(parameter)
    val keyColumnVarValues = getRuntimeKeyColumnVarValues(parameter).filter(item => (item._2.isInstanceOf[String] || item._2.isInstanceOf[List[String]]))
    getKeyPairs(keyColumnVarValues, _defaultFixKeyPairs)
  }

  def getRuntimeKeyColumnVarValues(parameter: Map[String, Any]): Map[String, Any] = {
    val reallyVarScope = getRuntimeVarScope(parameter)
    reallyVarScope.map(varScope => getKeyColumnValuesFromScope(varScope._2)).reduce(_ ++ _)
  }

  def getKeyColumnValuesFromScope(scopeMap : Map[String, (String, Char, String, String, Char)]): Map[String, Any]={
    var keyColumnValues = Map.empty[String, Any]
    scopeMap.foreach(value => {
      val parameterName = value._2._1
      val leftOperatorChar = value._2._2
      val leftValue = value._2._3
      val rightValue = value._2._4
      val rightOperatorChar = value._2._5

      if(_keyColumnInfo.contains(parameterName)) {
        val columnInfo = _keyColumnInfo.get(parameterName)
        if(leftOperatorChar ==  '='){
          keyColumnValues += (parameterName -> leftValue)
        }else  {
          if(columnInfo.get.isInstanceOf[DateTime]) {
            val pattern = columnInfo.get.asInstanceOf[DateTime].pattern
            val endTimeStr = if(rightOperatorChar == Char.MinValue) {
              format(new Date(), pattern)
            }else rightValue
            if(leftOperatorChar != Char.MinValue){
              val list = getDatetimeFormatStringBetween(leftValue, endTimeStr, pattern,
                if(leftOperatorChar == '[') true else false,
                if(rightOperatorChar == ']') true else false)
              keyColumnValues += (parameterName -> list)
            }else {
              keyColumnValues += (parameterName -> (rightOperatorChar, endTimeStr))
            }
          }else {
            if(leftOperatorChar != Char.MinValue && rightOperatorChar != Char.MinValue){
              val list = getNumberBetween(leftValue.toLong, rightValue.toLong, "",
                if(leftOperatorChar == '[') true else false,
                if(rightOperatorChar == ']') true else false)
              keyColumnValues += (parameterName -> list)
            }else if(leftOperatorChar != Char.MinValue){
              keyColumnValues += (parameterName -> (leftOperatorChar, leftValue))
            }else if(rightOperatorChar != Char.MinValue) {
              keyColumnValues += (parameterName ->(rightOperatorChar, rightValue))
            }
          }
        }
      }
    })
    keyColumnValues
  }

  def getKeyPairs(keyColumnValues: Map[String, Any], _keyPairs: List[Map[String, String]] = List.empty): List[Map[String, String]] = {
    var keyPairs = _keyPairs
    keyColumnValues.foreach(kv => {
      var newKeys = List[Map[String, String]]()
      kv._2 match {
        case list: List[String] => {
          list.foreach( x => {
            if(keyPairs.isEmpty) {
              newKeys = Map[String, String]((kv._1 -> x)) :: newKeys
            }else {
              newKeys = newKeys ++ keyPairs.map(key => {
                var newMap: Map[String, String] = key ++ Map[String, String]()
                newMap += (kv._1 -> x)
                newMap
              })
            }
          })
        }
        case x: String => {
          if(keyPairs.isEmpty) {
            newKeys = Map[String, String]((kv._1 -> x)) :: newKeys
          }else {
            newKeys = newKeys ++ keyPairs.map(key => {
              var newMap: Map[String, String] = key ++ Map[String, String]()
              newMap += (kv._1 -> x)
              newMap
            })
          }
        }

      }
      keyPairs = newKeys
    })

    if(keyPairs.isEmpty) keyPairs = Map[String, String]() :: keyPairs

    keyPairs
  }



  def getNumberBetween(start: Long, end: Long, dateFormat: String,
                       containStart: Boolean, containEnd: Boolean): List[String] = {
    List.range(start, end).map(x => StringUtils.leftPad(x.toString, dateFormat.length, "0"))

  }

  def getDatetimeFormatStringBetween(startDTStr: String, endDTStr: String, dateFormat: String,
                                     containStart: Boolean, containEnd: Boolean): List[String] = {

    var result = List[String]()
    if(containStart) result = startDTStr :: result

    val startDT = Calendar.getInstance()
    var start = parse(startDTStr.replaceAll("-","").replaceAll(":",""), dateFormat.replaceAll("-","").replaceAll(":",""))
    if(start == null) start = parse(startDTStr, "yyyy-MM-dd HH:mm:ss")
    if(start == null) start = parse(startDTStr, "yyyy-MM-dd HH:mm")
    if(start == null) start = parse(startDTStr, "yyyy-MM-dd HH")
    if(start == null) start = parse(startDTStr, "yyyy-MM-dd")
    if(start == null) start = parse(startDTStr, "yyyy-MM")
    if(start == null) start = parse(startDTStr, "yyyy")
    startDT.setTime(start)

    val endDT =Calendar.getInstance()
    var end = parse(endDTStr.replaceAll("-","").replaceAll(":",""), dateFormat.replaceAll("-","").replaceAll(":",""))
    if(end == null) end = parse(endDTStr, "yyyy-MM-dd HH:mm:ss")
    if(end == null) end = parse(endDTStr, "yyyy-MM-dd HH:mm")
    if(end == null) end = parse(endDTStr, "yyyy-MM-dd HH")
    if(end == null) end = parse(endDTStr, "yyyy-MM-dd")
    if(end == null) end = parse(endDTStr, "yyyy-MM")
    if(end == null) end = parse(endDTStr, "yyyy")
    endDT.setTime(end)

    val field = dateFormat.substring(dateFormat.length-2) match {
      case "yy" => Calendar.YEAR
      case "MM" => Calendar.MONTH
      case "dd" => Calendar.DATE
      case "HH" => Calendar.HOUR
      case "mm" => Calendar.MINUTE
      case "ss" => Calendar.SECOND
    }
    if(containStart) {
      result = format(startDT.getTime, dateFormat) :: result
      startDT.add(field, 1)
    }
    while(startDT.before(endDT)) {
      result = format(startDT.getTime, dateFormat) :: result
      startDT.add(field, 1)
    }
    result = format(startDT.getTime, dateFormat) :: result

    result
  }

  /**
   * 解析日期字符串
   *
   * @param str 日期串
   * @param fmt 日期格式
   * @return
   */
  def parse(str: String, fmt: String): Date = {
    val simDateFormat: SimpleDateFormat = new SimpleDateFormat(fmt)
    var date: Date = null
    try {
      date = simDateFormat.parse(str)
    }
    catch {
      case e: Exception => {
      }
    }
    return date
  }

  def format(date: Date, fmt: String): String = {
    val simDateFormat: SimpleDateFormat = new SimpleDateFormat(fmt)
    try {
      return simDateFormat.format(date)
    }catch {
      case e: Exception => {
        e.printStackTrace()
        return null
      }
    }
  }
}

trait MatchType
object AllMatchType extends MatchType
object FullMatchType extends MatchType
object PrefixMatchType extends MatchType
object RegexMatchType extends MatchType
case class ScopeMatchType() extends MatchType
object LessThenMatchType extends ScopeMatchType
object GreaterThenMatchType extends ScopeMatchType
object LessThenAndEqualMatchType extends ScopeMatchType
object GreaterThenAndEqualMatchType extends ScopeMatchType



case class HBaseScanExec(hbase: HBase,plan: LogicalPlan)
  extends NoSqlExec(plan) {

  lazy val _hbase =  LogicalPlanHelper.distinctDBEntity(plan).head.asInstanceOf[HBase]
  lazy val _table = {
    val tableNames = LogicalPlanHelper.distinctTableNames(plan)
    assert(tableNames.length == 1)
    _hbase.getTable(tableNames.head).asInstanceOf[HTable]
  }

  lazy val _columnMap: Map[String, HBaseQualifier] = _table.columns.map(column => column.asInstanceOf[HBaseQualifier].name -> column.asInstanceOf[HBaseQualifier]).toMap

  lazy val _returnColumnMapInfo : Map[String, java.util.List[String]]= {
    var returnColumnMapInfo : Map[String, java.util.List[String]] = Map.empty
    _projectPosition.filter(_.isInstanceOf[ColumnPosition]).map(column => {
      val columnPostion = column.asInstanceOf[ColumnPosition]

      val columnInfo = _columnMap(columnPostion.columnName)
      if(returnColumnMapInfo.contains(columnInfo.family)) {
        returnColumnMapInfo.get(columnInfo.family).get.add(columnInfo.name)
      }else {
        val list = new util.ArrayList[String]
        list.add(columnInfo.name)
        returnColumnMapInfo += (columnInfo.family -> list)
      }
    })
    returnColumnMapInfo
  }

  val keyDescriptor: Key = _table.keyDescriptor

  /**
   * 根据给定的keyColumnValues与keyColumnFixValues推算rowkey的模板，比如*_[userId]_[>*]
   * 1.根据模板如果只有[userId]类型部分组成，则为 -> FullMatchType
   * 2.根据模板全由*与[>*]组成，则为 -> AllMatchType
   * 2.如果模板*与[>*]部分不是全部在后面，及已知部分在中间或者尾部,则通过正则 -> RegexMatchType
   * 3.如果模板*与[>*]部分全部在后面，且第一个为[>*]， 则 -> GreaterThenMatchType,GreaterThenAndEqualMatchType，LessThenMatchType，LessThenAndEqualMatchType
   * 4.如果模板*与[>*]部分全部在后面，且第一个为*， 则 -> PrefixMatchType
   */
  val _filterMatchType: MatchType = {
    val keyColumnValues = getRuntimeKeyColumnValues()
    val keyColumnFixValues = getRuntimeKeyColumnFixValues()

    val pseudoKeyParts = keyDescriptor.keyParts.map(_ match {
      case SHA256(valName) => {
        if(keyColumnFixValues.contains(valName)) {
          s"SHA256($valName)"
        }else {
          "*"
        }
      }
      case ch: Character => {
        ch.char
      }
      case DateTime(valName, pattern ) => {
        if(keyColumnFixValues.contains(valName)) {
          s"[$valName]"
        }else if(keyColumnValues.contains(valName)) {
          val (char, value) = keyColumnValues.get(valName).get
          s"[$char*]"
        }else {
          "*"
        }
      }
      case Value(valName, _ ) => {
        if(keyColumnFixValues.contains(valName)) {
          s"[$valName]"
        }else if(keyColumnValues.contains(valName)) {
          val (char, value) = keyColumnValues.get(valName).get
          s"[$char*]"
        }else {
          "*"
        }
      }
    })

    val unknownPartSize = pseudoKeyParts.filter(_.contains("*")).size
    if(unknownPartSize == 0) {
      FullMatchType
    }else {
      var unknownCount = 0
      var headUnknownIsHalf = false
      var pseudoHalfKeyPart: String = null
      val loop = new Breaks
      loop.breakable{
        for(part <- pseudoKeyParts.reverse) {
          if(part.contains("*")){
            unknownCount +=1
            if(part.contains("*]")) {
              pseudoHalfKeyPart = part
              headUnknownIsHalf = true
            }else {
              headUnknownIsHalf = false
            }
          }
          else loop.break
        }
      }

      if(unknownCount != unknownPartSize) {
        RegexMatchType
      }else {
        if(headUnknownIsHalf) {
          if(pseudoHalfKeyPart.startsWith("(")) {
            GreaterThenMatchType
          }else if(pseudoHalfKeyPart.startsWith("[")) {
            GreaterThenAndEqualMatchType
          }else if(pseudoHalfKeyPart.startsWith(")")) {
            LessThenMatchType
          }else{
            LessThenAndEqualMatchType
          }

        }else {
          if(unknownPartSize == pseudoKeyParts.size) {
            AllMatchType
          }else {
            PrefixMatchType
          }
        }
      }
    }
  }


  def getKeys(parameters: Map[String, Any]): List[String] = {
    val keyPairs = getRuntimeKeyPairs(parameters)
    _filterMatchType match {
      case AllMatchType => List("*")
      case FullMatchType | PrefixMatchType => {
        keyPairs.map(keyPair => {
          keyDescriptor.keyParts.map(_ match {
            case SHA256(valName) => {
              if(keyPair.contains(valName)) {
                SHA256Func.invoke(keyPair.get(valName).get)
              }else null
            }
            case ch: Character => {
              ch.char
            }
            case DateTime(valName, pattern ) => {
              keyPair.get(valName).getOrElse(null)
            }
            case Value(valName, _ ) => {
              keyPair.get(valName).getOrElse(null)
            }
          }).filter(_ == null).mkString(keyDescriptor.joinChart)
        })
      }
      case RegexMatchType => {
        keyPairs.map(keyPair => {
          keyDescriptor.keyParts.map(_ match {
            case SHA256(valName) => {
              if(keyPair.contains(valName)) {
                SHA256Func.invoke(keyPair.get(valName).get)
              }else {
                ".*"
              }
            }
            case ch: Character => {
              ch.char
            }
            case DateTime(valName, pattern ) => {
              keyPair.get(valName).getOrElse(s".{${pattern.length}}")
            }
            case Value(valName, _ ) => {
              keyPair.get(valName).getOrElse(".*")
            }
          }).mkString(keyDescriptor.joinChart)
        })
      }
      case tp: ScopeMatchType => {
        val keys = {
          keyPairs.map(keyPair => {
            val floorChar = if(tp.eq(LessThenMatchType) || tp.eq(LessThenAndEqualMatchType)) {
              "z"
            }else {
              "0"
            }
            var hasPrefix = false
            keyDescriptor.keyParts.map(_ match {
              case SHA256(valName) => {
                if(keyPair.contains(valName)) {
                  SHA256Func.invoke(keyPair.get(valName).get)
                }else {
                  Array.fill(2)(floorChar).mkString
                }
              }
              case ch: Character => {
                ch.char
              }
              case DateTime(valName, pattern ) => {
                if(keyPair.contains(valName)) {
                  keyPair.get(valName)
                }else if(!hasPrefix){
                  val (char, value) = _keyColumnValuesFromValScope.get(valName).get
                  hasPrefix = true
                  value
                }else {
                  Array.fill(pattern.length)(floorChar).mkString
                }
              }
              case Value(valName, _ ) => {
                if(keyPair.contains(valName)) {
                  keyPair.get(valName)
                }else if(!hasPrefix){
                  val (char, value) = _keyColumnValuesFromValScope.get(valName).get
                  hasPrefix = true
                  value
                }else {//TODO 如何设置长度
                  Array.fill(5)(floorChar).mkString
                }
              }
            }).mkString(keyDescriptor.joinChart)
          })
        }
        keys
      }
    }
  }



  override protected def doExecute(parameters : Map[String, Any] = Map.empty[String, Any]): ResultSet[InternalRow] = {
    val runtimeKeys = getKeys(parameters)
    val checkKeys = getRuntimeKeyColumnCompareValues(parameters)
    println(s"[HBASE] ==> ${_hbase} >> ${_table.name} >> ${_project}")
    println(s"[HBASE] ==> ${_scopePrinter} >> ${parameters}")
    println(s"[HBASE] ==> ${_filterMatchType.getClass.getSimpleName} >> ${runtimeKeys}")
//    println("[PROJECT_POSITION]  ==>" + _projectPosition)
//    println("[FILTER]  ==>" + _filter)
//    println("[RETURNCOLUMNMAPINFO]  ==>" + _returnColumnMapInfo)

    val hBaseClient: client2.HBaseClient = client2.HBaseClient.getInstance(_hbase.zkList, _hbase.zkPort.toString)

    import scala.collection.JavaConverters._
    val scanResult = _filterMatchType match {
      case AllMatchType => hBaseClient.scanner().table(_table.name).addResultColumns(_returnColumnMapInfo.asJava).scan()
      case FullMatchType => hBaseClient.gets(_table.name, runtimeKeys.asJava)
      case PrefixMatchType => hBaseClient.scanner().table(_table.name).prefixRowsMatchAny(runtimeKeys.toArray:_*).addResultColumns(_returnColumnMapInfo.asJava).scan
      case RegexMatchType => hBaseClient.scanner().table(_table.name).regexRowsMatchAny(runtimeKeys.toArray:_*).addResultColumns(_returnColumnMapInfo.asJava).scan
      case LessThenMatchType | LessThenAndEqualMatchType=> hBaseClient.scanner().table(_table.name).stopRow(runtimeKeys.max).addResultColumns(_returnColumnMapInfo.asJava).scan
      case GreaterThenMatchType |GreaterThenAndEqualMatchType => hBaseClient.scanner().table(_table.name).startRow(runtimeKeys.min).addResultColumns(_returnColumnMapInfo.asJava).scan
    }

    import org.apache.hadoop.hbase.client.Result
    val resultList = scanResult match {
      case result : client2.HBaseClient.ScanResult => result.asScala.toList
      case result : Array[Result] => result.toList
    }


  val data = resultList.map(row => {
     val rowKey = new String(row.getRow)
     _projectPosition.map(_ match {
      case KeyPosition(index, joinChar, dataType, columnName) =>
        (rowKey.split(joinChar)(index), dataType, columnName)
      case ColumnPosition(columnName, dataType) =>
        val columnInfo = _columnMap(columnName)
        (client2.HBaseClient.resultGet(row, columnInfo.family, columnInfo.name), dataType, columnName)
    })
  }).map(_.filter(x => {
    if(checkKeys.contains(x._3)){
      val compareSides:(Long, Long) =  x._2 match {
        case LongType => (x._1.toLong, checkKeys.get(x._3).get._2.toLong)
        case IntegerType => (x._1.toInt, checkKeys.get(x._3).get._2.toInt)
        case _ => (x._1.toLong, checkKeys.get(x._3).get._2.toLong)
      }
      val bool = checkKeys.get(x._3).get._1 match {
        case '(' => compareSides._1 > compareSides._2
        case '[' => compareSides._1 >= compareSides._2
        case ')' => compareSides._1 < compareSides._2
        case ']' => compareSides._1 <= compareSides._2
      }
      bool
    }else true
  })).filter(_.size == _projectPosition.size).map(_.map(x => wrapValue(x._1, x._2)))

  println("[HBASE RESULT]  ==>" +data)

//  data = data.sortWith((row1, row2) => row1.head.asInstanceOf[Long] < row2.head.asInstanceOf[Long])

  //    val row = InternalRow.fromSeq(List.fill(10)(1l))

  //    new ResultSet(Seq(proj(row).copy()).toIterator)
  val result = data.map(row =>{
    val proj = UnsafeProjection.create(output, output)
    proj(InternalRow.fromSeq(row))
  })
  new ResultSet(result.sorted(keyOrdering).toIterator)

//    val proj = UnsafeProjection.create(output, output)
//
//    val row = InternalRow.fromSeq(output.map(_.dataType match{
//      case StringType => org.apache.spark.unsafe.types.UTF8String.fromString("测试值1")
//      case LongType => 1l
//      case _ => 1
//    }))
//
//    new ResultSet(Seq(proj(row).copy()).toIterator)
  }

  override def output: Seq[Attribute] = LogicalPlanHelper.getAttr(plan)
}


case class RedisScanExec(redis: Redis,plan: LogicalPlan)
  extends NoSqlExec(plan) {

  lazy val _redis:Redis =  LogicalPlanHelper.distinctDBEntity(plan).head.asInstanceOf[Redis]

  lazy val redisId =  _redis.asInstanceOf[DBEntity].id

  lazy val _table = {
    val tableNames = LogicalPlanHelper.distinctTableNames(plan)
    assert(tableNames.length == 1)
    _redis.getTable(tableNames.head).asInstanceOf[RTable]
  }

  lazy val redisIds = {
    _table.dbs.dbs.map(dbNo => s"$redisId-$dbNo")
  }

  val keyDescriptor: Key = _table.keyDescriptor

  def getKeys(parameters: Map[String, Any] = Map.empty): List[String] = {
    val key = keyDescriptor
    getRuntimeKeyPairs(parameters).map(keyPair => {
      key.keyParts.map(_ match {
        case SHA256(valName) => {
          if(keyPair.contains(valName)) {
            SHA256Func.invoke(keyPair.get(valName).get)
          }else {
            "*"
          }
        }
        case ch: Character => {
          ch.char
        }
        case DateTime(valName, pattern ) => {
          keyPair.get(valName).getOrElse(new String(Array.fill(pattern.length)('?')))
        }
        case Value(valName, _ ) => {
          keyPair.get(valName).getOrElse("*")
        }
      }).mkString(key.joinChart)
    })
  }

  lazy val _keys = getKeys()
  lazy val _keysRequired = _keyColumnInfo.size - getRuntimeKeyColumnFixValues().size> 0

  override protected def doExecute(parameters : Map[String, Any] = Map.empty[String, Any]): ResultSet[InternalRow] = {
//    println("[PROJECT_POSITION]  ==>" + _projectPosition)
//    println("[FILTER]  ==>" + _filter)
    val runtimeKeys = getKeys(parameters)
    val checkKeys = getRuntimeKeyColumnCompareValues(parameters)
    println(s"[REDIS] ==> ${_redis} >> ${_table.dbs} >> ${_table.name} >> ${_project}")
    println(s"[REDIS] ==> ${_scopePrinter} >> ${parameters}")
    println(s"[REDIS] ==> ${_keysRequired} >> ${runtimeKeys}")

    import scala.collection.JavaConverters._

    val data = redisIds.flatMap(_redisId => {
      val keys: List[String] = if(_keysRequired) {
        val patternKeys = RedisClient keysList(_redisId, runtimeKeys.asJava)
        patternKeys.asScala.values.flatMap(_.asInstanceOf[java.util.Set[String]].asScala).toList
      }else {
        runtimeKeys.toList
      }

      val dataMap = RedisClient.hGetList(_redisId, keys.asJava)
//      println("[DATA]  ==>" +dataMap)

      val result = dataMap.asScala.map(row => {
        _projectPosition.map(_ match {
          case KeyPosition(index, joinChar, dataType, columnName) =>
            (row._1.split(joinChar)(index), dataType, columnName)
          case ColumnPosition(columnName, dataType) =>
            (row._2.asInstanceOf[Map[String, Object]].get(columnName).getOrElse(null), dataType, columnName)
        })
      })
      result
    }).map(_.filter(x => {
      if(checkKeys.contains(x._3)){
        val reallyValue = if(x._1 == null) "" else x._1.toString
        val compareSides:(Long, Long) =  x._2 match {
          case LongType => (reallyValue.toLong, checkKeys.get(x._3).get._2.toLong)
          case IntegerType => (reallyValue.toInt, checkKeys.get(x._3).get._2.toInt)
          case _ => (reallyValue.toLong, checkKeys.get(x._3).get._2.toLong)
        }
        val bool = checkKeys.get(x._3).get._1 match {
          case '(' => compareSides._1 > compareSides._2
          case '[' => compareSides._1 >= compareSides._2
          case ')' => compareSides._1 < compareSides._2
          case ']' => compareSides._1 <= compareSides._2
        }
        bool
      }else true
    })).filter(_.size == _projectPosition.size).map(_.map(x => wrapValue(x._1, x._2)))


//    data = data.sortWith((row1, row2) => row1.head.asInstanceOf[Long] < row2.head.asInstanceOf[Long])

    println("[REDIS RESULT] ==>" +data)
//    val row = InternalRow.fromSeq(List.fill(10)(1l))

//    new ResultSet(Seq(proj(row).copy()).toIterator)
    val result = data.map(row =>{
       val proj = UnsafeProjection.create(output, output)
       proj(InternalRow.fromSeq(row))
    })
    new ResultSet(result.sorted(keyOrdering).toIterator)
  }

  override def output: Seq[Attribute] = LogicalPlanHelper.getAttr(plan)

}

object LogicalPlanHelper{
  def distinctDBEntity(plan: LogicalPlan, dbEntitys: Seq[DBEntity] = Seq.empty) : Seq[DBEntity] =  plan match {
    case CatalogRelation(tableMeta,_,_) => {
      val dbEntity = SmartSchemaManager.defaultManager.getDatabase(tableMeta.database)
      if(!dbEntitys.contains(dbEntity)){
        Seq(dbEntity) ++ dbEntitys
      }else{
        dbEntitys
      }
    }
    case logicalPlan : LogicalPlan => {
      logicalPlan.children.foldLeft(dbEntitys)((dBEnti,childPlan) =>{
        distinctDBEntity(childPlan,dBEnti)
      })
    }
  }

  def distinctTableNames(plan: LogicalPlan, tableNames: Seq[String] = Seq.empty) : Seq[String] =  plan match {
    case CatalogRelation(tableMeta,_,_) => {
      val tableName = tableMeta.identifier.table
      if(!tableNames.contains(tableName)){
        Seq(tableName.asInstanceOf[String]) ++ tableNames
      }else{
        tableNames
      }
    }
    case logicalPlan : LogicalPlan => {
      logicalPlan.children.foldLeft(tableNames)((dBEnti,childPlan) =>{
        distinctTableNames(childPlan,dBEnti)
      })
    }
  }

  def onlyEasyPlan(plan: LogicalPlan) : Boolean =
    if(plan.isInstanceOf[CatalogRelation] || plan.isInstanceOf[Project] || plan.isInstanceOf[Filter]) {
      true
    }else {
      false
    }


  def getAttr(plan: LogicalPlan): Seq[Attribute] = plan match {
    case Project(projectList, child) => {
      projectList.map(_.toAttribute)
    }
    case CatalogRelation(_, dataCols, _) => {
      dataCols.attrs
    }
    case Aggregate(groupingExpressions, aggregateExpressions, _) => {
      aggregateExpressions.map(_.toAttribute)
    }
    case _ => {
      getAttr(plan.children.head)
    }
  }

  def getProject(plan: LogicalPlan): Some[Project] = plan match {
    case proj: Project => {
      Some(proj)
    }
    case other : LogicalPlan => {
      if(other.children == null || other.children.size == 0)
        return null
      getProject(other.children.head)
    }
  }

  def getFilter(plan: LogicalPlan): Option[Filter] = plan match {
    case filter: Filter => {
      Some(filter)
    }
    case other : LogicalPlan => {
      if(other.children == null || other.children.size == 0)
        return None
      getFilter(other.children.head)
    }
  }

  def getConditions(express: Expression): List[Map[String, (String, Char, String, String, Char)]] = express match {
    case and: org.apache.spark.sql.catalyst.expressions.And => {
      val left: List[Map[String, (String, Char, String, String, Char)]] = getConditions(and.left)
      val right: List[Map[String, (String, Char, String, String, Char)]] = getConditions(and.right)
      if(left.size == 0 ){
        right
      }else if(right.size == 0) {
        left
      }else{
        left.flatMap(leftMap => {
          right.map(rightMap =>{
            val shareMap = leftMap.filterKeys(rightMap.contains(_)).map( entry => {
              val leftInfo =entry._2
              val rightInfo = rightMap.get(entry._1).get
              val list = leftInfo.productIterator.toList.zip(rightInfo.productIterator.toList).map(x => {
                if(x._1 == null || x._1 == Char.MinValue){
                  x._2
                }else {
                  x._1
                }
              })
              (entry._1 ,(list(0).asInstanceOf[String],
                          list(1).asInstanceOf[Char],
                          list(2).asInstanceOf[String],
                          list(3).asInstanceOf[String],
                          list(4).asInstanceOf[Char]))
            })
            leftMap ++ rightMap ++ shareMap
          })
        })
      }

    }
    case _: IsNotNull => {
      List[Map[String, (String, Char, String, String, Char)]]()
    }
    case than: EqualTo => {
      val keyValue:(String, String, Boolean) = getCondition(than.left, than.right)
      val result = (keyValue._1, '=', keyValue._2, null,  Char.MinValue)

      List[Map[String, (String, Char, String, String, Char)]](
        Map[String, (String, Char, String, String, Char)](
          keyValue._1 -> result
        )
      )
    }
    case than: GreaterThan => {
      val keyValue:(String, String, Boolean) = getCondition(than.left, than.right)
      val result = if(keyValue._3)
        (keyValue._1, '(', keyValue._2, null,  Char.MinValue)
      else
        (keyValue._1, Char.MinValue, null, keyValue._2,  ')')
      List[Map[String, (String, Char, String, String, Char)]](
        Map[String, (String, Char, String, String, Char)](
          keyValue._1 -> result
        )
      )
    }
    case than: LessThan => {
      val keyValue:(String, String, Boolean) = getCondition(than.left, than.right)
      val result = if(!keyValue._3)
        (keyValue._1, '(', keyValue._2, null,  Char.MinValue)
      else
        (keyValue._1, Char.MinValue, null, keyValue._2,  ')')
      List[Map[String, (String, Char, String, String, Char)]](
        Map[String, (String, Char, String, String, Char)](
          keyValue._1 -> result
        )
      )
    }
    case than: GreaterThanOrEqual => {
      val keyValue:(String, String, Boolean) = getCondition(than.left, than.right)
      val result = if(keyValue._3)
        (keyValue._1, '[', keyValue._2, null,  Char.MinValue)
      else
        (keyValue._1, Char.MinValue, null, keyValue._2,  ']')
      List[Map[String, (String, Char, String, String, Char)]](
        Map[String, (String, Char, String, String, Char)](
          keyValue._1 -> result
        )
      )
    }
    case than: LessThanOrEqual => {
      val keyValue:(String, String, Boolean) = getCondition(than.left, than.right)
      val result = if(!keyValue._3)
        (keyValue._1, '[', keyValue._2, null,  Char.MinValue)
      else
        (keyValue._1, Char.MinValue, null, keyValue._2,  ']')
      List[Map[String, (String, Char, String, String, Char)]](
        Map[String, (String, Char, String, String, Char)](
          keyValue._1 -> result
        )
      )
    }
    case other : Expression => {
      throw new RuntimeException(s"unsupprot $other")
    }
  }


  def getCondition(leftExpress: Expression, rightExpress: Expression):(String, String, Boolean) = {
    val left = parseOneSide(leftExpress)
    val right = parseOneSide(rightExpress)

    if(StringUtils.isNoneBlank(right._1)) {
      left.copy(right._1,left._1,false)
    }else {
      right.copy(left._1)
    }
  }

  def parseOneSide(oneSide: Expression):(String, String, Boolean) = oneSide match {
    case oneSide: Cast => {
      (oneSide.child.asInstanceOf[AttributeReference].name, null, true)
    }
    case oneSide: AttributeReference => {
      (oneSide.name, null, true)
    }
    case oneSide: Literal => {
      (null, oneSide.value.toString, true)
    }
  }
}