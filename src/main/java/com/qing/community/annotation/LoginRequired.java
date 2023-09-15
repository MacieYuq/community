package com.qing.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//声明自定义注解的范围（方法上）
@Retention(RetentionPolicy.RUNTIME)//声明自定义注解的作用时间（运行时）
public @interface LoginRequired {
}
