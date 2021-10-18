/*
 * @Date: 2021-08-18 16:59:39
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-18 17:24:08
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\controller\AllianceController.java
 */
package com.kaoqin.stzb.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.kaoqin.stzb.annotation.TokenCheck;
import com.kaoqin.stzb.entity.CallResultMsg;
import com.kaoqin.stzb.exception.CodeAndMsg;
import com.kaoqin.stzb.service.AllianceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

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
    public String createAlliance(@RequestBody Map<String, String> map, HttpServletResponse httpServletResponse)
            throws Exception {
        String email = map.get("email");
        String name = map.get("name");
        String introduce = map.get("introduce");
        log.info("用户: {} 开始创建同盟 {}", email, name);
        CallResultMsg resMsg = allianceService.createAlliance(email, name, introduce);
        if (!resMsg.isResult()) {
            log.error("用户: {} 介绍 {} 同盟 {} 创建失败", email, introduce, name);
            return resMsg.toString();
        }
        log.info("用户: {} 同盟 {} 创建成功", email, name);
        return resMsg.toString();
    }

    @TokenCheck
    @GetMapping(value = "/alliance/search")
    @Operation(summary = "检索同盟")
    public String searchAlliance(@RequestParam("search") String search, @RequestParam("searchType") String searchType,
            @RequestParam("email") String email, HttpServletResponse httpServletResponse) throws Exception {
        log.info("用户: {} 开始检索同盟 {}，检索类型 {}", email, search, searchType);
        CallResultMsg<List<JSONObject>> res = new CallResultMsg<>();
        if (!(searchType.equals("0") || searchType.equals("1"))) {
            log.warn("用户: {} 同盟检索失败 {}，检索类型 {} 参数异常", email, search, searchType);
            return res.fail(CodeAndMsg.INPUTERROR);
        }
        if (searchType.equals("0") && search.length() != 10) {
            // 若不是数字将抛出异常
            try {
                Integer.valueOf(search);
            } catch (Exception e) {
                return res.fail(CodeAndMsg.INPUTERROR);
            }
            return res.fail(CodeAndMsg.INPUTERROR);
        }
        if (searchType.equals("1") && search.length() == 0 || search.length() > 20) {
            return res.fail(CodeAndMsg.INPUTERROR);
        }
        res = allianceService.searchAlliance(email, search, searchType);
        if (res.isResult()) {
            log.info("用户: {} 检索同盟 {}，检索类型 {} 检索成功 ", email, search, searchType);
            return res.toString();
        }
        return res.fail(CodeAndMsg.ACCOUNTFREEZE);
    }
}
