<%@ page language="java"
import="java.util.*,com.nsc.dem.*,com.nsc.dem.bean.*" pageEncoding="UTF-8" %>
<%@page import="com.nsc.dem.service.archives.IarchivesService" %>
<%@page import="com.nsc.dem.service.system.IprofileService" %>
<%@page import="com.nsc.dem.bean.profile.TRole" %>
<%@page import="com.nsc.dem.action.system.TMenu" %>
<%@page import="com.nsc.dem.bean.profile.TUser" %>
<%@page import="com.nsc.base.util.Component" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
TUser user =(TUser) request.getSession().getAttribute("USER_KEY");
String mId=request.getParameter("menuId");
IprofileService profileService=(IprofileService)Component.getInstance("profileService",request.getSession().getServletContext());
boolean isDownLoad = profileService.getProfileByauthControl(user
, mId,  "下载");
request.setAttribute("isDownLoad",isDownLoad); %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/"; %>
<html>
    <head>
        <script src="../js/jquery-1.5.min.js" type="text/javascript">
        </script>
        <script src="../js/jquery-ui-1.8.10.custom.min.js" type="text/javascript">
        </script>
        <script src="../js/jquery.cookie.js" type="text/javascript">
        </script>
        <script src="../js/jquery.jqGrid.js" type="text/javascript">
        </script>
        <script type="text/javascript" src="../js/popShortcut.js">
        </script>
        <script type="text/javascript" src="../js/jquery.cookie.js">
        </script>
        <script type="text/javascript" src="../archives/js/docDetails.js">
        </script>
        <link href="../css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet" type="text/css">
        <link href="../css/base.css" rel="stylesheet" type="text/css">
        <link href="../css/skin-vista/ui.dynatree.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.jqgrid.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.multiselect.css" />
        <script type="text/javascript" src="../js/popShortcut.js">
        </script>
        <title>快速定位</title>
        <script type="text/javascript">
            $(function(){
                var mId = "${param.menuId}";
                imgDisplay(mId);
                var loginId = "${USER_KEY.loginId }";
            });
        </script>
        <script type="text/javascript">
            $(function(){
                $.ajaxSetup({
                    cache: false
                });
                $("#onLineSeeDIV").hide();
                docInfoDialog();
                $('#dialog').dialog({
                    width: 600,
                    autoOpen: false,//打开模式窗体
                    resizable: false,//是否可以调整模式窗体的大小
                    draggable: false,//是否可以拖动模式窗体
                    modal: true,//启用模式窗体
                    closeOnEscape: true,//按下esc可以退出
                    //bgiframe: true,
                    close: function(){
                        $("#preimge").attr("src", "../images/circle_animation.gif");
                    }
                });
                table();
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
                                    stringsplit += ":" + selectedIds[i];
                                }
                            }
                            tdocname = escape(encodeURIComponent(tdocname));
                            var url = "getPackAgeDownLoad.action?eachCheckbox=" + stringsplit + "&filename=" + tdocname;
                            $("#packAgeDownLoad").attr("src", url);
                            $("#showdialog").dialog('close');
                        }
                    },
                    close: function(){
                        $("#tdocname").attr("val", "");
                    }
                });
            })
            function divShow(){
                $("#onLineSeeDIV").hide();
                $("#mainDIV").show();
                document.ols.location.href = "about:blank";
            }
            
            //点击在线查看时触发的函数
            function onlineDown(tempid){
                document.ols.location.href = "onLineSee.jsp?tempid=" + tempid;
                $("#onLineSeeDIV").show();
                $("#mainDIV").hide();
            }
            
            var mId = "${param.menuId}";
            
            var tempurl = "getNodeListInFormation.action?menuId=" + mId;
            var nodes = new Array();
            function goURL(nodeId, menuId, params, title){
                $.ajaxSetup({
                    cache: false
                });
                var temptitle = $("#tempspan").html();
                if (params == null) {
                    $("#tempspan").html("");
                }
                else {
                    var site = params.split(";").length;
                    var hreftitle = "<a href='javascript:goURL(" + nodeId + ",7,\"" + params + "\",\"" + title + "\")'>" + title + "</a>";
                    nodes[site - 1] = hreftitle;
                    
                    renderTitle(site);
                }
                $("#tempspan").css("display", "block");
                $("#typetable tr").remove();
                //找到要追加的表格
                var newtable = $("#typetable");
                $.getJSON("parentType.action?time=" + new Date(), {
                    nodeId: nodeId,
                    options: params
                }, function(json){
                    if (json.length != 0) {
                        for (var i = 0; i < json.length; i++) {
                            //给表格添加一行
                            var newtr = $("<tr></tr>");
                            $(newtr).attr("id", json[i].key.id);
                            var title = $("<td></td>");
                            if (params) {
                                var options = params + ";" + json[i].key.parm + "=" + json[i].key.value;
                            }
                            else {
                                var options = json[i].key.parm + "=" + json[i].key.value;
                            }
                            var url = "goURL(" + json[i].key.id + ",7,\"" + options + "\",\"" + json[i].title + "\")";
                            $(title).html("<a  style='cursor: hand;'  href='javascript:" + url + ";'>" + json[i].title + "</a>");
                            $(newtr).append(title);
                            $(newtable).append($(newtr));
                        }
                        $("#tablecss").css("display", "none");
                    }
                    else {
                        $("#tablecss").css("display", "block");
                        if (params != null) {
                            $("#rowedtable").clearGridData();
                            var newUrl = tempurl + "&options=" + params;
                            $("#rowedtable").jqGrid('setGridParam', {
                                url: newUrl
                            }).trigger("reloadGrid");
                        }
                    }
                });
            }
            
            function renderTitle(end){
                var titleHtml = "";
                for (var i = 0; i < end; i++) {
                    if (i == end - 1) {
                        titleHtml += $(nodes[i]).text();
                    }
                    else {
                        titleHtml += nodes[i] + "->";
                    }
                    
                }
                $("#tempspan").html(titleHtml);
            }
            
            function table(){
                $("#rowedtable").clearGridData();
                $("#rowedtable").jqGrid({
                    Search: true,
                    url: tempurl,
                    datatype: "json",
                    colNames: ['文件名称', '档案类别', '工程名称', '格式', '上传时间', '文件大小', '操作'],
                    colModel: [{
                        id: 'filename',
                        name: 'filename',
                        index: 'filename',
                        align: 'left'
                    }, {
                        id: 'filetype',
                        name: 'filetype',
                        index: 'filetype',
                        align: 'center'
                    }, {
                        name: 'worksname',
                        index: 'worksname',
                        align: 'left'
                    }, {
                        name: 'style',
                        index: 'style',
                        align: 'center'
                    }, {
                        name: 'upload',
                        index: 'upload',
                        align: 'center'
                    }, {
                        name: 'filesize',
                        index: 'filesize',
                        align: 'right'
                    }, {
                        name: 'operate',
                        index: 'operate',
                        align: 'center'
                    }],
                    rowNum: 10,// 页数
                    pager: "#prowed4",// 页码
                    caption: "快速定位",
                    forceFit: true,// 是否超出DIV
                    multiselect: true,// 是否有全选
                    viewrecords: true,// 是否显示多少行
                    multiselectWidth: '40',// 调整选择的宽度
                    emptyrecords: '数据为空',// 空记录时的提示信息
                    width: '800',// 表格宽度
                    sortable: false,// 是否支持排序,
                    loadtext: "正在加载数据，请稍候……",
                    height: 'auto',
                    gridstate: 'hidden'
                });
            }
            
            //点击预览时触发的函数
            function previewImage(id){
                $("#preimge").attr("src", "getPreView.action?id=" + id);
                $('#dialog').dialog('open');
            }
            
          


          //点击打包下载时触 发的事件
            function packAgeDownLoad(){
				var stringsplit="";
				var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
                for (var i = 0; i < selectedIds.length; i++) {
                    if (selectedIds[i] != undefined) {
                        stringsplit += ":" + selectedIds[i];
                    }
                }
				if(stringsplit!=""){				
					$('#services').dialog("open");
					$("#showDownListId").empty();
					var url = "getDownLoadAddress.action?time="+(new Date()).valueOf();
                    var params = {   
		                eachCheckboxVals:stringsplit,  
		                pa:"test"   
		               };  
                    jQuery.post(url,params,callbackFun,'json'); 
				}else{
					alert("请先选择需打包下载的文件！");
				}
            }
            
        </script>
    </head>
    <body>
        <div id="mainDIV">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2" height="100%">
                <tr >
                    <td class="conttop2" colspan="2">
                        <div class="top2middle">
                            <span><%
                                List< TMenu> tm = (List< TMenu>) request.getAttribute("tm");
                                if (tm.size() != 0) { %>
                                <!--二级菜单开始-->
                                <s:iterator value="tm">
                                    <button class="dowbutton" onclick="javascript:goURL('<s:property value="id" />','7')" style="font-size: 12px; cursor: hand;">
                                        <s:property value="lable" />
                                    </button>&nbsp;&nbsp;
                                </s:iterator><!--二级菜单结束--><%
                                } %>
                            </span>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td   colspan="2"  class="contmiddle2">
            
                        <span style="font-size:13px;font-weight:bold;">当前位置：</span>
                         
                        <span id="tempspan" style="display:none;margin:10px 0 0 0;"></span>
                        <br/>
                       <div class="bianxian" style="width:750px;">
                        </div>
                        <table id="typetable" style="font-size:13px;" width="100%">
                        </table>
                        <div id="tablecss" style="display:none">
                            <s:if test="#request.isDownLoad">
                                <input type="submit" name="button6" id="button6" value="文件下载" class="dowbutton" onClick="packAgeDownLoad()"/>
                            </s:if>
                            <table id="rowedtable">
                            </table>
                            <div id="prowed4">
                            </div>
                        </div>
             
                    </td>
                </tr>
                <tr>
                    <td  colspan="2">
                        <div class="bottom2left">
                        </div>
                        <div class="bottom2right">
                        </div>
                        <div class="bottom2middle">
                        </div>
                    </td>
                </tr>
            </table>
            <div id="docInfo" title="详细信息">
                <div>
                    <ul id="docDetails">
                        <h3>文档信息</h3>
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
                <div style="padding-top:30px;">
                    <ul id="proDetials">
                        <h3>工程信息</h3>
                        <li id="ownerCode">
                        </li>
                        <li id="proCode">
                        </li>
                        <li id="proName">
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
                <div style="padding-top:30px;">
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
            </div>
            <div id="dialog" title="图片预览">
                <img id="preimge" src="../images/circle_animation.gif" alt="图片" style="width:100%">
            </div>
        </div>
        <div id="onLineSeeDIV">
            <iframe name="ols" id="ols" src="" height="100%" width="100%">
            </iframe>
        </div>
        <iframe id="packAgeDownLoad" style="width:200;height:200;display:none;">
        </iframe>
        <div id="showdialog" title="打包下载">
            请输入名称：<input type="text" name="tdocname" id="tdocname">
        </div>
    </body>
</html>