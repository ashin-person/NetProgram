package ljx.ashin.nio.version;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.UnsupportedEncodingException;

/**
 * ByteBuf
 * Created by AshinLiang on 2017/9/10.
 */
public class ByteBufUtils {

    public static String parseMsg(ByteBuf buf){
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
    public static ByteBuf getSendMsg(String msg){
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
}
