利用反射机制对DispatcherServlet处理业务部分进行重构.做到将来添加新的
业务(无论是新添加一个Controller类还是在某个Controller类中添加了新的
业务方法)DispatcherServlet都不会再做任何修改.

思想:
首先设计一个注解:@Controller.该注解用来修饰所有的业务处理类.便于让
DispatcherServlet筛选类使用.
每个Controller中用于处理某个请求的业务方法再使用注解:@RequestMapping
修饰,并且该注解传入一个参数用于标明该方法处理的请求路径,例如:
@RequestMapping("/myweb/regUser")
public void reg(HttpRequest request,HttpResponse response){...}
这样一来,DispatherServlet可以用该注解筛选处理请求的方法,并根据该参数
的值某个请求由哪个方法来处理了.


实现:
1:新建一个包:com.webserver.core.annotation
2:新建两个注解@Controller和@RequestMapping






