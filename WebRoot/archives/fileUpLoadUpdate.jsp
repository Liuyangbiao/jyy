<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
session.removeAttribute("fileData");
session.removeAttribute("fileName");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>My JSP 'upload.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="../css/base.css">
		<link rel="stylesheet" type="text/css" href="../css/themes/jquery-ui-1.8.11.custom.css" />
		<link rel="stylesheet" type="text/css" href="../css/uploadify.css"/>
		<link rel="stylesheet" type="text/css" href="../css/jquery.autocomplete.css" />
		<link rel="stylesheet" type="text/css" href="../css/skin-vista/ui.dynatree.css">
		<script type="text/javascript" src="../js/jquery-1.5.min.js"></script>
		<script type="text/javascript" src="../js/jquery-ui-1.8.10.custom.min.js"></script>
		<script type="text/javascript" src="../js/jquery.dynatree.js"></script>
		<script type="text/javascript" src="../js/jquery.autocomplete.js"></script>
		<script type="text/javascript" src="../js/swfobject.js"></script>
		<script type="text/javascript" src="../js/jquery.uploadify.min.js"></script>
		<script type="text/javascript" src="../js/jquery.ui.datepicker-zh-CN.js"></script>
		<script type="text/javascript" src="../js/jquery.maskedinput.js" ></script>
		<script type="text/javascript" src="../js/jquery.timepicker.js"></script>
		<script type="text/javascript" src="../js/jquery.validate.1.8.js"></script> 
		<style type="text/css">  
				label.error { float: left; color: red; padding-left: .5em; vertical-align: middle; }   
		   </style> 

		<script type="text/javascript">
		function progressBar(){
			$( "#progressBar" ).dialog({
				height:200,
				width:300,
				open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
				closeOnEscape: false,
				modal: true
			  });	
		    }

		$(function(){  
			var cd="${tPreDesgin.createDate}"
				
			$("#createDate").attr("value",cd.substr(0,10));
			  $("#myForm").validate({
				   rules: {
				  	  'tPreDesginT.sjrm':{maxlength: 50},
				  	  'tPreDesginT.xhrm':{maxlength: 50},
				  	'tPreDesginT.shrm':{maxlength: 50},
				  	'tPreDesginT.pzrm':{maxlength: 50},
				  	'tPreDesginT.ljrm':{maxlength: 50},
				  	'tPreDesginT.jcrm':{maxlength: 50},
				  	'tPreDesginT.sjjd':{maxlength: 250},
				  	'tPreDesginT.ajtm':{maxlength: 250},
				  	'tPreDesginT.tzmc':{maxlength: 250},
				  	'tPreDesginT.tzzs':{maxlength: 250,digits:true},
				  	'tPreDesginT.flbh':{maxlength: 250},
				  	'tPreDesginT.andh':{maxlength: 250},
				  	'tPreDesginT.jnyh':{maxlength: 250,digits:true},
				  	    remark:{maxlength: 250}
				   },
				   messages: {
					  'tPreDesginT.sjrm':{maxlength: "必须在50个字符之内!"},
					  'tPreDesginT.xhrm':{maxlength: "必须在50个字符之内!"},
					  'tPreDesginT.shrm':{maxlength: "必须在50个字符之内!"},
					  'tPreDesginT.pzrm':{maxlength: "必须在50个字符之内!"},
					  'tPreDesginT.ljrm':{maxlength: "必须在50个字符之内!"},
					  'tPreDesginT.jcrm':{maxlength: "必须在50个字符之内!"},
					  'tPreDesginT.sjjd':{maxlength: "必须在250个字符之内!"},
					  'tPreDesginT.ajtm':{maxlength: "必须在250个字符之内!"},
					  'tPreDesginT.tzmc':{maxlength: "必须在250个字符之内!"},
					  'tPreDesginT.tzzs':{maxlength: "必须在250个字符之内!",digits: "请输入整数!"},
					  'tPreDesginT.flbh':{maxlength: "必须在250个字符之内!"},
					  'tPreDesginT.andh':{maxlength: "必须在250个字符之内!"},
					  'tPreDesginT.jnyh':{maxlength: "必须在250个字符之内!",digits: "请输入整数!"},
					  remark:{maxlength:"备注必须在250个字符之内!"}
				   }
		       })
		  });
		  
		function goBack(){
	    	 window.history.back(-1);
	     }
		  
		　$(document).ready(function() {
			var inum=0;
			　$("#uploadify").uploadify({
				'swf': '../js/uploadify.swf',
				　'uploader':'<%=path %>/archives/doUploadUpLoad.action;jsessionid=<%=session.getId()%>',
				　'cancelImage': '../images/cancel.png',//取消下载文件图片路径
				　'queueID' : 'fileQueue', //和存放队列的DIV的id一致  
				　'fileDataName': 'Filedata', //必须，和以下input的name属性一致                 设置一个名字，在服务器处理程序中根据该名字来取上传文件的数据。默认为Filedata 
				　'auto'  : false, //是否自动开始  
				　'multi': true, //是否支持多文件上传  
				  'buttonImage':'../images/xuanze_button.gif',//浏览按钮的图片的路径 。 
				  'buttonText': '\u6D4F\u89C8', //按钮上的文字  
				　'simUploadLimit' : 1, //一次同步上传的文件数目  
				　'sizeLimit': 1073741824, //设置单个文件大小限制大小限制为1G，单位为byte  
				　'queueSizeLimit' : 10, //队列中同时存在的文件个数限制  
				　'fileDesc': '支持格式:${tDoc.name }.${tDoc.suffix }', //如果配置了以下的'fileExt'属性，那么这个属性是必须的  
				　'fileTypeExts': '${tDoc.name }.${tDoc.suffix }',//允许的格式
				  'height' : 22,  //设置图片大小
			      'width'  : 92,
			      'folder' : 'uploads', //要上传到的服务器路径， 
			      'removeCompleted' : true,
				  'onSelect': function(e, queueId, fileObj)
			        {
			            $("#startUp").attr("disabled",false);
			            $("#endUp").attr("disabled",false);
			        },
				　onComplete: function (event, queueID, fileObj, response, data) {  
					if(fileObj.size>1000*1000*1000){
                        alert("上传文件太大!请重新选择!");
					}else{
						var dd = new Date();
						var myDate=dd.toLocaleString( );       //获取日期与时间
						var table = document.getElementById("addRow");
						var newRow = table.insertRow(table.rows.length);
						var newTd0 = newRow.insertCell(0);newTd0.innerHTML='<input type="checkbox" name="boxName" value="'+inum+'"/>';  
						var newTd1 = newRow.insertCell(1);newTd1.innerHTML=fileObj.name;  
						var path=fileObj.filePath;
						var p=path.replace("//", "/");
						var newTd2 = newRow.insertCell(2);newTd2.innerHTML=p; 
						var newTd3 = newRow.insertCell(3);newTd3.innerHTML=myDate;
						if(fileObj.size<102400){ 
							var newTd4 = newRow.insertCell(4);newTd4.innerHTML=(Math.round((fileObj.size)*100/1024)/100)+"K";
						   }else{
							var newTd4 = newRow.insertCell(4);newTd4.innerHTML=(Math.round((fileObj.size)*100/1024/1024)/100)+"M";	
						}
						inum=inum+1;
						$("#uploadifySpan").hide();
						$("#startUp").hide();
						$("#endUp").hide();
						$("#deleteUp").show();
					}
				　},  
				　onError: function(event, queueID, fileObj) {  
				　		alert("文件:" + fileObj.name + "上传失败");  
				　},  
				　onCancel: function(event, queueID, fileObj){  
				
				　　　} 
			　});
		　});

		//删除已经上传文件
		function deleteFile(){
			 var r=document.getElementsByName("boxName");
			 var dfileName=""
	    	   var dfFlag=false;
	    	   for(var i=0;i<r.length;i++){
		    	   if(r[i].checked){
		    		   dfFlag=true;  
		    		   dfileName=dfileName+r[i].value+","; 
		    	   }
	    	   }
	    	   
		       	if(!dfFlag){
		               alert("请选择要删除的文件!");
		       	}else{
		       		if(confirm("你确定要删除选中的文件吗?")){
		       			$.ajax({
		       		        type: "POST",
		       		        url: "deleteFileUpLoad.action?time="+Math.random(),
		       		        data: "dfileName=" +dfileName,
		       		        success: function(){alert("删除成功");
		       		         var dfNames=dfileName.split(",");
		       		         var table = document.getElementById("addRow");
                             for(var i=0;i<dfNames.length;i++){
                            	var num=dfNames[i] ;
                            	for(var j=0;j<r.length;j++){
                  		    	   if(r[j].value==num){
                  		    		var newRow = table.deleteRow(j+1);   
                  		    	   }
                  	    	    }
                             }
                             $("#uploadifySpan").show();
                             $("#startUp").show();
     						 $("#endUp").show();
                             $("#deleteUp").hide();
		       		         return true;},
		       		        error: function(){alert("删除失败"); return true;}
		       		    });
		       		}
	        	}
		   }
	</script>
		<script type="text/javascript">
		
		
       $(function() {
    	 //密级
			var baomispan = $("#baomispan").text();
   		 $.getJSON('../system/dictionaryType.action',{
   	        paramValue:baomispan},
   	        function(json){
   	        	security(json);
   	     });
   		 function security(json){
				var security="${tDoc.security}";
				var s_root=document.getElementById('dftSecurity');   
				s_root.options.length=0;  
					for(var i in json){
				        var value=json[i].id;
				        var text=json[i].name;
				        if(text==undefined){
				        continue;
				        } 
				        var option = document.createElement("option"); 
				            option.text=text;   
				            option.value=value;  
				            if(text=='内部' && security==""){
					            //默认选中 内部
				            	option.selected=true;
				            }else if(value==security){
				            	option.selected=true;
				            }
				            s_root.options[s_root.options.length] =option;
				    }
			}

			   var status="${tDoc.status}";
			   if(status=='03'){
				   $("#fileStatusName").attr("value","待归档");
			   }else if(status=='01'){
				   $("#fileStatusName").attr("value","归档");
			   }
			   $("#createDate").mask("2999"+"-"+"99"+"-"+"99");
	           $("#createDate").datepicker($.datepicker.regional["zh-CN"]);
      	});
   </script>

