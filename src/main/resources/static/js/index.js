/*
 * @Date: 2021-08-23 14:31:01
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-26 18:04:54
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
            sessionStorage.setItem("myAllianceId", userInfo.alliance_id);
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
                innerHtml += " <button type='button' class='btn btn-warning' onclick='allianceAppHandle(this,0)'>同盟申请&nbsp;";
                if (userInfo.application > 0) {
                    innerHtml += "<span class='badge'>" + userInfo.application + "</span>";
                }
                innerHtml += " </button>";
                innerHtml += " <button type='button' class='btn btn-warning'>分组申请</button>";
                innerHtml += " <button type='button' class='btn btn-warning'>发布任务</button>";
                innerHtml += " <button type='button' class='btn btn-warning' onclick='allianceAppHandle(this,1)'>踢出同盟</button>";
                innerHtml += " <button type='button' class='btn btn-warning'>（警告）Warning</button>";
                innerHtml += " <button type='button' class='btn btn-warning'>（警告）Warning</button>";
            }
            if (userInfo.jurisdiction < 1) {
                innerHtml += "<h4>盟主</h4>";
                innerHtml += " <button type='button' class='btn btn-info' onclick='allianceAppHandle(this,2)'>创建分组</button>";
                innerHtml += " <button type='button' class='btn btn-info'>解散分组</button>";
                innerHtml += " <button type='button' class='btn btn-warning'>任命分组管理</button>";
                innerHtml += "<button type='button' class='btn btn-danger'>转让盟主</button>";
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
    var result = tokenService("/alliance", "POST", false, JSON.stringify(jsonData));
    if (result.result) {
        location.reload();
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
        createAllianceTable(res);
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
        createAllianceTable(res);
    }

});
/**
 * @description: 根据同盟检索结果生成同盟目录
 * @param {*} res
 * @return {*}
 */
function createAllianceTable(res) {
    if (res.result) {
        var innerHtml = "";
        if (res.data == "" || res.data == "{}" || res.data == null || res.data == "[]" || res.data.length == 0) {
            $("#searchresult").html("");
            innerHtml += "<h4>未查询到相关同盟，请确认后重试</h4>";
        } else {
            innerHtml += "<div class='table-responsive'>";
            innerHtml += "  <table class='table table-bordered .table-responsive'>";
            innerHtml += "<tr>";
            innerHtml += "  <td class='info'><h5>盟ID</h5></td>";
            innerHtml += "  <td class='info'><h5>同盟名</h5></td>";
            innerHtml += "  <td class='info'><h5>盟主</h5></td>";
            innerHtml += "  <td class='info'><h5>人数</h5></td>";
            innerHtml += "  <td class='info'><h5>盟介绍</h5></td>";
            innerHtml += "  <td class='info'><h5>创建时间</h5></td>";
            innerHtml += "  <td class='info'><h5>申请</h5></td>";
            innerHtml += "</tr>";
            for (var temp in JSON.parse(res.data)) {
                var alliance = JSON.parse(res.data)[temp];
                innerHtml += "<tr>";
                innerHtml += "  <td>" + allianceIdHandle(alliance.alliance_Id) + "</td>";
                innerHtml += "  <td>" + alliance.name + "</td>";
                innerHtml += "  <td>" + alliance.own_name + "</td>";
                innerHtml += "  <td>" + alliance.population + "</td>";
                innerHtml += "  <td>" + alliance.introduce + "</td>";
                innerHtml += "  <td>" + alliance.create_time + "</td>";
                if (alliance.application == "0") {//0:已经申请
                    innerHtml += "  <td><button class='btn btn-default' type='submit' disabled>已申请</button></td>";
                } else {
                    //1:未申请
                    innerHtml += "  <td><button class='btn btn-default' type='submit' onclick='applicationAlliance(this," + alliance.alliance_Id + ")'>申请</button></td>";
                }
                innerHtml += "</tr>";
            }
            innerHtml += "  </table>";
            innerHtml += "</div>";
        }
        $("#searchresult").html("");
        $("#searchresult").html(innerHtml);
    }
}
function applicationAlliance(Object, allianceId) {
    var jsonData = {};
    jsonData.type = "0";
    jsonData.email = sessionStorage.getItem("email");
    jsonData.id = allianceId;
    var result = tokenService("/application/alliance", "POST", false, JSON.stringify(jsonData));
    if (result.result) {
        $(Object).attr('disabled', true);
        $(Object).html("已申请");
    }
}
/**
 * @description: 为不足10位de联盟ID补0
 * @param {*} id
 * @return {*}
 */
function allianceIdHandle(id) {
    var str = "0000000000" + id;
    return str.substr(str.length - 10, 10);
}

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

function allianceAppHandle(object, pageType) {
    sessionStorage.setItem("pageType", pageType);
    layx.iframe('shadow-color', $(object).html(), 'handlePage', {
        shadable: 0.8
    });
    layx.setSize('shadow-color', { width: 700, height: 600 });
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
$("#signature").blur(function () {
    var url = "/userInfo/signature/" + sessionStorage.getItem("email") + "/" + $("#signature").val();
    if (sessionStorage.getItem("signature") != $("#signature").val()) {
        var result = tokenService(url, "PUT", false, null);
        if (result.result) {
            sessionStorage.setItem("signature", $("#signature").val());
            layx.alert('更新个性签名', '个性签名更新成功', null, { dialogIcon: 'success' });
        }
    }
});

