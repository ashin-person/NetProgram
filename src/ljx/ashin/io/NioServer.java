package ljx.ashin.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * nio的服务器
 * Created by AshinLiang on 2017/9/12.
 */
public class NioServer {
    /**
     * 端口
     */
    private int port;
    /**
     * 管道管理者
     */
    private Selector selector;

    public NioServer(int port){
        this.port = port;
        initServer();
        listenServer();
        System.out.println("服务器启动成功");
    }

    /**
     * 初始化服务器
     */
    private void initServer(){
        try {
            //获取ServerSocketChannel
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //非阻塞
            serverSocketChannel.configureBlocking(false);
            //把管道对应的serverSocket绑定到端口中
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            //获得一个管道管理者
            this.selector = Selector.open();
            // 将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后
            // 当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。
            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有在进行处理
     */
    private void listenServer(){
        System.out.println("开始监控服务端");
        while (true){//轮询访问selector
            try {
                //当注册事件到达时，返回，否则一直阻塞
                this.selector.select();
                //获取selector上的迭代器，选择的事件为注册
                Iterator<SelectionKey> its = selector.selectedKeys().iterator();
                while (its.hasNext()){
                    SelectionKey selectionKey = its.next();
                    //删除已经重复的key
                    its.remove();
                    handler(selectionKey);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 统一的处理器
     * @param selectionKey
     */
    private void handler(SelectionKey selectionKey){
        if (selectionKey.isAcceptable()){//客户端请求链接事件
            handlerAcception(selectionKey);
        }else if(selectionKey.isReadable()){//获得了可读的事件
            handlerReader(selectionKey);
        }
    }

    /**
     * 请求读取数据的处理器
     * @param selectionKey
     */
    private void handlerReader(SelectionKey selectionKey){
        //得到事件发生的Socket通道
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            //创建读取的缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int read = socketChannel.read(byteBuffer);
            if (read>0){
                byte[] data = byteBuffer.array();
                String msg = new String(data).trim();
                System.out.println("接收到客户端的信息:"+msg);
                //写回数据
                ByteBuffer sendMsgByteBuffer = ByteBuffer.wrap("服务器正在处理您的信息".getBytes());
                socketChannel.write(sendMsgByteBuffer);
            }else {
                System.out.println("客户端关闭");
                selectionKey.cancel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求链接事件的处理器
     * @param selectionKey
     */
    private void handlerAcception(SelectionKey selectionKey){
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        try {
            //获得和客户端链接的通道(也就是传统IO中的的socket，即套接字)
            SocketChannel socket = serverSocketChannel.accept();
            //设置成非阻塞
            socket.configureBlocking(false);
            System.out.println("新的客户端链接进来了");
            //在和客户端链接成功之后，为了能够接收到客户端传送过来的数据，需要给通道设置读的权限
            socket.register(this.selector,SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NioServer server = new NioServer(8765);
    }

}
