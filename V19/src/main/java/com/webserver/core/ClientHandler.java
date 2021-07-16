package com.webserver.core;


import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlet.DispatcherServlet;

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
            HttpResponse response = new HttpResponse(socket);

            //2处理请求
            DispatcherServlet servlet = new DispatcherServlet();
            servlet.service(request,response);

            //3发送响应
            response.flush();

        } catch(IOException e){
            e.printStackTrace();
        } catch (EmptyRequestException e) {
           //空请求异常捕获后什么都不用做
        } finally {
            try {
                //交互完毕后与客户端断开连接(HTTP协议要求)
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
