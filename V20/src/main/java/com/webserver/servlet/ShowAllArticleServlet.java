package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.vo.Article;
import com.webserver.vo.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 用来生成显示所有文章的动态页面
 */
public class ShowAllArticleServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("ShowAllUserServlet:开始生成动态页面");
        //扫描articles目录中的所有.obj文件并反序列化得到所有的User对象
        File articleDir = new File("./articles");
        File[] subs = articleDir.listFiles(
                e->e.isFile()&&e.getName().endsWith(".obj")
        );
        List<Article> articleList = new ArrayList<>();
        for(File articleFile : subs) {
            try (
                    ObjectInputStream ois = new ObjectInputStream(
                            new FileInputStream(articleFile)
                    )
            ) {
                //InvalidClassException
                Article article = (Article)ois.readObject();
                articleList.add(article);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        articleList.forEach(System.out::println);



        try{
           PrintWriter pw = response.getWriter();
           pw.println("<!DOCTYPE html>");
           pw.println("<html lang=\"en\">");
           pw.println("<head>");
           pw.println("<meta charset=\"UTF-8\">");
           pw.println("<title>文章列表</title>");
           pw.println("</head>");
           pw.println("<body>");
           pw.println("<center>");
           pw.println("<h1>文章列表</h1>");
           pw.println("<table border=\"1\">");
           pw.println("<tr>");
           pw.println("<td>标题</td>");
           pw.println("<td>作者</td>");
           pw.println("</tr>");
           for(Article article : articleList){
               pw.println("<tr>");
               pw.println("<td><a href='./showArticle?title="+article.getTitle()+"'>"+article.getTitle()+"</a></td>");
               pw.println("<td>"+article.getAuthor()+"</td>");
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


        System.out.println("ShowAllUserServlet:动态页面生成完毕!");
    }
}
