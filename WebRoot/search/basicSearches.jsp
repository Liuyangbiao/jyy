<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>
    <title>My JSP 'lqTest.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	 <link type="text/css" href="<%=path %>/css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet" />	
	 <link type="text/css" href="<%=path %>/css/jquery.autocomplete.css" rel="stylesheet" />
	 <link type="text/css" href="<%=path %>/css/base.css" rel="stylesheet">
	 
	 <script type="text/javascript" src="<%=path %>/js/jquery-1.5.min.js"></script>  
	 <script type="text/javascript" src="<%=path %>/js/jquery-ui-1.8.10.custom.min.js"></script>
	 <script type="text/javascript" src="<%=path %>/js/jquery.ui.datepicker-zh-CN.js"></script>
	 <script type="text/javascript" src="<%=path %>/js/jquery.autocomplete.js"></script>
	 <script type="text/javascript" src="<%=path %>/js/jquery.timepicker.js"></script>
	 <script type="text/javascript" src="<%=path %>/js/jquery.maskedinput.js" ></script>
     <script type="text/javascript" src="<%=path %>/js/popShortcut.js"></script>
    
	<script type="text/javascript">
	$(function() {
		var mId="${param.menuId}";
		imgDisplay(mId);
              $.ajaxSetup({
      			 cache: false
   			  });
	});

	//如果字段的名字为空,则它的隐藏的code也应该为空
    function checkNull(){
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
     }
    
		 $(function() {
			//此处加验证代码
            $("#from").mask("2999"+"-"+"99"+"-"+"99");
			$("#to").mask("2999"+"-"+"99"+"-"+"99");			
				$("#datepicker").datepicker($.datepicker.regional["zh-CN"]);
				var dates = $( "#from, #to" ).datepicker({
			//		defaultDate: "+1w",
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

				//文件格式
		        $.getJSON('../system/dictionaryType.action',{ paramValue:$("#docFormatspan").text() }, function(json) {
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

			function selected(){
				var selectAll=document.getElementById("selectAll");
				for(i=0;i<document.all("format").length;i++)
				{
					if(document.all("format")[i].checked==false){
						selectAll.checked = false;
					}
				}
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
        	 $.getJSON('../system/dictionaryType.action',{ paramValue:$("#projectStatusspan").text() }, function(json) {
	        	  initAutoComplete(json,"#statuss","");
	        	}
	        );
       	  $.getJSON('../system/dictionaryType.action',{ paramValue:$("#projetTypespan").text() }, function(json) {
	        	  initAutoComplete(json,"#projectType","");
	        	}
	        );
	        
        	$.getJSON('<%=path %>/search/projectNameBasic.action?time='+Math.random(),{ id:'' }, function(json) {
	        	  initAutoComplete(json,"#projectName","");
	        	}
	        );
	        $.getJSON('<%=path %>/search/unitBasic.action?time='+Math.random(),{ id:'' }, function(json) {
	        	  initAutoComplete(json,"#designUnit","");
	        	}
	        );
	        $.getJSON('../system/dictionaryType.action',{
                paramValue:$("#zhuanyespan").text()
                }, function(json){
                	initAutoComplete(json, "#proType", "");
            });
	        $.getJSON('<%=path %>/search/docBasic.action?time='+Math.random(),{ id:'' }, function(json) {
	        	  initAutoComplete(json,"#docType","");
	        	}
	        );
	        $.getJSON('<%=path %>/search/recordTypeBasic.action?time='+Math.random(),{ id:'' }, function(json) {
	        	  initAutoComplete(json,"#recordType","");
	        	}
	        );
	       

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
	    	        	});
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
   	  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2">
        <tr>
          <td class="conttop2"><div class="top2left"></div>
              <div class="top2right"></div>
              <div class="top2middle"> 
                  <span class="bottonszkj" id="img1"><img src="<%=path %>/images/szkj_botton.gif" onclick="topwin(${param.menuId })" style="cursor:hand"></span>
		   		  <span class="bottonszkj" id="img2"><img src="<%=path %>/images/kjysz_botton.gif"></span>
               	  <span class="topico2"><img src="../images/Accordion_ico03.gif"></span>
                  <h3>基本查询</h3>
            </div></td>
        </tr>
        <tr>
          <td class="contmiddle2">
           <form action="<%=path %>/search/resultBasicSearch.action" method="post" onsubmit="return checkNull();">
           <input type="hidden" value="${param.menuId}" name="menuId" id="menuId">
   	   	   <table width="750" border="0" cellpadding="0" cellspacing="5" style="font-size:14px;">
	    	 <tr>
		        <td width="9%" align="right"><span id="projectStatusspan">工程阶段</span> </td>
		        <td width="24%" ><input type="text" id="statuss" name="statuss"/></td>
		        <td align="right"><span id="projetTypespan">工程分类</span></td>
		        <td ><input type="text" id="projectType" name="projectType"/></td>
		        <td width="9%" align="right">工程名称</td>
		        <td width="24%"><input type="text" id="projectName" name="projectName"/></td>
	      	</tr>
	        <tr>
		        <td width="9%" align="right"><span id="zhuanyespan">专业</span></td>
		        <td width="24%"><input type="text" id="proType" name="proType"/></td>
		        <td width="9%" align="right">设计院</td>
		        <td width="24%"><input type="text" id="designUnit" name="designUnit"/></td>
		        <td></td>
		        <td></td>
	        </tr>
    	  </table>
	    <div class="bianxian" style="width:750px;"></div>
	    <table width="750" border="0" cellpadding="0" cellspacing="5" style="font-size:14px;">
	      <tr>
	        <td width="9%" align="right">文档类型</td>
	        <td width="24%"><input type="text" id="docType" name="docType"/></td>
	        <td width="9%" align="right">档案分类</td>
	        <td width="24%"><input type="text" id="recordType" name="recordType"/></td>
	        <td align="right">文件名包含</td>
	        <td><input type="text" id="fileName" name="fileName"/></td>
	      </tr>
	    </table>
	    <div class="bianxian" style="width:750px;"></div>
	    <table width="750" border="0" cellpadding="0" cellspacing="5" style="font-size:14px;">
	      <tr>
	        <td width="70" align="right">上传日期</td>
	        <td width="300"><div class="demo"><input type="text" id="from" name="from" size=12 />到
	                                <input type="text" id="to" name="to" size=12 ></div></td>
	        <td width="75" align="right"><span id="docFormatspan">文件格式</span></td>
	        <td width="300">
	        	<input type="checkbox" name="selectAll" checked id="selectAll" onClick="checkthis()"/>全选 <br/>
				<table  border="0" cellspacing="0" cellpadding="0" id="formattable">
	        	</table>
	        </td>
	      </tr>
	    </table>
		    <table width="750" border="0" cellspacing="0" cellpadding="0">
		      <tr>
		        <td><table width="96" border="0" align="center" cellpadding="0" cellspacing="0">
		          <tr>
		            <td width="96" height="50"><input type="submit" value="查  询" class="chaxunbutton"/></td>
		          </tr>
		          <tr style="display: none">
		            <td>工程分类</td>
		            <td><input type="text" id="projectTypeCode" name="projectTypeCode"/></td>
		          </tr>
		          <tr style="display: none">
		            <td>工程阶段</td>
		            <td><input type="text" id="statussCode" name="statussCode"/></td>
		          </tr>
		          <tr style="display: none">
		            <td>工程名称</td>
		            <td><input type="text" id="projectNameCode" name="projectNameCode"/></td>
		          </tr>
		          <tr style="display: none">
		            <td>设计院</td>
		            <td><input type="text" id="designUnitCode" name="designUnitCode"/></td>
		          </tr>
		          <tr style="display: none">
		            <td>专业分类</td>
		            <td><input type="text" id="proTypeCode" name="proTypeCode"/></td>
		          </tr>
		          <tr style="display: none">
		            <td>文档类型</td>
		            <td><input type="text" id="docTypeCode" name="docTypeCode"/></td>
		          </tr>
		          <tr style="display: none">
		            <td>档案分类</td>
		            <td><input type="text" id="recordTypeCode" name="recordTypeCode"/></td>
		          </tr>
		        </table></td>
		      </tr>
		    </table>
          </form>
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
