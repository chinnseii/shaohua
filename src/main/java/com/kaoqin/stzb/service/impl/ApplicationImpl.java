/*
 * @Date: 2021-10-04 14:51:03
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-25 13:16:24
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\service\impl\ApplicationImpl.java
 */
package com.kaoqin.stzb.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaoqin.stzb.dao.AllianceMapper;
import com.kaoqin.stzb.dao.ApplicationMapper;
import com.kaoqin.stzb.dao.UserInfoMapper;
import com.kaoqin.stzb.entity.Alliance;
import com.kaoqin.stzb.entity.Application;
import com.kaoqin.stzb.entity.CallResultMsg;
import com.kaoqin.stzb.entity.UserInfo;
import com.kaoqin.stzb.exception.CodeAndMsg;
import com.kaoqin.stzb.service.ApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationImpl implements ApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private AllianceMapper allianceMapper;

    @Override
    /**
     * @description: 申请加入同盟或者团队
     * @param {String}  email
     * @param {Integer} id
     * @param {Integer} type 0同盟 1团队
     * @return {*}
     */
    public int allianceApp(String email, Integer id, Integer type) {
        Application application = new Application();
        application.setEmail(email);
        application.setType(type);
        if (type == 0) {
            application.setAlliance_id(id);
        } else {
            application.setGroup_id(id);
        }
        application.setNick_name(userInfoMapper.selectById(email).getNick_name());
        application.setStatus(0);//申请状态
        application.setProcess_result(0);//申请结果
        return applicationMapper.insert(application);
    }

    @Override
    public int applicationCount(Integer id, Integer type) {
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        queryWrapper.eq("status", 0);
        if (type == 0) {
            queryWrapper.eq("alliance_id", id);
        } else {
            queryWrapper.eq("group_id", id);
        }
        return applicationMapper.selectCount(queryWrapper);
    }

    @Override
    public CallResultMsg<List<Application>> getAllianceApp(String alliacneId) {
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("alliance_id", alliacneId);
        queryWrapper.eq("type", 0);
        queryWrapper.eq("status", 0);
        List<Application> appList = applicationMapper.selectList(queryWrapper);
        return new CallResultMsg<>(appList);
    }

    @Override
    public CallResultMsg appAgree(String handleEmail, Integer id, Integer agree) {
        UserInfo hadnleInfo = userInfoMapper.selectById(handleEmail);
        Application application = applicationMapper.selectById(id);
        Alliance alliance = allianceMapper.selectById(application.getAlliance_id());
        if (hadnleInfo.getJurisdiction() == 2) {
            return new CallResultMsg<>(CodeAndMsg.NOJURISDICTION);
        }
        Integer appType = application.getType();
        UserInfo appUserInfo = userInfoMapper.selectById(application.getEmail());
        if (appType == 0) {
            if (agree == 0) {
                // 为申请人设置同盟ID
                appUserInfo.setAlliance_id(application.getAlliance_id());
                // 设置同盟名
                appUserInfo.setAlliance_name(alliance.getName());
                // 设置权限为普通
                appUserInfo.setJurisdiction(2);
                // 更新同盟人数
                alliance.setPopulation(alliance.getPopulation() + 1);
            }
        } else if (appType == 1) {
            // 等以后再填这个坑
            appUserInfo.setGroup_id(application.getGroup_id());
        } else {
            return new CallResultMsg<>(CodeAndMsg.ACCOUNTFREEZE);
        }
        application.setProcess_result(agree);
        application.setStatus(1);
        if (agree == 0) {
            userInfoMapper.updateById(appUserInfo);
            allianceMapper.updateById(alliance);
        }
        if (applicationMapper.updateById(application) == 1) {
            return new CallResultMsg<>(application);
        }
        return new CallResultMsg<>(CodeAndMsg.UNKNOWEXCEPTION);
    }
}
