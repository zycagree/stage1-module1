package com.zyc.bean;

import com.zyc.xml.config.SqlCommandType;

/**
 * @author zhangyongchao
 * @date 2020/4/21 21:23
 * @description
 */
public class MappedStatement {

    private String id;

    private Class<?> resultType;

    private Class<?> paramenerType;

    private String sqlText;

    private SqlCommandType sqlCommandType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }

    public Class<?> getParamenerType() {
        return paramenerType;
    }

    public void setParamenerType(Class<?> paramenerType) {
        this.paramenerType = paramenerType;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }
}
