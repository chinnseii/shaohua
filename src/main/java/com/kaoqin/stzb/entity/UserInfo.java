/*
 * @Date: 2021-07-21 09:23:19
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-17 14:30:51
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\entity\UserInfo.java
 */
package com.kaoqin.stzb.entity;

import java.sql.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;


@Data
@TableName("UserInfo")
public class UserInfo {
    @TableId(value = "email")
    private String email;
    private String avatar_path;
    private String signature;
    private Integer message;
    private Integer point;
    private String nick_name;
    private Integer alliance_id;
    private String alliance_name;
    private Integer group_id;
    private String group_name;
    private Integer jurisdiction;
    private Date create_time;
    private Date update_time;
}
