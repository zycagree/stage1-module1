package com.zyc.mapper;

import com.zyc.bean.User;
import com.zyc.proxy.MapperProxy;
import com.zyc.proxy.MapperProxy.ProxyType;

import java.util.List;

/**
 * @author zhangyongchao
 * @date 2020/4/26 12:29
 * @description
 */
@MapperProxy(ProxyType.BYTEBUDDY)
public interface IUserMapper {

    public User findUser(User user);

    public List<User> findAll();

    public int updateUser(User user);

    public int addUser(User user);

    public int deleteUser(User user);

}
