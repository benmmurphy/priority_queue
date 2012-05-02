import org.jboss.netty.buffer._
import org.jboss.netty.channel._
import org.jboss.netty.handler.codec.replay._
import java.nio.charset._
import RedisCommandDecoderState._

class RedisCommandDecoder extends ReplayingDecoder[RedisCommandDecoderState](ARGS) {
  type Result = List[Array[Byte]]

  val ASCII = Charset.forName("US-ASCII")
  var args : Int = 0
  var argsReceived : Int = 0
  var argValues = List[Array[Byte]]()


  override def decode(ctx: ChannelHandlerContext, channel: Channel, buffer: ChannelBuffer,
                      state: RedisCommandDecoderState) : Object = {
    val result = state match {
      case ARGS => args(buffer)
      case ARGLEN => arglen(buffer)
      case ARG => arg(buffer)
    }

    result
   
  }

  def arg(buffer: ChannelBuffer) : Result = {
    buffer.readBytes(argValues.head)
    argsReceived += 1
    assert(buffer.readByte() == '\r')
    assert(buffer.readByte() == '\n')
    if (argsReceived < args) {
      checkpoint(ARGLEN)
      arglen(buffer)
    } else {
      finished
    } 
  }

  def finished : Result = {
    argsReceived = 0
    checkpoint(ARGS)
    val result = argValues.reverse
    argValues = List[Array[Byte]]()
    result
  }
 
  def arglen(buffer: ChannelBuffer) : Result = {
    val byte = buffer.readByte()
    assert((byte & 0xFF) == '$')
    val lfPos = buffer.bytesBefore('\n'.toByte)
    if (lfPos == -1) {
      null
    } else {
      val argLen = buffer.toString(buffer.readerIndex(), lfPos - 1, ASCII).toInt
      buffer.readerIndex(buffer.readerIndex() + lfPos + 1)
      argValues = new Array[Byte](argLen) :: argValues
      checkpoint(ARG)
      arg(buffer)
    }
  }
 
  def args(buffer: ChannelBuffer) : Result = {
    val byte = buffer.readByte()
    assert((byte & 0xFF) == '*')
    
    val lfPos = buffer.bytesBefore('\n'.toByte)
    if (lfPos == -1) {
      null
    } else {
      args = buffer.toString(buffer.readerIndex(), lfPos - 1, ASCII).toInt
      buffer.readerIndex(buffer.readerIndex() + lfPos + 1)
      if (args <= 0) {
        finished
      } else {
        checkpoint(ARGLEN)
        arglen(buffer)
      }
    }
  } 

}
