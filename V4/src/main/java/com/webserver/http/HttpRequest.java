package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象
 * 该类的每一个实例用于表示客户端发送给浏览器的一个HTTP请求
 * HTTP协议规定一个请求包含三部分内容:
 * 请求行,消息头,消息正文
 */
public class HttpRequest {
    //请求行相关信息
    private String method;//请求方式
    private String uri;//抽象路径
    private String protocol;//协议版本

    //消息头相关信息
    //存放所有消息头的Map,key:消息头的名字 value:消息头的值
    private Map<String,String> headers = new HashMap<>();

    //消息正文相关信息


    private Socket socket;
    /**
     * 实例化HttpRequest的过程就是解析请求的过程
     */
    public HttpRequest(Socket socket) throws IOException {
        this.socket = socket;
        //1解析请求行
        parseRequestLine();
        //2解析消息头
        parseHeaders();
        //3解析消息正文
        parseContent();
    }

    /**
     * 解析请求行
     */
    private void parseRequestLine() throws IOException {
        String line = readLine();
        System.out.println("请求行:"+line);
        String[] data = line.split("\\s");
        method = data[0];
        uri = data[1];
        protocol = data[2];
        System.out.println("method:"+method);
        System.out.println("uri:"+uri);
        System.out.println("protocol:"+protocol);
    }

    /**
     * 解析消息头
     */
    private void parseHeaders() throws IOException {
        while(true) {
            String line = readLine();
            //读取请求行时如果返回的是空字符串,说明单独读取到了CRLF
            if(line.isEmpty()){
                break;
            }
            System.out.println("消息头:" + line);
            String[] data = line.split(":\\s");
            headers.put(data[0],data[1]);
        }
        System.out.println("headers:"+headers);
    }

    /**
     * 解析消息正文
     */
    private void parseContent(){}

    private String readLine() throws IOException {
        /*
            调用同一个socket的getInputStream方法时,无论调用多少次
            返回的输入流始终是同一个流.输出流也是一样的.
         */
        InputStream in = socket.getInputStream();
        StringBuilder builder = new StringBuilder();
        //cur表示本次读取到的字符,pre表示上次读取到的字符
        char cur='a',pre='a';
        int d;
        while((d = in.read())!=-1){
            cur = (char)d;
            //上次读到的是回车符本次读取到的是换行符
            if(pre==13&&cur==10){
                break;
            }
            builder.append(cur);
            pre = cur;
        }
        return builder.toString().trim();
    }
}
