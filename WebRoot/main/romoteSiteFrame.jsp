<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String url=(String)request.getAttribute("url");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>电子档案管理系统</title>
		<style type="text/css">
			<!--
			body {
	    		margin-left: 0px;
				margin-top: 0px;
				margin-right: 0px;
				margin-bottom: 0px;
				height:100%;	
			}
			html{
				height:100%;
			}
			-->
		</style>
	</head>
	<body style="overflow:hidden">
		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
  			<tr>
				<td  height="100%" width="100%" align="center" valign="top">
					<iframe name="mainContent"   height="100%" id="mainContentID" scrolling="auto"   width="100%" border="0" frameborder="0" src="<%=url %>"> 浏览器不支持嵌入式框架，或被配置为不显示嵌入式框架。</iframe>
				</td>
  			</tr> 
		</table>
	</body>
</html>
