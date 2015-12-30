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
		<link type="text/css" href="<%=path %>/css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet" />	
	    <link href="<%=path %>/css/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=path%>/js/jquery-1.5.min.js"></script>
		<script type="text/javascript" src="<%=path %>/js/jquery-ui-1.8.10.custom.min.js"></script>
		<script type="text/javascript" src="<%=path%>/js/jquery.autocomplete.js"></script>	
		<script type="text/javascript" src="<%=path%>/js/popShortcut.js"></script>
		<link href="../css/base.css" rel="stylesheet" type="text/css">
		<script type="text/javascript">
		$(function() {
			var mId="${param.menuId}";
			imgDisplay(mId);
		});
	  </script>
	  
	   <script language="javascript">
        $(function(){
        	function initAutoComplete(json,valId,valCode)
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
	        	  		 return  row.name + " (" + row.spell + ")";
	        	        //formatMatch是配合formatItem使用，作用在于，由于使用了formatItem，所以条目中的内容有所改变，而我们要匹配的是原始的数据，所以用formatMatch做一个调整，使之匹配原始数据
	        	    },
	        	    formatResult: function(row) {
	            		 return row.name;
	        	        //formatResult是定义最终返回的数据，比如我们还是要返回原始数据，而不是formatItem过的数据
	        	    }
	        	    }).result(function(event, row, formatted) {
	        	    	    
	        	    	     if(row!=undefined || row!=null){
	        	    	    	 $(valCode).attr("value", row.id);
	        	    	     }else{
	        	    	    	 $(valCode).attr("value", "");
	        	    	     }
	        	    });
	            }

        	$.getJSON('<%=path %>/search/unitBasic.action?time='+Math.random(),{ id:''}, function(json) {
	        	  initAutoComplete(json,"#unitName","#unitNameCode");
	        	} );
        	
        	$.getJSON('<%=path %>/search/projectNameBasic.action?time='+Math.random(),{ id:''}, function(json) {
	        	  initAutoComplete(json,"#projectName","#projectNameCode");
	        	} );
        });
        </script>
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
							缩略图生成
						</h3>
					</div>
				</td>
			</tr>
			<tr>
				<td class="contmiddle2">
					<form action="generateThumbnails.action?searchFlag=cjsy" method="post">
						<table width="730" border="0" cellpadding="0" cellspacing="5">
							<tr>
								<td width="14%">网省公司</td>
								<td ><input type="text" name="unitName" id="unitName" value="${unitName}" size="30"/></td>
								<td width="7%">工程</td>
								<td><input type="text" name="projectName" id="projectName" value="${projectName}" size="40"/></td>
							    <td width="7%" align="right">
									<input type="submit" name="button3" id="button3" value="重新生成"
										class="chaxunbutton" />
								</td>
							</tr>
						</table>
						<input type="hidden" name="unitNameCode" id="unitNameCode" value="${unitNameCode}"/>
						<input type="hidden" name="projectNameCode" id="projectNameCode" value="${projectNameCode}"/>
					</form>
					<form action="generateThumbnails.action?searchFlag=qbcj" method="post">
						<input type="submit" name="button4" id="button4" value="全部生成" class="chaxunbutton" />
						${message}
					</form>
					<div class="bianxian"></div>
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
