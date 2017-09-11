package ljx.ashin.nio.version;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty中的服务器端
 * Created by AshinLiang on 2017/9/9.
 */
public class Sever {
    /**
     * 端口
     */
    private int port;

    public Sever(int port){
        this.port = port;
        initParams();
    }

    /**
     * 设置服务器的相关参数
     */
    private void initParams(){
        //监听端口，接收新加的客户端
        EventLoopGroup boss = new NioEventLoopGroup();
        //处理读写请求
        EventLoopGroup worker = new NioEventLoopGroup();

        //服务器端的启动辅助类
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss,worker);
        bootstrap.channel(NioServerSocketChannel.class);//指定使用NioServerSocketChannel类来举例说明一个新的Channel如何接收进来的连接
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {//ChannelInitializer是一个特殊的处理类，他的目的是帮助使用者配置一个新的Channel,这些新的Channel
            //就是对应你的逻辑处理
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ServerHandler());//当业务复杂的时候，可以增加更多的处理类到pipline上

            }
        });
        bootstrap.option(ChannelOption.SO_BACKLOG,128);//连接数
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);//长连接
        bootstrap.childOption(ChannelOption.TCP_NODELAY,true);//不延迟，立即发送消息
        try {
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            if (channelFuture.isSuccess()){
                System.out.println("===服务器启动成功===");
            }
            Thread.sleep(600*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Sever sever = new Sever(8765);
    }
}
