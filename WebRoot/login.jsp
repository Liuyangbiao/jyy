<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>电子档案管理系统</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<base href="<%=basePath%>">
		
		<!--
			<link rel="stylesheet" type="text/css" href="styles.css">
	    -->
		<style type="text/css">
			* {
				font-family: Verdana;
				font-size: 98%;
			}
			
			label {
				width: 10em;
			}
			
			label.error {
				float: none;
				color: red;
				padding-left: .5em;
				vertical-align: top;
			}
			
			p {
				clear: both;
			}
			
			.submit {
				margin-left: 12em;
			}
			
			em {
				font-weight: bold;
				padding-right: 1em;
				vertical-align: top;
			}
		</style>
		<script type="text/javascript" src="<%=path %>/js/jquery.js"></script>
		<script type="text/javascript" src="<%=path %>/js/jquery.validate.js"></script>

		<script type="text/javascript">  
	        function ajaxMethod(cid){   
	        	var code = $("#captcha").attr("value");   
	    	    code = "c=" + code;   
	    	    $.ajax({   
	    	        type:"POST",   
	    	        url:"${pageContext.request.contextPath}/system/isRightCodeAppUser.action",   
	    	        data:code,   
	    	        success:callback   
	    	    });   
	    	    
	            document.getElementById(cid).setAttribute("src",null);   
	            //加上随机数的目的是为了每次都改变url 让浏览器不要加载缓存   
	            var svalue = "${pageContext.request.contextPath}/system/checkCodeAppUser.action?id=" + Math.random();      
	            document.getElementById(cid).setAttribute("src",svalue);    
	        }   
	
	        function callback(data){   
	    	    $("#checkCode").attr("value",data);  
	    	} 
	
	        function colorblue(id){   
	            document.getElementById(id).color="blue";   
	        }   
	        function colorblack(id){   
	            document.getElementById(id).color="black";   
	        }
	
	        //生成cookie
	        function readCookie(){
	        	var flag=$("#remember").attr("checked");
	        	var ii=$("#loginId").val();
	        	var login="loginId";
	        	if(flag){
	        		var Days = 30; //此 cookie 将被保存 30 天
	        	    var exp  = new Date();   
	        	    exp.setTime(exp.getTime() + Days*24*60*60*1000);
	        	    document.cookie = login + "="+ escape (ii) + ";expires=" + exp.toGMTString();
	        	}
	        }
	    </script>

		<script type="text/javascript">  
			$(document).ready(function(){
				//解决登录页面嵌入在frame内的情况
				  	if (top.location != location)
					  	top.location.href = location.href;
		
					  //得到用户的cookie
					  var arr = document.cookie.match(new RegExp("(^| )"+"loginId"+"=([^;]*)(;|$)"));
			             if(arr != null){
		                       var loginId=unescape(arr[2]);
				               $("#loginId").attr("value",loginId);
				             }
					  //表单验证
					   $("#loginForm").validate({
						   rules: {
							   loginId: "required",
						   password: {
						       required: true	,
						       minlength: 2	  ,
						       maxlength: 6
						     },
						   captcha: {
						       required: true	//,
						     //  equalTo: "#checkCode"
						     }
						   },
						   messages: {
							   loginId: "请输入用户名!",
						   password: {
						       required: "请输入密码!"  ,
						       minlength  :"密码长度应该大于2"       ,
						       maxlength:"密码长度应该小于6"
						     } ,
					     captcha: {
					       required: "请输入验证码!"  // ,
				     //      equalTo:"验证码不正确"
					     }
						   }
				       });
						   
	        	        var url = "${pageContext.request.contextPath}/system/getStations.action?time="+(new Date()).valueOf();
		            	jQuery.post(url,null,callbackFun,'json'); 
		        	    function callbackFun(data){
							for(var i = 0; i < data.length; i ++){
								var $option = $("<option>");
								$option.attr("value",data[i][1]);
								$option.text(data[i][0]);
								$("#loginType").append($option);
							}
			        	}
					    	//得到产生的验证码
					         var code = $("#captcha").attr("value");   
					        	    code = "c=" + code;   
					        	    $.ajax({   
					        	        type:"POST",   
					        	        url:"${pageContext.request.contextPath}/system/isRightCodeAppUser.action",   
					        	        data:code,   
					        	        success:callback   
					        	    });  
				  });   
		 </script>
		<link href="css/base.css" rel="stylesheet" type="text/css">
	</head>
	<body style="background-image:url(<%=path %>/images/loginbg.gif)"
		style="text-align:center;">

		<div id="main" class="logincont">
			<form id="loginForm"
				action="${pageContext.request.contextPath}/system/loginAppUser.action"
				method="post" onSubmit="return readCookie()">
				<div class="user" style="margin-top: 20px;">
					<ul>
						<li>
							用户名：
					    	<input type="text" name="loginId" id="loginId" size="16" />
						</li>
						<li>


								密&nbsp;&nbsp;&nbsp;码：
							<input type="password" name="password" id="password" size="16" />
						</li>
					<li style = "display:none;" >
				                      站&nbsp;&nbsp;&nbsp;点：
							<select name="loginType" id = "loginType">
								<option value="">--请选择--</option>
							</select>
						</li>
						<li>
							验证码：&nbsp;<span><input type="text" name="captcha" id="captcha"
									size="6" /> </span>
							<img id="checkCodeImage"
								src="${pageContext.request.contextPath}/system/checkCodeAppUser.action"
								style="cursor: pointer"
								onclick="javascript:ajaxMethod('checkCodeImage');" />
							<span style="visibility: hidden;"><input type="text"
									name="checkCode" id="checkCode" value="" size="1"> </span>
							
						</li>
					</ul>
					<div class="saveuser" style="margin-left: 53px;">
						<input type="checkbox" name="remember" id="remember" />
						<label>
							记住用户名
						</label>
						<div style="clear: both;margin-bottom: 40px;"></div>
					</div>
					<table width="100" border="0" cellspacing="5" cellpadding="0">
						<tr>
							<td>
								<input type="submit" value="登  录" class="denglubutton" />
							</td>
							<td>
								<input type="reset" value="重  置" class="denglubutton" />
							</td>
						</tr>
					</table>
					<span style="font-size: 12px; color: red;"> ${message} </span>
				</div>
			</form>
		</div>
	</body>
</html>
