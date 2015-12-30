<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<html>
    <head>
        <title>文件销毁列表信息</title>
        <script src="../js/jquery-1.5.min.js" type="text/javascript">
        </script>
        <script src="../js/jquery-ui-1.8.10.custom.min.js" type="text/javascript">
        </script>
        <script src="../js/jquery.cookie.js" type="text/javascript">
        </script>
        <script src="../js/jquery.dynatree.js" type="text/javascript">
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
        <script src="../js/jquery.maskedinput.js" type="text/javascript">
        </script>
        <script type="text/javascript" src="../js/jquery.autocomplete.js">
        </script>
        <link type="text/css" href="../css/jquery.autocomplete.css" rel="stylesheet" />
        <link href="../css/base.css" rel="stylesheet" type="text/css">
        <link href="../css/input.css" rel="stylesheet" type="text/css">
        <link href="../css/jquery.css" rel="stylesheet" type="text/css">
        <link href="../css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.jqgrid.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.multiselect.css" />
        <script type="text/javascript" src="js/docDetails.js">
        </script>
        <script type="text/javascript" src="../js/popShortcut.js">
        </script>
        <script type="text/javascript" src="js/docDestroyed.js">
        </script>
        <script type="text/javascript">
            $(function(){
                var mId = "${param.menuId}";
                imgDisplay(mId);
                //JqGrid
            
            });
        
            function projectNameBlur(){
                	var projectName = $("#projectName").val();
                	if(projectName.length==0){
                    	$("#projectNameCode").attr("value","");
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
                        <span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif" onclick="topwin(${param.menuId })"></a></span><span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span><span class="topico2"><img src="../images/Accordion_ico03.gif"></span><h3>文件销毁列表</h3>
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
                    <form action="selectDocDestoryed.action" method="post" name="docDestroyForm" id="docDestroyForm">
                        <table style="font-size:12px;font-weight:bold;"  width="60%">
                        	<tr>
                        		<td>
                                  <span id="projectTypespan">工程分类</span>  
                                </td>
                                <td>
                                    <select id="proClass" name="proClass" style="width:120px">
                                    </select>
                                </td>
                                <td>
                                    <span id="projectStatusspan">工程阶段</span>
                                </td>
                                <td >
                                    <select id="proStatus" style="width:80px" name="proStatus">
                                    </select>
                                </td>
                        	</tr>
                            <tr>
                            	<td>
                                    开工年份
                                </td>
                                <td>
                                    <input type="text" id="kaigongyear" name="kaigongyear" size="5"/>
                                </td>
                                <td>
                                    工程名称
                                </td>
                                <td colspan="3">
                                    <input id="projectNameCode" type="hidden" name="projectNameCode" >
                                    <input id="projectName" name="projectName" size="55" onblur="projectNameBlur()"/>
                                </td>
                           
                            </tr>
                            <tr>
                                 <td>
                                   <span id="baomispan">密级分类</span> 
                                </td>
                                <td >
                                    <select id="baomi" style="width:80px" name="baomi">
                                    </select>
                                </td>
                                <td>
                                    档案状态
                                </td>
                                <td colspan="3">
                                    <input type="radio" name="fileStatus" id="fileStatus" value="01" checked="checked">归档<input type="radio" name="fileStatus" id="fileStatus" value="02">销毁
                                </td>
                            </tr>
                            <tr>
                                <td colspan="4" align="center" style="padding-top:10px">
                                    <input type="button" value="查询" onclick="selectDocDestroy()" class="chaxunbutton">
                                </td>
                            </tr>
                        </table>
                        </form>
                        <div class="bianxian" style="width:750px;">
                        </div>
                        <table width="195" border="0" cellspacing="5" cellpadding="0">
                            <tr>
                                <td width="92">
                                    <input type="button" value="销毁" id="destory" onclick="updateDocDestory()" class="dowbutton">
                                </td>
                                <td width="88">
                                    <input type="button" value="恢复" id="comeBack" onclick="comeBackDocDestory()" class="upbutton">
                                </td>
                                <td width="88">
                                    <input type="button" value="清空" id="clear" onclick="clearDocDestory()" class="upbutton">
                                
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
        <!-- 档案详细信息 -->
        <div id="docInfo" title="档案信息">
            <div >
                <ul>
                    <h3>工程信息</h3>
                    <li id="proName">
                    </li>
                    <li id="ownerCode">
                    </li>
                    <li id="wangSheng">
                    </li>
                    <li id="projectType">
                    </li>
                    <li id="projectStatus">
                    </li>
                    <li id="proVoltagelevel">
                    </li>
                    <li id="designCode">
                    </li>
                </ul>
            </div>
            <div style="margin-top:20px;">
                <ul>
                    <h3>档案信息</h3>
                    <li id="zongHeFile">
                    </li>
                    <li id="docClass">
                    </li>
                    <!--<li id="sheJiName"></li>  -->
                    <li id="zhuanye">
                    </li>
                    <li id="docVersion">
                    </li>
                </ul>
            </div>
            <div style="margin-top:20px;">
                <ul>
                    <h3>文件信息</h3>
                    <li id="docName">
                    </li>
                    <li id="docFormat">
                    </li>
                    <li id="suffix">
                    </li>
                    <li id="baomiinfo">
                    </li>
                    <li id="docStatus">
                    </li>
                    <li id="docCreateDate">
                    </li>
                    <li id="docCreate">
                    </li>
                    <li id="docFileSize">
                    </li>
                </ul>
            </div>
        </div>
    </body>
</html>
