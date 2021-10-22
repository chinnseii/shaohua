/*
 * @Date: 2021-08-24 17:39:04
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-22 11:52:32
 * @FilePath: \stzb\src\main\resources\static\js\handlePage.js
 */
$(function () {
    var pageType = sessionStorage.getItem("pageType");
    switch (pageType) {
        case "0":
            //同盟申请
            pageType0();
            break;
        case "1":
            //踢出同盟
            pageType1();
            break;
        default:
            layx.msg('未知操作，请重试', { dialogIcon: 'error' });
            setTimeout(function () {
                window.location.href = "error";
            }, 3000);
    }
})
function pageType0() {
    $("#pageTitle").html("同盟申请");
    var res = tokenService("/application/alliance", "GET", false, {
        'email': sessionStorage.getItem("email"),
        'allianceId': sessionStorage.getItem("myAllianceId")
    });
    if (res.result) {
        var innerHtml = "";
        innerHtml += "<div class='table-responsive'>";
        innerHtml += "  <table class='table table-bordered .table-responsive'>";
        innerHtml += "<tr>";
        innerHtml += "  <td class='info'><h5>申请人</h5></td>";
        innerHtml += "  <td class='info'><h5>申请人详情</h5></td>";
        innerHtml += "  <td class='info'><h5>申请时间</h5></td>";
        innerHtml += "  <td class='info'><h5>是否同意</h5></td>";
        innerHtml += "</tr>";
        if(res.data=="[]"||res.data=="{}"||res.data==""||res.data==undefined){
            innerHtml += "<tr><td><h4>没有新的申请信息</h4></td></tr>";
        }else{
            var appList = JSON.parse(res.data);    
            for (var appIndex in appList) {
                var app = appList[appIndex];
                innerHtml += "<tr>";
                innerHtml += "  <td>" + app.nick_name + "</td>";
                innerHtml += "  <td><button class='btn btn-default' type='submit' value='" + app.email + "' onclick='getAppUserInfo(this)'>查看信息</button></td>";
                innerHtml += "  <td>" + app.create_time + "</td>";
                innerHtml += "  <td><button class='btn btn-default' name='button" + app.id + "' type='submit' onclick='agree(this," + app.id + ",0)' >同意</button>";
                innerHtml += "  <button class='btn btn-default' name='button" + app.id + "' type='submit' onclick='agree(this," + app.id + ",1)' >拒绝</button></td>";
                innerHtml += "</tr>";
            }
        }
        innerHtml += "  </table>";
        innerHtml += "</div>";
        $("#handlebody").html("");
        $("#handlebody").html(innerHtml);
    }
}

function pageType1() {
    $("#pageTitle").html("踢出同盟");
    var res = tokenService("/userInfo/alliance", "GET", false, {
        'allianceId': sessionStorage.getItem("myAllianceId"),
        'email': sessionStorage.getItem("email")
    });
    if (res.result) {
        var innerHtml = "";
        innerHtml += "<div class='table-responsive'>";
        innerHtml += "  <table class='table table-bordered .table-responsive'>";
        innerHtml += "<tr>";
        innerHtml += "  <td class='info'><h5>邮箱</h5></td>";
        innerHtml += "  <td class='info'><h5>昵称</h5></td>";
        innerHtml += "  <td class='info'><h5>个人总积分</h5></td>";
        innerHtml += "  <td class='info'><h5>剩余积分</h5></td>";
        innerHtml += "  <td class='info'><h5>权限</h5></td>";
        innerHtml += "  <td class='info'><h5>所属团</h5></td>";
        innerHtml += "  <td class='info'><h5>是否踢出</h5></td>";
        innerHtml += "</tr>";
        var userInfoList = JSON.parse(res.data);
        for (var index in userInfoList) {
            var userInfo = userInfoList[index];
            innerHtml += "<tr>";
            innerHtml += "  <td>" + userInfo.email + "</td>";
            innerHtml += "  <td>" + userInfo.nick_name + "</td>";
            innerHtml += "  <td>" + userInfo.point + "</td>";
            innerHtml += "  <td>" + userInfo.point_last + "</td>";
            innerHtml += "  <td>" + getJurisdiction(userInfo.jurisdiction) + "</td>";
            innerHtml += "  <td>" + getGroupName(userInfo.group_name) + "</td>";
            innerHtml += "  <td><button class='btn btn-default' type='submit' onclick='expel(this," + userInfo.email + ")' >踢出</button></td>";
            innerHtml += "</tr>";
        }
        innerHtml += "  </table>";
        innerHtml += "</div>";
        $("#handlebody").html("");
        $("#handlebody").html(innerHtml);
    }






}



