/*
 * @Date: 2021-07-21 09:23:19
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-25 12:22:10
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\entity\UserInfo.java
 */
package com.kaoqin.stzb.entity;

import java.sql.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@TableName("UserInfo")
@ApiModel("用户信息")
public class UserInfo {
    @TableId(value = "email")
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("头像路径")
    private String avatar_path;
    @ApiModelProperty("个性签名")
    private String signature;
    @ApiModelProperty("消息件数")
    private Integer message;
    @ApiModelProperty("个人积分")
    private Integer point;
    @ApiModelProperty("剩余积分")
    private Integer point_last;
    @ApiModelProperty("邮箱")
    private String nick_name;
    @ApiModelProperty("盟ID")
    private Integer alliance_id;
    @ApiModelProperty("盟名")
    private String alliance_name;
    @ApiModelProperty("团ID")
    private Integer group_id;
    @ApiModelProperty("团名")
    private String group_name;
    @ApiModelProperty("权限等级")
    private Integer jurisdiction;
    @ApiModelProperty("创建时间")
    private Date create_time;
    @ApiModelProperty("更新时间")
    private Date update_time;
}
