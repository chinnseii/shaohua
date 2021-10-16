/*
 * @Date: 2021-08-23 14:31:01
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-16 16:51:27
 * @FilePath: \stzb\src\main\resources\static\js\index.js
 */
/**
 * @description: 初始化主页获取各种信息
 * @param {*}
 * @return {*}
 */
function initIndex() {
    var result = tokenService("/userInfo", "GET", false, { 'email': sessionStorage.getItem("email") });
    if (result.result) {
        var userInfo = JSON.parse(result.data);
        $("title").html(userInfo.nick_name)
        $("#signature").val(userInfo.signature);
        sessionStorage.setItem("avatar_path", "profilephoto/" + userInfo.avatar_path);
        $(".profilephoto").attr("src", sessionStorage.getItem("avatar_path"));
        $("#myAlliance").css({ "background-color": "aliceblue" });
        $("#allianceView").css("background-color", "aliceblue");
        var innerHtml = "";
        innerHtml += "<div class='jumbotron'>";
        $("#allianceView").css("background-color", "aliceblue");
        if (userInfo.alliance_id == undefined || userInfo.alliance_id == null || userInfo.alliance_id == "") {
            innerHtml += "<h3>您还没有加入任何同盟</h3>";
            innerHtml += "<p>您可以选择创建同盟或加入同盟</p>";
            innerHtml += "<a class='btn btn-primary btn-lg' role='button'onclick='createAlliance()'>创建同盟</a>";
            innerHtml += "<a class='btn btn-primary btn-lg' role='button'onclick='searchAlliance()'>加入同盟</a>";
            innerHtml += "<div id='createAlliance'></div>";
            innerHtml += "<div id='searchAlliance'></div>";
        } else {
            if (userInfo.jurisdiction < 3) {
                innerHtml += "<h4>普通成员</h4>";
                innerHtml += " <button type='button' class='btn btn-info'>（一般信息）Info</button> ";
                innerHtml += " <button type='button' class='btn btn-info'>（一般信息）Info</button> ";
                innerHtml += " <button type='button' class='btn btn-info'>（一般信息）Info</button> ";
                innerHtml += " <button type='button' class='btn btn-info'>（一般信息）Info</button> ";
                innerHtml += " <button type='button' class='btn btn-info'>（一般信息）Info</button> ";
                innerHtml += " <button type='button' class='btn btn-info'>（一般信息）Info</button> ";
                innerHtml += " <button type='button' class='btn btn-info'>（一般信息）Info</button> ";
            }
            if (userInfo.jurisdiction < 2) {
                innerHtml += "<h4>管理</h4>";
                innerHtml += " <button type='button' class='btn btn-warning'>（警告）Warning</button>";
                innerHtml += " <button type='button' class='btn btn-warning'>（警告）Warning</button>";
                innerHtml += " <button type='button' class='btn btn-warning'>（警告）Warning</button>";
                innerHtml += " <button type='button' class='btn btn-warning'>（警告）Warning</button>";
                innerHtml += " <button type='button' class='btn btn-warning'>（警告）Warning</button>";
                innerHtml += " <button type='button' class='btn btn-warning'>（警告）Warning</button>";
            }
            if (userInfo.jurisdiction < 1) {
                innerHtml += "<h4>盟操作</h4>";
                innerHtml += "<button type='button' class='btn btn-danger'>解散同盟</button>";
            }
        }
        innerHtml += "</div>";
        $("#allianceView").append(innerHtml);
    }
}
$("#allianceView").on("click", "#create", () => {
    var name = $("#allianceName").val();
    if (name.length == 0) {
        layx.msg('请输入同盟名称', { dialogIcon: 'warn' });
        return;
    }
    var introduce = $("#introduce").val();
    if (introduce.length > 50) {
        layx.msg('你可以不写但是不能写多', { dialogIcon: 'warn' });
        return;
    }
    var jsonData = new Object();
    jsonData.name = name;
    jsonData.introduce = introduce;
    jsonData.email = sessionStorage.getItem("email");
    var result = tokenService("/alliance", "POST", true, JSON.stringify(jsonData));
    if (result.result) {
        window.location.href = "index";
    }
});

