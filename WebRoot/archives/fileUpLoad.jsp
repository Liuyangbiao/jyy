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
		<link rel="stylesheet" type="text/css" href="../css/base.css" >
		<link rel="stylesheet" type="text/css" href="../css/themes/jquery-ui-1.8.11.custom.css" />
		<link rel="stylesheet" type="text/css" href="../css/uploadify.css" />
		<link rel="stylesheet" type="text/css" href="../css/jquery.autocomplete.css" />
		<link rel="stylesheet" type="text/css" href="../css/skin-vista/ui.dynatree.css" />
		<script type="text/javascript" src="../js/jquery-1.5.min.js"></script>
		<script type="text/javascript" src="../js/jquery-ui-1.8.10.custom.min.js"></script>
		<script type="text/javascript" src="../js/jquery.dynatree.js"></script>
		<script type="text/javascript" src="../js/jquery.autocomplete.js"></script>
		<script type="text/javascript" src="../js/swfobject.js"></script>
		<script type="text/javascript" src="../js/jquery.uploadify.min.js"></script>
		<script type="text/javascript" src="../js/jquery.validate.1.8.js"></script> 
		<script type="text/javascript" src="../js/jquery.ui.datepicker-zh-CN.js"></script>
		<script type="text/javascript" src="../js/jquery.maskedinput.js" ></script>
		<script type="text/javascript" src="../js/jquery.timepicker.js"></script>
		<script type="text/javascript" src="../js/popShortcut.js"></script>
		<style type="text/css">  
				label.error { float: left; color: red; padding-left: .5em; vertical-align: middle; }   
		   </style> 

		<script type="text/javascript">
			$(function() {
				var mId="${param.menuId}";
				imgDisplay(mId);
			});
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
				var validate1= $("#myForm").validate({
					 submitHandler:function(form){
						form.submit();
						progressBar()
	                       },
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
				  	   remark:{maxlength: 250},
				  	   projectNameCode: "required",
				  	   isUpload:"required"
				   },
				   messages: {
					   projectNameCode: "请先选择文档分类,再选上工程名称!",
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
					  remark:{maxlength:"备注必须在250个字符之内!"},
					   isUpload: "提交前必须先上传文件!"
				   }
				});
			})
		    
			
		  
		　$(document).ready(function() {
			var inum=0;
			　$("#uploadify").uploadify({
				　'swf': '../js/uploadify.swf',
				　'uploader':'<%=path %>/archives/doUploadUpLoad.action;jsessionid=<%=session.getId()%>',
				　//'script': 'servlet/Upload?name=yangxiang',
				　'cancelImage': '../images/cancel.png',
				　'queueID' : 'fileQueue', //和存放队列的DIV的id一致  
				　'fileDataName': 'Filedata', //必须，和以下input的name属性一致                 设置一个名字，在服务器处理程序中根据该名字来取上传文件的数据。默认为Filedata 
				　'auto'  : false, //是否自动开始  
				　'multi': true, //是否支持多文件上传  
				  'buttonImage':'../images/xuanze_button.gif',         //浏览按钮的图片的路径 。 
				  'buttonText': '\u6D4F\u89C8', //按钮上的文字  
				　'simUploadLimit' : 1, //一次同步上传的文件数目  
				　'sizeLimit': 999999999, //设置单个文件大小限制，单位为byte  
				 // 'hideButton'：true,//设置为true则隐藏浏览按钮的图片
				'rollover':false,
				　'queueSizeLimit' : 10, //队列中同时存在的文件个数限制  
				　'fileDesc': '支持格式:jpg,doc,xls,dwg,pdf,tif,gif,jpeg,png,bmp,docx,xlsx,rtf,bwbd5,bwsd5,html', //如果配置了以下的'fileExt'属性，那么这个属性是必须的  
				　'fileExt': '*.jpg;*.doc;*.xls;*.dwg;*.pdf;*.tif;*.gif;*.jpeg;*.png;*.bmp;*.docx;*.xlsx;*.rtf;*.bwbd5;*.bwsd5;*.html',//允许的格式
				  'height' : 22,  //设置图片大小
			      'width'  : 92,
				  'wmode' : 'transparent' ,  //设置图片空白区域为透明
				  'removeCompleted' : true,
				  'onSelect': function(fileObj){
					var table = document.getElementById("addRow");
					var row=table.rows;
					for(var i=0;i<row.length;i++){
						if(i>0){
							var name=row[i].cells[1].innerText;
							if(fileObj.name==name){
	                            alert("该文件已经上传！");
	                            jQuery('#uploadify').uploadifyCancel(fileObj.id);
							}
						}
					}
		            $("#startUp").attr("disabled",false);
		            $("#clearUp").attr("disabled",false);
		        },
		        onSelectError: function(file,errorCode,errorMsg){},//选择文件有误触发事件
				onUploadComplete: function(file){//上传成功触发事件
				},
				onUploadProgress: function(file,fileBytesLoaded,fileTotalBytes){},//上传中触发事件
				onUploadStart: function(file){},//上传开始触发事件
	            onUploadSuccess: function(fileObj, response, status){ //当单个文件上传成功后激发的事件
					if(fileObj.size>1073741824){
                        alert("上传文件太大!请重新选择!");
					}else{
						var dd = new Date();
						var myDate=dd.toLocaleString( );       //获取日期与时间
						var table = document.getElementById("addRow");
						var newRow = table.insertRow(table.rows.length);
						var newTd0 = newRow.insertCell(0);newTd0.innerHTML='<input type="checkbox" name="boxName" value="'+inum+'"/>';  
						var newTd1 = newRow.insertCell(1);newTd1.innerHTML=fileObj.name;  
						var newTd2 = newRow.insertCell(2);newTd2.innerHTML=myDate;
						if(fileObj.size<102400){ 
							var newTd3 = newRow.insertCell(3);newTd3.innerHTML=(Math.round((fileObj.size)*100/1024)/100)+"K";
						   }else{
							var newTd3 = newRow.insertCell(3);newTd3.innerHTML=(Math.round((fileObj.size)*100/1024/1024)/100)+"M";	
						}
						$("#isUpload").attr("value","canUpload");
						$("#isUpload").focus();
						$("#isUpload").attr("value","canUpload");
						inum=inum+1;
						//在此做一次判断主要是解决 已经上传文件后 还提示请上传文件问题
						var v=$("#myForm").validate({
							 rules: {
							  isUpload:"required"
						   }
						});
						v.form();
					}
				} 
			　});
		　});

		//删除已经上传文件
		function deleteFile(){
			 var r=document.getElementsByName("boxName");
			 var dfileName="";
			 var dfileNameToo="";
	    	   var dfFlag=false;
	    	   for(var i=0;i<r.length;i++){
		    	   if(r[i].checked){
		    		   dfFlag=true;  
		    		   dfileNameToo=dfileNameToo+r[i].value+","; 
		    		   dfileName=dfileName+i+","; 
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
		       		         var dfNames=dfileNameToo.split(",");
		       		         var table = document.getElementById("addRow");
                             for(var i=0;i<dfNames.length;i++){
                            	var num=dfNames[i] ;
                            	for(var j=0;j<r.length;j++){
                  		    	   if(r[j].value==num){
                  		    		var newRow = table.deleteRow(j+1);   
                  		    	   }
                  	    	    }
                             }

                             //如果文件全部删除,则是不能提交的
                             if(table.rows.length<=1){
                            	 $("#isUpload").attr("value","");
                             }
		       		         return true;},
		       		        error: function(){alert("删除失败"); return true;}
		       		    });
		       		}
	        	}
	    	   
		   }
	</script>
		<script type="text/javascript">
       $(function() {
    	   var baomispan = $("#baomispan").text();
    		 $.getJSON('../system/dictionaryType.action',{
    	        paramValue:baomispan},
    	        function(json){
    			 var baomi = "";
    			 for(var i = 0;i<json.length;i++){
    				 baomi += "<option value=\"" + json[i].id + "\" >"
    				 + json[i].name + "</option>";
    			 }
    			 $("#dftSecurity").html(baomi)
    	     });
			 $("#createDate").mask("2999"+"-"+"99"+"-"+"99");
			 $("#createDate").datepicker($.datepicker.regional["zh-CN"]);
      	});

       //页面重置
    	function resetUpload(){
    		 var r=document.getElementsByName("boxName");
			 var dfileName="";
			 var dfileNameToo="";
			 for(var i=0;i<r.length;i++){
				   dfileNameToo=dfileNameToo+r[i].value+","; 
	    		   dfileName=dfileName+i+","; 
	    	   }
			 $.ajax({
    		        type: "POST",
    		        url: "deleteFileUpLoad.action?time="+Math.random(),
    		        data: "dfileName=" +dfileName,
    		        success: function(){
    		         var dfNames=dfileNameToo.split(",");
    		         var table = document.getElementById("addRow");
	                  for(var i=0;i<dfNames.length;i++){
	                 	var num=dfNames[i] ;
	                 	for(var j=0;j<r.length;j++){
	       		    	   if(r[j].value==num){
	       		    		var newRow = table.deleteRow(j+1);   
	       		    	   }
	       	    	    }
                     }

                  //如果文件全部删除,则是不能提交的
                  if(table.rows.length<=1){
                 	 $("#isUpload").attr("value","");
                  }
    		         return true;},
    		        error: function(){ return true;}
    		    });
    	}
   </script>

	<script language="javascript">
        $(function(){
        	function initAutoComplete(json,valId,pn)
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
	        	  			$(valId+"Code").attr("value", row.code);
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
	        	    	    	 $(valId+"Code").attr("value", row.code);
	        	    	     } 
	        	    	});
	          		  }
    
		    $("#tree").dynatree({
			      title: "Event samples",
			
			      onQueryActivate: function(activate, node) {
			        logMsg("onQueryActivate(%o, %o)", activate, node);
			             //return false;
			      },
			      onActivate: function(node) {
				      //必须点叶子节点才能调用下面代码
				    if(node.data.isLeaf=='1'){
					    	logMsg("onActivate(%o)", node);
					        var pn=$("#projectName").attr("value");
					        $("#projectName").attr("value","");
						    $("#projectNameCode").attr("value","");
					        $("#projectName").unbind();
					        $.getJSON('<%=path %>/archives/projectNameUpLoad.action?time='+Math.random(),{ tid:node.data.code,id:''}, function(json) {
					      	  initAutoComplete(json,"#projectName",pn);
					      		});
					        $("#docTypeCode").attr("value",node.data.code);
				  	  }
			      } 
		      });
		    var dtpc="${docTypeParentCode}";
		      if(dtpc!=""){
		    	  var dtc="${docTypeCode}";
		    	  $("#docTypeCode").attr(dtc);
		    	  $("#tree").dynatree("getTree").getNodeByKey("file"+dtc).select();
		    	  $("#tree").dynatree("getRoot").visit(function(node){   
		    	   var node1 = $("#tree").dynatree("getTree").getNodeByKey("folder"+dtpc).data.code;
		    	   if(node.data.code==node1){
		    	     node.expand(true);
		    		}
		    	 });
		      }
		  });
