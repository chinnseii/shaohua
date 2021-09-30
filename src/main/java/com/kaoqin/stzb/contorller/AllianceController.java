/*
 * @Date: 2021-08-18 16:59:39
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-29 18:39:42
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\contorller\AllianceController.java
 */
package com.kaoqin.stzb.contorller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.kaoqin.stzb.annotation.TokenCheck;
import com.kaoqin.stzb.service.AllianceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Controller
@ResponseBody
@Slf4j
public class AllianceController {
    @Autowired
    private AllianceService categoryService;

    @TokenCheck
    @PostMapping(value = "/createAlliance")
    public String createCategory(@RequestParam("jsonData") String jsonData,HttpServletResponse httpServletResponse) throws IOException{
        JSONObject res = new JSONObject();
        String allianceName="";
        String email="";
        try {
            JSONObject inputDate = new JSONObject(jsonData);
            allianceName = inputDate.getString("name");
            email = inputDate.getString("email");
            log.info("用户: " + email + " 开始创建同盟 「" + allianceName + "」 ");
            res = categoryService.createAlliance(email, allianceName, null);
            if (!res.getBoolean("result")) {
                log.error("用户: " + email + " 同盟 「" + allianceName + "」 创建失败 : " + res.getString("errorCode"));
                httpServletResponse.sendError(res.getInt("errorCode"));   
                return res.toString();
            }
        } catch (Exception e) {
            log.error("用户: " + email + " 同盟 「" + allianceName + "」 创建失败");
            e.printStackTrace();
            httpServletResponse.sendError(500);
            return null;
        }
        log.info("用户: " + email + " 同盟 「" + allianceName + "」 创建成功");
        return res.toString();
    }

    @TokenCheck
    @PostMapping(value = "/updateCategory")
    public String updateCategory(@RequestParam("email") String mobile,
            @RequestParam("categoryName") String categoryName, @RequestParam("status") String status) {
        log.info("ユーザー: " + mobile + " 科目 「" + categoryName + "」 更新開始");
        JSONObject res = new JSONObject();
        try {
            res = categoryService.updateCategory(mobile, categoryName, status);
            if (!res.getBoolean("result")) {
                log.error("ユーザー: " + mobile + " 科目 「" + categoryName + "」 作成失敗 : " + res.getString("errorCode"));
                return res.toString();
            }
        } catch (Exception e) {
            log.error("ユーザー: " + mobile + " 科目 「" + categoryName + "」 作成失敗");
            e.printStackTrace();
            return res.toString();
        }
        log.info("ユーザー: " + mobile + " 科目 「" + categoryName + "」 作成成功");
        return res.toString();
    }

    @TokenCheck
    @PostMapping(value = "/getCategoryName")
    public String getCategoryName(@RequestParam("jsonData") String jsonData) {
        String email = "";
        JSONObject res = new JSONObject();
        try {
            res.put("res", false);
            JSONObject jsonObject = new JSONObject(jsonData);
            email = jsonObject.getString("email");
            log.info("ユーザー: " + email + " 科目名取得開始");
            res = categoryService.getCategoryName(email);
            log.info("ユーザー: " + email + " 科目名取得成功");
        } catch (Exception e) {
            log.error("ユーザー: " + email + " 科目名取得失敗" + e.getMessage());
            e.printStackTrace();
        }
        return res.toString();
    }
}