/**
* @description: 
* @param {*}
* @return {*}
*/
function createAlliance() {
    $("#createAlliance").html("");
    $("#searchAlliance").html("");
    var innerHtml = "";
    innerHtml += "<div class='form-inline'>";
    innerHtml += "<div class='form - group'>";
    innerHtml += "<label for='exampleInputName2'><h4>同盟名称:　</h4></label>";
    innerHtml += "<input type='text' class='form - control' id='allianceName' placeholder='长度不能超过10'>";
    innerHtml += "</div>";
    innerHtml += "<div class='form - group'>";
    innerHtml += "<label for='exampleInputName2'><h4>同盟介绍:　</h4></label>";
    innerHtml += "<textarea class='form-control' id='introduce' rows='3' placeholder='50个字以内，别写多了'></textarea>";
    innerHtml += "</div>";
    innerHtml += "<button class='btn btn-default' id='create'>创建</button>";
    innerHtml += "</div>";
    $("#createAlliance").append(innerHtml);
}
function searchAlliance() {
    $("#createAlliance").html("");
    $("#searchAlliance").html("");
    var innerHtml = "";
    innerHtml += "<div class='input-group'>";
    innerHtml += "<input type='text' id='search' class='form-control' aria-label='...'>";
    innerHtml += "<div class='input-group-btn'>";
    innerHtml += "<button type='button' class='btn btn-default dropdown-toggle' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>检索 <span class='caret'></span></button>";
    innerHtml += "<ul class='dropdown-menu dropdown-menu-right'>";
    innerHtml += "<li><a id='idSearch'>盟ID检索</a></li>";
    innerHtml += "<li><a id='nameSearch'>盟名检索</a></li>";
    innerHtml += "</ul>";
    innerHtml += "</div>";
    innerHtml += "</div>";
    innerHtml += "<div id='searchresult'></div>";
    innerHtml += "</div>";
    $("#searchAlliance").append(innerHtml);
}
/**
 * @description: 根据同盟名检索同盟信息
 * @param {*} click
 * @param {*} nameSearch
 * @param {*} function
 * @return {*}
 */
$("#allianceView").on("click", "#idSearch", function () {
    var search = $("#search").val();
    if (search.length == 10 && !isNaN(search)) {
        var res = tokenService("/alliance/search", "GET", false, { 'search': search, 'searchType': 0, 'email': sessionStorage.getItem("email") });
        if (res.result) {
            var innerHtml = "";
            if(res.data==""||res.data=="{}"||res.data==null||res.data.length==0){
                innerHtml += "<h4>未查询到相关同盟，请确认后重试</h4>";
            }else{
                var alliance= JSON.parse(res.data)[0];
                innerHtml += "<div class='table-responsive'>";
                innerHtml += "  <table class='table table-bordered'>";
                innerHtml += "<tr>";
                innerHtml += "  <td class='info'><h5>盟ID</h5></td>";
                innerHtml += "  <td class='info'><h5>同盟名</h5></td>";
                innerHtml += "  <td class='info'><h5>盟主</h5></td>";
                innerHtml += "  <td class='info'><h5>人数</h5></td>";
                innerHtml += "  <td class='info'><h5>盟介绍</h5></td>";
                innerHtml += "  <td class='info'><h5>创建时间</h5></td>";
                innerHtml += "  <td class='info'><h5>申请</h5></td>";
                innerHtml += "</tr>";
                innerHtml += "<tr>";
                innerHtml += "  <td>"+alliance.alliance_Id+"</td>";
                innerHtml += "  <td>"+alliance.name+"</td>";
                innerHtml += "  <td>"+alliance.own_name+"</td>";
                innerHtml += "  <td>"+alliance.population+"</td>";
                innerHtml += "  <td>"+alliance.introduce+"</td>";
                innerHtml += "  <td>"+alliance.create_time+"</td>";
                innerHtml += "  <td>"+alliance.application+"</td>";
                innerHtml += "</tr>";
                innerHtml += "  </table>";
                innerHtml += "</div>";
            }
            $("#searchresult").html("");
            $("#searchresult").html(innerHtml);
        }
    } else {
        layx.msg('请输入正确的同盟ID', { dialogIcon: 'warn' });
    }
});
/**
 * @description: 根据同盟ID检索同盟信息
 * @param {*} click
 * @param {*} idSearch
 * @param {*} function
 * @return {*}
 */
$("#allianceView").on("click", "#nameSearch", function () {
    var search = $("#search").val();
    if (search.length > 20 || search.length == 0) {
        layx.msg('请输入正确的同盟名称', { dialogIcon: 'warn' });
    } else {
        var res = tokenService("/alliance/search", "GET", false, { 'search': search, 'searchType': 1, 'email': sessionStorage.getItem("email") });
    }

});


/**
 * @description: 更新头像页面呼出
 * @param {*} function
 * @return {*}
 */
$("#changeProfilePhoto").click(function () {
    layx.iframe('shadow-color', 'アバター更新', 'changeProfilePhoto', {
        shadable: 0.8
    });
    layx.setSize('shadow-color', { width: 950, height: 600 });
});
/**
 * @description: 登出
 * @param {*}
 * @return {*}
 */
