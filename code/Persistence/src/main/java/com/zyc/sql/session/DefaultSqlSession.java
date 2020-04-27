package com.zyc.sql.session;

import com.zyc.bean.Configuration;
import com.zyc.proxy.MapperJDKProxy;
import com.zyc.proxy.MapperProxy;
import com.zyc.proxy.MapperProxy.ProxyType;
import com.zyc.proxy.Proxy;
import com.zyc.proxy.ProxyFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zhangyongchao
 * @date 2020/4/21 23:17
 * @description
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor executor;

    public DefaultSqlSession(Configuration configuration , Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <E> E selectOne(String statementId, Object... params) throws Exception {
        List<Object> selectList = selectList(statementId, params);
        if (selectList.size() == 1) {
            return (E) selectList.get(0);
        } else {
            throw new RuntimeException("未查询到结果或返回结果过多!");
        }
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        List<E> query = executor.query(configuration, configuration.getMappers().get(statementId), params);
        return query;
    }

    @Override
    public int insert(String statementId, Object... params) throws IllegalAccessException, NoSuchFieldException, SQLException {
        return update(statementId, params);
    }

    @Override
    public int delete(String statementId, Object... params) throws IllegalAccessException, NoSuchFieldException, SQLException {
        return update(statementId, params);
    }

    @Override
    public int update(String statementId, Object... params) throws NoSuchFieldException, IllegalAccessException, SQLException {
        return executor.update(configuration, configuration.getMappers().get(statementId), params);
    }

    @Override
    public void commit() throws SQLException {
        executor.commit();
    }

    @Override
    public void close() {
        executor.close();
    }

    @Override
    public void close(boolean forceRollback) {
        executor.close(forceRollback);
    }

    @Override
    public <E> E getMapper(Class<?> mapperClass) throws InstantiationException, IllegalAccessException {
        MapperProxy mapperProxy = mapperClass.getAnnotation(MapperProxy.class);
        MapperProxy.ProxyType proxyType;
        if (mapperProxy == null){
            proxyType = ProxyType.JDK;
        } else {
            proxyType = mapperProxy.value();
        }

        Proxy proxyFactory = ProxyFactory.newProxy(proxyType, this);
        Object proxy = proxyFactory.getProxy(mapperClass);
        return (E) proxy;

    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }
}
