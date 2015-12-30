<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'classPreview.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" href="../css/themes/jquery.ui.all.css" type="text/css">
	
	<script src="../js/jquery-1.4.4.js"></script>
	<script src="../js/jquery.ui.core.js"></script>
	<script src="../js/jquery.ui.widget.js"></script>
	<script src="../js/jquery.ui.mouse.js"></script>
	<script src="../js/jquery.ui.draggable.js"></script>
	<script src="../js/jquery.ui.position.js"></script>
	<script src="../js/jquery.ui.resizable.js"></script>
	<script src="../js/jquery.ui.dialog.js"></script>
	<link rel="stylesheet" href="../css/demos.css" type="text/css">
     <script>
	$(function() {
		$( "#dialog-modal" ).dialog({
			height: 140,
			width:300,
			modal: true
		});
	});

	function test(){
      $("#dialog-modal").dialog("close");
	}
	</script>
  </head>
  
  <body>
    <div id="dialog-modal" title="Basic modal dialog">
		<p>Adding the modal overlay screen makes the dialog look more prominent because it dims out the page content.</p>
	              <button onclick="test()">test</button>
     </div>
  </body>
</html>
