/*
 * @Date: 2021-07-15 16:24:24
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-22 10:41:04
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\service\impl\UserServiceImpl.java
 */
package com.kaoqin.stzb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kaoqin.stzb.dao.UserMapper;
import com.kaoqin.stzb.entity.User;
import com.kaoqin.stzb.service.UserService;
import com.kaoqin.stzb.utils.MD5Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    /**
     * @description: 登录
     * @param {String} email
     * @param {String} password
     * @return {*}
     */
    public User login(String email, String password) {
        String passwordMd5 = MD5Util.MD5Encode(email + password, "UTF-8");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        queryWrapper.eq("password", passwordMd5);
        User user = userMapper.selectOne(queryWrapper);
        if(user==null){
            return null;
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("login_time=now()");
        updateWrapper.eq("id", user.getId());
        userMapper.update(new User(), updateWrapper);
        return user;
    }

    @Override
    /**
     * @description: 注册
     * @param {String} userNickName
     * @param {String} email
     * @param {String} userPassword
     * @return {*}
     */
    public int insert(String email, String userPassword) {
        String passwordMd5 = MD5Util.MD5Encode(email + userPassword, "UTF-8");
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordMd5);
        user.setLock_flg(0);
        return userMapper.insert(user);
    }

    @Override
    /**
     * @description: アカウント存在チェック
     * @param {String} email
     * @return {*}
     */
    public int checkEmile(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    /**
     * @description: ロックフラグ更新
     * @param {String} email
     * @param {int}    0:flg清0，1:fla加1
     * @return {*}
     */
    public int updateLockFlg(String email, int a) {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("email", email);
        User user = userMapper.selectOne(userUpdateWrapper);
        if (a == 0) {
            user.setLock_flg(0);
        } else {
            user.setLock_flg(user.getLock_flg() + 1);
        }
        return userMapper.updateById(user);
    }

    @Override
    /**
     * @description: ロックフラグチェック
     * @param {String} email
     * @return {*}
     */
    public int checkLockFlg(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("lock_flg").eq("email", email);
        return userMapper.selectOne(queryWrapper).getLock_flg();
    }

}
