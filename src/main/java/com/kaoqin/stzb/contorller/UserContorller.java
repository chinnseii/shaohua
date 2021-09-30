/*
 * @Date: 2021-07-15 16:24:23
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-29 14:10:23
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\contorller\UserContorller.java
 */
package com.kaoqin.stzb.contorller;

import javax.servlet.http.HttpServletRequest;

import com.kaoqin.stzb.annotation.PassToken;
import com.kaoqin.stzb.entity.Constant;
import com.kaoqin.stzb.entity.EmailMaster;
import com.kaoqin.stzb.entity.User;
import com.kaoqin.stzb.service.EmailMasterService;
import com.kaoqin.stzb.service.EmailService;
import com.kaoqin.stzb.service.UserService;
import com.kaoqin.stzb.utils.RedisUtil;
import com.kaoqin.stzb.utils.StringUtil;
import com.kaoqin.stzb.utils.TokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Controller
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
    @PostMapping(value = "/emailCode")
    public int emailCode(@RequestParam("email") String email, HttpServletRequest httpServletRequest) {
        String ipAddr = httpServletRequest.getRemoteAddr();
        if (redisUtil.hasKey(ipAddr)) {
            return 506;
        } else {
            EmailMaster emailMaster = emailMasterService.getMailMaster(1);
            String emailCode = StringUtil.random(6);
            if (emailService.sendHtmlMail(email, emailMaster.getTitle(),
                    emailMaster.getContent().replace("#code1", emailCode))) {
                redisUtil.set(ipAddr, email, 60);
                redisUtil.set(email, emailCode, 300);
            }
        }
        return 200;
    }

    @PassToken
    @PostMapping(value = "/register")
    public ModelAndView register(@RequestParam("email") String email, @RequestParam("nickName") String nickName,
            @RequestParam("userPassword") String userPassword, @RequestParam("emailcheck") String emailcheck,
            ModelAndView modelAndView, Model model, RedirectAttributes redirectAttributes) {
        log.info("用户:" + email + "注册处理开始");
        modelAndView.setViewName("register");
        if (!StringUtils.hasLength(email)) {
            model.addAttribute("registerErrorMsg", "请输入邮箱");
            log.error("未输入邮箱");
            return modelAndView;
        }
        if (!StringUtils.hasLength(nickName) || !StringUtils.hasLength(userPassword)) {
            model.addAttribute("registerErrorMsg", "游戏id或密码为空");
            log.error("用户:" + email + "游戏id或密码为空");
            return modelAndView;
        }
        if (!redisUtil.get(email).equals(emailcheck)) {
            model.addAttribute("registerErrorMsg", "验证码不正确或超时");
            log.error("用户:" + email + "验证码超时或不正确");
            return modelAndView;
        }
        if (userService.checkEmile(email) == 0) {
            if (userService.insert(email, userPassword) != 0) {
                log.info("用户:" + email + "注册成功，账号开始初始化");
                if (userInfoContorller.initUserInfo(nickName, email) != 1) {
                    model.addAttribute("registerErrorMsg", "账号初始化失败");
                    log.error("账号初始化失败");
                    return modelAndView;
                }
                log.info("ユーザー:" + email + " 账号开始初始化成功");
                return login(email, userPassword, modelAndView, model, redirectAttributes);
            } else {
                log.error("用户:" + email + " 注册失败");
                return modelAndView;
            }
        } else {
            log.error("用户:" + email + "邮箱已注册");
            model.addAttribute("registerErrorMsg", "邮箱已注册");
            return modelAndView;
        }
    }

    @PostMapping(value = "/login")
    @PassToken
    public ModelAndView login(@RequestParam("email") String email, @RequestParam("userPassword") String userPassword,
            ModelAndView modelAndView, Model model, RedirectAttributes redirectAttributes) {
        log.info("用户:" + email + " 开始登陆");
        modelAndView.setViewName("login");
        if (!StringUtils.hasLength(email) || !StringUtils.hasLength(userPassword)) {
            log.warn("用户:" + email + "账号或密码为空");
            model.addAttribute("loginErrorMsg", "账号或密码为空");
            return modelAndView;
        }
        // アカウント存在チェック
        // int checkExits = userService.checkEmile(email);
        if (userService.checkEmile(email) == 0) {
            log.warn("用户:" + email + "未注册");
            model.addAttribute("loginErrorMsg", "账号不存在或密码不正确");
            return modelAndView;
        }
        // ロックフラグチェック
        int checkLockFlg = userService.checkLockFlg(email);
        if (redisUtil.hasKey(email + "_Lock")) {
            log.warn("用户:" + email + "账号已冻结");
            model.addAttribute("loginErrorMsg", "账号已冻结，请3小时候重试");
            return modelAndView;
        } else {
            if(checkLockFlg==5){
                userService.updateLockFlg(email, 0);
                model.addAttribute("loginErrorMsg", "账号已冻结，请3小时候重试");
                redisUtil.set(email + "_Lock",StringUtil.getTimeHMS(), 20);
                return modelAndView;
            }       
        }
        // if (checkLockFlg == 5) {
        //     model.addAttribute("loginErrorMsg", "账号已冻结，请3小时候重试");
        //     redisUtil.set(email + "_Lock",StringUtil.getTimeHMS(), 20);
        //     return modelAndView;
        // }
        // ログイン処理
        User user = userService.login(email, userPassword);
        if (user == null) {
            log.warn("用户:" + email + "密码不正确");
            model.addAttribute("loginErrorMsg", "账号不存在或密码不正确");
            userService.updateLockFlg(email, 1);
            return modelAndView;
        } else {
            if (user.getLock_flg() != 0) {
                userService.updateLockFlg(email, 0);
            }
            // 生成动态密钥
            String secretKey = StringUtil.getRandomString(6)+constant.getSecretkey();
            long expire = constant.getExpire();
            //将动态密钥保存在redis，并设置过期时间
            redisUtil.set(email,secretKey,expire);
            String token = TokenUtil.createToken(email,secretKey);
            redirectAttributes.addFlashAttribute("token", token);
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("loginErrorMsg", "");
            modelAndView.setViewName("redirect:index");
            log.info("用户:" + email + " 登录成功");
            return modelAndView;
        }
    }
}