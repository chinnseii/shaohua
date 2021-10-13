/*
 * @Date: 2021-07-15 16:24:23
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-13 21:43:51
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\controller\UserContorller.java
 */
package com.kaoqin.stzb.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.kaoqin.stzb.annotation.PassToken;
import com.kaoqin.stzb.entity.CallResultMsg;
import com.kaoqin.stzb.entity.Constant;
import com.kaoqin.stzb.entity.EmailMaster;
import com.kaoqin.stzb.entity.User;
import com.kaoqin.stzb.exception.CodeAndMsg;
import com.kaoqin.stzb.service.EmailMasterService;
import com.kaoqin.stzb.service.EmailService;
import com.kaoqin.stzb.service.UserService;
import com.kaoqin.stzb.utils.RedisUtil;
import com.kaoqin.stzb.utils.StringUtil;
import com.kaoqin.stzb.utils.TokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;



@RestController
@Api(tags = "用户")
@Slf4j
public class UserContorller {
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoContorller userInfoContorller;
    @Autowired
    private Constant constant;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailMasterService emailMasterService;
    @Autowired
    private RedisUtil redisUtil;

    @PassToken
    @ResponseBody
    @PostMapping(value = "/user/register/email")
    @Operation(summary = "发送注册邮件")
    public String emailCode(@RequestParam("email") String email, HttpServletRequest httpServletRequest) {
        String ipAddr = httpServletRequest.getRemoteAddr();
        if (redisUtil.hasKey(ipAddr)) {
            return new CallResultMsg<>().fail(CodeAndMsg.MAILFAIL);
        } else {
            EmailMaster emailMaster = emailMasterService.getMailMaster(1);
            String emailCode = StringUtil.random(6);
            if (emailService.sendHtmlMail(email, emailMaster.getTitle(),
                    emailMaster.getContent().replace("#code1", emailCode))) {
                redisUtil.set(ipAddr, email, 60);
                redisUtil.set(email, emailCode, 300);
            }
        }
        return new CallResultMsg<>().success();
    }

    @PassToken
    @PostMapping(value = "/user/register")
    @Operation(summary = "注册")
    public String register(@RequestBody Map<String, String> registerMap) {
       String email= registerMap.get("email");
       String nickName= registerMap.get("nickName");
       String userPassword= registerMap.get("userPassword");
       String emailcheck= registerMap.get("emailcheck");
        log.info("用户: {} 注册处理开始", email);
        CallResultMsg<Object> res = new CallResultMsg<>();
        if (!StringUtils.hasLength(email)) {
            log.info("用户: {} 请输入邮箱",email);
            return res.fail(CodeAndMsg.MAILEMPTY);
        }
        if (!StringUtils.hasLength(nickName) || !StringUtils.hasLength(userPassword)) {
            log.error("用户: {} 未输入用户名或者密码", email);
            return res.fail(CodeAndMsg.NOIDORNOPSD);
        }     
        // if (!redisUtil.hasKey(email)||!redisUtil.get(email).equals(emailcheck)) {
        //     log.error("用户: {} 验证码超时或不正确", email);
        //     return res.fail(CodeAndMsg.MAILCHECKFAIL);
        // }
        if (userService.checkEmile(email) == 0) {
            if (userService.insert(email, userPassword) != 0) {
                log.info("用户: {} 注册成功，账号开始初始化", email);
                if (userInfoContorller.initUserInfo(nickName, email) != 1) {
                    log.error("用户: {} 用户信息初始化失败", email);
                    return res.fail(CodeAndMsg.INITFAIL);
                }
                log.info("用户: {} 账号初始化成功", email);
                return res.success();
            } else {
                log.error("用户: {} 注册失败", email);
                return res.fail(CodeAndMsg.UNKNOWEXCEPTION);
            }
        } else {
            log.error("用户: {} 邮箱已被注册", email);
            return res.fail(CodeAndMsg.MAILEXIST);
        }
    }

    @PostMapping(value = "/user/login")
    @PassToken
    @Operation(summary = "登录")
    public String login(@RequestBody Map<String, String> logInMap) {
        CallResultMsg<JSONObject> res = new CallResultMsg<>();
       String email= logInMap.get("email");
       String userPassword= logInMap.get("userPassword");
        log.info("用户: {} 开始登陆",email);
        if (!StringUtils.hasLength(email) || !StringUtils.hasLength(userPassword)) {
            log.warn("用户: {} 账号或密码为空",email);
            return res.fail(CodeAndMsg.NOIDORNOPSD);
        }
        // 判断是否存在
        if (userService.checkEmile(email) == 0) {
            log.warn("用户: {} 未注册",email);
            return res.fail(CodeAndMsg.NOREGISTER);
        }
        // ロックフラグチェック
        int checkLockFlg = userService.checkLockFlg(email);
        if (redisUtil.hasKey(email + "_Lock")) {
            log.warn("用户: {} 账号已冻结",email);
            return res.fail(CodeAndMsg.ACCOUNTFREEZE);
        } else {
            if (checkLockFlg == 5) {
                userService.updateLockFlg(email, 0);
                redisUtil.set(email + "_Lock", StringUtil.getTimeHMS(), 10800);
                log.warn("用户: {} 账号已冻结",email);
                return res.fail(CodeAndMsg.ACCOUNTFREEZE);
            }
        }
        // 登录处理
        User user = userService.login(email, userPassword);
        if (user == null) {
            log.warn("用户:" + email + "密码不正确");
            userService.updateLockFlg(email, 1);
            log.warn("用户: {} 账号或密码为空",email);
            return res.fail(CodeAndMsg.NOIDORNOPSD);
        } else {
            if (user.getLock_flg() != 0) {
                userService.updateLockFlg(email, 0);
            }
            // 生成动态密钥(6位随机字符加固定密钥)
            String secretKey = StringUtil.getRandomString(6) + constant.getSecretkey();
            long expire = constant.getExpire();
            // 将动态密钥保存在redis，并设置过期时间
            redisUtil.set(email, secretKey, expire);
            String token = TokenUtil.createToken(email, secretKey);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("token", token);
            jsonObject.put("email", email);
            log.info("用户: {} 登录成功",email);
            return res.success(jsonObject);
        }
    }
}