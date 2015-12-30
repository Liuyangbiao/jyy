<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
	<head>
	    <title>无标题文档</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<link type="text/css" rel="stylesheet" href="../css/themes/jquery-ui-1.8.11.custom.css"/>
	    <link type="text/css" rel="stylesheet" href="../css/jquery.autocomplete.css"/>
	    <link type="text/css" rel="stylesheet" href="../css/base.css">
		<script type="text/javascript" src="../js/jquery-1.5.min.js" ></script>
		<script type="text/javascript" src="../js/jquery-ui-1.8.10.custom.min.js"></script>
		<script type="text/javascript" src="../js/jquery.autocomplete.js"></script>
		<script type="text/javascript" src="../js/jquery.validate.1.8.js" ></script>
		<script type="text/javascript" src="js/wholeSearch.js" ></script>
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
			$(function() {
				$("#conditionForm").validate({
					rules: {
						conditions: "required"
					},
					messages: {
						conditions: "请输入查询条件！"
					}
				});
			});
		</script>
	</head>

	<body>
		<table width="90%" border="0" cellspacing="0" cellpadding="0"
			class="contert2">
			<tr>
				<td class="conttop2">
					<div class="top2left"></div>
					<div class="top2right"></div>
					<div class="top2middle">
						<span class="topico2"><img
								src="../images/Accordion_ico03.gif">
						</span>
						<h3>
							全文检索
						</h3>
					</div>
				</td>
			</tr>
			<tr>
				<td class="contmiddle2">
					<form id="conditionForm" action="searchIndexedArchive.action?rows=10&page=1"
						method="post">
						<table width="830" border="0" cellpadding="0" cellspacing="5">
							<tr>
								<td width="15%" height="18" align="center">
									<strong style="font-size: 14px;">全文关键字</strong>
								</td>
								<td width="60%">
									<div class="seachDiv">
										<div class="searchInput2" style="float: left;">
											<input name="conditions" type="text" id="conditions" value="${conditions}"
												autocomplete="off" size="100" class="quanwen2">
										</div>
										<div name="helper" id="helper" class="gac_m" style="visibility:hidden;" width="100%">
								        </div>
									</div>
								</td>
								<td width="25%" align="center">
									<input type="checkbox" name="highLighter" id="highLighter" checked
										class="checkbox" ${!highLighter?"checked":""}>
									<strong style="font-size: 14px;">结果高亮显示</strong>
									<input type="hidden" name="menuId" id="menuId" value="<%=request.getParameter("menuId")%>">
								</td>
							</tr>
						</table>

						<table width="730" border="0" cellpadding="0" cellspacing="5">
							<tr>
								<td align="right">
									<input type="submit" name="button3" id="button3" value="查 询"
										class="chaxunbutton" />
								</td>
								<td>
									<input type="reset" name="button4" id="button4" value="清 空"
										class="qkbutton02" />
								</td>
							</tr>
						</table>
						<div class="bianxian"></div>
					</form>
				</td>
			</tr>
			<tr>
				<td class="contbottom2">
					<div class="bottom2left"></div>
					<div class="bottom2right"></div>
					<div class="bottom2middle"></div>
				</td>
			</tr>
		</table>
	</body>
</html>
