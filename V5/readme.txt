此版本完成HTTP交互的第三步,发送响应
这里先实现响应给客户端一个固定的页面来实现发送响应的功能.

此版本要了解两个知识点:
1:HTML语法基础
2:HTTP响应格式

HTML:超文本标记语言,是构成一个"网页"所使用的语言.浏览器的主要职责就是
将HTML转化为一个图形界面展现给用户的.

创建第一个页面:
1:在项目目录下新建目录webapps.这个目录用于存放Web容器中的所有网络应用.
  每个网络应用作为一个子目录存放在webapps中,并且目录名就是该网络应用的
  名字.
2:新建第一个网络应用(第一个"网站"):myweb
  在webapps下新建一个子目录,取名myweb
3:在myweb下新建网站首页:index.html
  在myweb目录下创建文件:index.html

响应index页面给浏览器
在ClientHandler第三个环节发送响应这里,通过socket获取输出流,将一个
标准的HTTP响应发送给浏览器,其中在响应正文中包含index.html页面数据.
此时浏览器请求服务端后就应当能够看到上述页面了.