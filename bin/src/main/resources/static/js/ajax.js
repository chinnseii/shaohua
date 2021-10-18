/*
 * @Date: 2021-10-07 10:08:39
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-09 16:50:19
 * @FilePath: \stzb\src\main\resources\static\js\ajax.js
 */
/**
 * @description: 调用需要验证token的服务
 * @param {*} url
 * @param {*} jsonData
 * @return {*}
 */


function tokenService(url,type,async,jsonData) {
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
        dataType : 'JSON',
        contentType: "application/json; charset=utf-8",
        data:jsonData,
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
function noTokenService(url,type,async,jsonData) {
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
                errorCode(res.errorCode);
            }
        },
        error: function (error) {
           errorCode(error.status);
        }
    });
    return res;
}