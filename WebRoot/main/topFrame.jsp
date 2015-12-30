<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<html>
  <head>
    
    <title>My JSP 'topFrame.jsp' starting page</title>
   
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
		<script type="text/javascript" src="<%=path %>/js/jquery.jclock.js"></script>
  
 		<link href="../css/base.css" rel="stylesheet" type="text/css">
 
	<script type="text/javascript">
	
	    $(function(){
	  	  $.ajaxSetup({
	            cache: false
	        });
	         $('#nowTime').jclock({withDate:true, withWeek:true});
	         $('#help').click(function(){ 
	              //alert("dd");
	             parent.window.mainFrame.window.frames["mainContent"].location.href="<%=path %>/help/index.htm"
	            	  });
	         /*$('#logout').click(function(){ 
	          window.parent.location.href="../system/logoutAppUser.action";
	            });*/
	        });
	
	    //回到首页
	    /*function goHomePage(){
	  	     parent.window.mainFrame.window.frames["mainContent"].location="../search/doctypeMenu.action";
	    }*/
	  
          function goHome(){
			//parent.window.mainFrame.window.frames["mainContent"].location="../search/doctypeMenu.action?menuId=7";
        	  parent.window.mainFrame.window.frames["mainContent"].location="<%=path%>/index.jsp";
              }
	    function changePass(){
	    	parent.window.mainFrame.window.frames["mainContent"].location="../main/userInfo.jsp";
	    }
          
    </script>
  </head>
  <body>
  
<!--头部开始-->
<div class="header">


  <div class="headerbg">
  <div class="headerwidth">
  <div class="logo"><img src="<%=path %>/images/logo_eds.jpg" width="380" height="86" /></div>
<div class="headerbotton">
    <ul>
      <li id="help"><img src="<%=path %>/images/guild_botton_eds.gif" /></li>
      <li><img src="<%=path %>/images/prr_botton_eds.gif" onclick="changePass()"/></li>
      <li id="logout"><a target="_top" href="../system/logoutAppUser.action"><img src="<%=path %>/images/up_botton_eds.gif" /></a></li>
    </ul>
    <div style="clear:both"></div>
    </div>
    <div class="time">
      <ul>
        <li>
          <p><span id="userName">用户：${USER_KEY.name }</span></p>
        </li>
        <li>
          <p><span id="nowTime"></span></p>
        </li>
      </ul>
      <div style="clear:both;"></div>
    </div>
    <div style="clear:both"></div>
  </div>
  </div>
</div>

<!--头部结束-->
<!--快速功能条-->
<div class="kuaijie">
  <div class="kjcont">
  <div class="gnbotton"><img src="<%=path %>/images/sybotton.gif" width="84" height="28" onClick="goHome()" style="cursor: hand;"/></div>
  
  <div class="kuaijieico"><img src="<%=path %>/images/kuaijie_eds.gif" width="138" height="44" /></div>
  <!--快速功能开始-->
  <div class="kjbotton">
    <ul>
      <s:iterator value="tusList">
        <li>
           <a href="<%=path %>/<s:property value="TProfile.remark2" />" target="mainContent"><span><s:property value="TProfile.name" /></span></a>
        </li>
      </s:iterator>
    </ul>
</div>  <!--快速功能结束-->
  <div style="clear:both;"></div>
  </div>
  
  <!-- <div class="xtxxbutton"><img src="../images/xttsbutton2.gif"></div> -->
</div>
<!--快速功能条结束-->

</body>
</html>
