package com.zyc.sql.session;

import com.zyc.bean.BoundSql;
import com.zyc.bean.Configuration;
import com.zyc.bean.MappedStatement;
import com.zyc.transaction.JDBCTransactionFactory;
import com.zyc.transaction.Transaction;
import com.zyc.utils.GenericTokenParser;
import com.zyc.utils.ParameterMapping;
import com.zyc.utils.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyongchao
 * @date 2020/4/21 23:39
 * @description
 */
public class SimpleExecutor implements Executor {

    private Transaction transaction;

    public SimpleExecutor(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException {
        // 1、获取jdbc连接
        Connection connection = transaction.getConnection();

        // 2、 将#{}占位符替换为？，并将#{}中的字段名称的实际值从params中获取到赋值给PreparedStatement
        PreparedStatement preparedStatement = prepareStatement(mappedStatement, connection, params);

        // 3、执行SQL
        ResultSet resultSet = preparedStatement.executeQuery();

        // 4、封装结果
        List<Object> results = new ArrayList<>();
        Class<?> resultType = mappedStatement.getResultType();
        while(resultSet.next()){
            Object instance = resultType.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                // 列名
                String columnName = metaData.getColumnName(i);
                // 列名的值
                Object value = resultSet.getObject(columnName);

                // 利用内省机制设置值
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultType);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(instance, value);
            }
            results.add(instance);
        }
        return (List<E>) results;
    }

    private PreparedStatement prepareStatement(MappedStatement mappedStatement, Connection connection, Object... param) throws SQLException, NoSuchFieldException, IllegalAccessException {
        // 1、解析SQL，占位符替换成？形式
        // sql形式：select * from user where id = #{id}
        String sqlText = mappedStatement.getSqlText();
        //  将SQL中的#{}用？代替，并解析出#{}中的名称保存
        BoundSql boundSql = getBoundSql(sqlText);
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());

        // 2、 通过反射为PreparedStatement设置参数
        Class<?> paramenerType = mappedStatement.getParamenerType();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappings.size(); i++){
            ParameterMapping parameterMapping = parameterMappings.get(i);
            String content = parameterMapping.getContent();
            Field field = paramenerType.getDeclaredField(content);
            field.setAccessible(true);
            Object value = field.get(param[0]);

            preparedStatement.setObject(i + 1, value);
        }
        return preparedStatement;
    }

    @Override
    public int update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, NoSuchFieldException, IllegalAccessException {
        Connection connection = transaction.getConnection();
        PreparedStatement preparedStatement = prepareStatement(mappedStatement, connection, params);
        preparedStatement.execute();
        int rows = preparedStatement.getUpdateCount();
        return rows;
    }

    @Override
    public void commit() throws SQLException {
        transaction.commit();
    }

    @Override
    public void close() {
        close(false);
    }

    @Override
    public void close(boolean forceRollback) {
        try {
            try {
                // 关闭时根据是否强制回滚事务做回滚动作
                tryRollback(forceRollback);
            } finally {
                if (transaction != null){
                    transaction.close();
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("关闭连接出错!");
        }
    }

    private void tryRollback(boolean forceRollback) throws SQLException {
        if (forceRollback){
            transaction.rollback();
        }
    }

    private BoundSql getBoundSql(String sqlText) {
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser tokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);

        String sql = tokenParser.parse(sqlText);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        BoundSql boundSql = new BoundSql(sql, parameterMappings);
        return boundSql;
    }
}
