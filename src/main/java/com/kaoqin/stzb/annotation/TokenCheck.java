/*
 * @Date: 2021-07-27 16:19:17
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-13 17:29:05
 * @FilePath: \notec:\Users\BP-chenshengwei\Desktop\prc\stzb\src\main\java\com\kaoqin\stzb\annotation\TokenCheck.java
 */
package com.kaoqin.stzb.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/**
 * @description: 自定义注解:检查是否登录
 */
public @interface TokenCheck {
    boolean required() default true;
}
