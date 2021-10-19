/*
 * @Date: 2021-08-18 17:07:02
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-19 21:40:39
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\service\impl\AllianceServiceImpl.java
 */
package com.kaoqin.stzb.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaoqin.stzb.dao.AllianceMapper;
import com.kaoqin.stzb.dao.ApplicationMapper;
import com.kaoqin.stzb.entity.Alliance;
import com.kaoqin.stzb.entity.Application;
import com.kaoqin.stzb.entity.CallResultMsg;
import com.kaoqin.stzb.exception.CodeAndMsg;
import com.kaoqin.stzb.service.AllianceService;
import com.kaoqin.stzb.service.UserInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllianceServiceImpl implements AllianceService {
    @Autowired
    private AllianceMapper allianceMapper;
    @Autowired
    private ApplicationMapper applicationMapper;
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public CallResultMsg createAlliance(String email, String name, String introduce) throws Exception {
        if (name.length() > 10) {
            return new CallResultMsg<>(CodeAndMsg.INPUTTOLONG);
        }
        Alliance alliance = new Alliance();
        alliance.setOwn_email(email);
        alliance.setName(name);
        JSONObject userInfoJson=(JSONObject)userInfoService.getUserInfo(email).getData();
        alliance.setOwn_name(userInfoJson.getString("nick_name"));
        alliance.setIntroduce(introduce);
        alliance.setPopulation(1);
        if (allianceMapper.insert(alliance) == 1) {
            CallResultMsg update = userInfoService.updateUserAllianceId(email, alliance.getAlliance_Id(),
                    alliance.getName());
            if (update.isResult()) {
                return new CallResultMsg<>(alliance);
            } else {
                allianceMapper.deleteById(alliance.getAlliance_Id());
                return new CallResultMsg<>(CodeAndMsg.USERINFOUPDATEFAIL);
            }
        }
        return new CallResultMsg<>(CodeAndMsg.CREATEALLIANCEFAIL);
    }

    @Override
    public JSONObject updateCategory(String mobile, String categoryName, String status) throws Exception {
        JSONObject res = new JSONObject();
        res.put("result", false);
        Alliance category = new Alliance();
        allianceMapper.updateById(category);
        return null;
    }

 
    @Override
    /**
     * @description: 同盟检索
     * @param {String} email
     * @param {String} search
     * @param {String} searchType 0 利用id检索 1 利用同盟名检索
     * @return {*}
     */
    public CallResultMsg searchAlliance(String email, String search, String searchType) {
        List<JSONObject> resultList = new ArrayList<>();
        // 利用ID检索,
        if (searchType.equals("0")) {
            Alliance alliance = allianceMapper.selectById(Integer.valueOf(search));
            resultList.add(JSON.parseObject(JSON.toJSONString(alliance)));
        } else {
            // 同盟名称模糊查询
            QueryWrapper<Alliance> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name", search);
            List<Alliance> alliances = allianceMapper.selectList(queryWrapper);
            List<JSONObject> JsonAlliances = new ArrayList<>();
            //查询结果转换位json对象保存到List中
            for (Alliance alliance : alliances) {
                JsonAlliances.add(JSON.parseObject(JSON.toJSONString(alliance)));
            }
            JsonAlliances.forEach(resultList::add);
        }
        QueryWrapper<Application> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("email", email);
        queryWrapper2.eq("type", 0);
        List<Application> appList = applicationMapper.selectList(queryWrapper2);
        resultList.forEach(temp -> {
            temp.put("application", "1"); //默认未申请
            for (Application app : appList) {
                if (temp.get("alliance_Id").equals(app.getAlliance_id())) {
                    temp.put("application", "0");//已申请时跳出循环
                    break;
                }
            }            
        });
        return new CallResultMsg<>(resultList);
    }
}
