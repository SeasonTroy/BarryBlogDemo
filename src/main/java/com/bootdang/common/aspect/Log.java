package com.bootdang.common.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD) //这个注解注释在方法体身上
@Retention(RetentionPolicy.RUNTIME) //这个注解在方法执行完再失效
public @interface Log {
    String value() default "";
}
