package com.webserver.http;
import static com.webserver.http.HttpContext.CR;
import static com.webserver.http.HttpContext.LF;
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
    private String requestURI;//抽象路径中的请求部分
    private String queryString;//抽象路径中的参数部分
    private Map parameters = new HashMap();//保存每一组参数


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
        parseUri();//进一步解析uri
        System.out.println("method:"+method);
        System.out.println("uri:"+uri);
        System.out.println("protocol:"+protocol);
    }

    /**
     * 进一步解析uri
     */
    private void parseUri(){
        /*
            uri分为两种情况:含有参数和不含有参数
            如果uri不含有参数，则直接将uri的值赋值给requestURI即可

            如果uri中含有参数，则需要进一步拆分解析:
            首先按照"?"将uri拆分为两部分:请求部分和参数部分
            然后将请求部分赋值给requestURI
            参数部分赋值给queryString

            之后再进一步拆分出每一组参数
            将参数部分按照"&"进行拆分，每一组参数再按照"="拆分出参数名
            与参数值。并将参数名作为key，参数值作为value存入parameters
            这个Map中完成解析
         */
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
            if(pre==CR&&cur==LF){
                break;
            }
            builder.append(cur);
            pre = cur;
        }
        return builder.toString().trim();
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeader(String name){
        return headers.get(name);
    }
}
