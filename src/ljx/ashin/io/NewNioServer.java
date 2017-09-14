package ljx.ashin.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Nio非阻塞IO通讯
 * Created by AshinLiang on 2017/9/14.
 */
public class NewNioServer {
    /**
     * 端口号
     */
    private int port;
    /**
     * 管道管理者
     */
    private Selector selector;

    private ExecutorService executorService;

    public NewNioServer(int port){
        this.port = port;
        initNewNioServer();
        listenNewNioServer();
    }

    /**
     * 初始化服务器参数
     */
    private void initNewNioServer(){
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //非阻塞
            serverSocketChannel.configureBlocking(false);
            //获得channel上对应的serverSocket绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(this.port));
            this.selector = Selector.open();
            //获得channel上对应的serverSocket
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            executorService = Executors.newCachedThreadPool();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监控服务器
     */
    private void listenNewNioServer(){
        while (true){
            try {
                this.selector.select();//阻塞,当有请求过来的时候，往下走
                Iterator<SelectionKey> its = selector.selectedKeys().iterator();
                while (its.hasNext()){
                    final SelectionKey selectionKey = its.next();
                    its.remove();//删掉已经使用的key，以免重复

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            handler(selectionKey);
                        }
                    });


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 请求处理器
     * @param selectionKey
     */
    private void handler(SelectionKey selectionKey){
        if (selectionKey.isAcceptable()){//请求连接
            acceptionHandler(selectionKey);
        }else if (selectionKey.isReadable()){//客户端发送信息到服务器
            readerHandler(selectionKey);
        }
    }

    /**
     * 客户端请求连接的处理器
     * @param selectionKey
     */
    private void acceptionHandler(SelectionKey selectionKey){
        //获取该key上对应的管道
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        System.out.println("有新的客户端连接进来");
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            //设置成非阻塞的
            socketChannel.configureBlocking(false);
            //设置读的权限
            socketChannel.register(this.selector,SelectionKey.OP_READ);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 客户端发送信息到服务器的处理器
     * @param selectionKey
     */
    private void readerHandler(SelectionKey selectionKey){

        try {
            //获取对应的套接字通道
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            //读取客户端传送过来的信息
            byte[] bytes = new byte[1024];
            ByteBuffer byteBuf = ByteBuffer.allocate(1024);
            socketChannel.read(byteBuf);
            bytes = byteBuf.array();
            String msg = new String(bytes,"utf-8");
            System.out.println("接收到的客户端端的信息为:"+msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        NewNioServer newNioServer = new NewNioServer(8765);
    }
}
