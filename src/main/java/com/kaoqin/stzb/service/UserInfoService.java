/*
 * @Date: 2021-07-21 10:51:04
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-18 19:23:38
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\service\UserInfoService.java
 */
package com.kaoqin.stzb.service;

import com.kaoqin.stzb.entity.CallResultMsg;
import com.kaoqin.stzb.entity.UserInfo;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface UserInfoService {

    /**
     * @description: ユーザー情報初期化
     * @param {String} email
     * @return {*}
     */
    int initUserInfo(String nickName,String email);


    /**
     * @description: 获取统一返回形式的用户信息
     * @param {String} email
     * @return {*}
     */
    CallResultMsg getUserInfo(String email);

  
    /**
     * @description: 获取用户信息
     * @param {String} email
     * @return {*}
     */
    UserInfo getUserInfoObject(String email);

    /**
     * @description: 更新用户签名
     * @param {UserInfo} userInfo
     * @return {*}
     */
    CallResultMsg<UserInfo> updateUserSignature(UserInfo userInfo);


        /**
     * @description: 更新同盟信息
     * @param {UserInfo} userInfo
     * @return {*}
     */
    CallResultMsg updateUserAllianceId(String email,int id,String name);

    /**
     * @description: ユーザーアバター更新
     * @param {UserInfo} userInfo
     * @return {*}
     * @throws JSONException
     * @throws Exception
     */
    JSONObject updateUserProfilePhoto(UserInfo userInfo, JSONObject avatar, MultipartFile multipartFile)
            throws Exception;

}
