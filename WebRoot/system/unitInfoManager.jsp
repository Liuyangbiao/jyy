<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/"; %>
<html>
    <head>
        <title>单位信息管理</title>
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
        <script src="../js/jquery.ui.datepicker-zh-CN.js" type="text/javascript">
        </script>
  
        <script type="text/javascript" src="../js/jquery.autocomplete.js">
        </script>
        <script type="text/javascript" src="../js/popShortcut.js">
        </script>
        <script type="text/javascript" src="js/unitInfoManager.js">
        </script>
        <script type="text/javascript">
            $(function(){
                var mId = "${param.menuId}";
                imgDisplay(mId);
             // 自动联想功能
        		$.getJSON('unitType.action?time=' + Math.random(), function(json) {
        			initAutoComplete(json, "#unitType", "");
        		});

        		$("#unitTypeCode").bind('change', function() {
       			 var val = $("#unitTypeCode").attr("value");
       				 var pn = $("#unitName").attr("value");
       				 $("#unitName").attr("value", "");
       				 $("#unitNameCode").attr("value", "");
       				 $("#unitName").unbind();
       				 $.getJSON('unitNameAll.action?time=' + Math.random(), {
       					 type : val
       				 	}, function(json) {
       				 		initAutoComplete(json, "#unitName", pn);
       				});

       		     });
            });
            function unitNameBlur(){
             	var unitName = $("#unitName").val();
                	if(unitName.length==0){
                    	$("#unitNameCode").attr("value","");
                    	}
            }
            function initAutoComplete(json, valId, pn) {
            	$(valId).autocomplete(json, {
            		max : 10, // 列表里的条目数
            		minChars : 0, // 自动完成激活之前填入的最小字符,为0双击弹出列表
            		matchCase : false,// 不区分大小写
            		width : 180, // 提示的宽度，溢出隐藏
            		scrollHeight : 300, // 提示的高度，溢出显示滚动条
            		matchContains : true, // 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
            		autoFill : false, // 自动填充 ，就是在文本框中自动填充符合条件的项目
            		mustMatch : true, // 自动匹配，mustMatch表示必须匹配条目，也就是在文本框里输入的内容，必须是data参数里的数据，如果不匹配，文本框就被清空
            		formatItem : function(row, i, max, term) {
            			var v = $(valId).val();
            			return row.name;
            			// return i + '/' + max + ':' + "<I >" + row.name +"</I>"+ "<span
            			// style='display:none;'>"+ '"[' + row.id + ']' +"</span>";
            		// formatItem作用在于可以格式化列表中的条目，比如我们加了“I”，让列表里的字显示出了斜体
            		if (row.name.indexOf(v) == 0 || row.spell.indexOf(v) == 0) {
            			return row.name + " (" + row.spell + ")";
            		} else
            			return false;
            	},
            	formatMatch : function(row, i, max) {
            		// return row.name + row.id;
            		if (row.name == pn) {
            			$(valId).attr("value", pn);
            			$(valId + "Code").attr("value", row.id);
            		}
            		return row.name + " (" + row.spell + ")";
            		// formatMatch是配合formatItem使用，作用在于，由于使用了formatItem，所以条目中的内容有所改变，而我们要匹配的是原始的数据，所以用formatMatch做一个调整，使之匹配原始数据
            	},
            	formatResult : function(row) {

            		return row.name;
            		// formatResult是定义最终返回的数据，比如我们还是要返回原始数据，而不是formatItem过的数据
            	}
            	}).result(function(event, row, formatted) {

            		if (row == "undefined" || row == null) {
            			$(valId + "Code").attr("value", "");
            		} else {
            			$(valId + "Code").attr("value", row.id);
            		}

            		$(valId + "Code").trigger("change");
            	});
            }
            function changeUnitType(){
            	var unitType = $("#unitType").val();
            	if(unitType.length==0){
            		$("#unitTypeCode").attr("value","");
                	$("#unitName").attr("value","");
                	$("#unitNameCode").attr("value","");
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
                        <span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif" onclick="topwin(${param.menuId })"></a></span><span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span><span class="topico2"><img src="../images/Accordion_ico03.gif"></span><h3>单位管理</h3>
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
                    <form action="selectDocDestoryed.action" method="post" name="unitForm" id="unitForm">
                        <table style="font-size:12px;">
                            <tr>
                             <td>
                                    单位类型
                                </td>
                                <td>
                                <input type="text" name="unitType1" id="unitType" onblur="changeUnitType()">
                                </td>
                                <td>
                                    单位名称
                                </td>
                                <td>
                                    <input type="text" name="unitName" id="unitName" onblur="unitNameBlur()">
                                
<!--                                </td>-->
<!--                               -->
<!--                                <td>-->
                                    单位状态
<!--                                </td>-->
<!--                                <td>-->
                                    <input type="radio" checked="checked" id="isUsabletrue" name="isUsable" value="1">可用<input type="radio" id="isUsablefalse" name="isUsable" value="0">不可用
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    单位地址
                                </td>
                                <td width="100">
                                    <input type="text" id="unitAddress" name="unitAddress">
                                </td>
                                <td>
                                    联系电话
                                </td>
                                <td>
                                    <input type="text" name="unitTelePhone" id="unitTelePhone" onblur="checkTele()">(例：010-XXXXXXXX)<span id="unitTelePhoneerror" style="display: none; color: red; fontSize: 11px;">请输入正确的电话格式</span>
                                </td>
                                <td align="right">
                                    <input type="button" value="查询" onclick="selectUnitInfo()" class="chaxunbutton">
                                </td>
                            </tr>
                            <tr>
                            </tr>
                            <tr style="display:none">
                               <td>
                              <input type="text" name="unitType" id="unitTypeCode">
                               </td>
                               <td>    <input type="text" size="10" name="unitNameCode" id="unitNameCode"></td>
                            </tr>
                        </table>
                        <div class="bianxian" style="width: 750px;">
                        </div>
                        <table width="195" border="0" cellspacing="5" cellpadding="0">
                            <tr>
                                <td width="92">
                                    <input type="button" value="新增" id="destory" class="dowbutton" onclick="insertUnitShowDailog()">
                                </td>
                                <td width="88">
                                    <input type="button" value="删除" id="comeBack" onclick="deleteUnitInfo()" class="upbutton">
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
        <div id="insertUnitInfo" title="单位编辑">
            <form id="insertUniForm" action="insertUnit.action" method="post" name="insertUniForm">
                <table border="0" width="100%" style="font-size: 12px;">
                    <tr>
                        <td>
                            单位编码
                        </td>
                        <td>
                            <input type="text" name="unit.code" id="insertCode">
                            <br/>
                            <span id="insertCodeError" style="display: none; color: red; fontSize: 11px;">请输入单位编码</span>
                        </td>
                        <td>
                            单位名称
                        </td>
                        <td>
                            <input type="text" name="unit.name" id="insertName" onblur="checkUnitName()">
                            <br/>
                            <span id="insertNameerror" style="display: none; color: red; fontSize: 11px;">请输入单位名称</span>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            单位类型
                        </td>
                        <td>
                            <select id="insertUnitType" name="unit.type" onchange="updateUnitTypeValue()">
                            </select>
                        </td>
                        <td>
                            单位简称
                        </td>
                        <td>
                            <input type="text" name="unit.shortName" id="shortName" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            单位地址
                        </td>
                        <td colspan="3">
                            <input type="text" size="55" name="unit.address" id="insertAddress">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            联系电话
                        </td>
                        <td>
                            <input type="text" name="unit.telephone" id="insertTele" onblur="checkTelePhone()">(例：010-XXXXXXXX)<span id="insertTeleerror" style="display: none; color: red; fontSize: 11px;">请输入正确的电话格式</span>
                        </td>
                        <td>
                            单位状态
                        </td>
                        <td>
                            <input type="radio" checked="checked" id="usabletrue" name="unitisUsable" value="1">可用<input type="radio" id="usablefalse" name="unitisUsable" value="0">不可用
                        </td>
                    </tr>
                    <tr>
                        <td colspan="5"><input type="hidden" id="unitCode" name="unitCode">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div id="userRoleDialog" title="关联的用户信息">
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
                <h3 style="color: red;">此单位存在用户，不能删除该单位</h3>
            </center>
        </div>
        
        <div id="projectDialog" title="关联工程的初设单位信息">
            <table width="100%" border="0" cellpadding="0" cellspacing="1" id="desgtable" style="text-align:center; font-size:12px;" class="biaoge">
                <tr class="bgtr01">
                    <td>
                        工程编码
                    </td>
                    <td>
                        工程名称
                    </td>
                </tr>
            </table>
            <br/>
            <br/>
            <center>
                <h3 style="color: red;">此单位存在工程，不能删除该单位</h3>
            </center>
        </div>
        <div id="owenprojectDialog" title="关联工程的业主单位信息">
            <table width="100%" border="0" cellpadding="0" cellspacing="1" id="owentable" style="text-align:center; font-size:12px;" class="biaoge">
                <tr class="bgtr01">
                    <td>
                        工程编码
                    </td>
                    <td>
                        工程名称
                    </td>
                </tr>
            </table>
            <br/>
            <br/>
            <center>
                <h3 style="color: red;">此单位存在工程，不能删除该单位</h3>
            </center>
        </div>
    </body>
</html>