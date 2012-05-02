import java.net._
import org.jboss.netty.bootstrap._
import java.util.concurrent._
import org.jboss.netty.channel.socket.nio._

object RedisServer {
  def main(args: Array[String]) {
    val channelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), 
      Executors.newCachedThreadPool(), 1)
    val bootstrap = new ServerBootstrap(channelFactory);
    bootstrap.setPipelineFactory(new RedisPipelineFactory(new RedisState()))
    bootstrap.bind(new InetSocketAddress(6380))
  }
}
