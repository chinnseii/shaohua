/*
 * @Date: 2021-08-18 17:06:42
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-12 20:38:52
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\service\AllianceService.java
 */
package com.kaoqin.stzb.service;

import com.kaoqin.stzb.entity.CallResultMsg;

import org.springframework.boot.configurationprocessor.json.JSONObject;

public interface AllianceService {

    /**
     * @description: 创建同盟
     * @param {String} email
     * @param {String} name
     * @param {String} introduce
     * @return {*}
     */
    CallResultMsg createAlliance(String email, String name, String introduce) throws Exception;

    /**
     * @description: 科目更新
     * @param {String} mobile
     * @param {String} categoryName
     * @param {String} status
     * @return {*}
     */
    JSONObject updateCategory(String mobile, String categoryName, String status) throws Exception;

    JSONObject getCategoryName(String mobile) throws Exception;
}