function loginOut() {
    layx.confirm('登出', '是否要退出登录', null, {
        buttons: [
            {
                label: 'YSE',
                callback: function (id, button, event) {
                    sessionStorage.clear();
                    window.location.href = "login";
                }
            },
            {
                label: 'NO',
                callback: function (id, button, event) {
                    layx.destroy(id);
                }
            }
        ]
    });
}

/**
 * @description: 呼出笔记创建页面
 * @param {*}
 * @return {*}
 */
function createNote() {
    layx.iframe('shadow-color', 'ノート作成', 'createNote', {
        shadable: 0.8
    });
    layx.setSize('shadow-color', { width: 700, height: 600 });
}

/**
 * @description: 根数页数与状态，获取某一页笔记
 * @param {*} page
 * @param {*} status
 * @return {*}
 */
function getNote(page, status) {
    sessionStorage.setItem("pageNo", page);
    var getNoteObject = new Object();
    getNoteObject.email = sessionStorage.getItem("email");
    getNoteObject.status = status;
    getNoteObject.page = page;
    var jsonGetNote = JSON.stringify(getNoteObject);
    var res = javaService("/getNote", jsonGetNote);
    if (JSON.stringify(res) == '{}') {
        $("#noteView").empty();
        $("#noteView").append("<button type='button' id='createNote' class='btn btn-primary' onclick='createNote()'>ノート作成</button>");
    } else {
        noteView(page, res, status);
    }
}

/**
 * @description: 显示一页笔记
 * @param {*} page
 * @param {*} res
 * @return {*}
 */
function noteView(page, res, status) {
    var count = res.count;
    delete res.count;
    $("#noteView").empty();
    var innerHtml = "";
    for (var index in res) {
        var content = JSON.parse(res[index]);
        innerHtml += "<div class='panel panel-default'>";
        innerHtml += "<div class='panel-heading'>";
        innerHtml += "<h4>" + content.title;
        innerHtml += "<button type='button' class='btn btn-default' aria-label='Left Align' onclick='deleteNote(" + content.id + ")' style='float:right'>";
        innerHtml += "<span class='glyphicon glyphicon-trash' aria-hidden='true'></span>";
        innerHtml += "</button>";
        innerHtml += "<button type='button' class='btn btn-default' aria-label='Left Align' onclick='editNote(" + content.id + ")' style='float:right'>";
        innerHtml += "<span class='glyphicon glyphicon-pencil' aria-hidden='true'></span>";
        innerHtml += "</button>";
        innerHtml += "<button type='button' class='btn btn-default' style='float:right'>";
        innerHtml += "<span class='glyphicon glyphicon-star-empty' aria-hidden='true'></span> Star";
        innerHtml += "</button>";
        innerHtml += "</h4>";
        innerHtml += "<span class='label label-success' style=' margin-right: 3px;'>" + content.category_name + "</span>";
        innerHtml += "<span class='label label-info' style=' margin-right: 3px;'>star&nbsp;<span class='badge'>" + content.star + "</span></span>";
        innerHtml += "<span class='label label-warning' style=' margin-right: 3px;'>更新時間&nbsp;" + getTime(content.update_date) + "</span>";
        innerHtml += "</div>";
        innerHtml += "<div class='panel-body'>";
        innerHtml += "<pre>" + html2Escape(content.content) + "</pre>";
        innerHtml += "</div></div>";
    }
    innerHtml += "<nav aria-label='Page navigation'>";
    innerHtml += "<ul class='pagination'>";
    innerHtml += "<li>";
    innerHtml += "<a href='#' aria-label='Previous' onclick='getPrePage(" + status + ")'>";
    innerHtml += "<span aria-hidden='true'>&laquo;</span>";
    innerHtml += "</a>";
    innerHtml += "</li>";
    var allPage;
    if (count % 3 != 0) {
        allPage = Math.ceil(count / 3);
    } else {
        allPage = count / 3;
    }
    sessionStorage.setItem("allPage", allPage);
    var endPage;
    var startPage;
    //最多显示七页
    if (allPage < 7) {
        startPage = 0;
        endPage = allPage;
    } else {
        if (page <= 3) {
            startPage = 0;
            endPage = 6;
        } else {
            startPage = Number(page) - 3;
            endPage = Number(page) + 3;
        }
    }
    if (endPage > allPage) {
        endPage = allPage;
        startPage = Number(allPage) - 6;
    }
    for (var i = startPage; i < endPage; i++) {
        if (i == sessionStorage.getItem("pageNo")) {
            innerHtml += "<li><a href='#' style='pointer-events: none;background-color:#D3D3D3'>" + parseInt(i + 1) + "</a></li>";
        } else {
            innerHtml += "<li><a href='#' onclick='getNote(" + i + "," + status + ")'>" + parseInt(i + 1) + "</a></li>";
        }

    }
    innerHtml += "<li>";
    innerHtml += "<a href='#' aria-label='Next'  onclick='getNextPage(" + status + ")'>";
    innerHtml += "<span aria-hidden='true'>&raquo;</span>";
    innerHtml += "</a>";
    innerHtml += "</li>";
    innerHtml += "</ul>";
    innerHtml += "</nav>";
    $("#noteView").append(innerHtml);
}

