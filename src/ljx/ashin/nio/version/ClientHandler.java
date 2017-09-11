package ljx.ashin.nio.version;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by AshinLiang on 2017/9/10.
 */
public class ClientHandler extends ChannelHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        String strMsg = ByteBufUtils.parseMsg(byteBuf);
        System.out.println("客户端得到的信息:"+strMsg);

        ctx.writeAndFlush(ByteBufUtils.getSendMsg("写入内容"));

    }
}
