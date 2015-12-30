<%@ page language="java"import="java.util.*,com.nsc.dem.bean.archives.TDocType"pageEncoding="utf-8" %>
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>文档分类列表</title>
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
        <script type="text/javascript" src="js/docType.js">
        </script>
        <script type="text/javascript">
            $(function(){
                var mId = "${param.menuId}";
                imgDisplay(mId);
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
                
            });
        </script>
        <style type="text/css">
            .ui-datepicker SELECT.ui-datepicker-year {
                width: 70px;
            }
            
            .ui-datepicker SELECT.ui-datepicker-month {
                width: 63px;
            }
            
            .ui-state-default {
                font-size: 11px;
            }
            
            .ui-datepicker {
                width: 15em;
            }
            
            .ui-datepicker-calendar th {
                padding: 0.3em;
            }
        </style>
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
                        <span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif" onclick="topwin(${param.menuId })"></a></span><span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span><span class="topico2"><img src="../images/Accordion_ico03.gif"></span><h3>分类管理</h3>
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
                    <form action="getFileGrouping.action" method="post" id="jsonForm">
                        <table width="750" border="0" cellpadding="0" cellspacing="3" style="font-Size:12px; font-weight:bold;">
                            <tr>
                                <td align="right">
                                    分类编码 
                                </td>
                                <td>
                                    <select name="docCode" id="docCode" style="width:60pt">
                                    </select>
                                </td>
                                <td align="right">
                                   <span id="docClassspan">文档类型</span> 
                                </td>
                                <td>
                                    <select id="docClass" name="docClass" style="width:80pt">
                                    </select>
                                </td>
                                <td align="right">
                                    <span id="zhuanyeSpan">专业</span> 
                                </td>
                                <td>
                                    <select id="docZhuanye" name="docZhuanye" style="width:60pt">
                                    </select>
                                </td>
                                <td align="right">
                                <span id="baomispan">密级分类</span> 
                                </td>
                                <td>
                                    <select id="docBaomi" name="docBaomi" style="width:40pt">
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">
                                    分类名称
                                </td>
                                <td>
                                    <input type="text" id="docName" name="docName" size="15">
                                </td>
                                <td align="right">
                                    创建者 
                                </td>
                                <td>
                                    <input type="text" name="docCreator" id="docCreator" size="8" />
                                </td>
                                <td align="right">
                                    创建时间 
                                </td>
                                <td colspan="4">
                                    <input type="text" name="docCreateFormDate" id="from" size="10" />到 <input type="text" name="docCreateToDate" id="to" size="10" />
                                </td>
                            </tr>
                            <tr>
                                <td align="right">
                                    是否关联部件 
                                </td>
                                <td colspan="7">
                                    <input type="radio" name="docComFlag" id="docComFlag" value="0" checked="checked">否<input type="radio" name="docComFlag" id="docComFlag" value="1">是 
                                </td>
                            </tr>
                            <tr>
                                <td colspan="8" align="center">
                                    <input type="button" name="btnSelect" id="btnSelect" value="查   询" onclick="selectDictionary()" class="cxtjbutton" />
                                </td>
                            </tr>
                        </table>
                        <div class="bianxian" style="width:750px;">
                        </div>
                        <table width="187" border="0" cellspacing="5" cellpadding="0">
                            <tr>
                                <td width="92">
                                    <input type="button" name="btnCreate2" id="btnCreate2" value="增加 " onclick="insertFunction()" class="dowbutton">
                                </td>
                                <td width="88">
                                    <input type="button" name="btnDelete2" id="btnDelete2" value="删 除" onclick="deleteInfo()" class="upbutton" />
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
                    <div id="insertShowDialog" title="文档分类编辑" style="display:none;">
                        <form method="post" name="myform" action="getInsertFileGrouping.action" id="myform">
                            <table width="628" border="0" cellpadding="0" cellspacing="5" style="font-Size:12px;">
                                <tr>
                                    <td width="87" align="right">
                                        所属分类 
                                    </td>
                                    <td colspan="3">
                                        <select name="tdocType.parentCode" id="parentCode" onchange="codeRule(this)">
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">
                                        分类编码 
                                    </td>
                                    <td width="241">
                                        <input type="text" name="tdocType.code" id="insertcode" onblur="codetext()">
                                        <br/>
                                        <span id="insertcodeerrorvalue" style="display:block;color:red;fontSize:11px;">例：父编码(WL14ZH)子编码(WL14ZH1234)</span>
                                        <span id="insertcodeerror" style="display:none;color:red;fontSize:11px;">当前编码存在</span>
                                    </td>
                                    <td width="71" align="right">
                                        分类名称 
                                    </td>
                                    <td width="204">
                                        <input type="text" name="tdocType.name" id="insertname" size="20" onblur="nametext()"><span id="insertnameerror" style="display:none;color:red;fontSize:11px;">请输入分类名称</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">
                                        专业 
                                    </td>
                                    <td>
                                        <select id="insertspeciality" name="tdocType.speciality" style="width:60px">
                                        </select>
                                    </td>
                                    <td align="right">
                                        <span id="baomispan">密级分类</span>  
                                    </td>
                                    <td>
                                        <select id="insertdftSecurity" name="tdocType.dftSecurity" style="width:60px">
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">
                                        是否有关联部件 
                                    </td>
                                    <td>
                                        <input type="radio" name='tdocType.comFlag' value='0' checked='checked' id="insertcomFlagfalse">否<input type="radio" name='tdocType.comFlag' value='1' id="insertcomFlagtrue">是
                                    </td>
                                    <td align="right">
                                        &nbsp; 
                                    </td>
                                    <td>
                                        &nbsp;
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">
                                        说明
                                    </td>
                                    <td colspan="3">
                                        <textarea rows="5" cols="60" name="tdocType.remark" id="remark">
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
                                                    <input type="reset" value="清空" class="delbutton" style="font-size:12px;">
                                                </td>
                                            </tr>
                                        </table>
                                        <div align="center">
                                            <table width="100%" border="0" cellpadding="0" cellspacing="1" class="biaoge" id="roletable" style="text-align:center;font-Size:12px;">
                                                <tr class="bgtr01" style="color:#476c89;">
                                                    <td width="6%">
                                                        <input type="checkbox" id='allcheck' name='allcheck'>
                                                    </td>
                                                    <td width="31%">
                                                        角色名称 
                                                    </td>
                                                    <td width="63%">
                                                        操作权限 
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                            <input type="hidden" id="tempspan" name="tdocType.privilege"><input type="hidden" id="updateCode" name="updateCode"/>
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
