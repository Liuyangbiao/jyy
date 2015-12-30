<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>My JSP 'userInfo.jsp' starting page</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<link type="text/css" href="<%=path %>/css/jquery.autocomplete.css"
			rel="stylesheet" />
		<link href="<%=path %>/css/base.css" rel="stylesheet" type="text/css">
		<link href="<%=path %>/css/jquery.css" rel="stylesheet"
			type="text/css">
		<link href="<%=path %>/css/themes/jquery-ui-1.8.11.custom.css"
			rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" media="screen"
			href="<%=path %>/css/themes/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen"
			href="<%=path %>/css/themes/ui.multiselect.css" />
		<link href="<%=path %>/css/base.css" rel="stylesheet" type="text/css">
		<script src="<%=path %>/js/jquery-1.5.min.js" type="text/javascript">
        </script>
		<script src="<%=path %>/js/jquery-ui-1.8.10.custom.min.js"
			type="text/javascript">
        </script>
		<script src="<%=path %>/js/jquery.cookie.js" type="text/javascript">
        </script>
		<script src="<%=path %>/js/jquery.form.js" type="text/javascript">
        </script>
		<link href="<%=path %>/css/base.css" rel="stylesheet" type="text/css">
		<script type="text/javascript">
		
		$(function(){
			 $.ajaxSetup({
			        cache: false
			    });
			});
			function checkAllPassword(){
				var checkPass=checkPassword();
				var checktele = checkTelePhone();
				var checkzhiwu = checkDuty();
				var newPassword = checkRepassword();
				var newAgainPassword = checkAgainrepassword();
				var password=$("#yuanpassword").val();
				
				if(checkPass && checktele && newPassword && newAgainPassword && checkzhiwu){
					$("#updateUserForm").ajaxSubmit({
	                       dataType: "json",
	                       beforeSubmit: function(){
	                       },
	                       type: "post",
	                       contentType: "application/x-www-form-urlencoded;charset=utf-8",
	                       success: backCallUpdate
	                 });	
	             }
			}
	
			function backCallUpdate(data, status, xhr){
				  if (data == "true"){
				      alert("保存成功 ");
				      changeurl();
				  }
				  else {
				      alert("保存失败");
				  }
				}
			function changeurl(){
				parent.parent.window.topFrame.goHome();
				}
			function checkAgainrepassword(){
				var newPass = $("#repassword").val();
				var againPass = $("#againrepassword").val();
			    if (jQuery.trim($("#againrepassword").val()) != "") {
			        if(newPass!=againPass){
			        	$("#twoPassword").css("display","block");
			        	$("#againrepassworderror").css("display", "none");
			        	$("#repassword").focus();
			        	 return false;
			        }
			        $("#againrepassworderror").css("display", "none");
			        	$("#twoPassword").css("display","none");
			        	return true;
			    }
			    else {
			        $("#againrepassworderror").css("display", "block");
			        $("#twoPassword").css("display","none");
			        return false;
			    }
			}
			
			function checkPassword(){
			    var password = $("#yuanpassword").val();
			    var reg = /^[0-9]{6}$/;
				   if (reg.exec(password) && jQuery.trim(password) != ""){
				     if ($.getJSON("../system/checkPasswordUserInfoManagerAction.action", {
                    	password: password
                    }, function(json){
                        if (json == 'true') {
                         	$("#yuanpassworderrorvalue").css("display","none");
                        }
                        else if (json == 'false') {
                        	$("#yuanpassworderrorvalue").css("display","block");
                        	$("#yuanpassworderror").css("display","none");
                         }
                    })) {
				    	 	$("#yuanpassworderror").css("display","none");
				    		$("#yuanpassworderrorvalue").css("display","none");
                        return true;
                      }
                    else {
                        return false;
                    }
                }
                else {
                	$("#yuanpassworderror").css("display","block");
                    return false;
                }
			}
			
			function checkRepassword(){
				  var reg = /^[0-9]{6}$/;
				  var password =$("#repassword").val(); 
			    if (jQuery.trim(password) != "" && reg.exec(password)) {
			       $("#repassworderror").css("display", "none");
			        return true;
			    }
			    else {
			        $("#repassworderror").css("display", "block");
			        return false;
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
	           
		   function checkTelePhone(){
			   //alert("telephone");
	               var phone = $("#telephone").val();
	               if (phone.length != 0) {
	                   //var p2 =/^13\d$/gi;
	                   //|| p2.test(phone)
	                   var ph= /13\d{9}\b|15[0689]\d{8}\b|18[0156789]\d{8}\b|010[- ]?[1-9]\d{7}\b|02\d[- ]?[1-9]\d{7}\b|0[3-9]\d{2}[- ]?[1-9]\d{6,7}\b/;
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
	
	           
		   function checkDuty(){
			  // alert("duty");
	               if (jQuery.trim($("#duty").val()) != "") {
	                   $("#dutyerror").css("display", "none");
	                   return true;
	               }
	               else {
	                   $("#dutyerror").css("display", "block");
	                   return false;
	               }
	       }
		</script>
	</head>

	<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="contert2" height="100%">
			<tr>
				<td class="conttop2">
					<div class="top2left">
					</div>
					<div class="top2right">
					</div>
					<div class="top2middle">
						<span class="topico2"><img
								src="../images/Accordion_ico03.gif"> </span>
						<h3>
							修改基本信息
						</h3>
					</div>
				</td>
			</tr>
			<tr>
				<td class="contmiddle2" width="%">
				 <center>
				   <div id="updateUserInfo" title="用户信息">
						<form id="updateUserForm"
							action="<%=path %>/system/updatePasswordUserInfoManagerAction.action"
							method="post" name="updateUserForm">
							<table width="50%" height="%" border="0" cellpadding="0" cellspacing="1" id="roletable" style="text-align:center; font-size:12px;" class="biaoge">
								<tr >
									<td  align="right" width="15%">
										登录名:&nbsp;&nbsp;
									</td>
									<td align="left" >
										<span id="insertLoginId" >${USER_KEY.loginId }</span>
										<input type="hidden" value="${USER_KEY.loginId }" size="15"
											readonly="readonly" name="tuser.loginId" id="insertLoginId" >
									</td>
								</tr>
								<tr align="center">
									<td align="right">
										用户名称:&nbsp;&nbsp;
									</td>
									<td  align="left">
										<div style="float:left;"><input type="text" value="${USER_KEY.name }" size="15"
											name="name" id="insertUserName" onblur="checkUserName()"></div>
										<div id="insertUserNameerror" style="display: none; color: red; fontSize: 11px;">请输入用户名</div></td>
								</tr>
								<tr>
									<td  align="right">
										原始密码:&nbsp;&nbsp;
									</td>
									<td  align="left">
										<div style="float:left;"><input type="password"
											size="15" name="tuser.password"
											id="yuanpassword" onblur="checkPassword()"></div><div id="yuanpassworderrorvalue"  style="float:left;display: none; color: red; fontSize: 11px;">原来的密码有误！请重新输入!</div><div id="yuanpassworderror"
											style="display: none; color: red; fontSize: 11px;">请输入密码，且只能是6位数字</div></td>
								</tr>
								<tr >
									<td  align="right">
										新密码:&nbsp;&nbsp;
									</td>
									<td  align="left">
									<div style="float:left;">
										<input type="password" size="15" name="repassword"
											id="repassword" onblur="checkRepassword()">
											</div><div id="repassworderror" style="display: none; color: red; fontSize: 11px;">请新输入密码，且只能是6位数字</div></td>
								</tr>
								<tr >
									<td  align="right">
										确认密码:&nbsp;&nbsp;
									</td>
									<td align="left">
									<div style="float:left;">
										<input type="password" size="15" name="repassword2"
											id="againrepassword" onblur="checkAgainrepassword()"></div>
											<div id="againrepassworderror" style="display: none; color: red; fontSize: 11px;float:left;">请输入确认密码</div><div id="twoPassword" style="display: none; color: red;">两次密码输入不一致！请重新输入！</div></td>
								</tr>
								<tr>
									<td align="right">
										联系电话:&nbsp;&nbsp;
									</td>
									<td align="left">
										<div style="float:left;"><input type="text" value="${USER_KEY.telephone }" size="15"
											name="telephone" id="telephone" onblur="checkTelePhone()"></div><div id="telephoneerror"	style="display: none; color: red; fontSize: 11px;">请输入正确的电话号码或手机</div></td>
								</tr>
								<tr>
									<td align="right">
										职务:&nbsp;&nbsp;
									</td>
									<td  align="left">
										<div style="float:left;"><input type="text" value="${USER_KEY.duty }" size="15"
											name="tuser.duty" id="duty" onblur="checkDuty()" /></div><div id="dutyerror"	style="display: none; color: red; fontSize: 11px;">请输入职务</div>
									</td>
								</tr>
								<tr  >
									<td align="center" colspan="2">
										<input type="button" value="保存" class="querenbutton"
											onclick="checkAllPassword()" />
									
										<input type="reset" value="重置" class="querenbutton" />
									</td>
								</tr>
							</table>


						</form>
					</div>
					</center>
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
