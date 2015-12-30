<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/"; %>
<html>
    <head>
        <title>工程信息管理</title>
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
        <script src="../js/jquery.ui.datepicker-zh-CN.js" type="text/javascript">
        </script>
        <script type="text/javascript"src="../archives/js/docDetails.js">
        </script>
        <script type="text/javascript" src="../js/jquery.autocomplete.js">
        </script>
        <script type="text/javascript" src="../js/popShortcut.js">
        </script>
        <script type="text/javascript" src="js/projectInfoManager.js">
        </script>
             <script src="../js/jquery.maskedinput.js" type="text/javascript">
        </script>
        <script type="text/javascript">
            $(function(){
                var mId = "${param.menuId}";
                imgDisplay(mId);
            });
            function unitNameBlur(){
					var unitName = $("#unitName").val();
					if(unitName.length==0){
						 $("#unitNameCode").attr("value","");
					}
                }
            function projectNameBlur(){
                	var projectName = $("#projectName").val();
                	if(projectName.length==0){
                    	$("#projectNameId").attr("value","");
                    	}
                }
                
                function insertParentNameBlur(){
                var projectName = $("#insertParentName").val();
                	if(projectName.length==0){
                    	$("#insertParentNameId").attr("value","");
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
                        <span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif"></a></span><span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span><span class="topico2"><img src="../images/Accordion_ico03.gif"></span><h3>工程管理</h3>
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
                    <form action="selectProjectInfo.action" method="post" name="projectForm" id="projectForm">
                        <table style="font-size:12px;" width="60%">
                            <tr  height="30px" >
                                <td>
                                    工程名称
                                </td>
                                <td colspan="3">
                                    <input id="projectNameId" type="hidden" name="projectNameId" >
                                    <input id="projectName" name="projectName" size="35" onblur="projectNameBlur()"/>
                                </td>
                			   <td>
                              	      单位名称
                                </td>
                                <td colspan="3">
                                    <input type="text" name="unitName" id="unitName" size="35" onblur="unitNameBlur()">
                                    <input type="hidden" size="10" name="unitNameCode" id="unitNameCode">
                                </td>
                
                            </tr>
                            <tr height="30px">
                               <td>
                                     <span id="projectTypespan">工程分类</span>    
                                </td>
                                <td>
                                    <select id="proType" style="width:100px;" name="proType">
                                    </select>
                                </td>
                             
                                <td>
                                    <span id="projectStatusspan">工程阶段</span>
                                </td>
                                <td>
                                    <select id="proStatus" name="proStatus" style="width:100px;">
                                    </select>
                                </td>
                                                <td>
                                    <span id="voltageLevelspan">电压等级</span>
                                </td>
                                <td>
                                    <select id="voltageLevel" name="voltageLevel" style="width:100px;">
                                    </select>
                                </td>
                                <td>
                                    开工年份
                                </td>
                                <td>
                                    <input type="text" id="openYear" name="openYear" size="4"/><span style="color: red; fontSize: 11px;">*2011</span>
                                </td>
                            </tr>
                            <tr>
                            	<td colspan="9">&nbsp;</td>
                            </tr>
                            <tr>
                                <td colspan="9" align="center">
                                    <input type="button" value="查询" class="chaxunbutton" onclick="selectProjectInfo()">
                                </td>
                            </tr>
                        </table>
                        <div class="bianxian" style="width: 750px;">
                        </div>
                        <table width="195" border="0" cellspacing="5" cellpadding="0">
                            <tr>
                                <td width="92">
                                    <input type="button" value="新增" id="destory" class="dowbutton" onclick="insertProjectInfo()">
                                </td>
                                <td width="88">
                                    <input type="button" value="删除" id="comeBack" class="upbutton" onclick="deleteProjectInfo()">
                                </td>
                            </tr>
                        </table>
                    </form><!-- Jqgrid -->
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
        <div id="showProjectInfo" title="工程编辑">
            <center>
                <form action="updateProjectInfo.action" method="post" name="insertProjectForm" id="insertProjectForm">
                    <table style="font-size:12px;" width="90%">
                        <tr >
                            <td>
                                业主单位
                            </td>
                            <td colspan="3">
                                <input type="hidden" name="tproject.id" id="projectId"><input type="text" name="insertUnitName" id="insertUnitName" size="55" onblur="checkUnitName()"><span id="insertUnitNameerror" style="display: none; color: red; fontSize: 11px;">请输入业主单位</span>
                                <input type="hidden" size="10" name="insertUnitNameCode" id="insertUnitNameCode">
                            </td>
                        
                        </tr>
                        <tr>
                           
                            <td>
                                工程名称
                            </td>
                            <td>
                                <input type="text" id="insertProjectName" name="tproject.name" onblur="checkProjectName()"><span id="insertProjectNameerror" style="display: none; color: red; fontSize: 11px;">请输入工程名称</span>
                            </td>
                             <td>
                                <span id="sprojectCode">工程编码</span>
                            </td>
                            <td>
                                <input type="text" id="insertProjectCode" name="tproject.code" >
                            </td>
                    
                        </tr>
                        <tr>
                            <td>
                                所属工程
                            </td>
                            <td colspan="3">
                                <input type="text" size="55" id="insertParentName" >
                                <input type="hidden" id="insertParentNameId" name="tproject.parentId" onblur="insertParentNameBlur()">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                工程分类
                            </td>
                            <td>
                                <select id="insertProType" style="width:120px;" name="tproject.type">
                                </select>
                            </td>
                            <td>
                                初设单位
                            </td>
                            <td>
                                <input type="text" name="designUnitName" id="designUnitName" onblur="checkDesignUnitName()">
                                <input type="hidden" size="10" name="designUnitNameCode" id="designUnitNameCode"><span id="designUnitNameCodeerror" style="display: none; color: red; fontSize: 11px;">请输入初设单位</span>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                电压等级
                            </td>
                            <td>
                                <select id="insertVoltageLevel" style="width:120px;" name="tproject.voltageLevel">
                                </select>
                                <span id="voltagespan"></span>
                            </td>
                            <td>
                                工程阶段
                            </td>
                            <td>
                                <select id="insertProStatus" name="tproject.status" style="width:120px;">
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                初设日期
                            </td>
                            <td>
                                <input type="text" id="datepicker2" name="tproject.preDesignYear"  onchange="checkPreDesignYear()"><span id="preDesignYearerror" style="display: none; color: red; fontSize: 11px;">请输入初设年月</span>
                               <span id="fromspan"></span> 
                                
                            </td>
                            <td>
                                开工日期
                            </td>
                            <td>
                                <input type="text" id="from" name="tproject.openYear"  onchange="checkOpenYear()">
                                <span id="tospan"></span>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                竣工日期
                            </td>
                            <td colspan="3">
                                <input type="text" id="to" name="tproject.closeYear"/>
                            </td>
                        </tr>
                    </table>
                </form>
            </center>
        </div>
    </body>
</html>
