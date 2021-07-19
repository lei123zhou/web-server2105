package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import qrcode.QRCodeUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 生成二维码
 */
public class CreateQRServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("开始生成二维码");
        //获取用户希望在二维码上显示的文字
        String content = request.getParameter("content");

        try {

            QRCodeUtil.encode(
                    content,"./logo.jpg",
                    response.getOutputStream(),false
            );
            response.setContentType("image/jpeg");
        } catch (Exception e) {
            e.printStackTrace();
        }



        System.out.println("二维码生成完毕");
    }
}
