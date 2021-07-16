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
        String path = request.getRequestURI();
        //首先通过请求判定是否为请求一个业务
        if("/myweb/regUser".equals(path)){
            RegServlet servlet = new RegServlet();
            servlet.service(request,response);
        }else if("/myweb/loginUser".equals(path)){
            LoginServlet servlet = new LoginServlet();
            servlet.service(request,response);
        }else if("/myweb/createArticle".equals(path)){
            CreateArticleServlet servlet = new CreateArticleServlet();
            servlet.service(request,response);
        }else if("/myweb/showAllUser".equals(path)){
            ShowAllUserServlet servlet = new ShowAllUserServlet();
            servlet.service(request,response);
        }else {
            File file = new File("./webapps" + path);
            if (file.exists() && file.isFile()) {
                //文件存在则直接响应
                response.setEntity(file);
            } else {
                //文件不存在或者定位的是一个目录,则响应404
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                file = new File("./webapps/root/404.html");
                response.setEntity(file);
            }
        }

        response.putHeader("Server","WebServer");
    }
}
