package com.zyc.xml.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zyc.bean.Configuration;
import com.zyc.bean.MappedStatement;
import com.zyc.resource.Resource;
import com.zyc.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author zhangyongchao
 * @date 2020/4/21 21:40
 * @description
 */
public class XmlConfigurationBuilder {

    private Configuration configuration;

    public XmlConfigurationBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration parseConfiguration(InputStream inputStream) throws DocumentException, PropertyVetoException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();

        // 解析事务管理器
        addTransactionManager(rootElement);

        // 解析数据源并添加到Configuration
        addDataSource(rootElement);

        // 解析<mapper></mapper>,将mapper的xml文件中的SQL语句标签解析为MappedStatement对象
        addMappers(rootElement);

        return configuration;
    }

    private void addTransactionManager(Element rootElement) throws IllegalAccessException, InstantiationException {
        List<Element> transactionManagerElements = rootElement.selectNodes("//transactionManager");
        if (transactionManagerElements.size() == 1){
            Element transactionManager = transactionManagerElements.get(0);
            String type = transactionManager.attributeValue("type");
            Class<TransactionFactory> transactionFactoryClass = configuration.resolveAlias(type);
            TransactionFactory transactionFactory = transactionFactoryClass.newInstance();
            configuration.setTransactionFactory(transactionFactory);
        }
    }

    private void addMappers(Element rootElement) throws DocumentException, ClassNotFoundException {
        List<Element> mapperElements = rootElement.selectNodes("//mapper");
        for (int i = 0; i < mapperElements.size(); i++) {
            Element mapper = mapperElements.get(i);
            InputStream mapperInputStream = Resource.getResourceAsStream(mapper.attributeValue("resource"));
            XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(configuration);
            xmlMapperBuilder.parseMappedStatement(mapperInputStream);
        }
    }

    private void addDataSource(Element rootElement) throws PropertyVetoException {
        List<Element> propertyList = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (int i = 0; i < propertyList.size(); i++) {
            Element propertyElement = propertyList.get(i);
            properties.put(propertyElement.attributeValue("name"),propertyElement.attributeValue("value"));
        }

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(properties.getProperty("driverClass"));
        dataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        dataSource.setUser(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
        configuration.setDateSource(dataSource);
    }
}
