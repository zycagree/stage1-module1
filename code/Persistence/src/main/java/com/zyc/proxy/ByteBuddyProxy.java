package com.zyc.proxy;

import com.zyc.sql.session.SqlSession;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangyongchao
 * @date 2020/4/27 8:48
 * @description
 */
public class ByteBuddyProxy implements Proxy {

    private static final Map<Class, Class> PROXY_CLASS_CACHE = new ConcurrentHashMap<>();

    private SqlSession sqlSession;

    public ByteBuddyProxy(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public <T> T getProxy(Class<T> targetClass) throws IllegalAccessException, InstantiationException {
        Class<? extends T> proxyClass = PROXY_CLASS_CACHE.get(targetClass);
        if (proxyClass == null) {
            proxyClass = new ByteBuddy()
                    .subclass(targetClass)
                    .method(
                            ElementMatchers.isDeclaredBy(targetClass).or(ElementMatchers.isEquals())
                                    .or(ElementMatchers.isToString().or(ElementMatchers.isHashCode())))
                    .intercept(MethodDelegation.to(new ByteBuddyInvocationHandler(sqlSession)))
                    .make()
                    .load(targetClass.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                    .getLoaded();

            PROXY_CLASS_CACHE.put(targetClass, proxyClass);
        }

        return proxyClass.newInstance();
    }
}
