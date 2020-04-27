package com.zyc.transaction;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author zhangyongchao
 * @date 2020/4/26 14:54
 * @description
 */
public class JDBCTransactionFactory implements TransactionFactory{
    @Override
    public Transaction newTransaction(Connection connection) {
        return new JDBCTransaction(connection);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel isolationLevel, boolean autoCommit) {
        return new JDBCTransaction(dataSource, isolationLevel, autoCommit);
    }
}
