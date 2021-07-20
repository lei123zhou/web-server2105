package com.webserver.controller;

import com.webserver.core.annotation.Controller;
import com.webserver.core.annotation.RequestMapping;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.vo.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理与用户业务相关的操作
 */
@Controller
public class UserController {
    @RequestMapping("/myweb/regUser")
    public void reg(HttpRequest request, HttpResponse response){
        System.out.println("开始处理注册...");
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

        System.out.println("处理注册完毕!");
    }

    @RequestMapping("/myweb/loginUser")
    public void login(HttpRequest request,HttpResponse response){
        System.out.println("开始处理登录...");
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
        System.out.println("处理登录完毕!");
    }

    @RequestMapping("/myweb/showAllUser")
    public void showAllUser(HttpRequest request,HttpResponse response){
        System.out.println("开始生成动态页面");
        //扫描users目录中的所有.obj文件并反序列化得到所有的User对象
        File usersDir = new File("./users");
        File[] subs = usersDir.listFiles(
                e->e.isFile()&&e.getName().endsWith(".obj")
        );
        List<User> userList = new ArrayList<>();
        for(File userFile : subs) {
            try (
                    ObjectInputStream ois = new ObjectInputStream(
                            new FileInputStream(userFile)
                    )
            ) {
                //InvalidClassException
                User user = (User)ois.readObject();
                userList.add(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        userList.forEach(System.out::println);



        try{
            PrintWriter pw = response.getWriter();
            pw.println("<!DOCTYPE html>");
            pw.println("<html lang=\"en\">");
            pw.println("<head>");
            pw.println("<meta charset=\"UTF-8\">");
            pw.println("<title>用户列表</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<center>");
            pw.println("<h1>用户列表</h1>");
            pw.println("<table border=\"1\">");
            pw.println("<tr>");
            pw.println("<td>用户名</td>");
            pw.println("<td>密码</td>");
            pw.println("<td>昵称</td>");
            pw.println("<td>年龄</td>");
            pw.println("</tr>");
            for(User user : userList){
                pw.println("<tr>");
                pw.println("<td>"+user.getUsername()+"</td>");
                pw.println("<td>"+user.getPassword()+"</td>");
                pw.println("<td>"+user.getNickname()+"</td>");
                pw.println("<td>"+user.getAge()+"</td>");
                pw.println("</tr>");
            }

            pw.println("</table>");
            pw.println("</center>");
            pw.println("</body>");
            pw.println("</html>");

            response.setContentType("text/html");
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("动态页面生成完毕!");
    }
}
