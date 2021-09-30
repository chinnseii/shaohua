/*
 * @Date: 2021-07-28 14:18:58
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-15 13:44:04
 * @FilePath: \notec:\Users\BP-chenshengwei\Desktop\prc\stzb\src\main\java\com\kaoqin\stzb\entity\Constant.java
 */
package com.kaoqin.stzb.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "constant")
@PropertySource(value = "constant.properties", encoding = "UTF-8")
/**
 * @description: 常量类
 */
@Data
public class Constant {

    private String secretkey;
    private long expire;
    private String MOBILE_NULL_ERRORMSG;
    private String MOBILE_OR_PASSWORD_NULL_ERRORMSG;
    private String ACCOUNT_INIT_ERRORMSG;
    private String REGIST_ERRORMSG;
    private String MOBILE_OR_PASSWORD_INCORRECT_ERRORMSG;
    private String MOBILE_EXIST_INCORRECT_ERRORMSG;
    private String ACCOUNT_LOGKED_ERRORMSG;
    private String TOKEN_NULL_ERRORMSG;
    private String TOKEN_CONFIRM_ERRORMSG;
    private String AVATAR_PATH;
    private String INIT_AVATAR_NAME;
    private String CATEGORY_EXIST_ERRORMSG;
    private int NEW_NOTE_EXP;

    //redis配置
    private int port;
    private String host;
    private int timeout;
}
