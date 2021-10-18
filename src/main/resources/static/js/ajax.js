/*
 * @Date: 2021-10-07 10:08:39
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-18 16:55:41
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
            res = toObject(returnValue);
            if (!res.result) {
                errorMsg(res.code, res.message);
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
        dataType: 'JSON',
        contentType: "application/json; charset=utf-8",
        data: jsonData,      
        success: function (value) {
            res = toObject(value);
            if (!res.result) {
                errorMsg(res.code, res.message);
            }
        },
        error: function (error) {
            errorCode(error.status);
        }
    });
    return res;
}

function errorMsg(code, msg) {
    //身份认证失败
    var codeList1 = new Array(404, 402);
    for (var a in codeList1) {
        if (codeList1[a] == code) {
            layx.msg(code + ':' + msg, { dialogIcon: 'error' });
            setTimeout(function () {
                window.location.href = "login";
            }, 3000);
        }
    }
    //警告
    var codeList2 = new Array(507, 508, 509, 510, 511, 512, 513, 514, 515, 516);
    for (var a in codeList2) {
        if (codeList2[a] == code) {
            setTimeout(function () {
                layx.msg(code + ':' + msg, { dialogIcon: 'warn' });
            }, 3000);
        }
    }
}

function toObject(returnValue) {
    var res;
    if (returnValue == "") {
        returnValue = "{}";
    }
    if (typeof returnValue == "string") {
        res = JSON.parse(returnValue);
    } else {
        res = returnValue;
    }
    return res;
}

function errorCode(code) {
    var str = parseInt(code);
    switch (str) {
        case 401:
            layx.msg('401:身份令牌不存在，请重新登录', { dialogIcon: 'error' });
            setTimeout(function () {
                window.location.href = "login";
            }, 3000);
            break;
        case 402:
            layx.msg('402:身份令牌已过期，请重新登录', { dialogIcon: 'error' });
            setTimeout(function () {
                window.location.href = "login";
            }, 3000);
            break;
        case 403:
            layx.msg('403:身份令牌认证失败，请重新登录', { dialogIcon: 'error' });
            setTimeout(function () {
                window.location.href = "login";
            }, 3000);
            break;
        case 404:
            layx.msg('404:找不到对应服务', { dialogIcon: 'error' });
            setTimeout(function () {
                window.location.href = "login";
            }, 3000);
            break;
        case 500:
            layx.msg('500:系统异常', { dialogIcon: 'error' });
            setTimeout(function () {
                window.location.href = "login";
            }, 3000);
            break;
        case 501:
            layx.msg('501:输入不正确', { dialogIcon: 'error' });
            break;
        case 502:
            layx.msg('502:da', { dialogIcon: 'error' });
            break;
        case 503:
            layx.msg('503:个人信息获取失败', { dialogIcon: 'error' });
            break;
        case 504:
            layx.msg('504:サーバー異常、ノート削除失敗しました。', { dialogIcon: 'error' });
            break;
        case 505:
            layx.msg('505:ノートアップロード失敗しました。', { dialogIcon: 'error' });
            break;
        case 506:
            layx.msg('506:一分钟内只能发送一条邮件', { dialogIcon: 'error' });
            break;
        default:
            layx.msg('未知异常，请重试', { dialogIcon: 'error' });
            setTimeout(function () {
                window.location.href = "error";
            }, 3000);
    }
}



