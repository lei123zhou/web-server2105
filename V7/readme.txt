支持404的响应

上一个版本我们在ClientHandler中根据用户在浏览器地址栏中输入的抽象路径
部分去webapps下寻找对应的资源并成功响应给浏览器,使得用户可以通过改变地址
栏中的网页路径请求到对应的网页.
但是如果用户输入的是一个无效的地址(该路径去webapps下找不到对应的资源)时
服务端应当响应404

实现:
1:在webapps下新建一个目录root
  该目录存放所有网络应用的共用资源.比如404页面,因为无论用户将来请求哪个
  网络应用下的资源都可能发生该资源不存在的情况.此时都应当响应404页面
2:在root目录下新建页面404.html
  该页面居中显示一行字:404,资源不存在
3:在ClientHandler处理请求的环节,当根据抽象路径实例化File对象后,
  要先判断该文件是否存在,存在则正常响应.如果不存在则响应404.
  响应404的规则为:
  状态行中的状态代码和描述分别是:404 NotFound
  响应头Content-Length的长度为404页面的长度
  响应正文为404页面内容
之后进行测试