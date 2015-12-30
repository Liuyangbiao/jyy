<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>工程电子档案系统</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	 <link rel="stylesheet" type="text/css" href="<%=path %>/css/base.css" />
	 <link rel="stylesheet" type="text/css" href="<%=path %>/css/jquery.autocomplete.css" />
	 <link rel="stylesheet" type="text/css" href="<%=path %>/css/themes/ui.jqgrid.css" />
	 <link rel="stylesheet" type="text/css" href="<%=path %>/css/themes/ui.multiselect.css" />
	 <link rel="stylesheet" type="text/css" href="<%=path %>/css/themes/jquery-ui-1.8.11.custom.css" />

	 <script type="text/javascript" src="<%=path %>/js/jquery-1.5.min.js" ></script>
	 <script type="text/javascript" src="<%=path %>/js/jquery-ui-1.8.10.custom.min.js"></script>
	 <script type="text/javascript" src="../archives/js/docDetails.js"></script>
	 <script type="text/javascript" src="<%=path %>/js/jquery.jqGrid.js" ></script> 
	 <script type="text/javascript" src="<%=path %>/js/jquery.maskedinput.js" ></script>
	 <script type="text/javascript" src="<%=path %>/js/jquery.form.js" ></script>
	 <script type="text/javascript" src="../js/jquery.cookie.js"></script>
	 
    
		
     <script type="text/javascript">
     $(function(){
        $("#onLineSeeDIV").hide();
     	docInfoDialog();
     	
     	$('#dialog').dialog({

             width: 600,
             autoOpen: false,//打开模式窗体
             resizable: true,//是否可以调整模式窗体的大小
             draggable: true,//是否可以拖动模式窗体
             modal: true,//启用模式窗体
             closeOnEscape: true,//按下esc可以退出
             close: function(){
                 $("#preimge").attr("src", "../images/circle_animation.gif");
             }
         });
         
         $('#showdialog').dialog({
	        width: 200,
	        height: 200,
	        autoOpen: false,//打开模式窗体
	        resizable: false,//是否可以调整模式窗体的大小
	        draggable: false,//是否可以拖动模式窗体
	        modal: true,//启用模式窗体
	        closeOnEscape: true,//按下esc可以退出
	        bgiframe: true,
	        buttons: {
	            "确定": function(){
	                var tdocname = $("#tdocname").val();
	                var stringsplit;
	                var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
	                for (var i = 0; i < selectedIds.length; i++) {
	                    if (selectedIds[i] != undefined) {
	                        stringsplit += "," + selectedIds[i];
	                    }
	                }
	                var url = "../search/getPackAgeDownLoad.action?eachCheckbox=" + stringsplit + "&filename=" + tdocname;
	                $("#packAgeDownLoad").attr("src", url);
	                $("#showdialog").dialog('close');
	                
	            }
	        },
	        close: function(){
	            $("#tdocname").attr("val", "");
	        }
	        
	    });
     });

     function divShow(){
    	 $("#onLineSeeDIV").hide(); 
		 $("#mainDIV").show();
		 document.ols.location.href="about:blank";
     }
     
     //点击在线查看时触发的函数
	function onlineDown(tempid){
	    //url = "getOnLineSee.action?onlineid=" + tempid;
	    //window.open(url, "在线查看", "height=500,width=700,location=no,toolbar=no,status=no,resizable=yes");
		//this.location="onLineSee.jsp?tempid="+tempid;
		//window.navigator("onLineSee.jsp?tempid="+tempid);
		 document.ols.location.href="../search/onLineSee.jsp?tempid="+tempid;   
		 $("#onLineSeeDIV").show(); 
		 $("#mainDIV").hide();
	}

	//点击预览时触发的函数
	function previewImage(id){
	    $("#preimge").attr("src", "../search/getPreView.action?id=" + id);
	    $('#dialog').dialog('open');
	}
	
	//点击打包下载时触 发的事件
	function packAgeDownLoad(){
	    var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
	    var len = selectedIds.length;
	    if (len == 0) {
	        alert("请选择打包下载的文件!");
	    }
	    else {
	        $("#showdialog").dialog('open');
	    }
	    
	}
     
     function goBack(){
    	 window.history.back(-1);
     }

	     var tempurl = "<%=path %>/global/inFormationBasicSearchURL.action";
	     $(document).ready(function(){

         $("#rowedtable").jqGrid({
             Search: true,
             url: tempurl,
             datatype: "json",
             colNames: ['文件名称', '档案类别', '工程名称', '格式', '上传时间', '文件大小', '操作'],
             colModel: [{
                 id: 'filename',
                 name: 'filename',
                 index: 'filename',
                 width: 200,
                 align: 'center'
             }, {
                 id: 'filetype',
                 name: 'filetype',
                 index: 'filetype',
                 width: 150,
                 align: 'center'
             }, {
                 name: 'worksname',
                 index: 'worksname',
                 width: 160,
                 align: 'center'
             }, {
                 name: 'style',
                 index: 'style',
                 width: 80,
                 align: 'center'
             }, {
                 name: 'upload',
                 index: 'upload',
                 width: 100,
                 align: 'center'
             }, {
                 name: 'filesize',
                 index: 'filesize',
                 width: 100,
                 align: 'center'
             }, {
                 name: 'operate',
                 index: 'operate',
                 width: 170,
                 align: 'center'
             }],
             rowNum:10,  
             multiselect: true,
             caption: "档案查询",
             forceFit: true,// 是否超出DIV
             pager: '#prowed4',
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
                 id: "id",
                 flag:"jump"
             },
             onSelectRow: function(id){      
               //  alert("第"+id+"行被选中");     
             }
             
         });
     });
     
     </script>
     
  </head>
  
  <body>
      <div id="mainDIV">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2">
            <tr>
                <td class="conttop2">
                    <div class="top2left">
                    </div>
                    <div class="top2right">
                    </div>
                    <div class="top2middle">
                     <span class="topico2"><img src="../images/Accordion_ico03.gif"></span><h3>查询结果</h3>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="contmiddle2">
		    <table width="150" border="0" cellspacing="5" cellpadding="0">
		      <tr>
		        <td><input type="button" name="button6" id="button4" value="打包下载"  class="dowbutton" onClick="packAgeDownLoad()"/></td>
		      </tr>
		    </table>
     
          <div style="width:750px;">
            <table id="rowedtable" width="100%"></table>
            <div id="prowed4"></div>
          </div>
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
        <div id="dialog" title="图片预览" style="display: none;">
            <img id="preimge" src="../images/circle_animation.gif" alt="图片" style="width:100%">
        </div>
        <iframe id="packAgeDownLoad" style="width:200;height:200;display:none;">
        </iframe>
        <div id="showdialog" title="打包下载">
           	 请输入名称：<input type="text" name="tdocname" id="tdocname">
        </div>
         <div id="docInfo" title="详细信息">
        <table width="100%" height="100%" cellspacing="2" cellpadding="5" style="font-size:12px;">
        <tr>
        	<td>
            <div>
                <ul id="docDetails">
                    <h3 style="padding-bottom:10px;">文档信息</h3>
                    <li id="docName">
                    </li>
                    <li id="docClass">
                    </li>
                    <li id="docFormat">
                    </li>
                    <li id="docFileSize">
                    </li>
                    <li id="docVersion">
                    </li>
                    <li id="docCreate">
                    </li>
                    <li id="docCreateDate">
                    </li>
                </ul>
            </div>
            </td>
            <td>
            <div>
                <ul id="proDetials">
                    <h3 style="padding-bottom:10px;">工程信息</h3>
                    <li id="ownerCode">
                    </li>
                    <li id="proCode">
                    </li>
                    <li id="proName">
                    </li>
                    <li id="projectType">
                    	jkkdi
                    </li>
                    <li id="proVoltagelevel">
                    </li>
                    <li id="proOpenYear">
                    </li>
                    <li id="projectStatus">
                    </li>
                    <li id="proParent">
                    </li>
                </ul>
            </div>
            </td>
            </tr>
            <tr>
            <td></td>
            <td>
            <div style="padding-top:10px;">
                <ul id="preDesignDetails">
                    <h3>初设信息</h3>
                    <li id="unitName">
                    </li>
                    <li id="createDate">
                    </li>
                    <li id="ajtm">
                    </li>
                    <li id="andh">
                    </li>
                    <li id="flbh">
                    </li>
                    <li id="jcrm">
                    </li>
                    <li id="jnyh">
                    </li>
                    <li id="ljrm">
                    </li>
                    <li id="pzrm">
                    </li>
                    <li id="shrm">
                    </li>
                    <li id="sjjd">
                    </li>
                    <li id="sjrm">
                    </li>
                    <li id="tzmc">
                    </li>
                    <li id="tzzs">
                    </li>
                    <li id="xhrm">
                    </li>
                </ul>
            </div>
            </td>
            </tr>
            </table>
        </div>
     </div>
     <div id="onLineSeeDIV">
         <iframe name="ols" id="ols" src="" height="100%" width="100%"></iframe>
     </div>
  </body>
</html>
