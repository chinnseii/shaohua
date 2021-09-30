/*
 * @Date: 2021-08-18 14:27:12
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-29 17:56:36
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\entity\Alliance.java
 */
package com.kaoqin.stzb.entity;

import java.sql.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("Alliance")
public class Alliance {
    @TableId(value = "alliance_Id" ,type = IdType.AUTO)
    private Integer alliance_Id;
    private String email;
    private String name;
    private String group_introduce;
    private Integer population;
    private Date create_time;
    private Date update_time;

}
