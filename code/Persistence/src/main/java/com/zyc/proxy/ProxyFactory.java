package com.zyc.proxy;

import com.zyc.proxy.MapperProxy.ProxyType;
import com.zyc.sql.session.SqlSession;

/**
 * @author zhangyongchao
 * @date 2020/4/27 8:36
 * @description
 */
public class ProxyFactory {

    public static Proxy newProxy(ProxyType proxyType, SqlSession sqlSession) {
        Proxy proxy;
        switch (proxyType) {
            case JDK:
                proxy = new MapperJDKProxy(sqlSession);
                break;
            case BYTEBUDDY:
                proxy = new ByteBuddyProxy(sqlSession);
                break;
            default:
                throw new IllegalStateException("不支持指定的代理类型: " + proxyType);
        }

        return proxy;
    }

}
