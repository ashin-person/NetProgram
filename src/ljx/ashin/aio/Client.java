package ljx.ashin.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

/**
 * Created by AshinLiang on 2017/9/6.
 */
public class Client implements Runnable {

    private AsynchronousSocketChannel asynchronousSocketChannel;

    public Client() throws IOException {
        this.asynchronousSocketChannel = AsynchronousSocketChannel.open();
    }

    public void connect(){
        asynchronousSocketChannel.connect(new InetSocketAddress("127.0.0.1",8765));
    }

    @Override
    public void run() {

    }

    public void write(String request){
        try {
            asynchronousSocketChannel.write(ByteBuffer.wrap(request.getBytes())).get();
            read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void read() {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        try {
            asynchronousSocketChannel.read(buf).get();
            buf.flip();
            byte[] respByte = new byte[buf.remaining()];
            buf.get(respByte);
            System.out.println(new String(respByte,"utf-8").trim());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
