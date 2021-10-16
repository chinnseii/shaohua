/*
 * @Date: 2021-10-06 17:41:36
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-16 14:08:51
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\exception\CodeAndMsg.java
 */
package com.kaoqin.stzb.exception;

public enum CodeAndMsg {
    SUCCESS(200, "成功"),
    FORBIDDEN(403,"服务器理解请求客户端的请求，但是拒绝执行此请求"),
    UNAUTHORIZED(401,"请求要求用户的身份认证"),
    UNKNOWEXCEPTION(500, "发生未知错误"),
    INPUTTOLONG(501, "输入信息过长"),
    USERINFOUPDATEFAIL(502, "更新用户同盟信息失败"),
    CREATEALLIANCEFAIL(503, "创建同盟时遇到问题，请稍后重试"),
    MAILFAIL(507, "邮箱验证码发送频繁，请稍后再试"),
    MAILEMPTY(508, "请输入邮箱"),
    NOIDORNOPSD(509, "未输入用户名或者密码"),
    MAILCHECKFAIL(510, "验证码超时或不正确"),
    INITFAIL(511, "用户信息初始化失败"),
    MAILEXIST(512, "邮箱已被注册"),
    NOREGISTER(513,"账号不存在或密码不正确"),
    ACCOUNTFREEZE(514,"账号已冻结，请3小时候重试"),
    MAILSENDFAIL(515,"邮件发送异常"),
    CREATAPPFAIL(516,"服务器异常，申请失败，请稍后重试"),
    READUSERINFOFAIL(517,"读取用户信息失败"),
    UPDATEUSERINFOFAIL(518,"更新用户信息失败"),
    INPUTERROR(519, "请求参数异常"),
    UN(1000,"");




    private int code;
    private String msg;

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
    CodeAndMsg(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}

