package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.vo.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * 处理用户登录
 */
public class LoginServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("LoginServlet:开始处理登录...");
        //1获取登录信息
        //2处理登录
        //3响应登录结果页面


        //1
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(username==null||password==null){
            response.setEntity(new File("./webapps/myweb/login_fail.html"));
            return;
        }
        File userFile = new File("users/"+username+".obj");
        if(userFile.exists()){//如果该文件存在,则说明该用户存在
            try(
                    ObjectInputStream ois = new ObjectInputStream(
                            new FileInputStream(userFile)
                    )
            ){
                User user = (User)ois.readObject();
                if(user.getPassword().equals(password)) {
                    //登录成功
                    response.setEntity(new File("./webapps/myweb/login_success.html"));
                }else{
                    //密码不对就是登录失败
                    response.setEntity(new File("./webapps/myweb/login_fail.html"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }else{//用户不存在
            response.setEntity(new File("./webapps/myweb/login_fail.html"));
        }



        System.out.println("LoginServlet:处理登录完毕!");
    }
}
