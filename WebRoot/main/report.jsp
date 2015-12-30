<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>${param.title}</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	 <script type="text/javascript" src="../js/jquery-1.5.min.js" ></script>
	<script type="text/javascript">
		function onload(){
			var id="${param.sort}";
			if(id=="1"){
				parent.printCallBack($("#rTitle"));
			}else if(id=="2"){
				parent.printCallBack2($("#rTitle"));
			}
			
		}

		function addRow(tr){
			   var newtable = $("#roletable");
			   $(newtable).append($(tr));
		}

		function setHeader(tds){
			var newHeader = $("#roletable tr:eq(0)");
			   $(newHeader).append($(tds));
		}
	</script>
	
	<style type="text/css" media="print">
		    .biaoge {
				background-color: #476c89;
			}
			.biaoge td {
				padding: 5px;
				text-align:center;
				background-color: #48acff;
			}
			
			
			.biaoge .bgtr01 {
				background-image: url(<%= path%>/images/bctj_botton.gif);
				background-repeat: repeat-x;
				background-position: 50% 50%;
				background-color: #48acff;
				font-weight: bold;
				color: #476c89;
				height: 20px;
			}		
			
	  </style>
  </head>
    
  <body onload="onload()">
  	<div id="rTitle" align="center" style="blur">日志报表</div><br/>
    <table border="1" id="roletable" width="100%" class="biaoge"  bordercolor="#E0E0E0" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
    </table>
  </body>
</html>
