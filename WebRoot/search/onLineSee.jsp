<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@page import="com.nsc.dem.bean.profile.TUser"%>
<%@page import="com.nsc.dem.service.system.IprofileService"%>
<%@page import="com.nsc.base.util.Component"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/";
request.setCharacterEncoding("utf-8");

%>
<%
TUser user =(TUser) request.getSession().getAttribute("USER_KEY");
String mId=request.getParameter("menuId");
IprofileService profileService=(IprofileService)Component.getInstance("profileService",request.getSession().getServletContext());
boolean isDownLoad = profileService.getProfileByauthControl(user
, mId,  "下载");
request.setAttribute("isDownLoad",isDownLoad);
 %>
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>My JSP 'classConfigTab.jsp' starting page</title>
        <meta http-equiv="content-type" content="text/html; charset=utf-8">
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <script src="<%=path %>/js/jquery-1.5.min.js" type="text/javascript">
        </script>
        <script src="<%=path %>/js/jquery-ui-1.8.10.custom.min.js" type="text/javascript">
        </script>
        <script src="<%=path %>/js/jquery.cookie.js" type="text/javascript">
        </script>
         <link href="<%=path %>/css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet" type="text/css">
        <link href="<%=path %>/css/base.css" rel="stylesheet" type="text/css">
        <link href="<%=path %>/css/skin-vista/ui.dynatree.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" media="screen" href="<%=path %>/css/themes/ui.jqgrid.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="<%=path %>/css/themes/ui.multiselect.css" />
        <script type="text/javascript">

        function download(){
        	$("#onLineSeeForm").attr("target","downLoadFrame");
        	$("#isonline").attr("value","true");
        	$("#onLineSeeForm").submit();
        }
        
        function divShowParent(){
        	parent.divShow();
        }
        
        $(document).ready(function(){
        	function onLineSeeOrDown(){
				$("#onLineSeeForm").attr("target","onLineSee");
				$("#docid").attr("value","${param.docid}");
				$("#projectId").attr("value","${param.projectId}");
				$("#code").attr("value","${param.code}");
				$("#name").attr("value","${param.name}");
				$("#path").attr("value",decodeURI("${param.path}"));
				$("#suffix").attr("value","${param.suffix}");
				$("#isonline").attr("value","${param.isonline}");
				$("#onLineSeeForm").submit();
			}
        	onLineSeeOrDown();
        });
        </script>
    </head>
    <body>
   
  
    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2" height="98%">
            <tr>
                <td class="conttop2">
                    <div class="top2left">
                    </div>
                    <div class="top2right">
                    </div>
                    <div class="top2middle">
                    	<span class="bottonszkj" style="cursor: hand;" onclick="divShowParent();">
                    		<img src="../images/fanhui_botton.gif">
                    	</span>
                    	<span class="bottonszkj">
                    	
                    	   <c:if test="${isDownLoad}">

                    		<input type="button" name="button3" id="button3" value="文件下载"  onclick="download()"
										class="dowbutton" />
									  </c:if>
                    	</span>
                      	<span class="topico2">
                        	<img src="../images/Accordion_ico03.gif">
                        </span>
                        <h3>在线浏览</h3>
                    </div>
                </td>
            </tr>
           <tr height="*%">
             <td height="100%">
               <iframe width="100%" height="100%" id="onLineSee" name="onLineSee"></iframe >
             </td>
           </tr>
            <tr>
                <td class="contbottom2">
                    <div class="bottom2left">
                    </div>
                    <div class="bottom2right">
                    </div>
                    <div class="bottom2middle">
                    	<span class="bottonszkj" style="cursor: hand;" onclick="divShowParent();"><img src="../images/fanhui_botton.gif"></span>
                    </div>
                </td>
            </tr>
        </table>
        <iframe id="downLoadFrame" name="downLoadFrame"
			style="width: 200; height: 200; display: none">
		</iframe>
    
    
       <form id="onLineSeeForm" action="getOnLineSee.action" method="post">
        	<input type="hidden" name="docid" id="docid" value=""/>
        	<input type="hidden" name="projectId" id="projectId" value=""/>
        	<input type="hidden" name="code" id="code" value=""/>
        	<input type="hidden" name="name" id="name" value=""/>
        	<input type="hidden" name="path" id="path" value=""/>
        	<input type="hidden" name="suffix" id="suffix" value=""/>
        	<input type="hidden" name="isonline" id="isonline" value=""/>
        </form>
    
    
    </body>
    
    
    
</html>
