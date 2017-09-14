package ljx.ashin.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 改进之后的传统IO通讯--使用多线程
 * Created by AshinLiang on 2017/9/14.
 */
public class MultiIoServer {
    /**
     * 端口
     */
    private int port;

    public MultiIoServer(int port){
        this.port = port;
        initMultilIoServer();
    }

    /**
     * 初始化MultiIoServer
     */
    private void initMultilIoServer(){
        //线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            final ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("服务器启动成功");
            while (true){
              final Socket  socket = serverSocket.accept();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("新来了一个客户端");
                            handlerSocket(socket);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * socket的处理器
     * @param socket
     */
    private void handlerSocket(Socket socket){
        try {
            while (true){
                InputStream inputStream = socket.getInputStream();
                byte[] data = new byte[1024];
                int len = inputStream.read(data);
                String msg = new String(data,0,len);
                System.out.println("接收到的客户端信息为:"+msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MultiIoServer multiIoServer = new MultiIoServer(8765);
    }
}
