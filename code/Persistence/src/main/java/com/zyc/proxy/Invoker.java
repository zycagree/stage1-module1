package com.zyc.proxy;

import com.zyc.bean.Configuration;
import com.zyc.bean.MappedStatement;
import com.zyc.sql.session.SqlSession;
import com.zyc.xml.config.SqlCommandType;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author zhangyongchao
 * @date 2020/4/27 15:51
 * @description
 */
public class Invoker {

    private SqlSession sqlSession;

    public Invoker(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public Object execute(Object proxy, Method method, Object[] args) throws Exception {
        Object result = null;

        Class<?> declaringClass = method.getDeclaringClass();
        String name = method.getName();
        String statementId = declaringClass.getName() + "." + name;

        Configuration configuration = sqlSession.getConfiguration();
        MappedStatement mappedStatement = configuration.getMappers().get(statementId);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        switch (sqlCommandType) {
            case INSERT:
                result = sqlSession.insert(statementId, args);
                break;
            case DELETE:
                result = sqlSession.delete(statementId, args);
                break;
            case SELECT:
                Type genericReturnType = method.getGenericReturnType();
                if (genericReturnType instanceof ParameterizedType) {
                    List<Object> objects = sqlSession.selectList(statementId, args);
                    result = objects;
                } else {
                    result = sqlSession.selectOne(statementId, args);
                }
                break;
            case UPDATE:
                result = sqlSession.update(statementId, args);
                break;
            default:
                throw new RuntimeException(String.format("不支持当前的SQL类型：[%s]", sqlCommandType.toString()));
        }

        if (result == null && method.getReturnType().isPrimitive() && !returnVoid(method)) {
            throw new RuntimeException(String.format("返回类型为原始类型[%s]的方法[%s]尝试返回null!", method.getReturnType(), method.getName()));
        }

        return result;
    }

    private boolean returnVoid(Method method) {
        return void.class.equals(method.getReturnType());
    }

}