function getAppUserInfo(object) {
    alert(object.value + "功能还没实现");
}

function getJurisdiction(str) {
    if (str.toString() == "0") {
        return "盟主";
    } else if (str.toString() == "1") {
        return "团长";
    } else if (str.toString() == "2") {
        return "成员";
    }
}

function getGroupName(str) {
    if (str == null || str == "" || str == undefined) {
        return "未分组";
    }
    return str;
}
function expel(object, id){
    var jsonData = {};
    jsonData.email = sessionStorage.getItem("email");
    jsonData.id = id;
    var res = tokenService("/application/expel", "PUT", false, JSON.stringify(jsonData));  
}

function agree(object, id, type) {
    var className = object.name;
    var jsonData = {};
    jsonData.email = sessionStorage.getItem("email");
    jsonData.id = id;
    jsonData.type = type;
    var res = tokenService("/application/agree", "PUT", false, JSON.stringify(jsonData));
    if (res.result) {
        $("button[name='" + className + "']").attr('disabled', true);
        $("button[name='" + className + "']").css("background", "grey");
        if (type == 0) {
            $(object).html("已同意");
            $("button[name='" + className + "']")[1].remove();
        } else {
            $(object).html("已拒绝");
            $("button[name='" + className + "']")[0].remove();
        }
    }
}


// /**根据输入情况，禁用与开启确认按钮
//  * @description: 
//  * @param {*}
//  * @return {*}
//  */
// $("#content,#title").keyup(() => {
//     if (checkInput()) {
//         $("#confirm").removeAttr("disabled");
//     } else {
//         $("#confirm").attr("disabled", "disabled");
//     }
// })

// /**
//  * @description: 检测输入情况
//  * @param {*}
//  * @return {*}
//  */
// function checkInput() {
//     if ($("#title").val() == "") {
//         return false;
//     }
//     if ($("#title").val().length > 50) {
//         layx.msg('タイトルを50文字以下に設定してください', { dialogIcon: 'warn' });
//     }
//     if ($("#content").val() == "") {
//         return false;
//     }
//     if ($("#content").val().length > 4999) {
//         layx.msg('内容を5000文字以下に設定してください', { dialogIcon: 'warn' });
//     }
//     return true;
// }

// /**重置输入框
//  * @description: 
//  * @param {*}
//  * @return {*}
//  */
// function reset() {
//     $("select:first option:first").attr("selected", true).siblings("option").attr("selected", false);
//     $("#content").val();
//     $("#title").val();
// }

// function uploadNote() {
//     var jsonObject = new Object();
//     jsonObject.email = sessionStorage.getItem("email");
//     jsonObject.subjects = $("#subjectsList option:selected").text();
//     if ($("#status option:selected").text() == "公開") {
//         jsonObject.status = "0";
//     } else {
//         jsonObject.status = "1";
//     }
//     jsonObject.title = $("#title").val();
//     jsonObject.content = $("#content").val();
//     var jsonData = JSON.stringify(jsonObject);
//     var res = javaService("/uploadNote", jsonData);
//     if (res.result) {
//         layx.msg('ノート作成成功', { dialogIcon: 'success' });
//         layx.destroy('loadId');
//         parent.location.reload();
//     }
// }

// /**
//  * @description: ajax调用JAVA接口(同步)
//  * @param {*} url
//  * @param {*} jsonData
//  * @return {*}
//  */
// function javaService(url, jsonData) {
//     var headerObject = new Object();
//     headerObject.token = sessionStorage.getItem("token");
//     headerObject.email = sessionStorage.getItem("email");
//     var headerInfo = JSON.stringify(headerObject);
//     var res;
//     $.ajax({
//         type: "POST",
//         url: url,
//         async: false,
//         beforeSend: function (request) {
//             request.setRequestHeader("header", headerInfo);
//         },
//         data: {
//             'jsonData': jsonData
//         },
//         success: function (returnValue) {
//             if (typeof returnValue == "string") {
//                 res = JSON.parse(returnValue);
//             } else {
//                 res = returnValue;
//             }
//             if (res.errorCode != undefined) {
//                 errorCode(res.errorCode);
//             }
//         },
//         error: function (error) {
//             errorCode(error.status);
//         }
//     });
//     return res;
// }