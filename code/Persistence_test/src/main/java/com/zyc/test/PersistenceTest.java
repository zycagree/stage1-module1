package com.zyc.test;

import com.zyc.bean.User;
import com.zyc.mapper.IUserMapper;
import com.zyc.resource.Resource;
import com.zyc.sql.session.SqlSession;
import com.zyc.sql.session.SqlSessionFactory;
import com.zyc.sql.session.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;

/**
 * @author zhangyongchao
 * @date 2020/4/21 23:22
 * @description
 */
public class PersistenceTest {

    private SqlSession sqlSession;

    @Before
    public void before() throws Exception {
        InputStream inputStream = Resource.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        sqlSession = sqlSessionFactory.openSqlSession();
    }

    @Test
    public void testQuery() throws Exception {
        User user = new User();
        user.setId(3);
        user.setUsername("zhang33666");
        Object o = sqlSession.selectOne("com.zyc.mapper.IUserMapper.findUser", user);
        System.out.println(o);

        System.out.println("-----------------------------------------------------------------------------");

        List<User> users = sqlSession.selectList("com.zyc.mapper.IUserMapper.findAll");
        System.out.println(users);

    }

    @Test
    public void testQueryWithMapper() throws Exception {
        IUserMapper userMapper = sqlSession.getMapper(IUserMapper.class);
        List<User> users = userMapper.findAll();
        System.out.println(users);

        System.out.println("-----------------------------------------------------------------------------");

        User user = new User();
        user.setId(3);
        user.setUsername("zhang66666");
        User u = userMapper.findUser(user);
        sqlSession.close();
        System.out.println(u);

    }

    @Test
    public void testUpdateWithMapper() throws Exception {
        IUserMapper userMapper = sqlSession.getMapper(IUserMapper.class);

        User user = new User();
        user.setId(3);
        user.setUsername("zhang66666");
        int rows = userMapper.updateUser(user);
        sqlSession.commit();
        System.out.println(rows);

        System.out.println("-----------------------------------------------------------------------------");

        List<User> users = userMapper.findAll();
        System.out.println(users);

        sqlSession.close();

    }

    @Test
    public void testInsertWithMapper() throws Exception {
        IUserMapper userMapper = sqlSession.getMapper(IUserMapper.class);

        User user = new User();
        user.setId(4);
        user.setUsername("wu4");
        int rows = userMapper.addUser(user);
        sqlSession.commit();
        System.out.println(rows);

        System.out.println("-----------------------------------------------------------------------------");

        List<User> users = userMapper.findAll();
        sqlSession.close();
        System.out.println(users);
    }

    @Test
    public void testDeleteWithMapper() throws Exception {
        IUserMapper userMapper = sqlSession.getMapper(IUserMapper.class);

        User user = new User();
        user.setId(4);
        user.setUsername("wu4");
        int rows = userMapper.deleteUser(user);
        sqlSession.commit();
        System.out.println(rows);

        System.out.println("-----------------------------------------------------------------------------");

        List<User> users = userMapper.findAll();
        sqlSession.close();
        System.out.println(users);
    }


}
