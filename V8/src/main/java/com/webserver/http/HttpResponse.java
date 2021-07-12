package com.webserver.http;

import java.io.File;

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

    //响应正文相关信息
    private File entity;//正文对应的一个实体文件


    /**
     * 将当前响应对象内容按照标准的响应格式发送给客户端
     */
    public void flush(){
        //1发送状态行
        sendStatusLine();
        //2发送响应头
        sendHeaders();
        //3发送响应正文
        sendContent();
    }

    private void sendStatusLine(){}
    private void sendHeaders(){}
    private void sendContent(){}
}
