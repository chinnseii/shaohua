/*
 * @Date: 2021-07-15 16:24:23
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-17 17:48:00
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\contorller\HTMLController.java
 */
package com.kaoqin.stzb.contorller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HTMLController {
    @RequestMapping("login")
    public String login() {
        return "login";   //注意：通过thymeleaf访问的界面不应添加后缀
    }
    @RequestMapping("register")
    public String register() {
        return "register";   
    }
    @RequestMapping("index")
    public String index() {
        return "index";   
    }
    @RequestMapping("changeProfilePhoto")
    public String changeProfilePhoto() {
        return "changeProfilePhoto";   
    }
    @RequestMapping("createNote")
    public String createNote() {
        return "createNote";   
    }
    @RequestMapping("editNote")
    public String editNote() {
        return "editNote";   
    }
}
