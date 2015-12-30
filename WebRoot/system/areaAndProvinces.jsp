<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/"; 
%>
<html>
	<head>
		<title>各区域对应省公司</title>
		<link type="text/css" href="../css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet" />
		<link href="../css/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
		<link href="../css/input.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.multiselect.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.multiselect.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="../css/base.css">
		<script type="text/javascript" src="../js/jquery-1.5.min.js"></script>
		<script type="text/javascript" src="../js/jquery.validate.1.8.js"></script>
		<script type="text/javascript" src="../js/jquery.cookie.js"></script>
		<script type="text/javascript" src="../js/jquery.jqGrid.js"></script>
		<script type="text/javascript" src="../js/jquery-ui-1.8.10.custom.min.js"></script>
		<script src="../js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
		<script src="../js/jquery.timepicker.js" type="text/javascript"></script>
		<script type="text/javascript" src="../js/jquery.autocomplete.js"></script>
		<script type="text/javascript" src="../js/jquery.form.js"></script>
		<script type="text/javascript" src="../js/popShortcut.js"></script>
		<script src="../js/jquery.maskedinput.js" type="text/javascript"></script>
		<script type="text/javascript">
            $(function(){
                var areaId = '0'+${param.areaId};
                //设置快捷键
                var setKeyId = "${param.menuId}";
                imgDisplay(setKeyId);
                jQuery.post("${pageContext.request.contextPath}/system/"+
                        "getProvinces.action?time="+(new Date()).valueOf(),
                        {areaId:areaId},callbackFun,'json'); 
				function callbackFun(json){
					for ( var i = 0; i < json.length; i++) {
						var code = json[i].code;
						var ids = "tabs-" + code;
						var href = "#"+ids;
						var name = json[i].name;
						var appIp = json[i].appIp;
						var iframe ;
						if(null != appIp)
							iframe = "<iframe name='mainContent'   height='669' id='mainContentID' scrolling='auto'   width='98%' border='0' frameborder='0' src='remoteSite.action?remoteSite="+code+"&remotePage=doc'> 浏览器不支持嵌入式框架，或被配置为不显示嵌入式框架。</iframe>";
						else
							iframe = "<p style = 'padding:10px;'>无法与"+name+"服务器取得通信!</p>" ;
						var $a = $("<a>");
						var $li = $("<li>");
						var $div = $("<div>");
						$a.attr("href",href);
						$a.text(name);
						$li.append($a);
						$div.attr("id",ids);
						$div.append(iframe);
						$("#uls").append($li);
						$("#tabs").append($div);
					}
					$("#tabs").tabs();
				}
            });
        </script>
        <style>
        .tables{
	         border:1px solid #AED0EA;
	         width:100%;
	         margin-top:5px;
         }
         .tables tr td {
         	 font-size:13;
         }
         .tables2{
          	 margin-top:15px;
          	 width:100%; 
         }
         .tables2 tr th {
         	 color:#0063A9;
         	 font-weight:bold;
         	 font-family:Cursive;
         	 border-bottom:1px solid #AED0EA;
         }
        </style>
	</head>
	<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="contert2">
			<tr>
				<td class="conttop2">
					<div class="top2left">
					</div>
					<div class="top2right">
					</div>
					<div class="top2middle">
						<span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif"
									onclick="topwin(${param.menuId })" style="cursor: hand"></a>
						</span><span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a>
						</span><span class="topico2"><img src="../images/Accordion_ico03.gif"></span>
						<h3>查看省公司详细</h3>
					</div>
				</td>
			</tr>
			<tr>
				<td class="contmiddle2">
					<div id = "tabs" >
						<ul id = "uls" ></ul>
					</div>
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