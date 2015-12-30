<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'fileUpdate.jsp' starting page</title>
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

	 <script type="text/javascript" src="<%=path %>/js/jquery-1.5.min.js" ></script>
     <script type="text/javascript" src="<%=path %>/js/jquery.jqGrid.js" ></script> 
	 <script type="text/javascript" src="<%=path %>/js/jquery-ui-1.8.10.custom.min.js"></script>
	 <script type="text/javascript" src="<%=path %>/js/jquery.ui.datepicker-zh-CN.js"></script>
	 <script type="text/javascript" src="<%=path %>/js/jquery.autocomplete.js"></script>
	 <script type="text/javascript" src="<%=path %>/js/jquery.timepicker.js"></script>
	 <script type="text/javascript" src="<%=path %>/js/jquery.maskedinput.js" ></script>
	 <script type="text/javascript" src="<%=path %>/js/jquery.form.js" ></script>
	 <script type="text/javascript" src="<%=path %>/js/popShortcut.js"></script>
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
	
	<script type="text/javascript">

		$(function() {
			var mId="${param.menuId}";
			imgDisplay(mId);
		});

		function selected(){
			var selectAll=document.getElementById("selectAll");
			for(i=0;i<document.all("format").length;i++)
			{
				if(document.all("format")[i].checked==false){
					selectAll.checked = false;
				}
			}
		}
		
	     function update(tdocID,projectId,docTypeId){
	    	 window.location.href="<%=path %>/archives/initUpdateFileUpdate.action?tdocID="+tdocID+"&projectId="+projectId+"&docTypeId="+docTypeId;
	     }
	
	     function queryResult(){
			    $("#rowedtable").clearGridData();
			    jQuery("#rowedtable").jqGrid('setGridParam',{url:"<%=path %>/archives/displayResultFileUpdate.action?" + $('#myForm').formSerialize()}).trigger("reloadGrid");
			}
	
		 var tempurl = "<%= path%>/archives/displayResultFileUpdate.action?flag=jump";
		 $(document).ready(function(){
	         $("#rowedtable").jqGrid({
	             Search: true,
	             url: tempurl,
	             datatype: "json",
	             colNames: [ '工程名称', '文件名称', '文档分类','文件状态', '密级分类', '格式', '录入人', '录入时间','操作'],
	             colModel: [{
	                 id: 'projectName',
	                 name: 'projectName',
	                 index: 'projectName',
	                 align: 'left'
	             }, {
	                 id: 'fileName',
	                 name: 'fileName',
	                 index: 'fileName',
	                 align: 'left'
	             }, {
	                 name: 'docType',
	                 index: 'docType',
	                 align: 'center'
	             }, {
	                 name: 'fileStatus',
	                 index:'fileStatus',
	                 align: 'center'
	             },{
	                 name: 'security',
	                 index: 'security',
	                 align: 'center'
	             }, {
	                 name: 'fileFormat',
	                 index: 'fileFormat',
	                 align: 'center'
	             },{
	                 name: 'creator',
	                 index: 'creator',
	                 align: 'center'
	             },{
	                 name: 'createTime',
	                 index: 'createTime',
	                 align: 'center'
	             }, {
	                 name: 'operate',
	                 index: 'operate',
	                 align: 'center',
	                 editable: true
	             }],
	             rowNum:10,  
	             caption: "档案更新",
	             forceFit: true,// 是否超出DIV
	             pager: '#prowed4',
	             viewrecords: true,// 是否显示多少行
	             multiselectWidth:'40',// 调整选择的宽度
	             emptyrecords: '数据为空',// 空记录时的提示信息
	             sortable:false,//是否支持排序,
	             loadtext:"正在加载数据，请稍候……",
	             height: 'auto' ,  
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
	               //  alert("第"+id+"行被选中");     
	             }
	         });
	     });
     
     </script>
     
	<script type="text/javascript">
		$(function(){
			$("#from").mask("2999"+"-"+"99"+"-"+"99");
			$("#to").mask("2999"+"-"+"99"+"-"+"99");
			$.datepicker.setDefaults( $.datepicker.regional[ "zh-CN" ] );
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
			
			function initAutoComplete(json,valId,pn)
			{
				$(valId).autocomplete(json, {
				    max: 10, //列表里的条目数
				    minChars: 0, //自动完成激活之前填入的最小字符,为0双击弹出列表
				    matchCase:false,//不区分大小写
				    width: 180, //提示的宽度，溢出隐藏
				    scrollHeight: 300, //提示的高度，溢出显示滚动条
				    matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
				    autoFill: false, //自动填充 ，就是在文本框中自动填充符合条件的项目
				    mustMatch: true, //自动匹配，mustMatch表示必须匹配条目，也就是在文本框里输入的内容，必须是data参数里的数据，如果不匹配，文本框就被清空
				    formatItem: function(row, i, max, term) {
					var v = $(valId).val(); 
					return  row.name ;
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
			    	if(row.name==pn){
			  			$(valId).attr("value",pn);
			  			$(valId+"Code").attr("value", row.id);
			  		 }
			  		 return  row.name + " (" + row.spell + ")";
			        //formatMatch是配合formatItem使用，作用在于，由于使用了formatItem，所以条目中的内容有所改变，而我们要匹配的是原始的数据，所以用formatMatch做一个调整，使之匹配原始数据
			    },
			    formatResult: function(row) {
			    	 
		    		 return row.name;
			        //formatResult是定义最终返回的数据，比如我们还是要返回原始数据，而不是formatItem过的数据
			    }
			    }).result(function(event, row, formatted) {
		    	    
			    	if(row=="undefined" || row==null){
		   	    		 $(valId+"Code").attr("value", "");
			    	     }else{
			    	    	 $(valId+"Code").attr("value", row.id);
			    	     } 
			    
			    	$(valId+"Code").trigger("change");
			    });
		    }

			 //文档类型
			 //请注意  文档类型这里  是否该用doc()  还是 recordType() 还有待考证 
	        $.getJSON('<%=path %>/search/recordTypeBasic.action?time='+Math.random(),{ id:'' }, function(json) {
	        	  initAutoComplete(json,"#docType","");
	        	}
	        );
	      //工程名称
	      	$.getJSON('<%=path %>/search/projectNameBasic.action?time='+Math.random(),{ id:'' }, function(json) {
		        	  initAutoComplete(json,"#projectName","");
		        	}
		       );
	      //上传人
	        $.getJSON('<%=path %>/system/opertorLogOperate.action?time='+Math.random(),{ id:''}, function(json) {
	        	  initAutoComplete(json,"#creator","");
	        	}
        	 );
	      //保密
	       	var baomispan = $("#baomispan").text();
    		 $.getJSON('../system/dictionaryType.action',{
    	        paramValue:baomispan},
    	        function(json){
    			 var baomi = "<option value=''>无</option>";
    			 for(var i = 0;i<json.length;i++){
    				 baomi += "<option value=\"" + json[i].id + "\" >"
    				 + json[i].name + "</option>";
    			 }
    			 $("#dftSecurity").html(baomi)
    	     });
	      //文件格式docFormatspan
            $.getJSON('../system/dictionaryType.action', {
                paramValue: $("#docFormatspan").text()
            }, function(json){
                var table = $("#formattable");
                var row=-1;
                var tr;
                for (var i= 0 ;i<json.length;i++) {
                    var value = json[i].id;
                    var text = json[i].name;
                    if (text == undefined) {
                        continue;
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
			var selectAll=document.getElementById("selectAll");
			if (selectAll.checked == true){
				for(i=0;i<document.all("format").length;i++)
				{
					document.all("format")[i].checked=true;
				} 
			}else{
				for(i=0;i<document.all("format").length;i++)
				{
					document.all("format")[i].checked=false;
				} 
			}
		}
	</script>
	
  </head>
  
  <body>
     <table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2">
            <tr>
                <td class="conttop2">
                    <div class="top2left"></div>
                    <div class="top2right"></div>
                    <div class="top2middle">
                       <span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif" onclick="topwin(${param.menuId })" style="cursor:hand"></a></span>
	             	   <span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span>
                       <span class="topico2"><img src="../images/Accordion_ico03.gif"></span><h3>档案更新</h3>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="contmiddle2">
	                <form action="<%=path %>/search/resultBasicSearch.action" method="post" id="myForm" name="myForm">
			    	<table width="750" border="0" cellspacing="0" cellpadding="0" class="conttitle">
	           			 <tr>
	               			 <td height="22"><h3>查询条件</h3></td>
	            		 </tr>
	         		</table>
	        		<table width="750" border="0" cellpadding="0" cellspacing="2" style="font-size:12px; font-weight:bold;">
					      <tr>
					         <td>文件名</td>
			         		 <td><input type="text" id="fileName" name="fileName" size="20"/></td>
					         <td align="center">文档分类</td><td><input type="text" id="docType" name="docType" /></td>
					         <td width="10%">工程名称</td>
				           	  <td><input type="text" id="projectName" name="projectName" size="20"/></td>
					      </tr>
			    		  <tr>
			    		     <td width="9%">录入单位</td>
		           			 <td  width="12%"><input type="text" id="unname" name="unname" size="20"/></td>
			               	 <td align="center"><span id="baomispan">密级分类</span></td>
			               	 <td bgcolor="#EDF1F6" style="padding:5px;">
										<select name="dftSecurity" id="dftSecurity">
				  						</select>										
				  			 </td>
				  			 <td>创建者</td><td><input name="creator" type="text" id="creator" size="20"/></td>
			      		  </tr>
		     		</table>
	        		<table width="750" border="0" cellpadding="0" cellspacing="2" style="font-size:12px; font-weight:bold;">
		           		<tr>
		           		    <td width="52"><span id="docFormatspan">文件格式</span></td>
		           		     <td width="340">
		           		         <input type="checkbox" name="selectAll" checked id="selectAll" onClick="checkthis()"/>全选 <br/>
					        	 <table border="0" cellspacing="0" cellpadding="0" id="formattable">
					        	 </table>  
			               	 </td>
				            <td width="9%">录入日期</td>
				            <td width="30%">
				          	   <div class="demo">
				          	       <input type="text" id="from" name="from" size="10"/>到
				                   <input type="text" id="to" name="to" size="10"/>
				               </div>
				            </td>
			          	</tr>
			          	<tr>
			          	   <td>文件状态</td>
			          	   <td>
			          	       <select name="fileStatus" id="fileStatus">
			          	           <!-- <option value="0">--所有--</option>  -->
			          	           <option value="03">待归档</option>
			          	           <option value="01">归档</option>
				  			   </select>
				  		   </td>
			          	   <td> </td>
				           <td>
				               <input type="button" value="查询" class="querenbutton" onclick="queryResult();"/>
				               <input type="reset" value="清空" class="querenbutton" style = "margin-left:20px;" />
				            </td>
			          	</tr>
	        	    </table>
	                <div class="bianxian" style="width:750px;"></div>
	     
				    <span style="display: none;">
						      工程名称<input type="text" id="projectNameCode" name="projectNameCode" value="${requestScope.projectNameCode}"/><br>
						      文档类型<input type="text" id="docTypeCode" name="docTypeCode" value="${requestScope.docTypeCode}"/><br>
						      上传者Code<input type="text" id="creatorCode" name="creatorCode" value="${requestScope.creatorCode}"/><br>
					 </span>  
	         		 </form>
	          		 <div style="width:750px;">
			           	 <table id="rowedtable" width="100%"></table>
			             <div id="prowed4"></div>
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
  </body>
</html>
