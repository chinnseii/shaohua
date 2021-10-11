/*
 * @Date: 2021-10-04 14:51:03
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-04 17:07:28
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\service\impl\ApplicationImpl.java
 */
package com.kaoqin.stzb.service.impl;


import com.kaoqin.stzb.dao.ApplicationMapper;
import com.kaoqin.stzb.dao.UserInfoMapper;
import com.kaoqin.stzb.entity.Application;
import com.kaoqin.stzb.service.ApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationImpl implements ApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    /**
     * @description: 申请加入同盟或者团队
     * @param {String} email
     * @param {Integer} id
     * @param {Integer} type 0同盟 1团队
     * @return {*}
     */
    public int allianceApp(String email, Integer id, Integer type){
        Application application = new Application();
        application.setEmail(email);
        application.setType(type);
        if (type == 0) {
            application.setAlliance_id(id);
        } else {
            application.setGroup_id(id);
        }
        application.setNick_name(userInfoMapper.selectById(email).getNick_name());
        application.setStatus(0);
        application.setProcess_result(0);
        return applicationMapper.insert(application);
    }
}
