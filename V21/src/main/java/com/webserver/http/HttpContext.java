package com.webserver.http;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存放所有HTTP协议规定内容
 */
public class HttpContext {
    /**
     * ASC中回车符
     */
    public static final char CR = 13;
    /**
     * ASC中换行符
     */
    public static final char LF = 10;

    /**
     * 资源后缀名与Content-Type的对应关系
     * key:资源后缀名
     * value:Content-Type头对应的值
     */
    private static Map<String,String> mimeMapping = new HashMap<>();

    static{
        initMimeMapping();
    }
    /**
     * 初始化mimeMapping
     */
    private static void initMimeMapping(){
//        mimeMapping.put("html","text/html");
//        mimeMapping.put("css","text/css");
//        mimeMapping.put("js","application/javascript");
//        mimeMapping.put("gif","image/gif");
//        mimeMapping.put("png","image/png");
//        mimeMapping.put("jpg","image/jpeg");
        /*
            web.xml是tomcat提供的文件,里面保存了所有资源后缀与对应
            的Content-Type头的值.
            通过解析config/web.xml文件初始化mimeMapping
            将根元素中所有名为<mime-mapping>的子标签获取到,并将其中
            的子标签:
            <extension>中的文本作为key
            <mime-type>中的文本作为value
            以一组键值对形式存入mimeMapping这个Map.
            最终完成后,mimeMapping应该有1011个元素.
         */
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read("./config/web.xml");
            Element root = doc.getRootElement();
            List<Element> list = root.elements("mime-mapping");
            for(Element e : list){
                String key = e.elementText("extension");
                String value = e.elementText("mime-type");
                mimeMapping.put(key,value);
            }
            System.out.println(mimeMapping.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据给定的资源的后缀名获取对应的Content-Type头的值
     * @param ext
     * @return
     */
    public static String getMimeType(String ext){
        return mimeMapping.get(ext);
    }

    public static void main(String[] args) {
        System.out.println(getMimeType("gif"));
    }
}
