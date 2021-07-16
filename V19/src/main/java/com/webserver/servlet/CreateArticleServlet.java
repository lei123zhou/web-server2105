package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.vo.Article;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * 发表文章
 */
public class CreateArticleServlet {
    public void service(HttpRequest request, HttpResponse response){
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
}
