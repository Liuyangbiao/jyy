$(document).ready(function() {
	// 整 面不缓存
		$.ajaxSetup( {
			cache : false
		});
		userRoleDialog();
		projectDialog();
		owenprojectDialog();
		select();
		// JqGrid
		tableJqGrid();
		insertUnitInfo();

		// $.getJSON('unitNameAll.action?time=' + Math.random(), function(json)
		// {
		// initAutoComplete(json, "#unitName", "");
		// });
	})

// table Jqrid
function tableJqGrid() {
	var newurl = "selectUnitInfo.action?" + $('#unitForm').formSerialize();
	$("#rowedtable").clearGridData();
	$("#rowedtable").jqGrid(
			{
				url : newurl,
				datatype : "json",
				type : "POST",
				colNames : [ '单位编码', '单位名称', '单位简称', '单位状态','代理单位', '单位类型', '单位地址',
						'联系电话', '创建者', '创建时间', '操作' ],
				colModel : [ {
					id : 'unitCode',
					name : 'unitCode',
					index : 'unitCode',
					align : 'left',
					width : '200'
				}, {
					id : 'unitName',
					name : 'unitName',
					index : 'unitName',
					align : 'left'
				}, {
					id : 'unitJian',
					name : 'unitJian',
					index : 'unitJian',
					align : 'left'
				}, {
					id : 'unitType',
					name : 'unitType',
					index : 'unitType',
					align : 'center'
				}, {
					id : 'poxCode',
					name : 'poxCode',
					index : 'poxCode',
					align : 'center'
				}, {
					id : 'unitStatus',
					name : 'unitStatus',
					index : 'unitStatus',
					align : 'center'
				}, {
					id : 'unitAddress',
					name : 'unitAddress',
					index : 'unitAddress',
					align : 'center'
				}, {
					id : 'telphone',
					name : 'telphone',
					index : 'telphone',
					align : 'center'
				}, {
					id : "creator",
					name : 'creator',
					index : 'creator',
					align : 'center'
				}, {
					id : "createDate",
					name : 'createDate',
					index : 'createDate',
					align : 'center'
				}, {
					id : "href",
					name : 'href',
					index : 'href',
					align : 'center'
				} ],
				height : 'auto',
				rowNum : 10,// 页数
				pager : "#prowed4",// 页码
				caption : "单位管理信息列表",
				forceFit : true,// 是否超出DIV
				multiselect : true,// 是否有全选
				viewrecords : true,// 是否显示多少行
				multiselectWidth : '40',// 调整选择的宽度
				emptyrecords : '数据为空',// 空记录时的提示信息
				width : '950',// 表格宽度
				sortable : false,// 是否支持排序,
				loadtext : "正在加载数据，请稍候……",
				jsonReader : {
					root : "rows",
					page : "page",
					total : "total",
					records : "records",
					repeatitems : true,
					cell : "cell",
					id : "id"
				}
			});
}


// 下拉列表框 值
function select() {
	//单位类型
	var url = "../system/unitType.action";
	$.getJSON(url, function(json) {
		// 单位类型
			var unitType = "<option value=''>无</option>";
			var insertUnitType = "";
			for ( var i = 0; i < json.length; i++) {
				unitType += "<option value=\"" + json[i].id + "\" >"
						+ json[i].name + "</option>";
				insertUnitType += "<option value=\"" + json[i].id + "\" >"
						+ json[i].name + "</option>";
			}
			// $("#unitType").html(unitType);
			$("#insertUnitType").html(insertUnitType);
		});
}

// 点击查询时触发的事件
function selectUnitInfo() {
	$("#rowedtable").clearGridData();
	var newurl = "selectUnitInfo.action?" + $('#unitForm').formSerialize();
	$("#rowedtable").jqGrid('setGridParam', {
		url : newurl
	}).trigger("reloadGrid");
}
function projectDialog() {
	// 模式窗体
	$('#projectDialog').dialog( {
		width : 500,
		height : 350,
		autoOpen : false,// 打开模式窗体
		resizable : true,// 是否可以调整模式窗体的大小
		draggable : true,// 是否可以拖动模式窗体
		modal : true,// 启用模式窗体
		closeOnEscape : true,// 按下esc可以退出
		bgiframe : true
	});
}

