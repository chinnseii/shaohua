/*
 * @Date: 2021-07-21 09:49:05
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-12 16:05:06
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\controller\UserInfoContorller.java
 */
package com.kaoqin.stzb.controller;

import com.alibaba.fastjson.JSON;
import com.kaoqin.stzb.annotation.TokenCheck;
import com.kaoqin.stzb.entity.UserInfo;
import com.kaoqin.stzb.service.UserInfoService;
import com.kaoqin.stzb.utils.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserInfoContorller {
  @Autowired
  private UserInfoService userInfoService;

  public int initUserInfo(String nickName, String email) {
    return userInfoService.initUserInfo(nickName, email);
  }

  // ユーザー情報を取得
  @TokenCheck
  @ResponseBody
  @PostMapping(value = "/getUserInfo")
  public String getUserInfo(@RequestParam("email") String email) throws JSONException {
    log.info("用户:" + email + " 账号信息读取开始...");
    UserInfo userInfo = userInfoService.getUserInfo(email);
    if (userInfo == null) {
      log.warn("用户:" + email + " 账号信息读取失败");
      return new JSONObject().put("errorCode", 503).toString();
    }
    log.warn("用户:" + email + " 账号信息读取成功");
    return JSON.toJSONString(userInfo);
  }

  @TokenCheck
  @ResponseBody
  @PostMapping(value = "/updateUserSignature")
  public String updateUserSignature(@RequestParam("email") String email, @RequestParam("signature") String signature) {
    log.info("用户:" + email + " 开始更新个人签名");
    UserInfo userInfo = new UserInfo();
    userInfo.setSignature(signature);
    JSONObject res = new JSONObject();
    try {
      if (userInfoService.updateUserSignature(userInfo) != 1) {
        log.info("ユーザー:" + email + " 個人説明を更新失敗");
        res.put("res", false);
        return res.toString();
      }
      res.put("res", true);
    } catch (JSONException e) {
      log.error("ユーザー:" + email + e.getMessage());
      e.printStackTrace();
    }
    log.info("ユーザー:" + email + " 個人説明更新成功");
    return res.toString();
  }

  @TokenCheck
  @ResponseBody
  @PostMapping(value = "/updateProfilePhoto")
  public String updateProfilePhoto(@RequestParam("email") String email, @RequestParam("top") String top,
      @RequestParam("left") String left, @RequestParam("right") String right, @RequestParam("bottom") String bottom,
      @RequestParam("rotation") String rotation, @RequestParam("scale") String scale,
      @RequestParam("file") MultipartFile multipartFile) {
    log.info("ユーザー:" + email + " アバター更新開始");
    JSONObject res = new JSONObject();
    String fileName = multipartFile.getOriginalFilename();
    try {
      res.put("res", false);
      if (fileName == null || fileName.length() == 0) {
        res.put("defeat", "新しいアバターを選んでください。");
        return res.toString();
      }
      JSONObject avatar = new JSONObject();
      avatar.put("top", top);
      avatar.put("left", left);
      avatar.put("right", right);
      avatar.put("bottom", bottom);
      avatar.put("rotation", rotation);
      avatar.put("scale", scale);
      avatar.put("updatetime", StringUtil.getTimeToday());
      // 古いアバターの情報を取得
      UserInfo userInfo = userInfoService.getUserInfo(email);
      if (userInfo == null) {
        return res.toString();
      }
      res = userInfoService.updateUserProfilePhoto(userInfo, avatar, multipartFile);
    } catch (Exception e) {
      log.error("ユーザー:" + email + " アバター更新失敗しました" + e.getMessage());
      e.printStackTrace();
    }
    return res.toString();
  }
}
