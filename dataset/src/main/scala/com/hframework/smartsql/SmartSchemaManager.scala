package com.hframework.smartsql

import java.net.URI

import com.hframework.smartsql.client.DBClient
import com.hframework.smartsql.client2.RedisClient
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry._
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.{FunctionIdentifier, TableIdentifier}
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry
import org.apache.spark.sql.catalyst.catalog._
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, SubqueryAlias}
import org.apache.spark.sql.internal.SQLConf
import org.apache.spark.sql.types._
import org.apache.spark.bridge.Utils
/**
 * Created by zhangquanhong on 2017/8/1.
 */

object SmartSchemaManager {
  val defaultManager = new SmartSchemaManager


}

class DBEntity(var id: String = null, var description: String = null, var tables: Map[String, TableEntity] = Map.empty[String, TableEntity]){
  def addTable(table: TableEntity) = {
    tables +=(table.name -> table)
  }

  def getTable(tableName: String) : TableEntity = {
    tables(tableName)
  }

  def getDatabase(id: String): DBEntity ={
    SmartSchemaManager.defaultManager.getDatabase(id)
  }
}

case class Mysql(host: String, port: Int, database: String,username: String, password: String) extends DBEntity

case class HBase(zkList: String, zkPort: Int) extends DBEntity

case class Redis( host:String, port: Int, auth: String) extends DBEntity

 abstract class  TableEntity(val name: String, val columns: Seq[ColumnEntity]){
   val allColumns:Seq[ColumnEntity]
}

case class Table(override val name: String, override val columns: Seq[ColumnEntity], var dbId: String = null) extends TableEntity(name, columns) {
  lazy val allColumns: Seq[ColumnEntity] = {
    columns
  }
}


case class RTable(override val name: String, dbs: RDb, keyDescriptor: Key,override val columns: Seq[ColumnEntity], var dbId: String = null) extends TableEntity(name, columns){

  lazy val allColumns: Seq[ColumnEntity] = {
    keyDescriptor.keyParts.filter(_.isInstanceOf[Value]).map(kp => KeyColumn(kp.asInstanceOf[Value].varName, kp.asInstanceOf[Value].dataType)).toSeq  ++  keyDescriptor.keyParts.filter(_.isInstanceOf[DateTime]).map(kp => KeyColumn(kp.asInstanceOf[DateTime].varName, DateType)).toSeq ++ columns
    
  }
}

case class HTable(override val name: String, keyDescriptor: Key, override val columns: Seq[ColumnEntity], var dbId: String = null) extends TableEntity(name, columns){
  lazy val allColumns: Seq[ColumnEntity] = {
    keyDescriptor.keyParts.filter(_.isInstanceOf[Value]).map(kp => KeyColumn(kp.asInstanceOf[Value].varName, kp.asInstanceOf[Value].dataType)).toSeq  ++  keyDescriptor.keyParts.filter(_.isInstanceOf[DateTime]).map(kp => KeyColumn(kp.asInstanceOf[DateTime].varName, DateType)).toSeq ++ columns
  }
}

class ColumnEntity(val name:String, val dataType: DataType, val isPartition: Boolean = false)

case class Column(override val name:String, override val dataType: DataType = StringType, override val isPartition: Boolean = false)  extends ColumnEntity(name, dataType, isPartition)

case class RedisMapItem(override val name:String,
                        override val dataType: DataType = StringType,
                        pattern: String = null,
                        override val isPartition: Boolean = false) extends ColumnEntity(name, dataType, isPartition)

case class RedisItem(override val name:String = "_VALUE",
                     override val dataType: DataType = StringType,
                     pattern: String = null,
                     override val isPartition: Boolean = false) extends ColumnEntity(name, dataType, isPartition)

case class HBaseQualifier(family: String = null,
                          override val name:String,
                          override val dataType: DataType = StringType,
                          pattern: String = null,
                          override val isPartition: Boolean = false) extends ColumnEntity(name, dataType, isPartition)

case class KeyColumn(override val name:String,
                     override val dataType: DataType = StringType,
                     pattern: String = null,
                     override val isPartition: Boolean = false) extends ColumnEntity(name, dataType, isPartition)

case class RDb(dbs: Seq[Int], partition: RPartition = null)


