/*
 * @Date: 2021-07-21 09:49:05
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-14 11:45:56
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\controller\UserInfoContorller.java
 */
package com.kaoqin.stzb.controller;

import com.alibaba.fastjson.JSON;
import com.kaoqin.stzb.annotation.TokenCheck;
import com.kaoqin.stzb.entity.CallResultMsg;
import com.kaoqin.stzb.entity.UserInfo;
import com.kaoqin.stzb.exception.CodeAndMsg;
import com.kaoqin.stzb.service.UserInfoService;
import com.kaoqin.stzb.utils.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(tags = "用户信息管理")
public class UserInfoContorller {
  @Autowired
  private UserInfoService userInfoService;

  public int initUserInfo(String nickName, String email) {
    return userInfoService.initUserInfo(nickName, email);
  }

  // 获取用户信息
  @TokenCheck
  @GetMapping(value = "/userInfo")
  @Operation(summary = "根据邮箱获取用户信息")
  public String getUserInfo(@RequestParam("email") String email) throws JSONException {
    CallResultMsg<UserInfo> res = new CallResultMsg<>();
    log.info("用户: {} 用户信息读取开始...", email);
    UserInfo userInfo = userInfoService.getUserInfo(email);
    if (userInfo == null) {
      log.warn("用户: {} 账号信息读取失败", email);
      return res.fail(CodeAndMsg.READUSERINFOFAIL);
    }
    log.warn("用户: {} 账号信息读取成功", email);
    return res.success(userInfo);
  }

  @TokenCheck
  @ResponseBody
  @PutMapping(value = "/userInfo/signature")
  @Operation(summary = "更新个性签名")
  public String updateUserSignature(@RequestParam("email") String email, @RequestParam("signature") String signature) {
    log.info("用户: {} 开始更新个人签名", email);
    UserInfo userInfo = new UserInfo();
    userInfo.setSignature(signature);
    userInfo.setEmail(email);
    CallResultMsg<UserInfo> res = userInfoService.updateUserSignature(userInfo);
    if (!res.isResult()) {
      log.warn("用户: {} 个人签名更新失败", email);
      return res.toString();
    }
    log.info("用户: {} 个人签名更新成功", email);
    return res.toString();
  }

  @TokenCheck
  @ResponseBody
  @PostMapping(value = "/updateProfilePhoto")
  @Operation(summary = "更新用户头像")
  public String updateProfilePhoto(@RequestParam("email") String email, @RequestParam("top") String top,
      @RequestParam("left") String left, @RequestParam("right") String right, @RequestParam("bottom") String bottom,
      @RequestParam("rotation") String rotation, @RequestParam("scale") String scale,
      @RequestParam("file") MultipartFile multipartFile) {
    log.info("用户: {} 头像更新开始",email);
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
      // 获取旧头像信息
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
