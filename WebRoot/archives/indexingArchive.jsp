<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>无标题文档</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link type="text/css" rel="stylesheet" href="<%=path %>/css/themes/jquery-ui-1.8.11.custom.css"/>	
	    <link type="text/css" rel="stylesheet" href="<%=path %>/css/jquery.autocomplete.css" />
	    <link type="text/css" rel="stylesheet" href="<%=path %>/css/themes/ui.multiselect.css" />
	    <link type="text/css" rel="stylesheet" href="<%=path %>/css/base.css">
		<script type="text/javascript" src="<%=path%>/js/jquery-1.5.min.js"></script>
		<script type="text/javascript" src="<%=path %>/js/jquery.cookie.js" ></script>
		<script type="text/javascript" src="<%=path %>/js/jquery-ui-1.8.10.custom.min.js"></script>
		<script type="text/javascript" src="<%=path%>/js/jquery.autocomplete.js"></script>	
		<script type="text/javascript" src="<%=path%>/js/popShortcut.js"></script>
		
		<script type="text/javascript">
		$(function() {
			var mId="${param.menuId}";
			imgDisplay(mId);
			 $.ajaxSetup({
			        cache: false
			    });
		//	 $("#indexing").css("display","none");
		});

		$(function() {
			//Tab表格
			$( "#tabs" ).tabs({
				cookie: {
					// store cookie for a day, without, it would be a session cookie
					expires: 1
				}
			});
		});
		
		function importArchives(){
			$.getJSON("importArchives.action",function(json){
					if(json=="error"){
						alert(" 同步数据失败");
						}else
					 if(json=="null"){
					    alert("同步数据已全部完成");
						 }
					 else {
						 alert("同步数据已全部完成");
						 $("#indexing").attr("value",json);
						 $("#indexing").css("display","block");
						 }
				})
			}
	  </script>
	  
	   <script language="javascript">
        $(function(){
        	function initAutoComplete(json,valId,valCode,pn)
	        	{
	        		$(valId).autocomplete(json, {
	        		    max: 10, //列表里的条目数
	        		    minChars: 0, //自动完成激活之前填入的最小字符,为0双击弹出列表
	        		    matchCase:false,//不区分大小写
	        		    width: 240, //提示的宽度，溢出隐藏
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
	        	  			$(valCode).attr("value", row.id);
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
	       	    	    		 $(valCode).attr("value", "");
		       	    	     }else{
		       	    	    	 $(valCode).attr("value", row.id);
		       	    	     } 
		        	    
		        	    	$(valCode).trigger("change");
	        	    });
	            }

        	$.getJSON('<%=path %>/search/unitDocBasic.action?time='+Math.random(),{ id:''}, function(json) {
	        	  initAutoComplete(json,"#unitName1","#unitNameCode1","");
	        	} );
        	
        	$.getJSON('<%=path %>/search/projectNameDocBasic.action?time='+Math.random(),{ id:''}, function(json) {
	        	  initAutoComplete(json,"#projectName1","#projectNameCode1","");
	        	} );
        	$.getJSON('<%=path %>/search/unitDocBasic.action?time='+Math.random(),{ id:''}, function(json) {
	        	  initAutoComplete(json,"#unitName2","#unitNameCode2","");
	        	} );
      	
        	$.getJSON('<%=path %>/search/projectNameDocBasic.action?time='+Math.random(),{ id:''}, function(json) {
	        	  initAutoComplete(json,"#projectName2","#projectNameCode2","");
	        	} );


        	$("#unitNameCode2").bind("change", function(){
   	        	var val=$("#unitNameCode2").attr("value");
                  
                 var pn=$("#projectName2").attr("value");
 		        	    $("#projectName2").attr("value","");
 		        	    $("#projectNameCode2").attr("value","");
                   $("#projectName2").unbind();
                  
 	                  $.getJSON('<%=path %>/search/projectNameDocBasic.action?time='+Math.random(),{ id:val }, function(json) {
 	    	        	  initAutoComplete(json,"#projectName2","#projectNameCode2",pn);
 	    	        	});
 	        	});
	        	
        	 $("#unitNameCode1").bind("change", function(){
   	        	var val=$("#unitNameCode1").attr("value");
                  
                 var pn=$("#projectName1").attr("value");
 		        	    $("#projectName1").attr("value","");
 		        	    $("#projectNameCode1").attr("value","");
                   $("#projectName1").unbind();
                  
 	                  $.getJSON('<%=path %>/search/projectNameDocBasic.action?time='+Math.random(),{ id:val }, function(json) {
 	    	        	  initAutoComplete(json,"#projectName1","#projectNameCode1",pn);
 	    	        	});
 	        	});
        });
        </script>
		<style type="text/css">
			/*demo page css*/
			body{ font-size: 12px; }
		</style>
	</head>

	<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="contert2">
			<tr>
				<td class="conttop2">
					<div class="top2left"></div>
					<div class="top2right"></div>
					<div class="top2middle">
						<span class="bottonszkj2" id="img1"><a href="#"><img
									src="../images/szkj_botton.gif"
									onclick="topwin(${param.menuId })"> </a> </span>
						<span class="bottonszkj2" id="img2"><a href="#"><img
									src="../images/szkj_botton.gif"> </a> </span>
						<span class="topico2"><img
								src="../images/Accordion_ico03.gif"> </span>
						<h3>
							档案维护
						</h3>
					</div>
				</td>
			</tr>
			<tr>
				<td class="contmiddle2">
				   <table width="750" border="0" cellspacing="0" cellpadding="0"  style="font-size:12px; font-weight:bold;">
				     <tr>
		              <td>
				      <div id="tabs" align="center">
				        <ul>
				          <li><a href="#tabs-1">重建索引</a></li>
				          <li><a href="#tabs-2">生成缩略图</a></li>
				          <li><a href="#tabs-3">同步档案数据</a></li>
				        </ul>
				        <div id="tabs-1">
							<form action="indexingArchive.action" method="post">
								<table width="730" border="0" cellpadding="0" cellspacing="5">
									<tr>
										<td><strong style="font-size:14px;">网省公司</strong></td>
										<td align="left"><input type="text" name="unitName1" id="unitName1" value="${unitName}" size="30"/></td>
										<td align="right"><strong style="font-size:14px;">工程</strong></td>
										<td align="left"><input type="text" name="projectName1" id="projectName1" value="${projectName}" size="40"/></td>
									</tr>
									<tr>
									   <td colspan="2"><input type="checkbox" name="indexCheckBox" id="indexCheckBox"/>
									   <strong style="font-size:14px;">重新生成</strong></td>
									   <td colspan="2">${message1}</td>
									</tr>
									<tr>
									   <td colspan="4" align="right">
									      <input type="submit" name="button1" id="button1" value="创建索引" class="chaxunbutton" />
									   </td>
									</tr>
								</table>
								<input type="hidden" name="unitNameCode1" id="unitNameCode1" value="${unitNameCode1}"/>
								<input type="hidden" name="projectNameCode1" id="projectNameCode1" value="${projectNameCode1}"/>
							</form>
						</div>
						<div id="tabs-2">
						     <form action="generateThumbnails.action" method="post">
								<table width="730" border="0" cellpadding="0" cellspacing="5">
									<tr>
										<td><strong style="font-size:14px;">网省公司</strong></td>
										<td align="left"><input type="text" name="unitName2" id="unitName2" value="${unitName2}" size="30"/></td>
										<td align="right"><strong style="font-size:14px;">工程</strong></td>
										<td align="left"><input type="text" name="projectName2" id="projectName2" value="${projectName2}" size="40"/></td>
									</tr>
									<tr>
									   <td colspan="2"><input type="checkbox" name="thumbnailsCheckBox" id="thumbnailsCheckBox"/><strong style="font-size:14px;">重新生成</strong></td>
									   <td colspan="2">${message2}</td>
									</tr>
									<tr>
									   <td colspan="4" align="right">
									      <input type="submit" name="button2" id="button2" value="生成缩略图" class="chaxunbutton" />
									   </td>
									</tr>
								</table>
								<input type="hidden" name="unitNameCode2" id="unitNameCode2" value="${unitNameCode2}"/>
								<input type="hidden" name="projectNameCode2" id="projectNameCode2" value="${projectNameCode2}"/>
							</form>
						</div>
                        <div id="tabs-3">
                             <table border="0" width="100%">
                                    <tr>
                                        <td>
                                            <strong style="font-size:14px;">消息：</strong>
                                        </td>
									    <td>
									    	<textarea id="indexing"  rows="8" cols="70" disabled="disabled"></textarea>
										</td>
								    </tr>
								    <tr>
								   	    <td align="right" colspan="2">
								     	   <input type="button" name="button3" id="button3" value="同步档案数据" onclick="importArchives()"class="cxtjbutton" />
										</td>	
								    </tr>
							  </table>
                        </div>
                       </div>
					</td>
				    </tr>
				  </table>
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
