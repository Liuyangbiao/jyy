<%@ page language="java" import="java.util.*,java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@page import="com.nsc.dem.bean.profile.TUser"%>
<%@page import="com.nsc.dem.service.system.IprofileService"%>
<%@page import="com.nsc.base.util.Component"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<%
request.setCharacterEncoding("utf-8");
TUser user =(TUser) request.getSession().getAttribute("USER_KEY");
String mId=request.getParameter("menuId");
IprofileService profileService=(IprofileService)Component.getInstance("profileService",request.getSession().getServletContext());
boolean isPrint = profileService.getProfileByauthControl(user
, mId,  "打印");
request.setAttribute("isPrint",isPrint); 
String type = request.getParameter("type");
type = type == null ? "" : type;
String paramLoginId  = request.getParameter("loginId");
paramLoginId = paramLoginId == null ? "" : paramLoginId;

String paramLoginName = request.getParameter("loginName");
paramLoginName = paramLoginName == null ? "" : paramLoginName;

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'logDisplay.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link type="text/css" rel="stylesheet" href="../css/themes/jquery-ui-1.8.11.custom.css" />
	<link type="text/css" rel="stylesheet" href="../css/themes/ui.jqgrid.css" />
	<link type="text/css" rel="stylesheet" href="../css/jquery.autocomplete.css"   />
	<link type="text/css" rel="stylesheet" href="../css/themes/ui.multiselect.css" />
	<link type="text/css" rel="stylesheet" href="../css/base.css" >
	
	<script type="text/javascript" src="../js/jquery-1.5.min.js" ></script>
	<script type="text/javascript" src="../js/jquery.cookie.js" ></script>
    <script type="text/javascript" src="../js/jquery.jqGrid.js" ></script> 
	<script type="text/javascript" src="../js/jquery-ui-1.8.10.custom.min.js"></script>
	<script type="text/javascript" src="../js/jquery.autocomplete.js"></script>	
	<script type="text/javascript" src="../js/jquery.ui.datepicker-zh-CN.js"></script>
	<script type="text/javascript" src="<%=path %>/js/jquery.maskedinput.js" ></script>
	
	<script type="text/javascript" src="../js/popShortcut.js"></script>
	<script type="text/javascript">
	$(function() {
		var mId="${param.menuId}";
		imgDisplay(mId);
		
	
		//
	});
	
	function print(id){
		document.all.iname.src="../main/report.jsp?sort="+id;
	}
	function print2(id){
		document.all.iname2.src="../main/report.jsp?sort="+id;
	}

	function printCallBack(title){
		title.attr("innerText","系统日志报表");
		var v=jQuery("#sysLogTable").getRowData();
		//给表格添加第一列
	     var newtr = $("<tr></tr>");
	     $(newtr).attr("class","bgtr01");
	     var td="";
	     td="<td width='100'>日志编号</td><td width='140'>时间</td><td width='100'>分类</td><td width='120'>操作人</td><td width='100'>操作对象</td><td width='*%'>操作内容</td>";
	       $(newtr).append($(td));
	       iname.addRow(newtr);
	     for(var i=0;i<v.length;i++){
	     	newtr = $("<tr></tr>");
	 			var	td="<td cellspacing='1'>"+ v[i].id +"</td><td>"+ v[i].time +"</td><td>"+ v[i].type +"</td>"+
	 			"<td>"+ v[i].opertor +"</td><td>"+ v[i].operteDate +"</td><td>"+ v[i].operateContent +"</td>";
	 			 $(newtr).append($(td));
	              iname.addRow(newtr);
	 				}
	        	 iname.focus();
	        	 iname.print();
			}
	function printCallBack2(title){
		title.attr("innerText","档案日志报表");
		var v=jQuery("#recordLogTable").getRowData();
		//给表格添加第一列
	     var newtr = $("<tr></tr>");
	     $(newtr).attr("class","bgtr01");
	     var td="";
	    
	     td="<td width='100'>日志编号</td><td width='140'>时间</td><td width='100'>分类</td><td width='120'>操作人</td><td width='100'>操作对象</td><td width='*%'>操作内容</td>";
	       $(newtr).append($(td));
	       iname2.addRow(newtr); 
	     for(var i=0;i<v.length;i++){
	     	newtr = $("<tr></tr>");
	 			var	td="<td cellspacing='1'>"+ v[i].id +"</td><td>"+ v[i].time +"</td><td>"+ v[i].type +"</td>"+
	 			"<td>"+ v[i].opertor +"</td><td>"+ v[i].operteDate +"</td><td>"+ v[i].operateContent +"</td>";
	 			 $(newtr).append($(td));
	 			iname2.addRow(newtr);
	 				}
	            iname2.focus();
	            iname2.print();
			}
	</script>
	<script type="text/javascript">
		$(function() {
			//Tab表格
			$( "#tabs" ).tabs({
				cookie: {
					// store cookie for a day, without, it would be a session cookie
					expires: 1
				}
			});

			 $("#from").mask("2999"+"-"+"99"+"-"+"99");
			 $("#to").mask("2999"+"-"+"99"+"-"+"99");
		     $("#from2").mask("2999"+"-"+"99"+"-"+"99");
			 $("#to2").mask("2999"+"-"+"99"+"-"+"99");
			//时间选择
			$("#datepicker").datepicker($.datepicker.regional["zh-CN"]);
			var dates = $( "#from, #to" ).datepicker({
	//			defaultDate: "+1w",
				changeMonth: true,
				numberOfMonths: 1,  //显示多少个月
				onSelect: function( selectedDate ) {
					var option = this.id == "from" ? "minDate" : "maxDate",
						instance = $( this ).data( "datepicker" ),
						date = $.datepicker.parseDate(
							instance.settings.dateFormat ||
							$.datepicker._defaults.dateFormat,
							selectedDate, instance.settings );
					dates.not( this ).datepicker( "option", option, date );
				}
			});

			var dates2 = $( "#from2, #to2" ).datepicker({
		//		defaultDate: "+1w",
				changeMonth: true,
				numberOfMonths: 1,  //显示多少个月
				onSelect: function( selectedDate ) {
					var option = this.id == "from2" ? "minDate" : "maxDate",
						instance = $( this ).data( "datepicker" ),
						date = $.datepicker.parseDate(
							instance.settings.dateFormat ||
							$.datepicker._defaults.dateFormat,
							selectedDate, instance.settings );
					dates2.not( this ).datepicker( "option", option, date );
				}
			});

			//查询日志分类
			$.getJSON('<%=path %>/system/typeLogLogOperate.action?time='+Math.random(),{ id:'' }, function(json) {
				typeLog(json,"typeLog");
	        	}
	        );
			//查询日志分类
			$.getJSON('<%=path %>/system/typeLog2LogOperate.action?time='+Math.random(),{ id:'' }, function(json) {
				typeLog(json,"typeLog2");
	        	}
	        );
           //日志分类显示
			function typeLog(json,id){
				var s_root=document.getElementById(id);   
				s_root.options.length=0;  
				var option = document.createElement("option"); 
		            option.text="   ---所有---  ";   
		            option.value="";   
		            s_root.options[s_root.options.length] =option;
					for(var i in json){   
				        var value=json[i].code;
				        var text=json[i].name;  
				        if(text==undefined){
				        continue;
				        } 
				        var option = document.createElement("option"); 
				            option.text=text;   
				            option.value=value;   
				            s_root.options[s_root.options.length] =option;
				    }

					var paramtype = "<%=type%>";
					if(paramtype.length!=0){
						$("#typeLog2").attr("value",paramtype);
						}
			}

           //操作人
			$.getJSON('<%=path %>/system/opertorLogOperate.action?time='+Math.random(),{ id:''}, function(json) {
	        	  initAutoComplete(json,"#operator");
	        	} );
        	
			 //操作人
			$.getJSON('<%=path %>/system/opertorLogOperate.action?time='+Math.random(),{ id:''}, function(json) {
	        	  initAutoComplete(json,"#operator2");
	        	});

			var from=$("#from2").val();
			var to=$("#to2").val();
			var tl="<%=type%>"
			var oc="<%=paramLoginId%>";
		
			var content=$("#content2").val();
			content=escape(encodeURIComponent(content));
			var vals="["+","+from+","+to+","+tl+","+oc+","+content+",]";
			if(tl.length==0){
				var newurl ="<%= path%>/system/displayLogOperate.action?flag=doc";
				logtable(newurl);
			}else {
				var tmpurl = "<%= path%>/system/displayLogOperate.action?flag=doc&vals="+vals;
				logtable(tmpurl);
				}
		

			
    	
        	
		});

		//自动补全
		function initAutoComplete(json,valId)
			{
				$(valId).autocomplete(json, {
				    max: 10, //列表里的条目数
				    minChars: 0, //自动完成激活之前填入的最小字符,为0双击弹出列表
				    matchCase:false,//不区分大小写
				    width: 155, //提示的宽度，溢出隐藏
				    scrollHeight: 300, //提示的高度，溢出显示滚动条
				    matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
				    autoFill: false, //自动填充 ，就是在文本框中自动填充符合条件的项目
				    mustMatch: true, //自动匹配，mustMatch表示必须匹配条目，也就是在文本框里输入的内容，必须是data参数里的数据，如果不匹配，文本框就被清空
				    formatItem: function(row, i, max, term) {
					var v = $(valId).val(); 
					
					return  row.name;
				      //  return i + '/' + max + ':' + "<I >" + row.name +"</I>"+ "<span style='display:none;'>"+ '"[' + row.id + ']' +"</span>";
				        //formatItem作用在于可以格式化列表中的条目，比如我们加了“I”，让列表里的字显示出了斜体
				        if(row.name.indexOf(v) == 0 || row.spell.indexOf(v)==0)
						{
							return  row.name + " (" + row.spell + ")";
						}
						else
							return false;
			    },
			    formatMatch: function(row, i, max) {
			  		 //return row.name + row.id;
			  		 return  row.name + " (" + row.spell + ")";
			        //formatMatch是配合formatItem使用，作用在于，由于使用了formatItem，所以条目中的内容有所改变，而我们要匹配的是原始的数据，所以用formatMatch做一个调整，使之匹配原始数据
			    },
			    formatResult: function(row) {
		    		 return row.name;
			        //formatResult是定义最终返回的数据，比如我们还是要返回原始数据，而不是formatItem过的数据
			    }
			    }).result(function(event, row, formatted) {
			    	     if(row!=undefined || row!=null){
			    	    	 $(valId+"Code").attr("value", row.code);
			    	     }else{
			    	    	 $(valId+"Code").attr("value", "");
			    	     }
			    });

				var paramLoginId = "<%=paramLoginId%>";
				var paramLoginName = "<%=paramLoginName%>";
				
				if(paramLoginId.length!=0){
			
					$("#operator2Code").attr("value",paramLoginId);
		        	$("#operator2").attr("value",paramLoginName);
				}
	        	
		    }
	</script>
	
	<script type="text/javascript">
        //系统日志  分页查询
		function sysLogWin(){
			var from=$("#from").val();
			var to=$("#to").val();
			var tl=$("#typeLog").val();
			var oc=$("#operatorCode").val();
			var content=$("#content").val();
			content=escape(encodeURIComponent(content));
			var vals="["+","+from+","+to+","+tl+","+oc+","+content+",]";
		
			 $("#sysLogTable").clearGridData();
			jQuery("#sysLogTable").jqGrid('setGridParam',{url:"<%= path%>/system/displayLogOperate.action?flag=sys&vals="+vals}).trigger("reloadGrid");
		}
		
		var tempurl = "<%= path%>/system/displayLogOperate.action?flag=sys";
	     $(document).ready(function(){
	    	 $("#sysLogTable").jqGrid({
	             Search: true,
	             url: tempurl,
	             datatype: "json",
	             colNames: ['日志编号', '日志分类', '操作人', '操作对象', '时间', '操作内容'],
	             colModel: [ {
	                 name: 'id',
	                 index: 'id',
	                 width: 180,
	                 align: 'center'
	             },  {
	                 name: 'type',
	                 index: 'type',
	                 width: 140,
	                 align: 'center'
	             }, {
	                 name: 'opertor',
	                 index: 'opertor',
	                 width: 100,
	                 align: 'center'
	             }, {
	                 name: 'operteDate',
	                 index: 'operteDate',
	                 width: 140,
	                 align: 'center'
	             },{
	                 name: 'time',
	                 index: 'time',
	                 width: 140,
	                 align: 'center'
	             },{
	                 name: 'operateContent',
	                 index: 'operateContent',
	                 width: 230,
	                 align: 'center'
	             }],
	             rowNum:10,  
	             multiselect: true,
	             caption: "系统日志",
	             
	             forceFit: true,// 是否超出DIV
	             pager: '#prowed2',
	             viewrecords: true,// 是否显示多少行
	             multiselectWidth:'40',// 调整选择的宽度
	             emptyrecords: '数据为空',// 空记录时的提示信息
	             sortable:false,//是否支持排序,
	             loadtext:"正在加载数据，请稍候……",
	             width:'750',  
	             jsonReader: {
	                 root: "rows",
	                 page: "page",
	                 total: "total",
	                 records: "records",
	                 repeatitems: true,
	                 cell: "cell",
	                 id: "id"
	             },
	             onSelectRow: function(id){      
	              //   alert("第"+id+"行被选中");     
	             }
	         });
		 });
     </script>
	 
	 <script type="text/javascript">
        //系统日志  分页查询
		function recordLogWin(){
			var from=$("#from2").val();
			var to=$("#to2").val();
			var tl=$("#typeLog2").val();
			var oc=$("#operator2Code").val();
			var content=$("#content2").val();
			content=escape(encodeURIComponent(content));
			var vals="["+","+from+","+to+","+tl+","+oc+","+content+",]";
		
			 $("#recordLogTable").clearGridData();
			jQuery("#recordLogTable").jqGrid('setGridParam',{url:"<%= path%>/system/displayLogOperate.action?flag=doc&vals="+vals}).trigger("reloadGrid");
		}
		
		
	   

		 function logtable(tempurl){
			
			 $("#recordLogTable").jqGrid({
	             Search: true,
	             url: tempurl,
	             datatype: "json",
	             colNames: ['日志编号', '日志分类', '操作人', '操作对象', '时间', '操作内容'],
	             colModel: [ {
	                 name: 'id',
	                 index: 'id',
	                 width: 180,
	                 align: 'center'
	             },{
	                 name: 'type',
	                 index: 'type',
	                 width: 140,
	                 align: 'center'
	             }, {
	                 name: 'opertor',
	                 index: 'opertor',
	                 width: 100,
	                 align: 'center'
	             }, {
	                 name: 'operteDate',
	                 index: 'operteDate',
	                 width: 130,
	                 align: 'center'
	             },{
	                 name: 'time',
	                 index: 'time',
	                 width: 130,
	                 align: 'center'
	             }, {
	                 name: 'operateContent',
	                 index: 'operateContent',
	                 width: 250,
	                 align: 'center'
	             }],
	             rowNum:10,  
	             multiselect: true,
	             caption: "档案日志",
	             forceFit: true,// 是否超出DIV
	             pager: '#prowed4',
	             viewrecords: true,// 是否显示多少行
	             multiselectWidth:'40',// 调整选择的宽度
	             emptyrecords: '数据为空',// 空记录时的提示信息
	             sortable:false,//是否支持排序,
	             loadtext:"正在加载数据，请稍候……",
	             width:'760',  
	             jsonReader: {
	                 root: "rows",
	                 page: "page",
	                 total: "total",
	                 records: "records",
	                 repeatitems: true,
	                 cell: "cell",
	                 id: "id"
	             },
	             onSelectRow: function(id){      
	              //   alert("第"+id+"行被选中");     
	             }
	         });
	 }
		 function onbluroperator2(){
				var operator2 = $("#operator2").val();
				if(operator2.length==0){
					 $("#operator2Code").attr("value","");
				}
			 }
		 function onbluroperator(){
			 var operator = $("#operator").val();
				if(operator.length==0){
					 $("#operatorCode").attr("value","");
				}
			 }
     </script>
	<style type="text/css">
			 .ui-jqgrid tr.jqgrow td {
				    white-space: normal !important;
				    height:auto;
				    vertical-align:text-top;
				    padding-top:2px;
				}
			/*demo page css*/
			body{ font-size: 12px; }
			.ui-datepicker SELECT.ui-datepicker-year{width: 58px;}
			.ui-datepicker SELECT.ui-datepicker-month{width: 63px;}
			.ui-state-default {font-size: 11px;}
			.ui-datepicker {width: 15em;}
			.ui-datepicker-calendar th {padding: 0.3em;}
		</style>
  </head>
  
  <body>
     <!-- Tabs -->
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2">
		<tr>
		    <td class="conttop2"><div class="top2left"></div>
		      <div class="top2right"></div>
		      <div class="top2middle">
		        <span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif" onClick="topwin(${param.menuId })" style="cursor:hand"></a></span>
		        <span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span>
		        <span class="topico2"><img src="../images/Accordion_ico03.gif"></span>
		        <h3>日志查看</h3>
		      </div></td>
		  </tr>
		  <tr>
		    <td class="contmiddle2">
		     <table width="750" border="0" cellspacing="0" cellpadding="0"  style="font-size:12px; font-weight:bold;">
		     <tr>
              <td>
		      <div id="tabs" align="center">
				<ul>
					<li><a href="#tabs-1">系统日志</a></li>
					<li><a href="#tabs-2">档案日志</a></li>
				</ul>
				<div id="tabs-1">
                      <table width="750" border="0" cellpadding="0" cellspacing="5" style="font-size:14px;">
						    <tr>
						       <td width="132" align="right">
						       	  <strong style="font-size:12px;">	操作日期:           </strong>                     </td>
                               <td width="254"><input type="text" id="from" name="from" size=10/>
                              	  <strong style="font-size:12px;">  到                                               </strong>
                               <input type="text" id="to" name="to" size=10 /></td>
                               <td width="80" align="right">
                                  <strong style="font-size:12px;">     日志分类:          </strong>                    </td>
                               <td width="259"><select name="typeLog" id="typeLog">
                               </select>                               </td>
						    </tr>
						    <tr>
							   <td align="right">
						     	<strong style="font-size:12px;">     操作人: </strong> </td>
							   <td><input type="text" name="operator" id="operator"  onblur="onbluroperator()"/>
                                 <input type="hidden" name="operatorCode" id="operatorCode" /></td>
							   <td align="right">
							 	<strong style="font-size:12px;">	 内容过滤:				</strong>			   </td>
						       <td><input type="text" name="content" id="content" /></td>
						    </tr>
						    <tr>
						    	<td colspan="4" align="center">
						    	    <button onClick="sysLogWin()" class="chaxunbutton"><span style="font-size:12px;">查询</span></button>
           					    	</td>
					    	</tr>
					  </table>
                      
                <iframe id="iname" name="iname" height="0" width="0"></iframe>
                <table width="750" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td height="22" align="right">
                          <c:if test="${isPrint}">
                          <button onClick="print(1)" class="delbutton" ><span style="font-size:12px;">打印</span></button>
                          </c:if>	</td>
                          
                        </tr>
                </table>
                <div class="bianxian"></div>
            	 <div>
		            <br/>
		            <table id="sysLogTable" width="100%"></table>
		            <div id="prowed4"></div>
		         </div>
            </div>
			<div id="tabs-2">
			      <table width="750" border="0" cellpadding="0" cellspacing="5" style="font-size:12px;">
						    <tr>
						       <td width="132" align="right">
						       	  <strong style="font-size:12px;">	操作日期:           </strong>                     </td>
                               <td width="254"><input type="text" id="from2" name="from2" size=10 />
                              	  <strong style="font-size:12px;">  到                                               </strong>
                               <input type="text" id="to2" name="to2" size=10 /></td>
                               <td width="80" align="right">
                                  <strong style="font-size:12px;">     日志分类:          </strong>                    </td>
                               <td width="259"><select name="typeLog2" id="typeLog2">
                               </select>                               </td>
						    </tr>
						    <tr>
							   <td align="right">
						     	<strong style="font-size:12px;">     操作人: </strong> </td>
							   <td><input type="text" name="operator2" id="operator2"  onblur="onbluroperator2()"/>
                                 <input type="hidden" name="operator2Code" id="operator2Code" /></td>
							   <td align="right">
							 	<strong style="font-size:12px;">	 内容过滤:				</strong>			   </td>
						       <td><input type="text" name="content2" id="content2" /></td>
						    </tr>
						    <tr>
						    	<td colspan="4" align="center">
						    	    <button onClick="recordLogWin()" class="chaxunbutton"><span style="font-size:14px;">查询</span></button>
           					    	</td>
					    	</tr>
					  </table>
                      
                <iframe id="iname2" name="iname2" height="0" width="0"></iframe>
                <table width="750" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td height="22" align="right">
                          
                          <c:if test="${isPrint}"><button onClick="print2(2)" class="delbutton" ><span style="font-size:12px;">打印</span></button>
                          </c:if>	</td>
                        </tr>
                </table>
                <div class="bianxian"></div>
            	 <div>
		            <br/>
		            <table id="recordLogTable" width="100%"></table>
		            <div id="prowed2"></div>
		         </div>
			</div>
		</div>
	</table>
	 </td>
		  </tr>
		  <tr>
		    <td class="contbottom2"><div class="bottom2left"></div>
		      <div class="bottom2right"></div>
		      <div class="bottom2middle"></div></td>
		  </tr>
	</table>
  </body>
</html>
