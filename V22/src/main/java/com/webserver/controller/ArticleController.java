package com.webserver.controller;

import com.webserver.core.annotation.Controller;
import com.webserver.core.annotation.RequestMapping;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.vo.Article;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理与文章相关业务
 */
@Controller
public class ArticleController {
    @RequestMapping("/myweb/createArticle")
    public void create(HttpRequest request, HttpResponse response){
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String content = request.getParameter("content");

        try(
                ObjectOutputStream oos = new ObjectOutputStream(
                        new FileOutputStream(
                                "./articles/"+title+".obj"
                        )
                )
        ){
            Article article = new Article(title,author,content);
            oos.writeObject(article);
            response.setEntity(
                    new File("./webapps/myweb/createArticle_success.html")
            );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/myweb/showAllArticle")
    public void showAllArticle(HttpRequest request,HttpResponse response){
        System.out.println("开始生成动态页面");
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


        System.out.println("动态页面生成完毕!");
    }

    @RequestMapping("/myweb/showArticle")
    public void showArticle(HttpRequest request,HttpResponse response){
        System.out.println("处理显示某一片文章的业务!!!!!!!!!!!!!!!!!!!");
    }
}
