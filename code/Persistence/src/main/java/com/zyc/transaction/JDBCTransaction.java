package com.zyc.transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zhangyongchao
 * @date 2020/4/26 15:35
 * @description
 */
public class JDBCTransaction implements Transaction {

    private Connection connection;

    private DataSource dataSource;

    private TransactionIsolationLevel isolationLevel;

    private boolean autoCommit;

    public JDBCTransaction(Connection connection) {
        this.connection = connection;
    }

    public JDBCTransaction(DataSource dataSource, TransactionIsolationLevel isolationLevel, boolean autoCommit) {
        this.dataSource = dataSource;
        this.isolationLevel = isolationLevel;
        this.autoCommit = autoCommit;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null){
            openConnection();
        }
        return connection;
    }

    private void openConnection() throws SQLException {
        connection = dataSource.getConnection();
        if (isolationLevel != null){
            connection.setTransactionIsolation(isolationLevel.getLevel());
        }

        // 设置是否自动提交事务
        // 如果当前连接的事务提交模式与期望的不一样，则修改成期望的模式，否则不修改
        if (connection.getAutoCommit() != autoCommit){
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void commit() throws SQLException {
        // 如果不是自动提交事务模式，则此处手动提交
        if (connection != null && !connection.getAutoCommit()){
            connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (connection != null && !connection.getAutoCommit()){
            connection.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null ){
            if (!connection.getAutoCommit()) {
                // 关闭连接前强制提交事务
                connection.setAutoCommit(true);
            }
            connection.close();
        }
    }
}
