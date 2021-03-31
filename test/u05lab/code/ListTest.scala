package u05lab.code

import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.{BeforeEach, Test}

class ListTest {

  @Test
  def testIncremental() {
    assert(true)
  }

  @Test
  def testZipRight() {
    //costruire lista di coppie, i primi elem sono la lista originale

    val l = List("a","b","c")
    assertEquals(List.nil, List.nil.zipRight)
    assertEquals(List(("a",0), ("b",1), ("c",2)), l.zipRight)
  }

  @Test
  def testPartition(): Unit ={
    val l = List(10,20,11,21)
    val l2 = List(10,20,30)
    val l3 = List(0,-2,4,-5,-4,8)
    assertEquals((List(10,20), List(11,21)), l.partition(e =>  e % 2 == 0))
    assertEquals((List(10,20,30), List.nil), l2.partition(e =>  e % 2 == 0))
    assertEquals((List(0,4,8), List(-2,-5,-4)), l3.partition(e =>  e >= 0))
  }

  @Test
  def testSpan: Unit ={
    val l  = List(10, 20, 11, 20)
    val l2 = List(10,20)
    assertEquals((List(10,20), List(11,20)), l.span(e => e % 2 == 0))
    assertEquals((List(10,20), List.nil), l2.span(e => e % 2 == 0))
    assertEquals((List.nil, List(10, 20, 11, 20)), l.span(e => e % 2 != 0))
  }

  @Test
  def testReduce: Unit = {
    val l = List (10,20,30,40)
    val l2 = List.nil[Int]
    assertEquals(100, l.reduce(_+_))
    assertThrows(classOf[UnsupportedOperationException], () => l2.reduce(_+_))
  }

  @Test
  def testTakeRight: Unit ={
    val l = List(10,20,30)
    val l2 = List.nil[Int]
    assertEquals(List(20,30), l.takeRight(2))
    assertEquals(List(10,20,30), l.takeRight(20))
    assertThrows(classOf[UnsupportedOperationException], () => l.takeRight(-5))
    assertEquals(List.nil[Int], l2.takeRight(2))
  }

}