/*
 * @Date: 2021-10-04 14:47:30
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-25 14:25:24
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\controller\ApplicationContorller.java
 */
package com.kaoqin.stzb.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.kaoqin.stzb.annotation.TokenCheck;
import com.kaoqin.stzb.entity.Application;
import com.kaoqin.stzb.entity.CallResultMsg;
import com.kaoqin.stzb.exception.CodeAndMsg;
import com.kaoqin.stzb.service.ApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(value = "/application", tags = "申请信息管理")
public class ApplicationContorller {
    @Autowired
    private ApplicationService applicationService;

    @TokenCheck
    @PostMapping(value = "/application/alliance")
    @Operation(summary = "同盟申请")
    public String allianceApp(@RequestBody Map<String, String> map) throws IOException, JSONException {
        String email = map.get("email");
        Integer id = Integer.valueOf(map.get("id"));
        Integer type = Integer.valueOf(map.get("type"));
        log.info("用户: {} 申请类型 : {} 申请加入 :{}", email, type, id);
        if (applicationService.allianceApp(email, id, type) == 1) {
            log.info("用户: {} 申请类型 : {} 申请加入 :{} 申请成功", email, type, id);
            return new CallResultMsg<>().success();
        } else {
            return new CallResultMsg<>().fail(CodeAndMsg.CREATAPPFAIL);
        }
    }

    @TokenCheck
    @GetMapping(value = "/application/alliance")
    @Operation(summary = "获取同盟申请信息")
    public String getAllianceApp(@RequestParam(value = "email") String email,
            @RequestParam(value = "allianceId") String alliacneId) throws IOException, JSONException {
        log.info("用户: {} 开始获取同盟: {} 相关申请信息", email, alliacneId);
        CallResultMsg<List<Application>> res = applicationService.getAllianceApp(alliacneId);
        if (!res.isResult()) {
            log.info("用户: {} 获取同盟: {} 相关申请信息失败", email, alliacneId);
        }
        log.info("用户: {} 获取同盟: {} 相关申请信息成功，获取 {} 条同盟申请", email, alliacneId, res.getData().size());
        return res.toString();
    }

    @TokenCheck
    @PutMapping(value = "/application/agree")
    @Operation(summary = "申请信息同意处理")
    public String putAllianceApp(@RequestBody Map<String, String> map) {
        String email = map.get("email");
        Integer id = Integer.valueOf(map.get("id"));
        Integer type = Integer.valueOf(map.get("type"));
        if (!(type==0||type==1)) {
            return new CallResultMsg<>().fail(CodeAndMsg.INPUTERROR);
        }
        log.info("用户: {} 开始处理同盟申请信息，申请ID: {},是否同意: {}",email,id,type);
        CallResultMsg<Application> res= applicationService.appAgree(email,id,type);
        if(res.isResult()){
            log.info("用户: {} 处理同盟申请信息: {} 成功",email,id);
        }else{
            log.info("用户: {} 处理同盟申请信息: {} 失败 :{}",email,id,res.getMessage()); 
        }
        return res.toString();
    }
}
