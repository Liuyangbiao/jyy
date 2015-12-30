<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="档案管理">
		<link type="text/css" href="../css/jquery.autocomplete.css" rel="stylesheet" />
        <link href="../css/base.css" rel="stylesheet" type="text/css">
        <link href="../css/jquery.css" rel="stylesheet" type="text/css">
        <link href="../css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.jqgrid.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.multiselect.css" />
        <link href="../css/base.css" rel="stylesheet" type="text/css">
        <link href="../css/uploadify.css" rel="stylesheet" type="text/css" />
        <link href="../css/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
        <link href="../css/skin-vista/ui.dynatree.css" rel="stylesheet" type="text/css" />
        <script src="../js/jquery-1.5.min.js" type="text/javascript">
        </script>
        <script src="../js/jquery-ui-1.8.10.custom.min.js" type="text/javascript">
        </script>
    
        <script src="../js/jquery.cookie.js" type="text/javascript">
        </script>
  
        <script src="../js/jquery.form.js" type="text/javascript">
        </script>
		<script type="text/javascript" src="../js/jquery.jclock.js"></script>
  
 <link href="../css/base.css" rel="stylesheet" type="text/css">
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
<style> 
.navPoint { 
COLOR: white; CURSOR: hand; FONT-FAMILY: Webdings; FONT-SIZE: 9pt 
} 
</style> 
<title>档案管理</title>
<script language="JavaScript">
var isexpand=true;
</script>

<script>
		function switchSysBar(bool_var){ 
		
		 //var subWeb=document.frames?document.frames["mainContentID"].document:frm.contentDocument; 
		// var sWidth=subWeb.documentElement.scrollWidth ; 
		// var nw = 0
		if(bool_var)
		{ 
		document.getElementById("img1").src="<%=path%>/images/shousuo_botton.gif";
		document.getElementById("frmTitle").style.display="none" ;
		//nw = parseInt(sWidth)+190;
		} 
		else
		{ 
		document.getElementById("img1").src="<%=path%>/images/shousuo_botton2.gif";
		document.getElementById("frmTitle").style.display="" ;
		// nw = parseInt(sWidth)-190;
		} 
		isexpand=!isexpand;	
		//jQuery("#rowedtable").jqGrid('setGridWidth',1000);
        //try
       // {
		//document.frames['mainContentID'].window.ReSizeTab()
	    //}
	   // catch(e)
	   // {
	     
	   // }
		//alert(document.frames['mainContentID']);
		//frm.ReSizeTab();
		} 
</script>

<script type="text/javascript" language="javascript">  
<!--  
function TuneHeight(fm_name,fm_id){  
    var frm=document.getElementById(fm_id);  
    var subWeb=document.frames?document.frames[fm_name].document:frm.contentDocument;  
    if(frm != null && subWeb != null){
     //   frm.style.height = subWeb.documentElement.scrollHeight+"px";

    // frm.style.width = subWeb.documentElement.scrollWidth+"px"; // 自动适应高度
         
    }  
}  
//-->  
</script>



</head>
<body style="overflow:hidden">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
  <tr>
<td width="190" height="100%" align="center" valign="top" noWrap id="frmTitle" name="fmTitle" style=" border-right:#004386 1px solid;">
      <iframe name="I1" height="100%"   id="I1ID" width="190"    src="<%=path%>/system/menuSystemMenu.action" border="0" frameborder="0" scrolling="no"> 浏览器不支持嵌入式框架，或被配置为不显示嵌入式框架。</iframe>
</td>
<td height="100%" style="width:8px;"valign="middle" onclick=switchSysBar(isexpand)>
<SPAN class=navPoint 
id=switchPoint title=关闭/打开左栏><img name="img1" width=8 height=111 id="img1" src="<%=path%>/images/shousuo_botton.gif"  ></SPAN>
</td>
<td   height="100%" width="100%" align="center" valign="top">
<iframe name="mainContent"   height="100%" id="mainContentID" scrolling="auto"   width="100%" border="0" frameborder="0" src="<%=path%>/index.jsp"> 浏览器不支持嵌入式框架，或被配置为不显示嵌入式框架。</iframe>
</td>
  </tr> 
</table>


</body>
</html>