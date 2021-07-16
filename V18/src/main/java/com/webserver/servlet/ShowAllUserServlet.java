package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.vo.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 用来生成显示所有注册用户的动态页面
 */
public class ShowAllUserServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("ShowAllUserServlet:开始生成动态页面");
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
           PrintWriter pw = new PrintWriter(
                   "userList.html","UTF-8"
           );
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
           pw.println("<td>用户名</td>");
           pw.println("<td>用户名</td>");
           pw.println("<td>用户名</td>");
           pw.println("</tr>");
           pw.println("</table>");
           pw.println("</center>");
           pw.println("</body>");
           pw.println("</html>");
           pw.flush();
        }catch(Exception e){
            e.printStackTrace();
        }


        System.out.println("ShowAllUserServlet:动态页面生成完毕!");
    }
}
