package ljx.ashin.nty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * netty5多连接客户端
 * Created by AshinLiang on 2017/9/17.
 */
public class MutilClient {
    /**
     * 端口
     */
    private int port;
    /**
     * 端口
     */
    private String ip;
    /**
     * 启动辅助类
     */
    private Bootstrap bootstrap;
    /**
     * 连接通道的列表
     */
    private List<Channel> channelList;
    /**
     * 计数
     */
    private AtomicInteger index = new AtomicInteger();
    /**
     * 连接数
     */
    private int count;

    public MutilClient(int port,String ip,int count){
        this.port = port;
        this.ip = ip;
        this.count = count;
        initClient();
    }

    private void initClient(){
        bootstrap = new Bootstrap();
        channelList = new ArrayList<Channel>();

        /**
         * 线程池
         */
        EventLoopGroup worker = new NioEventLoopGroup();

        bootstrap.group(worker);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new StringDecoder());
                channel.pipeline().addLast(new StringEncoder());
                channel.pipeline().addLast(new MyClientHandler());
            }
        });

        for (int i = 0; i < count; i++) {
            ChannelFuture channelFuture = bootstrap.connect(ip,port);
            channelList.add(channelFuture.channel());
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            try {
                System.out.println("请输入要发送的内容:");
                String msg = bufferedReader.readLine();
                nextChannel().writeAndFlush(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取下一个可用的channel
     * @return
     */
    public Channel nextChannel(){
        Channel channel = getFirstActiveChannel(0);
        return channel;
    }

    private Channel getFirstActiveChannel(int count){
        Channel channel =  channelList.get(Math.abs(index.getAndIncrement()%channelList.size()));
        //判断是否断开了连接
        if (!channel.isActive()){
            reConnect(channel);
            if (count>channelList.size()){
                throw new RuntimeException("没有可用的连接");
            }
            channel = getFirstActiveChannel(count+1);
        }

        return channel;
    }

    /**
     * 重新连接
     * @param channel
     */
    private void reConnect(Channel channel){
        //判断是否还在这个列表中
        if (channelList.indexOf(channel)==-1){
            return;
        }else {
            Channel reConnectChannel = bootstrap.connect(ip,port).channel();
            channelList.set(channelList.indexOf(channel),reConnectChannel);
        }
    }

    public static void main(String[] args) {
        MutilClient mutilClient = new MutilClient(8765,"192.168.0.105",5);
    }

}
