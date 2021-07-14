package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * 用于处理用户注册业务的类
 */
public class RegServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("RegServlet:开始处理注册...");
        //1通过request获取注册页面上表单提交上来的注册信息
        //2将该用户信息保存到硬盘(序列化到一个文件中)
        //3设置response响应注册结果页面

        //1
        /*
            获取reg.html页面表单输入框数据时,getParameter方法参数要与
            该输入框name属性指定的名字完全一致!
         */
        //<input name="username" type="text">对应的页面上输入框
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        System.out.println(username+","+password+","+nickname+","+ageStr);


        System.out.println("RegServlet:处理注册完毕!");
    }
}