/**
 * @description: 显示下一页
 * @param {*} status
 * @return {*}
 */
function getNextPage(status) {
    var pageNo = sessionStorage.getItem("pageNo");
    var allPage = sessionStorage.getItem("allPage");
    var newPage = Number(pageNo) + 1;
    if (newPage >= allPage) {
        getNote(pageNo, status);
    } else {
        getNote(newPage, status);
    }

}

/**
 * @description: 显示上一页
 * @param {*} status
 * @return {*}
 */
function getPrePage(status) {
    var pageNo = sessionStorage.getItem("pageNo");
    if (pageNo == 0) {
        getNote(parseInt(pageNo), status);
    } else {
        getNote((Number(pageNo) - 1), status);
    }
}

/**
 * @description: 时间变换成年月日 00:00:00格式
 * @param {*} date
 * @return {*}
 */
function getTime(date) {
    return date.substring(0, 4) + "年" + date.substring(4, 6) + "月" + date.substring(6, 8) + "日 " + date.substring(8, 10) + ":" + date.substring(10, 12) + ":" + date.substring(12, 14)
}


/**
 * @description: 删除一条笔记
 * @param {*} noteId
 * @return {*}
 */
function deleteNote(noteId) {
    layx.confirm('WARN', 'このノートを削除しますか', function (id) {
        var jsonObject = new Object();
        jsonObject.id = noteId;
        jsonObject.email = sessionStorage.getItem("email");
        var jsonData = JSON.stringify(jsonObject);
        var json = javaService("/deleteNote", jsonData);
        if (json.result) {
            layx.msg('ノート作成成功しました。', { dialogIcon: 'success' });
        }
        layx.destroy('loadId');
        parent.location.reload();
    }, { dialogIcon: 'warn' });
}


/**
 * @description:编辑笔记 
 * @param {*} noteId
 * @return {*}
 */
function editNote(noteId) {
    sessionStorage.setItem("editNoteId", noteId);
    layx.iframe('shadow-color', 'ノート作成', 'editNote', {
        shadable: 0.8
    });
    layx.setSize('shadow-color', { width: 700, height: 600 });
}

/**
 * @description: HTML标签转义（< -> &lt;）
 * @param {*} sHtml
 * @return {*}
 */
function html2Escape(sHtml) {
    return sHtml.replace(/[<>&"]/g, function (c) {
        return { '<': '&lt;', '>': '&gt;', '&': '&amp;', '"': '&quot;' }[c];
    });
}

$("#createCategory").click(function () {
    layx.prompt('科目作成', '科目名を入力してください。', function (id, value, textarea, button, event) {
        layx.confirm('', '科目: ' + value + ' 公開しますか？', null, {
            buttons: [
                {
                    label: 'はい',
                    callback: function (id, button, event) {
                        createCategory(value, 0);
                        layx.destroy(id);
                    }
                },
                {
                    label: 'いええ',
                    callback: function (id, button, event) {
                        createCategory(value, 1);
                        layx.destroy(id);
                    }
                }
            ]
        });
    });
});
$("#signature").blur(function () {
    var headerObject = new Object();
    headerObject.token = sessionStorage.getItem("token");
    headerObject.email = sessionStorage.getItem("email");
    var headerInfo = JSON.stringify(headerObject);
    if (sessionStorage.getItem("signature") != $("#signature").val()) {
        $.ajax({
            type: "POST",
            url: "/updateUserSignature",
            beforeSend: function (request) {
                request.setRequestHeader("header", headerInfo);
            },
            data: {
                'email': sessionStorage.getItem("email"),
                'signature': $("#signature").val()
            },
            success: function (res) {
                var result;
                if (typeof res == "string") {
                    result = JSON.parse(res);
                } else {
                    result = res;
                }
                if (result.errorCode != undefined) {
                    errorCode(result.errorCode);
                }
                if (!result.res) {
                    layx.msg('個人説明更新失敗しました、も一度更新してください。', { dialogIcon: 'error' });
                } else {
                    sessionStorage.setItem("signature", $("#signature").val());
                    layx.alert('個人説明更新', '個人説明更新成功', null, { dialogIcon: 'success' });
                }
            },
            error: function (result) {
                errorCode(result.status);
            }
        });
    }
});

