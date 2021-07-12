package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
            /*
                1:根据file获取用户请求的资源的文件名
                2:通过文件名截取出后缀
                3:根据后缀设置Content-Type的值,具体参照http.txt文件最后
             */
            Map<String,String> mimeMapping = new HashMap<>();
            mimeMapping.put("html","text/html");
            mimeMapping.put("css","text/css");
            mimeMapping.put("js","application/javascript");
            mimeMapping.put("gif","image/gif");
            mimeMapping.put("png","image/png");
            mimeMapping.put("jpg","image/jpeg");

            String fileName = file.getName();
            String ext = fileName.substring(fileName.lastIndexOf(".")+1);
            String type = mimeMapping.get(ext);
            response.putHeader("Content-Type",type);
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
