package com.webserver.http;

import java.util.HashMap;
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
        mimeMapping.put("html","text/html");
        mimeMapping.put("css","text/css");
        mimeMapping.put("js","application/javascript");
        mimeMapping.put("gif","image/gif");
        mimeMapping.put("png","image/png");
        mimeMapping.put("jpg","image/jpeg");
    }

    /**
     * 根据给定的资源的后缀名获取对应的Content-Type头的值
     * @param ext
     * @return
     */
    public static String getMimeType(String ext){
        return mimeMapping.get(ext);
    }
}
