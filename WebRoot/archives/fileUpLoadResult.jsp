<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'fileUpLoadResult.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="../css/base.css">
	<link rel="stylesheet" type="text/css" href="../css/themes/jquery-ui-1.8.11.custom.css" />
    <script type="text/javascript">
         function goBack(name){
             if("档案更新"==name){
                window.location.href="fileUpdate.jsp?menuId=13"; 
             }else if("档案录入"==name){
            	 window.location.href="initTreeUpLoad.action"; 
             }
         }
    </script>

  </head>
  
  <body>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2">
          <tr>
            <td class="conttop2"><div class="top2left"></div>
                <div class="top2right"></div>
	            <div class="top2middle">
	            	<span class="bottonszkj" style="cursor: hand;" onclick="goBack('${pageTitle }');"><img src="../images/fanhui_botton.gif"></span>
	             	<span class="topico2"><img src="../images/Accordion_ico03.gif"></span>
	                <h3> ${pageTitle }</h3>
	            </div>
	        </td>
          </tr>
          <tr>
            <td valign="top" class="contmiddle2">
			      <br/>
			             <table>
			                <tr><td align="center" width="760"><h2> ${message }</h2></td></tr>
			             </table>
			                <br/><br/>
			           <table id="addRow" width="760" border="0" cellspacing="1" cellpadding="0" class="biaoge" >
						  <tr class="bgtr01" align="center" height="40">
						    <td>工程名称</td>
						    <td>文档分类</td>
						    <td>文件名称</td>
						    <td width="60">文件格式</td>
						    <td width="60">文件后缀</td>
						    <td width="60">文件大小</td>
						  </tr>
						  
						  <s:iterator value="listResult">
						  	<s:debug></s:debug>
						      <tr align="center">
						      	<td><s:property value="projectName" /></td>
						      	<td><s:property value="docTypeName" /></td>
					      		<td><s:property value="name" /></td>
				      			<td><s:property value="format" /></td>
			    				<td><s:property value="suffix" /></td>
			    				<td><s:property value="fileSize" /></td>
						      </tr>
						 </s:iterator>
					</table>
					  <br/>
			</td>
       </tr>
       <tr>
            <td class="contbottom2"><div class="bottom2left"></div>
                <div class="bottom2right"></div>
                <div class="bottom2middle"><span class="bottonszkj">
                    <span class="bottonszkj" style="cursor: hand;" onclick="goBack('${pageTitle }');"><img src="../images/fanhui_botton.gif"></span>
                </span></div>
            </td>
       </tr>
    </table>
  </body>
</html>
