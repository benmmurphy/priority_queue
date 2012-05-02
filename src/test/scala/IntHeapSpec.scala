import org.scalatest._
import org.scalatest.matchers._


class IntHeapSpec extends FlatSpec with ShouldMatchers {
  it should "maintain the mininum heap property" in {
    val heap = new IntHeap[String]
    for (val i <- 17 to (1, -1)) {
    	heap.insert(i.toString, i)
    	heap.min()._2.should(equal(i.toString))
    }

    for (val i <- 1 to 17) {
    	heap.pop().should(equal(i.toString))
    }
  }

  it should "pop in the correct order" in {
    val heap = new IntHeap[String]()
    heap.insert("0", 0)
    heap.insert("0", 0)
    heap.insert("1", 1)

    heap.pop.should(equal("0"))
    heap.pop.should(equal("0"))
    heap.pop.should(equal("1"))
  }

}
