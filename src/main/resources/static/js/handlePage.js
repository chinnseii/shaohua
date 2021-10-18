/*
 * @Date: 2021-08-24 17:39:04
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-18 21:58:09
 * @FilePath: \stzb\src\main\resources\static\js\handlePage.js
 */
$(function () {
    var pageType = sessionStorage.getItem("pageType");
    switch (pageType) {
        case "0":
            pageType0();
            break;
        case 401:
            layx.msg('402:身份令牌不存在，请重新登录', { dialogIcon: 'error' });
            setTimeout(function () {
                window.location.href = "login";
            }, 3000);
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
        innerHtml += "  <td class='info'><h5>申请时间</h5></td>";
        innerHtml += "  <td class='info'><h5>是否同意</h5></td>";
        innerHtml += "</tr>";
        var appList = JSON.parse(res.data);
        for (var appIndex in appList) {
            var app=appList[appIndex];
            innerHtml += "<tr>";
            innerHtml += "  <td>" + app.nick_name + "</td>";
            innerHtml += "  <td>" + app.create_time + "</td>";
            innerHtml += "  <td><button class='btn btn-default' type='submit' >同意</button>";
            innerHtml += "  <button class='btn btn-default' type='submit' >拒绝</button>";
            innerHtml += "</td>";
            innerHtml += "</tr>";
        }
        innerHtml += "  </table>";
        innerHtml += "</div>";
        $("#handlebody").html("");
        $("#handlebody").html(innerHtml);
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