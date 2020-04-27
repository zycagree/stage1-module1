package com.zyc.resource;

import java.io.InputStream;

/**
 * @author zhangyongchao
 * @date 2020/4/21 21:16
 * @description
 */
public class Resource {

    public static InputStream getResourceAsStream(String path){
        ClassLoader classLoader = Resource.class.getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream(path);
        return  resourceAsStream;
    }

}
