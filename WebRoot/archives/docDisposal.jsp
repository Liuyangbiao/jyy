<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/";
 %>

<html>
	<head>
		<title>归档处理列表</title>
		<link type="text/css" href="../css/jquery.autocomplete.css"
			rel="stylesheet" />
		<link href="../css/base.css" rel="stylesheet" type="text/css">
		<link href="../css/jquery.css" rel="stylesheet" type="text/css">
		<link href="../css/themes/jquery-ui-1.8.11.custom.css"
			rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" media="screen"
			href="../css/themes/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen"
			href="../css/themes/ui.multiselect.css" />
		<link href="../css/base.css" rel="stylesheet" type="text/css">
		<link href="../css/uploadify.css" rel="stylesheet" type="text/css" />
		<link href="../css/jquery.autocomplete.css" rel="stylesheet"
			type="text/css" />
		<link href="../css/skin-vista/ui.dynatree.css" rel="stylesheet"
			type="text/css" />
		<script src="../js/jquery-1.5.min.js" type="text/javascript">
        </script>
		<script src="../js/jquery.maskedinput.js" type="text/javascript">
        </script>
		<script src="../js/jquery-ui-1.8.10.custom.min.js"
			type="text/javascript">
        </script>
		<script src="../js/jquery.dynatree.js" type="text/javascript">
        </script>
		<script src="../js/jquery.cookie.js" type="text/javascript">
        </script>
		<script src="../js/jquery.jqGrid.js" type="text/javascript">
        </script>
		<script src="../js/jquery.form.js" type="text/javascript">
        </script>
		<script src="../js/rowedex4.js" type="text/javascript">
        </script>
		<script src="../js/jquery.timepicker.js" type="text/javascript">
        </script>
		<script src="../js/jquery.ui.datepicker-zh-CN.js"
			type="text/javascript">
        </script>
		<script type="text/javascript" src="js/docDetails.js">
        </script>
		<script type="text/javascript" src="../js/jquery.autocomplete.js">
        </script>
		<script type="text/javascript" src="js/dicDisposal.js">
        </script>
        <script type="text/javascript" src="../search/js/download.js" charset="utf-8"></script>
		<style type="text/css">
			label.error {
				float: none;
				color: red;
				padding-left: .5em;
				vertical-align: middle;
			}
			
			ul.dynatree-container
			{
				width: 300px;
			}
			
			ul.dynatree-container li
			{
				width: 300px;
			}
		</style>
		<script type="text/javascript" src="../js/popShortcut.js">
        </script>
		<script type="text/javascript">
            $(function(){
                var mId = "${param.menuId}";
                imgDisplay(mId);
              $("#onLineSeeDIV").hide();
               // $("#onLineSeeDIV").show();
                
            });
             function projectNameBlur(){
                	var projectName = $("#projectName").val();
                	if(projectName.length==0){
                    	$("#projectNameId").attr("value","");
                    	}
                }

             function divShow(){
               	 $("#onLineSeeDIV").hide(); 
           		 $("#mainDIV").show();
           		 document.ols.location.href="about:blank";
                }
                
             //点击在线查看时触发的函数
             function onlineDown(docid,projectid,companyid,name,path,suffix){

                var action = "../search/onLineSee.jsp";
         		var target = "ols";
         		var isOnLine = "false";
         		
         		onLineSeeOrDown(action,target,docid,projectid,companyid,
                         name,path,suffix,isOnLine)
         		
            		$("#onLineSeeDIV").show(); 
            		$("#mainDIV").hide();
             }
                
        </script>
	</head>
	<body>
	<div id="mainDIV">
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="contert2" style="font-Size: 11px;">
			<tr>
				<td class="conttop2">
					<div class="top2left">
					</div>
					<div class="top2right">
					</div>
					<div class="top2middle">
						<span class="bottonszkj2" id="img1"><a href="#"><img
									src="../images/szkj_botton.gif"
									onclick="topwin(${param.menuId })">
						</a>
						</span><span class="bottonszkj2" id="img2"><a href="#"><img
									src="../images/kjysz_botton.gif">
						</a>
						</span><span class="topico2"><img
								src="../images/Accordion_ico03.gif">
						</span>
						<h3>
							档案归档
						</h3>
					</div>
				</td>
			</tr>
			<tr>
				<td class="contmiddle2">
					<table width="800" border="0" cellspacing="0" cellpadding="0"
						class="conttitle">
						<tr>
							<td height="22">
								<h3>
									检索条件
								</h3>
							</td>
						</tr>
					</table>
					<!-- 查询条件 -->
					<form action="selectDocList.action" method="post"
						id="docDisposalForm">
						<input type="hidden" id="profileId" name="profileId" value="${param.menuId}">
						<table width="700" border="0" style="font-size: 12px;font-weight:bold;">
							<tr>
								<td align="right">
									工程名称
								</td>
								<td>
									<input id="projectNameId" type="hidden" name="projectNameId">
									<input id="projectName" name="projectName" size="35" onblur="projectNameBlur()" />
								</td>
								<td align="right">
									录入单位
								</td>
								<td>
									<input type="text" name="unitName" id="unitName" size="35" />
									<input type="hidden" size="10" name="unitNameCode"
										id="unitNameCode">
								</td>
							</tr>
							<tr>
								<td align="right">
									创建者
								</td>
								<td>
									<input type="text" name="creatorName" size="8" />
								</td>
								<td align="right">
									录入日期
								</td>
								<td>
									<input type="text" id="from" name="editorFormDate" size="8" />
									到
									<input type="text" id="to" name="editorToDate" size="8" />
								</td>
							</tr>
							<tr>
								<td align="right">
									文档分类
								</td>
								<td>
									<select id="docClassName" name="docClassName">
									</select>
								</td>
								<td align="right">
									<span id="baomispan">密级分类</span>
								</td>
								<td>
									<select id="baomi" name="baomi">
									</select>
								</td>

							</tr>
							<tr>
								<td align="right">
									文件名称
								</td>
								<td>
									<input type="text" name="fileName" size="15" />
								</td>
								<td align="right">
									<span id="docFormatspan">文件格式</span>
								</td>
								<td>
									<select id="fileFormat" name="fileFormat">
									</select>
								</td>
							</tr>
							<tr>
								<td align="right">
									档案状态
								</td>
								<td colspan="3" align="left">
									<input type="radio" name="fileStatus" id="fileStatus"
										value="03" checked="checked">未归档
									<input type="radio" name="fileStatus" id="fileStatus"
										value="01">归档
								</td>
							</tr>
							<tr>
								<td height="44" colspan="5" align="center">
									<input type="button" value="查询" onclick="selectDocDisposal()"
										class="chaxunbutton" />
								</td>
							</tr>
						</table>
					</form>
					<div class="bianxian" style="width: 750px;">
					</div>
					<table width="195" border="0" cellspacing="5" cellpadding="0">
						<tr>
							<td width="92">
								<input type="button" value="归档" id="disposal"
									onclick="guiDangClick()" class="dowbutton">
							</td>
							<td width="88">
								<input type="button" value="删除" id="delete"
									onclick="deleteDocDetails()" class="upbutton">
							</td>
							<td width="88">
								<input type="button" value="迁移" id="move"
									onclick="moveDocDetails()" class="upbutton">
							</td>
						</tr>
					</table>
					</form>
					<!-- jqgrid表格 -->
					<table id="rowedtable">
					</table>
					<!-- 显示页码的部分 -->
					<div id="prowed4">
					</div>
				</td>
			</tr>
			<tr>
				<td class="contbottom2">
					<div class="bottom2left">
					</div>
					<div class="bottom2right">
					</div>
					<div class="bottom2middle">
					</div>
				</td>
			</tr>
		</table>
		<!-- 档案详细信息 -->
		<s:include value="docInfo.jsp"/>
		<!-- 档案迁移 -->
		<div id="moveDocDialog" title="档案迁移">
			<div id="tree" style="height:155px;width:300px;">
				<ul>
					<s:iterator value="tDocTypeList"   >
						<li class="folder" data="code: '<s:property value="code" />',isLeaf:'0'">
							<s:property value="name" />
							<ul>
								<s:iterator value="list">
									<li data="code: '<s:property value="code" />',isLeaf:'1'">&nbsp;
										<s:property value="name" />
								</s:iterator>
							</ul>
					</s:iterator>
				</ul>
			</div>
			<input type="hidden" name="doccodes" id="doccodes" />
			<input type="hidden" name="docTypeCode" id="docTypeCode" />
		</div>
		<div id="dialog" title="图片预览">
                <img id="preimge" src="../images/circle_animation.gif" alt="图片" style="width:100%">
            </div>
		</div>
		 <div id="onLineSeeDIV">
            <iframe name="ols" id="ols" src="" height="100%" width="100%">
            </iframe> 
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
