/*
 * @Date: 2021-10-04 14:47:30
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-09 13:50:11
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\contorller\ApplicationContorller.java
 */
package com.kaoqin.stzb.contorller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.kaoqin.stzb.annotation.TokenCheck;
import com.kaoqin.stzb.entity.CallResultMsg;
import com.kaoqin.stzb.service.ApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
        JSONObject res = new JSONObject();
       // Integer id = 0;
        //String email = "";
       // Integer type = 0;
      //  JSONObject inputDate = new JSONObject(jsonData);
       // id = Integer.valueOf(inputDate.getString("id"));
       // email = inputDate.getString("email");
       // type = Integer.valueOf(inputDate.getString("type"));
        log.info("用户: " + email + " 申请类型 :" + type + " 申请加入「" + id + "」");
        int a = applicationService.allianceApp(email, id, type);
        if (a == 1) {
            res.put("result", true);
            log.info("用户: " + email + " 申请类型 :" + type + " 申请加入 「" + id + "」 申请成功");
            return new CallResultMsg<>().success();
        }
        res.put("result", false);
        log.error("用户: " + email + " 申请类型 :" + type + " 申请加入 「" + id + "」 申请失败");
        return res.toString();
    }
}
