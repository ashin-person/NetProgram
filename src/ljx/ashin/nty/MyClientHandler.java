package ljx.ashin.nty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by AshinLiang on 2017/9/17.
 */
public class MyClientHandler extends SimpleChannelInboundHandler<String>{
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("接收到的服务器端的信息为："+s);
    }
}
