package com.webserver.core;

import com.webserver.core.annotation.Controller;
import com.webserver.core.annotation.RequestMapping;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 保存所有请求与处理类的对应关系
 */
public class HandlerMapping {
    /**
     * 保存所有请求与对应的处理方法
     * key:请求路径
     * value:处理该请求的某个Controller中的对应方法
     * 例如:
     * key:"/myweb/regUser"
     * value:UserController中的reg方法
     */
    private static Map<String, MethodMapping> mapping = new HashMap<>();

    static {
        initMapping();
    }

    private static void initMapping(){
        try{
            //1:扫描com.webserver.controller包中的所有类
            //1.1定位目录
            File dir = new File(
                    HandlerMapping.class.getClassLoader()
                    .getResource("com/webserver/controller").toURI()
            );
            //1.2获取该目录中所有的.class文件用于确定有多少个类
            File[] subs = dir.listFiles(e->e.getName().endsWith(".class"));
            //1.3根据字节码文件的文件名加载类对象
            for(File sub : subs){
                //UserController.class
                String fileName = sub.getName();
                //UserController
                String className
                        = fileName.substring(0,fileName.indexOf("."));
                //加载类对象
                Class cls = Class.forName("com.webserver.controller."+className);

                //2筛选所有被@Controller修饰的类
                if(cls.isAnnotationPresent(Controller.class)){
                    //实例化这个Controller备用
                    Object controller = cls.newInstance();

                    //3筛选该Controller中被@RequestMapping修饰的方法
                    Method[] methods = cls.getDeclaredMethods();
                    for(Method method : methods){
                        if(method.isAnnotationPresent(RequestMapping.class)){
                            //通过@RequestMapping注解获取该方法对应的请求
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);
                            String path = rm.value();
                            //将业务方法和该方法所属对象构建成一个MethodMapping对象
                            MethodMapping methodMapping = new MethodMapping();
                            methodMapping.setMethod(method);
                            methodMapping.setObj(controller);
                            /*
                            将请求路径与处理该请求的业务类实例(某Controller实例)和对应方法
                            存入mapping中.
                             */
                            mapping.put(path,methodMapping);
                        }
                    }
                }
            }

//            mapping.forEach(
//                    (k,v)-> System.out.println(
//                        "请求路径:"+k+",对应的处理类实例:"+
//                        v.getObj()+",方法:"+v.getMethod().getName()
//                    )
//            );

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据给定的请求路径获取处理该路径的Controller以及对应方法
     * @param path
     * @return
     */
    public static MethodMapping getMethod(String path){
        return mapping.get(path);
    }



    /**
     * 处理某业务的方法与其所属类的对应关系
     * 在反射中我们调用某个方法时的操作通常为:
     * method.invoke(obj)
     * method为方法对象,obj为该方法所属对象.
     *
     * 因此我们在反射中操作方法时要有两个必要条件,方法对象和该方法的
     * 所属对象.
     */
    public static class MethodMapping{
        private Object obj;//方法的所属对象
        private Method method;//方法对象

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }
    }

}
