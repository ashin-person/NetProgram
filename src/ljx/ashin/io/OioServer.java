package ljx.ashin.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 传统的IO通讯
 * Created by AshinLiang on 2017/9/14.
 */
public class OioServer {
    /**
     * 端口
     */
    private int port;

    public OioServer(int port){
        this.port = port;
        initServer();
    }

    /**
     * 初始化客户端
     */
    private void initServer(){
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("服务器启动成功");
            Socket socket = serverSocket.accept();//阻塞点
            System.out.println("有新的客户端进来了");
            while (true){
                InputStream inputStream = socket.getInputStream();
                byte[] data = new byte[1024];
                int len = inputStream.read(data);//阻塞点
                String msg = new String(data,0,len);
                System.out.println("接收到的客户端信息为:"+msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        OioServer oioServer = new OioServer(8765);
    }


}
