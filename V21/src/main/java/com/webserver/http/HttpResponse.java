package com.webserver.http;
import static com.webserver.http.HttpContext.CR;
import static com.webserver.http.HttpContext.LF;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 响应对象
 * 该类的每一个实例用于表示一个HTTP的响应.每个响应应当包含三部分:
 * 状态行,响应头,响应正文
 */
public class HttpResponse {
    //状态行相关信息
    private int statusCode = 200;//状态代码(默认值为200)
    private String statusReason = "OK";//状态描述

    //响应头相关信息
    //key:响应头名字  value:响应头对应的值
    private Map<String,String> headers = new HashMap<>();

    //响应正文相关信息
    private File entity;//正文对应的一个实体文件

    private byte[] data;//将一组字节直接作为正文内容
    /*
        ByteArrayOutputStream是一个低级流,内部维护着一个字节数组
        通过这个流写出的字节实际上都写入了该字节数组.最终这个字节数组
        的长度就是就是通过这个流写出的总字节量.
     */
    private ByteArrayOutputStream baos;


    private Socket socket;
    public HttpResponse(Socket socket){
        this.socket = socket;
    }


    /**
     * 将当前响应对象内容按照标准的响应格式发送给客户端
     */
    public void flush() throws IOException {
        //发送响应前的准备工作
        beforeSend();
        //1发送状态行
        sendStatusLine();
        //2发送响应头
        sendHeaders();
        //3发送响应正文
        sendContent();
    }

    /**
     * 发送响应前的必要工作
     */
    private void beforeSend(){
        //通过baos将生成的动态数据获取到
        if(baos!=null){//说明调用过getWriter()方法
            data = baos.toByteArray();
        }
        //如果data存在,则将该数组长度作为响应头Content-Length的值
        if(data!=null){
            putHeader("Content-Length",data.length+"");
        }
    }

    private void sendStatusLine() throws IOException {
        String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
        println(line);
    }
    private void sendHeaders() throws IOException {
//        String line = "Content-Type: text/html";
//        println(line);
//        line = "Content-Length: "+entity.length();
//        println(line);
        /*
            headers:
            key                 value
            Content-Type        text/html
            Content-Length      1235
            Server              WebServer
         */
        Set<Map.Entry<String,String>> set = headers.entrySet();
        for(Map.Entry<String,String> e : set){
            String name = e.getKey();
            String value = e.getValue();
            String line = name +": "+value;
            println(line);
        }



        //单独发送CRLF表示响应头发送完毕
        println("");
    }
    private void sendContent() throws IOException {
        if(data!=null){
            OutputStream out = socket.getOutputStream();
            out.write(data);

        }else if(entity!=null) {
            OutputStream out = socket.getOutputStream();
            try (
                    FileInputStream fis = new FileInputStream(entity);
            ) {
                byte[] data = new byte[1024 * 10];
                int len;
                while ((len = fis.read(data)) != -1) {
                    out.write(data, 0, len);
                }
            }//不用写catch,因为异常要抛给流程控制,所以在方法上定义了throws
            //不写finally是因为上面的try使用了自动关闭特性,编译器会补充finally
        }
    }

    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write(line.getBytes("ISO8859-1"));
        out.write(CR);//发送了一个回车符
        out.write(LF);//发送了一个换行符
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getEntity() {
        return entity;
    }

    public void setEntity(File entity) {
        this.entity = entity;
        /*
           1:根据file获取用户请求的资源的文件名
           2:通过文件名截取出后缀
           3:根据后缀设置Content-Type的值,具体参照http.txt文件最后
        */
        String fileName = entity.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".")+1);
        String type = HttpContext.getMimeType(ext);
        putHeader("Content-Type",type);
        putHeader("Content-Length",entity.length()+"");
    }

    /**
     * 添加一个要发送的响应头
     * @param name  响应头的名字
     * @param value 响应头对应的值
     */
    public void putHeader(String name,String value){
        headers.put(name,value);
    }

    public PrintWriter getWriter() throws UnsupportedEncodingException {
        baos = new ByteArrayOutputStream();
        return new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                baos,"UTF-8"
                        )
                ),true
        );
    }

    /**
     * 添加响应头Content-Type
     * @param type
     */
    public void setContentType(String type){
        putHeader("Content-Type",type);
    }

    /**
     * 获取一个字节输出流,通过这个流写出的字节会作为正文内容最终响应
     * 给客户端
     * @return
     */
    public OutputStream getOutputStream(){
        baos = new ByteArrayOutputStream();
        return baos;
    }
}
