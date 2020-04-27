package com.zyc.proxy;

/**
 * @author zhangyongchao
 * @date 2020/4/26 12:05
 * @description
 */
public interface Proxy {
    public <T> T getProxy(Class<T> targetClass) throws IllegalAccessException, InstantiationException;
}
