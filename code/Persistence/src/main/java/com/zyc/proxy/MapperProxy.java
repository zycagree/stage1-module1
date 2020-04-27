package com.zyc.proxy;

import java.lang.annotation.*;

/**
 * @author zhangyongchao
 * @date 2020/4/27 8:21
 * @description
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MapperProxy {

    ProxyType value() default ProxyType.JDK;

    public enum ProxyType {
        JDK,
        BYTEBUDDY
    }

}
