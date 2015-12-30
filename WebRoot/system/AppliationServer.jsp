<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/"; %>
<html>
	<head>
		<title>服务器配置</title>
		<script type="text/javascript" src="../js/jquery-1.5.min.js"></script>
		<link type="text/css" href="../css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet" />
		<link href="../css/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
		<link href="../css/input.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.multiselect.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.multiselect.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="../css/base.css">
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
                $("#tabs").tabs({
                    cookie: {
                        expires: 1
                    }
                });
                var select = $("select");
				for(var i = 0; i < select.length; i++){
					var sel = $(select[i]);
					//获取ID
					var id = sel.attr("id");
					var preSuffix = id.substring(0, id.indexOf('protocol'));
					changeSelect(preSuffix);
				}
                $("#serverTypeHiden").css("display","none");
                var serverType = "${serverType}";
 			   //国家电网公司
 			   if(serverType == 'country'){
 			   		$("#country").attr("checked","checked");
 			   //省公司
 			   }else if (serverType == 'province'){
 			   	  $("#province").attr("checked","checked");
 			   //没有配置信息
 			   }else {
 			 		alert("请设置单位ID");
 			   }
               var setKeyId = "${param.menuId}";
               imgDisplay(setKeyId);
               $("#tabs_2").click(function(){
					window.location.href = "getAllProvinceInfo.action" ;
               });
	          $("#submit").click(function(){
	              if(check('text') < 0 || check('password') < 0 ){
	                  alert("请填写完整的服务器信息!");
	                  return false;
	              }else{
	            	  $("#severForm").ajaxSubmit({
	                      dataType: "json",
	                      type: "post",
	                      contentType: "application/x-www-form-urlencoded;charset=utf-8",
	                      success: backCallUpdate
	                      });
	              }
	            });
	          function check(type){
	              var flag = 0 ;
	        	   $("input[type='"+type+"']").each(function(){
		        	   if(!$(this).attr("disabled"))
		                   if($(this).val() == null || $(this).val() == "" ){
		                      flag--;
		                   }
	               });
	               return flag;
	          }
	       // 更新与插入的回调函数
	          function backCallUpdate(data, status, xhr){
	          	if(data == 'success'){
	          		alert("配置成功！");
	          	}else{
	          		alert("配置失败！");
	          	}
	          }
          });
          function changeSelect(str){
        	var protocol = $("#"+str+"protocol").val();
			if(protocol == 'FTP'){
				$("#"+str+"context").attr("disabled","disabled");
				$("#"+str+"ftpLoginName").attr("disabled","");
				$("#"+str+"ftpPwd").attr("disabled","");
				$("#"+str+"ftpIP").html("FTP的IP");
				$("#"+str+"ftpPort").html("FTP的端口号");
			}else if(protocol == 'HTTP'){
				$("#"+str+"context").attr("disabled","");
				$("#"+str+"ftpLoginName").attr("disabled","disabled");
				$("#"+str+"ftpPwd").attr("disabled","disabled");
				$("#"+str+"ftpIP").html("文件服务器IP");
				$("#"+str+"ftpPort").html("文件服务器端口号");
			}
          }  
          function checkIp(str){
              //将DOM对象转换为jQuery对象
              var $str = $(str);
        	  var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
        	  var reg = $str.attr("value").match(exp);
        	  if(null == reg ){
            	  alert("请填写正确的IP地址!");
        	  }
          }
          function checkPort(str){
              var $str = $(str);
              if( true == isNaN($str.attr("value")) || "" == jQuery.trim($str.attr("value"))){
                  alert("端口号只能是整数,且不能为空!");
              }
          }
        </script>
		<style type="text/css">
			body {
				font-size: 12px;
			}
		</style>
	</head>
	<body>
		<!-- Tabs -->
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
						<h3>服务器配置</h3>
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
										<li>
											<a href="#tabs-1"><c:if test="${requestScope.serverType == 'country'}">国家电网服务器配置</c:if><c:if test = "${serverType=='province'}">省公司服务器配置</c:if></a>
										</li>
										<c:if test="${serverType =='country'}">
											<li>
												<a id = "tabs_2" href="#tabs-2">查看省公司服务器配置</a>
											</li>
										</c:if>
									</ul>
									<div id="tabs-1">
										<form action="saveServiceSetInfo.action" method="post" name="severForm" id="severForm">
										<table>
											<tr>
					<td>
						<table width="750" border="0" cellspacing="0" cellpadding="0" class="conttitle">
							<tr>
								<td height="22"><h3>单位信息</h3></td>
							</tr>
						</table>
						<table border="1" class="biaoge" style="font-size: 12px;" cellspacing="1" width="100%" height="%">
							<tr>
								<td width = "32%" >单位编码</td>
								<td>
									<div style="float: left;">
										<input type="text" name="unitCode" id="unitCode" value="${infos[0].id }" readonly>
									</div>
								</td>
							</tr>
							<tr>
								<td>单位名称</td>
								<td>
									<div style="float: left;">
										<input type="text" name="unitName" id="unitName" value="${infos[0].name }">
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr id="serverTypeHiden">
					<td align="left">
						<input type="radio" name="serverType" id="country" value="country">国家电网
						<input type="radio" name="serverType" id="province" value="province" />省公司
					</td>
				</tr>
				<tr>
					<td>
						<table width="750" border="0" cellspacing="0" cellpadding="0" class="conttitle">
							<tr>
								<td height="22"><h3>网段</h3></td>
							</tr>
						</table>
						<table border="1" class="biaoge" style="font-size: 12px;" cellspacing="1"
							width="100%" height="%">
							<c:forEach items="${infos }" var="ssb" varStatus="sub">
								<tr>
									<td width = "14%" >${ssb.name }</td>
									<td width = "18%" >开始地址</td>
									<td width = "24%" >
										<div style="float: left;">
											<input type="text" name="allInfos[${sub.index}].startNetWay" id="${ssb.id}startNetWay" value="${ssb.startNetWay }" size=16>
											<input type="hidden" name="allInfos[${sub.index}].id" id="${ssb.id}id" value="${ssb.id }">
											<input type="hidden" name="allInfos[${sub.index}].name" id="${ssb.id}name" value="${ssb.name }">
										</div>
									</td>
									<td width = "20%">结束地址</td>
									<td width = "24%" >
										<div style="float: left;">
											<input type="text" name="allInfos[${sub.index}].endNetWay" id="${ssb.id}endNetWay" value="${ssb.endNetWay }" size=16>
										</div>
									</td>
								</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
				<c:if test="${requestScope.serverType == 'country'}">
					<tr>
						<td>
							<table width="750" border="0" cellspacing="0" cellpadding="0" class="conttitle">
								<tr><td height="22"><h3>四个区Webservers配置</h3></td></tr>
							</table>
							<table border="1" class="biaoge" style="font-size: 12px;"
								cellspacing="1" width="100%" height="%">
								<c:forEach items="${infos }" var="ssb" varStatus="sub">
									<tr>
										<td width = "32%" >${ssb.name }</td>
										<td><input type = "text" name = "allInfos[${sub.index}].wsUrl" id = "${ssb.id}wsUrl" value = "${ssb.wsUrl }" size=80/> </td>
									</tr>
								</c:forEach>
							</table>
						</td>
					</tr>
				</c:if>
				<tr>
					<td>
						<table width="750" border="0" cellspacing="0" cellpadding="0" class="conttitle">
							<tr>
								<td height="22">
									<h3>
										文件服务器配置
									</h3>
								</td>
							</tr>
						</table>
						<table border="1"  class="biaoge" style="font-size: 12px;" cellspacing="1"
							width="100%" height="%">
							<c:forEach items="${infos }" var="ssb" varStatus="sub">
								<tr>
									<td rowspan="3" width = "14%" >${ssb.name }</td>
									<td width = "18%">文件服务器的协议</td>
									<td width = "24%" >
										<div style="float: left;">
											<select name = "allInfos[${sub.index}].protocol" id = "${ssb.id}protocol" onchange = "javascript:changeSelect('${ssb.id }');" >
												<option value = "FTP" <c:if test="${ssb.protocol == 'FTP'}">selected</c:if>>FTP</option>
												<option value ="HTTP" <c:if test="${ssb.protocol == 'HTTP'}">selected</c:if>>HTTP</option>
											</select>
										</div>
									</td>
									<td width = "20%" id="conFSContext" >文件服务器的上下文</td>
									<td width ="24%" >
										<div style="float: left;">
											<input type="text" name="allInfos[${sub.index}].context" id="${ssb.id}context" value="${ssb.context}" size=16>
										</div>
									</td>
								</tr>
								<tr>
									<td id="${ssb.id }ftpIP">FTP的IP</td>
									<td>
										<div style="float: left;">
											<input type="text" name="allInfos[${sub.index}].ftpIP" id="${ssb.id}ftpIP" value="${ssb.ftpIP }" onblur = "checkIp(this)" size=16>
										</div>
									</td>
									<td id="${ssb.id }ftpPort">FTP的端口号</td>
									<td>
										<div style="float: left;">
											<input type="text" name="allInfos[${sub.index}].ftpPort" id="${ssb.id}ftpPort" value="${ssb.ftpPort }" onblur = "checkPort(this)"  size=16>
										</div>
									</td>
								</tr>
								<tr>
									<td>FTP的用户名</td>
									<td>
										<div style="float: left;">
											<input type="text" name="allInfos[${sub.index}].ftpLoginName" id="${ssb.id}ftpLoginName" value="${ssb.ftpLoginName }" size=16>
										</div>
									</td>
									<td>FTP的密码</td>
									<td>
										<div style="float: left;">
											<input type="password" name="allInfos[${sub.index}].ftpPwd" id="${ssb.id}ftpPwd" value="${ssb.ftpPwd }" size=16>
										</div>
									</td>
								</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table border="0" width="54%">
							<tr>
								<td></td><td>&nbsp;</td>
							</tr>
							<tr>
								<td align="center"><input type="button" value="提交 " id="submit" class="querenbutton"></td>
								<td align="left"><input type="reset" id="reset" value="重置" class="querenbutton"></td>
							</tr>
						</table>
					</td>
				</tr>
										</table>
										</form>
									</div>
									<c:if test="${requestScope.serverType == 'country'}">
									<div id="tabs-2">
										<table id = "provinceInfos" border="1" class="biaoge" style="font-size: 12px;margin-top:10px;" cellspacing="1" width="100%" height="%">
											<tr><td>省公司</td><td>协议</td><td>FTP端口号</td><td>FTP地址</td><td>上下文</td>
											<td>用户名</td><td>密码</td><td>开始地址</td><td>结束地址</td></tr>
											<c:forEach   items="${returnList}" var='obj'>
												<tr>
													<td rowspan = 2>${obj.name }</td>
													<td>${obj.protocol }</td>
													<td>${obj.ftpPort }</td>
													<td>${obj.ftpIP }</td>
													<td>${obj.context }</td>
													<td>${obj.ftpLoginName }</td>
													<td>${obj.ftpPwd }</td>
													<td>${obj.startNetWay }</td>
													<td>${obj.endNetWay }</td>
												</tr>
												<tr>
													<td colspan = 2>WebService地址</td>
													<td colspan = 6>${obj.wsUrl }</td>
												</tr>
											</c:forEach>
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
				<td class="contbottom2">
					<div class="bottom2left"></div>
					<div class="bottom2right"></div>
					<div class="bottom2middle"></div>
				</td>
			</tr>
		</table>
	</body>
</html>