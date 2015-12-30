<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/"; %>
<html>
    <head>
        <link type="text/css" href="../css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet" />
        <link href="<%=path%>/css/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.jqgrid.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.multiselect.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="../css/base.css">
        <script type="text/javascript" src="<%=path%>/js/jquery-1.5.min.js">
        </script>
        <script type="text/javascript" src="<%=path%>/js/jquery.cookie.js">
        </script>
        <script type="text/javascript" src="../js/jquery.jqGrid.js">
        </script>
        <script type="text/javascript" src="<%=path%>/js/jquery-ui-1.8.10.custom.min.js">
        </script>
        <script src="../js/jquery.ui.datepicker-zh-CN.js" type="text/javascript">
        </script>
        <script src="../js/jquery.timepicker.js" type="text/javascript">
        </script>
        <script type="text/javascript" src="../js/jquery.autocomplete.js">
        </script>
        <script type="text/javascript" src="<%=path%>/js/jquery.form.js">
        </script>
        <script type="text/javascript" src="<%=path%>/js/popShortcut.js">
        </script>
        <script src="../js/jquery.maskedinput.js" type="text/javascript">
        </script>
        <script type="text/javascript">
            $(function(){
                $.ajaxSetup({
                    cache: false
                });
                var mId = "${param.menuId}";
                imgDisplay(mId);
                $("#allcheck").click(function(){
                    if (this.checked) {
                        $("input[name='eachCheck']").each(function(){
                            this.checked = true;
                        });
                    }
                    else {
                        $("input[name='eachCheck']").each(function(){
                            this.checked = false;
                        });
                    }
                });
                //删除按钮显示或者显示隐藏 
                btnclose();
                btnDataclose();
                //Tab表格
                $("#tabs").tabs({
                    cookie: {
                        // store cookie for a day, without, it would be a session cookie
                        expires: 1
                    }
                });
                //已控股权插入模式窗体
                insertYishouquanShow();
                insertDataProfileShow();
                //已授权限管理
                yishouquantable();
                dataProfiletable();
                //用户名称联想
                $.getJSON('<%=path %>/system/opertorLogOperate.action?time=' + Math.random(), {
                    id: ''
                }, function(json){
                    userinitAutoComplete(json, "#login");
                });

                $.getJSON('<%=path %>/system/opertorLogOperate.action?time=' + Math.random(), {
                    id: ''
                }, function(json){
                    userinitAutoComplete(json, "#datalogin");
                });
                //角色下拉列表框
                roleselect();
                //时间控件
                timer();
                timerdata();
            });
            //已授权初始信息表格
            function yishouquantable(){
                var authurl = "selectYiShouQuanInfo.action?" + $("#selectYiShouQuanForm").formSerialize();
                $("#authrowedtable").clearGridData();
                $("#authrowedtable").jqGrid({
                    url: authurl,
                    datatype: "json",
                    type: "POST",
                    colNames: ['用户名称', '授权类型', '控制对象/角色', '授权操作', '开始时间', '结束时间', '允许次数', '操作次数', '授权人', '授权时间', '操作'],
                    colModel: [{
                        id: 'userName',
                        name: 'userName',
                        index: 'userName',
                        align: 'left'
                    }, {
                        id: 'proType',
                        name: 'proType',
                        index: 'proType',
                        align: 'center'
                    }, {
                        id: 'kongzhiObject',
                        name: 'kongzhiObject',
                        index: 'kongzhiObject',
                        align: 'center'
                    }, {
                        id: 'shouquanhref',
                        name: 'shouquanhref',
                        index: 'shouquanhref',
                        align: 'left'
                    }, {
                        id: 'starTime',
                        name: 'starTime',
                        index: 'starTime',
                        align: 'center'
                    }, {
                        id: 'endTime',
                        name: 'endTime',
                        index: 'endTime',
                        align: 'center'
                    }, {
                        id: 'count',
                        name: 'count',
                        index: 'count',
                        align: 'center'
                    }, {
                        id: 'hrefCount',
                        name: 'hrefCount',
                        index: 'hrefCount',
                        align: 'center'
                    }, {
                        id: 'shouquanuser',
                        name: 'shouquanuser',
                        index: 'shouquanuser',
                        align: 'center'
                    }, {
                        id: 'shouquantime',
                        name: 'shouquantime',
                        index: 'shouquantime',
                        align: 'center'
                    }, {
                        id: 'href',
                        name: 'href',
                        index: 'href',
                        align: 'center'
                    }],
                    height: 'auto',
                    rowNum: 10,// 页数
                    pager: "#authprowed4",// 页码
                    caption: "功能授权限信息管理",
                    forceFit: true,// 是否超出DIV
                    multiselect: true,// 是否有全选
                    viewrecords: true,// 是否显示多少行
                    multiselectWidth: '40',// 调整选择的宽度
                    emptyrecords: '数据为空',// 空记录时的提示信息
                    width: '1250',// 表格宽度
                    sortable: false,// 是否支持排序,
                    loadtext: "正在加载数据，请稍候……",
                    jsonReader: {
                        root: "rows",
                        page: "page",
                        total: "total",
                        records: "records",
                        repeatitems: true,
                        cell: "cell",
                        id: "id"
                    }
                });
            }

            function dataProfiletable(){
            	   var authurl = "dataProfileInfoAction.action?" + $("#selectDataProfileForm").formSerialize();
                $("#dataProfiletable").clearGridData();
                $("#dataProfiletable").jqGrid({
                    url:authurl,
                    datatype: "json",
                    type: "POST",
                    colNames: ['授权的用户','临时的授权角色', '开始时间','结束时间','授权人' ,'授权时间','操作'],
                    colModel: [{
						id:'profileName',
						name:'profileName',
						index:'profileName',
						align:'center'
                        },{
                        id: 'profileRole',
                        name: 'profileRole',
                        index: 'profileRole',
                        align: 'center'
                    },{
                        id: 'dataStartTime',
                        name: 'dataStartTime',
                        index: 'dataStartTime',
                        align: 'center'
                    },{
                        id: 'endStartTime',
                        name: 'endStartTime',
                        index: 'endStartTime',
                        align: 'center'
                    },{
                        id: 'profileUser',
                        name: 'profileUser',
                        index: 'profileUser',
                        align: 'center'
                    },{
                        id: 'profileDate',
                        name: 'profileDate',
                        index: 'profileDate',
                        align: 'center'
                    },{
                        id:'href',
                        name:'href',
                        index:'href',
                        align:'center'
                    }],
                    height: 'auto',
                    rowNum: 10,// 页数
                    pager: "#dataprowed4",// 页码
                    caption: "数据授权信息管理",
                    forceFit: true,// 是否超出DIV
                    multiselect: true,// 是否有全选
                    viewrecords: true,// 是否显示多少行
                    multiselectWidth: '40',// 调整选择的宽度
                    emptyrecords: '数据为空',// 空记录时的提示信息
                    width: '850',// 表格宽度
                    sortable: false,// 是否支持排序,
                    loadtext: "正在加载数据，请稍候……",
                    jsonReader: {
                        root: "rows",
                        page: "page",
                        total: "total",
                        records: "records",
                        repeatitems: true,
                        cell: "cell",
                        id: "id"
                    }
                });
            }
            //用户名称联想
            function userinitAutoComplete(json, valId, pn){
                $(valId).autocomplete(json, {
                    max: 10, // 列表里的条目数
                    minChars: 0, // 自动完成激活之前填入的最小字符,为0双击弹出列表
                    matchCase: false,// 不区分大小写
                    width: 285, // 提示的宽度，溢出隐藏
                    scrollHeight: 300, // 提示的高度，溢出显示滚动条
                    matchContains: true, // 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                    // autoFill : true, // 自动填充 ，就是在文本框中自动填充符合条件的项目
                    mustMatch: true, // 自动匹配，mustMatch表示必须匹配条目，也就是在文本框里输入的内容，必须是data参数里的数据，如果不匹配，文本框就被清空
                    formatItem: function(row, i, max, term){
                        var v = $(valId).val();
                        return row.name;
                        // return i + '/' + max + ':' + "<I >" + row.name +"</I>"+ "<span
                        // style='display:none;'>"+ '"[' + row.id + ']' +"</span>";
                        // formatItem作用在于可以格式化列表中的条目，比如我们加了“I”，让列表里的字显示出了斜体
                        if (row.name.indexOf(v) == 0 || row.spell.indexOf(v) == 0) {
                            return row.name + " (" + row.spell + ")";
                        }
                        else 
                            return false;
                    },
                    formatMatch: function(row, i, max){
                        // return row.name + row.id;
                        if (row.name == pn) {
                            $(valId).attr("value", pn);
                            $(valId + "Id").attr("value", row.id);
                        }
                        return row.name + " (" + row.spell + ")";
                        // formatMatch是配合formatItem使用，作用在于，由于使用了formatItem，所以条目中的内容有所改变，而我们要匹配的是原始的数据，所以用formatMatch做一个调整，使之匹配原始数据
                    },
                    formatResult: function(row){
                        return row.name;
                        // formatResult是定义最终返回的数据，比如我们还是要返回原始数据，而不是formatItem过的数据
                    }
                }).result(function(event, row, formatted){
                    if (row == "undefined" || row == null) {
                        $(valId + "Id").attr("value", "");
                    }
                    else {
                        $(valId + "Id").attr("value", row.id);
                    }
                    $(valId + "Id").trigger("change");
                });
            }
            
            /**
             * 查询已授权信息
             */
            function selectYiShouQuanInfo(){
            	btnclose();
                $("#authrowedtable").clearGridData();
                var newurl = "selectYiShouQuanInfo.action?" + $("#selectYiShouQuanForm").formSerialize();
                $("#authrowedtable").jqGrid('setGridParam', {
                    url: newurl
                }).trigger("reloadGrid");
            }

           function  dataProfileSelect(){
        	   btnDataclose();
        	   $("#dataProfiletable").clearGridData();
        	   var authurl = "dataProfileInfoAction.action?" + $("#selectDataProfileForm").formSerialize();
               $("#dataProfiletable").jqGrid('setGridParam', {
                   url: authurl
               }).trigger("reloadGrid");
             }

            function btnclose(){
            	if(document.getElementById("guoqi").checked){
             	  $("#delete").attr("disabled", true); 
             	}  else {
             		$("#delete").attr("disabled", false); 
                 }   
               }
            function btnDataclose(){
            	if(document.getElementById("dataguoqi").checked){
             	  $("#datadelete").attr("disabled", true); 
             	}  else {
             		$("#datadelete").attr("disabled", false); 
                 }   
               }
            /**
             * 清空用户名称中的值
             */
            function clearLoginId(){
                var login = $("#login").val();
                if (login.length == 0) {
                    $("#loginId").attr("value", "");
                }
            }

            function clearDataLoginId(){
            	 var login = $("#datalogin").val();
                 if (login.length == 0) {
                     $("#dataloginId").attr("value", "");
                 }
                }
            function insertDataProfileShow(){
					$("#insertDataProfilediv").dialog({
	                    width: 300,
	                    height: 480,
	                    autoOpen: false,// 打开模式窗体
	                    resizable: true,// 是否可以调整模式窗体的大小
	                    draggable: true,// 是否可以拖动模式窗体
	                    modal: true,// 启用模式窗体
	                    closeOnEscape: true,// 按下esc可以退出
	                    bgiframe: true,
	                    buttons: {
	                        "保存": function(){
						checkDataPriFileNotNullAll();
	                        },
	                        "取消 ": function(){
	                            $(this).dialog("close");
	                        }
	                    }
	                });
	            }
            /**
             * 插入授权信息的模式窗体
             */
            function insertYishouquanShow(){
                // 模式窗体
                $('#insertYishouquanShowDialog').dialog({
                    width: 500,
                    height: 650,
                    autoOpen: false,// 打开模式窗体
                    resizable: true,// 是否可以调整模式窗体的大小
                    draggable: true,// 是否可以拖动模式窗体
                    modal: true,// 启用模式窗体
                    closeOnEscape: true,// 按下esc可以退出
                    bgiframe: true,
                    buttons: {
                        "保存": function(){
                            checkPriFileNotNullAll();
                        },
                        "取消 ": function(){
                            $(this).dialog("close");
                        }
                    }
                });
            }

            function checkDataPriFileNotNullAll(){
            	var id = checkDataId();
            	var from = checkdataFrom();
            	var to = checkdataTo();
            	 if (id && from && to) {
                	 $('#dataProfileForm').ajaxSubmit({
                         dataType: "json",
                         beforeSubmit: function(){
                         },
                         type: "post",
                         contentType: "application/x-www-form-urlencoded;charset=utf-8",
                         success: profilebackCallInsert
                     });
                    }
                }
            function checkPriFileNotNullAll(){
            	var id = checkId();
            	var permit = checkPermitCount();
            	var from = checkFrom();
            	var to = checkTo();
                if (id && permit && from && to ) {
            	 roleValue();
            	 $('#insertYiShouQuanForm').ajaxSubmit({
                     dataType: "json",
                     beforeSubmit: function(){
                     },
                     type: "post",
                     contentType: "application/x-www-form-urlencoded;charset=utf-8",
                     success: profilebackCallInsert
                 });
                }
              }
            /**
             * 插入用户授权信息的回调函数
             * 
             * @param {Object}
             *            data
             * @param {Object}
             *            status
             * @param {Object}
             *            xhr
             */
            function profilebackCallInsert(data, status, xhr){
                if (data == "true") {
                    alert("保存成功 ");
                    $("#iderrorvalue").css("display", "block");
                    $("#dataiderrorvalue").css("display", "block");
                    $("#insertYishouquanShowDialog").dialog('close');
                    $("#insertDataProfilediv").dialog('close');
                    selectYiShouQuanInfo();
                    dataProfileSelect();
                }
                else {
                    alert("保存失败");
                }
            }
           // 取出授权信息
            function roleValue(){
                var allChkStr = "";
                // 取出表格的行数
                $("#roletable").find("tr:not(:eq(0))").each(function(){
                    var roleid = $(this).attr("id");
                    var chkStr = "";
                    $(this).find("td:eq(2)").children().each(function(){
                        // chkStr+= $(this).attr("checked")==true?"1":"0";
                        if ($(this).attr("checked") == true) {
                            chkStr += "1";
                        }
                        else {
                            chkStr += "0";
                        }
                    })
                    var num = new Number(chkStr);
                    if(num!=0){
                    	allChkStr+=";"+roleid+ ":"+chkStr;
                    }
                })
                $("#tempspan").val(allChkStr.substr(1));
            }

            function insertDataProfileInfo(id){
                timerdata();
                $("#datafrom").attr("value","");
                $("#datato").attr("value","");
                $("#dataId").attr("value","");
                $("#dataUpdateId").attr("value","");
                $("#dataIderror").css("display","none");
                $("#dataIderrorvalue").css("display","none");

                $("#datafromerror").css("display", "none");
                $("#datatoerror").css("display", "none");
            	 id = id == null ? "" : id;
            	 if (id == "" || id == null) {
            		 roleselect();
            		 $("#dataId").attr("readOnly", false);
                     $("#insertDataProfilediv").dialog("open");
                 }
                 else {
              	     $("#dataId").attr("readOnly", true);
                	 updateDataProfile(id);
                     $("#insertDataProfilediv").dialog('open');
                 }
                }

            function updateDataProfile(id){
                  $.getJSON("updateDataProfileInfo.action",{
						id:id
                      },function(json){
                    	  for(var i = 0;i<json.length;i++){
  							$("#dataId").attr("value",json[i].id);
  							$("#dataRoleId").attr("value",json[i].roleId);
  						    $("#proRoleId").attr("value",json[i].tmpRoleId);
  							$("#datafrom").attr("value",json[i].startTime);
  							$("#datato").attr("value",json[i].endTime);
  							$("#dataUpdateId").attr("value",json[i].id);
  							var user ="<option value=\"" + json[i].userId+ "\" >" + json[i].userName + "</option>"; 
						    $("#dataUserId").html(user);
  						}
                       });
                }
            /**
             * 插入授权信息的事件
             */
            function insertYiShouQuanInfo(id){
            	    timer();
            	   $("#permitCounterror").css("display", "none");
            	   $("#fromerror").css("display", "none");
             	   $("#toerror").css("display", "none");
                   $("#iderror").css("display", "none");
                   $("#iderrorvalue").css("display", "none");
            	   $("#id").attr("value","");
				   $("#updateId").attr("value","");
				   $("#permitCount").attr("value","");
				   $("#from").attr("value","");
			       $("#to").attr("value","");
            	   id = id == null ? "" : id;
            	   var roleId = $("#roleId").val();
                   if (id == "" || id == null) {
                	   roleselect();
                	   var roleId = $("#roleId").val();
                       var profileId = $("#profileObject").val();
                       rolefunction(roleId, profileId);
                	   $("#id").attr("readOnly", false);
                      $("#insertYishouquanShowDialog").dialog("open");
                   }
                   else {
                	   $("#id").attr("readOnly", true);
                	   updateYiShouQuanInfo(id);
                	   var roleId = $("#roleId").val();
                	   var profileId = $("#profileObject").val();
                       $("#insertYishouquanShowDialog").dialog('open');
                       
                   }
            }
            function updateYiShouQuanInfo(Id){
                $.getJSON("updateYiShouQuan.action",{
					id:Id
                    },function(json){
						for(var i =0;i<json.length;i++){
							$("#id").attr("value",json[i].id);
							$("#updateId").attr("value",json[i].id);
							$("#permitCount").attr("value",json[i].permitCount);
							$("#roleId").attr("value",json[i].roleId);
							var user ="<option value=\"" + json[i].userId+ "\" >" + json[i].userName + "</option>"; 
						    $("#userId").html(user);
							$("#profileType").attr("value",json[i].profileType);
							$("#prcsCount").attr("value",json[i].prcsCount);
							$("#from").attr("value",json[i].startTime);
						    $("#to").attr("value",json[i].endTime);
							var profileObject ="<option value=\"" + json[i].profileObject+ "\" >" + json[i].profileName + "</option>"; 
						    $("#profileObject").html(profileObject);
					       rolefunction($("#roleId").val(),json[i].profileObject);
						}
                 });
              }
            /**
             * 角色名称下拉列表框
             */
            function roleselect(){
                $.getJSON("roleList.action", function(json){
                    var role = "";
                    var dataRoleId= "";
                    var proRoleId = "";
                    for (var i = 0; i < json.length; i++) {
                        role += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
                        dataRoleId += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
                        proRoleId+= "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
                    }
                    $("#roleId").html(role);
                    $("#dataRoleId").html(dataRoleId);
                    $("#proRoleId").html(proRoleId);
                    userSelect($("#roleId").val());   
                    dataRole($("#dataRoleId").val());
                     
                });
               
           	 $.getJSON('../system/dictionaryType.action',{
                  paramValue:'权限分类'},
                  function(json){
                	  var authClass = "";
           		 for(var i = 0;i<json.length;i++){
           			authClass += "<option value=\"" + json[i].id + "\" >"
           			 + json[i].name + "</option>";
           		 	}
           		 $("#profileType").html(authClass);
           	 	 getProfileType( $("#profileType"));
               });
            }
            
            /**
             * 根据控制类型，更改控制对象的值
             * @param {Object} obj 控制类型
             */
            function getProfileType(obj){
                var type = $(obj).val();
                var roleId = $("#roleId").val();
                $.getJSON("getTprofileTempByTypeandRoleId.action", {
                    type: type,
                    roleId: roleId
                }, function(json){
                    if (json.length != 0) {
                        var profileObject = "";
                        for (var i = 0; i < json.length; i++) {
                            profileObject += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
                        }
                        $("#profileObject").html(profileObject);
                    }
                    else {
                        var profileObject = "<option value=''  >无</option>";
                        $("#profileObject").html(profileObject);
                    }
                });
                var profileId = $("#profileObject").val();
                //rolefunction(roleId, profileId);
            }
            
            /**
             * 根据角色，重新加载操作权限
             * @param {Object} obj 角色名称
             */
            function getRoleId(obj){
                var roleId = $(obj).val();
                var profileId = $("#profileObject").val();
                rolefunction(roleId, profileId);
                userSelect(roleId);
            }

             function dataRoleIdChange(obj){
            	 var dataRoleId = $(obj).val();
            	 dataRole(dataRoleId);
                }
			function dataRole(dataRoleId){
				 $.getJSON("userList.action",{
                     roleId:dataRoleId},function(json){
                     	  var user = "";
                           for (var i = 0; i < json.length; i++) {
                         	  user += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
                           }
                           $("#dataUserId").html(user);
                      });
				}
             function userSelect(roleId){
            	 $.getJSON("userList.action",{
                     roleId:roleId},function(json){
                     	  var user = "";
                           for (var i = 0; i < json.length; i++) {
                         	  user += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
                           }
                           $("#userId").html(user);
                      });
                 }
            /**
             * 根据控制对象，重新加载操作权限
             * @param {Object} obj 控制对象
             */
            function getProfileObject(obj){
                var roleId = $("#roleId").val();
                var profileId = $(obj).val();
                //rolefunction(roleId, profileId);
            }
            
            /**
             * 角色授权
             *
             * @param {Object}
             *            roleId 角色ID
             * @param {Object}
             *            profileId 权限ID
             */
            function rolefunction(roleId, profileId){
                roleId = roleId == null ? "" : roleId;
                profileId = profileId == null ? "" : profileId;
                $("#roletable tr:not(:eq(0))").remove();
                // 找到要追加的表格
                var newtable = $("#roletable");
                $.post("profileTempInfo.action", {
                    roleId: roleId,
                    roleProfileId: profileId
                }, function(json){
                    for (var i = 0; i < json.length; i++) {
                        // 给表格添加一行
                        var newtr = $("<tr id='eachtr'  height='28'></tr>");
                        $(newtr).attr("id", json[i].rolebean.roleid);
                        // 给表格添加第一列
                        var eacheck = $("<td ><input type='checkbox' name='eachCheck' id='eachCheck'></td>");
                        // 角色
                        var role = $("<td></td>");
                        $(role).html("<input type='hidden' value='" + json[i].rolebean.roleid + "'>" + json[i].rolebean.rolename)
                        var readPri = $("<td></td>");
                        var shouquan = json[i].readPri;
                        var chkStr = "";
                        // 授权格式 XXXX
                        
                        for (var k = 0; k < shouquan.length; k++) {
                            if ('1' == shouquan[k]) {
                                chkStr += ("<input type='checkbox' checked='checked' id='" +
                                json[i].rolebean.roleid +
                                "' name='rolecheck'/>" +
                                json[i].names[k]);
                            }
                            else {
                                chkStr += ("<input type='checkbox' id='" +
                                json[i].rolebean.roleid +
                                "' name='rolecheck'/>" +
                                json[i].names[k]);
                            }
                        }
                        $(readPri).html(chkStr);
                        // 将列数据追加到行中
                        $(newtr).append(eacheck);
                        $(newtr).append(role);
                        $(newtr).append(readPri);
                        // 将行数据追加到表格中
                        $(newtable).append($(newtr));
                    }
                });
            }
            
            /**
             * 时间控制
             */
            function timer(){
                $("#from").mask("2999" + "-" + "99" + "-" + "99");
                $("#to").mask("2999" + "-" + "99" + "-" + "99");
                $("#datepicker").datepicker($.datepicker.regional["zh-CN"]);
                var dates = $("#from, #to").datepicker({
                    defaultDate: "+1w",
                    changeMonth: true,
                    numberOfMonths: 1, // 显示多少个月
                    onSelect: function(selectedDate){
                        var option = this.id == "from" ? "minDate" : "maxDate", instance = $(this).data("datepicker"), date = $.datepicker.parseDate(instance.settings.dateFormat ||
                        $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
                        dates.not(this).datepicker("option", option, date);
                    }
                });
                $("#timepicker").timepicker();

                
            }

            function timerdata(){
                $("#datafrom").mask("2999" + "-" + "99" + "-" + "99");
                $("#datato").mask("2999" + "-" + "99" + "-" + "99");
                $("#datepicker").datepicker($.datepicker.regional["zh-CN"]);
                var dates = $("#datafrom, #datato").datepicker({
                    defaultDate: "+1w",
                    changeMonth: true,
                    numberOfMonths: 1, // 显示多少个月
                    onSelect: function(selectedDate){
                        var option = this.id == "datafrom" ? "minDate" : "maxDate", instance = $(this).data("datepicker"), date = $.datepicker.parseDate(instance.settings.dateFormat ||
                        $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
                        dates.not(this).datepicker("option", option, date);
                    }
                });
                $("#timepicker").timepicker();
            }

            //验证ID是否为空
            function checkId(){
            	  var id = $("#id").val();
            	  var num = /\d{1,2}/;
            	    if (num.exec(id)) {
            	        if ($.getJSON("selectProfileTempIdBoolean.action", {
            	            id: id
            	        }, function(json){
            	            if (json == 'true') {
            	            	checkupdateID();
            	                return false;
            	            }
            	            else 
            	                if (json == 'false') {
            	                	 $("#iderror").css("display", "none");
                                     $("#iderrorvalue").css("display", "none");
            	                    return true;
            	                }
            	        })) {
            	            return true;
            	        }
            	        else {
            	            return false;
            	        }
            	    }
            	    else {
            	        $("#iderror").css("display", "block");
            	        return false;
            	    }
               }

            function checkupdateID(){
                var updateCode = $("#updateId").val();
                if (updateCode.len != 0) {
                    $("#iderror").css("display", "none");
                    $("#iderrorvalue").css("display", "none");
                }
                if (updateCode == "") {
                    $("#iderrorvalue").css("display", "block");
                }
            }
            
            //验证允许次数是否为空
            function checkPermitCount(){
            	 if (jQuery.trim($("#permitCount").val()) != "") {
         	        $("#permitCounterror").css("display", "none");
         	        return true;
         	    }
         	    else {
         	        $("#permitCounterror").css("display", "block");
         	        return false;
         	    }
              }
            //验证开始时间
            function checkFrom(){
            	 if (jQuery.trim($("#from").val()) != "") {
          	        $("#fromerror").css("display", "none");
          	        return true;
          	    }
          	    else {
          	        $("#fromerror").css("display", "block");
          	        return false;
          	    }
             }
            //验证结束时间
            function checkTo(){
            	 if (jQuery.trim($("#to").val()) != "") {
           	        $("#toerror").css("display", "none");
           	        return true;
           	    }
           	    else {
           	        $("#toerror").css("display", "block");
           	        return false;
           	    }
              }

            // 删除已授权信息
            function deleteYiShouQuanInfo(){
            	   var selectedIds = $("#authrowedtable").jqGrid("getGridParam", "selarrrow");
            	    var len = selectedIds.length;
            	    if (len == 0) {
            	        alert("请您选择删除的授权信息！");
            	    }
            	    else {
            	        var profileId = "";
            	        for (var i = 0; i < selectedIds.length; i++) {
            	            if (selectedIds[i] != undefined) {
            	                profileId += "," + selectedIds[i];
            	            }
            	        }
            	        var result = confirm("确定要删除功能授权信息！");
            	        if (result == true) {
            	            $.getJSON("deleteProfileTemp.action", {
            	                profileIdList: profileId.substr(1)
            	            }, function(json){
            	                if (json == "success") {
            	                    alert("删除成功!");
            	                    selectYiShouQuanInfo();
            	                }
            	                else {
            	                    alert("删除失败!");
            	                }
            	            });
            	        }
            	    }
            }

            function deleteDataProfileInfo(){
            	 var selectedIds = $("#dataProfiletable").jqGrid("getGridParam", "selarrrow");
         	    var len = selectedIds.length;
         	    if (len == 0) {
         	        alert("请您选择删除的数据授权信息！");
         	    }
         	    else {
         	        var profileId = "";
         	        for (var i = 0; i < selectedIds.length; i++) {
         	            if (selectedIds[i] != undefined) {
         	                profileId += "," + selectedIds[i];
         	            }
         	        }
         	        var result = confirm("确定要删除数据授权信息！");
         	        if (result == true) {
         	            $.getJSON("deleteProfileTemp.action", {
         	                profileIdList: profileId.substr(1)
         	            }, function(json){
         	                if (json == "success") {
         	                    alert("删除成功!");
         	                   dataProfileSelect();
         	                }
         	                else {
         	                    alert("删除失败!");
         	                }
         	            });
         	        }
         	    }
                }
            function checkDataId(){
            	 var id = $("#dataId").val();
           	  var num = /\d{1,2}/;
           	    if (num.exec(id)) {
           	        if ($.getJSON("selectProfileTempIdBoolean.action", {
           	            id: id
           	        }, function(json){
           	            if (json == 'true') {
           	            	checkDataupdateID();
           	                return false;
           	            }
           	            else 
           	                if (json == 'false') {
           	                	 $("#dataIderror").css("display", "none");
                                 $("#dataIderrorvalue").css("display", "none");
           	                    return true;
           	                }
           	        })) {
           	            return true;
           	        }
           	        else {
           	            return false;
           	        }
           	    }
           	    else {
           	        $("#dataIderror").css("display", "block");
           	        return false;
           	    }
             }
            function checkDataupdateID(){
            	 var updateCode = $("#dataUpdateId").val();
                 if (updateCode.len != 0) {
                     $("#dataIderror").css("display", "none");
                     $("#dataIderrorvalue").css("display", "none");
                 }
                 if (updateCode == "") {
                     $("#dataIderrorvalue").css("display", "block");
                 }
            }

          //验证开始时间
            function checkdataFrom(){
            	 if (jQuery.trim($("#datafrom").val()) != "") {
          	        $("#datafromerror").css("display", "none");
          	        return true;
          	    }
          	    else {
          	        $("#datafromerror").css("display", "block");
          	        return false;
          	    }
             }
            //验证结束时间
            function checkdataTo(){
            	 if (jQuery.trim($("#datato").val()) != "") {
           	        $("#datatoerror").css("display", "none");
           	        return true;
           	    }
           	    else {
           	        $("#datatoerror").css("display", "block");
           	        return false;
           	    }
              }
            
        </script>
        <style type="text/css">
            /*demo page css*/
            body {
                font-size: 12px;
            }
        </style>
        <style type="text/css">
            .biaoge td {
                padding: 5px;
                text-align: center;
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
                <td class="conttop2">
                    <div class="top2left">
                    </div>
                    <div class="top2right">
                    </div>
                    <div class="top2middle">
                        <span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif" onclick="topwin(${param.menuId })" style="cursor: hand"></a></span><span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span><span class="topico2"><img src="../images/Accordion_ico03.gif"></span><h3>临时授权管理</h3>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="contmiddle2">
                    <table width="750" border="0" cellspacing="0" cellpadding="0" style="font-size: 12px; font-weight: bold;">
                        <tr>
                            <td>
                            <div id="tabs" align="center">
                                    <ul>
                                        <li>
                                            <a href="#tabs-1">功能授权</a>
                                        </li>
                                        <li>
                                            <a href="#tabs-2">数据授权</a>
                                        </li>
                                    </ul>
                                    <div id="tabs-1">
                                        <div>
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="conttitle">
                                                <tr>
                                                    <td height="22">
                                                        <h3>检索条件</h3>
                                                    </td>
                                                </tr>
                                            </table>
                                            <form action="selectYiShouQuanInfo.action" method="post" name="selectYiShouQuanForm" id="selectYiShouQuanForm">
                                                <table style="font-size: 12px;" border="0" width="500px">
                                                    <tr>
                                                        <td align="left">
                                                            用户名称<input type="text" size="10" name="login" id="login" onblur="clearLoginId()"><input type="hidden" size="10" name="loginId" id="loginId">
                                                        <input type="checkbox" name="guoqishouquan" value="true" id="guoqi">过期授权
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="3" align="right">
                                                            <button class="querenbutton" onclick="selectYiShouQuanInfo()">
                                                                <span style="font-size: 12px;">查询</span>
                                                            </button>&nbsp;
                                                            <button class="querenbutton" onclick="insertYiShouQuanInfo()">
                                                                <span style="font-size: 12px;">新增</span>
                                                            </button>&nbsp;
                                                            <button class="querenbutton" onclick="deleteYiShouQuanInfo()" id="delete">
                                                                <span style="font-size: 12px;">删除</span>
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </form>
                                            <table id="authrowedtable">
                                            </table>
                                            <div id="authprowed4">
                                            </div>
                                        </div>
                                    </div>
                                    <div id="tabs-2">
                                       <div>
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="conttitle">
                                                <tr>
                                                    <td height="22">
                                                        <h3>检索条件</h3>
                                                    </td>
                                                </tr>
                                            </table>
                                            <form action="dataProfileInfoAction.action" method="post" name="selectDataProfileForm" id="selectDataProfileForm">
                                                <table style="font-size: 12px;" border="0" width="500px">
                                                    <tr>
                                                        <td align="left">
                                                        		    用户名称<input type="text" size="10" name="datalogin" id="datalogin" onblur="clearDataLoginId()">
                                                      		    <input type=hidden size="10" name="dataloginId" id="dataloginId">
                                                        <input type="checkbox" name="guoqishouquan" value="true" id="dataguoqi">过期授权
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="3" align="right">
                                                            <button class="querenbutton" onclick="dataProfileSelect()">
                                                                <span style="font-size: 12px;">查询</span>
                                                            </button>&nbsp;
                                                            <button class="querenbutton" onclick="insertDataProfileInfo()">
                                                                <span style="font-size: 12px;">新增</span>
                                                            </button>&nbsp;
                                                            <button class="querenbutton" onclick="deleteDataProfileInfo()" id="datadelete">
                                                                <span style="font-size: 12px;">删除</span>
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </form>
                                              <table id="dataProfiletable">
                                            </table>
                                            <div id="dataprowed4">
                                            </div>
                                        </div>
                                    </div>
                                  </div>
                            </td>
                        </tr>
                    </table>
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
        <div id="insertYishouquanShowDialog" title="授权编辑">
            <form action="insertYiShouQuanInfo.action" method="post" name="insertYiShouQuanForm" id="insertYiShouQuanForm">
                <table style="font-size: 12px;" border="0" width="90%">
                	<tr>
                	   <td  align="right">临时授权ID</td>
                	   <td colspan="3"><input type="text" name="profileTemp.id" id="id" size="10" onblur="checkId()">
                	   <span id="iderror" style="display: none; color: red; fontSize: 11px;">请输入ID(数字：1或12)</span>
                	    <span id="iderrorvalue" style="display: none; color: red; fontSize: 11px;">当前ID已存在</span>
                	    </td>
                	</tr>
                    <tr>
                        <td align="right">
                            角色名称
                        </td>
                        <td >
                            <select id="roleId"  name="profileTemp.role" onchange="getRoleId(this)" >
                            </select>
                        </td>
                        <td align="right">用户名称</td>
                        <td><select id="userId" name="userId"></select></td>
                    </tr>
                    <tr>
                        <td align="right">
                            控制类型
                        </td>
                        <td>
                            <select id="profileType" onchange="getProfileType(this)">
                            </select>
                        </td>
                        <td align="right">
                            控制对象
                        </td>
                        <td>
                            <select id="profileObject" onchange="getProfileObject(this)" name="profileId">
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            开始时间
                        </td>
                        <td>
                            <input type="text" id="from" size="10" name="profileTemp.startTime" onblur="checkFrom()"/>
                            <span id="fromerror" style="display: none; color: red; fontSize: 11px;">请输入开始时间</span>
                        </td>
                        <td align="right">
                            结束时间
                        </td>
                        <td>
                            <input type="text" id="to" size="10" name="profileTemp.endTime" onblur="checkTo()"/>
                            <span id="toerror" style="display: none; color: red; fontSize: 11px;">请输入开始时间</span>
                        </td>
                    </tr>
                    <tr>
                	            <td align="right">
                            允许次数
                        </td>
                        <td colspan="3">
                            <input type="text" id="permitCount" size="10" name="profileTemp.permitCount" onblur="checkPermitCount()">
                            <span id="permitCounterror" style="display: none; color: red; fontSize: 11px;">请输入允许次数</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="conttitle" style="border-top:none;">
                                <tr>
                                    <td height="22">
                                        <h3 style="font-size:12px; color:#476c89;">操作权限</h3>
                                    </td>
                                </tr>
                            </table>
                            <div align="center">
                                <table width="100%" border="0" cellpadding="0" cellspacing="1" id="roletable" style="text-align:center; font-size:12px;" class="biaoge">
                                    <tr class="bgtr01">
                                        <td width="7%">
                                            <input type="checkbox" id='allcheck' name='allcheck'>
                                        </td>
                                        <td width="26%">
                                            角色名称 
                                        </td>
                                        <td width="67%">
                                            操作权限 
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
                <input type="hidden" id="updateId" name="updateId">
                <input type="hidden" id="prcsCount" name="profileTemp.prcsCount">
                 <input type="hidden" id="tempspan" name="profileTemp.grantPrivilege">
            </form>
        </div>
        
        <div id="insertDataProfilediv" title="授权编辑">
        	<form action="insertYiShouQuanInfo.action" method="post" id="dataProfileForm">
        	<input type="hidden" name="updateId" id="dataUpdateId">        	
        	<table style="font-size: 12px;" border="0" width="100%">
        		<tr>
        		  <td align="right">临时授权ID</td>
        		  <td><input type="text" name="profileTemp.id" id="dataId" size="10" onblur="checkDataId()">
                	   <span id="dataIderror" style="display: none; color: red; fontSize: 11px;">请输入ID(数字：1或12)</span>
                	    <span id="dataIderrorvalue" style="display: none; color: red; fontSize: 11px;">当前ID已存在</span></td>
        		</tr>
        		<tr>
        		  <td width="30%" align="right">角色名称</td>
        		  <td><select id="dataRoleId" onchange="dataRoleIdChange(this)"></select></td>
        		</tr>
        		<tr>
        		  <td  align="right">用户列表</td>
        		  <td><select id="dataUserId" name="userId"></select></td>
        		</tr>
        		<tr>
        		  <td  align="right">临时赋予角色</td>
        		  <td><select id="proRoleId" name="profileTemp.role"></select></td>
        		</tr>
        		<tr>
        		  <td align="right">开始时间</td>
        		  <td><input type="text"  id="datafrom" name="profileTemp.startTime" size="12" onblur="checkdataFrom()"/>
        		  <span id="datafromerror" style="display: none; color: red; fontSize: 11px;">请输入开始时间</span>
        		  </td>
        		</tr>
        		<tr>
        		  <td align="right">结束时间</td>
        		  <td><input type="text" id="datato" name="profileTemp.endTime"  size="12" onblur="checkdataTo()"/>
        		  <span id="datatoerror" style="display: none; color: red; fontSize: 11px;">请输入结束时间</span>
        		  </td>
        		</tr>
        	</table>
        	</form>
        </div>
    </body>
</html>

