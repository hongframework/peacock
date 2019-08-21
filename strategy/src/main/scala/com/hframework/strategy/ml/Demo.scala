package com.hframework.strategy.ml

/**
 * Created by zhangquanhong on 2017/7/10.
 */
object Demo {
  def main(args: Array[String]) {
//    println("123")

    val users = List(
      User(1,"张三", 23),
      User(3,"王二", 21),
      User(2,"李四", 24),
      User(4,"麻子", 23)
    )

    var orders = List(
      Order(1,1,50),
      Order(2,2,150),
      Order(3,3,52),
      Order(4,2,70)
    )

    println("select * from user; => " + users)
    println("select * from user limit 1; => " + users.take(1))
    println("select * from user order by id; => " + users.sortBy(_.id))
    println("select * from user where age > 22; => " + users.filter(_.age > 22))
    println("select max(age) from user; => "  + users.aggregate(0)((a, b) => Math.max(a,  b.age), null))
    println("select age, count(*) from user group by age; => "
      + users.groupBy(_.age).mapValues(_.aggregate(0)(_ + _.age, null)))

    println("select * from user u where not exists (select 1 from order o where o.user_id = u.id); => "
      + users.filterNot(u => orders.exists(_.userId == u.id)))

    println("select max(age),min(age),sum(age),avg(age),count(age) from user; => "  +
      users.aggregate(Aggregate())
      ((aggregate, user) => aggregate.from(user.age),
        null))

    println("select max(age),min(age),sum(age),avg(age),count(age) from user; => "  +
      users.aggregate(Aggregate())((a, b) => a.from(b.age), null).toSeq)

    println("select * from user u, order o where u.id = o.userId; => " +  users.flatMap(u => {
      orders.filter(_.userId == u.id).map(order => (order.id, order.userId, u.name, u.age, order.amount))
    }))


    //    println("select max(age),min(age),sum(age),avg(age),count(age) from user; => "  +
    //      users.aggregate((0,1000,0,0,0.0))
    //      ((aggregate, user) => (
    //        Math.max(aggregate._1, user.age),
    //        Math.min(aggregate._2, user.age),
    //        aggregate._3 + user.age,
    //        aggregate._4 + 1,
    //        BigDecimal(aggregate._3 + user.age)./(BigDecimal(aggregate._4 + 1)).doubleValue()),
    //        null))

//    println("select max(age) from user; => "  +
//      users.reduce((total, user) => {
//        total.age += user.age
//        total
//      }).age)

//    println("select max(age) from user; => "  +
//      users.fold(User(0,null,0))((u1, u2) => {
//        u1.age += u2.age
//        u1
//      }).age)

    //
//    val list = List(1,2,3,4, 7,3,2, 10)
//    println(list)
//    println(list.distinct)
//    println(list.distinct.filter(_ > 2))
//    println(list.distinct.max)
//    println(list.distinct.sum)
//    println(list.distinct.sorted)
//    println(list.distinct.take(2))
  }

  case class Aggregate(var max:Long=Long.MinValue,var min:Long = Long.MaxValue,var count:Long = 0, var sum:Long=0){
    def from(target: Long):Aggregate ={
      max = Math.max(max, target)
      min = Math.min(min, target)
      sum += target
      count += 1
      this
    }

    def avg():Double = {
      BigDecimal(sum)./(BigDecimal(count)).doubleValue()
    }

    def toSeq():(Long, Long, Long, Long, Double) = {
      (max, min, count, sum, avg)
    }
  }


  case class User(var id:Int, var name:String, var age:Int)

  case class Order(var id: Int, var userId: Int, var amount: Float)


  case class Query(var tableName : String) {
    def map():Query = this
  }
}
