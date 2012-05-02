import org.jboss.netty.channel._
import java.util.Arrays
import java.util.Locale

object RedisCommandHandler {

  val ZADD = "ZADD".getBytes("US-ASCII")
  val ZREMRANGEBYRANK = "ZREMRANGEBYRANK".getBytes("US-ASCII")
  val ZCOUNT = "ZCOUNT".getBytes("US-ASCII")

}

import RedisCommandHandler._

class RedisCommandHandler (private val state : RedisState ) extends SimpleChannelUpstreamHandler {
  override def messageReceived(context: ChannelHandlerContext, messageEvent: MessageEvent) {
    val channel = context.getChannel()
    val message = messageEvent.getMessage().asInstanceOf[List[Array[Byte]]]


    val command =  new String(message.head).toLowerCase(Locale.US)
    val restCommand = message.tail

    command match {
      case "zadd" =>
        val (key :: score :: member :: Nil) = restCommand
        state.zadd(key, new String(score).toInt, member)
        channel.write(1)
      case "zcount" =>
        val (key :: Nil) = restCommand
        channel.write(state.zcount(key))
      case "zremrangebyrank" =>
        val (key :: _Rest) = restCommand
        val result = state.zremrangebyrank(key)
        channel.write(if (result == null) None else result)
      case "flushall" =>
        state.flushall()
        channel.write(1)
    }

  }
}
