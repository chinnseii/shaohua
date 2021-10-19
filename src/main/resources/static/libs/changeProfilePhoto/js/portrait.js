//默认图像位置
function cutImage(obj, w, h) {
    var iw = obj.width(),
        ih = obj.height();
    if (iw >= w || ih >= h) {
        if (iw / ih > w / h) {
            obj.css({
                width: w,
                height: w * ih / iw,
                marginTop: (h - w * ih / iw) / 2
            });
            obj.parent().css({
                width: w,
                height: w * ih / iw
            });
            if (w == 300) {
                obj.css({
                    width: w,
                    height: w * ih / iw,
                    margin: 0
                });
            }
        } else {
            obj.css({
                height: h,
                width: h * iw / ih,
                marginLeft: (w - h * iw / ih) / 2
            });
            obj.parent().css({
                height: h,
                width: h * iw / ih
            });
            if (h == 300) {
                obj.css({
                    height: h,
                    width: h * iw / ih,
                    margin: 0
                });
            }
        }
    } else {
        var iw = obj.parent().width(),
            ih = obj.parent().height();
        if (iw / ih > w / h) {
            obj.css({
                width: w,
                height: w * ih / iw,
                marginTop: (h - w * ih / iw) / 2
            });
        } else {
            obj.css({
                height: h,
                width: h * iw / ih,
                marginLeft: (w - h * iw / ih) / 2
            });
        }
    }
}

function resetImgSize(obj) {
    var imgWidth = $(obj).width();
    var imgHeight = $(obj).height();
    if (imgWidth > imgHeight) {
        var scale = 300 / imgWidth;
        var newHeight = imgHeight * scale;
        $(obj).css({ width: 300, height: newHeight });
        $(obj).parent().css({ width: 300, height: newHeight });
    } else {
        var scale = 300 / imgHeight;
        var newWidth = imgWidth * scale;
        $(obj).css({ width: newWidth, height: 300 });
        $(obj).parent().css({ width: newWidth, height: 300 });
    }
}
//提交，保存图片

//{放大缩小
//放大缩小图片
function zoomIn() {
    var $scale = $("#scale").val();
    if ($scale < 10 && $("#selectArea").is(":visible")) {
        imgToSize(20);
        var position = parseInt($("#sliderBlock").position().left);
        if ($("#sliderBlock").position().left > 0) {
            position = parseInt(position / 220 * 100);
        }
        $("#scale").val(parseInt($scale) + 1);
        $("#sliderBlock").css({ left: "" + parseInt(position + 10) + "%" });
    }
}
function zoomOut() {
    var $scale = $("#scale").val();
    if ($scale != 0 && $("#selectArea").is(":visible")) {
        imgToSize(-20);
        var position = parseInt($("#sliderBlock").position().left);
        if ($("#sliderBlock").position().left > 0) {
            position = parseInt(position / 220 * 100);
        }
        $("#scale").val(parseInt($scale) - 1);
        $("#sliderBlock").css({ left: "" + parseInt(position - 10) + "%" });
    }
}
$("#idBig").click(function (e) {
    zoomIn();
    return false;
});
$("#idSmall").click(function (e) {
    zoomOut();
    return false;
});
function imgToSize(size) {
    var iw = $('#avatarPrototype>img').width(),
        ih = $('#avatarPrototype>img').height(),
        _data = $("#rotation").val(),
        _w = Math.round(iw + size),
        _h = Math.round(((iw + size) * ih) / iw);

    if ((BasisUI.browser.msie) && (_data == 90 || _data == 270)) {
        $('#avatarPrototype>img').width(_h).height(_w);
    } else {
        $('#avatarPrototype>img').width(_w).height(_h);
    }

    var fiw = $('#avatarPrototype>img').width(),
        fih = $('#avatarPrototype>img').height(),
        ow = ($('#avatarPrototype').width() - fiw) / 2,
        oh = ($('#avatarPrototype').height() - fih) / 2,
        cx = $("#selectArea").position().left,
        cy = $("#selectArea").position().top,
        rx = 160 / $("#selectArea").width(),
        ry = 160 / $("#selectArea").height(),
        rx1 = 100 / $("#selectArea").width(),
        ry1 = 100 / $("#selectArea").height(),
        rx2 = 50 / $("#selectArea").width(),
        ry2 = 50 / $("#selectArea").height();

    if ((BasisUI.browser.msie) && (_data == 90 || _data == 270)) {
        pre_img2($('#avatarPreviewBig img'), rx, fih, ry, fiw, cx, cy, ow, oh);
        pre_img2($('#avatarPreviewNormal img'), rx1, fih, ry1, fiw, cx, cy, ow, oh);
        pre_img2($('#avatarPreviewSmall img'), rx2, fih, ry2, fiw, cx, cy, ow, oh);
    } else {
        pre_img2($('#avatarPreviewBig img'), rx, fiw, ry, fih, cx, cy, ow, oh);
        pre_img2($('#avatarPreviewNormal img'), rx1, fiw, ry1, fih, cx, cy, ow, oh);
        pre_img2($('#avatarPreviewSmall img'), rx2, fiw, ry2, fih, cx, cy, ow, oh);
    }
    $("#avatarPrototype img").css({
        left: ow,
        top: oh
    });

    var X1 = cx - ow,
        X2 = X1 + $("#selectArea").width(),
        Y1 = cy - oh,
        Y2 = Y1 + $("#selectArea").height();

    $('#offsetX1').val(X1);
    $('#offsetX2').val(X2);
    $('#offsetY1').val(Y1);
    $('#offsetY2').val(Y2);
};

