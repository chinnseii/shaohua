/*
 * @Date: 2021-08-18 16:59:39
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-12 21:25:40
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\controller\AllianceController.java
 */
package com.kaoqin.stzb.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.kaoqin.stzb.annotation.TokenCheck;
import com.kaoqin.stzb.entity.CallResultMsg;
import com.kaoqin.stzb.service.AllianceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(tags = "同盟管理")
public class AllianceController {
    @Autowired
    private AllianceService allianceService;

    @TokenCheck
    @PostMapping(value = "/alliance")
    @Operation(summary = "创建同盟")
    public String createAlliance(@RequestParam("name") String name, @RequestParam("introduce") String introduce,
            @RequestParam("email") String email, HttpServletResponse httpServletResponse) throws Exception {
        log.info("用户: {} 开始创建同盟 {}", email, name);
        CallResultMsg resMsg = allianceService.createAlliance(email, name,introduce);
        if (!resMsg.isResult()) {
            log.error("用户: {} 介绍 {} 同盟 {} 创建失败" ,email,introduce,name);
            return resMsg.toString();
        }
        log.info("用户: {} 同盟 {} 创建成功",email,name);
        return resMsg.toString();
    }
}
