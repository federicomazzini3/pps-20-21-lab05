package u05lab.code

import u05lab.code.PerformanceUtils.{MeasurementResults, measure}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration
import scala.collection.mutable._
import scala.collection.immutable._
import scala.util.Random

object PerformanceUtils {
  case class MeasurementResults[T](result: T, duration: FiniteDuration)(msg: String) extends Ordered[MeasurementResults[_]] {
    override def compare(that: MeasurementResults[_]): Int = duration.toNanos.compareTo(that.duration.toNanos)

    def print(): Unit = {
      if(!msg.isEmpty) println(msg + " -- " + duration.toNanos + " nanos; " + duration.toMillis + "ms")
    }
  }

  def measure[T](msg: String)(expr: => T): MeasurementResults[T] = {
    val startTime = System.nanoTime()
    val res = expr
    val duration = FiniteDuration(System.nanoTime()-startTime, TimeUnit.NANOSECONDS)
    MeasurementResults(res, duration)(msg)
  }

  def measure[T](expr: => T): MeasurementResults[T] = measure("")(expr)
}

object MyAlias{
  type Map[A, +B] = scala.collection.immutable.Map[A, B]
}

object CollectionsTest extends App {
  import MyAlias._
  val min = 1
  val max = 1000000
  val rand: Int = Random.between(min, max)
  val contains: ListBuffer[MeasurementResults[Boolean]] = ListBuffer()
  val last: ListBuffer[MeasurementResults[Int]] = ListBuffer()
  val retrieveNth: ListBuffer[MeasurementResults[Int]] = ListBuffer()

  /* Linear sequences: List, ListBuffer */
  var list: List[Int] = (min to max).toList
  var mutableList: ListBuffer[Int] = ListBuffer.from(list)
  //contains
  contains += measure("list contains"){list contains rand}
  contains += measure("mutable list contains"){mutableList contains rand}
  //last
  last += measure("list last"){list.last}
  last += measure("mutable list last"){mutableList.last}
  //get first n element
  retrieveNth += measure("list get n-th element"){list(rand)}
  retrieveNth += measure("mutable list get n-th element"){mutableList(rand)}

  /* Indexed sequences: Vector, Array, ArrayBuffer */
  var vector: Vector[Int] = (min to max).toVector
  var array: Array[Int] = (min to max).toArray
  val arrayList: ArrayBuffer[Int] = ArrayBuffer.from(min to max)
  //contains
  contains += measure("vector contains"){vector contains rand}
  contains += measure("array contains"){array contains rand}
  contains += measure("arrayBuffer contains"){arrayList contains rand}
  //last
  last += measure("vector last"){vector.last}
  last += measure("array last"){array.last}
  last += measure("arrayBuffer last"){arrayList.last}
  //get n-th element
  retrieveNth += measure("vector get n-th element"){vector(rand)}
  retrieveNth += measure("array get n-th element"){array(rand)}
  retrieveNth += measure("arrayBuffer get n-th element"){arrayList(rand)}

  /* Sets */
  val set = (min to max).toSet
  //contains
  contains += measure("set contains"){set contains rand}
  //last
  last += measure("set last"){list.last}

  /* Maps */
  val map: Map[Int,Int] = (min to max).map(x => (x, x+1)).toMap
  //contains
  contains += measure("map contains"){map contains rand}
  //last
  last += measure("map last"){map.last._1}

  /* Comparison */
  println("\nfinal result for contains");
  contains.sorted foreach(_.print())
  println("\nfinal result for last")
  last.sorted foreach(_.print())
  println("\nfinal result for getting the " + rand + " element")
  retrieveNth.sorted foreach(_.print())

}