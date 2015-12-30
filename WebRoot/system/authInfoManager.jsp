<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/"; %>
<html>
    <head>
        <link type="text/css" href="../css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet" />
        <link href="<%=path%>/css/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.jqgrid.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.multiselect.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="../css/base.css">
        <script type="text/javascript" src="<%=path%>/js/jquery-1.5.min.js">
        </script>
        <script type="text/javascript" src="<%=path%>/js/jquery.cookie.js">
        </script>
        <script type="text/javascript" src="../js/jquery.jqGrid.js">
        </script>
        <script type="text/javascript" src="<%=path%>/js/jquery-ui-1.8.10.custom.min.js">
        </script>
        <script type="text/javascript" src="../js/jquery.autocomplete.js">
        </script>
        <script type="text/javascript" src="<%=path%>/js/jquery.form.js">
        </script>
        <script type="text/javascript" src="<%=path%>/js/popShortcut.js">
        </script>
        <script src="../js/jquery.maskedinput.js" type="text/javascript">
        </script>
        <script src="js/authInfoManager.js" type="text/javascript">
        </script>
        <script type="text/javascript">
            $(function(){
                $("#allcheck").click(function(){
                    if (this.checked) {
                        $("input[name='eachCheck']").each(function(){
                            this.checked = true;
                        });
                    }
                    else {
                        $("input[name='eachCheck']").each(function(){
                            this.checked = false;
                        });
                    }
                });
                $.ajaxSetup({
                    cache: false
                });
                
                select();
                insertProfileDialog();
                insertUserProfileShow();
                var mId = "${param.menuId}";
                imgDisplay(mId);
                insertRoeProfileShowDailog();
                //Tab表格
                $("#tabs").tabs({
                    cookie: {
                        // store cookie for a day, without, it would be a session cookie
                        expires: 1
                    }
                });
           
            
            });
        </script>
        <style type="text/css">
            /*demo page css*/
            body {
                font-size: 12px;
            }
        </style>
        <style type="text/css">
            .biaoge td {
                padding: 5px;
                text-align: center;
            }
            
            .biaoge .bgtr01 {
                background-image: url(../images/biaogetitlebg.gif);
                background-repeat: repeat-x;
                background-position: 50% 50%;
                background-color: #48acff;
                font-weight: bold;
                color: #476c89;
                height: 20px;
            }
        </style>
    </head>
    <body>
        <!-- Tabs -->
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2">
            <tr>
                <td class="conttop2">
                    <div class="top2left">
                    </div>
                    <div class="top2right">
                    </div>
                    <div class="top2middle">
                        <span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif" onclick="topwin(${param.menuId })" style="cursor: hand"></a></span><span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span><span class="topico2"><img src="../images/Accordion_ico03.gif"></span><h3>权限管理</h3>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="contmiddle2">
                    <table width="750" border="0" cellspacing="0" cellpadding="0" style="font-size: 12px; font-weight: bold;">
                        <tr>
                            <td>
                                <div id="tabs" align="center">
                                    <ul>
                                        <li>
                                            <a href="#tabs-1">权限定义</a>
                                        </li>
                                        <li>
                                            <a href="#tabs-2">角色授权</a>
                                        </li>
                                        <li>
                                            <a href="#tabs-3">用户授权</a>
                                        </li>
                                    </ul>
                                    <div id="tabs-1">
                                        <div>
                                            <form action="selectProfileAll.action" method="post" name="profileForm" id="profileForm">
                                                <br/>
                                                <table style="font-size: 12px;" border="0" width="600px">
                                                    <tr>
                                                        <td align="right">
                                                            <span id="authClassSpan">权限分类</span>
                                                        </td>
                                                        <td>
                                                            <select id="authClass" style="width: 90px" name="authClass"  onchange="getProClassChange(this)">
                                                            </select>
                                                        </td>
                                                        <td>控制对象</td>
                                                        <td><select id="parentId" name="selectparentId" style="width:100px">
                                                        </select></td>
                                                        <td align="right">
                                                            说明
                                                        </td>
                                                        <td>
                                                            <input type="text" name="remark">
                                                        </td>
                                                        <td>
                                                            <button class="chaxunbutton" onclick="selectProfile()">
                                                                <span style="font-size: 14px;">查询</span>
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <table width="177" border="0" cellspacing="5" cellpadding="0">
                                                    <tr>
                                                        <td>
                                                            <button class="querenbutton" onclick="insertProfileshowDialog()">
                                                                <span style="color: #11437f; font-size: 12px;">增
                                                                    加</span>
                                                            </button>
                                                        </td>
                                                        <td>
                                                            <button class="qkbutton">
                                                                <span style="color: #375100; font-size: 12px;">角色分配</span>
                                                            </button>
                                                        </td>
                                                        <td>
                                                            <button class="qkbutton">
                                                                <span style="color: #375100; font-size: 12px;">用户分配</span>
                                                            </button>
                                                        </td>
                                                        <td>
                                                            <button class="qkbutton" onclick="deleteProfile()">
                                                                <span style="color: #375100; font-size: 12px;">删除</span>
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </form>
                                            <div class="bianxian" style="width: 750px;" class="conttitle">
                                            </div>
                                            <table id="authrowedtable">
                                            </table>
                                            <div id="authprowed4">
                                            </div>
                                        </div>
                                    </div>
                                    <div id="tabs-2">
                                        <div>
                                            <form action="selectRoleProfile.action" method="post" name="roleProfileName" id="roleProfileName">
                                                <br/>
                                                <table style="font-size: 12px;" border="0" width="60%">
                                                    <tr>
                                                        <td align="right">
                                                            权限分类
                                                        </td>
                                                        <td>
                                                            <select id="roleAuthClass" style="width: 90px" name="roleProfileType">
                                                            </select>
                                                        </td>
                                                        <td align="right">
                                                            角色名称
                                                        </td>
                                                        <td>
                                                            <select id="roleId" style="width: 120px" name="roleId">
                                                            </select>
                                                        </td>
                                                        <td>
                                                            <button class="chaxunbutton" onclick="selectRoleProfile()">
                                                                <span style="font-size: 14px;">查询</span>
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <table width="177" border="0" cellspacing="5" cellpadding="0">
                                                    <tr>
                                                        <td>
                                                            <button class="querenbutton" onclick="insertRoleProfileshowDialog()">
                                                                <span style="color: #11437f; font-size: 12px;">增
                                                                    加</span>
                                                            </button>
                                                        </td>
                                                        <td>
                                                            <button class="qkbutton" onclick="deleteRoleProfile()">
                                                                <span style="color: #375100; font-size: 12px;">删除</span>
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </form>
                                            <div class="bianxian" style="width: 750px;" class="conttitle">
                                            </div>
                                            <table id="rolerowedtable">
                                            </table>
                                            <div id="roleprowed4">
                                            </div>
                                        </div>
                                    </div>
                                    <div id="tabs-3">
                                        <div>
                                            <form action="selectUserProfileInfo.action" method="post" name="userProfileForm" id="userProfileForm">
                                                <br/>
                                                <table style="font-size: 12px;" border="0">
                                                    <tr>
                                                        <td>
                                                            角色名称
                                                        </td>
                                                        <td>
                                                            <select id="userRoleId" style="width: 120px;" name="userRoleName">
                                                            </select>
                                                        </td>
                                                        <td>
                                                            权限分类
                                                        </td>
                                                        <td>
                                                            <select id="userAuthClass" style="width: 90px" name="userProfileType">
                                                            </select>
                                                        </td>
                                                        <td>
                                                            用户名称
                                                        </td>
                                                        <td>
                                                            <input type="text" size="10" name="userName" />
                                                        </td>
                                                        <td>
                                                            <button class="chaxunbutton" onclick="selectUserProfile()">
                                                                <span style="font-size: 14px;">查询</span>
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <table width="177" border="0" cellspacing="5" cellpadding="0">
                                                    <tr>
                                                        <td>
                                                            <button class="querenbutton" onclick="insertUserProfile()">
                                                                <span style="color: #11437f; font-size: 12px;">增
                                                                    加</span>
                                                            </button>
                                                        </td>
                                                        <td>
                                                            <button class="qkbutton" onclick="deleteUserProfile()">
                                                                <span style="color: #375100; font-size: 12px;">删除</span>
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </form>
                                            <div class="bianxian" style="width: 750px;" class="conttitle">
                                            </div>
                                            <table id="userrowedtable">
                                            </table>
                                            <div id="userprowed4">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
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
        <div id="insertProfile" title="权限信息">
            <form action="insertProfile.action" id="insertProfileForm" name="insertProfileForm" method="post">
                <input type="hidden" id="updateProfileId" name="updateProfileId">
                <table border="0" style="font-size: 12px;" width="100%">
                    <tr>
                        <td align="right">
                            对象代码
                        </td>
                        <td>
                            <input type="text" id="insertProfileId" name="profile.id" onblur="checkProfileID()"/><span id="insertProfileIderror" style="display: none; color: red; fontSize: 11px;">请输入对象代码(例:12)</span>
                            <span id="insertProfileIderrorvalue" style="display: none; color: red; fontSize: 11px;">对象代码已存在</span>
                        </td>
                        <td align="right">
                            对象名称
                        </td>
                        <td>
                            <input type="text" id="insertProfileName" onblur="checkProfileName()" name="profile.name"><span id="insertProfileNameerror" style="display: none; color: red; fontSize: 11px;">请输入对象名称</span>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            权限分类
                        </td>
                        <td>
                            <select id="inserProClass" style="width: 80px" name="profile.type" onchange="getProClassChange(this)">
                            </select>
                        </td>
                        <td align="right">
                        <span id="spanisParent" style="display:none">
                            所属权限分类</span>
                        </td>
                        <td><div id="divisParent" style="display:none">
                            <select id="isParent" name="profile.remark1">
                            </select>
                        </div>
                        
                        </td>
                    </tr>
                    <tr>
                       <td align="right">
                       <span id="spandiv" style="display:none">页面路径</span>                        
                        </td>
                        <td colspan="3">
                        <div  id="divurl" style="display:none">
                            <input type="text" id="profileUrl" onblur="checkProfileUrl()" name="profile.remark2"  size="40">
                            <span id="profileUrlerror" style="display: none; color: red; fontSize: 11px;">请输入URL</span>
                        </div>
                        </td>
                        
                    </tr>
                    <tr>
                    <td align="right">伸缩</td>
                     <td><input type="radio" name="profile.remark3" id="remark3true" value="1" checked="checked">是
                     <input type="radio" name="profile.remark3" id="remark3false" value="0">否</td>
                   
                    <td align="right">可用</td>
                     <td><input type="radio" name="profile.remark4" id="remark4true" value="1" checked="checked">是
                     <input type="radio" name="profile.remark4" id="remark4false" value="0">否</td>
                    </tr>
                    <tr>
                     <td>图片路径</td>
                     <td  colspan="2">
                     <input type="text" id="zhuimageUrl" name="zhuimageUrl" size="10">
                  				<select id="imageUrl" name="profile.remark5" onChange="imgShow()">
			                <option value="/images/Accordion_ico01.gif">图片1</option>
			                <option value="/images/Accordion_ico02.gif">图片2</option>
			                <option value="/images/Accordion_ico03.gif">图片3</option>
			                <option value="/images/Accordion_ico04.gif">图片4</option>
			                <option value="/images/Accordion_ico05.gif">图片5</option>
	             		</select>	<img alt="图片" id="imgId" src="<%=path %>/images/Accordion_ico01.gif"></td>
                    </tr>
                    <tr>
                     <td>排序位置</td>
                     <td><input type="text" name="profile.sortNo" id="sortNo" onblur="checkSortNo()" size="10">
                      <span id="sortNoerror" style="display:none;color:red;fontSize:11px;">请输入排序位置(例：1或12)</span>
                     </td>
                    </tr>
                    <tr>
                        <td align="right">
                            说明
                        </td>
                        <td colspan="3">
                            <textarea rows="4" cols="35" id="profileRemark" name="profile.description">
                            </textarea>
                        </td>
                    </tr>
                    
                </table>
            </form>
        </div>
        <div id="insertRoleProfile" title="角色授权">
            <form action="insertRoleProfile.action" method="post" id="insertRoleProfileForm" name="insertRoleProfileForm">
                <div id="spanrole" style="display: none;">
                    角色名称：<span id="temproleName"></span>
                    控制类型：<span id="tempinsertProfileType"></span>
                    控制对象：<span id="tempinsertProfileObject"></span>
                </div>
                <div id="divrole">
                    <table border="0" style="font-size: 12px;" width="100%">
                        <tr>
                            <td>
                                角色名称
                            </td>
                            <td>
                                <select id="insertRoleId" style="width: 120px" name="roleName" onchange="getRoleIdChange(this)">
                                </select>
                            </td>
                            <td>
                                控制类型
                            </td>
                            <td>
                                <select id="insertProfileType" style="width: 120px" onchange="getProfileType(this)" name="profileParentID">
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                控制对象
                            </td>
                            <td colspan="3">
                                <select id="insertProfileObject" style="width: 100px" name="profileID">
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <table width="100%" border="0" cellpadding="0" cellspacing="1" id="roletable" style="text-align: center; font-size: 12px;" class="biaoge">
                    <tr class="bgtr01">
                        <td width="7%">
                            <input type="checkbox" id='allcheck' name='allcheck'>
                        </td>
                        <td width="26%">
                            角色名称
                        </td>
                        <td width="67%">
                            操作权限
                        </td>
                    </tr>
                </table>
                <input type="hidden" id="rolegrantPrivilege" name="roleProfile.grantPrivilege"><input type="hidden" id="updateroleId" name="updateroleId"><input type="hidden" id="updateroleprofileId" name="updateroleprofileId">
                <br/>
                <table align="right" width="30%">
                    <tr>
                        <td>
                            <input type="button" class="querenbutton" id="btnroleSave" value="保存" style="color: #11437f; font-size: 12px;" onclick="checkRoleProfileNotNullAll()">
                        </td>
                        <td>
                            <input type="button" class="querenbutton" id="btnroleEsc" value="取消" style="color: #11437f; font-size: 12px;" onclick="roleEsc()">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div id="insertUserRolefile" title="用户授权">
            <form action="insertUserProfile.action" method="post" name="insertUserProfimeForm" id="insertUserProfimeForm">
                <div id="divspanuser" style="display:none">
                    角色名称：<span id="spanUserRolename"></span>
                    用户名称：<span id="spanUserName"></span>
                    控制对象：<span id="spanObject"></span>
                </div>
                <div id="usertable">
                    <table style="font-size:12px;">
                        <tr>
                            <td>
                                角色名称
                            </td>
                            <td>
                                <select style="width: 120px" id="insertuserRoleName" onchange="getUserRoleChange(this)" name="insertuserRoleId">
                                </select>
                            </td>
                            <td>
                                用户名称
                            </td>
                            <td>
                                <select style="width: 100px" id="alluser" onchange="getUserNameChange(this)" name="insertUserName">
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                控制对象
                            </td>
                            <td colspan="3">
                                <select style="width: 100px" id="userProfileObject" name="userProfileObject">
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <table width="100%" border="0" cellpadding="0" cellspacing="1" id="userroletable" style="text-align: center; font-size: 12px;" class="biaoge">
                    <tr class="bgtr01">
                        <td width="7%">
                            <input type="checkbox" id='userallcheck' name='userallcheck'>
                        </td>
                        <td width="26%">
                            角色名称
                        </td>
                        <td width="67%">
                            操作权限
                        </td>
                    </tr>
                </table>
                <br/>
                <input type="hidden" name="userProfile.grantPrivilege" id="grantPrivilege" /><input type="hidden" name="updateUserId" id="updateUserId"><input type="hidden" name="updateUserProfileId" id="updateUserProfileId">
                <table align="right" width="30%">
                    <tr>
                        <td>
                            <input type="button" class="querenbutton" id="btnuserSave" value="保存" style="color: #11437f; font-size: 12px;" onclick="saveUserProfile()">
                        </td>
                        <td>
                            <input type="button" class="querenbutton" id="btnuserEsc" value="取消" style="color: #11437f; font-size: 12px;" onclick="btnuserProfileEsc()">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>