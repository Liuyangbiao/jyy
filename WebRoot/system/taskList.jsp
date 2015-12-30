<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/"; %>
<html>
	<head>
		<title>任务调度</title>
		<link type="text/css" href="../css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet" />
		<link href="../css/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
		<link href="../css/input.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.multiselect.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.multiselect.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="../css/base.css">
		<script type="text/javascript" src="../js/jquery-1.5.min.js"></script>
		<script type="text/javascript" src="../js/jquery.validate.1.8.js"></script>
		<script type="text/javascript" src="../js/jquery.cookie.js"></script>
		<script type="text/javascript" src="../js/jquery.jqGrid.js"></script>
		<script type="text/javascript" src="../js/jquery-ui-1.8.10.custom.min.js"></script>
		<script src="../js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
		<script src="../js/jquery.timepicker.js" type="text/javascript"></script>
		<script type="text/javascript" src="../js/jquery.autocomplete.js"></script>
		<script type="text/javascript" src="../js/jquery.form.js"></script>
		<script type="text/javascript" src="../js/popShortcut.js"></script>
		<script src="../js/jquery.maskedinput.js" type="text/javascript"></script>
		<script type="text/javascript">
            $(function(){
                //Tabs
                $("#tabs").tabs({
                    cookie: {expires: 1}
                });
                //设置快捷键
                var setKeyId = "${param.menuId}";
                imgDisplay(setKeyId);
              	//Ajax请求函数
                if(${fn:length(USER_KEY.TUnit.code) ==8 }){
                    $("#pSelAll").attr("disabled","disabled");
                    $("#pUnselAll").attr("disabled","disabled");
                }
                $("#tab-1").click(function(){
                    clearSel('cTasks');
                });
                $("#tab-2").click(function(){
					$("#whoForm").attr("action","getAreaInfoTask.action");
                    $("#whoForm").ajaxSubmit({
						dataType: "json" ,
						type: "post" ,
						contentType: "application/x-www-form-urlencoded;charset=utf-8",
						success:function callBack(data){
						   		$("#areaTab tr:not(:eq(0))").remove();//删除第一行
		                        var newtr = $("<tr></tr>");
				   	            	 for (var i = 0; i < data.length; i++) {
				   		                 var role = $("<td></td>");
				   	                     // 角色
				   	                     $(role).html("<input type='checkbox' value='" + data[i].code + "' id = '" + data[i].code + "' name = 'area' >" + data[i].name);
				   	                     $(newtr).append(role);
				   	                 }
			                        // 将行数据追加到表格中
			                        $("#areaTab").append($(newtr));
				   	             }
                    });
                    clearSel('aTasks');
                });
				$("#tab-3").click(function(){
					$("#whoForm").attr("action","getProvinceInfoTask.action");
                    $("#whoForm").ajaxSubmit({
						dataType: "json" ,
						type: "post" ,
						contentType: "application/x-www-form-urlencoded;charset=utf-8",
						success:function callBack(json){
								$("#proTab tr:not(:eq(0))").remove();
						   		var table = $("#proTab");
			                    var row=-1;
			                    var tr;
			                    for (var i= 0 ;i<json.length;i++) {
			                        var value = json[i].code;
			                        var text = json[i].name;
			                        if (text == undefined) {
			                            continue;
			                        }
			                        var td=$("<td><input type='checkbox' name='province' id = '"+ value +"' value='" + value + "'/>" + text + "</td>");
									var crow =parseInt(i/4);
									//新行
									if(crow!=row ){
						                   $(table).append($(tr));
											var trid="proCheckBox"+crow;
					     	                tr=$("<tr id='"+trid+"'></tr>");
					     	                row=crow;
										}
								   $(tr).append(td);					 
								  //最后一行
								  if((i+1)==json.length){
					                   $(table).append($(tr));
									}
			                    }
				   	             }
                    });
	                clearSel('pTasks');
                });
                //国网任务
                $("#cExecute").click(function(){
                    if(checkChecked("cTasks") > 0 ){
	                	$("#taskLists").attr("value",getTaskList("cTasks"));
	                	$("#serverLists").attr("value","");
	                	$("#whoTask").attr("value","country");
						$("#myForm").ajaxSubmit({
		                      dataType: "json",
		                      type: "post",
		                      contentType: "application/x-www-form-urlencoded;charset=utf-8",
		                      success: function(json){alert(json);}
		                 });
                    }else {
                        alert("请选中需要执行的任务");
                        return false;
                    }
                });
                //区域任务
				$("#aExecute").click(function(){
					if(${fn:length(USER_KEY.TUnit.code) == 6 }){
						if(checkChecked("aTasks") > 0){
							$("#taskLists").attr("value",getTaskList("aTasks"));
		                    $("#serverLists").attr("value",getTaskList("area"));
		                	$("#whoTask").attr("value","country");
							$("#myForm").ajaxSubmit({
			                      dataType: "json",
			                      type: "post",
			                      contentType: "application/x-www-form-urlencoded;charset=utf-8",
			                      success: function(json){alert(json);}
			                 });
						}else {
							alert("请选中需要执行的任务或区域!");
							return false;
						}
					}else {
						if(checkChecked("aTasks") > 0 && checkChecked("area") > 0 ){
							$("#taskLists").attr("value",getTaskList("aTasks"));
		                    $("#serverLists").attr("value",getTaskList("area"));
		                	$("#whoTask").attr("value","country");
							$("#myForm").ajaxSubmit({
			                      dataType: "json",
			                      type: "post",
			                      contentType: "application/x-www-form-urlencoded;charset=utf-8",
			                      success: function(json){alert(json);}
			                 });
						}else {
							alert("请选中需要执行的任务或区域!");
							return false;
						}
					}
                });
                //省公司任务
				$("#pExecute").click(function(){
					if(${fn:length(USER_KEY.TUnit.code) ==8 }){
						if(checkChecked("pTasks") > 0){
		                    $("#taskLists").attr("value",getTaskList("pTasks"));
		                    $("#serverLists").attr("value",getTaskList("province"));
		                    if(${fn:length(USER_KEY.TUnit.code) <=6  }){
		                		$("#whoTask").attr("value","country");
		                    }else {
		                    	$("#whoTask").attr("value","province");
		                    }
							$("#myForm").ajaxSubmit({
			                      dataType: "json",
			                      type: "post",
			                      contentType: "application/x-www-form-urlencoded;charset=utf-8",
			                      success: function(json){alert(json);}
			                 });
						}else {
							alert("请选中需要执行的任务或省份!");
							return false;
						}
					}else {
						if(checkChecked("pTasks") > 0 && checkChecked("province") > 0 ){
		                    $("#taskLists").attr("value",getTaskList("pTasks"));
		                    $("#serverLists").attr("value",getTaskList("province"));
		                    if(${fn:length(USER_KEY.TUnit.code) <=6  }){
		                		$("#whoTask").attr("value","country");
		                    }else {
		                    	$("#whoTask").attr("value","province");
		                    }
							$("#myForm").ajaxSubmit({
			                      dataType: "json",
			                      type: "post",
			                      contentType: "application/x-www-form-urlencoded;charset=utf-8",
			                      success: function(json){alert(json);}
			                 });
						}else {
							alert("请选中需要执行的任务或省份!");
							return false;
						}
					}
				});
				//判定是否选中其中一个
				function checkChecked(flag){
					return $("input[name='"+ flag +"']:checked").length;
				}
				//国网全选
				$("#cSelAll").click(function(){
					selAll("cTasks");
				});
				$("#cUnselAll").click(function(){
					unSelAll("cTasks");
				});
				//区域全选
				$("#aSelAll").click(function(){
					selAll("area");
				});
				$("#aUnselAll").click(function(){
					unSelAll("area");
				});
				//省公司全选
				$("#pSelAll").click(function(){
					selAll("province");
				});
				$("#pUnselAll").click(function(){
					unSelAll("province");
				});
				$("#psSelAll").click(function(){
					selAll("pTasks");
				});
				$("#psUnselAll").click(function(){
					unSelAll("pTasks");
				});
                //获取执行任务列表
                function getTaskList(flag){
                	var lists="";
                    $("input[name='"+flag+"']:checked").each(function(){
                    	lists+=$(this).val()+",";
                    });
                    return lists;
                };
                //全选
                function selAll(flag){
                	$("input[name='"+flag+"']").attr("checked",'true');
                };
              	//反选
                function unSelAll(flag){
                	$("input[name='"+flag+"']").each(function(){
                 	   if($(this).attr("checked")) {
                 	   	  $(this).removeAttr("checked");
                 	   } else {
                 	   	  $(this).attr("checked",'true');
                 	   }
                 	 });
                };
                //清空选中
                function clearSel(flag){
                    $("input[name='"+flag+"']").each(function(){
						if($(this).attr("checked"))
                 	   	  $(this).removeAttr("checked");
                    });
                }
            });
        </script>
        <style>
        .tables{
	         border:1px solid #AED0EA;
	         width:100%;
	         margin-top:5px;
         }
         .tables tr td {
         	 font-size:13;
         }
         .tables2{
          	 margin-top:15px;
          	 width:100%; 
         }
         .tables2 tr th {
         	 color:#0063A9;
         	 font-weight:bold;
         	 font-family:Cursive;
         	 border-bottom:1px solid #AED0EA;
         }
        </style>
	</head>
	<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="contert2">
			<tr>
				<td class="conttop2">
					<div class="top2left">
					</div>
					<div class="top2right">
					</div>
					<div class="top2middle">
						<span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif"
									onclick="topwin(${param.menuId })" style="cursor: hand"></a>
						</span><span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a>
						</span><span class="topico2"><img src="../images/Accordion_ico03.gif"></span>
						<h3>任务调度管理</h3>
					</div>
				</td>
			</tr>
			<tr>
				<td class="contmiddle2">
					<table width="750" border="0" cellspacing="0" cellpadding="0"
						style="font-size: 12px; font-weight: bold;">
						<tr>
							<td>
								<div id="tabs">
									<ul>
										<c:if test="${fn:length(USER_KEY.TUnit.code)==4 }">
										<li>
											<a href="#tabs-1" id = "tab-1" >国网任务</a>
										</li>
										</c:if><c:if test="${fn:length(USER_KEY.TUnit.code) <=6 }">
										<li>
											<a href="#tabs-2" id = "tab-2" >区域任务</a>
										</li>
										</c:if><c:if test="${fn:length(USER_KEY.TUnit.code) <=8 }">
										<li>
											<a href="#tabs-3" id = "tab-3" >省公司任务</a>
										</li>
										</c:if>
									</ul>
									<c:if test="${fn:length(USER_KEY.TUnit.code) ==4 }">
									<div id="tabs-1">
										<table class = "tables2" >
											<tr>
												<th style = "text-align:center;" >国网任务列表</th>
											</tr>
											</table><table class = "tables" >
											<tr>
												<td><input type = "checkbox" id = "createTask" name = "cTasks" value = "A" />创建并发送数据同步清单</td>
												<td><input type = "checkbox" id = "docIndex" name = "cTasks" value = "F" />创建索引</td>
											</tr>
											<tr>
												<td><input type = "checkbox" id = "fileReceive" name = "cTasks" value = "I" />文件接收(来自博微)</td>
												<td><input type = "checkbox" id = "docImg" name = "cTasks" value = "H" />生成缩略图</td>
											</tr>
											<tr>
												<td><input type = "checkbox" id = "downloadIndex" name = "cTasks" value = "G" />下载检索文件</td>
												<td><input type = "checkbox" id = "suggestion" name = "cTasks" value = "J" />检索提示信息</td>
											</tr>
											<tr>
												<td><input type = "checkbox" id = "backup" name = "cTasks" value = "K" />日志备份</td>
												<td><input type = "checkbox" id = "logDelete" name = "cTasks" value = "L" />日志删除</td>
											</tr>
											<tr>
												<td colspan = 2 ><input type = "checkbox" id = "sendServersInfo" name = "cTasks" value = "D" />下发服务器配置信息</td>
											</tr>
											<tr>
												<td colspan = 2 style = "text-align:center" ><input type = "button" class = "dowbutton" value = "执行" id = "cExecute" >
												<input type = "button" class = "upbutton" value = "全选" id = "cSelAll" >
												<input type = "button" class = "upbutton" value = "反选" id = "cUnselAll" ></td>
											</tr>
										</table>
									</div>
									</c:if><c:if test="${fn:length(USER_KEY.TUnit.code) <=6 }">
									<div id="tabs-2">
										<table class = "tables2" >
											<tr>
												<th style = "text-align:center" >区域任务列表</th>
											</tr>
											<tr>
												<td>
													<table class = "tables"  id = "areaTab" >
													<tr><td colspan = 4 style = 'text-align:right;' ><input type = 'button' class = 'upbutton' value = '全选' id = 'aSelAll' >
													<input type = 'button' class = 'upbutton' style = 'margin-left:5px;' value = '反选' id = 'aUnselAll' ></td></tr>
													</table>
													<table class = "tables" >
														<tr>
															<td><input type = "checkbox" id = "dataSyn" name = "aTasks" value = "B" />数据同步</td>
															<td><input type = "checkbox" id = "uploadIndex" name = "aTasks" value = "E" />上传检索文件</td>
															<td><input type = "checkbox" id = "uploadServer" name = "aTasks" value = "C" />上传服务器配置信息</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td colspan = 2 style = "text-align:center" ><input type = "button" class = "dowbutton" style = "font-size:13; " value = "执行" id = "aExecute" ></td>
											</tr>
										</table>
									</div>
									</c:if><c:if test="${fn:length(USER_KEY.TUnit.code) <=8 }">
									<div id="tabs-3">
										<table class = "tables2" >
											<tr>
												<th style = "text-align:center" >省公司任务列表</th>
											</tr>
											<tr>
												<td>
													<table class = "tables"  id = "proTab" >
													<tr><td colspan = 4 style = 'text-align:right;' ><input type = 'button' class = 'upbutton' value = '全选'  id = 'pSelAll' >
													<input type = 'button' class = 'upbutton' style = 'margin-left:5px;' value = '反选' id = 'pUnselAll' ></td></tr>
													</table>
													<table class = "tables"  >
														<tr>
															<td><input type = "checkbox" id = "dataSyn" name = "pTasks" value = "B" />数据同步</td>
															<td><input type = "checkbox" id = "docIndex" name = "pTasks" value = "F" />创建索引</td>
														</tr>
														<tr>
															<td><input type = "checkbox" id = "fileReceive" name = "pTasks" value = "I" />文件接收(来自博微)</td>
															<td><input type = "checkbox" id = "docImg" name = "pTasks" value = "H" />生成缩略图</td>
														</tr>
														<tr>
															<td><input type = "checkbox" id = "uploadIndex" name = "pTasks" value = "E" />上传检索文件</td>
															<td><input type = "checkbox" id = "suggestion" name = "pTasks" value = "J" />检索提示信息</td>
														</tr>
														<tr>
															<td><input type = "checkbox" id = "backup" name = "pTasks" value = "K" />日志备份</td>
															<td><input type = "checkbox" id = "logDelete" name = "pTasks" value = "L" />日志删除</td>
														</tr>
														<tr>
															<td><input type = "checkbox" id = "uploadServer" name = "pTasks" value = "C" />上传服务器配置信息</td>
															<td><input type = "checkbox" id = "dataCount" name = "pTasks" value = "M" />跨域统计</td>
														</tr>
														<tr>
															<td colspan = 2 style = "text-align:right;" ><input type = "button" class = "upbutton" value = "全选" id = "psSelAll" ><input type = "button" class = "upbutton" style = "margin-left:5px;" value = "反选" id = "psUnselAll" ></td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td colspan = 2 style = "text-align:center" ><input type = "button" class = "dowbutton" style = "font-size:13; " value = "执行" id = "pExecute" ></td>
											</tr>
										</table>
									</div>
									</c:if> 
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<form action = "executeTaskTask.action" method = "post" id = "myForm" >
						<input type = "hidden" name = "taskLists" id = "taskLists" value = "" />
						<input type = "hidden" name = "serverLists" id = "serverLists" value = "" />
						<input type = "hidden" name = "whoTask" id = "whoTask" value = "" />  
					</form>
					<form action="getProvinceInfoTask.action" method = "post" id = "whoForm" >
					</form>
				</td>
			</tr>
			<tr>
				<td class="contbottom2">
					<div class="bottom2left"></div>
					<div class="bottom2right"></div>
					<div class="bottom2middle"></div>
				</td>
			</tr>
		</table>
	</body>
</html>