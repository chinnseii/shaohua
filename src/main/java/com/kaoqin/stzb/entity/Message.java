/*
 * @Date: 2021-09-08 16:41:07
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-08 17:12:51
 * @FilePath: \note\src\main\java\com\cloud\note\entity\message.java
 */
package com.kaoqin.stzb.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

@Data
@TableName("message")
public class Message {
    private String id;
    private String email;
    private int read_status;
    private String create_date;
    private String update_date;
    private String expiration;
    private String title;
    private String content;
}
