import org.jboss.netty.channel._
import org.jboss.netty.handler.logging._
import org.jboss.netty.logging._

class RedisPipelineFactory (private val state : RedisState) extends ChannelPipelineFactory {

  def getPipeline() : ChannelPipeline = {
    return Channels.pipeline(/*new LoggingHandler(InternalLogLevel.INFO, true), */new RedisCommandDecoder(), new RedisCommandHandler(state), new RedisCommandEncoder())
  }
}
