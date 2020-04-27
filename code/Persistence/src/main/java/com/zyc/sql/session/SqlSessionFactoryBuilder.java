package com.zyc.sql.session;

import com.zyc.bean.Configuration;
import com.zyc.xml.config.XmlConfigurationBuilder;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * @author zhangyongchao
 * @date 2020/4/21 21:31
 * @description
 */
public class SqlSessionFactoryBuilder {

    private Configuration configuration;

    public SqlSessionFactoryBuilder() {
        this.configuration = new Configuration();
    }

    public SqlSessionFactory build(InputStream inputStream) throws PropertyVetoException, DocumentException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 解析xml配置文件，封装Configuration
        XmlConfigurationBuilder xmlConfigurationBuilder = new XmlConfigurationBuilder(configuration);
        Configuration configuration = xmlConfigurationBuilder.parseConfiguration(inputStream);
        // 创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return sqlSessionFactory;
    }

}
