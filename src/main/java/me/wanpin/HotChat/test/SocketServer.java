package me.wanpin.HotChat.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SocketServer {
    public static void main(String[] args) {
        ServerSocket server = null;
        Map<String, Socket> CLENT_MAP = new HashMap<>();
        try {
            server = new ServerSocket(1024);
            System.out.println("The server is working, wait to connect the client server.");
            while (true){
                Socket socket = server.accept();
                String ip = socket.getInetAddress().getHostAddress();
                System.out.println("The Client server connect ip: "+ip+"port is: "+socket.getPort());
                String clientKey = ip + socket.getPort();
                CLENT_MAP.put(clientKey,socket);

                new Thread(() -> {
                    while (true){
                        try {
                            InputStream inputStream = socket.getInputStream();
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"GBK");
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String readData = bufferedReader.readLine();
                            System.out.println("Receive message from Client server-> "+readData);

                            CLENT_MAP.forEach((k,v) -> {
                                try {
                                    OutputStream outputStream = v.getOutputStream();
                                    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream, "GBK"));
                                    printWriter.println(socket.getPort() + ":" + readData);
                                    printWriter.flush();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            });
//                            OutputStream outputStream = socket.getOutputStream();
//                            PrintWriter printWriter = new PrintWriter(outputStream);
//                            printWriter.println("This is Server, I got your message: " + readData);
//                            printWriter.flush();
                        }catch (Exception e){
                            e.printStackTrace();
                            break;
                        }
                    }
                }).start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
//    @RequestMapping("/test")
//    public String test(){
//        return "Successful!!! Swan !!!";
//
//    }
}
