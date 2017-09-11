package ljx.ashin.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.Constant;

import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * Created by AshinLiang on 2017/9/8.
 */
public class NettyServerHandler extends ChannelHandlerAdapter {
    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        System.out.println("有客户端链接过来，IP为:"+remoteAddress.toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        String recieved = getMsg(buf);
        System.out.println("服务器接到信息:"+recieved);

        try {
            ctx.writeAndFlush(getSendByteBuf("apple"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private ByteBuf getSendByteBuf(String message) throws UnsupportedEncodingException {
        byte[] req = message.getBytes("utf-8");
        ByteBuf pingMsg = Unpooled.buffer();
        pingMsg.writeBytes(req);
        return pingMsg;
    }

    /**
     * 从msg中获取信息,使用utf-8编码返回
     * @param buf
     * @return
     */
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
