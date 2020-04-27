package com.zyc.sql.session;

/**
 * @author zhangyongchao
 * @date 2020/4/21 21:34
 * @description
 */
public interface SqlSessionFactory {

    public SqlSession openSqlSession();

}
