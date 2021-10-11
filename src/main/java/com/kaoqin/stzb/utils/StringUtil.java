package com.kaoqin.stzb.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {

    /**
     * @description: 获取文件后缀
     * @param {String} fileName
     * @return {*}
     */
    public static String getFileType(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    /**
     * @description: 获取时间（年月日）
     * @param {*}
     * @return {*}
     */
    public static String getTimeToday() {
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyyMMdd");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        return sdf.format(date);
    }

    /**
     * @description: 获取时间（年月日时分秒）
     * @param {*}
     * @return {*}
     */
    public static String getTimeHMS() {
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyyMMddHHmmss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        return sdf.format(date);
    }

    /**
     * @description: 生成随机数字
     * @param {*}位数
     * @return {*}
     */
    public static String random(int a) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < a; i++) {
            result += random.nextInt(10);
        }
        return result;
    }
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
          int number=random.nextInt(62);
          sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
