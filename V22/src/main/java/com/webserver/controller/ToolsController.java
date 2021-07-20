package com.webserver.controller;

import com.webserver.core.annotation.Controller;
import com.webserver.core.annotation.RequestMapping;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import qrcode.QRCodeUtil;

/**
 * 工具
 * 生成二维码,验证码等
 */
@Controller
public class ToolsController {
    @RequestMapping("/myweb/createQR")
    public void createQR(HttpRequest request, HttpResponse response){
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
