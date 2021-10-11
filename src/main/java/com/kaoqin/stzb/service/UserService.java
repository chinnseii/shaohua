/*
 * @Date: 2021-07-15 16:24:24
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-17 13:29:51
 * @FilePath: \notec:\Users\BP-chenshengwei\Desktop\prc\stzb\src\main\java\com\kaoqin\stzb\service\UserService.java
 */
package com.kaoqin.stzb.service;

import com.kaoqin.stzb.entity.User;

public interface UserService {
  
    /**
     * 新用户注册
     *
     * @param userNickName
     * @param email
     * @param userPassword
     * @return
     */
    int insert(String email, String userPassword);

   /**
     * 登录
     *
     * @param email
     * @param userPassword
     * @return
     */
    User login( String email, String userPassword);

    /**
     * 检查邮箱是否存在
     *
     * @param email
     * @return
     */
    int checkEmile(String emile);

    /**
     * 检查是否锁定
     *
     * @param email
     * @return
     */
    int checkLockFlg(String email);

    /**
     * @description: 更新锁定区分
     * @param {String} email
     * @param {int} a 1:flg加1，0:flg重置
     * @return {*}
     */
    int updateLockFlg(String email, int a);

}
