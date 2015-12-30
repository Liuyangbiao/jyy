<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.nsc.dem.bean.profile.TUser"%>
<%@page import="com.nsc.dem.service.system.IprofileService"%>
<%@page import="com.nsc.base.util.Component"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; %>
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
		<title>My JSP 'basicSearchResult.jsp' starting page</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="<%=path %>/css/base.css" />
		<link rel="stylesheet" type="text/css" href="<%=path %>/css/jquery.autocomplete.css" />
		<link rel="stylesheet" type="text/css" href="<%=path %>/css/themes/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" href="<%=path %>/css/themes/ui.multiselect.css" />
		<link rel="stylesheet" type="text/css" href="<%=path %>/css/themes/jquery-ui-1.8.11.custom.css" />
		<script type="text/javascript" src="<%=path %>/js/jquery-1.5.min.js"></script>
		<script type="text/javascript" src="<%=path %>/js/jquery-ui-1.8.10.custom.min.js"></script>
		<script type="text/javascript" src="../archives/js/docDetails.js"></script>
		<script type="text/javascript" src="<%=path %>/js/jquery.jqGrid.js"></script>
		<script type="text/javascript" src="<%=path %>/js/jquery.ui.datepicker-zh-CN.js"></script>
		<script type="text/javascript"src="<%=path %>/js/jquery.autocomplete.js"></script>
		<script type="text/javascript" src="<%=path %>/js/jquery.maskedinput.js"></script>
		<script type="text/javascript" src="<%=path %>/js/jquery.timepicker.js"></script>
		<script type="text/javascript" src="<%=path %>/js/jquery.form.js"></script>
		<script type="text/javascript" src="../js/jquery.cookie.js"></script>
		<script type="text/javascript" src="js/download.js" charset="utf-8"></script>
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
                $("#onLineSeeDIV").hide();
                docInfoDialog();
                    $.ajaxSetup({
      			 		 cache: false
   					 });
                $('#dialog').dialog({
                
                    width: 600,
                    autoOpen: false,//打开模式窗体
                    resizable: true,//是否可以调整模式窗体的大小
                    draggable: true,//是否可以拖动模式窗体
                    modal: true,//启用模式窗体
                    closeOnEscape: true,//按下esc可以退出
                    close: function(){
                        $("#preimge").attr("src", "../images/circle_animation.gif");
                    }
                });
            });

        
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
            
            //点击预览时触发的函数
            function previewImage(id){
                $("#preimge").attr("src", "getPreView.action?id=" + id);
                $('#dialog').dialog('open');
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
            

            function goBack(){
                window.history.back(-1);
            }
            
            function queryResult(){
                $("#rowedtable").clearGridData();
                jQuery("#rowedtable").jqGrid('setGridParam', {
                    url: "inFormationBasicSearch.action?" + $('#myForm').formSerialize()+"&menuId=${menuId}"
                }).trigger("reloadGrid");
            }
            
            var tempurl = "inFormationBasicSearch.action?flag=jump&menuId=${menuId}";
            $(document).ready(function(){
            
                $("#rowedtable").jqGrid({
                    Search: true,
                    url: tempurl,
                    datatype: "json",
                    colNames: ['文件名称', '档案类别', '工程名称', '格式', '上传时间', '文件大小', '操作'],
                    colModel: [{
                        id: 'filename',
                        name: 'filename',
                        index: 'filename',
                        align: 'left'
                    }, {
                        id: 'filetype',
                        name: 'filetype',
                        index: 'filetype',
                         align: 'center'
                    }, {
                        name: 'worksname',
                        index: 'worksname',
                        
                        align: 'left'
                    }, {
                        name: 'style',
                        index: 'style',

                        align: 'center'
                    }, {
                        name: 'upload',
                        index: 'upload',
         
                        align: 'center'
                    }, {
                        name: 'filesize',
                        index: 'filesize',
     
                        align: 'center'
                    }, {
                        name: 'operate',
                        index: 'operate',
    
                        align: 'center'
                    }],
                    rowNum: 10,
                    multiselect: true,
                    caption: "档案查询",
                    forceFit: true,// 是否超出DIV
                    pager: '#prowed4',
                    viewrecords: true,// 是否显示多少行
                    multiselectWidth: '40',// 调整选择的宽度
                    emptyrecords: '数据为空',// 空记录时的提示信息
                    sortable: false,//是否支持排序,
                    loadtext: "正在加载数据，请稍候……",
                    height: 'auto',
                    width: '750',
                    jsonReader: {
                        root: "rows",
                        page: "page",
                        total: "total",
                        records: "records",
                        repeatitems: true,
                        cell: "cell",
                        id: "id",
                        flag: "jump"
                    },
                    onSelectRow: function(id){
                        //  alert("第"+id+"行被选中");     
                    }
                    
                });
            });
        --></script>
		<script type="text/javascript">
            
            $(function(){
                $("#from").mask("2999" + "-" + "99" + "-" + "99");
                $("#to").mask("2999" + "-" + "99" + "-" + "99");
                $("#datepicker").datepicker($.datepicker.regional["zh-CN"]);
                var dates = $("#from, #to").datepicker({
                    //				defaultDate: "+1w",
                    changeMonth: true,
                    numberOfMonths: 1, //显示多少个月
                    onSelect: function(selectedDate){
                        var option = this.id == "from" ? "minDate" : "maxDate", instance = $(this).data("datepicker"), date = $.datepicker.parseDate(instance.settings.dateFormat ||
                        $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
                        dates.not(this).datepicker("option", option, date);
                    }
                });
                //文件格式
              //文件格式
		        $.getJSON('../system/dictionaryType.action',{ paramValue:$("#doctypespan").text() }, function(json) {
		        		var f="[,"+"${format}"+",]";
		        		var sf=f.split(",");
		        		var table = $("#formattable");
		                var row=-1;
		                var tr;
		                for (var i= 0 ;i<json.length;i++) {
				        	var flag=false;
					        var value=json[i].id;
					        var text=json[i].name;  
					        if(text==undefined){
					        continue;
					        } 
					        for(var j=0;j<sf.length;j++){
						        //去掉前后空格
					        	var r=sf[j].replace(/^\s+|\s+$/g,""); 
                                 if(value==r){
                                   flag=true;
                                 }
					        }
					      
					        var td=$("<td><input type='checkbox' checked name='format' onclick='selected();' value='" + value + "'/>" + text + "</td>");
							var crow =parseInt(i/5);
							//新行
							if(crow!=row ){
				                   $(table).append($(tr));
									var trid="formatCheckBox"+crow;
			     	                tr=$("<tr id='"+trid+"'></tr>");
			     	                row=crow;
								}
						   $(tr).append(td);					 
						  //最后一行
						  if((i+1)==json.length){
			                   $(table).append($(tr));
							}
					    }
		        	});
            });
            
            //是否全选文件类型
            function checkthis(){
                var selectAll = document.getElementById("selectAll");
                if (selectAll.checked == true) {
                    for (i = 0; i < document.all("format").length; i++) {
                        document.all("format")[i].checked = true;
                    }
                }
                else {
                    for (i = 0; i < document.all("format").length; i++) {
                        document.all("format")[i].checked = false;
                    }
                }
            }
            
            function selected(){
                var selectAll = document.getElementById("selectAll");
                for (i = 0; i < document.all("format").length; i++) {
                    if (document.all("format")[i].checked == false) {
                        selectAll.checked = false;
                    }
                }
            }
        </script>
		<script language="javascript">
            $(function(){
                function initAutoComplete(json, valId, pn){
                    $(valId).autocomplete(json, {
                        max: 10, //列表里的条目数
                        minChars: 0, //自动完成激活之前填入的最小字符,为0双击弹出列表
                        matchCase: false,//不区分大小写
                        width: 155, //提示的宽度，溢出隐藏
                        scrollHeight: 300, //提示的高度，溢出显示滚动条
                        matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                        autoFill: false, //自动填充 ，就是在文本框中自动填充符合条件的项目
                        mustMatch: true, //自动匹配，mustMatch表示必须匹配条目，也就是在文本框里输入的内容，必须是data参数里的数据，如果不匹配，文本框就被清空
                        formatItem: function(row, i, max, term){
                            var v = $(valId).val();
                            
                            return row.name;
                            //  return i + '/' + max + ':' + "<I >" + row.name +"</I>"+ "<span style='display:none;'>"+ '"[' + row.id + ']' +"</span>";
                            //formatItem作用在于可以格式化列表中的条目，比如我们加了“I”，让列表里的字显示出了斜体
                            if (row.name.indexOf(v) == 0 || row.spell.indexOf(v) == 0) {
                                return row.name + " (" + row.spell + ")";
                            }
                            else 
                                return false;
                        },
                        formatMatch: function(row, i, max){
                            //return row.name + row.id;
                            
                            if (row.name == pn) {
                                $(valId).attr("value", pn);
                                $(valId + "Code").attr("value", row.id);
                            }
                            return row.name + " (" + row.spell + ")";
                            //formatMatch是配合formatItem使用，作用在于，由于使用了formatItem，所以条目中的内容有所改变，而我们要匹配的是原始的数据，所以用formatMatch做一个调整，使之匹配原始数据
                        },
                        formatResult: function(row){
                        
                            return row.name;
                            //formatResult是定义最终返回的数据，比如我们还是要返回原始数据，而不是formatItem过的数据
                        }
                    }).result(function(event, row, formatted){
                    
                        if (row == "undefined" || row == null) {
                            $(valId + "Code").attr("value", "");
                        }
                        else {
                            $(valId + "Code").attr("value", row.id);
                        }
                        
                    });
                }
                
                $.getJSON('${pageContext.request.contextPath}/search/recordTypeResultBasic.action?time=' + Math.random(), {
                    id: '${requestScope.projectTypeCode}',
                    tid: '${requestScope.docTypeCode}'
                }, function(json){
                    initAutoComplete(json, "#recordType", "");
                });
                $.getJSON('${pageContext.request.contextPath}/search/projectNameBasic.action?time=' + Math.random(), {
                    tid: '${requestScope.projectTypeCode}',
                    id: '${requestScope.statussCode}'
                }, function(json){
                    initAutoComplete(json, "#projectName", "");
                });
            });

            function recordTypeOnblur(){
            	var recordType = $("#recordType").val();
            	if(recordType.length==0){
                	$("#recordTypeCode").attr("value","");
                	}
                }
        $("#button4").click(function(){alert("123");});
        </script>
	</head>
	<body>
		<div id="mainDIV">

			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				class="contert2">
				<tr>
					<td class="conttop2">
						<div class="top2left">
						</div>
						<div class="top2right">
						</div>
						<div class="top2middle">
							<span class="bottonszkj" style="cursor: hand;"
								onclick="goBack();"><img
									src="../images/fanhui_botton.gif"> </span><span class="topico2"><img
									src="../images/Accordion_ico03.gif"> </span>
							<h3>
								查询结果
							</h3>
						</div>
					</td>
				</tr>
				<tr>
					<td class="contmiddle2">
						<form action="<%=path %>/search/resultBasicSearch.action"
							method="post" id="myForm" name="myForm">
							<table width="750" border="0" cellspacing="0" cellpadding="0"
								class="conttitle">
								<tr>
									<td height="22">
										<h3>
											缩小检索范围
										</h3>
									</td>
								</tr>
							</table>
							<table width="750" border="0" cellpadding="0" cellspacing="2"
								style="font-size: 12px; font-weight: bold;">
								<tr>
									<td align="right">
										文件名
									</td>
									<td>
										<input type="text" id="fileName" name="fileName"
											value="${requestScope.fileName}" size="20" />
									</td>
									<td align="right">
										档案分类
									</td>
									<td>
										<input type="text" id="recordType" name="recordType"
											value="${requestScope.recordType}"
											onblur="recordTypeOnblur()" />
									</td>
									<td width="10%" align="right">
										工程名称
									</td>
									<td>
										<input type="text" id="projectName" name="projectName"
											value="${requestScope.projectName}" size="25"
											onblur="projectNameOnblur()" />
									</td>
								</tr>
								<tr>
									<td width="70" align="right">
										<span id="doctypespan">文件格式</span>
									</td>
									<td colspan="5">
										<input type="checkbox" name="selectAll" checked id="selectAll"
											onClick="checkthis()" />
										全选
										<br />
										<table border="0" cellspacing="0" id="formattable"
											cellpadding="0">
										</table>
									</td>
								</tr>
							</table>
							<table width="750" border="0" cellpadding="0"
								style="font-size: 12px; font-weight: bold;">
								<tr>
									<td width="6%" align="right">
										上传日期
									</td>
									<td width="44%">
										<input type="text" id="from" name="from"
											value="${requestScope.from}" size="12" />
										到
										<input type="text" id="to" name="to"
											value="${requestScope.to}" size="12" />
									</td>
									<td width="9%">
										<input type="button" value="查询" class="querenbutton"
											onclick="queryResult();" />
									</td>
									<!-- onclick="queryResult();" -->
									<td width="11%">
										<input type="reset" value="重置" class="qkbutton" />
									</td>
								</tr>
							</table>
							<div class="bianxian" style="width: 750px;">
							</div>
							<table width="150" border="0" cellspacing="5" cellpadding="0">
								<tr>
									<td>
										<c:if test="${isDownLoad}">
											<input type="button" name="button6" id="button4" value="打包下载"
												class="dowbutton" onclick="packAgeDownLoad()" />
										</c:if>
									</td>
								</tr>
							</table>
							<span style="display: none;">工程分类<input type="text"
									id="projectTypeCode" name="projectTypeCode"
									value="${requestScope.projectTypeCode}" /> <br> 工程分类名<input
									type="text" id="projectType" name="projectType"
									value="${requestScope.projectType}" /> <br> 工程阶段<input
									type="text" id="statussCode" name="statussCode"
									value="${requestScope.statussCode}" /> <br> 工程阶段名称<input
									type="text" id="statuss" name="statuss"
									value="${requestScope.statuss}" /> <br> 工程名称<input
									type="text" id="projectNameCode" name="projectNameCode"
									value="${requestScope.projectNameCode}" /> <br> 设计院 <input
									type="text" id="designUnitCode" name="designUnitCode"
									value="${requestScope.designUnitCode}" /> <br> 设计院 <input
									type="text" id="designUnit" name="designUnit"
									value="${requestScope.designUnit}" /> <br> 专业分类<input
									type="text" id="proTypeCode" name="proTypeCode"
									value="${requestScope.proTypeCode}" /> <br> 专业分类<input
									type="text" id="proType" name="proType"
									value="${requestScope.proType}" /> <br> 文档类型<input
									type="text" id="docTypeCode" name="docTypeCode"
									value="${requestScope.docTypeCode}" /> <br> 文档类型<input
									type="text" id="docType" name="docType"
									value="${requestScope.docType}" /> <br> 档案分类<input
									type="text" id="recordTypeCode" name="recordTypeCode"
									value="${requestScope.recordTypeCode}" /> <br> <!-- 高级检索   查询条件 -->文件大小判断<input
									type="text" id="fileSizeJudge" name="fileSizeJudge"
									value="${requestScope.fileSizeJudge}" /> <br> 文件大小<input
									type="text" id="fileSize" name="fileSize"
									value="${requestScope.fileSize}" /> <br> 文件大小单位<input
									type="text" id="fileSizeUnits" name="fileSizeUnits"
									value="${requestScope.fileSizeUnits}" /> <br> 上传者Code<input
									type="text" id="creatorCode" name="creatorCode"
									value="${requestScope.creatorCode}" /> <br> 上传者<input
									type="text" id="creator" name="creator"
									value="${requestScope.creator}" /> <br> 档案版本<input
									type="text" id="version" name="version"
									value="${requestScope.version}" /> <br> 电压等级<input
									type="text" id="voltageLevel" name="voltageLevel"
									value="${requestScope.voltageLevel}" /> <br> 电压等级Code<input
									type="text" id="voltageLevelCode" name="voltageLevelCode"
									value="${requestScope.voltageLevelCode}" /> <br> 初设年份<input
									type="text" id="pre_design_year" name="pre_design_year"
									value="${requestScope.pre_design_year}" /> <br> 开工年份<input
									type="text" id="open_year" name="open_year"
									value="${requestScope.open_year}" /> <br> 竣工年份<input
									type="text" id="close_year" name="close_year"
									value="${requestScope.close_year}" /> <br> 业主单位<input
									type="text" id="unname" name="unname"
									value="${requestScope.unname}" /> <br> </span>
						</form>
						<div style="width: 750px;">
							<table id="rowedtable" width="100%">
							</table>
							<div id="prowed4">
							</div>
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
							<span class="bottonszkj" style="cursor: hand;"
								onclick="goBack();"><img
									src="../images/fanhui_botton.gif"> </span>
						</div>
					</td>
				</tr>
			</table>
			<div id="dialog" title="图片预览" style="display: none;">
				<img id="preimge" src="../images/circle_animation.gif" alt="图片"
					style="width: 100%">
			</div>
			<iframe id="packAgeDownLoad"
				style="width: 200; height: 200; display: none;">
			</iframe>
			
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
			
			
			<div id="docInfo" title="详细信息">
				<div style="padding-bottom: 30px;">
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

				<div style="padding-bottom: 30px;">
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
						<li id="projectType">
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


				<div style="padding-bottom: 30px;">
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
