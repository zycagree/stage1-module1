package com.zyc.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zhangyongchao
 * @date 2020/4/26 14:45
 * @description
 */
public interface Transaction {

    public Connection getConnection() throws SQLException;

    public void commit() throws SQLException;

    public void rollback() throws SQLException;

    public void close() throws SQLException;

}
