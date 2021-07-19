package com.webserver.core;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = "abc%E8%8C%83%E4%BC%A0%E5%A5%87";
        String line = URLDecoder.decode(str,"UTF-8");
        System.out.println(line);
    }
}
