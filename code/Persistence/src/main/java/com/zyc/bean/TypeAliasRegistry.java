package com.zyc.bean;

import com.zyc.transaction.JDBCTransactionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangyongchao
 * @date 2020/4/26 22:52
 * @description
 */
public class TypeAliasRegistry {

    private static final String JDBC = "JDBC";

    private Map<String, Class<?>> TYPE_ALIAS = new HashMap<>();

    public TypeAliasRegistry() {
        registryTypeAlias(JDBC, JDBCTransactionFactory.class);

    }

    public void registryTypeAlias(String alias, Class<?> clazz){
        if (alias == null){
            throw new RuntimeException(String.format("别名不能为空！"));
        }
        if (TYPE_ALIAS.containsKey(alias) && TYPE_ALIAS.get(alias) != null && !TYPE_ALIAS.get(alias).equals(clazz)){
            throw new RuntimeException(String.format("别名[%s]已经被注册为了类型[%s],不能再注册为类型[%s]!", alias, TYPE_ALIAS.get(alias), clazz));
        }
        TYPE_ALIAS.put(alias.toUpperCase(), clazz);
    }

    public <T> Class<T> resolveAlias(String alias){
        if (alias == null){
            throw new RuntimeException("要解析的别名不能为空!");
        }
        String aliasKey = alias.toUpperCase();
        if (TYPE_ALIAS.containsKey(aliasKey)){
            return (Class<T>) TYPE_ALIAS.get(aliasKey);
        }
        throw new RuntimeException(String.format("别名[%s]未注册!", alias));
    }
}
