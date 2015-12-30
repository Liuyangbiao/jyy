<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>分类列表配置添加</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="../css/base.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="../js/jquery-1.5.min.js"></script>   
    <script type="text/javascript" src="../js/jquery.form.js"></script> 
    <script type="text/javascript" src="../js/jquery.validate.1.8.js"></script> 
    <script type="text/javascript" src="js/classConfigPop.js"></script> 
    <script type="text/javascript">
     var f="${treeId}";
     var tName="${tTreeDef.name }";
     var pid="${param.id }";
     //关闭窗口前,先让typeName失去焦点 不然关闭时JS有错
		  function  window.onbeforeunload()  { 
			   var ctrl=document.getElementById("remark");      
               ctrl.focus();
                }
          function getFocus(){
        	  document.myForm.remark.focus();
          }
	 </script>
	<style type="text/css">  
			label.error { float: left; color: red; padding-left: .5em; vertical-align: middle; }   
	   </style> 
     
    <style type="text/css">  
         h1{color:Green;} 
         #listLeft{width:100px;}  
         .normal{
	color:#11437f;
	font-weight:bold;
	font-size:14px;
	width: 95px;
	background-color: #e3f1fa;
         }  
		 .conttitle {
	border-top-width: 1px;
	border-bottom-width: 1px;
	border-top-style: dotted;
	border-bottom-style: dotted;
	border-top-color: #11437F;
	border-bottom-color: #11437F;
	margin: 5px 0 5px 0;
}
.conttitle h3{ margin:0px; padding:0px; font-size:12px;}

     </style>  
  </head>
  
  <body onload="getFocus()">
  <form id="myForm" name="myForm" action="<%=path %>/search/saveClassLevelConfig.action" method="post" onsubmit="sub();">
  <table border="0" cellspacing="0" cellpadding="0" class="contert2" style="width:650px;" align="center">
    <tr>
         <td class="conttop2"><div class="top2left"></div>
             <div class="top2right"></div>
           <div class="top2middle"><span class="topico2"><img src="<%=path %>/images/Accordion_ico03.gif"></span>
                 <h3>分类等级配置编辑</h3>
         </div></td>
       </tr>
       <tr>
         <td  class="contmiddle2">
         <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-left:55px;margin-top: 15px;">
            <tr>
	     <td width="530" align="center" valign="top">
	    	<table width="100%" height="320" border="0" cellpadding="0" cellspacing="1" bgcolor="#476C89"  style="font-size:12px;" align="center">
	              <tr>
	                  <td width="30%" align="right" bgcolor="#FFFFFF">树名称:&nbsp;</td>
	                  <td width="70%" bgcolor="#FFFFFF">
	                  	 &nbsp;<input type="text" id="treeName" name="treeName" value="${tTreeDef.name }" onBlur="nameExistence(this.value)"><br/>
                  	     <span style="color: red;" id="existence"></span>
                 	  </td>
	              </tr>
	              <tr>
	                  <td bgcolor="#FFFFFF" align="right"><input type="hidden" id="treeId" name="treeId" value="${tTreeDef.id }"> 图片路径:&nbsp;</td>
	                  <td bgcolor="#FFFFFF">
	                     &nbsp;<select id="imageUrl" name="imageUrl" onChange="imgShow()">
			                <option value="/images/Accordion_ico01.gif">图片1</option>
			                <option value="/images/Accordion_ico02.gif">图片2</option>
			                <option value="/images/Accordion_ico03.gif">图片3</option>
			                <option value="/images/Accordion_ico04.gif">图片4</option>
			                <option value="/images/Accordion_ico05.gif">图片5</option>
	             		</select>
	             		<img alt="图片" id="imgId" src="<%=path %>/images/Accordion_ico01.gif">
	             	  </td>
	              </tr>
              <tr>
                  <td bgcolor="#FFFFFF" align="right"> 说明:&nbsp;</td>
                  <td bgcolor="#FFFFFF">&nbsp;<textarea rows="4" cols="30" id="remark" name="remark">${tTreeDef.remark }</textarea><br/>
                  <input type="hidden" id="menuId" name="menuId"></td>
              </tr>
              <tr style="background-color: #FFFFFF;">
			    <td colspan="2" align="center">
				    <div align="center" style="margin-left: 10px;"> 
				         <div style="width:100px;float:left;">  
				             <select size="10" name="listLeft" id="listLeft" class="normal" title="双击可实现右移">   <!-- multiple -->
				             </select>   
				         </div>  
				         <div style="width:40px;float:left; padding-top:60px;">  
				             <input type="button" id="btnRight" value="→" style="width:25px"/><br />  
				             <input type="button" id="btnLeft" value="←" style="width:25px"/>  
				         </div>  
				         <div style="width:250px;float:left;">  
				             <select size="10" style="width: 250px;"  name="listRight" id="listRight" class="normal" title="双击可实现左移">   
				             </select>  
				         </div>
				         <div style="width:40px;float:left; padding-top:50px;">  
				             <div style="width: 100px;">
					             <input type="button" id="btnUp" value="↑" style="width:25px"/><br/>
					             <input type="button" id="downRight" value="←" style="width:25px"/>
					             <input type="button" id="upRight" value="→" style="width:25px"/><br/>
					             <input type="button" id="btnDown" value="↓" style="width:25px"/>
				             </div>
				         </div> 
				     </div>
			     </td>
			  </tr>
        	</table>    
        	<br>
   		 </td>
    </tr>
</table>     
		<table width="99" border="0" cellspacing="5" cellpadding="0" align="center">
              <tr>
                  <td><input type="submit" value="提交" class="querenbutton"/></td>
                  <td><input type="button" onClick="closeWin()" value="返回" class="qkbutton"/></td>
              </tr>
        </table> 
        </td>
    </tr>
       <tr>
         <td class="contbottom2"><div class="bottom2left"></div>
             <div class="bottom2right"></div>
           <div class="bottom2middle"></div></td>
       </tr>
     </table>
      </form>
</body>
</html>
