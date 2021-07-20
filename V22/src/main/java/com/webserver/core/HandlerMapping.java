package com.webserver.core;

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

                System.out.println(cls.getName());
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

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
