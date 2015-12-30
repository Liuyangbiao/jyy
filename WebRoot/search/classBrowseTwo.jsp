<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.nsc.dem.bean.profile.TUser"%>
<%@page import="com.nsc.dem.service.system.IprofileService"%>
<%@page import="com.nsc.base.util.Component"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/";
String nodeId=request.getParameter("nodeId"); %>
<%
TUser user =(TUser) request.getSession().getAttribute("USER_KEY");
String mId=request.getParameter("menuId");
IprofileService profileService=(IprofileService)Component.getInstance("profileService",request.getSession().getServletContext());
boolean isDownLoad = profileService.getProfileByauthControl(user
, mId,  "下载");
request.setAttribute("isDownLoad",isDownLoad);

boolean isUpLoad = profileService.getProfileByauthControl(user
, mId,  "上传");
request.setAttribute("isUpLoad",isUpLoad); 

%>

<html>
	<head>
		<title>My JSP 'classConfigTab.jsp' starting page</title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script src="../js/jquery-1.5.min.js" type="text/javascript">
        </script>
		<script src="../js/jquery-ui-1.8.10.custom.min.js"
			type="text/javascript">
        </script>
		<script src="../js/jquery.cookie.js" type="text/javascript">
        </script>
		<script src="../js/jquery.dynatree.js" type="text/javascript">
        </script>
		<script src="../js/jquery.jqGrid.js" type="text/javascript">
        </script>
		<script type="text/javascript" src="js/docnode.js">
        </script>
		<script type="text/javascript" src="../js/popShortcut.js">
        </script>
		<script type="text/javascript" src="../js/jquery.cookie.js">
        </script>
		<script type="text/javascript" src="../archives/js/docDetails.js">
        </script>
		<link href="../css/themes/jquery-ui-1.8.11.custom.css"
			rel="stylesheet" type="text/css">
		<link href="../css/base.css" rel="stylesheet" type="text/css">
		<link href="../css/skin-vista/ui.dynatree.css" rel="stylesheet"
			type="text/css">
		<link rel="stylesheet" type="text/css" media="screen"
			href="../css/themes/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen"
			href="../css/themes/ui.multiselect.css" />
		<script type="text/javascript" src="js/download.js" charset="utf-8"></script>

		<script type="text/javascript">
                    var mId = "${param.menuId}";
                    var tempurl = "getNodeListInFormation.action?menuId="+mId;
                    
                        var id=<%=nodeId%>;
                        $(function(){
                            var mId = "${param.menuId}";
                             imgDisplay(mId);
                           
                        });
            
                        function divShow(){
                       	 $("#onLineSeeDIV").hide(); 
                   		 $("#mainDIV").show();
                   		 document.ols.location.href="about:blank";
                        }

                         //点击打包下载时触 发的事件
                        function packAgeDownLoad(){
            				var stringsplit="";
            				var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
                            for (var i = 0; i < selectedIds.length; i++) {
                                if (selectedIds[i] != undefined) {
                                    stringsplit += ":" + selectedIds[i];
                                }
                            }
            				if(stringsplit!=""){				
            					$('#services').dialog("open");
            					$("#showDownListId").empty();
            					var url = "getDownLoadAddress.action?time="+(new Date()).valueOf();
                                var params = {   
            		                eachCheckboxVals:stringsplit,  
            		                pa:"test"   
            		               };  
                                jQuery.post(url,params,callbackFun,'json'); 
            				}else{
            					alert("请先选择需打包下载的文件！");
            				}
                        }
                        
                        //点击在线查看时触发的函数
                        function onlineDown(docid,projectid,companyid,name,path,suffix){
                            var action = "onLineSee.jsp";
                    		var target = "ols";
                    		var isOnLine = "false";
                    		onLineSeeOrDown(action,target,docid,projectid,companyid,
                                    name,path,suffix,isOnLine)
                       		$("#onLineSeeDIV").show(); 
                       		$("#mainDIV").hide();
                        }

                        
                        $(function(){
                        	$("#onLineSeeDIV").hide();
                            $("#button9").attr("disabled", true);
                            docInfoDialog();
                            // Tabs
                            $('#tabs').tabs();
                            
                            $('#tabs2').tabs();
                            $.getJSON("selectIdGetName.action", {
                                id: id
                            }, function(json){
                                $("#treeName").text(json);
                            })
                        });
                        
                        function ReSizeTab(sWidth){
                             jQuery("#rowedtable").jqGrid('setGridWidth', sWidth);
                        }
                    
        </script>
	</head>
	<body>
		<table width="100%" height="669px;" border="0" cellspacing="0"
			cellpadding="0" class="contert2">
			<tr>
				<td class="contmiddle2">
					<div class="contnav">
						<ul>
							<li>
								当前的分类位置&nbsp;&nbsp;
							</li>
							<li id="position">
							</li>
						</ul>
						<div style="clear: both;">
						</div>
					</div>
					<table border="0" cellpadding="0" cellspacing="3" width="750">
						<tr>
							<td width="541" valign="top">
								<table width="100%" border="0" cellspacing="1" bgcolor="#aabbc4">
									<tr bgcolor="#edf1f6" width="100%">
										<td>
											<table width="193" height="37" border="0" cellpadding="0"
												cellspacing="2">
												<tr>
													<td height="33" align="center">
														<c:if test="${isDownLoad}">
															<input type="submit" name="button6" id="button6"
																value="打包下载" class="dowbutton"
																onClick="packAgeDownLoad()" />
														</c:if>
													</td>
													<td align="center">
														<c:if test="${isUpLoad}">
															<input type="submit" name="button9" id="button9"
																value="文件上传" class="upbutton" />
														</c:if>
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr bgcolor="#edf1f6" width="100%">
										<td style="padding: 5px;">
											<table id="rowedtable">
											</table>
											<div id="prowed4">
											</div>
										</td>
									</tr>
								</table>
							</td>
							<td valign="top">
								<table border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td width="16px" height="21" class="wdlrtitle_1">
										</td>
										<td width="380px" class="wdlrtitle_2">
											<span id="treeName"></span>
										</td>
										<td width="9px" class="wdlrtitle_3">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td colspan="3" valign="top" class="wdlrmiddle">
											<div id="tree"
												style="width: 300px; height: 409px; overflow: auto;">
												<!--树形菜单开始-->
												<ul id="browser" class="filetree">
												</ul>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="3" class="wdlrbottom">
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<div id="dialog" title="图片预览">
			<img id="preimge" src="../images/circle_animation.gif" alt="图片"
				style="width: 100%">
		</div>
		<iframe id="downLoadFrame"
			style="width: 200; height: 200; display: none;" name="downLoadFrame">
		</iframe>
		<div id="showdialog" title="打包下载">
			请输入打包名称：
			<input type="text" name="tdocname" id="tdocname">
		</div>
		<div id="services" title="下载地址" class="contert">
			<div id="displayMessage"></div>
			<form id='dowloadFormId' name='dowloadFormId'
				action='downloadFile.action' method="post" target="downLoadFrame">
				<input type="hidden" id="downType" name="downType" value="isLocal" />
				<input type="hidden" id="filename" name="filename" value="" />
				<div id="showDownListId">
				</div>
			</form>
		</div>
		<div id="docInfo" title="详细信息">
			<div>
				<ul id="docDetails">
					<h3>
						文档信息
					</h3>
					<li id="docName">
					</li>
					<li id="docClass">
					</li>
					<li id="docFormat">
					</li>
					<li id="docFileSize">
					</li>
					<li id="docVersion">
					</li>
					<li id="docCreate">
					</li>
					<li id="docCreateDate">
					</li>
				</ul>
			</div>
			<div style="padding-top: 30px;">
				<ul id="proDetials">
					<h3>
						工程信息
					</h3>
					<li id="ownerCode">
					</li>
					<li id="proCode">
					</li>
					<li id="proName">
					</li>
					<li id="proVoltagelevel">
					</li>
					<li id="proOpenYear">
					</li>
					<li id="projectStatus">
					</li>
					<li id="proParent">
					</li>
				</ul>
			</div>
			<div style="padding-top: 30px;">
				<ul id="preDesignDetails">
					<h3>
						初设信息
					</h3>
					<li id="unitName">
					</li>
					<li id="createDate">
					</li>
					<li id="ajtm">
					</li>
					<li id="andh">
					</li>
					<li id="flbh">
					</li>
					<li id="jcrm">
					</li>
					<li id="jnyh">
					</li>
					<li id="ljrm">
					</li>
					<li id="pzrm">
					</li>
					<li id="shrm">
					</li>
					<li id="sjjd">
					</li>
					<li id="sjrm">
					</li>
					<li id="tzmc">
					</li>
					<li id="tzzs">
					</li>
					<li id="xhrm">
					</li>
				</ul>
			</div>
		</div>
		<div id="onLineSeeDIV">
			<iframe name="ols" id="ols" src="" height="100%" width="100%">
			</iframe>
		</div>
		<form id="onLineSeeForm" action="" target="" method="post">
			<input type="hidden" name="docid" id="docid" value="" />
			<input type="hidden" name="projectId" id="projectId" value="" />
			<input type="hidden" name="code" id="code" value="" />
			<input type="hidden" name="name" id="name" value="" />
			<input type="hidden" name="path" id="path" value="" />
			<input type="hidden" name="suffix" id="suffix" value="" />
			<input type="hidden" name="isonline" id="isonline" value="" />
		</form>
	</body>
</html>
