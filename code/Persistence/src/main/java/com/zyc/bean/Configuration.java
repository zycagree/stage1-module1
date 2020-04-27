package com.zyc.bean;

import com.zyc.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangyongchao
 * @date 2020/4/21 21:19
 * @description
 */
public class Configuration {

    private DataSource dateSource;

    private TransactionFactory transactionFactory;

    private TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    private Map<String,MappedStatement> mappers = new HashMap<>();

    public DataSource getDateSource() {
        return dateSource;
    }

    public void setDateSource(DataSource dateSource) {
        this.dateSource = dateSource;
    }

    public Map<String, MappedStatement> getMappers() {
        return mappers;
    }

    public void setMappers(Map<String, MappedStatement> mappers) {
        this.mappers = mappers;
    }

    public TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public void setTypeAliasRegistry(TypeAliasRegistry typeAliasRegistry) {
        this.typeAliasRegistry = typeAliasRegistry;
    }

    public <T> Class<T> resolveAlias(String alias){
        return typeAliasRegistry.resolveAlias(alias);
    }
}
