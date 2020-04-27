package com.zyc.sql.session;

import com.zyc.bean.Configuration;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zhangyongchao
 * @date 2020/4/21 22:27
 * @description
 */
public interface SqlSession {

    public <E> E selectOne(String statementId, Object... params) throws Exception;

    public <E> List<E> selectList(String statementId, Object... params) throws Exception;

    public int insert(String statementId, Object... params) throws IllegalAccessException, NoSuchFieldException, SQLException;

    public int delete(String statementId, Object... params) throws IllegalAccessException, NoSuchFieldException, SQLException;

    public int update(String statementId, Object... params) throws NoSuchFieldException, IllegalAccessException, SQLException;

    public void commit() throws SQLException;

    public void close();

    public void close(boolean forceRollback);

    public <E> E getMapper(Class<?> mapperClass) throws InstantiationException, IllegalAccessException;

    public Configuration getConfiguration();

}
