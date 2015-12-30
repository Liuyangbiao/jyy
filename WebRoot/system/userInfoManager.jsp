<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<html>
    <head>
        <title>用户信息管理</title>
        <meta http-equiv="content-type" content="text/html; charset=utf-8">
        <link type="text/css" href="../css/jquery.autocomplete.css" rel="stylesheet" />
        <link href="../css/base.css" rel="stylesheet" type="text/css">
        <link href="../css/jquery.css" rel="stylesheet" type="text/css">
        <link href="../css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.jqgrid.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.multiselect.css" />
        <link href="../css/base.css" rel="stylesheet" type="text/css">
        <link href="../css/uploadify.css" rel="stylesheet" type="text/css" />
        <link href="../css/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
        <link href="../css/skin-vista/ui.dynatree.css" rel="stylesheet" type="text/css" />
        <script src="../js/jquery-1.5.min.js" type="text/javascript">
        </script>
        <script src="../js/jquery-ui-1.8.10.custom.min.js" type="text/javascript">
        </script>
        <script src="../js/jquery.dynatree.js" type="text/javascript">
        </script>
        <script src="../js/jquery.cookie.js" type="text/javascript">
        </script>
        <script src="../js/jquery.jqGrid.js" type="text/javascript">
        </script>
        <script src="../js/jquery.form.js" type="text/javascript">
        </script>
        <script src="../js/rowedex4.js" type="text/javascript">
        </script>
        <script src="../js/jquery.timepicker.js" type="text/javascript">
        </script>
        <script src="../js/jquery.maskedinput.js" type="text/javascript">
        </script>
        <script src="../js/jquery.ui.datepicker-zh-CN.js" type="text/javascript">
        </script>
        <script type="text/javascript" src="../js/jquery.autocomplete.js">
        </script>
        <script type="text/javascript" src="../js/popShortcut.js">
        </script>
        <script type="text/javascript">
            $(function(){
                $.ajaxSetup({
                    cache: false
                });
                buttonDisable();
                $("#password").mask("999999");
                var mId = "${param.menuId}";
                imgDisplay(mId);
                select();
                insertUserinfo();
                tableJqGrid();
                $.getJSON('unitNameAction.action?time=' + Math.random(), function(json){
                    unitNameAutoComplete(json, "#uName", "");
                });
                $.getJSON('unitNameAction.action?time=' + Math.random(), function(json){
                    unitNameAutoComplete(json, "#insertUName", "");
                });
                
            });
            
            function select(){
                $.getJSON("roleList.action", function(json){
                    var role = "<option value=''  >无</option>";
                    var insertRole = "";
                    for (var i = 0; i < json.length; i++) {
                        role += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
                        insertRole += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
                    }
                    $("#roleId").html(role);
                    $("#insertRoleId").html(insertRole);
                });
            }
            
            // 单位名称联想
            function unitNameAutoComplete(json, valId, pn){
                $(valId).autocomplete(json, {
                    max: 10, // 列表里的条目数
                    minChars: 0, // 自动完成激活之前填入的最小字符,为0双击弹出列表
                    matchCase: false,// 不区分大小写
                    width: 285, // 提示的宽度，溢出隐藏
                    scrollHeight: 300, // 提示的高度，溢出显示滚动条
                    matchContains: true, // 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                    // autoFill : false, // 自动填充 ，就是在文本框中自动填充符合条件的项目
                    mustMatch: true, // 自动匹配，mustMatch表示必须匹配条目，也就是在文本框里输入的内容，必须是data参数里的数据，如果不匹配，文本框就被清空
                    formatItem: function(row, i, max, term){
                        var v = $(valId).val();
                        return row.name;
                        // return i + '/' + max + ':' + "<I >" + row.name +"</I>"+ "<span
                        // style='display:none;'>"+ '"[' + row.id + ']' +"</span>";
                        // formatItem作用在于可以格式化列表中的条目，比如我们加了“I”，让列表里的字显示出了斜体
                        if (row.name.indexOf(v) == 0 || row.spell.indexOf(v) == 0) {
                            return row.name;
                        }
                        else 
                            return false;
                    },
                    formatMatch: function(row, i, max){
                        // return row.name + row.id;
                        if (row.name == pn) {
                            $(valId).attr("value", pn);
                            $(valId + "Code").attr("value", row.id);
                        }
                        return row.name + " (" + row.spell + ")";
                        // formatMatch是配合formatItem使用，作用在于，由于使用了formatItem，所以条目中的内容有所改变，而我们要匹配的是原始的数据，所以用formatMatch做一个调整，使之匹配原始数据
                    },
                    formatResult: function(row){
                        return row.name;
                        // formatResult是定义最终返回的数据，比如我们还是要返回原始数据，而不是formatItem过的数据
                    }
                }).result(function(event, row, formatted){
                
                    if (row == "undefined" || row == null) {
                        $(valId + "Code").attr("value", "");
                    }
                    else {
                        $(valId + "Code").attr("value", row.id);
                    }
                    $(valId + "Code").trigger("change");
                });
            }
            
            //table Jqrid
            function tableJqGrid(){
                $("#rowedtable").clearGridData();
                var newurl = "selectUserInfoManager.action?" + $('#jsonForm').formSerialize();
                $("#rowedtable").jqGrid({
                    url: newurl,
                    datatype: "json",
                    type: "POST",
                    colNames: ['登录名', '用户名称', '职务', '所属单位', '联系电话', '角色名称', '登录时间', '上次登录', '登录次数', '是否有效', '创建人', '创建日期', '操作'],
                    colModel: [{
                        id: 'loginId',
                        name: 'loginId',
                        index: 'loginId',
                        align: 'left'
                    }, {
                        id: 'userName',
                        name: 'userName',
                        index: 'userName',
                        align: 'left'
                    }, {
                        id: 'zhiWu',
                        name: 'zhiWu',
                        index: 'zhiWu',
                        align: 'center'
                    }, {
                        id: 'uCode',
                        name: 'uCode',
                        index: 'uCode',
                        align: "center"
                    }, {
                        id: 'telePhone',
                        name: 'telePhone',
                        index: 'telePhone',
                        align: "center"
                    }, {
                        id: 'roleName',
                        name: 'roleName',
                        index: 'roleName',
                        align: 'center'
                    }, {
                        id: 'loginTime',
                        name: 'loginTime',
                        index: 'loginTime',
                        align: 'center'
                    }, {
                        id: "fLogin",
                        name: 'fLogin',
                        index: 'fLogin',
                        align: 'center'
                    }, {
                        id: "LoginCount",
                        name: 'LoginCount',
                        index: 'LoginCount',
                        align: 'center'
                    }, {
                        id: "isValid",
                        name: 'isValid',
                        index: 'isValid',
                        align: 'center'
                    }, {
                        id: "creator",
                        name: 'creator',
                        index: 'creator',
                        align: 'center'
                    }, {
                        id: "creatorDate",
                        name: 'creatorDate',
                        index: 'creatorDate',
                        align: 'center'
                    }, {
                        id: "href",
                        name: 'href',
                        index: 'href',
                        align: 'center'
                    }],
                    rowNum: 10,// 页数
                    pager: "#prowed4",// 页码
                    caption: "用户管理",
                    forceFit: true,// 是否超出DIV
                    multiselect: true,// 是否有全选
                    viewrecords: true,// 是否显示多少行
                    multiselectWidth: '40',// 调整选择的宽度
                    emptyrecords: '数据为空',// 空记录时的提示信息
                    width: '1100',//表格宽度 
                    sortable: false,//是否支持排序,
                    height: 'auto',
                    loadtext: "正在加载数据，请稍候……"
                });
            }
            
            //查询事件
            function selectUserinfo(){
                buttonDisable();
                $("#rowedtable").clearGridData();
                var newurl = "selectUserInfoManager.action?" + $('#jsonForm').formSerialize();
                $("#rowedtable").jqGrid('setGridParam', {
                    url: newurl
                }).trigger("reloadGrid");
            }
            
            // 点击删除时触 发的事件
            function deleteUserinfo(){
                var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
                var len = selectedIds.length;
                if (len == 0) {
                    alert("请您选择删除的用户信息！");
                }
                else {
                    var stringsplit = "";
                    for (var i = 0; i < selectedIds.length; i++) {
                        if (selectedIds[i] != undefined) {
                            stringsplit += "," + selectedIds[i];
                        }
                    }
                    $.getJSON("deleteUserinfo.action?time=" + new Date().getTime(), {
                        codes: stringsplit.substr(1)
                    }, function(data){
                        if (data == "success") {
                            alert("删除成功!");
                            selectUserinfo();
                        }
                        else {
                            alert("删除失败");
                        }
                    });
                }
            }
            
            function insertUserinfo(){
                // 模式窗体
                $('#insertUserInfo').dialog({
                    width: 600,
                    height: 400,
                    autoOpen: false,// 打开模式窗体
                    resizable: true,// 是否可以调整模式窗体的大小
                    draggable: true,// 是否可以拖动模式窗体
                    modal: true,// 启用模式窗体
                    closeOnEscape: true,// 按下esc可以退出
                    bgiframe: true,
                    buttons: {
                        "保存": function(){
                            checkNotNullAll();
                        },
                        "取消 ": function(){
                            $(this).dialog("close");
                        }
                    }
                });
            }
            
            // 显示模式窗体
            function insertUserShowDialog(id){
                $("#insertLoginId").attr("readOnly", false);
                $("#updateId").attr("value", "");
                $("#insertLoginId").attr("value", "");
                $("#insertUserName").attr("value", "");
                $("#password").attr("value", "");
                $("#insertUName").attr("value", "");
                $("#insertUNameCode").attr("value", "");
                $("#telephone").attr("value", "");
                $("#duty").attr("value", "");
                $("#loginCount").attr("value","");
                $("#insertLoginIderror").css("display", "none");
                $("#insertUserNameerror").css("display", "none");
                $("#passworderror").css("display", "none");
                $("#telephoneerror").css("display", "none");
                $("#dutyerror").css("display", "none");
                $("#insertLoginIderrorvalue").css("display", "none");
                
                
                id = id == null ? "" : id;
                if (id == "" || id == null) {
                
                    $("#insertUserInfo").dialog('open');
                }
                else {
                 
                    updateSelect(id);
                    $("#insertUserInfo").dialog('open');
                }
            }
            
            //验证
            function checkNotNullAll(){
                var login = checkLogin();
                var userName = checkUserName();
                var password = checkPassword();
                var duty = checkDuty();
                var tele = checkTelePhone();
                if (login && userName && password && duty && tele) {
                    $('#insertUserForm').ajaxSubmit({
                        dataType: "json",
                        beforeSubmit: function(){
                        },
                        type: "post",
                        contentType: "application/x-www-form-urlencoded;charset=utf-8",
                        success: backCallInsert
                    });
                    
                }
            }
            
            function backCallInsert(data, status, xhr){
                if (data == "true") {
                    alert("保存成功 ");
                    selectUserinfo();
                    select();
                    $("#insertUserInfo").dialog('close');
                }
                else {
                    alert("保存失败");
                }
            }
            
            //非空验证字典编码
            function checkLogin(){
                var id = $("#insertLoginId").val();
                if (jQuery.trim(id) != "") {
                
                    if ($.getJSON("selectUserBoolean.action", {
                        isLoginId: id
                    }, function(json){
                        if (json == 'true') {
                            checkupdateCode();
                            return false;
                        }
                        else if (json == 'false') {
                                $("#insertLoginIderror").css("display", "none");
                                $("#insertLoginIderrorvalue").css("display", "none");
                                return true;
                            }
                    })) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                else {
                    $("#insertLoginIderror").css("display", "block");
                    return false;
                }
            }
            
            function checkupdateCode(){
            
                var updateCode = $("#updateId").val();
                if (updateCode.len != 0) {
                    $("#insertLoginIderror").css("display", "none");
                    $("#insertLoginIderrorvalue").css("display", "none");
                }
                if (updateCode == "") {
                    $("#insertLoginIderrorvalue").css("display", "block");
                }
            }
            
            function checkUserName(){
                if (jQuery.trim($("#insertUserName").val()) != "") {
                    $("#insertUserNameerror").css("display", "none");
                    return true;
                }
                else {
                    $("#insertUserNameerror").css("display", "block");
                    return false;
                }
            }
            
            function checkPassword(){
                if (jQuery.trim($("#password").val()) != "") {
                    $("#passworderror").css("display", "none");
                    return true;
                }
                else {
                    $("#passworderror").css("display", "block");
                    return false;
                }
            }
            
            function checkDuty(){
                if (jQuery.trim($("#duty").val()) != "") {
                    $("#dutyerror").css("display", "none");
                    return true;
                }
                else {
                    $("#dutyerror").css("display", "block");
                    return false;
                }
            }
            
            
            // 验证电话号码
            function checkTelePhone(){
                var phone = $("#telephone").val();
                if (phone.length != 0) {
                    //var p2 =/^13\d$/gi;
                    //|| p2.test(phone)
                    var ph= /13\d{9}\b|15[0689]\d{8}\b|18[0156789]\d{8}\b|010[- ]?[1-9]\d{7}\b|02\d[- ]?[1-9]\d{7}\b|0[3-9]\d{2}[- ]?[1-9]\d{6,7}\b/;
                    //var p1 = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
                    var me = false;
                    if (ph.test(phone)) 
                        me = true;
                    if (!me) {
                        $("#telephoneerror").css("display", "block");
                        return false;
                    }
                    else {
                        $("#telephoneerror").css("display", "none");
                        return true;
                    }
                }
                else {
                    $("#telephoneerror").css("display", "none");
                    return true;
                }
            }
            
            function updateSelect(id){
            
                $.getJSON("updateSelectUser.action", {
                    loginId: id
                }, function(json){
                    for (var i = 0; i < json.length; i++) {
                        $("#insertLoginId").attr("readOnly", true);
                        $("#insertLoginId").attr("value", json[i].loginId);
                        $("#insertUserName").attr("value", json[i].username);
                        $("#password").attr("value", json[i].password);
                        $("#insertUName").attr("value", json[i].uname);
                        $("#insertUNameCode").attr("value", json[i].ucode);
                        $("#telephone").attr("value", json[i].telephone);
                        $("#duty").attr("value", json[i].duty);
                        $("#insertRoleId").attr("value", json[i].roleId);
                        $("#updateId").attr("value", json[i].loginId);
                        $("#loginCount").attr("value",json[i].loginCount);
                        if (json[i].isValid == true) {
                            $("#isValidtrue").attr("checked", true);
                        }
                        else {
                            $("#isValidfalse").attr("checked", true);
                        }
                    }
                });
            }
            
            //按钮显示或者不显示
            function buttonDisable(){
                var selects = $('input[name="isValid"]');
                if (selects[0].checked == true) {
                    $("#btnDelete").attr("disabled", false);
                }
                if (selects[1].checked == true) {
                    $("#btnDelete").attr("disabled", "true");
                    
                }
            }
            function uNameBlur(){
            	var uName = $("#uName").val();
				if(uName.length==0){
					$("#uNameCode").attr("value","");
					}
                }
            function insertUNameBlur(){
                var  insertUName = $("#insertUName").val();
                if(insertUName.length==0){
                      $("#insertUNameCode").attr("value","");
                    }
            }
        </script>
    </head>
    <body>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2">
            <tr>
                <td class="conttop2">
                    <div class="top2left">
                    </div>
                    <div class="top2right">
                    </div>
                    <div class="top2middle">
                        <span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif"></a></span><span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span><span class="topico2"><img src="../images/Accordion_ico03.gif"></span><h3>用户管理</h3>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="contmiddle2">
                    <table width="750" border="0" cellspacing="0" cellpadding="0" class="conttitle">
                        <tr>
                            <td height="22">
                                <h3>检索条件</h3>
                            </td>
                        </tr>
                    </table>
                    <form action="selectUserInfoManager.action" method="post" name="jsonForm" id="jsonForm">
                        <table style="font-size:12px;" border="0" width="60%">
                            <tr>
                                <td align="right">
                                    登录名
                                </td>
                                <td>
                                    <input type="text" name="loginId" id="loginId" size="10">
                                </td>
                                <td align="right">
                                    用户名
                                </td>
                                <td>
                                    <input type="text" name="userName" id="userName" size="10">
                                </td>
                                <td align="right">
                                    所属单位
                                </td>
                                <td>
                                    <input type="text" name="uName" id="uName" onblur="uNameBlur()">
                                    <input type="hidden" size="10" name="uNameCode" id="uNameCode">
                                </td>
                            </tr>
                            <tr>
                                <td align="right">
                                    角色
                                </td>
                                <td>
                                    <select id="roleId" name="roleId">
                                    </select>
                                </td>
                                <td align="right">
                                    有效与否
                                </td>
                                <td colspan="3">
                                    <input type="radio" checked="checked" id="isUsabletrue" name="isValid" value="1">有效<input type="radio" id="isUsablefalse" name="isValid" value="0">无效
                                </td>
                            </tr>
                            <tr>
                                <td colspan="6" align="center">
                                    <input type="button" value="查询" onClick="selectUserinfo()" class="chaxunbutton">
                                </td>
                            </tr>
                        </table>
                        <div class="bianxian" style="width: 750px;">
                        </div>
                        <table width="195" border="0" cellspacing="5" cellpadding="0">
                            <tr>
                                <td width="92">
                                    <input type="button" value="新增" id="btnInsert" class="dowbutton" onclick="insertUserShowDialog()">
                                </td>
                                <td width="88">
                                    <input type="button" value="删除" id="btnDelete" class="upbutton" onclick="deleteUserinfo()">
                                </td>
                            </tr>
                        </table>
                    </form>
                    <table id="rowedtable">
                    </table>
                    <div id="prowed4">
                    </div>
                </td>
            </tr>
            <tr>
                <td class="contbottom2">
                    <div class="bottom2left">
                    </div>
                    <div class="bottom2right">
                    </div>
                    <div class="bottom2middle">
                    </div>
                </td>
            </tr>
        </table>
        <div id="insertUserInfo" title="用户信息">
            <form id="insertUserForm" action="insertUserinfo.action" method="post" name="insertUserForm">
                <input type="hidden" name="updateId" id="updateId">
                <input type="hidden" name="loginCount" id="loginCount"/>
                <table border="0" width="100%" style="font-size: 12px;">
                    <tr>
                        <td align="right">
                            登录名
                        </td>
                        <td>
                            <input type="text" size="10" name="tuser.loginId" id="insertLoginId" onblur="checkLogin()"><span id="insertLoginIderror" style="display: none; color: red; fontSize: 11px;">请输入登录名</span>
                            <span id="insertLoginIderrorvalue" style="display: none; color: red; fontSize: 11px;">用户名已存在</span>
                        </td>
                        <td align="right">
                            有效与否
                        </td>
                        <td>
                            <input type="radio" checked="checked" id="isValidtrue" name="insertisValid" value="1">有效<input type="radio" id="isValidfalse" name="insertisValid" value="0">无效
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            用户名称
                        </td>
                        <td>
                            <input type="text" size="10" name="tuser.name" id="insertUserName" onblur="checkUserName()"><span id="insertUserNameerror" style="display: none; color: red; fontSize: 11px;">请输入用户名</span>
                        </td>
                        <td align="right">
                            密码
                        </td>
                        <td>
                            <input type="text" size="10" name="tuser.password" id="password" onblur="checkPassword()">*数字<span id="passworderror" style="display: none; color: red; fontSize: 11px;">请输入密码</span>
                        </td>
                        <td align="right">
                            所属单位
                        </td>
                        <td>
                            <input type="text" id="insertUName" onblur="insertUNameBlur()">
                            <input type="hidden" size="10" name="insertUNameCode" id="insertUNameCode">
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            联系电话
                        </td>
                        <td>
                            <input type="text" size="15" name="tuser.telephone" id="telephone" onblur="checkTelePhone()"><span id="telephoneerror" style="display: none; color: red; fontSize: 11px;">请输入正确的电话号码或手机</span>
                        </td>
                        <td align="right">
                            职务
                        </td>
                        <td >
                            <input type="text" size="10" name="tuser.duty" id="duty" onblur="checkDuty()"/><span id="dutyerror" style="display: none; color: red; fontSize: 11px;">请输入职务</span>
                        </td>
                        <td align="right">
                            角色
                        </td>
                        <td>
                            <select id="insertRoleId" name="insertRoleId">
                            </select>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>
