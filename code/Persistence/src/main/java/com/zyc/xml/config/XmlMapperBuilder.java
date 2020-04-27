package com.zyc.xml.config;

import com.zyc.bean.Configuration;
import com.zyc.bean.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * @author zhangyongchao
 * @date 2020/4/21 22:39
 * @description
 */
public class XmlMapperBuilder {

    private Configuration configuration;

    public XmlMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parseMappedStatement(InputStream inputStream) throws DocumentException, ClassNotFoundException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
//        List<Element> selectNodes = rootElement.selectNodes("//select");
        List<Element> selectNodes = rootElement.selectNodes("select|insert|update|delete");
        String namespace = rootElement.attributeValue("namespace");

        for (int i = 0; i < selectNodes.size(); i++) {
            Element select = selectNodes.get(i);
            String id = select.attributeValue("id");
            String resultType = select.attributeValue("resultType");
            String parameterType = select.attributeValue("parameterType");
            Class<?> resultTypeClass = asClass(resultType);
            Class<?> parameterTypeClass = asClass(parameterType);
            String sqlText = select.getTextTrim();
            String statementId = namespace + "." + id;

            String nodeName = select.getName();
            SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase());

            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setResultType(resultTypeClass);
            mappedStatement.setParamenerType(parameterTypeClass);
            mappedStatement.setSqlText(sqlText);
            mappedStatement.setSqlCommandType(sqlCommandType);

            configuration.getMappers().put(statementId, mappedStatement);
        }
    }

    private Class<?> asClass(String type) throws ClassNotFoundException {
        if (type != null){
            return Class.forName(type);
        }
        return null;
    }
}
