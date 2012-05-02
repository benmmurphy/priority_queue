import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

object IntHeapSpecification extends Properties("IntHeap") {


  property("insert-all-then-pop-all") = forAll((priorities: List[Int]) => {
  	val heap = new IntHeap[String]()
  	for (priority <- priorities) {
  		heap.insert(priority.toString, priority)
  	}
    
    var popped_priorities = List[Int]()
    while (heap.size > 0) {
    	popped_priorities ::= heap.pop.toInt
    }

    priorities.sorted == popped_priorities.reverse
  })

}