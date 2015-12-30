<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 

<html>
<head>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
<title>Insert title here</title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	overflow:hidden;
}
-->
</style>
<script type="text/javascript" language="javascript">  

function SetIframeSize(iframeName) { 
var iframe = document.getElementById(iframeName); 
 
try { 
var bHeight = parent.document.body.scrollHeight; 
var dHeight = parent.document.documentElement.scrollHeight; 
//据说这两个高度可能不一样 
var height = Math.max(bHeight, dHeight); 
alert(height);
iframe.height =height; 
}catch (ex){
} 
} 


<!--  
function TuneHeight(fm_id){  
alert(1);
    var frm=document.getElementById(fm_id);  
    alert(frm);
    alert(frm.style.height);
    alert(parent.document.body.scrollHeight);
 
        frm.style.height = parent.document.body.scrollHeight+"px";
        //alert(subWeb.documentElement.scrollHeight);
      
     //frm.style.width = subWeb.documentElement.scrollWidth+"px"; // 自动适应高度
    }  
  
 -->  
</script>
</head>
<body>
<table width="100%" style="margin:0;"  border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="500" id="tabid">
 
    
    <iframe name="I2ID" id="I2ID" marginwidth="0"marginheight="0"hspace="0"vspace="0" scrolling="no" height="600"  width="100%" border="0" frameborder="0" src="middel.jsp"> 浏览器不支持嵌入式框架，或被配置为不显示嵌入式框架。</iframe></td>
     <td width="6" bgcolor="#E6EAED" style=" width:6px;">&nbsp;</td>
  </tr>
</table>
</body>
</html>