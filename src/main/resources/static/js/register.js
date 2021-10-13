/*
 * @Date: 2021-10-12 21:48:57
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-13 21:52:04
 * @FilePath: \stzb\src\main\resources\static\js\register.js
 */
$(function () {
    $("#emailcheck").hide();
    $(".createEmailCode").hide();
    $("#btnSubmit").attr('disabled', true);
    // Retrieve
    $("#name").val(sessionStorage.getItem("name"));
    $("#email").val(sessionStorage.getItem("email"));
    $("#psd").val(sessionStorage.getItem("psd"));
    sessionStorage.clear();
    //为表单元素添加失去焦点事件
    $("form :input").blur(function () {
        $(".result").empty();
        var $parent = $(this).parent();
        $parent.find(".msg").remove(); //删除以前的提醒元素（find()：查找匹配元素集中元素的所有匹配元素）
        //验证姓名
        if ($(this).is("#name")) {
            var nameVal = $.trim(this.value);
            var regName = /[~#^$@%&!*()<>:;'"{}【】  ]/;
            if (nameVal == "" || nameVal.length < 2 || regName.test(nameVal)) {
                var errorMsg = " 姓名非空，长度2-20位，不包含特殊字符！";
                $parent.append("<span class='msg onError'>" + errorMsg + "</span>");
            } else {
                var okMsg = " 输入正确";
                $parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
            }
        }
        //验证邮箱
        if ($(this).is("#email")) {
            var email = $.trim(this.value);
            if (!checkEmail(email)) {
                var errorMsg = " 请输入正确的邮箱格式！";
                $(".createEmailCode").hide();
                $("#emailcheck").hide();
                $parent.append("<span class='msg onError'>" + errorMsg + "</span>");
            } else {
                var okMsg = " 输入正确";
                $parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
                $(".createEmailCode").show();
                $("#emailcheck").show();
            }
        }
        //验证密码
        if ($(this).is("#psd")) {
            var psdVal = $.trim(this.value);
            var regPsd = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$/;
            if (psdVal == "" || !regPsd.test(psdVal)) {
                var errorMsg = " 密码为6-20位字母、数字的组合！";
                $parent.append("<span class='msg onError'>" + errorMsg + "</span>");
            } else {
                var okMsg = " 输入正确";
                $parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
            }
        }
        //验证验证码
        if ($(this).is("#emailcheck")) {
            var input = $.trim(this.value);
            if (!checkRate(input)) {
                var errorMsg = " 验证码为6位数字的组合！";
                $parent.append("<span class='msg onError'>" + errorMsg + "</span>");
            } else {
                var okMsg = " 输入正确";
                $parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
            }
        }
    }).keyup(function () {
        //triggerHandler 防止事件执行完后，浏览器自动为标签获得焦点
        $(this).triggerHandler("blur");
        checkAll();
    }).focus(function () {
        $(this).triggerHandler("blur");
        checkAll();
    });
    //点击确认后记录输入内容
    $("#btnSubmit").click(function () {
        sessionStorage.setItem("name", $("#name").val());
        sessionStorage.setItem("email", $("#email").val());
        sessionStorage.setItem("psd", $("#psd").val());
        var json = new Object();
        json.nickName = $("#name").val();
        json.email = $("#email").val();
        json.userPassword = $("#psd").val();
        json.emailcheck = $("#emailcheck").val();
        var res = noTokenService("/user/register", "POST", false, JSON.stringify(json));
        if (res.result) {
            $(".result").html("注册成功，开始登陆...");
            setTimeout(function () {
                var loginres = noTokenService("/user/login", "POST", false, JSON.stringify(json));
                if (loginres.result) {
                    var data = JSON.parse(loginres.data);
                    sessionStorage.setItem("token", data.token);
                    sessionStorage.setItem("email", data.email)
                    window.location.href = "index";
                } else {
                    $(".result").html(loginres.message);
                }
            }, 1500);
        } else {
            $(".result").html(res.message);
        }

    });
})

function checkAll() {
    var name = $("#name").val();
    var psd = $("#psd").val();
    var email = $("#email").val();
    var emailcheck = $("#emailcheck").val();
    var numError = $("form .onError").length;
    if (numError == 0 && name.length != 0 && psd.length != 0 && email.length != 0 && emailcheck.length != 0) {
        $("#btnSubmit").attr('disabled', false);
    } else {
        $("#btnSubmit").attr('disabled', true);
    }
};

$("#name, #psd, #email, #emailcheck").keyup(() => {
    var name = $("#name").val();
    var psd = $("#psd").val();
    var email = $("#email").val();
    var emailcheck = $("#emailcheck").val();
    var numError = $("form .onError").length;
    if (numError == 0 && name.length != 0 && psd.length != 0 && email.length != 0 && emailcheck.length != 0) {
        $("#btnSubmit").attr('disabled', false);
    } else {
        $("#btnSubmit").attr('disabled', true);
    }

});
function checkEmail(str) {
    var reg = /^[0-9a-zA-Z_.-]+[@][0-9a-zA-Z_.-]+([.][a-zA-Z]+){1,2}$/; //正则表达式
    if (str === "") { //输入不能为空
        return false;
    } else if (!reg.test(str)) { //正则验证不通过，格式不对
        return false;
    } else {
        return true;
    }
}
function checkRate(input) {
    if ($.isNumeric(input) && input.length == 6) {
        return true;
    }
    return false;
}
function createEmailCode() {
    $(".createEmailCode").attr('disabled', true);
    $.ajax({
        type: "POST",
        url: "/user/register/email",
        async: true,
        data: {
            'email': $("#email").val()
        },
        success: function (returnValue) {
            var res = toObject(returnValue);
            if (res.result) {
                $('.createEmailCode').html("邮件发送成功");
                setTimeout("$('.createEmailCode').html(60)", 1000);
                setTimeout("daojishi()", 1000);
            } else {
                errorMsg(res.code, res.message);
                setTimeout("$('.createEmailCode').html(60)", 1000);
                setTimeout("daojishi()", 1000);
            }
        },
        error: function (error) {
            errorCode(error.status);
        }
    });
    $(".createEmailCode").html("邮件发送中...");
}
function daojishi() {
    $(".createEmailCode").css("background-color", "grey");
    var starttime = $(".createEmailCode").html();
    if (starttime == 0) {
        $(".createEmailCode").attr('disabled', false);
        $(".createEmailCode").css("background-color", "lightseagreen");
        $(".createEmailCode").html("发送验证码");
        return;
    }
    starttime--;
    $(".createEmailCode").html(starttime);
    setTimeout("daojishi()", 1000);
}
$(".download_btn").click(function () {
    if ($(".QRcode").css("display") == "none") {
        $(".QRcode").show();
        $(".download_btn").text("关闭二维码");
    } else {
        $(".QRcode").hide();
        $(".download_btn").text("请皮卡丘喝奶茶");
    }
});