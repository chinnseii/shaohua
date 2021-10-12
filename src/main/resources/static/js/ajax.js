/*
 * @Date: 2021-10-07 10:08:39
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-12 19:31:41
 * @FilePath: \stzb\src\main\resources\static\js\ajax.js
 */
/**
 * @description: 调用需要验证token的服务
 * @param {*} url
 * @param {*} jsonData
 * @return {*}
 */
function tokenService(url, type, async, jsonData) {
    var headerObject = new Object();
    headerObject.token = sessionStorage.getItem("token");
    headerObject.email = sessionStorage.getItem("email");
    var headerInfo = JSON.stringify(headerObject);
    var res;
    $.ajax({
        type: type,
        url: url,
        async: async,
        beforeSend: function (request) {
            request.setRequestHeader("header", headerInfo);
        },
        dataType: 'JSON',
        contentType: "application/json; charset=utf-8",
        data: jsonData,
        success: function (returnValue) {
            if (returnValue == "") {
                returnValue = "{}";
            }
            if (typeof returnValue == "string") {
                res = JSON.parse(returnValue);
            } else {
                res = returnValue;
            }
            if (res.errorCode != undefined) {
                errorCode(res.errorCode);
            }
        },
        error: function (error) {
            errorCode(error.status);
        }
    });
    return res;
}

/**
 * @description: 调用不需要验证token的服务
 * @param {*} url
 * @param {*} jsonData
 * @return {*}
 */
function noTokenService(url, type, async, jsonData) {
    var res;
    $.ajax({
        type: type,
        url: url,
        async: async,
        beforeSend: function (request) {
            // request.setRequestHeader("header", headerInfo);
        },
        contentType: "application/json; charset=utf-8",
        data: jsonData,
        success: function (returnValue) {
            if (returnValue == "") {
                returnValue = "{}";
            }
            if (typeof returnValue == "string") {
                res = JSON.parse(returnValue);
            } else {
                res = returnValue;
            }
            if (res.errorCode != undefined) {
                errorMsg(res.errorCode, res.message);
            }
        },
        error: function (error) {
            errorMsg(error.status);
        }
    });
    return res;
}

function errorMsg(code, msg) {
        //身份认证失败
    var codeList1=new Array(404,402);
    for(var a in codeList1){
       if(a==code){
        layx.msg(code + ':' + msg, { dialogIcon: 'error' });
        setTimeout(function () {
            window.location.href = "login";
        }, 3000);
       } 
    }
    //警告
    var codeList2=new Array(507,508,509,510,511,512,513,514,515,516);
    for(var a in codeList2){
       if(a==code){
        layx.msg(code + ':' + msg, { dialogIcon: 'warn' });
        return;
       } 
    }
    window.location.href = "error";
}




