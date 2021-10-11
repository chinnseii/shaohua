/*
 * @Date: 2021-07-27 16:14:48
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-13 17:28:59
 * @FilePath: \notec:\Users\BP-chenshengwei\Desktop\prc\stzb\src\main\java\com\kaoqin\stzb\annotation\PassToken.java
 */
package com.kaoqin.stzb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/**
 * @description: 自定义注解:跳过验证
 */
public @interface PassToken {
    boolean required() default true;
}
