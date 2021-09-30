/*
 * @Date: 2021-08-18 17:06:42
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-29 17:34:32
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\service\AllianceService.java
 */
package com.kaoqin.stzb.service;

import org.springframework.boot.configurationprocessor.json.JSONObject;

public interface AllianceService {

    /**
     * @description: 新建一个类目
     * @param {String} mobile
     * @param {String} categoryName
     * @param {String} status
     * @return {*}
     */
    JSONObject createAlliance(String mobile, String categoryName, String status) throws Exception;

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
