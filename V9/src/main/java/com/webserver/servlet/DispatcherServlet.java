package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;

/**
 * 用于处理请求环节
 */
public class DispatcherServlet {
    public void service(HttpRequest request, HttpResponse response){
        String path = request.getUri();
        File file = new File("./webapps"+path);
        if(file.exists()&&file.isFile()){
            //文件存在则直接响应
            response.setEntity(file);

            response.putHeader("Content-Type","text/html");
            response.putHeader("Content-Length",file.length()+"");
        }else{
            //文件不存在或者定位的是一个目录,则响应404
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File("./webapps/root/404.html");
            response.setEntity(file);
            response.putHeader("Content-Type","text/html");
            response.putHeader("Content-Length",file.length()+"");
        }

        response.putHeader("Server","WebServer");
    }
}
