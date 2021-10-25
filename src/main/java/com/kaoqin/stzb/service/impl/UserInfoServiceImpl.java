/*
 * @Date: 2021-07-21 10:53:11
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-25 15:47:52
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\service\impl\UserInfoServiceImpl.java
 */
package com.kaoqin.stzb.service.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kaoqin.stzb.dao.AllianceMapper;
import com.kaoqin.stzb.dao.UserInfoMapper;
import com.kaoqin.stzb.entity.Alliance;
import com.kaoqin.stzb.entity.CallResultMsg;
import com.kaoqin.stzb.entity.Constant;
import com.kaoqin.stzb.entity.UserInfo;
import com.kaoqin.stzb.exception.CodeAndMsg;
import com.kaoqin.stzb.service.AllianceService;
import com.kaoqin.stzb.service.ApplicationService;
import com.kaoqin.stzb.service.UserInfoService;
import com.kaoqin.stzb.utils.MD5Util;
import com.kaoqin.stzb.utils.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private AllianceMapper allianceMapper;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private Constant constant;

    @Override
    public int initUserInfo(String nickName, String email) {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setAvatar_path(constant.getINIT_AVATAR_NAME());
        userInfo.setNick_name(nickName);
        userInfo.setPoint(0);
        userInfo.setPoint_last(0);
        return userInfoMapper.insert(userInfo);
    }

    @Override
    public CallResultMsg getUserInfo(String email) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        CallResultMsg res = new CallResultMsg<>(userInfo);
        // 获取入盟申请信息
        if (userInfo.getAlliance_id() == null || userInfo.getAlliance_id() == 0) {
            res.addData("application", "0");
        } else {
            res.addData("application",
                    String.valueOf(applicationService.applicationCount(userInfo.getAlliance_id(), 0)));
        }
        return res;
    }

    @Override
    public UserInfo getUserInfoObject(String email) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return userInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public CallResultMsg<UserInfo> updateUserSignature(UserInfo userInfo) {
        if (userInfoMapper.updateById(userInfo) == 1) {
            return new CallResultMsg<>(userInfoMapper.selectById(userInfo.getEmail()));
        }
        return new CallResultMsg<>(CodeAndMsg.UPDATEUSERINFOFAIL);

    }

    @Override
    public CallResultMsg updateUserAllianceId(String email, int AllianceId, String name) {
        UserInfo userInfo = userInfoMapper.selectById(email);
        userInfo.setAlliance_id(AllianceId);
        userInfo.setAlliance_name(name);
        userInfo.setJurisdiction(0);
        if (userInfoMapper.updateById(userInfo) == 1) {
            return new CallResultMsg<>();
        }
        return new CallResultMsg<>(CodeAndMsg.USERINFOUPDATEFAIL);

    }

    @Override
    public JSONObject updateUserProfilePhoto(UserInfo userInfo, JSONObject avatar, MultipartFile multipartFile)
            throws Exception {
        JSONObject res = new JSONObject();
        res.put("res", false);
        // 如果头像没有设定则不需要删除旧头像
        if (!userInfo.getAvatar_path().equals(constant.getINIT_AVATAR_NAME())) {
            Path path = Paths.get(constant.getAVATAR_PATH() + userInfo.getAvatar_path());
            Files.deleteIfExists(path);
            String dirPath = userInfo.getAvatar_path().replaceAll(path.toFile().getName(), "");
            // 删除旧头像如果文件夹为空则文件夹也一并删除
            if (Paths.get(dirPath).toFile().exists() && Paths.get(dirPath).toFile().listFiles().length == 0) {
                Files.deleteIfExists(Paths.get(dirPath));
            }
        }
        // 根据日期生成文件夹
        String avatarDir = constant.getAVATAR_PATH() + StringUtil.getTimeToday();
        File avatarDirFile = new File(avatarDir);
        if (!avatarDirFile.isDirectory()) {
            avatarDirFile.mkdirs();
        }
        String filetype = StringUtil.getFileType(multipartFile.getOriginalFilename());
        String avatarNewPath = avatarDir + File.separator + MD5Util.MD5Encode(userInfo.getEmail(), "UTF-8") + "."
                + filetype;
        avatar.put("path", avatarNewPath);
        try {
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
            if (bufferedImage == null) {
                log.info("用户:" + userInfo.getEmail() + "上传的不是图片");
                res.put("errorCode", 510);
                return res;
            }
            double originalWidth = bufferedImage.getWidth();
            double originalHeight = bufferedImage.getHeight();
            double tmpWidth;
            double tmpHeiht;
            // 图片缩放比例,每放大一级，图片放大十五分之一倍
            double tmpscale = Float.parseFloat(avatar.getString("scale")) * Double.parseDouble("0.0666");
            // 图片按长宽300px等比缩放
            double widthWithHeight = originalWidth / originalHeight;
            if (originalWidth >= originalHeight) {
                tmpWidth = 300;
                tmpHeiht = tmpWidth / widthWithHeight;
            } else {
                tmpHeiht = 300;
                tmpWidth = tmpHeiht * widthWithHeight;
            }
            int newWidth = (int) (tmpWidth * (1 + tmpscale));
            int newHeight = (int) (tmpHeiht * (1 + tmpscale));
            // 生成一个图片并按比率裁剪
            BufferedImage tmpImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = tmpImage.createGraphics();
            tmpImage = graphics.getDeviceConfiguration().createCompatibleImage(newWidth, newHeight, 3);
            graphics = tmpImage.createGraphics();
            graphics.drawImage(bufferedImage, 0, 0, newWidth, newHeight, null);
            graphics.dispose();
            // 剪辑
            int x = Integer.parseInt(avatar.getString("left"));
            int y = Integer.parseInt(avatar.getString("top"));
            int selectWidth = Integer.parseInt(avatar.getString("right")) - x;
            int selectHeight = Integer.parseInt(avatar.getString("bottom")) - y;
            BufferedImage newImage = tmpImage.getSubimage(x, y, selectWidth, selectHeight);
            // 生成头像图片
            ImageIO.write(newImage, filetype, new File(avatarNewPath));
        } catch (Exception e) {
            log.info("用户:" + userInfo.getEmail() + " 图片上传失败", e.getMessage());
            e.printStackTrace();
            res.put("errorCode", 509);
            return res;
        }
        log.info("用户: {}  头像上传成功，开始更新数据库", userInfo.getEmail());
        userInfo.setAvatar_path(avatarNewPath.replace(constant.getAVATAR_PATH(), ""));
        if (userInfoMapper.updateById(userInfo) != 1) {
            log.error("用户: {}  数据库头像信息更新失败", userInfo.getEmail());
            res.put("errorCode", 509);
        }
        log.info("用户:" + userInfo.getEmail() + " 数据库头像信息更新成功");
        res.put("res", true);
        return res;
    }

    @Override
    public CallResultMsg getAllianceUserInfo(Integer allianceId) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("alliance_id", allianceId);
        queryWrapper.orderByDesc("point");
        List<UserInfo> res = userInfoMapper.selectList(queryWrapper);
        if (res.isEmpty()) {
            return new CallResultMsg<>();
        }
        return new CallResultMsg<>(res);
    }

    @Override
    public CallResultMsg updateUserInfo(UserInfo userInfo) {
        int res = userInfoMapper.updateById(userInfo);
        if (res != 1) {
            return new CallResultMsg<>(CodeAndMsg.USERINFOUPDATEFAIL);
        }
        return new CallResultMsg<>();
    }

    @Override
    public CallResultMsg expel(String email, String expel_email) {
        UserInfo userInfo = userInfoService.getUserInfoObject(email);
        if (userInfo.getJurisdiction() == 2) {
            return new CallResultMsg<>(CodeAndMsg.NOJURISDICTION);
        }
        UserInfo expelUserInfo = userInfoService.getUserInfoObject(expel_email);
        Integer allianceId = expelUserInfo.getAlliance_id();
        UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("alliance_id", null);
        updateWrapper.set("alliance_name", null);
        updateWrapper.set("group_id", null);
        updateWrapper.set("group_name", null);
        updateWrapper.set("jurisdiction", null);
        updateWrapper.eq("email", expel_email);
        Alliance alliance = allianceMapper.selectById(allianceId);
        alliance.setPopulation(alliance.getPopulation() - 1);
        if (userInfoMapper.update(expelUserInfo, updateWrapper) == 1 && allianceMapper.updateById(alliance) == 1) {
            return new CallResultMsg<>();
        }
        return new CallResultMsg<>(CodeAndMsg.UNKNOWEXCEPTION);
    }

}