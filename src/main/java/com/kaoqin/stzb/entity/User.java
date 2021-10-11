/*
 * @Date: 2021-07-15 16:24:24
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-17 17:07:29
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\entity\User.java
 */
package com.kaoqin.stzb.entity;

import java.sql.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("User")
public class User {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String email;
    private String password;
    private Integer lock_flg;
    private Date create_time;
    private Date login_time;
    private Date update_time;
}
