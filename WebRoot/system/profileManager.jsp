<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'profileManager.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="../css/themes/jquery-ui-1.8.11.custom.css" />
    <link rel="stylesheet" type="text/css" href="../css/base.css" >
    
    <script type="text/javascript" src="../js/jquery-1.5.min.js" ></script>
	<script type="text/javascript" src="../js/jquery.cookie.js" ></script>
	<script type="text/javascript" src="../js/jquery-ui-1.8.10.custom.min.js"></script>
	<script type="text/javascript" src="../js/jquery.form.js"></script> 
    <script type="text/javascript" src="../js/jquery.validate.1.8.js"></script> 
	<script type="text/javascript" src="<%=path %>/js/popShortcut.js"></script>
	<script type="text/javascript">
		$(function() {
			var mId="${param.menuId}";
			imgDisplay(mId);
		});

		$(function() {
			//Tab表格
			$( "#tabs" ).tabs({
				cookie: {
					// store cookie for a day, without, it would be a session cookie
					expires: 1
				}
			});
		});
        var chtr;
        function queryByKey(id){
             var pkey=$("#"+id+"_pkey").val();
             if(pkey==""){
                 alert("请输入查询条件!");
             }else{
             	 
	             $("#"+id+"_tab").find("tr:not(:eq(0))").each(function(){
	                 var row = $(this);
	                 row.css("color","");
	                 var tdValue= $(this).find("td:eq(0)").html();
	                 if(tdValue.indexOf(pkey)>-1){
	                     location.href = "#"+tdValue;
	                	 row.css("color","red");
	                	 chtr=row;
	                 }
	                 
	             })
             }
           return false;
		}
		
		function addWin(key,name,value){
		   $("#namespace").attr("value" ,name);
		   $("#key").attr("value" ,key);
		   $("#value").attr("value" ,value);
		   $("#dialog-modal").dialog("moveToTop");
			$( "#dialog-modal" ).dialog({
				height:380,
				width:350,
				modal: true
			  });	
			$("#dialog-modal").dialog("moveToTop");
			$("#notes").focus();		
		}
	</script>
	
	 <script type="text/javascript">
          $(document).ready(function() { 
 	    	 $.ajaxSetup({ cache:false });
 	    	    var options = { 
 	    	        dataType:  'json',
 	    	        type: 'post',
 	    	        url: "saveProfileManager.action?time="+Math.random(),
 	    	        success:   processJson 
 	    	    	 };
 	    	    	 $('#myForm2').ajaxForm(options); 
 	    	});
 	     function processJson(data) { 
 	    	    alert("保存成功!"); 
 	    	    //给父窗口返回参数,如果接到参数为1,则刷新父窗口
 	    	    window.location.reload();
 	    	    winClose();
 	    	}
	    	
 	     function winClose(){
 	    	$("#dialog-modal").dialog("close");
         }
    </script>
  </head>
  
  <body>
    <!-- Tabs -->
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2">
		  <tr>
		    <td class="conttop2"><div class="top2left"></div>
		      <div class="top2right"></div>
		      <div class="top2middle">
		        <span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif" onclick="topwin(${param.menuId })" style="cursor:hand"></a></span>
		        <span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span>
		        <span class="topico2"><img src="../images/Accordion_ico03.gif"></span>
		        <h3>配置文件管理</h3>
		      </div></td>
		  </tr>
		  <tr>
		    <td class="contmiddle2">
		     <table width="750" border="0" cellspacing="0" cellpadding="0">
		     <tr>
              <td>
		      <div id="tabs" align="center">
		        <ul>
			      <s:iterator value="codeList">
			      	 <li><a href='#<s:property value="key" />'><s:property value="key" /></a></li>
			      </s:iterator>
		        </ul>
		      <s:iterator value="codeList" id="it">
		         
		       <div id="<s:property value="key" />">
		          <form action="#" method="post" id="myForm" name="myForm" onsubmit="return queryByKey('<s:property value="key" />')">
		            <table width="550">
		              <tr>
		                <td width="250" height="32" align="center"><strong style="font-size:14px;">属性键:</strong>
		                  <input type="text" name="<s:property value="key" />_pkey" id="<s:property value="key" />_pkey"/>
						</td>
		                <td width="90" align="left"><button onClick="queryByKey('<s:property value="key" />')" class="chaxunbutton"><span style="font-size:14px;">查询</span></button>    </td>
		                <td width="90" align="left"><button onClick="addWin('','<s:property value="key" />','')" class="chaxunbutton"><span style="font-size:14px;">增加</span></button>     </td>
		              </tr>
		            </table>
		            <div class="bianxian"></div>
		          </form>
			          <div>
			            <table id="<s:property value="key" />_tab" width="700" border="0" cellspacing="1" cellpadding="0" class="biaoge" >
			               <tr class="bgtr01" align="center">
						    <td width="50">属性键</td>
						    <td>属性值</td>
						    <td width="100">操作</td>
						  </tr>
						  
			               <s:iterator var="test" value="#it.value">
			                  <tr id="<s:property value="key"/>"  align="center">
			                     <td width="50"><s:property value="key"/></td>
			                     <td><s:property value="value"/></td>
			                     <td width="100">
			                       <a href="#" onclick="addWin('<s:property value="key"/>','<s:property value="#it.key" />','<s:property value="value"/>');">修改</a>
			                     </td>
			                  </tr>
			               </s:iterator>
			             </table>
			          </div>
		         </div>
		        </s:iterator>
		      </div></td>
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
		<!-- 弹出窗口 -->
		<div id="dialog-modal" title="配置文件操作" style="display: none;">
		  <center>
		    <form action="#" method="post" id="myForm2" name="myForm2">
	            <input type="hidden" id="namespace" name="namespace">
	            <table width="80%" border="0" align="center" cellpadding="0" cellspacing="1" id="addRow" class="biaoge">
	               <tr>
	                   <td>属性键</td>
	                   <td><input type="text" name="key" id="key" size="30"/></td>
	               </tr>
	                <tr>
	                   <td>属性值</td>
	                   <td><input type="text" name="value" id="value" size="30"/></td>
	               </tr>
	                <tr>
	                   <td>注释</td>
	                   <td><input type="text" name="notes" id="notes" size="30" value=""/></td>
	               </tr>
	            </table>
	            <br/>
	            <input type="submit" id="subtn" value="保存" class="querenbutton"/>&nbsp;&nbsp;
	            <button id="cancel" onclick="winClose();" class="qkbutton">取消</button>
            </form>
           </center>
		</div>
  </body>
</html>
