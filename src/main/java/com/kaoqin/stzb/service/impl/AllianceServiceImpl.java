/*
 * @Date: 2021-08-18 17:07:02
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-29 18:47:28
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\service\impl\AllianceServiceImpl.java
 */
package com.kaoqin.stzb.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaoqin.stzb.dao.AllianceMapper;
import com.kaoqin.stzb.entity.Alliance;
import com.kaoqin.stzb.service.AllianceService;
import com.kaoqin.stzb.service.UserInfoService;
import com.kaoqin.stzb.utils.StringUtil;

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
    public JSONObject createAlliance(String email, String name, String status) throws Exception {
        JSONObject res = new JSONObject();
        if(name.length()>10){
            res.put("result", false);
            res.put("errorCode", 501); 
            return res;
        }
        Alliance alliance = new Alliance();
        alliance.setEmail(email);
        alliance.setName(name);
        alliance.setPopulation(0);
        if(allianceMapper.insert(alliance)==1){
          userInfoService.updateUserAllianceId(email,alliance.getAlliance_Id(),alliance.getName());
          res= new JSONObject(JSON.toJSONString(alliance));
          res.put("result", true);
        }else{
            res.put("result", false);
            res.put("errorCode", 500);    
        }
        return res;
    }

    @Override
    public JSONObject updateCategory(String mobile, String categoryName, String status) throws Exception {
        JSONObject res= new JSONObject();
        res.put("result", false);
        Alliance category= new Alliance();
        allianceMapper.updateById(category);
        return null;
    }

    @Override
    public JSONObject getCategoryName(String mobile) throws Exception {
        QueryWrapper<Alliance> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("category_name").eq("email", mobile);
        List<Alliance> nameList=allianceMapper.selectList(queryWrapper);  
        List<String> list = new ArrayList<>();  
        // if(!nameList.isEmpty()){
        //     nameList.forEach(item -> list.add(item.getCategory_name()));
        // }
        JSONObject jsonObject=new JSONObject();
        int i=0;
        for(String name :list){
            jsonObject.put(String.valueOf(i++), name);
        }
        return jsonObject;
    }
} 