function owenprojectDialog() {
	// 模式窗体
	$('#owenprojectDialog').dialog( {
		width : 500,
		height : 350,
		autoOpen : false,// 打开模式窗体
		resizable : true,// 是否可以调整模式窗体的大小
		draggable : true,// 是否可以拖动模式窗体
		modal : true,// 启用模式窗体
		closeOnEscape : true,// 按下esc可以退出
		bgiframe : true
	});
}
function userRoleDialog() {
	// 模式窗体
	$('#userRoleDialog').dialog( {
		width : 600,
		height : 350,
		autoOpen : false,// 打开模式窗体
		resizable : true,// 是否可以调整模式窗体的大小
		draggable : true,// 是否可以拖动模式窗体
		modal : true,// 启用模式窗体
		closeOnEscape : true,// 按下esc可以退出
		bgiframe : true
	});
}

// 点击删除时触 发的事件
function deleteUnitInfo() {
	var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
	var len = selectedIds.length;
	if (len == 0) {
		alert("请您选择删除的单位信息！");
	} else {
		var stringsplit = "";
		for ( var i = 0; i < selectedIds.length; i++) {
			if (selectedIds[i] != undefined) {
				stringsplit += "," + selectedIds[i];
			}
		}
		var result = confirm("您确定要删除有关此用户的相关信息吗？");
		if (result == true) {
			$("#roletable tr:not(:eq(0))").remove();
			// 找到要追加的表格
			var newtable = $("#roletable");
			$.getJSON("getUserByUnitCode.action", {
				unitcode : stringsplit.substr(1)
			}, function(json) {
				if (json.length != 0) {
					for ( var i = 0; i < json.length; i++) {
						// 给表格添加一行
					var newtr = $("<tr></tr>");
					$(newtr).attr("id", json[i].loginId);

					var loginId = $("<td></td>");
					$(loginId).html(json[i].loginId);
					var userName = $("<td></td>");
					$(userName).html(json[i].username);
					var ucode = $("<td></td>");
					$(ucode).html(json[i].uname);
					var duty = $("<td></td>");
					$(duty).html(json[i].duty);
					var duty = $("<td></td>");
					$(duty).html(json[i].duty);

					var isValid = $("<td></td>");
					if (json[i].isValid == true) {
						$(isValid).html("有效");
					} else {
						$(isValid).html("无效");
					}
					$(newtr).append(loginId);
					$(newtr).append(userName);
					$(newtr).append(ucode);
					$(newtr).append(duty);
					$(newtr).append(isValid);
					$(newtable).append($(newtr));
				}
				$("#userRoleDialog").dialog('open');
			} else {
				$("#userRoleDialog").dialog('close');
				$("#desgtable tr:not(:eq(0))").remove();
				// 找到要追加的表格
					var desgtable = $("#desgtable");
					$.getJSON("getProjectByDesCode.action", {
						unitcode : stringsplit.substr(1)
					}, function(json) {
						if (json.length != 0) {
							for ( var i = 0; i < json.length; i++) {
								// 给表格添加一行
							var newtr = $("<tr></tr>");
							$(newtr).attr("id", json[i].code);

							var code = $("<td></td>");
							$(code).html(json[i].code);

							var name = $("<td></td>");
							$(name).html(json[i].name);

							$(newtr).append(code);
							$(newtr).append(name);
							$(desgtable).append($(newtr));
						}
						$("#userRoleDialog").dialog('close');
						$("#projectDialog").dialog('open');
					} else {
						$("#userRoleDialog").dialog('close');
						$("#projectDialog").dialog('close');
						$("#owentable tr:not(:eq(0))").remove();
						// 找到要追加的表格
							var owentable = $("#owentable");
							$.getJSON("getProjectByOwenCode.action", {
								unitcode : stringsplit.substr(1)
							}, function(json) {
								if (json.length != 0) {
									for ( var i = 0; i < json.length; i++) {
										// 给表格添加一行
									var newtr = $("<tr></tr>");
									$(newtr).attr("id", json[i].code);
									var code = $("<td></td>");
									$(code).html(json[i].code);

									var name = $("<td></td>");
									$(name).html(json[i].name);
									$(newtr).append(code);
									$(newtr).append(name);
									$(owentable).append($(newtr));
								}
								$("#userRoleDialog").dialog('close');
								$("#projectDialog").dialog('close');
								$("#owenprojectDialog").dialog('open');
							} else {
								$("#owenprojectDialog").dialog('close');
								$("#userRoleDialog").dialog('close');
								$("#projectDialog").dialog('close');
								$.getJSON("deleteUnit.action?time="
										+ new Date().getTime(), {
									codes : stringsplit.substr(1)
								}, function(data) {
									if (data == "success") {
										alert("删除成功!");
										selectUnitInfo();
									} else {
										alert("删除失败");
									}
								});
							}
						}	);
						}
					});

				}
			});

		}
	}
}

