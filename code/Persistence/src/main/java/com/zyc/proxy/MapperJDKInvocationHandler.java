package com.zyc.proxy;

import com.zyc.bean.Configuration;
import com.zyc.bean.MappedStatement;
import com.zyc.sql.session.SqlSession;
import com.zyc.xml.config.SqlCommandType;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static com.zyc.xml.config.SqlCommandType.*;

/**
 * @author zhangyongchao
 * @date 2020/4/26 12:09
 * @description
 */
public class MapperJDKInvocationHandler implements InvocationHandler {

    private SqlSession sqlSession;

    public MapperJDKInvocationHandler(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invoker invoker = new Invoker(sqlSession);
        return invoker.execute(proxy, method, args);
    }

}
