/*
 * @Date: 2021-08-18 17:06:42
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-16 14:03:46
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\service\AllianceService.java
 */
package com.kaoqin.stzb.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.kaoqin.stzb.entity.CallResultMsg;



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


    /**
     * @description: 根据条件检索同盟
     * @param {String} email
     * @param {String} search
     * @param {String} searchType
     * @return {*}
     */
    CallResultMsg<List<JSONObject>> searchAlliance(String email, String search, String searchType);
}
