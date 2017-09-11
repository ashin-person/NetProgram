package ljx.ashin.nio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by AshinLiang on 2017/9/8.
 */
public class NettyServerBoostrap {
    private int port;//端口

    public NettyServerBoostrap(int port){
        this.port = port;
        bind();
    }

    /**
     * 初始化配置
     */
    private void bind(){

        EventLoopGroup boss = new NioEventLoopGroup();//负责网络连接
        EventLoopGroup worker = new NioEventLoopGroup();//负责处理数据

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //绑定需要处理的线程池
            serverBootstrap.group(boss,worker);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.SO_BACKLOG,1024);//连接数
            serverBootstrap.option(ChannelOption.TCP_NODELAY,true);//不延迟，消息立即发送
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);//长连接
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline channelPipeline = socketChannel.pipeline();
                    channelPipeline.addLast(new NettyServerHandler());
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            if (channelFuture.isSuccess()){
                System.out.println("Netty服务器启动成功,端口号:"+port);
            }
            //关闭
            Thread.sleep(600*10000);
            channelFuture.channel().close().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        NettyServerBoostrap server = new NettyServerBoostrap(8765);

    }
}
