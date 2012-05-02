/*
  children @ 2n+1 and 2n+2 5 -> 11,12
  parent @ (n + 1)/2 - 1
*/

class IntHeap [T : Manifest] {
  private var priorities : Array[Int] = new Array[Int](16)
  private var values : Array[T] = new Array[T](16)

  private var nextInsertionPoint : Int = 0


  def resize[@specialized V : Manifest](array : Array[V], size : Int) = {
    val newArray = new Array[V](size)
    System.arraycopy(array, 0, newArray, 0, array.length)
    newArray
  }

  def min() : (Int, T) = {
    (priorities(0), values(0))
  }

  def size() : Int = {
    nextInsertionPoint
  }

  def swap[@specialized V](array : Array[V], idx1 : Int, idx2 :Int ) {
    val tmp = array(idx1)
    array(idx1) = array(idx2)
    array(idx2) = tmp

  }
  def insert(value : T, priority : Int) {
    if (nextInsertionPoint >= priorities.length) {
      val newSize = priorities.length * 2
      priorities = resize(priorities, newSize)
      values = resize(values, newSize)
    }

    priorities(nextInsertionPoint) = priority
    values(nextInsertionPoint) = value


    var current = nextInsertionPoint

    while (current != 0) {
      val parent = (current + 1) / 2 - 1
      if (priorities(current) < priorities(parent)) {
        swap(priorities, current, parent)
        swap(values, current, parent)
        current = parent
      } else {
        current = 0
      }
    }

    nextInsertionPoint = nextInsertionPoint + 1

  }


  def pop() = {
    if (nextInsertionPoint == 0) {
      throw new RuntimeException("fuckyou")
    }

    val result = values(0)

    priorities(0) = priorities(nextInsertionPoint - 1)
    values(0) = values(nextInsertionPoint - 1)

    values(nextInsertionPoint - 1) = null.asInstanceOf[T]
    priorities(nextInsertionPoint - 1) = 0

    nextInsertionPoint = nextInsertionPoint - 1

    var current = 0
    while(current < nextInsertionPoint) {
      val child1 = 2 * current + 1
      val child2 = 2 * current + 2


      var bestChildPriority = Integer.MAX_VALUE
      var bestChild = -1

      if (child1 < nextInsertionPoint) {
        bestChildPriority = priorities(child1)
        bestChild = child1
      }

      if (child2 < nextInsertionPoint) {
        if (priorities(child2) < bestChildPriority) {
          bestChild = child2
          bestChildPriority = priorities(child2)
        }
      }

      if (bestChild != -1 && priorities(current) > bestChildPriority) {
          swap(priorities, current, bestChild)
          swap(values, current, bestChild)
          current = bestChild
      } else {
        current = nextInsertionPoint
      }

    }
    

    result

  }
}