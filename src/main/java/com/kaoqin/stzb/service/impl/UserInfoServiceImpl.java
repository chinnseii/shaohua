/*
 * @Date: 2021-07-21 10:53:11
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-30 09:56:48
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\service\impl\UserInfoServiceImpl.java
 */
package com.kaoqin.stzb.service.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaoqin.stzb.dao.UserInfoMapper;
import com.kaoqin.stzb.entity.Constant;
import com.kaoqin.stzb.entity.UserInfo;
import com.kaoqin.stzb.service.UserInfoService;
import com.kaoqin.stzb.utils.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private Constant constant;

    @Override
    public int initUserInfo(String nickName,String email) {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setAvatar_path(constant.getINIT_AVATAR_NAME());
        userInfo.setNick_name(nickName);
        return userInfoMapper.insert(userInfo);
    }

    @Override
    public UserInfo getUserInfo(String email) {
        QueryWrapper<UserInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return userInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public int updateUserSignature(UserInfo userInfo) {
        return userInfoMapper.updateById(userInfo);
    }

    @Override
    public int updateUserNote(String email,Boolean a,int b) {
        UserInfo userInfo=userInfoMapper.selectById(email);
        return userInfoMapper.updateById(userInfo);
    }

    @Override
    public int updateUserAllianceId(String email,int AllianceId,String name) {
        UserInfo userInfo=userInfoMapper.selectById(email);
        userInfo.setAlliance_id(AllianceId);
        userInfo.setAlliance_name(name);;
        return userInfoMapper.updateById(userInfo);
    }

    @Override
    public JSONObject updateUserProfilePhoto(UserInfo userInfo, JSONObject avatar, MultipartFile multipartFile)
            throws Exception {
        JSONObject res = new JSONObject();
        res.put("res", false);
        // アバター設定されてない場合削除処理行わない
        if (!userInfo.getAvatar_path().equals(constant.getINIT_AVATAR_NAME())) {
            Path path = Paths.get(constant.getAVATAR_PATH() + userInfo.getAvatar_path());
            Files.deleteIfExists(path);
            String dirPath = userInfo.getAvatar_path().replaceAll(path.toFile().getName(), "");
            // 旧アバター削除済み、空のフォルダの場合フォルダも削除
            if (Paths.get(dirPath).toFile().exists() && Paths.get(dirPath).toFile().listFiles().length == 0) {
                Files.deleteIfExists(Paths.get(dirPath));
            }
        }
        // 日付によって、アバター保存用フォルダを作成
        String avatarDir = constant.getAVATAR_PATH() + StringUtil.getTimeToday();
        File avatarDirFile = new File(avatarDir);
        if (!avatarDirFile.isDirectory()) {
            avatarDirFile.mkdirs();
        }
        String filetype = StringUtil.getFileType(multipartFile.getOriginalFilename());
        String avatarNewPath = avatarDir + File.separator + userInfo.getEmail() + "." + filetype;
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
            // アバターファイル作成
            ImageIO.write(newImage, filetype, new File(avatarNewPath));
        } catch (Exception e) {
            log.info("用户:" + userInfo.getEmail() + " 图片上传失败", e.getMessage());
            e.printStackTrace();
            res.put("errorCode", 509);
            return res;
        }
        log.info("用户:" + userInfo.getEmail() + " 头像上传成功，开始更新数据库");
        userInfo.setAvatar_path(avatarNewPath.replace(constant.getAVATAR_PATH(), ""));
        if (userInfoMapper.updateById(userInfo) != 1) {
            log.error("用户: " + userInfo.getEmail() + " 数据库头像信息更新失败");
            res.put("errorCode", 509);
        }
        log.info("用户:" + userInfo.getEmail() + " 数据库头像信息更新成功");
        res.put("res", true);
        return res;
    }
}