<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=utf-8"%>
<%@ page import="com.nsc.dem.bean.profile.TUser"%>
<%@ page import="com.nsc.dem.service.system.IprofileService"%>
<%@ page import="com.nsc.base.util.Component"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
String path = request.getContextPath();
TUser user =(TUser) request.getSession().getAttribute("USER_KEY");
String mId=request.getParameter("menuId");
IprofileService profileService=(IprofileService)Component.getInstance("profileService",request.getSession().getServletContext());
boolean isDownLoad = profileService.getProfileByauthControl(user
, mId,  "下载");
boolean isOnLineSee = profileService.getProfileByauthControl(user
		, mId,  "在线查看预览");
request.setAttribute("isDownLoad",isDownLoad);
request.setAttribute("isOnLineSee",isOnLineSee); %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>无标题文档</title>
		<link rel="stylesheet" type="text/css" href="<%=path %>/css/base.css" />
		<link rel="stylesheet" type="text/css" href="<%=path %>/css/jquery.autocomplete.css" />
		<link rel="stylesheet" type="text/css" href="<%=path %>/css/themes/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" href="<%=path %>/css/themes/ui.multiselect.css" />
		<link rel="stylesheet" type="text/css" href="<%=path %>/css/themes/jquery-ui-1.8.11.custom.css" />
		
		<script type="text/javascript" src="<%=path %>/js/jquery-1.5.min.js" ></script>
		<script type="text/javascript" src="<%=path %>/js/jquery-ui-1.8.10.custom.min.js"></script>
		<script type="text/javascript" src="<%=path %>/js/jquery.autocomplete.js"></script>
		<script type="text/javascript" src="<%=path %>/js/jquery.validate.1.8.js" ></script>
        <script type="text/javascript" src="<%=path %>/js/popShortcut.js"></script>
		<script type="text/javascript" src="<%=path %>/search/js/wholeSearch.js" ></script>
		
		<script type="text/javascript" src="<%=path %>/js/jquery.form.js" ></script>
		<script type="text/javascript" src="<%=path %>/search/js/download.js" charset="utf-8"></script>
        <style type="text/css">
		.gac_m {
			background:white none repeat scroll 0 0;
			border:1px solid black;
			cursor:default;
			font-size:13px;
			line-height:17px;
			margin:0;
			position:absolute;
			z-index:99;
			width:100px;
			left:10px;
			top:10px;
			}
			.gac_b {
			background:#3366cc !important; color:white;
			}
		</style>
		<script type="text/javascript">		     
			$(function(){        
                $("#conditionForm").validate({
				rules: {
					conditions: "required"
				},
				messages: {
					conditions: "请输入查询条件！"
				}
			   });
			})
			
			function divShow(){
		    	 $("#onLineSeeDIV").hide(); 
				 $("#mainDIV").show();
				 document.ols.location.href="about:blank";
		     }


			//输入包名时调用
			$(function(){        
			    $("#onLineSeeDIV").hide();   
				$('#showdialog').dialog({
		            width: 400,
		            height: 130,
		            autoOpen: false,//打开模式窗体
		            resizable: false,//是否可以调整模式窗体的大小
		            draggable: false,//是否可以拖动模式窗体
		            modal: true,//启用模式窗体
		            closeOnEscape: true,//按下esc可以退出
		            bgiframe: true,
		            buttons: {
		                     "下载": function(){
		                     var tdocname = $("#tdocname").val();
							 $('#filename').attr("value",tdocname);
							 $('#dowloadFormId').submit();
							 $(this).dialog("close");
		                   },
		                  "取消": function(){
		                	  $(this).dialog("close");
		                  }
		                },
		            close: function(){
		            	$("#tdocname").attr("value","");
		            }
		        });
			})
			
			
            //跳转页面
            function onkeyPage(obj){
                var page = $(obj).val();
                if(event.keyCode=="13"){
                	 var conditions = $("#conditions").val();
                    $("#conditionForm").attr("action","searchIndexedArchive.action?rows=${rows}&page="+page);
     		        $("#conditionForm").submit();
                  }
               }
            
		</script>
	</head>

	<body>
		<div id="mainDIV">
			<table width="90%" border="0" cellspacing="0" cellpadding="0"
				class="contert2">
				<tr>
					<td class="conttop2">
						<div class="top2left"></div>
						<div class="top2right"></div>
						<div class="top2middle">
							<span class="bottonszkj"><a
								href="wholeSearch.jsp?menuId=${menuId}" ><img
										src="../images/fanhui_botton.gif"> </a> </span>
							<span class="topico2"><img
									src="../images/Accordion_ico03.gif"> </span>
							<h3>
								检索结果
							</h3>
						</div>
					</td>
				</tr>
				<tr>
					<td class="contmiddle2">
						<form id="conditionForm"
							action="searchIndexedArchive.action?rows=${rows}&page=1"
							method="post">
							<table width="850" border="0" cellpadding="0" cellspacing="5">
								<tr>
									<td width="18%" height="18" align="center">
										<strong style="font-size: 14px;">全文关键字</strong>
									</td>
									<td width="60%">
										<div class="seachDiv">
											<div class="searchInput2" style="float: left;">
												<input name="conditions" type="text" value="${conditions}"
													autocomplete="off" id="conditions" size="80" class="quanwen2">
											</div>
											<div id="helper" class="gac_m" style="visibility:hidden;">
	      									</div>	
										</div>
									</td>
									<td width="25%" align="center">
										<input type="checkbox" name="highLighter" id="highLighter"
											${highLighter?"checked":""}
											class="checkbox">
										<strong style="font-size: 14px;">结果高亮显示</strong>
									</td>
									<td width="13%" align="center">
										<input type="submit" name="button4" id="button4" value="重新检索"
											class="chaxunbutton" />
									</td>
								</tr> 
								<tr>
									<td height="48">
										<c:if test="${isDownLoad}">
										<input type="button" name="button6" id="button6" value="打包下载"
											class="dowbutton" onclick="packAgeDownLoad();" />
											</c:if>
									</td>
									<td width="60%">
										&nbsp;
									</td>
									<td width="17%">
										&nbsp;
									</td>
									<td width="12%">
										&nbsp;
									</td>
								</tr>
							</table>
							<div class="bianxian"></div>
						</form>
	
						<!-- 此处放置查询结果 -->
						<s:iterator value="docList">
							<div class="contjieguo">
								<h2>
									<span class="jieguoxz"><input type="checkbox"
											name="checkDownloadVals" id="${docid}${projectid}"  value="${docid}<>${projectid}<>${companyid}<>${title}<>${url}<>${suffix}"> 
									</span>
									<c:if test="${isOnLineSee}">
								<a href="javascript:onlineDown('${docid}${projectid}')">
									<s:property value="title" escape="false" default="无标题" />
								</a>
								</c:if>
								<c:if test="${!isOnLineSee}">
								<s:property value="title" escape="false" default="无标题" />
								</c:if>
								
									<span><img src="../images/<s:property value="format" default="unkown" />.bmp" width="15px"></span>
									<strong style="font-size:12px">
										<c:if test="${isDownLoad}">
											<a href="javascript:download('${docid}${projectid}')">下载全文</a>
										</c:if>
									</strong>
								</h2>
								<ul>
									<li>
										<s:property value="doctype" default="无文档分类" />
									</li>
									<li class="character">
										-
									</li>
									<li>
										<s:property value="proType" default="无工程类型" />
									</li>
									<li class="character">
										-
									</li>
									<li>
										<s:property value="project" default="无工程" /> 
									</li>
									<li class="character">
										-
									</li>
									<li>
										<s:property value="pharase" default="无阶段" />
									</li>
									<li class="character">
										-
									</li>
									<li>
										<s:property value="company" default="无业主单位" />
									</li>
									<li class="character">
										-
									</li>
									<li class="character">
										<s:property value="author" default="无作者" />
									</li>
								</ul>
								<div style="clear: both;"></div>
								<p>
									<s:property value="contents" escape="false" default="无内容" />
								</p>
							</div>
						</s:iterator>
	
						<div id="pagebar" style="width: 98%">
							<s:if test="page>10">
								<a href="#"></a>
							</s:if>
							<s:if test="page>5">
								<c:forEach begin="${page-5}" end="${page-1}" step="1"
									var="i" varStatus="status">
									<s:url id="url" action="searchIndexedArchive.action"
										encode="true">
										<s:param name="rows">${rows}</s:param>
										<s:param name="page">${i}</s:param>
										<s:param name="conditions">${conditions}</s:param>
										<s:param name="highLighter">${!highLighter}</s:param>
									</s:url>
									<s:a href="%{url}">${i}</s:a>
								</c:forEach>
							</s:if>
							<s:else>
								<c:forEach begin="1" end="${page-1}" step="1"
									var="i" varStatus="status">
									<s:url id="url" action="searchIndexedArchive.action"
										encode="true">
										<s:param name="rows">${rows}</s:param>
										<s:param name="page">${i}</s:param>
										<s:param name="conditions">${conditions}</s:param>
										<s:param name="highLighter">${!highLighter}</s:param>
									</s:url>
									<s:a href="%{url}">${i}</s:a>
								</c:forEach>
							</s:else>
							<span class="page_now">${page}</span>
	
							<s:if test="page+5>totals">
								<c:forEach begin="${page+1}" end="${totals}" step="1" var="i"
									varStatus="status">
									<s:url id="url" action="searchIndexedArchive.action">
										<s:param name="rows">${rows}</s:param>
										<s:param name="page">${i}</s:param>
										<s:param name="conditions">${conditions}</s:param>
										<s:param name="highLighter">${!highLighter}</s:param>
									</s:url>
									<s:a href="%{url}">${i}</s:a>
								</c:forEach>
							</s:if>
							<s:else>
								<c:forEach begin="${page+1}" end="${page+5}" step="1" var="i"
									varStatus="status">
									<s:url id="url" action="searchIndexedArchive.action">
										<s:param name="rows">${rows}</s:param>
										<s:param name="page">${i}</s:param>
										<s:param name="conditions">${conditions}</s:param>
										<s:param name="highLighter">${!highLighter}</s:param>
									</s:url>
									<s:a href="%{url}">${i}</s:a>
								</c:forEach>
							</s:else>
	
							<p>
								共${totals}页&nbsp;&nbsp;共${records}个文件
								<s:if test="page>10">
									跳到第
									<input name="textfield3" type="text" id="textfield3" size="5"
										class="text" onkeyup="onkeyPage(this)">
									页
								</s:if>
							</p>
							<div style="clear: both;"></div>
						</div>
					</td>
				</tr>
				<tr>
					<td class="contbottom2">
						<div class="bottom2left"></div>
						<div class="bottom2right"></div>
						<div class="bottom2middle">
							<span class="bottonszkj"><a
								href="wholeSearch.jsp?menuId=${menuId}" target="mainContent"><img
										src="../images/fanhui_botton.gif"> </a> </span>
						</div>
					</td>
				</tr>
			</table>

			<div id="showdialog" title="打包下载" >
				请输入打包名称：
				<input type="text" name="tdocname" id="tdocname">
			</div>
			
	     	<div id="services" title="下载地址" class="contert">
				<div id="displayMessage"></div>
				
				<form id='dowloadFormId' name='dowloadFormId' action='downloadFile.action' method="post" target="downLoadFrame">
					<input type="hidden" id="downType" name="downType" value="isLocal" /> 
					<input type="hidden" id="filename" name="filename" value=""/>
					<div id="showDownListId">
					</div>
				</form>
				<%--
				</iframe>
				--%>
			</div>
			<iframe id="downLoadFrame" name="downLoadFrame"
				style="width: 200; height: 200; display: none">
			</iframe>
		</div>
	    <div id="onLineSeeDIV">
	    	<iframe name="ols" id="ols" src="" height="728" width="100%"></iframe>
	    </div>
        <form id="onLineSeeForm" action="" target="" method="post">
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
