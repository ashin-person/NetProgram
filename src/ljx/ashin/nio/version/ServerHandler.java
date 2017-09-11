package ljx.ashin.nio.version;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;

/**
 * netty服务器端的处理类
 * Created by AshinLiang on 2017/9/9.
 */
public class ServerHandler extends ChannelHandlerAdapter{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        String strMsg = parseMsg(buf);
        System.out.println("接收到客户端的信息："+strMsg);
        ctx.writeAndFlush(getSendMsg("服务器已经处理了你发送的信息"));
    }

    private String parseMsg(ByteBuf buf){
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            return new String(con,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将需要发送的String类型的msg转为ByteBuf类型
     * @param msg
     * @return
     */
    private ByteBuf getSendMsg(String msg){
        try {
            byte[] con = msg.getBytes("utf-8");
            ByteBuf buf = Unpooled.buffer();
            buf.writeBytes(con);
            return buf;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete method");
    }

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        System.out.println("bind method");
    }
}
