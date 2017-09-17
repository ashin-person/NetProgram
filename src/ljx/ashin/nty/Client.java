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

/**
 * Created by AshinLiang on 2017/9/16.
 */
public class Client {

    private int port;

    private String ip;

    public Client(int port,String ip){
        this.port = port;
        this.ip = ip;
        initClient();
    }

    /**
     * 初始化客户端配置
     */
    private void initClient(){
        Bootstrap bootstrap = new Bootstrap();

        //线程池
        EventLoopGroup worker = new NioEventLoopGroup();
        //绑定线程池
        bootstrap.group(worker);
        //设置对应的channel
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new StringEncoder());
                channel.pipeline().addLast(new StringDecoder());
                channel.pipeline().addLast(new MyClientHandler());
            }
        });

        try {
            ChannelFuture channelFuture =bootstrap.connect(this.ip,this.port).sync();
            Channel channel = channelFuture.channel();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            while (true){
                System.out.println("请输入内容:");
                try {
                    String msg = bufferedReader.readLine();
                    channel.writeAndFlush(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        Client client = new Client(8765,"127.0.0.1");
    }

}
