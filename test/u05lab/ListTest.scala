package u05lab.code

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions._

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

    assertEquals((List(10,20), List(11,21)), l.partition(e =>  e % 2 == 0))
  }


}