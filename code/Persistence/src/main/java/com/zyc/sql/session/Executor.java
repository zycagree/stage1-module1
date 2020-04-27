package com.zyc.sql.session;

import com.zyc.bean.Configuration;
import com.zyc.bean.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zhangyongchao
 * @date 2020/4/21 23:35
 * @description
 */
public interface Executor {
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    public int update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, NoSuchFieldException, IllegalAccessException;

    public void commit() throws SQLException;

    public void close();

    public void close(boolean forceRollback);
}