</script>
</head>
	<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2">
          <tr>
            <td class="conttop2"><div class="top2left"></div>
                <div class="top2right"></div>
	            <div class="top2middle">
	             	<span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif" onclick="topwin(${param.menuId })" style="cursor:hand"></a></span>
	             	<span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span>
	             	<span class="topico2"><img src="../images/Accordion_ico03.gif"></span>
	                <h3>档案录入</h3>
	            </div>
	        </td>
          </tr>
          <tr>
            <td valign="top" class="contmiddle2">
            	<form action="<%=path %>/archives/entryFileUpLoad.action" method="post" id="myForm" name="myForm">
				<table width="100%" border="0">
					<tr>
					   <td width="15%" valign="top">
						  <table width="200" border="0" cellspacing="0" cellpadding="0" style="width:200px;" >
                              <tr>
                                 <td class="wdlrtitle">文档分类列表（请选择文档分类）</td>
                              </tr>
                              <tr>
                                 <td valign="top" class="wdlrmiddle" style="height:320px;">
                                 <div id="tree" style=" width:185px;height:320px; overflow: auto;">
									<ul>
										<s:iterator value="tDocTypeList">
											<li id="folder<s:property value="code" />" class="folder" data="code: '<s:property value="code" />',isLeaf:'0'">
												<s:property value="name" />
												<ul>
													<s:iterator value="list">
														<li id="file<s:property value="code" />" data="code: '<s:property value="code" />',isLeaf:'1'">
															<s:property value="name" />
													</s:iterator>
												</ul>
										</s:iterator>
									</ul>
									</div>
								      <input type="hidden" name="docTypeCode" id="docTypeCode" value=${docTypeCode }/></td>
                               </tr>
                               <tr>
                                    <td class="wdlrbottom"></td>
                               </tr>
                           </table>
                       </td>
					   <td width="85%" valign="top">
							<table width="88%" border="0" cellpadding="0" cellspacing="1" style="font-size:12px;font-weight:bold;">
								<tr>
									<td width="10%" height="37" align="center">
											工程名称									</td>
									<td width="40%" bgcolor="#EDF1F6" style="padding:5px;">
										<input id="projectName" name="projectName" type="text" size="45" value="${projectName }"/>
										<input type="hidden" id="projectNameCode" name="projectNameCode" value="${projectNameCode }"/><br/>
										<span style = "color:red;" >*从左边树列表选择</span>
								  </td>
								  <td width="10%" align="center">
											<span id="baomispan">密级分类</span>										
									</td>
		  						  <td width="40%" style="padding:5px;">
										<select name="dftSecurity" id="dftSecurity">
				  						</select>										
				  				  </td>
		  					  </tr>
							  <tr>
									<td height="92" align="center">
											初设档案										
									</td>
		 						    <td style="padding:5px;" colspan="3">
				 						<table width="92%" border="0" cellpadding="0" cellspacing="1" style="font-size:12px;">
							 				<tr>
												<td nowrap height="27" align="right" >
														设计人</td>
												<td width="23%" style="padding:5px;">
													<input id="sjrm" size="10" name="tPreDesginT.sjrm" type="text" value="${tPreDesgin.sjrm }"/>
											    </td>
											    <td nowrap height="27" align="right" >
														校核人</td>
												<td width="23%" style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.xhrm" id="xhrm"  value="${tPreDesgin.xhrm }">
											    </td>
											    <td nowrap height="27" align="right" >
														审核人	</td>
												<td width="23%" style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.shrm" id="shrm"  value="${tPreDesgin.shrm }">
											    </td>
											</tr>
											<tr>
												<td nowrap height="27" align="right" >
														批准人</td>
												<td style="padding:5px;">
													<input id="pzrm" size="10" name="tPreDesginT.pzrm" type="text" value="${tPreDesgin.pzrm }"/>
											    </td>
											    <td nowrap height="27" align="right" >
														立卷人</td>
												<td nowrap style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.ljrm" id="ljrm" value="${tPreDesgin.ljrm }" >
											    </td>
											    <td nowrap height="27" align="right" >
														检查人	</td>
												<td style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.jcrm" id="jcrm"  value="${tPreDesgin.jcrm }">
											    </td>
											</tr>
											<tr>
												<td nowrap height="27" align="right" >
														设计阶段</td>
												<td style="padding:5px;">
													<input id="sjjd" size="10" name="tPreDesginT.sjjd" type="text" value="${tPreDesgin.sjjd }"/>
											    </td>
											    <td nowrap height="27" align="right" >
														案卷提名</td>
												<td style="padding:5px;">
													 <input type="text"  size="10" name="tPreDesginT.ajtm" id="ajtm" value="${tPreDesgin.ajtm }" >
											    </td>
											    <td nowrap height="27" align="right" >
														图纸名称	</td>
												<td style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.tzmc" id="tzmc"  value="${tPreDesgin.tzmc }">
											    </td>
											</tr>
											<tr>
												<td nowrap height="27" align="right" >
														图纸张数</td>
												<td style="padding:5px;">
													<input id="tzzs" size="10" name="tPreDesginT.tzzs" type="text" value="${tPreDesgin.tzzs }"/>
											    </td>
											    <td nowrap height="27" align="right" >
														分类号</td>
												<td style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.flbh" id="flbh"  value="${tPreDesgin.flbh}">
											    </td>
											    <td nowrap height="27" align="right" >
														案卷档号	</td>
												<td style="padding:5px;">
													 <input type="text" size="10" name="tPreDesginT.andh" id="andh"  value="${tPreDesgin.andh}">
											    </td>
											</tr>
											<tr>
												<td nowrap height="27" align="right" >
														卷内页号</td>
												<td style="padding:5px;">
													<input id="jnyh"  size="10" name="tPreDesginT.jnyh" type="text" value="${tPreDesgin.jnyh}"/>
											    </td>
											    <td nowrap height="27" align="right" >
														编制日期</td>
												<td style="padding:5px;">
													 <input type="text"  size="10" name="tPreDesginT.createDate" id="createDate" value="${tPreDesgin.createDate}">
											    </td>
											    <td nowrap height="27" align="right" >
																</td>
												<td style="padding:5px;">
													 
											    </td>
											</tr>
				 						</table>
									</td>
			 					 </tr>
							  <tr>
								 <td height="92" align="center">
										备注										
								 </td>
	 						     <td style="padding:5px;" colspan="3">
			 						 <textarea name="remark" id="remark" rows="5" cols="60"></textarea>	<span style="color: red; fontSize: 11px;">250个字符以内</span>     
								 </td>
		 					  </tr>
							</table>		  
							<table width="530" border="0" cellspacing="2" cellpadding="0">
								 <tr>
									 <td width="13%" height="38" valign="middle">
								 	   <input type="file" name="uploadify" id="uploadify" class="xuanzebutton" />
      								 </td>
					                 <td width="13%" valign="middle">
	                                        <input type="button"
														onclick="jQuery('#uploadify').uploadifyUpload()" value="开始上传" disabled="disabled" class="dowbutton" id="startUp">
    								 </td>
						             <td width="13%" valign="middle">
					    				     <input type="button" 
								    	 				onclick="jQuery('#uploadify').uploadifyCancel('*')" value="批量取消上传"  disabled="disabled" class="upbutton" id="clearUp">
    								 </td>
    								 <td width="61%" valign="middle">&nbsp;</td>
							  	</tr>
  								<tr>
   									 <td height="12" colspan="3" valign="top"><div id="fileQueue"></div>
   									 <input type="text"
									       id="isUpload" name="isUpload" style="display: none;">
   									 </td>
   							    </tr>
							</table>
							<table width="530" border="0" cellspacing="0" cellpadding="0" class="conttitle">
								<tr>
								  <td height="22" ALIGN="CENTER"><h3>已上传文件</h3></td>
					  			</tr><tr>
								  <td height="22" ><h3>
								  <input type="button" 
								  			onclick="deleteFile()" value="删除上传文件" class="upbutton">
								  </h3></td>
					  			</tr>
							</table>
						    <table id="addRow" width="530" border="0" cellspacing="1" cellpadding="0" class="biaoge" >
								  <tr class="bgtr01">
								    <td>选择</td>
								    <td>文件名称</td>
								    <td>上传时间</td>
								    <td>文件大小</td>
								  </tr>
							</table>
    						<table width="530" border="0" cellpadding="0" cellspacing="3">
								<tr>
								    <td height="34" align="right"><input type="submit" value="提  交" class="chaxunbutton" ></td>
								    <td height="34" width="30" align="left"><input type="reset" value="重  置" class="qkbutton02" onclick="resetUpload();"></td>
								    <td height="34" align="left"><input type="reset" value="返  回" class="qkbutton02" onclick="javascript:window.history.back();"></td>
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
	    	   正在上传,请稍后·····
       </center>
    </div>
  </body>
</html>
