/*
 * @Date: 2021-10-14 11:42:56
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-14 13:23:57
 * @FilePath: \stzb\src\main\resources\static\js\login.js
 */
$(function () {
    $("#email").val(sessionStorage.getItem("email"));
    $("#psd").val(sessionStorage.getItem("loginPsd"));
    //为表单元素添加失去焦点事件
    $("form :input").blur(function () {
        $(".result").empty();
        var $parent = $(this).parent();
        $parent.find(".msg").remove(); //删除以前的提醒元素（find()：查找匹配元素集中元素的所有匹配元素）	
        //邮箱验证
        if ($(this).is("#email")) {
            var email = $.trim(this.value);
            if (!checkEmail(email)) {
                var errorMsg = " 请输入正确的邮箱格式！";
                $parent.append("<span class='msg onError'>" + errorMsg + "</span>");
            } else {
                var okMsg = " 输入正确";
                $parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
            }
        }
        //验证密码
        if ($(this).is("#psd")) {
            var psdVal = $.trim(this.value);
            var regPsd = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$/;
            if (psdVal == "" || !regPsd.test(psdVal)) {
                var errorMsg = " 密码为6-20位字母、数字的组合！";
                $parent.append("<span class='msg onError'>" + errorMsg + "</span>");
            }
            else {
                var okMsg = " 输入正确";
                $parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
            }
        }
    }).keyup(function () {
        //triggerHandler 防止事件执行完后，浏览器自动为标签获得焦点
        $(this).triggerHandler("blur");
    }).focus(function () {
        $(this).triggerHandler("blur");
    });

})
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
//点击重置按钮时，通过trigger()来触发文本框的失去焦点事件
$("#btnSubmit").click(function () {
    sessionStorage.setItem("email", $("#email").val());
    sessionStorage.setItem("loginPsd", $("#psd").val());
    var jsonData=new Object();
    jsonData.email=$("#email").val();
    jsonData.userPassword=$("#psd").val();
    var result = noTokenService("/user/login","POST", false,JSON.stringify(jsonData));
    if (result.result) {
        var data = JSON.parse(result.data);
        sessionStorage.setItem("token", data.token);
        sessionStorage.setItem("email", data.email)
        window.location.href = "index";
    } else {
        $(".result").html(result.message);
    }
});
$(".download_btn").click(function () {
    if ($(".QRcode").css("display") == "none") {
        $(".QRcode").show();
        $(".download_btn").text("关闭二维码");
    } else {
        $(".QRcode").hide();
        $(".download_btn").text("请皮卡丘喝奶茶");
    }
});