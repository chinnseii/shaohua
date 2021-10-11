/*
 * @Date: 2021-09-16 17:50:47
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-16 17:59:44
 * @FilePath: \notec:\Users\BP-chenshengwei\Desktop\prc\stzb\src\main\java\com\kaoqin\stzb\service\EmailMasterService.java
 */
package com.kaoqin.stzb.service;

import com.kaoqin.stzb.dao.EmailMasterMapper;
import com.kaoqin.stzb.entity.EmailMaster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailMasterService {
    @Autowired
    private EmailMasterMapper emailMasterMapper;
    
    public EmailMaster getMailMaster(Integer i) {
      return  emailMasterMapper.selectById(i);
    }
}
