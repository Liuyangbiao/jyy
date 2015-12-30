<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.nsc.dem.bean.profile.TUser"%>
<%@page import="com.nsc.dem.bean.profile.TRole"%>
<%@page import="com.nsc.base.util.DateUtils"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.nsc.base.conf.Configurater"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	TUser user = (TUser) request.getSession().getAttribute("USER_KEY");
	TRole role = user.getTRole();
	String roleid = role.getId();
	String unitName = user.getTUnit().getName();
	String unitId = user.getTUnit().getProxyCode();
	SimpleDateFormat timeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	String datetime = (timeFormat.format(user.getLastLoginTime()));
	Configurater con = Configurater.getInstance();
	String profileRole = con.getConfigValue("admin");
%>
<html>
	<head>
		<title>系统首页</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link type="text/css" href="css/jquery.autocomplete.css"
			rel="stylesheet" />
		<link href="css/base.css" rel="stylesheet" type="text/css">
		<link href="css/jquery.css" rel="stylesheet" type="text/css">
		<link href="css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet"
			type="text/css">
		<link rel="stylesheet" type="text/css" media="screen"
			href="css/themes/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen"
			href="css/themes/ui.multiselect.css" />
		<script src="js/jquery-1.5.min.js" type="text/javascript"></script>
		<script src="js/jquery-ui-1.8.10.custom.min.js" type="text/javascript"></script>
		<link rel="stylesheet" href="css/themes/jquery.ui.all.css">
		<link rel="stylesheet" href="css/demos.css">
		<script src="js/jquery.cookie.js" type="text/javascript"></script>
		<script src="js/jquery-ui-1.8.10.custom.min.js" type="text/javascript"></script>
		<script src="js/jscharts_mb_line.js" type="text/javascript"></script>
		<script src="js/jscharts_mb.js" type="text/javascript"></script>
		<link href="css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet"
			type="text/css">
		<script type="text/javascript"><!--
            var insertDocCounturl = "<%=path%>/search/insertDocCount.action";
            var insertProjectCounturl = "<%=path%>/search/insertProjectCount.action";
            var BrowseOperateLogurl = "<%=path%>/search/selectBrowseOperateLog.action";
            var selectInsertDocPicurl = "<%=path%>/search/selectInsertDocPic.action";
            var selectProjectDocCounturl = "<%=path%>/search/selectProjectDocCount.action";
            var userInfoUrl = "<%=path%>/search/userInfo.action";
            var unitId = "<%=unitId%>";
            var loginId = "${USER_KEY.loginId}";
            var date = new Date();
            var count = new Array();
            $(function(){
                $("#onLineSeeDIV").hide();
                $.ajaxSetup({
                    cache: false
                });
                userInfo();
                browsedoc();
                insertDocCount();
                insertProjectCount();
                queryMonth();
            });
            
            function selectInsertDocPic(drawNamePic,drawNameLine, drawTitle, drawNameX, drawNameY,searchType){
                $.getJSON(selectInsertDocPicurl, {
                    unitId: unitId,
                    searchType: searchType
                }, function(json){
                	var bgColor = "";
                    var colors = new Array();
                    var color = "" ;
               		if(json.length!=0){
	            	   	for (var i = 0; i < json.length; i++) {
	                       while(true){
	                       	bgColor = (Math.random() * 0xffffff << 0).toString(16);
	                       	if (bgColor.length == 6) {
                               colors[i] = "#" + bgColor;
                               color = colors[i-1];
                               break;
	                        }
	                      }
		                }
		                selectinsertDocPicBar(json, colors, drawNamePic, drawTitle, drawNameX, drawNameY);
		                if(json.length == 1){
			                if(json[0][0].indexOf("-") != -1){
			                	selectinsertDocPicLine(json, color, drawNameLine,drawTitle,drawNameX,drawNameY);
			                }else{
				                $("#"+drawNameLine).html("暂无对比信息，走势图无法显示!");
			                }
		                }else{
		                	selectinsertDocPicLine(json, color, drawNameLine,drawTitle,drawNameX,drawNameY);
		                }
	                }
                });
            }

            //从国网点击按区域、按电压调用
            function selectInsertDocPicWithTesion(drawNamePic,drawNameLine, drawTitle, drawNameX, drawNameY,searchType){
                $.getJSON(selectInsertDocPicurl, {
                    unitId: unitId,
                    searchType: searchType
                }, function(json){
                	var bgColor = "";
                    var colors = new Array();
                    var color = "" ;
               		if(json.length!=0){
	            	   	for (var i = 0; i < json.length; i++) {
	                       while(true){
	                       	bgColor = (Math.random() * 0xffffff << 0).toString(16);
	                       	if (bgColor.length == 6) {
                               colors[i] = "#" + bgColor;
                               color = colors[i-1];
                               break;
	                        }
	                      }
		                }
		                selectinsertDocPicBar(json, colors, drawNamePic, drawTitle, drawNameX, drawNameY);
		                selectProjectDocCountBar(json, colors, drawNameLine, drawTitle);
	                }
                });
            }
            
			//从国网点击按省公司统计调用
            function selectInsertDocPicWithCountry(drawNamePic,drawNameLine, drawTitle, drawNameX, drawNameY,searchType){
                $.getJSON(selectInsertDocPicurl, {
                    unitId: unitId,
                    searchType: searchType
                }, function(json){
                	var bgColor = "";
                    var colors = new Array();
               		if(json.length!=0){
	            	   	for (var i = 0; i < json.length; i++) {
	                       while(true){
	                       	bgColor = (Math.random() * 0xffffff << 0).toString(16);
	                       	if (bgColor.length == 6) {
                               colors[i] = "#" + bgColor;
                               break;
	                        }
	                      }
		                }
	            	   	selectinsertDocPicBarWithCountry(json, colors, drawNamePic, drawTitle, drawNameX, drawNameY);
	                }
                });
            }
            
            //走势图开始
            function selectinsertDocPicLine(myData,color,drawName,drawTitle,drawNameX,drawNameY){
            	var myChart = new JSChart(drawName, 'line');
				myChart.patchMbString();
				for(var i=0;i<myData.length;i++){
            		var ymonth=myData[i][0];
            	   	var lable=myData[i][0];
            	   	var toolTip = myData[i][1];
            	   	myData[i][0]=i;
            		myChart.setTooltip([i, toolTip]);
            		myChart.setLabelX([i,lable]);
            	}
            	myChart.setDataArray(myData);
            	myChart.setTitle(drawTitle);
            	myChart.setTitleColor('#8E8E8E');
            	myChart.setTitleFontSize(11);
                myChart.setAxisNameX(drawNameX);
                myChart.setAxisNameY(drawNameY);
            	myChart.setAxisColor('#c6c6c6');
            	myChart.setAxisValuesColor('#949494');
                myChart.setAxisPaddingTop(60);
                myChart.setAxisPaddingLeft(60);
                myChart.setAxisPaddingBottom(60);
                myChart.setTextPaddingBottom(20);
                myChart.setTextPaddingLeft(10);
            	myChart.setShowXValues(false);
            	myChart.setLineColor(color);//折线颜色
            	myChart.setLineWidth(2);
            	myChart.setSize(450, 274);
            	myChart.draw();
            }
            //画柱状图开始
            function selectinsertDocPicBar(myData,colors,drawName,drawTitle,drawNameX,drawNameY){
                var myChart = new JSChart(drawName, 'bar');
                myChart.patchMbString();
                myChart.setDataArray(myData);
                myChart.colorizeBars(colors);
                myChart.setTitle(drawTitle);
                myChart.setTitleColor('#8E8E8E');
                myChart.setAxisNameX(drawNameX);
                myChart.setAxisNameY(drawNameY);
                myChart.setAxisColor('#c6c6c6');
                myChart.setAxisWidth(1);
                myChart.setAxisNameColor('#9a9a9a');
                myChart.setAxisValuesColor('#939393');
                myChart.setAxisPaddingTop(60);
                myChart.setAxisPaddingLeft(60);
                myChart.setAxisPaddingBottom(60);
                myChart.setTextPaddingBottom(20);
                myChart.setTextPaddingLeft(10);
                myChart.setTitleFontSize(11);
                myChart.setAxisNameFontSize(12);
                myChart.setAxisValuesFontSize(9);
                myChart.setBarValuesFontSize(9);
                myChart.setBarBorderWidth(0);
                myChart.setBarSpacingRatio(50);
                myChart.setBarValuesColor('#737373');
                myChart.setGrid(false);
                myChart.setSize(450, 274);
                myChart.draw();
            }
          //画柱状图开始
            function selectinsertDocPicBarWithCountry(myData,colors,drawName,drawTitle,drawNameX,drawNameY){
                var myChart = new JSChart(drawName, 'bar');
                myChart.patchMbString();
                myChart.setDataArray(myData);
                myChart.colorizeBars(colors);
                myChart.setTitle(drawTitle);
                myChart.setTitleColor('#8E8E8E');
                myChart.setAxisNameX(drawNameX);
                myChart.setAxisNameY(drawNameY);
                myChart.setAxisColor('#c6c6c6');
                myChart.setAxisWidth(1);
                myChart.setAxisNameColor('#9a9a9a');
                myChart.setAxisValuesColor('#939393');
                myChart.setAxisPaddingTop(60);
                myChart.setAxisPaddingLeft(60);
                myChart.setAxisPaddingBottom(60);
                myChart.setTextPaddingBottom(20);
                myChart.setTextPaddingLeft(10);
                myChart.setTitleFontSize(11);
                myChart.setAxisNameFontSize(12);
                myChart.setAxisValuesFontSize(9);
                myChart.setBarValuesFontSize(9);
                myChart.setBarBorderWidth(0);
                myChart.setBarSpacingRatio(50);
                myChart.setBarValuesColor('#737373');
                myChart.setGrid(false);
                var width = myData.length*43 <=450?450:myData.length*43;
                myChart.setSize(width, 274);
                myChart.draw();
            }
            //画饼图开始
            function selectProjectDocCountBar(myData, colors,drawName,drawTitle){
                var myChart = new JSChart(drawName, 'pie');
                myChart.patchMbString();
                myChart.setFontFamily("微软雅黑");
                myChart.setDataArray(myData);
                myChart.colorizePie(colors);
                myChart.setTitle(drawTitle);
                myChart.setPieUnitsFontSize(10);
                myChart.setTitleFontSize(10);
                myChart.setTextPaddingTop(5);
                myChart.setPieUnitsColor('#580022');
                myChart.setPieValuesColor('#005126');
                myChart.setPieValuesFontSize(10);
                myChart.setPieUnitsFontSize(10);
                myChart.setPieUnitsOffset(10);
                myChart.setSize(450, 274);
                myChart.setPieRadius(75);
                myChart.draw();
            }
            //用户信息
            function userInfo(){
                $("#userInfotable tr:not(:eq(0))").remove();
                $("#myloginInfotable tr:not(:eq(0))").remove();
                var newtable = $("#userInfotable");
                var mylogintable = $("#myloginInfotable");
                var roleId = "<%=roleid%>";
                if (roleId == "<%=profileRole%>") {
                    $.getJSON(userInfoUrl, {
                        loginId: loginId
                    }, function(json){
                        for (var i = 0; i < json.length; i++) {
                            var href = "system/logDisplay.jsp?loginId=" + json[i].loginId +"&type=L01&loginName="+json[i].loginName;
                            $("#userInfotable").css("display", "block");
                            $("#myloginInfotable").css("display", "none");
                            var onetr = $("<tr></tr>");
                            $(onetr).attr("id", i);
                            var loginName = $("<td></td>");
                            var myloginName = "<a href='" + href + "' style='text-decoration:underline;font-weight:normal;'>" + json[i].loginName + "</a>"
                            $(loginName).html(myloginName);
                            var loginCount = $("<td align='right'></td>");
                            $(loginCount).html(json[i].loginCount);
                            $("#loginId").css("display", "block");
                            $("#myLoginInfo").css("display", "none");
                            var lastloginTime = $("<td align='center'></td>");
                            $(lastloginTime).html(json[i].lastloginTime);
                            $(onetr).append(loginName);
                            $(onetr).append(loginCount);
                            $(onetr).append(lastloginTime);
                            $(newtable).append($(onetr));
                        }
                    })
                }
                else {
                    $("#myloginInfotable").css("display", "block");
                    $("#userInfotable").css("display", "none");
                    $("#myLoginInfo").css("display", "block");
                    $("#loginId").css("display", "none");
                    var myloginName = "<%=user.getName()%>";
                    var onetr = $("<tr></tr>");
                    $(onetr).attr("id", 1);
                    var logintd = $("<td></td>");
                    var loginname = $("<td></td>");
                    $(logintd).html("用户名");
                    $(loginname).html(myloginName);
                    $(onetr).append(logintd);
                    $(onetr).append(loginname);
                    var twotr = $("<tr></tr>");
                    var dutytd = $("<td></td>");
                    var duty = $("<td></td>");
                    $(dutytd).html("职务");
                    $(duty).html("<%=user.getDuty()%>");
                    $(twotr).append(dutytd);
                    $(twotr).append(duty);
                    var three = $("<tr></tr>");
                    var unittd = $("<td></td>");
                    var unitName = $("<td></td>");
                    $(unittd).html("所属单位");
                    $(unitName).html("<%=unitName%>");
                    $(three).append(unittd);
                    $(three).append(unitName);
                    var four = $("<tr></tr>");
                    var lasttd = $("<td></td>");
                    var lasttime = $("<td ></td>");
                    $(lasttd).html("上次登录时间");
                    $(lasttime).html("<%=datetime%>");
                    $(four).append(lasttd);
                    $(four).append(lasttime);
                    var five = $("<tr></tr>");
                    var loginCounttd = $("<td></td>");
                    var loginCount = $("<td></td>");
                    $(loginCounttd).html("登录次数");
                    $(loginCount).html("<%=user.getLoginCount()%>");
                    $(five).append(loginCounttd);
                    $(five).append(loginCount);
                    $(myloginInfotable).append($(onetr));
                    $(myloginInfotable).append($(twotr));
                    $(myloginInfotable).append($(three));
                    $(myloginInfotable).append($(four));
                    $(myloginInfotable).append($(five));
                }
                var userCodeLength = ${fn:length(USER_KEY.TUnit.code) } ;
                if( userCodeLength == 8 ){
                    	$("#searchProvince").css("display","none");
                    	$("#searchMonth").css("display","block");
                    	$("#searchTesion").css("display","block");
                    	$("#searchArea").css("display","none");
                    } else if ( userCodeLength == 6 ){
                    	$("#searchArea").css("display","none");
                    	$("#searchProvince").css("display","block");
                    	$("#searchMonth").css("display","block");
                    	$("#searchTesion").css("display","block");
                    } else if ( userCodeLength == 4 ){
                    	$("#searchArea").css("display","block");
                    	$("#searchProvince").css("display","block");
                    	$("#searchMonth").css("display","block");
                    	$("#searchTesion").css("display","block");
                    } else if ( userCodeLength == 2 ){
                    	$("#searchArea").css("display","block");
                    	$("#searchProvince").css("display","block");
                    	$("#searchMonth").css("display","block");
                    	$("#searchTesion").css("display","block");
                    }
            }
            
            //最近浏览文件
            function browsedoc(){
                $("#browseDoctable tr:not(:eq(0))").remove();
                var newtable = $("#browseDoctable");
                $.getJSON(BrowseOperateLogurl, {
                    loginId: loginId
                }, function(json){
                    if (json.length > 0) {
                        $("#spanbrowse").css("display", "none");
                        $("#cborwsedoc").css("display", "block");
						$("#browseDoctable").css("display","block");
						for (var i = 0; i < json.length; i++) {
                            var onetr = $("<tr></tr>");
                            $(onetr).attr("id", i);
                            var datetime = $("<td align='center'></td>");
                            $(datetime).html(json[i].datetime);
                           var href = "<a href='#' style='text-decoration:underline;font-weight:normal;' onclick='onlineDown(\"" + json[i].docid +"\",\""+
                           json[i].projectid +"\",\""+json[i].companyid+"\",\""+json[i].name+"\",\""+json[i].suffix+"\",\""+json[i].path+
                            "\")' style='cursor:hand'>" +
                            json[i].name+"."+json[i].suffix
                           "</a>";
                            var filename = $("<td></td>");
                            $(filename).html(href);
                            $(onetr).append(filename);
                            $(onetr).append(datetime);
                            $(newtable).append($(onetr));
                        }
                    }else {
                        $("#spanbrowse").css("display", "block");
                        $("#cborwsedoc").css("display", "none");
						$("#browseDoctable").css("display","none");
                    }
                });
            }
            function divShow(){
              	 $("#onLineSeeDIV").hide(); 
          		 $("#mainDIV").show();
          		 document.ols.location.href="about:blank";
               }
            //点击在线查看时触发的函数
            function onlineDown(docid, projectid,companyid,name,suffix,path){
            	var action = "search/onLineSee.jsp";
          		var target = "ols";
          		var isOnLine = "false";
          		onLineSeeOrDown(action,target,docid,projectid,companyid,name,path,suffix,isOnLine)
             	$("#onLineSeeDIV").show(); 
             	$("#mainDIV").hide();
            }

            function onLineSeeOrDown(action,target,docid,projectid,companyid,
                    filename,path,suffix,isOnLine){
				$("#onLineSeeForm").attr("action",action);
				$("#onLineSeeForm").attr("target",target);
				$("#docid").attr("value",docid);
				$("#projectId").attr("value",projectid);
				$("#code").attr("value",companyid);
				$("#name").attr("value",filename);
				$("#path").attr("value",path);
				$("#suffix").attr("value",suffix);
				$("#isonline").attr("value",isOnLine);
				$("#onLineSeeForm").submit();
			}
            
            //按月份查询统计
            function queryMonth(){
              	$("#area").css("display","none");
              	$("#province").css("display","none");
              	$("#month").css("display","block");
              	$("#tesion").css("display","none");
              	if($("#searchMonth").attr("id")!=null){
              		selectInsertDocPic("monthDocPic","monthDocPicLine","每月档案数量情况","年月","档案数量",$("#searchMonth").attr("id"));}
              	else {
              		return false;}
            }
                
            //新增工程数
            function insertProjectCount(){
                $("#insertProjectCount tr:not(:eq(0))").remove();
                //找到要追加的表格
                var newtable = $("#insertProjectCount");
                $.getJSON(insertProjectCounturl, {
                    loginId: loginId,
                    unitId: unitId
                }, function(json){
                    var newtr = $("<tr></tr>");
                    $(newtr).attr("id", 1);
                    var total = $("<td width='135px'></td>");
                    $(total).html("工程总数："+json[1] + " 个");
                    var projectCount = $("<td></td>");
                    $(projectCount).html("新增工程："+json[0] + " 个(自上次登录后)");
                    $(newtr).append(total);
                    $(newtr).append(projectCount);
                    $(newtable).append($(newtr));
                });
            }
            
            //新增档案数
            function insertDocCount(){
                $("#insertDocCount tr:not(:eq(0))").remove();
                //找到要追加的表格
                var newtable = $("#insertDocCount");
                $.getJSON(insertDocCounturl, {
                    unitId: unitId
                }, function(json){
                    var newtr = $("<tr></tr>");
                    $(newtr).attr("id", 1);
					var total = $("<td  width='135px'></td>");
					$(total).html("档案总数："+json[1] + " 个");
                    var docCount = $("<td  ></td>");
                    $(docCount).html("新增档案："+json[0] + " 个(自上次登录后)");
                    $(newtr).append(total);
                    $(newtr).append(docCount);
                    $(newtable).append($(newtr));
                });
                //按区域统计，只显示柱状图
                $("#searchArea").click(function(){
                	searchArea();
                });
                function searchArea(){
                	$("#area").css("display","block");
                	$("#province").css("display","none");
                	$("#month").css("display","none");
                	$("#tesion").css("display","none");
                	if($("#searchArea").attr("id")!=null)
                		selectInsertDocPicWithTesion("areaDocPic","areaDocPicLine","各区域档案数量统计","区域","档案数量",$("#searchArea").attr("id"));
                	else
                		return false;
                }
                //按省公司统计
                $("#searchProvince").click(function(){
                	searchProvince();
                });
                function searchProvince(){
                	$("#area").css("display","none");
                	$("#province").css("display","block");
                	$("#month").css("display","none");
                	$("#tesion").css("display","none");
                	if($("#searchProvince").attr("id")!=null)
                		selectInsertDocPicWithCountry("provinceDocPic","provinceDocPicLine","工程档案数量分布图","省公司","档案数量",$("#searchProvince").attr("id"));
                	else
                		return false;
                }
                //按月份统计,只显示柱状图
                $("#searchMonth").click(function(){
                	queryMonth($("#searchMonth").attr("id"));
                });
              
                //按电压统计，只显示柱状图
                $("#searchTesion").click(function(){
                	$("#area").css("display","none");
                	$("#province").css("display","none");
                	$("#month").css("display","none");
                	$("#tesion").css("display","block");
                	if($("#searchTesion").attr("id")!=null)
                		selectInsertDocPicWithTesion("tesionDocPic","tesionDocPicLine","电压等级档案数量统计","电压等级","档案数量",$("#searchTesion").attr("id"));
                	else
                		return flase;
                });
            }
        --></script>
		<style type="text/css">
		#resizable {
			width: 150px;
			height: 100%;
			padding: 0.5em;
		}
		
		#resizable h5 {
			text-align: left;
			margin: 0;
		}
		
		.ui-resizable-helper {
			border: 2px dotted gray;
		}
		
		#fileCount {
			width: 150px;
			height: 150px;
			padding: 0.5em;
		}
		
		#fileCount h5 {
			text-align: left;
			margin: 0;
		}
		
		#userInfo {
			width: 150px;
			height: 150px;
			padding: 0.5em;
		}
		
		#userInfo h5 {
			text-align: left;
			margin: 0;
		}
		
		#picture {
			width: 150px;
			height: 150px;
			padding: 0.5em;
		}
		
		#picture h5 {
			text-align: left;
			margin: 0;
		}
		</style>
	</head>
	<body>
		<div id="mainDIV">
			<table border="0" width="100%">
				<tr>
					<td align="left" width="100%" colspan="2">
						<div class="ui-widget-content" id="picture"
							style="width: 100%; height: 100%">
							<H5 style="border-bottom: 1px dashed #5793B0; width: 75%;padding-top:15px;margin-top:5px;">
								综合统计信息
							</H5>
							<table>
								<tr>
									<td>
										<table id="insertDocCount" border="0" style="font-size: 13px;" width="350px"></table>
									</td>
								</tr>
								<tr>
									<td>
										<table id="insertProjectCount" style="font-size: 13px;" width="350px"></table>
									</td>
								</tr>
							</table>
							<h5 style="border-bottom: 1px dashed #5793B0; width: 75%;padding-top:25px;margin-bottom:5px;">
								统计信息
							</h5>
							<table width="100%" border="0"
								style="font-size: 10pt;">
								<tr>
									<td>
										<ul>
											<li style="float: left; width: 90px;" id="searchMonth" style="dispaly: none;">
												<a href="#" class = "dowbutton" style = "padding-top:4px;padding-left:12px;" >按月度统计</a>
											</li>
											<li style="float: left; width: 90px;" id="searchArea" style="dispaly: none">
												<a href="#" class = "dowbutton" style = "padding-top:4px;padding-left:12px;" >按区域统计</a>
											</li>
											<li style="float: left; width: 100px;" id="searchProvince" style="dispaly: none">
												<a href="#" class = "dowbutton" style = "padding-top:4px;padding-left:6px;" >按省公司统计</a>
											</li>
											<li style="float: left; width: 90px;" id="searchTesion" style="dispaly: none">
												<a href="#" class = "dowbutton" style = "padding-top:4px;padding-left:14px;" >按电压统计</a>
											</li>
										</ul>
									</td>
								</tr>
							</table>
							<table>
								<tr>
									<td>
										<!-- 按月份统计 仅显示柱状图  -->
										<table width="100%" border="0" id="month">
											<tr>
												<td>
													<table><tr><td>
														<div id="monthDocPic" style="margin-top: 10px;background-color:#f6f6f6;">
														</div>	
													</td><td>
														<div id="monthDocPicLine" style="margin-top: 10px;background-color:#f6f6f6;">
														</div>
													</td></tr></table>
												</td>
											</tr>
										</table>
										<!-- 按区域统计 仅显示柱状图 -->
										<table width="100%" border="0" id="area" style="display: none;">
											<tr>
												<td>
													<table><tr><td>
														<div id="areaDocPic" style="margin-top: 10px;background-color:#f6f6f6;">
														</div>	
													</td><td>
														<div id="areaDocPicLine" style="margin-top: 10px;background-color:#f6f6f6;">
														</div>
													</td></tr></table>
												</td>
											</tr>
										</table>
										<!-- 按省公司统计  -->
										<table width="100%" border="0" id="province" style="display: none;">
											<tr>
												<td>
													<table><tr><td>
														<div id="provinceDocPic" style="margin-top: 10px;background-color:#f6f6f6;">
														</div>	
													</td><td>
														<div id="provinceDocPicLine" style="margin-top: 10px;background-color:#f6f6f6;">
														</div>
													</td></tr></table>
												</td>
											</tr>
										</table>
										<!-- 按电压统计 仅显示柱状图  -->
										<table width="100%" border="0" id="tesion" style="display: none;">
											<tr>
												<td>
													<table><tr><td>
														<div id="tesionDocPic" style="margin-top: 10px;background-color:#f6f6f6;">
														</div>	
													</td><td>
														<div id="tesionDocPicLine" style="margin-top: 10px;background-color:#f6f6f6;">
														</div>
													</td></tr></table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<h5 style="border-bottom: 1px dashed #5793B0; width: 75%; padding-top:25px;margin-bottom:5px;">
								档案信息统计
							</h5>
							<div id="fileCount" style="width: 44%; float: left;margin-right:1%"
								class="ui-widget-content">
								<h5 id="cborwsedoc" class="ui-widget-header"
									style="display: none;">
									曾经浏览过的文档
								</h5>
								<h5 id="spanbrowse" class="ui-widget-header"
									style="display: none;">
									您最近没有浏览文档
								</h5>
								<table id="browseDoctable" width="100%" border="0"
									cellpadding="0" cellspacing="1" class="biaoge"
									style="text-align: left; font-Size: 12px; display: none;">
									<tr class="bgtr01" style="color: #476c89;">
										<td>文件名</td>
										<td width='130px' align='center'>浏览时间</td>
									</tr>
								</table>
							</div>
							<div id="userInfo" class="ui-widget-content"
								style="width: 30%; float: left;">
								<H5 id="loginId" class="ui-widget-header" style="display: none">
									登录信息
								</H5>
								<H5 id="myLoginInfo" class="ui-widget-header" style="display: none">
									我的信息
								</H5>
								<table width="100%" id="userInfotable" style="font-size: 12px;"
									class="biaoge" border="0" cellpadding="0" cellspacing="1">
									<tr class="bgtr01" style="color: #476c89;">
										<td>用户名</td>
										<td>登录次数</td>
										<td width='130px' align='center'>上次登录时间</td>
									</tr>
								</table>
								<table id="myloginInfotable" width="100%" style="font-size: 12px;" class="biaoge" border="0"
									cellpadding="0" cellspacing="1">
								</table>
							</div>
						</div>
					</td>
				</tr>
			</table>
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