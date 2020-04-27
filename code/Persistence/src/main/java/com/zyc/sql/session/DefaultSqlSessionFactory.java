package com.zyc.sql.session;

import com.zyc.bean.Configuration;
import com.zyc.sql.session.SqlSessionFactory;
import com.zyc.transaction.JDBCTransactionFactory;
import com.zyc.transaction.Transaction;
import com.zyc.transaction.TransactionFactory;
import com.zyc.transaction.TransactionIsolationLevel;

/**
 * @author zhangyongchao
 * @date 2020/4/21 22:24
 * @description
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSqlSession() {
        return openSqlSession(null, false);
    }

    public SqlSession openSqlSession(boolean autoCommit){
        return openSqlSession(null, autoCommit);
    }

    public SqlSession openSqlSession(TransactionIsolationLevel isolationLevel, boolean autoCommit){
        TransactionFactory transactionFactory = configuration.getTransactionFactory();
        Transaction transaction = transactionFactory.newTransaction(configuration.getDateSource(), isolationLevel, autoCommit);
        Executor executor = new SimpleExecutor(transaction);
        return new DefaultSqlSession(configuration, executor);
    }
}
