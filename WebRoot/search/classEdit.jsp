<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>分类定义操作页面</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" href="../css/themes/jquery.ui.all.css" type="text/css">
<link rel="stylesheet" href="../css/demos.css" type="text/css">
<link href="../css/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
<link href="../css/base.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../js/jquery-1.5.min.js" ></script>
<script src="../js/jquery.bgiframe-2.1.2.js"></script>
<script src="../js/jquery.ui.core.js"></script>
<script src="../js/jquery.ui.widget.js"></script>
<script src="../js/jquery.ui.mouse.js"></script>
<script src="../js/jquery.ui.button.js"></script>
<script src="../js/jquery.ui.draggable.js"></script>
<script src="../js/jquery.ui.position.js"></script>
<script src="../js/jquery.ui.resizable.js"></script>
<script src="../js/jquery.ui.dialog.js"></script>
<script src="../js/jquery.effects.core.js"></script>
<script type="text/javascript" src="../js/jquery.form.js"></script>
<script type="text/javascript" src="../js/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../js/jquery.validate.1.8.js"></script>
<script type="text/javascript" src="js/classEdit.js"></script>
<style type="text/css">
label.error {
	float: left;
	color: red;
	padding-left: .5em;
	vertical-align: middle;
}
</style>

<script type="text/javascript">
	var f="${nodeId}";
	var tName="${typeName}";
	//关闭窗口前,先让typeName失去焦点 不然关闭时JS有错
	function  window.onbeforeunload()  { 
		 document.myForm.source.focus();
	      }
    
	function getFocus(){
  	  document.myForm.source.focus();
    }
</script>
</head>
<body onload="getFocus()">

<!-- 结果预览 -->
<div id="dialog-modal" title="结果预览" style="display: none;">
  <center>
    <table width="90%" border="0" align="center" cellpadding="0" cellspacing="1" class="biaoge" id="addRow">
      <tr class="bgtr01">
        <td>编码</td>
        <td>名称</td>
      </tr>
    </table>
    <br/>
    <button onClick="test()" class="delbutton">关闭</button>
  </center>
</div>
<!-- 添加参数-->
<div id="dialog-form" title="添加参数" style="display: none;">
  <p class="validateTips" style="font-weight:bold; color:#11437f; border-bottom:#11437f 1px dotted; margin:0 0 5px 0;">请输入参数</p>
  <form id="dForm" >
    <fieldset id="fset" >
    </fieldset>
  </form>
</div>
<!-- 数据字典-->
<div id="dialog-form-dictionary" title="检索条件" style="display: none;">
  <table border="0" cellpadding="0" cellspacing="1" class="biaoge">
    <tr>
      <td>字典名称
        <input type="text" name="dictionaryName" id="dictionaryName"/></td>
      <td>字典编码
        <input type="text" name="dictionaryCode" id="dictionaryCode" readonly="readonly"/></td>
    </tr>
  </table>
  <table width="462" border="0" cellspacing="5" cellpadding="0">
    <tr>
      <td width="462" align="center"><button onClick="result()" class="chaxunbutton">查询</button></td>
    </tr>
  </table>
  <div>
    <table width="600" border="0" cellpadding="0" cellspacing="1" class="biaoge" id="addRow2">
      <tr class="bgtr01">
        <td>选择</td>
        <td>字典编码</td>
        <td>字典名称</td>
        <td>权限控制</td>
        <td>创建人</td>
        <td>创建时间</td>
        <td>备注</td>
      </tr>
    </table>
  </div>
</div>
<table border="0" align="center" cellpadding="0" cellspacing="0" class="contert2" style="width:550px;">
 <tr>
         <td class="conttop2"><div class="top2left"></div>
             <div class="top2right"></div>
           <div class="top2middle"><span class="topico2"><img src="<%=path %>/images/Accordion_ico03.gif"></span>
                 <h3>添加</h3>
         </div></td>
       </tr>
       <tr>
         <td height="311" class="contmiddle2">
         <form action="<%= path%>/search/addClassConfig.action" id="myForm" name="myForm" method="post">
           <table border="0" align="center" cellpadding="0" cellspacing="1" class="biaoge" style="font-size:12px; margin:5px;">
    <tr>
      <td bgcolor="#FFFFFF">分类名称</td>
      <td bgcolor="#FFFFFF"><input type="text" name="typeName" id="typeName" value="${typeName }" onBlur="nameExistence(this.value)"/>
        <span style="color: red;" id="existence"></span></td>
    </tr>
    <tr>
      <td bgcolor="#FFFFFF" colspan="2"><table width="216" border="0" cellspacing="5" cellpadding="0">
          <tr>
            <td width="108"><button id="popWin" onClick="topwin()" class="upbutton">数据字典</button></td>
            <td width="108"><button onClick="showUI()" class="dowbutton">结果预览</button></td>
          </tr>
        </table></td>
    </tr>
    <tr>
      <td bgcolor="#FFFFFF">数据源</td>
      <td bgcolor="#FFFFFF">
      	 <textarea id="source" name="source" rows="5" cols="50">${source }</textarea>
      </td>
    </tr>
    <tr>
      <td bgcolor="#FFFFFF">参数代码</td>
      <td bgcolor="#FFFFFF"><input type="text" name="params" id="params" value="${params }" /></td>
    </tr>
    <tr>
      <td bgcolor="#FFFFFF">备注</td>
      <td bgcolor="#FFFFFF"><textarea id="remark" name="remark" rows="3" cols="50">${remark }</textarea></td>
    </tr>
    <tr align="center">
      <td bgcolor="#FFFFFF" colspan="2">
         <input type="hidden" name="nodeId" id="nodeId" value="${nodeId }"/>
         <input type="submit" value="保存" class="querenbutton"/>
         <input type="reset" value="重置" class="qkbutton"/>
         <input type="button" value="关闭" onClick="closeWin()" class="qkbutton"/>
      </td>
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