case class Key(keyParts:List[KeyPart] = List[KeyPart](), joinChart:String = null){
//  private var keyParts = List[KeyPart]()
//  private var joinChart:String = null;
  def add(part: KeyPart): Key ={
//    keyParts = part +: keyParts
//    this
    Key(keyParts :+ part, joinChart)
  }
  def sha256(varName : String): Key = {
    add(SHA256(varName))
  }

  def underline(): Key ={
    add(Character("_"))
  }

  def prechars(chars: String): Key = {
    add(Character(chars))
  }

  def column(columnName: String, dataType : DataType = LongType): Key ={
    add(Value(columnName, LongType))
  }

  def dateTimeYYYYMM(varName : String): Key ={
    dateTime(varName, "yyyyMM")
  }
  def dateTimeYYYYMMDD(varName : String): Key ={
    dateTime(varName, "yyyyMMdd")
  }
  def dateTime(varName : String, pattern:String = "yyyyMMddHHmmss"): Key ={
    add(DateTime(varName, pattern))
  }
  def joinwith(char: String): Key = {
    Key(keyParts, char)
  }
}

trait KeyPart

case class Value(varName : String, dataType : DataType = LongType) extends KeyPart
case class SHA256(varName : String) extends KeyPart

//yyyy-MM-dd HH:mm:ss
case class DateTime(varName : String, pattern:String = "yyyyMMddHHmmss") extends KeyPart

case class Character(char : String) extends KeyPart

case class VarKeyPart(varName: String)
case class FuncKeyPart()

trait  RPartition {
  def partition(keyId: Int, length: Int): Int
}

object RHashPartition extends RPartition{
  def partition(keyId: Int, length: Int): Int = keyId / length
}

trait Formatter {
  def format(pattern: String, parameters : Map[String, String]) : String
}

object DefaultFormatter extends Formatter{
  def format(pattern: String, parameters : Map[String, String]): String = {
    s""
  }
}

object ParameterInput extends FunctionBuilder {
  var placeholderAndParameters =  Map[Any, String]()
  var count = 0
  def apply(children: Seq[Expression]): Expression = {
    count += 1
    val name =children.head.asInstanceOf[Literal].value.asInstanceOf[org.apache.spark.unsafe.types.UTF8String].toString
    val value = children.last.asInstanceOf[Literal].value.asInstanceOf[org.apache.spark.unsafe.types.UTF8String].toString match {
      case  "int" => -998100000 - count
      case "date" => "0000-00-0" + count
    }
    placeholderAndParameters += ( value.toString -> name)
    Literal(value)
  }
}

class SmartSchemaManager {

  val DEFAULT_DATABASE_NAME = "_DEFAULT_"

  var databases = Map.empty[String, DBEntity]
  val conf = new SQLConf
  val catalog: SessionCatalog = new SessionCatalog(newEmptyCatalog(), FunctionRegistry.builtin, conf){
    override protected def makeFunctionBuilder(name: String, functionClassName: String): FunctionBuilder = {
      ParameterInput
    }
    override def lookupRelation(name: TableIdentifier): LogicalPlan = {
      val logicalPlan = super.lookupRelation(name)

      logicalPlan match {
        case SubqueryAlias(alias,child) =>
          if(child.isInstanceOf[CatalogRelation]){
            val realDb = child.asInstanceOf[CatalogRelation].tableMeta.owner
            if(DEFAULT_DATABASE_NAME.equals(child.asInstanceOf[CatalogRelation].tableMeta.database.toUpperCase)) {
              val realTableIdentifier = name.copy(database = Some(realDb))
              val relation = child.asInstanceOf[CatalogRelation]
              SubqueryAlias(alias, relation.copy(tableMeta = relation.tableMeta.copy(identifier = realTableIdentifier)))
            }else {
              logicalPlan
            }

          }else logicalPlan
        case _ => logicalPlan
      }
    }
  }
  catalog.createDatabase(CatalogDatabase(DEFAULT_DATABASE_NAME,DEFAULT_DATABASE_NAME, newUriForDatabase(), Map.empty), ignoreIfExists = false)
  catalog.setCurrentDatabase(DEFAULT_DATABASE_NAME)