function insertUnitInfo() {
	// 模式窗体
	$('#insertUnitInfo').dialog( {
		width : 600,
		height : 370,
		autoOpen : false,// 打开模式窗体
		resizable : true,// 是否可以调整模式窗体的大小
		draggable : true,// 是否可以拖动模式窗体
		modal : true,// 启用模式窗体
		closeOnEscape : true,// 按下esc可以退出
		bgiframe : true,
		buttons : {
			"保存" : function() {
				checkNotNullAll();
			},
			"取消 " : function() {
				$(this).dialog("close");
			}
		}
	});
}

// 显示模式窗体
function insertUnitShowDailog(id) {
	$("#insertCode").attr("readOnly", false);
	$("#insertTeleerror").css("display", "none");
	$("#insertCode").attr("value", "");
	$("#insertName").attr("value", "");
	$("#usabletrue").attr("checked", true);
	$("#shortName").attr("value", "");
	$("#insertAddress").attr("value", "");
	$("#insertTele").attr("value", "");
	$("#unitCode").attr("value", "");
	$("#insertNameerror").css("display","none");
	id = id == null ? "" : id;
	if (id == "" || id == null) {
		updateUnitTypeValue();
		$("#insertUnitInfo").dialog('open');
	} else {
		updateUnitTypeValue();
		updateSelect(id);
		$("#insertUnitInfo").dialog('open');
	}
}

function updateSelect(id) {
	$.getJSON("updateSelectUnit.action", {
		code : id
	}, function(json) {
		   for (var i = 0; i < json.length; i++) {
				$("#insertCode").attr("value", json[i].code);
				$("#insertCode").attr("disabled", "disabled");
				$("#insertName").attr("value", json[i].name);
				$("#insertUnitType").attr("value", json[i].type);
				$("#shortName").attr("value", json[i].shortName);
				$("#insertAddress").attr("value", json[i].address);
				$("#insertTele").attr("value", json[i].telePhone);
				$("#unitCode").attr("value", json[i].code);
				if (json[i].isUsable == true) {
					$("#usabletrue").attr("checked", true);
				} else {
					$("#usablefalse").attr("checked", true);
				}
				$("#proxyName").attr("value",json[i].proxyName);
				$("#proxyNameCode").attr("value",json[i].proxyCode);
		   }
		   
	});
}

// 非空验证
function checkNotNullAll() {
	var name = checkUnitName();
	if (name && checkTelePhone()) {
		$('#insertUniForm').ajaxSubmit( {
			dataType : "json",
			beforeSubmit : function() {// alert("aa"); return true;
			},
			type : "post",
			contentType : "application/x-www-form-urlencoded;charset=utf-8",
			success : backCallInsert
		});

	}
}

// 插入的回传函数
function backCallInsert(data, status, xhr) {
	if (data == "success") {
		alert("保存成功 ");
		$("#insertUnitInfo").dialog('close');
		selectUnitInfo();
	} else {
		alert("保存失败");
	}
}

// 非空验证单位名称
function checkUnitName() {
	if (jQuery.trim($("#insertName").val()) != "" && jQuery.trim($("#insertCode").val()) != null) {
		$("#insertNameerror").css("display", "none");
		$("#insertCodeError").css("display", "none");
		return true;
	} else {
		$("#insertNameerror").css("display", "block");
		$("#insertCodeError").css("display", "block");
		return false;
	}
}

// 验证电话号码
function checkTelePhone() {
	var phone = $("#insertTele").val();
	if (phone.length != 0) {
		var p1 = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
		var me = false;
		if (p1.test(phone))
			me = true;
		if (!me) {
			$("#insertTeleerror").css("display", "block");
			return false;
		} else {
			$("#insertTeleerror").css("display", "none");
			return true;
		}
	} else {

		$("#insertTeleerror").css("display", "none");
		return true;
	}
}

function checkTele() {
	var telephone = $("#unitTelePhone").val();

	if (telephone.length != 0) {
		var p = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
		var me = false;
		if (p.test(telephone))
			me = true;
		if (!me) {
			$("#unitTelePhoneerror").css("display", "block");

			return false;
		} else {
			$("#unitTelePhoneerror").css("display", "none");
			return true;
		}
	}
	if (telephone.length == 0) {

		$("#unitTelePhoneerror").css("display", "none");
		return true;
	}
}

function updateUnitTypeValue() {
	var unittype = ($("#insertUnitType").val());
	if (unittype == "C01") {
		$("#insertCode").attr("readOnly", false);
	} else {
		$("#insertCode").attr("value", "");
		$("#insertCode").attr("readOnly", true);
	}
}