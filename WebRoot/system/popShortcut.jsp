<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>快捷菜单设置</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link href="<%=path %>/css/base.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="<%=path %>/js/jquery-1.6.2.min.js"></script>
		<script type="text/javascript">
         $(document).ready(function() {  
               // 初始化ListBox 右边   即该用户已存在的快捷菜单
     			$.getJSON('system/initMenuShortcut.action?time='+Math.random(),{ id:'' }, function(json) {
     				menu(json,"listRight");
     	        	}
     	        );
     		   // 初始化ListBox 左边   即该用户准备添加的快捷菜单
     			$.getJSON('system/newMenuShortcut.action?time='+Math.random(),{ id:${param.id }}, function(json) {
     				menu(json,"listLeft");
     	        	}
     	        );
     			//ListBox 初始化显示
     			function menu(json,flag){
    				var s_root=document.getElementById(flag);   
    				s_root.options.length=0;  
    				var option = document.createElement("option"); 
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
     			}
           
             //right move  
             $("#btnRight").click(function() {  
                 moveright();  
             });  
             //double click to move left  
             $("#listLeft").dblclick(function() {  
                 moveright();  
             });  
     
             //left move   
             $("#btnLeft").click(function() {  
                 moveleft();  
             });  
     
             //double click to move right  
             $("#listRight").dblclick(function() {  
                 moveleft();  
             });  

             //up move   
             $("#btnUp").click(function() {  
                 moveup();  
             }); 
             //down move   
             $("#btnDown").click(function() {  
                 movedown();  
             });  
             function moveright() {  
                 //数据option选中的数据集合赋值给变量vSelect  
                 var vSelect = $("#listLeft option:selected");  
                 var obj = document.getElementsByTagName("select")[1]  ;
                 //左边最多只能有5个菜单
                 if(obj.options.length>=5){
                	//克隆数据添加到listRight中  
                     vSelect.clone().appendTo("#listRight");
    				 //同时移除listRight中的option  
                     vSelect.remove();  
                	 var lleft = document.getElementsByTagName("select")[0]; 
                	 var lright = document.getElementsByTagName("select")[1].options[0];  
                	 var option = document.createElement("option"); 
			            option.text=lright.text;   
			            option.value=lright.value;   
			            lleft.options[0] =option;
                	 obj.remove(obj.options[0]);
                 }  else{
                	//克隆数据添加到listRight中  
                     vSelect.clone().appendTo("#listRight");
    				 //同时移除listRight中的option  
                     vSelect.remove();  
                 }
             }  
             function moveleft() {  
                 var vSelect = $("#listRight option:selected");  
                 vSelect.clone().appendTo("#listLeft");  
                 vSelect.remove();  
             }  
			 function moveup(){
				var vSelected = $("#listRight option:selected");
				$(vSelected).prev().insertAfter($(vSelected));
			 }
			 function movedown(){
			 	var vSelected = $("#listRight option:selected");
				$(vSelected).next().insertBefore($(vSelected));
			 }
         });  

         function sub(){
           var obj = document.getElementsByTagName("select")[1]  ;
           var selectOptions = obj.options;  
           var len=selectOptions.length;
           var menuId="";
           //得到左边菜单的所有ID
           for(var i=0;i<len;i++){
                  menuId=menuId+selectOptions[i].value+",";
           }
           //更新操作
           $.getJSON('system/editShortcut.action?time='+Math.random(),{ menuIds:menuId }, function(json) {
        	        alert("操作成功!");
        	        window.returnValue="1";
        	        closeWin();
	        	} );
          
         }
         //窗口关闭
         function closeWin(){
        	   window.close();
         }
     </script>
		<style type="text/css">
h1 {
	color: Green;
}

#listLeft {
	width: 100px;
}

.normal {
	color: #11437f;
	font-weight: bold;
	font-size: 14px;
	width: 95px;
	background-color: #e3f1fa;
}
</style>
	</head>

	<body>
		<table border="0" cellspacing="0" cellpadding="0" class="contert2"
			style="width: 350px; margin-top: 50;" align="center">
			<tr>
				<td class="conttop2">
					<div class="top2left"></div>
					<div class="top2right"></div>
					<div class="top2middle">
						<span class="topico2"><img
								src="<%=path %>/images/Accordion_ico03.gif">
						</span>
						<h3>
							设置快捷
						</h3>
					</div>
				</td>
			</tr>
			<tr>
				<td height="206" class="contmiddle2">
					<form id="myForm" name="myForm"
						action="<%=path %>/search/saveClassLevelConfig.action"
						method="post" onSubmit="sub()">
						<table width="290" border="0" align="center" cellpadding="0"
							cellspacing="0" style="margin: 5px;">
							<tr>
								<td width="290" align="center" valign="top">
									<div style="width: 100px; float: left;">
										<select size="10" name="listLeft" id="listLeft" class="normal"
											title="双击可实现右移" multiple>
										</select>
									</div>
									<div style="width: 40px; float: left; padding-top: 60px;">
										<input type="button" id="btnRight" value="→"
											style="width: 25px" />
										<br />
										<input type="button" id="btnLeft" value="←"
											style="width: 25px" />
									</div>
									<div style="width: 100px; float: left;">
										<select size="10" name="listRight" id="listRight"
											class="normal" title="双击可实现左移">
										</select>
									</div>
									<div style="width: 40px; float: left; padding-top: 60px;">
										<input type="button" id="btnUp" value="↑" style="width: 25px" />
										<br />
										<input type="button" id="btnDown" value="↓"
											style="width: 25px" />
									</div>
									<input type="hidden" name="menuIds" id="menuIds" value="">
								</td>
							</tr>
						</table>
						<table width="99" border="0" align="center" cellpadding="0"
							cellspacing="5">
							<tr>
								<td width="42" height="21">
									<input type="button" onClick="sub()" value="设置"
										class="querenbutton" />
								</td>
								<td width="94">
									<input type="button" onClick="closeWin()" value="返回"
										class="qkbutton" />
								</td>
							</tr>
						</table>
					</form>
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
