<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.nsc.dem.bean.profile.TUser"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>电子档案管理系统</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		
	</head>

	<frameset id=frameset name=framesetpop rows="130,*,30" frameborder="no"
		border="0" framespacing="0">
		<frame src="system/initTopShortcut.action" name="topFrame"
			frameborder="no" scrolling="NO" noresize>
		<frame src="main/middel.jsp" name="mainFrame" id="mainFrame">	
		<frame src="main/bottomFrame.htm" name="bottomFrame" scrolling="NO"
			noresize>
	</frameset>
	<body>
	</body>
</html>
