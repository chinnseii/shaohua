/*
 * @Date: 2021-10-04 14:47:30
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-14 13:03:47
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\controller\ApplicationContorller.java
 */
package com.kaoqin.stzb.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.kaoqin.stzb.annotation.TokenCheck;
import com.kaoqin.stzb.entity.CallResultMsg;
import com.kaoqin.stzb.exception.CodeAndMsg;
import com.kaoqin.stzb.service.ApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(value = "/application", tags = "申请信息管理")
public class ApplicationContorller {
    @Autowired
    private ApplicationService applicationService;

    @TokenCheck
    @GetMapping(value = "/application/alliance")
    @ApiOperation(value = "同盟申请")
    public String allianceApp(@ApiParam(name="email",value="邮箱",required=true) @RequestParam("email") String email,
    @ApiParam(name="id",value="id",required=true) @RequestParam("id") Integer id,
    @ApiParam(name="type",value="申请类型",required=true) @RequestParam("type") Integer type,
    HttpServletResponse httpServletResponse)
            throws IOException, JSONException {
        log.info("用户: " + email + " 申请类型 :" + type + " 申请加入「" + id + "」");
        if (applicationService.allianceApp(email, id, type) == 1) {
            log.info("用户: " + email + " 申请类型 :" + type + " 申请加入 「" + id + "」 申请成功");
            return new CallResultMsg<>().success();
        }else{
            return new CallResultMsg<>().fail(CodeAndMsg.CREATAPPFAIL);
        }
    }
}
