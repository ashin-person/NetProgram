package ljx.ashin.nty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by AshinLiang on 2017/9/16.
 */
public class MyServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        String msg = s;
        System.out.println("接收到的客户端的信息为:"+msg);

        channelHandlerContext.writeAndFlush("嗨");
    }
}
