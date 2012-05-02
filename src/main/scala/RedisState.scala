import scala.collection.mutable.{HashMap => MHashMap}
import java.util.PriorityQueue
import java.nio.ByteBuffer

class RedisState (private val pqs : MHashMap[ByteBuffer, IntHeap[Array[Byte]]]) {

	def this() = this(new MHashMap())

	def flushall() {
		pqs.clear
	}

	def zadd(key : Array[Byte], score : Int, member : Array[Byte]) {
		val bb = ByteBuffer.wrap(key)

		val queue = pqs.getOrElse(bb, {
			val newq = new IntHeap[Array[Byte]]
			pqs(bb) = newq
			newq
		})

		queue.insert(member, score)
 
	}

	def zremrangebyrank(key : Array[Byte]) : Array[Byte] = {
		val bb = ByteBuffer.wrap(key)
		var queue = pqs.get(bb)

		queue match {
			case Some(q) =>
				if (q.size == 0) null else q.pop
			case _ =>
				null
		}
	}

	def zcount(key : Array[Byte]) : Int = {
		val bb = ByteBuffer.wrap(key)

		var queue = pqs.get(bb)

		queue match {
			case Some(q) => q.size()
			case _ => 0
		}
 
	}
}