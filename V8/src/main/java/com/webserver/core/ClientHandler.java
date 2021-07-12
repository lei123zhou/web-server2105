package com.webserver.core;


import com.webserver.http.HttpRequest;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理与指定客户端的HTTP交互
 * HTTP协议要求客户端与服务端交互为"一问一答"原则.因此当客户端连接
 * 服务端后,服务端便会启动一个线程来执行ClientHandler.
 * 这里分为三步完成一次HTTP交互处理:
 * 1:解析请求(读取客户端发送过来的HTTP请求内容)
 * 2:处理请求(分析请求内容理解意图并进行对应的处理)
 * 3:发送响应(将处理结果包含在一个HTTP响应内容中,将其回复给客户端)
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }
    public void run() {
        try{
            //1解析请求
            HttpRequest request = new HttpRequest(socket);

            //2处理请求
            String path = request.getUri();
            File file = new File("./webapps"+path);


            if(!file.exists()||file.isDirectory()){
                //文件不存在或者定位的是一个目录,则响应404
                statusCode = 404;
                statusReason = "NotFound";
                file = new File("./webapps/root/404.html");
            }
            //3发送响应
            /*
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......
             */
            OutputStream out = socket.getOutputStream();
            //3.1发送状态行
            String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);//发送了一个回车符
            out.write(10);//发送了一个换行符
            //3.2发送响应头
            line = "Content-Type: text/html";
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);//发送了一个回车符
            out.write(10);//发送了一个换行符
            line = "Content-Length: "+file.length();
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);//发送了一个回车符
            out.write(10);//发送了一个换行符
            //单独发送CRLF表示响应头发送完毕
            out.write(13);//发送了一个回车符
            out.write(10);//发送了一个换行符

            //3.3发送响应正文
            FileInputStream fis = new FileInputStream(file);

            byte[] data = new byte[1024*10];
            int len;
            while((len = fis.read(data))!=-1){
                out.write(data,0,len);
            }


        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try {
                //交互完毕后与客户端断开连接(HTTP协议要求)
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
