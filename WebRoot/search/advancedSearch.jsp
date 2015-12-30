<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'advancedSearch.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link type="text/css" rel="stylesheet" href="<%=path %>/css/themes/jquery-ui-1.8.11.custom.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path %>/css/jquery.autocomplete.css"/>
	<link href="../css/base.css" rel="stylesheet" type="text/css">
	 
	<script type="text/javascript" src="<%=path %>/js/jquery-1.5.min.js"></script>  
	<script type="text/javascript" src="<%=path %>/js/jquery-ui-1.8.10.custom.min.js"></script>
	<script type="text/javascript" src="<%=path %>/js/jquery.ui.datepicker-zh-CN.js"></script>
	<script type="text/javascript" src="<%=path %>/js/jquery.autocomplete.js"></script>
	<script type="text/javascript" src="<%=path %>/js/jquery.timepicker.js"></script>
	<script type="text/javascript" src="<%=path %>/js/jquery.form.js"></script> 
	<script type="text/javascript" src="<%=path %>/js/jquery.maskedinput.js" ></script>
	<script type="text/javascript" src="<%=path %>/js/popShortcut.js"></script>

	<script type="text/javascript">
		$(function() {
			var mId="${param.menuId}";
			$("#curMenuId").val(mId);
			imgDisplay(mId);
		   $.ajaxSetup({
    		 cache: false
   	    	 });
		});
	
		//保存查询条件
      function saveQuery(){
        var queryName=$("#queryName").val();
        if(queryName=="" || queryName==null){
            alert("请输入您要保存的条件名称!");
    	}else{
    		queryName=escape(encodeURIComponent(queryName));
    		$.ajaxSetup({ cache:false });
			$('#myform').ajaxSubmit({
		        dataType: "json",
		        url:  "<%=path %>/search/saveQueryAdvancedSearch.action?queryName="+queryName,
		        beforeSubmit: function(){//alert("aa"); return true;
		        },
		        success:  showResponse
		    });
         }
      }

		//如果字段的名字为空,则它的隐藏的code也应该为空
      function checkNull(){
          var vol= $("#voltageLevel").attr("value");
          if(vol==""){
        	  $("#voltageLevelCode").attr("value","");
          }
          var sta= $("#statuss").attr("value");
          if(sta==""){
        	  $("#statussCode").attr("value","");
          }
          var pn= $("#projectName").attr("value");
          if(pn==""){
        	  $("#projectNameCode").attr("value","");
          }
          var dc= $("#docType").attr("value");
          if(dc==""){
        	  $("#docTypeCode").attr("value","");
          }
          var rt= $("#recordType").attr("value");
          if(rt==""){
        	  $("#recordTypeCode").attr("value","");
          }
          var cto= $("#creator").attr("value");
          if(cto==""){
        	  $("#creatorCode").attr("value","");
          }
      }
       // 删除查询条件
      function deleteQuery(){
    	var queryName=$("#queryName").val();
    	if(queryName=="" || queryName==null){
            alert("请输入您要删除的条件名称!");
    	}else{
    		if(confirm("你确定要删除该查询条件吗?")){
    			$.ajax({
    		        type: "POST",
    		        url: "<%=path %>/search/deleteQueryAdvancedSearch.action?time="+Math.random(),
    		        data: "queryName=" +queryName,
    		        success: function(){alert("删除成功");
    		        var obj = null;
    		        for (var i = 0; i <= myform.elements.length - 1; i++) {
    		            obj = myform.elements[i];
    		            if (obj.tagName == "INPUT" && obj.type == "text") {
    		                obj.setAttribute("value", "");
    		            }
    		            if (obj.tagName == "INPUT" && obj.type == "checkbox") {
    		                obj.setAttribute("checked", false);
    		            }

    		            if (obj.tagName == "SELECT") {
    		                obj.options[0].selected = true;
    		            }
    		        }
    		        $("#queryName").attr("value","");
    		        $("#queryName").unbind();
    		        //查询条件名称
    	            $.getJSON('<%=path %>/search/queryNameAdvancedSearch.action?time='+Math.random(),{ id:'' }, function(json) {
    		        	  initAutoCompleteNameDel(json,"#queryName");
    		        	}
    		        );
    		         return true;
    		         },
    		        error: function(){alert("删除失败"); return true;}
    		    });
    		}
    	}
      }

      // 查询条件载入
      function loading(){
    	var queryName=$("#queryName").val();
      	if(queryName=="" || queryName==null){
              alert("请输入您要载入的条件名称!");
      	}else{
      		$.getJSON('<%=path %>/search/loadingAdvancedSearch.action?time='+Math.random(),{ queryName:queryName }, function(json) {
      			for(var i in json){   
			        var value=json[i].queryKey;
			        var text=json[i].queryParams;  
			        $("#"+value).attr("value",text);
						        if(value=="format"){
				                        var str=text.split(",");
				                       	for(j=0;j<document.all("format").length;j++)
				           				{
				                       		var flag=false;
				                       		for(var i=0;i<str.length-1;i++){
					               				if(document.all("format")[j].value==str[i]){
					                                   flag=true;
					               				}
				                       		}
				                       		document.all("format")[j].checked=flag;
				           				} 
			                     }
      			}
	        });
       	 }
      }
      
      function showResponse(){
            alert("保存成功!");
      }
	    //时间设置
		$(function() {
			$("#fileSize").mask('999'+'.'+"99",{placeholder:"0"});
			$("#from").mask("2999"+"-"+"99"+"-"+"99");
			$("#to").mask("2999"+"-"+"99"+"-"+"99");
			$("#pre_design_year").mask("2999");
			$("#open_year").mask("2999");
			$("#close_year").mask("2999");
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

		function selected(){
			var selectAll=document.getElementById("selectAll");
			for(i=0;i<document.all("format").length;i++)
			{
				if(document.all("format")[i].checked==false){
					selectAll.checked = false;
				}
			}
		}

		function initAutoCompleteNameDel(json,valId)
    	{
    		$(valId).autocomplete(json, {
    		    max: 10, //列表里的条目数
    		    minChars: 0, //自动完成激活之前填入的最小字符,为0双击弹出列表
    		    matchCase:false,//不区分大小写
    		    width: 155, //提示的宽度，溢出隐藏
    		    scrollHeight: 300, //提示的高度，溢出显示滚动条
    		    matchContains: false, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
    		    autoFill: false, //自动填充 ，就是在文本框中自动填充符合条件的项目
    		    mustMatch: false, //自动匹配，mustMatch表示必须匹配条目，也就是在文本框里输入的内容，必须是data参数里的数据，如果不匹配，文本框就被清空
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
    	  		 return  row.name + " (" + row.spell + ")";
    	        //formatMatch是配合formatItem使用，作用在于，由于使用了formatItem，所以条目中的内容有所改变，而我们要匹配的是原始的数据，所以用formatMatch做一个调整，使之匹配原始数据
    	    },
    	    formatResult: function(row) {
        		 return row.name;
    	        //formatResult是定义最终返回的数据，比如我们还是要返回原始数据，而不是formatItem过的数据
    	    }
    	    }).result(function(event, row, formatted) {
    	    });
        }
        
		//初始化
		$(function(){

			function initAutoCompleteName(json,valId)
        	{
        		$(valId).autocomplete(json, {
        		    max: 10, //列表里的条目数
        		    minChars: 0, //自动完成激活之前填入的最小字符,为0双击弹出列表
        		    matchCase:false,//不区分大小写
        		    width: 155, //提示的宽度，溢出隐藏
        		    scrollHeight: 300, //提示的高度，溢出显示滚动条
        		    matchContains: false, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
        		    autoFill: false, //自动填充 ，就是在文本框中自动填充符合条件的项目
        		    mustMatch: false, //自动匹配，mustMatch表示必须匹配条目，也就是在文本框里输入的内容，必须是data参数里的数据，如果不匹配，文本框就被清空
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
        	  		 return  row.name + " (" + row.spell + ")";
        	        //formatMatch是配合formatItem使用，作用在于，由于使用了formatItem，所以条目中的内容有所改变，而我们要匹配的是原始的数据，所以用formatMatch做一个调整，使之匹配原始数据
        	    },
        	    formatResult: function(row) {
            		 return row.name;
        	        //formatResult是定义最终返回的数据，比如我们还是要返回原始数据，而不是formatItem过的数据
        	    }
        	    }).result(function(event, row, formatted) {
        	    });
            }
            
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
            //查询条件名称
            $.getJSON('<%=path %>/search/queryNameAdvancedSearch.action?time='+Math.random(),{ id:'' }, function(json) {
	        	  initAutoCompleteName(json,"#queryName");
	        	}
	        );
            //工程阶段
            $.getJSON('../system/dictionaryType.action', {
                paramValue: $("#projectStatusspan").text()
            }, function(json){
                initAutoComplete(json, "#statuss", "");
            });
            //工程分类
            $.getJSON('../system/dictionaryType.action', {
                paramValue: $("#projetTypespan").text()
            }, function(json){
                initAutoComplete(json, "#projectType", "");
            });
	        //工程名称
	      	$.getJSON('<%=path %>/search/projectNameBasic.action?time='+Math.random(),{ id:'' }, function(json) {
		        	  initAutoComplete(json,"#projectName","");
		        	}
		        );
	      	 //电压等级
            $.getJSON('../system/dictionaryType.action', {
                paramValue: $("#voltageLevelspan").text()
            }, function(json){
                initAutoComplete(json, "#voltageLevel", "");
            });
            //文档类型
            $.getJSON('../system/dictionaryType.action', {
                paramValue: $("#doctypespan").text()
            }, function(json){
                initAutoComplete(json, "#docType", "");
            });
            $.getJSON('../system/dictionaryType.action', {
                paramValue: $("#zhuanyespan").text()
            }, function(json){
                initAutoComplete(json, "#proType", "");
            });
	        //档案分类
	        $.getJSON('<%=path %>/search/recordTypeBasic.action?time='+Math.random(),{ id:'' }, function(json) {
	        	  initAutoComplete(json,"#recordType","");
	        	}
	        );
	        //上传人
	        $.getJSON('<%=path %>/system/opertorLogOperate.action?time='+Math.random(),{ id:''}, function(json) {
	        	  initAutoComplete(json,"#creator","");
	        	}
        	 );
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
	      	 $("#statussCode").bind("change", function(){
		        	//监听工程阶段的改变 
		        	var val=$("#statussCode").attr("value");
	                var projectTypeVal=$("#projectTypeCode").attr("value"); 
	                var pn=$("#projectName").attr("value");
		        	    $("#projectName").attr("value","");
		        	    $("#projectNameCode").attr("value","");
	                $("#projectName").unbind();
	                if(projectTypeVal=="" || projectTypeVal==null){
		                	$.getJSON('<%=path %>/search/projectNameBasic.action?time='+Math.random(),{ id:val }, function(json) {
		  		        	  initAutoComplete(json,"#projectName",pn);
		  		        	}
		  		          );  
	                }else{
	                		$.getJSON('<%=path %>/search/projectNameBasic.action?time='+Math.random(),{ id:val,tid:projectTypeVal }, function(json) {
		  		        	  initAutoComplete(json,"#projectName",pn);
		  		        	}
		  		          );  
	                }
		        });
		        
              $("#projectTypeCode").bind("change", function(){
    	        	//监听工程分类的改变 
    	        	var val=$("#projectTypeCode").attr("value");
                    var statussVal=$("#statussCode").attr("value"); 
                    var pn=$("#projectName").attr("value");
		        	    $("#projectName").attr("value","");
		        	    $("#projectNameCode").attr("value","");
                    $("#projectName").unbind();


                    var dt=$("#docType").attr("value");
	        	    $("#docType").attr("value","");
	        	    $("#docTypeCode").attr("value","");
                    $("#docType").unbind();

                    $.getJSON('<%=path %>/search/docBasic.action?time='+Math.random(),{ id:val }, function(json) {
	  		        	  initAutoComplete(json,"#docType",dt);
	  		        	}
	  		          );  

                    if(statussVal=="" || statussVal==null){
    	                	$.getJSON('<%=path %>/search/projectNameBasic.action?time='+Math.random(),{ tid:val }, function(json) {
    	  		        	  initAutoComplete(json,"#projectName",pn);
    	  		        	}
    	  		          );  
                    }else{
                    		$.getJSON('<%=path %>/search/projectNameBasic.action?time='+Math.random(),{ tid:val,id:statussVal }, function(json) {
    	  		        	  initAutoComplete(json,"#projectName",pn);
    	  		        	}
    	  		          );  
                    }
	        	});

              $("#docTypeCode").bind("change", function(){
    	        	//监听文档类型的改变 
    	        	var val=$("#docTypeCode").attr("value");
                   
                  var pn=$("#recordType").attr("value");
  		        	    $("#recordType").attr("value","");
  		        	    $("#recordTypeCode").attr("value","");
                    $("#recordType").unbind();
                   
  	                  $.getJSON('<%=path %>/search/recordTypeBasic.action?time='+Math.random(),{ id:val }, function(json) {
  	    	        	  initAutoComplete(json,"#recordType",pn);
  	    	        	}
  	    	        );
                    
  	        	});
		});
	</script>
   
    <style type="text/css">
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

	<input type="hidden" id="curMenuId" />
	
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2">
            <tr>
                <td class="conttop2">
                    <div class="top2left">
                    </div>
                    <div class="top2right">
                    </div>
                    <div class="top2middle">
	                    <span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif" onclick="topwin(${param.menuId })" style="cursor:hand"></a></span>
			       		<span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span>
                        <span class="topico2"><img src="../images/Accordion_ico03.gif"></span>
                      <h3>高级检索</h3>
                    </div>
              </td>
            </tr>
            <tr>
              <td height="473" class="contmiddle2" style="font-size:12px;">
              <table width="505" border="0" cellspacing="5" cellpadding="0" style="font-size:14px;">
		          <tr>
		            <td width="150" align="right">自定制查询条件</td>
		            <td width="142" align="left"><input name="queryName" type="text" id="queryName" size="20"/></td>
		            <td width="49"><button onClick="loading()" class="querenbutton">载入</button></td>
		            <td width="53"><button onClick="deleteQuery()" class="qkbutton">删除</button> </td>
		          </tr>
       	 	 </table>
             <br/>
      	<form action="<%=path %>/search/queryAdvancedSearch.action" id="myform" method="post" onsubmit="checkNull();">
      	<input type="hidden" name="menuId" id="menuId" value="${param.menuId }">
			<table width="750" border="0" cellspacing="0" cellpadding="0" class="conttitle">
              <tr>
                <td height="22"><h3>根据工程</h3></td>
              </tr>
            </table>
		    <table width="750" border="0" cellspacing="5" cellpadding="0" style="font-size:14px;">
              <tr>
                <td width="89" align="right">业主单位</td><td width="140"><input name="unname" type="text" id="unname" size="20"/></td>
                <td width="93" align="right"><span id="projectStatusspan">工程阶段</span></td><td width="140"><input name="statuss" type="text" id="statuss" size="20"/></td>
                <td width="111" align="right"><span id="projetTypespan">工程分类</span></td><td width="142"><input name="projectType" type="text" id="projectType" size="20"/></td>
              </tr>
              <tr>
                <td align="right"><span id="voltageLevelspan">电压等级</span></td><td><input name="voltageLevel" type="text" id="voltageLevel" size="20"/></td>
                <td align="right">工程名称</td> <td><input name="projectName" type="text" id="projectName" size="20"/></td>
                <td align="right">初设年份</td> <td><input name="pre_design_year" type="text" id="pre_design_year" size="20"/></td>
              </tr>
              <tr>
                <td align="right">开工年份</td><td><input name="open_year" type="text" id="open_year" size="20"/></td>
                <td align="right">竣工年份</td><td><input name="close_year" type="text" id="close_year" size="20"/></td>
                <td align="right" style="display: none;">工程分类</td><td style="display: none;"><input name="projectTypeCode" type="text" id="projectTypeCode" size="20"/></td>
              </tr>
              <tr style="display: none;">
	               <td align="right">工程阶段</td><td><input name="statussCode" type="text" id="statussCode" size="20"/></td>
	               <td align="right">工程名称</td> <td><input name="projectNameCode" type="text" id="projectNameCode" size="20"/></td>
	               <td align="right">电压等级</td><td><input name="voltageLevelCode" type="text" id="voltageLevelCode" size="20"/></td>
              </tr>
            </table>  
		    <table width="750" border="0" cellspacing="0" cellpadding="0" class="conttitle">
              <tr>
                <td height="22"><h3>根据档案</h3></td>
              </tr>
            </table>
		    <table width="750" border="0" cellspacing="5" cellpadding="0" style="font-size:14px;">
              <tr>
                <td width="89" align="right"><span id="doctypespan">文档类型</span></td><td width="140"><input name="docType" type="text" id="docType" size="20"/></td>
                <td width="93" align="right">档案分类</td> <td width="140"><input name="recordType" type="text" id="recordType" size="20"/></td>
                <td width="111" align="right"><span id="zhuanyespan">专业</span></td><td width="142"><input name="proType" type="text" id="proType" size="20"/></td>
              </tr>
              <tr>
                <td align="right">档案版本</td><td><input name="version" type="text" id="version" size="20"/></td>
                <td align="right">上传者</td><td><input name="creator" type="text" id="creator" size="20"/></td>
                <td align="right" style="display: none;">专业</td><td style="display: none;"><input name="proTypeCode" type="text" id="proTypeCode" size="20"/></td>
              </tr>
              <tr style="display: none;">
                <td align="right">文档类型</td><td><input name="docTypeCode" type="text" id="docTypeCode" size="20"/></td>
                <td align="right">档案分类</td><td><input name="recordTypeCode" type="text" id="recordTypeCode" size="20"/></td>
                <td align="right">上传者</td><td><input name="creatorCode" type="text" id="creatorCode" size="20"/></td>
              </tr>
            </table>
			<table width="750" border="0" cellspacing="0" cellpadding="0" class="conttitle">
              <tr>
                <td height="22"><h3>根据文件</h3></td>
              </tr>
            </table>
	      <table width="750" border="0" cellspacing="5" cellpadding="0" style="font-size:14px;">
              <tr>
                <td width="100" align="right">文件名包含</td><td width="260"><input type="text" name="fileName" id="fileName"/></td>
                <td width="80" align="right"><span id="docFormatspan">文件格式</span></td><td width="340">
                     <input type="checkbox" name="selectAll" checked id="selectAll" onClick="checkthis()"/>全选 <br/>
 					<table border="0" cellspacing="0" cellpadding="0" id="formattable">
		        	 </table>                
               	</td>
              </tr>
              <tr>
                <td align="right">上传时间从</td>
                <td><input name="from" type="text" id="from" size="15"/>  到<input name="to" type="text" id="to" size="15"/></td>
                <td align="right">文件大小</td><td><select id="fileSizeJudge" name="fileSizeJudge">
			                                  <option value=">">大于</option>
			                                  <option value="&lt;">小于</option>
			                            </select>
									    <input type="text" name="fileSize" id="fileSize"/>
									    <select id="fileSizeUnits" name="fileSizeUnits">
									    	<option value="M">M</option><option value="K">K</option><option value="B">B</option>
									    </select></td>
              </tr>
          </table>
	<table width="750" border="0" cellspacing="5" cellpadding="0">
  <tr>
    <td width="366" align="right"><input type="submit" value="检索" class="chaxunbutton" /></td>
    <td width="369"><button onClick="saveQuery()" class="cxtjbutton">保存查询条件</button></td>
  </tr>
</table>
	  </form>
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
