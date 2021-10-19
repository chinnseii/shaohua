/*
 * @Date: 2021-07-15 16:24:24
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-19 20:37:14
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\entity\User.java
 */
package com.kaoqin.stzb.entity;

import java.sql.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("User")
@ApiModel("用户")
public class User {
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("用户ID")
    private Integer id;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("是否冻结，5为冻结点")
    private Integer lock_flg;
    @ApiModelProperty("创建时间")
    private Date create_time;
    @ApiModelProperty("登陆时间")
    private Date login_time;
    @ApiModelProperty("更新时间")
    private Date update_time;
}
