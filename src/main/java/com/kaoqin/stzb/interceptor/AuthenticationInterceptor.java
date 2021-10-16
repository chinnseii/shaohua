/*
 * @Date: 2021-07-27 16:57:06
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-14 10:25:05
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\interceptor\AuthenticationInterceptor.java
 */
package com.kaoqin.stzb.interceptor;

import com.kaoqin.stzb.annotation.PassToken;
import com.kaoqin.stzb.annotation.TokenCheck;
import com.kaoqin.stzb.entity.*;
import com.kaoqin.stzb.utils.RedisUtil;
import com.kaoqin.stzb.utils.TokenUtil;

import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            Object object) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        String userAddrCount = httpServletRequest.getRemoteAddr() + "_Request";
        // 每分钟请求超过100次服务器,拒绝服务
        if (redisUtil.hasKey(userAddrCount)) {
            if (Integer.valueOf(redisUtil.get(userAddrCount).toString()) > 100) {
                log.warn("请求频繁: {}", httpServletRequest.getRemoteAddr());
                return false;
            }
            redisUtil.incr(userAddrCount, 1);
        } else {
            redisUtil.set(userAddrCount, "0", 60);
        }
        // 检查是否有passtoken注释，有则跳过认证
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        // 检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(TokenCheck.class)) {
            TokenCheck userLoginToken = method.getAnnotation(TokenCheck.class);
            if (userLoginToken.required()) {
                String header = httpServletRequest.getHeader("header");// 从 http 请求头中取出
                // 获取 token 中的 user id
                if (!StringUtils.hasLength(header)) {
                    log.info("IP: {} ,请求 : {} 身份认证信息不存在", httpServletRequest.getRemoteAddr(),
                            httpServletRequest.getRequestURI());
                    httpServletResponse.sendError(401, "身份认证信息不存在");
                    return false;
                }
                String token = "";
                String email = "";
                try {
                    JSONObject jsonObject = new JSONObject(header);
                    token = jsonObject.getString("token");
                    email = jsonObject.getString("email");
                } catch (Exception e) {
                    httpServletResponse.sendError(401, "身份认证信息不存在");
                    log.info("IP: {} ,请求 : {} 身份认证信息错误", httpServletRequest.getRemoteAddr(),
                            httpServletRequest.getRequestURI());
                    return false;
                }
                // 执行认证
                if (token.equals("")) {
                    httpServletResponse.sendError(401);
                    log.info("IP: {} ,请求 : {} 身份认证信息不存在", httpServletRequest.getRemoteAddr(),
                            httpServletRequest.getRequestURI());
                    return false;
                }
                if (!redisUtil.hasKey(email)) {
                    httpServletResponse.sendError(402, "身份认证信息已过期");
                    log.info("IP: {} ,请求 : {} 身份认证信息已过期:", httpServletRequest.getRemoteAddr(),
                            httpServletRequest.getRequestURI());
                    return false;
                }
                if (!TokenUtil.checkToken(token, String.valueOf(redisUtil.get(email)), email)) {
                    httpServletResponse.sendError(403, "token解析失败");
                    log.info("用户: {} IP: {},token解析失败 ", email, httpServletRequest.getRemoteAddr(),
                            httpServletRequest.getRequestURI());
                    return false;
                }
            }
        }
        return true;
    }
}