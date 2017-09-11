package ljx.ashin.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by AshinLiang on 2017/9/8.
 */
public class NettyClientHandler extends ChannelHandlerAdapter {

    private ByteBuf firstMsg;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] data =  "服务器，给我一个apple".getBytes();
        firstMsg = Unpooled.buffer();
        firstMsg.writeBytes(data);
        ctx.writeAndFlush(firstMsg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        String rev = getMsg(byteBuf);
        System.out.println("客户端接收到的服务器端的消息为："+rev);
    }

    private String getMsg(ByteBuf buf){
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            return new String(con,"utf-8");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
