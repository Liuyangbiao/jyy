<%@ page language="java" import="java.util.*,com.nsc.dem.action.system.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>折叠菜单</title>
		<link href="<%=path %>/css/accordion.css" rel="stylesheet" type="text/css">
		<script type="text/javascript"
			src="<%=path %>/js/jquery-1.5.min.js"></script>
		<script type="text/javascript"
			src="<%=path %>/js/jquery-ui-1.8.10.custom.min.js"></script>
		<script type="text/javascript">
		$(function() {
			// Accordion
			$("#accordion").accordion( {
				header : "h3",
				fillspace : true,
				collapsible : true
			/*默认情况下，总有一个是展开的，该选项让所有菜单都可收起*/
			});
		});

		//可令div能够重定义大小
		$(function() {
			$("#accordionResizer").resizable( {
				minHeight : 300,
				minWidth : 160,
				resize : function() {
					$("#accordion").accordion("resize");
				}
			});
		});

			/*	//拖动菜单头使菜单重新排序，
			$(function() {
				var stop = false;
		
				$( "#accordion h3" ).click(function( event ) {
					if ( stop ) {
						event.stopImmediatePropagation();
						event.preventDefault();
						stop = false;
					}
				});
				$( "#accordion" )
					.accordion({
						header: "> div > h3"
					})
					.sortable({
						axis: "y",
						handle: "h3",
						stop: function() {
							stop = true;
						}
					});
			});
			 */

		function goURL(goUrl,id) {
		    id=id==undefined?"":id;
			if(goUrl.indexOf("?")!=-1){
				goUrl=goUrl+"&menuId="+id;
			}else{
				goUrl=goUrl+"?menuId="+id;
			}
			window.parent.frames['mainContent'].location.href = "../"+goUrl;
		}
	</script>
			<style type="text/css">
			/*demo page css*/
			body {
				font-size: 12px;
				margin-top: 0px;
				margin-left: 0px;
			}
			
	</style>

	</head>
	<body style="background-color:#ECF1F6;">

		<!--手风琴菜单开始-->
        <div id="accordionResizer" style="padding:0px; width:190px;" >
    
			<div id="accordion">
   			      <% List<TMenu> tm = (List<TMenu>)request.getAttribute("tm");
 					if(tm.size()!=0){%>
	 					<div id="kuaishu">
					<h3><strong class="zhubg00"><a href="#">快速定位</a></strong></h3><!--其中zhubg00是单独图标的样式-->
						<!--二级菜单开始-->
	                    <div class="acccont">
						  <ul style="margin:0;">
						    <s:iterator value="tm">
								<li onclick="javascript:goURL('<s:property value="url" />','7',this)">
									<span><img src="<%=path %><s:property value="image" />"></span>
									<s:property value="lable" />
								</li>
							</s:iterator>
	                      </ul>
						</div>
	                    <!--二级菜单结束-->
				  </div>

 <% }
%>
			 <!--主菜单开始-->
	            
	                <!--主菜单结束-->
				<s:iterator value="tMenuList">
					<div>
						<h3>
							<strong class="<s:property value="image" />"><a href="#"><s:property value="lable" /></a></strong>
						</h3>
						<s:if test="!leaf">
							<div class="acccont">
								
								<ul style="height: ${height };margin:0;">
									<s:iterator value="list">
										<li onclick="javascript:goURL('<s:property value="url" />','<s:property value="id"/>')">
										<span><img src="<%=path %><s:property value="image" />"></span>
											<s:property value="lable" />
										</li>
									</s:iterator>
								</ul>
							</div>
						</s:if>
					</div>
				</s:iterator>
			</div>
		</div>
	</body>
</html>
