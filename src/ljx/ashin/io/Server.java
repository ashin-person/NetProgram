package ljx.ashin.io;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 传统的io通信
 * Created by AshinLiang on 2017/9/11.
 */
public class Server {
    /**
     * 端口
     */
    private int port;

    public Server(int port){
        this.port = port;
        initParams();
    }

    /**
     * 初始化参数配置
     * @throws IOException
     */
    private void initParams() {
       /* ServerSocket serverSocket = null;
        Socket socket = null;*/
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("服务器启动成功");
            final Socket socket = serverSocket.accept();//阻塞，当有新的客户端请求过来的时候，调用此处的代码

            while (true){
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("有新的客户端过来了");
                        socketHandler(socket);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        }
    }

    /**
     * socket的处理类
     * @param socket
     */
    private void socketHandler(Socket socket){
        InputStream inputStream = null;
        byte[] con = new byte[1024];
        try {
            inputStream =  socket.getInputStream();

            while (true){
                int len = inputStream.read(con);
                System.out.println("获得客户端的信息为:"+new String(con,0,len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                System.out.println("socket关闭");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8765);
    }
}
