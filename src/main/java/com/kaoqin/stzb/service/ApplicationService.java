/*
 * @Date: 2021-10-04 14:50:42
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-19 17:59:21
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\service\ApplicationService.java
 */
package com.kaoqin.stzb.service;

import java.util.List;

import com.kaoqin.stzb.entity.Application;
import com.kaoqin.stzb.entity.CallResultMsg;

public interface ApplicationService {

    int allianceApp(String email, Integer alliance_Id, Integer type);

    int applicationCount(Integer id, Integer type);

    CallResultMsg<List<Application>> getAllianceApp(String alliacneId);

    CallResultMsg<Application> appAgree(String email, Integer id, Integer type);
    
}