function submitAvatar() {
    var headerObject = new Object();
    headerObject.token = sessionStorage.getItem("token");
    headerObject.email = sessionStorage.getItem("email");
    var headerInfo = JSON.stringify(headerObject);
    var formData = new FormData($("#cropForm")[0]);  //创建一个forData 
    formData.append('email', sessionStorage.getItem("email"));
    $.ajax({
        type: "POST",
        url: "/updateProfilePhoto",
        data: formData,
        dataType: 'JSON',
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        beforeSend: function (request) {
            request.setRequestHeader("header", headerInfo);
        },
        success: function (result) {
            if(result==undefined){
                layx.msg("[[${errorMsg}]]",{dialogIcon:'warn'});
            }
            if (!result.res) {
                layx.msg(result.defeat,{dialogIcon:'error'});
            }else{
                layx.load('loadId', '头像更新中...');
                setTimeout(function () {
                    layx.destroy('loadId');
                    parent.location.reload();
                }, 500);   
            }                  
        }
    });
}
function saveCallBack(responseText) {
    if (responseText.status) {
        $("#memberZoomPortrait").attr("src", responseText.imageUrl);
        alertMessagePopup("编辑头像成功");
        if ($("#rotation").val() != 0) {
            window.location.reload();
        }
    } else {
        alertMessagePopup("编辑头像失败");
    }
}
function pre_img2(obj, rx, iw, ry, ih, cx, cy, ow, oh) {
    obj.css({
        width: Math.round(rx * iw) + 'px',
        height: Math.round(ry * ih) + 'px'
    });
    if (cy >= oh && cx >= ow) {
        obj.css({
            marginLeft: '-' + Math.round(rx * (cx - ow)) + 'px',
            marginTop: '-' + Math.round(ry * (cy - oh)) + 'px'
        });
    } else if (cy <= oh && cx >= ow) {
        obj.css({
            marginLeft: "-" + Math.round(rx * (cx - ow)) + 'px',
            marginTop: Math.round(ry * (oh - cy)) + 'px'
        });
    } else if (cy >= oh && cx <= ow) {
        obj.css({
            marginLeft: Math.round(rx * (ow - cx)) + 'px',
            marginTop: '-' + Math.round(ry * (cy - oh)) + 'px'
        });
    } else if (cy <= oh && cx <= ow) {
        obj.css({
            marginLeft: Math.round(rx * (ow - cx)) + 'px',
            marginTop: Math.round(ry * (oh - cy)) + 'px'
        });
    }

};
function showPreview(c) {
    var iw = $('#avatarPrototype>img').width(),
        ih = $('#avatarPrototype>img').height(),
        ow = ($('#avatarPrototype').width() - iw) / 2,
        oh = ($('#avatarPrototype').height() - ih) / 2,
        rx = 160 / c.w,
        ry = 160 / c.h,
        rx1 = 100 / c.w,
        ry1 = 100 / c.h,
        rx2 = 50 / c.w,
        ry2 = 50 / c.h;
    pre_img2($('#avatarPreviewBig img'), rx, iw, ry, ih, c.x, c.y, ow, oh);
    pre_img2($('#avatarPreviewNormal img'), rx1, iw, ry1, ih, c.x, c.y, ow, oh);
    pre_img2($('#avatarPreviewSmall img'), rx2, iw, ry2, ih, c.x, c.y, ow, oh);
    var X1 = c.x - ow,
        X2 = c.x2 - ow,
        Y1 = c.y - oh,
        Y2 = c.y2 - oh;
    $('#offsetX1').val(X1);
    $('#offsetX2').val(X2);
    $('#offsetY1').val(Y1);
    $('#offsetY2').val(Y2);
}

function resetAvatar() {
    imgToSize(-$("#scale").val() * 20);
    cutImage($("#avatarPreviewBig>img"), 160, 160);
    cutImage($("#avatarPreviewNormal>img"), 100, 100);
    cutImage($("#avatarPreviewSmall>img"), 50, 50);
    $("#sliderBlock").css({ left: 0 });
    $("#scale").val(0);
    $('#offsetX1').val(0);
    $('#offsetX2').val(0);
    $('#offsetY1').val(0);
    $('#offsetY2').val(0);
}