</head>
	<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2">
          <tr>
            <td class="conttop2"><div class="top2left"></div>
                <div class="top2right"></div>
	            <div class="top2middle">
	            	<span class="bottonszkj" style="cursor: hand;" onclick="goBack();"><img src="../images/fanhui_botton.gif"></span>
	             	<span class="topico2"><img src="../images/Accordion_ico03.gif"></span>
	                <h3>档案更新</h3>
	            </div>
	        </td>
          </tr>
          <tr>
            <td valign="top" class="contmiddle2">
            	<form action="<%=path %>/archives/fileUpdateUpLoad.action" method="post" id="myForm" name="myForm">
				<table width="100%" border="0">
					<tr>
					   <td width="79%" valign="top">
							<table width="80%" border="0" cellpadding="0" cellspacing="1" bgcolor="#aabbc4" style="font-size:12px;">
								<tr>
									<td width="10%" height="37" align="center" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
											工程名称									</td>
									<td width="40%" bgcolor="#EDF1F6" style="padding:5px;">
										<input id="projectNameShow" name="projectNameShow" type="text" value="${tProject.name }" disabled="disabled" size="50"/>
										<input type="hidden" id="projectNameCode" name="projectNameCode" value="${tProject.code }"/>
										<input type="hidden" id="projectName" name="projectName" value="${tProject.name }"/>
								    </td>
								    <td width="10%" height="37" align="center" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
											分类名称								</td>
									<td width="40%" bgcolor="#EDF1F6" style="padding:5px;">
										 <input type="text" name="docType" id="docType" value="${tDocType.name }" disabled="disabled" size="40">
								    </td>
								  </tr>
								<tr>
									<td height="37" align="center" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;">
											<span id="baomispan">密级分类</span>										
									</td>
		  							<td bgcolor="#EDF1F6" style="padding:5px;">
										<select name="dftSecurity" id="dftSecurity">
				  						</select>	
				  					</td>
				  					<td height="37" align="center" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
											文件状态						</td>
									<td bgcolor="#EDF1F6" style="padding:5px;">
									    <input type="text" disabled="disabled" name="fileStatusName" id="fileStatusName">
									    <input type="hidden" value="${tDoc.status}" name="fileStatus" id="fileStatus" >
								    </td>
			 					 </tr>
			 					 <tr>
									<td height="92" align="center" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;">
											初设档案										
									</td>
		 						    <td bgcolor="#EDF1F6" style="padding:5px;" colspan="3">
				 						<table width="90%" border="0" cellpadding="0" cellspacing="1" bgcolor="#EDF1F6" style="font-size:12px;">
							 				<tr>
												<td nowrap height="27" align="right" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
														设计人</td>
												<td width="23%" bgcolor="#EDF1F6" style="padding:5px;">
													<input id="sjrm" size="10" name="tPreDesginT.sjrm" type="text" value="${tPreDesgin.sjrm }"/>
											    </td>
											    <td nowrap height="27" align="right" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
														校核人</td>
												<td width="23%" bgcolor="#EDF1F6" style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.xhrm" id="xhrm"  value="${tPreDesgin.xhrm }">
											    </td>
											    <td nowrap height="27" align="right" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
														审核人	</td>
												<td width="23%" bgcolor="#EDF1F6" style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.shrm" id="shrm"  value="${tPreDesgin.shrm }">
											    </td>
											</tr>
											<tr>
												<td nowrap height="27" align="right" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
														批准人</td>
												<td bgcolor="#EDF1F6" style="padding:5px;">
													<input id="pzrm" size="10" name="tPreDesginT.pzrm" type="text" value="${tPreDesgin.pzrm }"/>
											    </td>
											    <td nowrap height="27" align="right" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
														立卷人</td>
												<td bgcolor="#EDF1F6" style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.ljrm" id="ljrm" value="${tPreDesgin.ljrm }" >
											    </td>
											    <td nowrap height="27" align="right" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
														检查人	</td>
												<td bgcolor="#EDF1F6" style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.jcrm" id="jcrm"  value="${tPreDesgin.jcrm }">
											    </td>
											</tr>
											<tr>
												<td nowrap height="27" align="right" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
														设计阶段</td>
												<td bgcolor="#EDF1F6" style="padding:5px;">
													<input id="sjjd" size="10" name="tPreDesginT.sjjd" type="text" value="${tPreDesgin.sjjd }"/>
											    </td>
											    <td nowrap height="27" align="right" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
														案卷提名</td>
												<td bgcolor="#EDF1F6" style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.ajtm" id="ajtm" value="${tPreDesgin.ajtm }" >
											    </td>
											    <td nowrap height="27" align="right" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
														图纸名称	</td>
												<td bgcolor="#EDF1F6" style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.tzmc" id="tzmc"  value="${tPreDesgin.tzmc }">
											    </td>
											</tr>
											<tr>
												<td nowrap height="27" align="right" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
														图纸张数</td>
												<td bgcolor="#EDF1F6" style="padding:5px;">
													<input id="tzzs" size="10" name="tPreDesginT.tzzs" type="text" value="${tPreDesgin.tzzs }"/>
											    </td>
											    <td nowrap height="27" align="right" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
														分类号</td>
												<td nowrap bgcolor="#EDF1F6" style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.flbh" id="flbh"  value="${tPreDesgin.flbh}">
											    </td>
											    <td nowrap height="27" align="right" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
														案卷档号	</td>
												<td bgcolor="#EDF1F6" style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.andh" id="andh"  value="${tPreDesgin.andh}">
											    </td>
											</tr>
											<tr>
												<td nowrap height="27" align="right" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
														卷内页号</td>
												<td bgcolor="#EDF1F6" style="padding:5px;">
													<input id="jnyh" size="10" name="tPreDesginT.jnyh" type="text" value="${tPreDesgin.jnyh}"/>
											    </td>
											    <td nowrap height="27" align="right" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
														编制日期</td>
												<td bgcolor="#EDF1F6" style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.createDate" id="createDate" >
											    </td>
											    <td height="27" align="right" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;" >
																</td>
												<td bgcolor="#EDF1F6" style="padding:5px;">
													 
											    </td>
											</tr>
				 						</table>
									</td>
			 					 </tr>
								 <tr>
									<td height="92" align="center" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;">
											备注										
									</td>
		 						    <td bgcolor="#EDF1F6" style="padding:5px;" colspan="3">
				 						 <textarea name="remark" id="remark" rows="5" cols="60">${tDoc.remark }</textarea>	
									</td>
			 					 </tr>
			 					 <tr>
									<td height="37" align="center" bgcolor="#EDF1F6" style="color:#476c89; font-weight:bold;">
											原有文件						
									</td>
		 						    <td bgcolor="#EDF1F6" style="padding:5px;" colspan="3">
				 						 ${tDoc.name }.${tDoc.suffix}
									</td>
			 					 </tr>
			 					 <tr style="display: none;">
			 					     <td>隐藏字段</td>
			 					     <td  colspan="3">
			 					        <input type="text" name="tdocId" id="tdocId" value="${tDoc.id }"> 文件 ID 
			 					        <input type="text" name="docTypeId" id="docTypeId" value="${tDocType.code }"> 文档分类 ID
			 					        <input type="text" name="projectId" id="projectId" value="${tProject.id }"> 工程对象ID
			 					     </td>
			 					 </tr>
							</table>		
							<table width="530" border="0" cellspacing="2" cellpadding="0">
								 <tr>
									 <td width="13%" height="38" valign="middle">
									   <span id="uploadifySpan">
								 	  		<input type="file" name="uploadify" id="uploadify" class="xuanzebutton" />
								 	   </span>
      								 </td>
					                 <td width="13%" valign="middle">
	                                        <input type="button"
														onclick="jQuery('#uploadify').uploadifyUpload()" value="开始上传" id="startUp" disabled="disabled" class="dowbutton">
    								 </td>
						             <td width="13%" valign="middle">
					    				     <input type="button" 
								    	 				onclick="jQuery('#uploadify').uploadifyCancel('*')" value="取消所有上传" id="endUp" disabled="disabled" class="upbutton">
    								 </td>
    								 <td width="61%" valign="middle">
					    				     <input type="button" 
								    	 				onclick="deleteFile()" value="删除上传文件" id="deleteUp" class="upbutton">
    								 </td>
							  	</tr>
  								<tr>
   									 <td height="12" colspan="3" valign="top"><div id="fileQueue"></div></td>
   							    </tr>
							</table>
							<table width="530" border="0" cellspacing="0" cellpadding="0" class="conttitle">
								<tr>
								  <td height="22"><h3>已上传文件</h3></td>
					  			</tr>
							</table>
						    <table id="addRow" width="530" border="0" cellspacing="1" cellpadding="0" class="biaoge" >
								  <tr class="bgtr01">
								    <td>选择</td>
								    <td>文件名称</td>
								    <td>文件路径</td>
								    <td>上传时间</td>
								    <td>文件大小</td>
								  </tr>
							</table>
    						<table width="530" border="0" cellpadding="0" cellspacing="3">
								<tr>
								    <td height="34" align="right"><input type="submit" value="提  交" class="chaxunbutton" onclick="progressBar();"></td>
								    <td height="34" align="left"><input type="button" value="返  回" class="qkbutton02" onclick="goBack();"></td>
			  					</tr>
							</table>
					  </td>
	   				 </tr>
				</table>
			 </form>
          </td>
       </tr>
       <tr>
            <td class="contbottom2"><div class="bottom2left"></div>
                <div class="bottom2right"></div>
                <div class="bottom2middle"><span class="bottonszkj"></span></div>
            </td>
       </tr>
    </table>
    <div id="progressBar" style="display: none;">
       <center>
	       <br/>
	       <img alt="请稍后····" src="../images/progressBar.gif">
	       <br/>
	    	   正在更新,请稍后·····
       </center>
    </div>
  </body>
</html>
