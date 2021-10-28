/*
 * @Date: 2021-08-24 17:39:04
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-27 18:09:24
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
        case "2":
            //创建分组
            pageType2();
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
        if (res.data == "[]" || res.data == "{}" || res.data == "" || res.data == undefined) {
            innerHtml += "<tr><td><h4>没有新的申请信息</h4></td></tr>";
        } else {
            var appList = JSON.parse(res.data);
            for (var appIndex in appList) {
                var app = appList[appIndex];
                innerHtml += "<tr>";
                innerHtml += "  <td>" + app.nick_name + "</td>";
                innerHtml += "  <td><button class='btn btn-default' type='submit' value='" + app.email + "' onclick='getAppUserInfo(this)'>查看信息</button></td>";
                innerHtml += "  <td>" + app.create_time + "</td>";
                innerHtml += "  <td><button class='btn btn-default' name='button" + app.id + "' type='submit' onclick='agree(this," + app.id + ",0)'  >同意</button>";
                innerHtml += "  <button  class='btn btn-default' name='button" + app.id + "' type='submit' onclick='agree(this," + app.id + ",1)' >拒绝</button></td>";
                innerHtml += "</tr>";
            }
        }
        innerHtml += "  </table>";
        innerHtml += "</div>";
        innerHtml += " <button type='button' style='width:70%;margin: auto;' class='btn btn-default btn-lg btn-block' onclick='returnHome()'>返回主页</button>";
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
            if (userInfo.email != sessionStorage.getItem("email")) {
                innerHtml += "<tr>";
                innerHtml += "  <td>" + userInfo.email + "</td>";
                innerHtml += "  <td>" + userInfo.nick_name + "</td>";
                innerHtml += "  <td>" + userInfo.point + "</td>";
                innerHtml += "  <td>" + userInfo.point_last + "</td>";
                innerHtml += "  <td>" + getJurisdiction(userInfo.jurisdiction) + "</td>";
                innerHtml += "  <td>" + getGroupName(userInfo.group_name) + "</td>";
                innerHtml += "  <td><button class='btn btn-default' type='submit' onclick='expel(this," + changeToString(userInfo.email) + ")' >踢出</button></td>";
                innerHtml += "</tr>";
            }
        }
        innerHtml += "  </table>";
        innerHtml += "</div>";
        $("#handlebody").html("");
        $("#handlebody").html(innerHtml);
    }
}

function pageType2() {
    $("#pageTitle").html("创建分组");
    var res = tokenService("/userInfo/alliance", "GET", false, {
        'allianceId': sessionStorage.getItem("myAllianceId"),
        'email': sessionStorage.getItem("email")
    });
    if (res.result) {
        var innerHtml = "";
        innerHtml += "<form>";
        innerHtml += "<div class='form-group'>";
        innerHtml += "<label for='exampleInputEmail1'>分组名称</label>";
        innerHtml += "<input type='text' class='form-control' id='groupName' placeholder='请输入分组名称'>";
        innerHtml += "</div>";
        innerHtml += "<div class='form-group'>";
        innerHtml += "<label for='exampleInputEmail1'>指定管理者</label>";
        innerHtml += "<div class='input-group'>";
        innerHtml += "<div class='input-group-btn'>";
        innerHtml += "<button type='button' class='btn btn-default dropdown-toggle' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>管理者昵称<span class='caret'></span></button>";
        innerHtml += "<ul class='dropdown-menu dropdown-menu-left' style='text-align:center'>";
        var userInfoList = JSON.parse(res.data);
        if (userInfoList.length == 0) {
            innerHtml += "<li><p>无任何符合条件的成员</p></li>";
        }
        for (var index in userInfoList) {
            var userInfo = userInfoList[index];
            if (userInfo.jurisdiction == 2) {
                innerHtml += "<li><a onclick='selectGroupLerder(" + changeToString(userInfo.nick_name) + ")'><span class='glyphicon glyphicon-user' aria-hidden='true'></span>&nbsp;" + userInfo.nick_name + "&nbsp;<span class='glyphicon glyphicon-asterisk' aria-hidden='true'></span>&nbsp;个人总积分:&nbsp;" + userInfo.point + "&nbsp;点&nbsp;<span class='glyphicon glyphicon-flag' aria-hidden='true'></span>&nbsp;当前分组&nbsp;:&nbsp;" + getGroupName(userInfo.group_name) + "</a></li>";
                if (index != userInfoList.length - 1) {
                    innerHtml += "<li role='separator' class='divider'></li>";
                }
            }
        }
        innerHtml += "</ul>";
        innerHtml += "</div>";
        innerHtml += "<input type='text' class='form-control' placeholder='请输入成员昵称' id='leaderName'>";
        innerHtml += "</div>";
        innerHtml += "</div>";
        innerHtml += "<div class='form-group'>";
        innerHtml += "<p></p><label for='exampleInputFile'>分组介绍</label>";
        innerHtml += "<textarea class='form-control' id='group_introduce' rows='2'></textarea>";
        innerHtml += "</div>";
        innerHtml += "<div class='buttonbox'>";
        innerHtml += "<button type='button' class='btn btn-default btn-lg ' onclick='returnHome()'>返回主页</button>";
        innerHtml += "<button type='button' class='btn btn-primary btn-lg ' disabled='disabled' id='createGroup'>创建</button>";
        innerHtml += "</div>";
        innerHtml += "</form>";
        $("#handlebody").html("");
        $("#handlebody").html(innerHtml);
        sessionStorage.setItem("pageType2Date", res.data);
        $("#handlebody ,ul").css("width", $(window).width());
    }
}

$("#handlebody").on("keyup", "#leaderName", function () {
    var userInfoList = JSON.parse(sessionStorage.getItem("pageType2Date"));
    var name = $("#leaderName").val();
    if (userInfoList.length != 0) {
        var innerHtml = "";
        for (var index in userInfoList) {
            var userInfo = userInfoList[index];
            if (userInfo.jurisdiction == 2 && (userInfo.nick_name).indexOf(name) >= 0) {
                innerHtml += "<li><a onclick='selectGroupLerder(" + changeToString(userInfo.nick_name) + ")'><span class='glyphicon glyphicon-user' aria-hidden='true'></span>&nbsp;" + userInfo.nick_name + "&nbsp;<span class='glyphicon glyphicon-asterisk' aria-hidden='true'></span>&nbsp;个人总积分:&nbsp;" + userInfo.point + "&nbsp;点&nbsp;<span class='glyphicon glyphicon-flag' aria-hidden='true'></span>&nbsp;当前分组&nbsp;:&nbsp;" + getGroupName(userInfo.group_name) + "</a></li>";
                if (index != userInfoList.length - 1) {
                    innerHtml += "<li role='separator' class='divider'></li>";
                }
            }
        }
        $("ul").empty();
        $("ul").html(innerHtml);
        if ($("ul li:last-child").attr("class") == "divider") {
            $("ul li:last-child").remove();
        }
        $("ul").css("width", $(window).width());
        $(".input-group-btn").addClass("open");
    }
});
$("#handlebody").bind("keyup", function () {
    changeButtonDisable();
});

function selectGroupLerder(str) {
    $("#leaderName").val(str);
    changeButtonDisable();
}
function changeButtonDisable(){
    if(checkCroupInput()){
        $("#createGroup").removeAttr("disabled");
    }else{
        $("#createGroup").attr("disabled","disabled");
    }
}
function checkCroupInput(){
    var leaderName=$("#leaderName").val().trim();
    if(leaderName.length==0||leaderName.length>20){
        return false;
    }
    var groupName=$("#groupName").val().trim();
    if(groupName.length==0||groupName.length>20){
        return false;
    }
    var group_introduce=$("#group_introduce").val().trim();
    if(group_introduce.length>50){
        return false;
    }
    return true;
}


function getAppUserInfo(object) {
    alert(object.value + "功能还没实现");
}

function changeToString(str) {
    return JSON.stringify(str);
}
function returnHome() {
    parent.layx.destroy(getLayxId());
    window.location.href = "index";
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
function getLayxId() {
    var id;
    if (self != top && self.frameElement && self.frameElement.tagName == "IFRAME") {
        var layxWindow = $(self.frameElement).parents(".layx-window");
        id = layxWindow.attr("id").substr(5);
    }
    return id;
}

function getGroupName(str) {
    if (str == null || str == "" || str == undefined) {
        return "未分组";
    }
    return str;
}
function expel(object, expel_email) {
    var jsonObject = new Object();
    jsonObject.email = sessionStorage.getItem("email");
    jsonObject.expel_email = expel_email;
    var res = tokenService("/userInfo/expel/" + JSON.stringify(jsonObject), "PUT", false, JSON.stringify(jsonObject));
    if (res.result) {
        $(object).html("已踢出");
        $(object).attr('disabled', true);
        $(object).css("background", "grey");
    }
}

function agree(object, id, type) {
    var className = object.name;
    var jsonData = {};
    jsonData.email = sessionStorage.getItem("email");
    jsonData.id = id;
    jsonData.type = type;
    var res = tokenService("/application/agree", " ", false, JSON.stringify(jsonData));
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