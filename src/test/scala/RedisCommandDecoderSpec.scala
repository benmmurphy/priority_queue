import org.scalatest._
import org.scalatest.matchers._
import java.nio.charset._
import org.jboss.netty.buffer._

class RedisCommandDecoderSpec extends FlatSpec with ShouldMatchers {
  it should "parse an INFO command" in {
    val decoder = new RedisCommandDecoder


    val channelBuffer = new ByteBufferBackedChannelBuffer(Charset.forName("US-ASCII").encode("*1\r\n$4\r\nINFO\r\n"))

    val result = decoder.decode(null, null, channelBuffer, RedisCommandDecoderState.ARGS).asInstanceOf[List[Array[Byte]]]
    result.map(new String(_)) should equal(List("INFO"))
  }


  it should "parse ZADD command" in {
  	 val decoder = new RedisCommandDecoder


    val channelBuffer = new ByteBufferBackedChannelBuffer(Charset.forName("US-ASCII").encode("*4\r\n$4\r\nZADD\r\n$3\r\nkey\r\n$2\r\n12\r\n$3\r\nbar\r\n"))

    val result = decoder.decode(null, null, channelBuffer, RedisCommandDecoderState.ARGS).asInstanceOf[List[Array[Byte]]]
    result.map(new String(_)) should equal(List("ZADD", "key", "12", "bar"))

  	
  }

}
