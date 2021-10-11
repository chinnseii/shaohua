/*
 * @Date: 2021-10-04 14:39:37
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-06 14:52:42
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\entity\Application.java
 */
package com.kaoqin.stzb.entity;

import java.sql.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("Application")
/**
 * @description:
 * @param {*}
 * @return {*}
 */
public class Application {
    // 申请ID id int auto_increment
    // 申请人邮箱 email varchar(320)
    // 申请人昵称 nick_name varchar(20)
    // 盟id alliance_id int(10) unsigned zerofill
    // 团id group_id int
    // 申请种类 type int
    // 申请时间 create_time datetime
    // 受理状态 status int
    // 处理结果 process_result int
    // 更新时间 update_time datetime on update CURRENT_TIMESTAMP
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String email;
    private String nick_name;
    private Integer alliance_id;
    private Integer group_id;
    private Integer type;
    private Date create_time;
    private Integer status;
    private Integer process_result;
    private Date update_time;
}
