/*
 * @Date: 2021-09-16 17:46:11
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-16 17:52:36
 * @FilePath: \notec:\Users\BP-chenshengwei\Desktop\prc\stzb\src\main\java\com\kaoqin\stzb\entity\emailmaster.java
 */
package com.kaoqin.stzb.entity;
import java.sql.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("Emailmaster")
public class EmailMaster {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content; 
    private Date create_time; 
    private Date update_time;   
}
