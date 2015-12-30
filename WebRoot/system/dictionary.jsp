<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8">
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
        <link href="../css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet" type="text/css">
        <link href="../css/base.css" rel="stylesheet" type="text/css">
        <link href="../css/input.css" rel="stylesheet" type="text/css">
        <link href="../css/jquery.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.jqgrid.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.multiselect.css" />
        <script src="../js/jquery.validate.1.8.js" type="text/javascript">
        </script>
        <script src="../js/jquery.maskedinput.js" type="text/javascript">
        </script>
        <script type="text/javascript" src="../js/popShortcut.js">
        </script>
        <title>数据字典管理</title>
        <script type="text/javascript" src="js/dictionary.js">
        </script>
        <script type="text/javascript">
            $(function(){
                var mId = "${param.menuId}";
                imgDisplay(mId);
            });
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
                        <span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif" onclick="topwin(${param.menuId })"></a></span><span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span><span class="topico2"><img src="../images/Accordion_ico03.gif"></span><h3>数据字典管理</h3>
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
                    <form action="getDictionaryData.action" method="post" id="jsonForm">
                        <table width="750">
                            <tr>
                                <td width="107" align="right">
                                    <strong style="font-size:14px;">字典编码</strong>
                                </td>
                                <td width="106">
                                    <select id="diccode" name="diccode" style="display:block;width:150px">
                                    </select>
                                </td>
                                <td width="77" align="right">
                                    <strong style="font-size:14px;">字典名称</strong>
                                </td>
                                <td width="304">
                                    <input type="text" name="dicname" id="dicname">
                                </td>
                            </tr>
                            <tr>
                                <td colspan="4" align="center">
                                    <input type="button" value="查询" onClick="selectDictionary()" class="chaxunbutton">
                                </td>
                            </tr>
                        </table>
                        <div class="bianxian" style="width:750px;">
                        </div>
                        <table width="195" border="0" cellspacing="5" cellpadding="0">
                            <tr>
                                <td width="88">
                                    <input type="button" value="增 加" onClick="insertDictionary()" class="dowbutton">
                                </td>
                                <td width="92">
                                    <input type="button" value="删除" onClick="deleteInfo()" class="upbutton">
                                </td>
                            </tr>
                        </table>
                        <div style="width:750px;">
                            <table id="rowedtable">
                            </table>
                            <div id="prowed4">
                            </div>
                        </div>
                    </form>
                    <div id="insertShowDialog" title="字典编辑" style="display:none;">
                        <form action="getUpdateDictionary.action" method="post" name="myform" id="myform">
                            <table border="0" cellpadding="0" cellspacing="5" style="font-size:12px;">
                                <tr>
                                    <td align="right">
                                        父类编码 
                                    </td>
                                    <td>
                                        <select id="parentCode" name="tdictionary.parentCode">
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">
                                        字典编码 
                                    </td>
                                    <td>
                                        <input type="text" id="insertcode" name="tdictionary.code" onblur="codetext()"><span id="insertcodeerror" style="display:none;color:red;font-size:11px;">请输入分类编码</span>
                                        <span id="insertcodeerrorValue" style="display:none;color:red;font-size:11px;">当前编码已存在</span>
                                    </td>
                                    <td align="right">
                                        字典名称 
                                    </td>
                                    <td>
                                        <input type="text" id="insertname" name = "tdictionary.name" onblur="nametext()"/><span id="insertnameerror" style="display:none;color:red;font-size:11px;">请输入分类名称</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">
                                        备注 
                                    </td>
                                    <td colspan="3">
                                        <textarea rows="5" cols="60" name="tdictionary.remark" id="insertremark">
                                        </textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        &nbsp; 
                                    </td>
                                    <td colspan="3">
                                        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="conttitle" style="border-top:none;">
                                            <tr>
                                                <td height="22">
                                                    <h3 style="font-size:12px; color:#476c89;">操作权限</h3>
                                                </td>
                                            </tr>
                                        </table>
                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td height="30" align="right">
                                                    <input type="button" value="清空" onClick="deleteroleinfo()" class="delbutton" style="font-size:12px;">
                                                </td>
                                            </tr>
                                        </table>
                                        <div align="center">
                                            <table width="100%" border="0" cellpadding="0" cellspacing="1" id="roletable" style="text-align:center; font-size:12px;" class="biaoge">
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
                                        </div>
                                    </td>
                                </tr>
                            </table>
                            <input type="hidden" id="tempspan" name="tdictionary.authControl"><input type="hidden" name="updateCode" id="updateCode">
                        </form>
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
    </body>
</html>
