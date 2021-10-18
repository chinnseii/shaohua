/*
 * @Date: 2021-08-24 17:39:04
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-08 15:51:22
 * @FilePath: \note\src\main\resources\static\js\editNote.js
 */
$(function () {
    //根据ID获取要编辑的笔记
    var editNoteInfo = getNoteById();
    //设置状态
    if (editNoteInfo.status == 1) {
        $("#status").val("私有");
    }
    //获取已创建的科目，生成选择框
    var jsonObject = new Object();
    jsonObject.email = sessionStorage.getItem("email");
    var jsonData = JSON.stringify(jsonObject);
    var optionlist = javaService("/getCategoryName", jsonData);
    sessionStorage.setItem("jsonData",JSON.stringify(optionlist));
    if (JSON.stringify(optionlist) !== '{}') {
        for (var temp in optionlist) {
            $("#subjectsList").append("<option>" + optionlist[temp] + "</option>");
        }
    }
    $("#subjectsList").val(editNoteInfo.category_name);
    $("#content").val(editNoteInfo.content);
    $("#title").val(editNoteInfo.title);
    if (checkInput()) {
        $("#confirm").removeAttr("disabled");
    } else {
        $("#confirm").attr("disabled", "disabled");
    }
})
/**根据输入情况，禁用与开启确认按钮
 * @description: 
 * @param {*}
 * @return {*}
 */
$("#content,#title").keyup(() => {
    if (checkInput()) {
        $("#confirm").removeAttr("disabled");
    } else {
        $("#confirm").attr("disabled", "disabled");
    }
})

/**
 * @description: 检测输入情况
 * @param {*}
 * @return {*}
 */
function checkInput() {
    if ($("#title").val() == "") {
        return false;
    }
    if ($("#title").val().length > 50) {
        layx.msg('タイトルを50文字以下に設定してください', { dialogIcon: 'warn' });
    }
    if ($("#content").val() == "") {
        return false;
    }
    if ($("#content").val().length > 4999) {
        layx.msg('内容を5000文字以下に設定してください', { dialogIcon: 'warn' });
    }
    return true;
}

/**重置输入框
 * @description: 
 * @param {*}
 * @return {*}
 */
function reset() {
    $("select:first option:first").attr("selected", true).siblings("option").attr("selected", false);
    $("#content").val();
    $("#title").val();
}

function editNote() {
    var jsonObject = new Object();
    jsonObject.email = sessionStorage.getItem("email");
    jsonObject.id = sessionStorage.getItem("editNoteId");
    jsonObject.subjects = $("#subjectsList option:selected").text();
    if ($("#status option:selected").text() == "公開") {
        jsonObject.status = "0";
    } else {
        jsonObject.status = "1";
    }
    jsonObject.title = $("#title").val();
    jsonObject.content = $("#content").val();
    var jsonData = JSON.stringify(jsonObject);
    var res = javaService("/updateNote", jsonData);
    if (res.result) {
        layx.msg('ノート更新成功', { dialogIcon: 'success' });
        layx.destroy('loadId');
        parent.location.reload();
    }
}

/**
 * @description: ajax调用JAVA接口(同步)
 * @param {*} url
 * @param {*} jsonData
 * @return {*}
 */
function javaService(url, jsonData) {
    var headerObject = new Object();
    headerObject.token = sessionStorage.getItem("token");
    headerObject.email = sessionStorage.getItem("email");
    var headerInfo = JSON.stringify(headerObject);
    var res;
    $.ajax({
        type: "POST",
        url: url,
        async: false,
        beforeSend: function (request) {
            request.setRequestHeader("header", headerInfo);
        },
        data: {
            'jsonData': jsonData
        },
        success: function (returnValue) {
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

function getNoteById() {
    var jsonObject = new Object();
    jsonObject.email = sessionStorage.getItem("email");
    jsonObject.id = sessionStorage.getItem("editNoteId");
    var jsonData = JSON.stringify(jsonObject);
    return javaService("/getNoteById", jsonData);
}