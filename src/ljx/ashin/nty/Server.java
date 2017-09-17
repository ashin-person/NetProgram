package ljx.ashin.nty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * netty5.0的客户端
 * Created by AshinLiang on 2017/9/16.
 */
public class Server {
    /**
     * 端口
     */
    private int port;

    public Server(int port){
        this.port = port;
        initServer();
    }

    /**
     * 初始化Server服务器
     */
    private void initServer(){
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        //启动的辅助类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //设置两个线程池
        serverBootstrap.group(boss,worker);
        //设置线程队列的大小
        serverBootstrap.option(ChannelOption.SO_BACKLOG,1024);
        //一直保持连接
        serverBootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        //关闭延迟发送
        serverBootstrap.option(ChannelOption.TCP_NODELAY,true);
        //设置管道类型
        serverBootstrap.channel(NioServerSocketChannel.class);
        //设置处理器
        serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new StringDecoder());
                channel.pipeline().addLast(new StringEncoder());
                channel.pipeline().addLast(new MyServerHandler());
            }
        });

        ChannelFuture channelFuture = null;
        try {
            channelFuture = serverBootstrap.bind(this.port).sync();
            if (channelFuture.isSuccess()){
                System.out.println("服务器启动成功");
            }
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
