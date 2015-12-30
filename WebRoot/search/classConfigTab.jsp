<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<base target="_self" />
    <title>My JSP 'classConfigTab.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link type="text/css" href="../css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet" />	
	<link href="<%=path %>/css/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.multiselect.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="../css/base.css" >
	 <script type="text/javascript" src="<%=path %>/js/jquery-1.5.min.js" ></script>
	 <script type="text/javascript" src="<%=path %>/js/jquery.cookie.js" ></script>
     <script type="text/javascript" src="../js/jquery.jqGrid.js" ></script> 
	 <script type="text/javascript" src="<%=path %>/js/jquery-ui-1.8.10.custom.min.js"></script>
	<script type="text/javascript" src="../js/jquery.autocomplete.js"></script>	
	<script type="text/javascript" src="<%=path %>/js/jquery.form.js"></script> 
	<script type="text/javascript" src="<%=path %>/js/popShortcut.js"></script>
		<script type="text/javascript">
		$(function() {
			var mId="${param.menuId}";
			imgDisplay(mId);
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

			//分类定义--点击删除时调用该方法
			function del(){
				var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
				var len=selectedIds.length;
				if(len==0){
					alert("请选择要删除的对象!");
				}else{
			        var r=confirm("确认删除所选中的值吗?");
			        if(r){
			        	 window.location.href="<%=path %>/search/delClassConfig.action?id="+selectedIds;
			        	 alert("删除成功!");
			        }
				}
			}

			//分类等级配置--点击删除时调用该方法
			function del2(){
				var selectedIds = $("#configTable").jqGrid("getGridParam", "selarrrow");
				var len=selectedIds.length;
				if(len==0){
					alert("请选择要删除的对象!");
				}else{
			        var r=confirm("确认删除所选中的值吗?");
			        if(r){
			        	 window.location.href="<%=path %>/search/delClassLevelConfig.action?id="+selectedIds;
			        	 alert("删除成功!");
			        }
				}
			}
			
            //分类定义--点击更新时调用该方法
			function edit(id){
				var re=window.showModalDialog("<%= path%>/search/editClassConfig.action?id="+id, "test",
						"status:no;dialogWidth:" + 750 + "px; dialogHeight:" + 450
	            	          + "px");
				if(re==1){
					definedWin();
			    }
            }

			//分类等级配置--点击更新时调用该方法
			function edit2(id){
				var re=window.showModalDialog("<%= path%>/search/editClassLevelConfig.action?id="+id, "test",
						"status:no;dialogWidth:" + 750 + "px; dialogHeight:" + 480
	            	          + "px");
				if(re==1){
					configWin();
			    }
            }

			//分类等级配置--点击角色分配时调用该方法
            function roleAssign(id){
                $("#tdId").attr("value",id);
            	 var table = document.getElementById("addRow");
		           var len=table.rows.length;
		           for(var i=len-1;i>0;i--){
		        	   table.deleteRow(i);
		           }

					$.getJSON('<%= path%>/search/roleClassLevelConfig.action?time='+Math.random(),{ id:id }, function(json) {
						if(0==json.length){
                            alert("查询数据不存在!");
						}else{
							for(var i in json){   
								var newRow = table.insertRow(table.rows.length);
								//如果json[i].existence=='1' 则说明该角色是已经选择上的
								if(json[i].existence=='1'){
									var newTd1 = newRow.insertCell(0);newTd1.innerHTML='<input type="checkbox" checked name="boxName" value="'+json[i].code+'"/>';
								   }else{
									var newTd1 = newRow.insertCell(0);newTd1.innerHTML='<input type="checkbox" name="boxName" value="'+json[i].code+'"/>';
									}
								var newTd2 = newRow.insertCell(1);newTd2.innerHTML=json[i].name; 
								//如果json[i].isUsable=='1' 则说明该角色的是否可用是已经选择上的
								if(json[i].isUsable=='1'){
									var newTd3 = newRow.insertCell(2);newTd3.innerHTML='<input type="checkbox" checked name="boxFlag" value="'+json[i].code+'"/>';
								  }else{
									var newTd3 = newRow.insertCell(2);newTd3.innerHTML='<input type="checkbox" name="boxFlag" value="'+json[i].code+'"/>';
									}
				 	           }
					  	  }
				     });
				
				$( "#dialog-modal" ).dialog({
					height:300,
					width:300,
					modal: true
				  });	
            }

            function saveRole(){
                 var id=$("#tdId").attr("value");
            	 var bn = document.getElementsByName("boxName");
            	 var bf = document.getElementsByName("boxFlag");
            	 var bnames=""; var bfalgs="";
            	 //得到所有被选中的  是否有效
            	 for(var i = 0 ;i<bf.length;i++)
            	 {
            	  if(bf[i].checked)
            	    {
            		  bfalgs=bfalgs+bf[i].value+",";
            	    }
            	 } 
            	 //得到所有被选中的角色ID
            	 for(var i = 0 ;i<bn.length;i++)
            	 {
            	  if(bn[i].checked)
            	    {
                       bnames=bnames+bn[i].value+",";
            	    }
            	 }  	
            	 var r=confirm("确认保存吗?");
			        if(r){
		            	 $.getJSON('<%= path%>/search/saveRoleTreeClassLevelConfig.action?time='+Math.random(),{ id:id,bn:bnames,bf:bfalgs }, function(json) {
								alert("保存成功!");
						     }); 
			        }
			        $("#dialog-modal").dialog("close");
            }
			function closeWin(){
			      $("#dialog-modal").dialog("close");
				}

			//弹出新窗口----分类等级配置-添加
			function popWin(){
				 var re=window.showModalDialog("<%= path%>/search/classConfigPop.jsp", "test",
	            	      "status:no;dialogWidth:" + 750 + "px; dialogHeight:" + 480
	            	          + "px");
				 if(re==1){
					 configWin();
				    }
			}
			
			//弹出新窗口----分类定义-添加
			function popWinDefine(){
				var re=window.showModalDialog("<%= path%>/search/classEdit.jsp", "test",
						"status:no;dialogWidth:" + 750 + "px; dialogHeight:" + 450
	            	          + "px");
				if(re==1){
					 definedWin();
			    }
			}

		</script>
		<style type="text/css">
			/*demo page css*/
			body{ font-size: 12px; }
		</style>
		
		<script type="text/javascript">
		function roleNameBlur(){
			var roleName = $("#roleName").val();
        	if(roleName.length==0){
            	$("#roleNameCode").attr("value","");
            	}
		 }
        //分类配置  分页查询
		function configWin(){
			$("#configTable").clearGridData();
            var tln=$("#typeLevelName").val();
            tln=escape(encodeURIComponent(tln)); 
			var rnc=$("#roleNameCode").val();
			jQuery("#configTable").jqGrid('setGridParam',{url:"<%= path%>/search/displayClassLevelConfig.action?levelName="+tln+"&roleCode="+rnc}).trigger("reloadGrid");
		}

        //分类定义 分页查询
		function definedWin(){
		    $("#rowedtable").clearGridData();
			var tn=$("#typeName").val();
			tn=escape(encodeURIComponent(tn)); 
			jQuery("#rowedtable").jqGrid('setGridParam',{url:"<%= path%>/search/displayClassConfig.action?name="+tn}).trigger("reloadGrid");
		}
		
		var tempurl = "<%= path%>/search/displayClassConfig.action?name=${typeName}";
	     var configUrl = "<%= path%>/search/displayClassLevelConfig.action?levelName=${typeLevelName}&roleCode=${roleNameCode}";
	     $(document).ready(function(){
	    	 $("#rowedtable").jqGrid({
	             Search: true,
	             url: tempurl,
	             datatype: "json",
	             colNames: ['分类名称', '类型', '数据源', '创建者', '创建时间', '操作'],
	             colModel: [ {
	                 name: 'name',
	                 index: 'name',
	                 width: 200,
	                 align: 'center'
	             }, {
	                 name: 'type',
	                 index: 'type',
	                 width: 140,
	                 align: 'center'
	             }, {
	                 name: 'source',
	                 index: 'source',
	                 width: 230,
	                 align: 'center'
	             }, {
	                 name: 'creator',
	                 index: 'creator',
	                 width: 100,
	                 align: "right"
	             }, {
	                 name: 'createDate',
	                 index: 'createDate',
	                 width: 140,
	                 align: 'center'
	             },{
	                 name: 'operate',
	                 index: 'operate',
	                 width: 120,
	                 align: 'center'
	             }],
	             rowNum:10,  
	             multiselect: true,
	  //         rowList: [10, 20, 30],
	             caption: "分类定义",
	             forceFit: true,// 是否超出DIV
	             pager: '#prowed4',
	             viewrecords: true,// 是否显示多少行
	             multiselectWidth:'40',// 调整选择的宽度
	             emptyrecords: '数据为空',// 空记录时的提示信息
	             sortable:false,//是否支持排序,
	             loadtext:"正在加载数据，请稍候……",
	 //          sortorder: "desc",
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
	              //   alert("第"+id+"行被选中");     
	             }
	             
	         });
		 
                 //分类等级配置
		         $("#configTable").jqGrid({
		             Search: true,
		             url: configUrl,
		             datatype: "json",
		             colNames: ['分类类别名称', '备注', '分类名称', '创建者', '创建日期', '操作'],
		             colModel: [{
		                 name: 'name',
		                 index: 'name',
		                 width: 200,
		                 align: 'center'
		             }, {
		                 name: 'remark',
		                 index: 'remark',
		                 width: 150,
		                 align: 'center'
		             }, {
		                 name: 'parentType',
		                 index: 'parentType',
		                 width: 160,
		                 align: 'center'
		             }, {
		                 name: 'creator',
		                 index: 'creator',
		                 width: 100,
		                 align: 'center'
		             }, {
		                 name: 'createDate',
		                 index: 'createDate',
		                 width: 150,
		                 align: 'center'
		             },{
		                 name: 'operate',
		                 index: 'operate',
		                 width: 170,
		                 align: 'center'
		             }],
		             rowNum:10,  
		             multiselect: true,
		  //         rowList: [10, 20, 30],
		             caption: "分类等级配置",
		             forceFit: true,// 是否超出DIV
		             pager: '#configProwed',
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
     
     <script language="javascript">
        $(function(){
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
	        	    	    	 $("#roleNameCode").attr("value", row.code);
	        	    	     }else{
	        	    	    	 $("#roleNameCode").attr("value", "");
	        	    	     }
	        	    });
	            }

        	$.getJSON('<%= path%>/search/roleClassLevelConfig.action?time='+Math.random(),{ id:''}, function(json) {
	        	  initAutoComplete(json,"#roleName");
	        	} );
        });
        </script>
 
 <style type="text/css">
 		.biaoge td {
				padding: 5px;
				text-align:center;
			}
			
			.biaoge .bgtr01 {
				background-image: url(../images/biaogetitlebg.gif);
				background-repeat: repeat-x;
				background-position: 50% 50%;
				background-color: #48acff;
				font-weight: bold;
				color: #476c89;
				height: 20px;
			}
 </style> 
  </head>
  
  <body>
    <!-- Tabs -->
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2">
		  <tr>
		    <td class="conttop2"><div class="top2left"></div>
		      <div class="top2right"></div>
		      <div class="top2middle">
		        <span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif" onclick="topwin(${param.menuId })" style="cursor:hand"></a></span>
		        <span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span>
		        <span class="topico2"><img src="../images/Accordion_ico03.gif"></span>
		        <h3>分类检索配置</h3>
		      </div></td>
		  </tr>
		  <tr>
		    <td class="contmiddle2">
		     <table width="750" border="0" cellspacing="0" cellpadding="0"  style="font-size:12px; font-weight:bold;">
		     <tr>
              <td>
		      <div id="tabs" align="center">
		        <ul>
		          <li><a href="#tabs-1">分类定义</a></li>
		          <li><a href="#tabs-2">分类等级配置</a></li>
		        </ul>
		        <div id="tabs-1">
		          <form action="<%= path%>/search/showClassConfig.action" method="post">
		            <table width="426" cellspacing="5" style="font-size:14px;">
		              <tr>
		                <td width="265" height="36" align="left" valign="middle"><strong style="font-size:14px;">分类名称:</strong>&nbsp;                  
							<input type="text" name="typeName" id="typeName"/>
		                </td>
		                <td width="140" align="left" valign="middle">
		                  <button onClick="definedWin()" class="chaxunbutton"><span style="font-size:14px;">查询</span></button>
		                  </td>
		              </tr>
		            </table>
		            <div class="bianxian"></div>
		         </form>
		         <div> 
		         <table width="177" border="0" cellspacing="5" cellpadding="0">
		 				 <tr>
						    <td><button onClick="popWinDefine()" class="querenbutton"><span style="color:#11437f; font-size:12px;">增 加</span></button></td>
						    <td><button onClick="del()" class="qkbutton"><span style="color:#375100; font-size:12px;">删除</span></button></td>
		 				 </tr>
				 </table>
					<br/>
		            <table id="rowedtable"></table>
		            <div id="prowed4"></div>
		        </div>
		      </div>
		      <div id="tabs-2">
		          <form action="<%= path%>/search/displayClassLevelConfig.action" method="post" id="myForm" name="myForm">
		            <table width="673" style="font-size:14px;">
		              <tr>
		                <td width="295" height="32"><strong style="font-size:14px;">分类列表名称:</strong>
		                  <input type="text" name="typeLevelName" id="typeLevelName" value="${typeLevelName }"/>
						</td>
		                <td width="256"><strong style="font-size:14px;">角色名称:</strong>
		                  <input type="text" id="roleName" name="roleName" onblur="roleNameBlur()"/>
		                  <input type="hidden" id="roleNameCode" name="roleNameCode"/>
		                  </td>
		                <td width="106" align="left"><button onClick="configWin()" class="chaxunbutton"><span style="font-size:14px;">查询</span></button>     </td>
		              </tr>
		            </table>
		            <div class="bianxian"></div>
		          </form>
		          <!-- 文档等级 配置分页表-->
		          <div>
		          <table width="190" border="0" cellspacing="5" cellpadding="0">
		  				 <tr>
						    <td width="92"><button onClick="popWin()" class="querenbutton"><span style="color:#11437f; font-size:12px;">增加</span></button></td>
						    <td width="83"><button onClick="del2()" class="qkbutton"><span style="color:#11437f; font-size:12px;">删除</span></button></td>
					     </tr>
				  </table><br/>
		              <table id="configTable" width="100%"></table>
		              <div id="configProwed"></div>
		          </div>
		          <!-- 角色分配 弹出窗口 -->
		          <div id="dialog-modal" title="角色分配" style="display: none;">
		            <center>
		              <table width="90%" border="0" align="center" cellpadding="0" cellspacing="1" id="addRow" class="biaoge">
		                <tr  class=" bgtr01">
		                  <td>选择</td>
		                  <td>角色名称</td>
		                  <td>有效</td>
		                </tr>
		              </table>
		              <br/>
		              <input type="hidden" id="tdId" name="tdId" value=""/>
		              <button onClick="saveRole()" class="querenbutton">保存</button>
		              <button onClick="closeWin()" class="qkbutton">取消</button>
		            </center>
		          </div>
		        </div>
		      </div></td>
		     </tr>
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
