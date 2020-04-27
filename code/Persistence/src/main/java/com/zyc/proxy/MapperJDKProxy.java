package com.zyc.proxy;

import com.zyc.sql.session.SqlSession;

/**
 * @author zhangyongchao
 * @date 2020/4/26 12:07
 * @description
 */
public class MapperJDKProxy implements Proxy {

    private SqlSession sqlSession;

    public MapperJDKProxy(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public <T> T getProxy(Class<T> interfaceClass) {
        Object proxy = java.lang.reflect.Proxy.newProxyInstance(MapperJDKProxy.class.getClassLoader(),
                new Class[]{interfaceClass},
                new MapperJDKInvocationHandler(sqlSession));
        return (T) proxy;
    }
}
