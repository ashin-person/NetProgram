package ljx.ashin.aio;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务器端
 * Created by AshinLiang on 2017/9/6.
 */
public class Server {
    //线程池
    private ExecutorService executorService;
    //线程组
    private AsynchronousChannelGroup asynchronousChannelGroup;
    //服务器通道
    public AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public Server(int port){
        try {
            //创建一个缓冲线程池
            executorService = Executors.newCachedThreadPool();
            //创建线程组
            asynchronousChannelGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService,1);
            //创建服务器通道
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open(asynchronousChannelGroup);
            //进行绑定
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("socket start ,port:"+port);
            //进行阻塞
            asynchronousServerSocketChannel.accept(this,new ServerCompletionHandler());
            //进行阻塞，不让服务器停止
            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8765);
    }
}
