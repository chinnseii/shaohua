/*
 * @Date: 2021-08-18 17:07:02
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-12 21:15:58
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\service\impl\AllianceServiceImpl.java
 */
package com.kaoqin.stzb.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaoqin.stzb.dao.AllianceMapper;
import com.kaoqin.stzb.entity.Alliance;
import com.kaoqin.stzb.entity.CallResultMsg;
import com.kaoqin.stzb.exception.CodeAndMsg;
import com.kaoqin.stzb.service.AllianceService;
import com.kaoqin.stzb.service.UserInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class AllianceServiceImpl implements AllianceService {
    @Autowired
    private AllianceMapper allianceMapper;
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
        alliance.setOwn_name(userInfoService.getUserInfo(email).getNick_name());
        alliance.setIntroduce(introduce);
        alliance.setPopulation(0);
        if (allianceMapper.insert(alliance) == 1) {
            CallResultMsg update = userInfoService.updateUserAllianceId(email, alliance.getAlliance_Id(),
                    alliance.getName());
            if (update.isResult()) {
                return new CallResultMsg<>(alliance);
            }else{
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
    public JSONObject getCategoryName(String mobile) throws Exception {
        QueryWrapper<Alliance> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("category_name").eq("email", mobile);
        List<Alliance> nameList = allianceMapper.selectList(queryWrapper);
        List<String> list = new ArrayList<>();
        // if(!nameList.isEmpty()){
        // nameList.forEach(item -> list.add(item.getCategory_name()));
        // }
        JSONObject jsonObject = new JSONObject();
        int i = 0;
        for (String name : list) {
            jsonObject.put(String.valueOf(i++), name);
        }
        return jsonObject;
    }
}