  catalog.createFunction(CatalogFunction(FunctionIdentifier("input"),funcClass, Seq.empty), false)

  def registerDatabase(id: String, meta: DBEntity, description: String = null): Unit ={
    var _id = id
    var _description = description

    if(_id == null) _id = meta.id
    if(_description == null) _description = meta.description

    if(meta.id == null && _id != null) meta.id = _id
    if(meta.description == null && _description != null) meta.description = _description

    if(_id == null) throw new IllegalArgumentException("id should not empty !")
    databases += (_id -> meta)

    catalog.createDatabase(CatalogDatabase(_id, _description, newUriForDatabase(), Map.empty), ignoreIfExists = false)

    setCurrentDatabaseIfNecessary(_id)

    if(meta.isInstanceOf[Mysql])  {
      val mysql = meta.asInstanceOf[Mysql]
      val url = s"jdbc:mysql://${mysql.host}:${mysql.port}/${mysql.database}"
      client2.DBClient.registerDatabase(id,
        url,
        mysql.username,
        mysql.password)
    }
  }

  def getDatabase(id: String): DBEntity ={
    databases.get(id).get
  }


  def setCurrentDatabaseIfNecessary(id: String): Unit ={
    val currentDatabase = catalog.getCurrentDatabase
    if(catalog.databaseExists(currentDatabase)) return
//    assert(catalog.databaseExists(currentDatabase), s"'$currentDatabase' database is not exists !")
    catalog.setCurrentDatabase(id)
  }

  def registerTable(dbId: String, table: TableEntity): Unit ={
    if(dbId == null) new IllegalArgumentException("register table: dbId should not empty !")

    val tableName = table match {
      case table: Table  => {
        if (table.dbId == null) table.dbId = dbId
        table.name
      }
      case table: RTable  => {
        if (table.dbId == null) table.dbId = dbId
        table.name
      }
      case table: HTable  => {
        if (table.dbId == null) table.dbId = dbId
        table.name
      }
    }

    assert(databases.get(dbId).isDefined, s"register table: $dbId 's db is not exists !")
    databases.get(dbId).get.addTable(table)

    if(table.isInstanceOf[RTable]) {
      val rTable = table.asInstanceOf[RTable]

      val redisInfo = databases.get(dbId).get.asInstanceOf[Redis]
      val dbs = rTable.dbs
      if(!dbs.dbs.isEmpty) {
        dbs.dbs.foreach(dbNo => RedisClient.registerRedis(s"$dbId-$dbNo",redisInfo.host, redisInfo.port, redisInfo.auth, dbNo))
      }
    }

    val catalogTable = CatalogTable(
      identifier = TableIdentifier(tableName, Some(dbId)),
      tableType = CatalogTableType.EXTERNAL,
      storage = storageFormat.copy(locationUri = Some(Utils.createTempDir().toURI)),
      schema = table.allColumns.foldLeft(new StructType)((schema, col) => {
        schema.add(col.name, col.dataType)
      }),
      stats = Some(CatalogStatistics(sizeInBytes = BigInt(999999))),
      owner = dbId,
      provider = Some(defaultProvider),
      partitionColumnNames = table.allColumns.filter(_.isPartition).map(_.name)
      /*,bucketSpec = Some(BucketSpec(4, Seq("col1"), Nil))*/)
    catalog.createTable(catalogTable, ignoreIfExists = false)
    catalog.createTable(catalogTable.copy(identifier = TableIdentifier(tableName, Some(DEFAULT_DATABASE_NAME))), ignoreIfExists = false)
  }

  val tableInputFormat: String = "org.apache.park.SequenceFileInputFormat"
  val tableOutputFormat: String = "org.apache.park.SequenceFileOutputFormat"
  val defaultProvider: String = "parquet"
  val funcClass = "org.apache.spark.myFunc"


  lazy val storageFormat = CatalogStorageFormat(
    locationUri = None,
    inputFormat = Some(tableInputFormat),
    outputFormat = Some(tableOutputFormat),
    serde = None,
    compressed = false,
    properties = Map.empty)

  def newEmptyCatalog(): ExternalCatalog = new InMemoryCatalog

  def newUriForDatabase(): URI = new URI(Utils.createTempDir().toURI.toString.stripSuffix("/"))
}
