/*
 * @Date: 2021-10-05 15:25:34
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-12 17:10:36
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\entity\CallResultMsg.java
 */
package com.kaoqin.stzb.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kaoqin.stzb.exception.CodeAndMsg;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @param <T>
 * @description: 统一返回格式
 * @param {*}
 * @return {*}
 */
@Data
@ApiModel("api通用返回数据")
public class CallResultMsg<T> {
    @ApiModelProperty("api结果")
    private boolean result;
    @ApiModelProperty("标识码")
    private int code;
    @ApiModelProperty("错误信息")
    private String message;
    @ApiModelProperty("返回数据")
    private T data;

    public CallResultMsg(CodeAndMsg codeAndMsg) {
        this.result = false;
        this.code = codeAndMsg.getCode();
        this.message = codeAndMsg.getMsg();
        this.data = null;
    }

    public CallResultMsg() {
        this.result = true;
        this.code = CodeAndMsg.SUCCESS.getCode();
        this.message = CodeAndMsg.SUCCESS.getMsg();
        this.data = null;
    }

    public CallResultMsg(T data) {
        this.result = true;
        this.code = CodeAndMsg.SUCCESS.getCode();
        this.message = CodeAndMsg.SUCCESS.getMsg();
        this.data = data;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", this.result);
        jsonObject.put("code", this.code);
        jsonObject.put("message", this.message);
        if (this.data == null) {
            jsonObject.put("data", "");
        } else {
            jsonObject.put("data",JSON.toJSONString(data));
        }
        return jsonObject.toJSONString();
    }

    public String success() {
        return new CallResultMsg<>().toString();
    }

    public String success(T data) {
        return new CallResultMsg<>(data).toString();
    }

    public String fail(CodeAndMsg codeAndMsg) {
        return new CallResultMsg<>(codeAndMsg).toString();
    }
}
