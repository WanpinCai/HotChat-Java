package me.wanpin.HotChat.test;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    public static void main(String[] args) {
        Socket socket = null;
        try{
            socket = new Socket("127.0.0.1",1024);

            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            System.out.println("Please enter the content:");

            new Thread(()->{
                while (true){
                    Scanner scanner = new Scanner(System.in);
                    String input = scanner.nextLine();
                    try {
                        printWriter.println(input);
                        printWriter.flush();
                    }catch (Exception e){
                        e.printStackTrace();
                        break;

                    }
                }
            }).start();

            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            new Thread(()->{
                while (true){
                    try {
                        String readData = bufferedReader.readLine();
                        System.out.println("Receive message from server"+readData);
                    }catch (Exception e){
                        e.printStackTrace();
                        break;
                    }
                }
            }).start();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
