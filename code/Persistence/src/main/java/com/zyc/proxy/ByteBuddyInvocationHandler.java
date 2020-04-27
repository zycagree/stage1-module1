package com.zyc.proxy;

import com.zyc.bean.Configuration;
import com.zyc.bean.MappedStatement;
import com.zyc.sql.session.SqlSession;
import com.zyc.xml.config.SqlCommandType;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

/**
 * @author zhangyongchao
 * @date 2020/4/27 9:14
 * @description
 */
public class ByteBuddyInvocationHandler {

    private SqlSession sqlSession;

    public ByteBuddyInvocationHandler(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @RuntimeType
    public Object byteBuddyInvoke(@This Object proxy, @Origin Method method, @AllArguments @RuntimeType Object[] args) throws Exception {
        Invoker invoker = new Invoker(sqlSession);
        return invoker.execute(proxy, method, args);
    }

}
