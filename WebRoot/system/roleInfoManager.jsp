<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<html>
    <head>
        <title>角色信息管理</title>
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
                select();
                insertRoleInfo();
                var mId = "${param.menuId}";
                imgDisplay(mId);
                tableJqGrid();
                userRoleDialog();
                
            });
            
            
            //table Jqrid
            function tableJqGrid(){
                $("#rowedtable").clearGridData();
                var newurl = "selectRoleInfo.action?" + $('#jsonForm').formSerialize();
                $("#rowedtable").jqGrid({
                    url: newurl,
                    datatype: "json",
                    type: "POST",
                    colNames: ['角色代码', '角色名称', '角色描述', '默认密级', '创建人', '创建日期', '操作'],
                    colModel: [{
                        id: 'roleCode',
                        name: 'roleCode',
                        index: 'roleCode',
                        align: 'left'
                    }, {
                        id: 'roleName',
                        name: 'roleName',
                        index: 'roleName',
                        align: 'left'
                    }, {
                        id: 'description',
                        name: 'description',
                        index: 'description',
                        align: 'center'
                    }, {
                        id: 'security',
                        name: 'security',
                        index: 'security',
                        align: "center"
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
                    caption: "角色管理",
                    forceFit: true,// 是否超出DIV
                    multiselect: true,// 是否有全选
                    viewrecords: true,// 是否显示多少行
                    multiselectWidth: '40',// 调整选择的宽度
                    emptyrecords: '数据为空',// 空记录时的提示信息
                    width: '750',//表格宽度 
                    sortable: false,//是否支持排序,
                    height: 'auto',
                    loadtext: "正在加载数据，请稍候……"
                });
            }
            
            function select(){
                $.getJSON("roleList.action", function(json){
                    var role = "<option value=''  >无</option>";
                    var insertRole = "";
                    for (var i = 0; i < json.length; i++) {
                        role += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
                        insertRole += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
                    }
                    $("#roleId").html(role);
                    
                });
                
              //保密
           	 var baomispan = $("#baomispan").text();
           	 $.getJSON('../system/dictionaryType.action',{
                   paramValue:baomispan},
                   function(json){
           		 var baomi = "<option value=''>无</option>";
           		 for(var i = 0;i<json.length;i++){
           			 baomi += "<option value=\"" + json[i].id + "\" >"
           			 + json[i].name + "</option>";
           		 }
           		 $("#baomi").html(baomi);
                });
            }
            
            function selectRoleInfo(){
                $("#rowedtable").clearGridData();
                var newurl = "selectRoleInfo.action?" + $('#jsonForm').formSerialize();
                $("#rowedtable").jqGrid('setGridParam', {
                    url: newurl
                }).trigger("reloadGrid");
            }
            
            function deleteRoleInfo(){
                var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
                var len = selectedIds.length;
                if (len == 0) {
                    alert("请您选择删除的角色信息！");
                }
                else {
                    var roleId = "";
                    for (var i = 0; i < selectedIds.length; i++) {
                        if (selectedIds[i] != undefined) {
                            roleId += "," + selectedIds[i];
                        }
                    }
                    var result = confirm("您确定要删除有关此角色的相关信息吗？");
                    if (result == true) {
                        $("#roletable tr:not(:eq(0))").remove();
                        //找到要追加的表格
                        var newtable = $("#roletable");
                        $.getJSON("getUserByRoleId.action", {
                            roleId: roleId.substr(1)
                        }, function(json){
                            if (json.length != 0) {
                                for (var i = 0; i < json.length; i++) {
                                    //给表格添加一行
                                    var newtr = $("<tr></tr>");
                                    $(newtr).attr("id", json[i].loginId);
                                    
                                    var loginId = $("<td></td>");
                                    $(loginId).html(json[i].loginId);
                                    var userName = $("<td></td>");
                                    $(userName).html(json[i].username);
                                    var ucode = $("<td></td>");
                                    $(ucode).html(json[i].uname);
                                    var duty = $("<td></td>");
                                    $(duty).html(json[i].duty);
                                    var duty = $("<td></td>");
                                    $(duty).html(json[i].duty);
                                    
                                    var isValid = $("<td></td>");
                                    if (json[i].isValid == true) {
                                        $(isValid).html("有效");
                                    }
                                    else {
                                        $(isValid).html("无效");
                                    }
                                    
                                    $(newtr).append(loginId);
                                    $(newtr).append(userName);
                                    $(newtr).append(ucode);
                                    $(newtr).append(duty);
                                    $(newtr).append(isValid);
                                    $(newtable).append($(newtr));
                                }
                                $("#userRoleDialog").dialog('open');
                            }
                            else {
                                $("#userRoleDialog").dialog('close');
                                $.getJSON("deleteRoleInfo.action", {
                                    codes: roleId.substr(1)
                                }, function(json){
                                    if (json == "success") {
                                        alert("删除成功!");
                                        selectRoleInfo();
                                    }
                                    else {
                                        alert("删除失败!");
                                    }
                                });
                                
                            }
                            
                        });
                        
                    }
                }
            }
            
            function userRoleDialog(){
                // 模式窗体
                $('#userRoleDialog').dialog({
                    width: 600,
                    height: 450,
                    autoOpen: false,// 打开模式窗体
                    resizable: true,// 是否可以调整模式窗体的大小
                    draggable: true,// 是否可以拖动模式窗体
                    modal: true,// 启用模式窗体
                    closeOnEscape: true,// 按下esc可以退出
                    bgiframe: true
                });
            }
            
            function insertRoleInfo(){
                // 模式窗体
                $('#insertRoleInfo').dialog({
                    width: 600,
                    height: 350,
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
            function insertRoleShowDialog(id){
                select();
                $("#updateId").attr("value", "");
                $("#insertRoelId").attr("readOnly", false);
                $("#insertRoelId").attr("value", "");
                $("#insertRoleName").attr("value", "");
                $("#insertDescription").attr("value", "");
                $("#insertRoelIderror").css("display", "none");
                $("#insertRoleNameerror").css("display", "none");
                $("#insertRoelIderrorvalue").css("display", "none");
                
                id = id == null ? "" : id;
                if (id == "" || id == null) {
                    $("#insertRoleInfo").dialog('open');
                }
                else {
                    updateSelect(id);
                    $("#insertRoleInfo").dialog('open');
                }
                
            }
            
            function checkNotNullAll(){
                var roleId = checkRoleId();
                var roleName = checkRoleName();
                if (roleId && roleName) {
                    $('#insertRoleForm').ajaxSubmit({
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
                    selectRoleInfo();
                    select();
                    $("#insertRoleInfo").dialog('close');
                }
                else {
                    alert("保存失败");
                }
            }
            
            //非空验证字典编码
            function checkRoleId(){
                var id = $("#insertRoelId").val();
                if (jQuery.trim(id) != "") {
                    if ($.getJSON("selectRoleBoolean.action", {
                        insertRoelId: id
                    }, function(json){
                        if (json == 'true') {
                            checkupdateCode();
                            return false;
                        }
                        else 
                            if (json == 'false') {
                                $("#insertRoelIderror").css("display", "none");
                                $("#insertRoelIderrorvalue").css("display", "none");
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
                    $("#insertRoelIderror").css("display", "block");
                    return false;
                }
            }
            
            function checkupdateCode(){
            
                var updateCode = $("#updateId").val();
                if (updateCode.len != 0) {
                    $("#insertRoelIderror").css("display", "none");
                    $("#insertRoelIderrorvalue").css("display", "none");
                }
                if (updateCode == "") {
                    $("#insertRoelIderrorvalue").css("display", "block");
                }
            }
            
            function checkRoleName(){
                if (jQuery.trim($("#insertRoleName").val()) != "") {
                    $("#insertRoleNameerror").css("display", "none");
                    return true;
                }
                else {
                    $("#insertRoleNameerror").css("display", "block");
                    return false;
                }
            }
            
            function updateSelect(id){
                $.getJSON("updateSelectRole.action", {
                    insertRoelId: id
                }, function(json){
                    for (var i = 0; i < json.length; i++) {
                        $("#insertRoelId").attr("readOnly", true);
                        $("#insertRoelId").attr("value", json[i].roleId);
                        $("#insertRoleName").attr("value", json[i].roleName);
                        $("#insertDescription").attr("value", json[i].description);
                        $("#baomi").attr("value", json[i].baomi);
                        $("#updateId").attr("value", json[i].roleId);
                    }
                });
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
                        <span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif"></a></span><span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span><span class="topico2"><img src="../images/Accordion_ico03.gif"></span><h3>角色管理</h3>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="contmiddle2">
                    <table width="750" border="0" cellspacing="0" cellpadding="0" class="conttitle">
                        <tr>
                            <td height="22">
                                <h3>操作项</h3>
                            </td>
                        </tr>
                    </table>
                    <form action="selectRoleInfo.action" method="post" name="jsonForm" id="jsonForm">
                        <table style="font-size:12px;" border="0" width="40%">
                            <tr>
                                <td align="right">
                                    角色名称
                                </td>
                                <td>
                                    <select id="roleId" name="roleId" style="width:120px">
                                    </select>
                                </td>
                                <td align="right">
                                    角色描述
                                </td>
                                <td>
                                    <input type="text" name="description" id="description">
                                </td>
                            </tr>
                            <tr>
                                <td colspan="6" align="center">
                                    <input type="button" value="查询" onclick="selectRoleInfo()" class="chaxunbutton">
                                </td>
                            </tr>
                        </table>
                        <div class="bianxian" style="width: 750px;">
                        </div>
                        <table width="195" border="0" cellspacing="5" cellpadding="0">
                            <tr>
                                <td width="92">
                                    <input type="button" value="新增" onclick="insertRoleShowDialog()" class="dowbutton">
                                </td>
                                <td width="88">
                                    <input type="button" value="删除" onclick="deleteRoleInfo()" class="upbutton">
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
        <div id="insertRoleInfo" title="角色信息">
            <form id="insertRoleForm" action="insertRoleInfo.action" method="post" name="insertRoleForm">
                <table border="0" style="font-size: 12px;">
                    <tr>
                        <td width="20%"  align="right">
                            角色代码
                        </td>
                        <td width="20%">
                            <input type="text" size="10" name="role.id" onblur="checkRoleId()" id="insertRoelId"><span id="insertRoelIderror" style="display: none; color: red; fontSize: 11px;">请输入角色代码</span>
                            <span id="insertRoelIderrorvalue" style="display: none; color: red; fontSize: 11px;">角色已存在</span>
                        </td>
                        <td width="10%"  align="right">
                            角色名称
                        </td>
                        <td width="20%">
                            <input type="text" size="10" name="role.name" onblur="checkRoleName()" id="insertRoleName"/><span id="insertRoleNameerror" style="display: none; color: red; fontSize: 11px;">请输入角色名称</span>
                        </td>
                        <td width="10%"  align="right">
                            <span id="baomispan">密级分类</span>
                        </td>
                        <td>
                            <select id="baomi" name="role.security">
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td  align="right">
                            角色描述
                        </td>
                        <td colspan="5">
                            <textarea rows="5" cols="60" name="role.description" id="insertDescription">
                            </textarea>
                            <input type="hidden" id="updateId" name="updateId">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div id="userRoleDialog" title="用户角色">
            <table width="100%" border="0" cellpadding="0" cellspacing="1" id="roletable" style="text-align:center; font-size:12px;" class="biaoge">
                <tr class="bgtr01">
                    <td>
                        登录名
                    </td>
                    <td>
                        用户名
                    </td>
                    <td>
                        所属单位
                    </td>
                    <td>
                        职务
                    </td>
                    <td>
                        有效与否
                    </td>
                </tr>
            </table>
            <br/>
            <br/>
            <center>
                <h3 style="color: red;">此角色存在用户，不能删除该角色</h3>
            </center>
        </div>
    </body>
</html>
