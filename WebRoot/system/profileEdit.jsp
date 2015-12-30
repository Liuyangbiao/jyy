<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'profileEdit.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="../css/base.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="../js/jquery-1.5.min.js"></script>   
    <script type="text/javascript" src="../js/jquery.form.js"></script> 
    <script type="text/javascript" src="../js/jquery.validate.1.8.js"></script> 

    <script type="text/javascript">
          $(document).ready(function() { 
 	    	 $.ajaxSetup({ cache:false });
 	    	    var options = { 
 	    	        dataType:  'json',
 	    	        type: 'post',
 	    	        url: "saveProfileManager.action?time="+Math.random(),
 	    	        success:   processJson 
 	    	    	 };
 	    	    	 $('#myForm').ajaxForm(options); 
 	    	});
 	     function processJson(data) { 
 	    	    alert("保存成功!"); 
 	    	    //给父窗口返回参数,如果接到参数为1,则刷新父窗口
 	    	    window.returnValue="1";
 	    	    winClose();
 	    	}
	    	
 	     function winClose(){
            window.close();
         }
    </script>
  </head>
  
  <body>
     <center>
         <h2>配置文件操作</h2>
            <form action="saveProfileManager.action" method="post" id="myForm" name="myForm">
            <input type="hidden" id="namespace" name="namespace" value="${param.name }">
            <table>
               <tr>
                   <td>属性键</td>
                   <td><input type="text" name="key" id="key" value="${param.key }"/></td>
               </tr>
                <tr>
                   <td>属性值</td>
                   <td><input type="text" name="value" id="value" value="${param.value }"/></td>
               </tr>
                <tr>
                   <td>注释</td>
                   <td><input type="text" name="notes" id="notes" value=""/></td>
               </tr>
               <tr>
                  <td><input type="submit" value="保存"/></td>
                  <td><button onclick="winClose();">取消</button></td>
               </tr>
            </table>
            </form>
         <br>
     </center>
  </body>
</html>
