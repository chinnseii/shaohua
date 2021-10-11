/*
 * @Date: 2021-09-02 17:17:38
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-06 13:33:24
 * @FilePath: \note\src\main\java\com\cloud\note\service\LevelMasterService.java
 */
package com.kaoqin.stzb.service;


public interface LevelMasterService {
    /**
     * @description: 获取当前等级升级所需经验值
     * @param {*}
     * @return {*}
     */
    Double getExp(String level);  

    /**
     * @description: 提升等级
     * @param {String} exp
     * @param {String} level
     * @return {*}
     */
    void levelUp(String exp,String level);











}
