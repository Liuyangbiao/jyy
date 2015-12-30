<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			
	String msg=(String)request.getAttribute("errMsg");
	String uMsg=(String)request.getAttribute("userMsg");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My JSP '500.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link href="css/base.css" rel="stylesheet" type="text/css">

	</head>

	<body>	
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2">
        <tr>
          <td class="conttop2"><div class="top2left"></div>
              <div class="top2right"></div>
              <div class="top2middle">
               	  <span class="topico2"><img src="images/404.png" height="22"></span>
                  <h3>错误画面</h3>
            </div></td>
        </tr>
        <tr>
          <td class="contmiddle2">
          	<img src="images/500.png">
          	<%=msg==null?"":msg%>
          	<%=uMsg==null?"":uMsg%>
          </td>
        </tr>
        <tr>
          <td class="contbottom2"><div class="bottom2left"></div>
              <div class="bottom2right"></div>
            <div class="bottom2middle"></div></td>
        </tr>
      </table>
	</body>
</html>
