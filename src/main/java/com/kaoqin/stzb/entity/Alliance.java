/*
 * @Date: 2021-08-18 14:27:12
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-12 21:13:03
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\entity\Alliance.java
 */
package com.kaoqin.stzb.entity;

import java.sql.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("Alliance")
@ApiModel("同盟")
public class Alliance {
    @TableId(value = "alliance_Id" ,type = IdType.AUTO)
    @ApiModelProperty("同盟ID")
    private Integer alliance_Id;
    @ApiModelProperty("盟主邮箱")
    private String own_email;
    @ApiModelProperty("盟主昵称")
    private String own_name;
    @ApiModelProperty("盟名称")
    private String name;
    @ApiModelProperty("盟介绍")
    private String introduce;
    @ApiModelProperty("盟人口")
    private Integer population;
    @ApiModelProperty("创建时间")
    private Date create_time;
    @ApiModelProperty("更新时间")
    private Date update_time;

}
