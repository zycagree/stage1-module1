package com.zyc.transaction;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author zhangyongchao
 * @date 2020/4/26 14:44
 * @description
 */
public interface TransactionFactory {

    public Transaction newTransaction(Connection connection);

    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel isolationLevel, boolean autoCommit);

}
