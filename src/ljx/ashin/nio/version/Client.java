package ljx.ashin.nio.version;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * netty的客户端
 * Created by AshinLiang on 2017/9/10.
 */
public class Client {
    /**
     * 端口
     */
    private int port;
    /**
     * ip
     */
    private String ip;

    public Client(int port,String ip){
        this.port = port;
        this.ip = ip;
        initParams();

    }

    /**
     * 初始化参数配置
     */
    public void initParams(){

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.remoteAddress(ip,port);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ClientHandler());
            }
        });
        try {
            ChannelFuture channelFuture = bootstrap.connect(ip,port).sync();
            if (channelFuture.isSuccess()){
                System.out.println("客户端成功连接上");
            }
//            bootstrap.connect(ip,port)
            Thread.sleep(600*1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client(8765,"127.0.0.1");
    }


}
