package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.vo.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
        /*
            首先要验证用户输入的注册信息。
            要求如下:
            四项内容都必须写，不能有null的情况。
            并且年龄必须是一个整数格式（正则表达式验证）

            否则直接响应注册失败提示页面:reg_info_error.html
            该页面剧中显示一行字:注册失败，注册信息有误!请重新注册。
         */
        if(username==null||password==null||nickname==null||
            ageStr==null||!ageStr.matches("[0-9]+")){
            File file = new File("./webapps/myweb/reg_info_error.html");
            response.setEntity(file);
            return;
        }
        int age = Integer.parseInt(ageStr);
        /*
            2 将该用户以User对象形式序列化到文件中。
              文件名格式:用户名.obj
         */
        /*
            重复用户的验证，如果是重复用户则直接响应重复用户的提示页面:
            have_user.html.中显示一行字:该用户已存在，请重新注册。
         */
        File userFile = new File("./users/"+username+".obj");
        if(userFile.exists()){//如果文件存在说明是重复用户
            response.setEntity(new File("./webapps/myweb/have_user.html"));
            return;
        }

        try(
                ObjectOutputStream oos = new ObjectOutputStream(
                        new FileOutputStream(
                                userFile
                        )
                )
        ){
            User user = new User(username,password,nickname,age);
            oos.writeObject(user);

            //3
            File file = new File(
                    "./webapps/myweb/reg_success.html");
            response.setEntity(file);

        }catch(IOException e){
            e.printStackTrace();
        }





        System.out.println("RegServlet:处理注册完毕!");
    }
}
