$(document).ready(function() {
	// 整 面不缓存
		$.ajaxSetup( {
			cache : false
		});
		timer();
		select();
		showProjectDialog();
		$("#openYear").mask("2999");
		$("#from").mask("2999" + "-" + "99" + "-" + "99");
		$("#to").mask("2999" + "-" + "99" + "-" + "99");
		$("#datepicker2").mask("2999" + "-" + "99" + "-" + "99");
		// JqGrid
		tableJqGrid();
		// 自动联想功能
		$.getJSON('../system/unitNameAction.action?time=' + Math.random(),
				function(json) {
					unitNameAutoComplete(json, "#unitName", "");
				});
		$.getJSON('../system/unitNameAction.action?time=' + Math.random(),
				function(json) {
					unitNameAutoComplete(json, "#insertUnitName", "");
				});
		$.getJSON('../system/desginName.action?time=' + Math.random(),
				function(json) {
					unitNameAutoComplete(json, "#designUnitName", "");
				});
		$.getJSON('projectName.action?time=' + Math.random(), {
			tid : ''
		}, function(json) {
			projectNameComplete(json, "#projectName", "");
		});
		$.getJSON('projectParentName.action?time=' + Math.random(), {
			tid : ''
		}, function(json) {
			projectNameComplete(json, "#insertParentName", "");
		});
	})
// 工程名称联想
function projectNameComplete(json, valId, pn) {
	$(valId).autocomplete(json, {
		max : 10, // 列表里的条目数
		minChars : 0, // 自动完成激活之前填入的最小字符,为0双击弹出列表
		matchCase : false,// 不区分大小写
		width : 285, // 提示的宽度，溢出隐藏
		scrollHeight : 300, // 提示的高度，溢出显示滚动条
		matchContains : true, // 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
		autoFill : false, // 自动填充 ，就是在文本框中自动填充符合条件的项目
		mustMatch : true, // 自动匹配，mustMatch表示必须匹配条目，也就是在文本框里输入的内容，必须是data参数里的数据，如果不匹配，文本框就被清空
		formatItem : function(row, i, max, term) {
			var v = $(valId).val();
			return row.name;
			// return i + '/' + max + ':' + "<I >" + row.name +"</I>"+ "<span
		// style='display:none;'>"+ '"[' + row.id + ']' +"</span>";
		// formatItem作用在于可以格式化列表中的条目，比如我们加了“I”，让列表里的字显示出了斜体
		if (row.name.indexOf(v) == 0 || row.spell.indexOf(v) == 0) {
			return row.name + " (" + row.spell + ")";
		} else
			return false;
	},
	formatMatch : function(row, i, max) {
		// return row.name + row.id;
		if (row.name == pn) {
			$(valId).attr("value", pn);
			$(valId + "Id").attr("value", row.id);
		}
		return row.name + " (" + row.spell + ")";
		// formatMatch是配合formatItem使用，作用在于，由于使用了formatItem，所以条目中的内容有所改变，而我们要匹配的是原始的数据，所以用formatMatch做一个调整，使之匹配原始数据
	},
	formatResult : function(row) {
		return row.name;
		// formatResult是定义最终返回的数据，比如我们还是要返回原始数据，而不是formatItem过的数据
	}
	}).result(function(event, row, formatted) {
		if (row == "undefined" || row == null) {
			$(valId + "Id").attr("value", "");
		} else {
			$(valId + "Id").attr("value", row.id);
		}
		$(valId + "Id").trigger("change");
	});
}

// 单位名称联想
function unitNameAutoComplete(json, valId, pn) {
	$(valId).autocomplete(json, {
		max : 10, // 列表里的条目数
		minChars : 0, // 自动完成激活之前填入的最小字符,为0双击弹出列表
		matchCase : false,// 不区分大小写
		width : 285, // 提示的宽度，溢出隐藏
		scrollHeight : 300, // 提示的高度，溢出显示滚动条
		matchContains : true, // 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
		// autoFill : false, // 自动填充 ，就是在文本框中自动填充符合条件的项目
		mustMatch : true, // 自动匹配，mustMatch表示必须匹配条目，也就是在文本框里输入的内容，必须是data参数里的数据，如果不匹配，文本框就被清空
		formatItem : function(row, i, max, term) {
			var v = $(valId).val();
			return row.name;
			// return i + '/' + max + ':' + "<I >" + row.name +"</I>"+ "<span
		// style='display:none;'>"+ '"[' + row.id + ']' +"</span>";
		// formatItem作用在于可以格式化列表中的条目，比如我们加了“I”，让列表里的字显示出了斜体
		if (row.name.indexOf(v) == 0 || row.spell.indexOf(v) == 0) {
			return row.name;
		} else
			return false;
	},
	formatMatch : function(row, i, max) {
		// return row.name + row.id;
		if (row.name == pn) {
			$(valId).attr("value", pn);
			$(valId + "Code").attr("value", row.id);
		}
		return row.name + " (" + row.spell + ")";
		// formatMatch是配合formatItem使用，作用在于，由于使用了formatItem，所以条目中的内容有所改变，而我们要匹配的是原始的数据，所以用formatMatch做一个调整，使之匹配原始数据
	},
	formatResult : function(row) {
		return row.name;
		// formatResult是定义最终返回的数据，比如我们还是要返回原始数据，而不是formatItem过的数据
	}
	}).result(function(event, row, formatted) {

		if (row == "undefined" || row == null) {
			$(valId + "Code").attr("value", "");
		} else {
			$(valId + "Code").attr("value", row.id);
		}
		$(valId + "Code").trigger("change");
	});
}

// table Jqrid
function tableJqGrid() {
	var newurl = "selectProjectInfo.action?" + $('#projectForm').formSerialize();
	$("#rowedtable").clearGridData();
	$("#rowedtable").jqGrid(
			{
				url : newurl,
				datatype : "json",
				type : "POST",
				colNames : [ '工程编码', '工程名称', '所属工程', '业主单位', '初设单位', '工程类型',
						'电压等级', '工程状态', '初设年份', '开工年份', '竣工年份', '创建者', '创建日期',
						'操作' ],
				colModel : [ {
					id : 'proCode',
					name : 'proCode',
					index : 'proCode',
					width : '350',
					align : 'left'
				}, {
					id : 'proName',
					name : 'proName',
					index : 'proName',
					align : 'left'
				}, {
					id : 'parentPro',
					name : 'parentPro',
					index : 'parentPro',
					align : 'center'
				}, {
					id : 'unitName',
					name : 'unitName',
					index : 'unitName',
					align : 'center'
				}, {
					id : 'chusheunit',
					name : 'chusheunit',
					index : 'chusheunit',
					align : 'center'
				}, {
					id : 'proType',
					name : 'proType',
					index : 'proType',
					align : 'center'
				}, {
					id : 'dianya',
					name : 'dianya',
					index : 'dianya',
					align : 'center'
				}, {
					id : 'proStatus',
					name : 'proStatus',
					index : 'proStatus',
					align : 'center'
				}, {
					id : 'chusheyear',
					name : 'chusheyear',
					index : 'chusheyear',
					align : 'center'
				}, {
					id : 'kaigongYear',
					name : 'kaigongYear',
					index : 'kaigongYear',
					align : 'center'
				}, {
					id : 'jungongYear',
					name : 'jungongYear',
					index : 'jungongYear',
					align : 'center'
				}, {
					id : 'creator',
					name : 'creator',
					index : 'creator',
					align : 'center'
				}, {
					id : 'createDate',
					name : 'createDate',
					align : 'center',
					index : 'createDate'
				}, {
					id : 'href',
					name : 'href',
					align : 'center',
					index : 'href'
				} ],
				rowNum : 10,// 页数
				pager : "#prowed4",// 页码
				caption : "工程信息列表",
				forceFit : true,// 是否超出DIV
				multiselect : true,// 是否有全选
				viewrecords : true,// 是否显示多少行
				multiselectWidth : '40',// 调整选择的宽度
				emptyrecords : '数据为空',// 空记录时的提示信息
				width : '1220',// 表格宽度
				sortable : false,// 是否支持排序,
				height: 'auto' ,  
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
	//工程分类
	 var projectTypespan = $("#projectTypespan").text();
	 $.getJSON("../system/dictionaryType.action",{
	    	paramValue:projectTypespan
	        	}, function(json){    
	        var ptype = "<option value=''>无</option>";
	        var itype = "";
	        for (var i = 0; i < json.length; i++) {
	        	ptype += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
	        	itype += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
	        }
	        //填充下拉列表
	        $("#proType").html(ptype);
	        $("#insertProType").html(itype);
	    });
	  //工程状态
	 var projectStatusspan = $("#projectStatusspan").text();
	 $.getJSON("../system/dictionaryType.action",{
	    	paramValue:projectStatusspan
	        	}, function(json){    
	        var pstatus = "<option value=''>无</option>";
	        var ipstatus="";
	        for (var i = 0; i < json.length; i++) {
	        	pstatus += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
	        	ipstatus += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
	        }
	        //填充下拉列表
	        $("#proStatus").html(pstatus);
	        $("#insertProStatus").html(ipstatus);
	    });
	 //电压等级
	 var voltageLevelspan = $("#voltageLevelspan").text();
	 $.getJSON("../system/dictionaryType.action",{
	    	paramValue:voltageLevelspan
	        	}, function(json){    
	        var ptype = "<option value=''>无</option>";
	        var iptye = "";
	        for (var i = 0; i < json.length; i++) {
	        	ptype += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
	        	iptye += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
	        }
	        //填充下拉列表
	        $("#voltageLevel").html(ptype);
	        $("#insertVoltageLevel").html(iptye);
	  });
}

// 查询
function selectProjectInfo() {
	$("#rowedtable").clearGridData();
	var newurl = "selectProjectInfo.action?"
			+ $('#projectForm').formSerialize();
	$("#rowedtable").jqGrid('setGridParam', {
		url : newurl
	}).trigger("reloadGrid");
}

// 点击删除时触 发的事件
function deleteProjectInfo() {
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
		var result = confirm("您确定要删除该工程及工程下的所有文档吗！");

		if (result == true) {
			$.getJSON("deleteProjectInfo.action?time=" + new Date().getTime(),
					{
					  codes : stringsplit.substr(1)
					}, function(data) {
						if (data == "success") {
							alert("删除成功!");
							selectProjectInfo();
							refreshProject();
						} else {
							alert("删除失败");
						}
					});
		}
	}
}

// 点击插入时触 发的事件
function insertProjectInfo(id) {
	$("#from").css("display","block");
	$("#to").css("display","block");
	
	$("#datepicker2").css("display","block");
	$("#fromspan").css("display","none");
	$("#tospan").css("display","none")
	$("#fromspan").text("");
	$("#insertProjectCode").attr("value", "");
	$("#insertUnitName").attr("value", "");
	$("#insertUnitNameCode").attr("value", "");
	$("#insertProjectName").attr("value", "");
	$("#insertParentName").attr("value", "");
	$("#insertParentNameId").attr("value", "");
	$("#designUnitName").attr("value", "");
	$("#designUnitNameCode").attr("value", "");
	$("#from").attr("value", "");
	$("#to").attr("value", "");
	$("#datepicker2").attr("value", "");
	$("#projectId").attr("value", "");
	
	$("#insertProjectNameerror").css("display", "none");
	$("#insertUnitNameerror").css("display", "none");
	$("#preDesignYearerror").css("display", "none");
	
	$("#designUnitNameCodeerror").css("display", "none");
	id = id == null ? "" : id;
	if (id == "" || id == null) {
		
		$("#insertProjectCode").css("display", "none");
		$("#sprojectCode").css("display", "none");
		select();
		$("#showProjectInfo").dialog('open');
	} else {
		

		$("#fromspan").css("display","block");
		$("#tospan").css("display","block")
		$("#insertProjectCode").css("display", "block");
		$("#sprojectCode").css("display", "block");
		updateSelect(id);
		$("#showProjectInfo").dialog('open');
	}
}

// 模式窗体
function showProjectDialog() {
	// 模式窗体
	$('#showProjectInfo').dialog( {
		width : 601,
		height : 539,
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

// 非空验证
function checkNotNullAll() {
	var projectName = checkProjectName();
	var unitName = checkUnitName();
	var designUnitName = checkDesignUnitName();
	var designYear = checkPreDesignYear();
	//var openYear = checkOpenYear();
	if($("#datepicker2").val()<=$("#from").val() && $("#from").val() < $("#to").val()){
		if (projectName && unitName && designUnitName && designYear ) {
			$('#insertProjectForm').ajaxSubmit( {
				dataType : "json",
				beforeSubmit : function() {// alert("aa"); return true;
				},
				type : "post",
				contentType : "application/x-www-form-urlencoded;charset=utf-8",
				success : backCallInsert
			});
		}
	} else {
		alert("请仔细检查初设日期、开工日期以及竣工日期是否填写正确!");
		return false;
	}
}

// 插入的回传函数
function backCallInsert(data, status, xhr) {
	if (data != null) {
		$("#insertProjectCode").attr("value", data);
		$("#insertProjectCode").css("display", "block");
		$("#sprojectCode").css("display", "block");
		alert("保存成功");
		$("#showProjectInfo").dialog('close');
		selectProjectInfo();
		refreshProject();
	} else {
		alert("保存失败");
	}
}

// 非空验证工程名称 、初设单位、业主单 位
function checkProjectName() {
	if (jQuery.trim($("#insertProjectName").val()) != "") {
		$("#insertProjectNameerror").css("display", "none");
		return true;
	} else {
		$("#insertProjectNameerror").css("display", "block");
		return false;
	}
}

// 验证业主单位
function checkUnitName() {
	if (jQuery.trim($("#insertUnitNameCode").val()) != "") {
		$("#insertUnitNameerror").css("display", "none");
		return true;
	} else {
		$("#projectId").attr("value","");
		$("#insertUnitNameerror").css("display", "block");
		return false;
	}
}

// 验证初设单位
function checkDesignUnitName() {
	if (jQuery.trim($("#designUnitNameCode").val()) != "") {
		$("#designUnitNameCodeerror").css("display", "none");
		return true;
	} else {
		$("#designUnitNameCode").attr("value","");
		$("#designUnitNameCodeerror").css("display", "block");
		return false;
	}
}

// 非空验证初设日期
function checkPreDesignYear() {
	if (jQuery.trim($("#datepicker2").val()) != "") {
		$("#preDesignYearerror").css("display", "none");
		return true;
	} else {
		var span = $("#fromspan").text();
		if(span.length!=0){
			$("#preDesignYearerror").css("display", "none");
			return true;
		}else {
			$("#preDesignYearerror").css("display", "block");
			return false;	
		}
	}
}


// 时间控件
function timer() {
	$("#datepicker").datepicker($.datepicker.regional["zh-CN"]);
	var dates = $("#from, #to")
			.datepicker(
					{
						defaultDate : "+1w",
						changeMonth : true,
						numberOfMonths : 1, // 显示多少个月
						onSelect : function(selectedDate) {
							var option = this.id == "from" ? "minDate"
									: "maxDate", instance = $(this).data(
									"datepicker"), date = $.datepicker
									.parseDate(
											instance.settings.dateFormat
													|| $.datepicker._defaults.dateFormat,
											selectedDate, instance.settings);
							dates.not(this).datepicker("option", option, date);
						}
					});

	$("#datepicker2").datepicker( {
		changeMonth : true,
		changeYear : true
	});
	$("#datepicker2").datepicker($.datepicker.regional["zh-CN"]);
	$("#timepicker").timepicker();
}

// 更新时查询
function updateSelect(id) {
	$.getJSON("updateProjectInfoSelect.action", {
		id : id
	}, function(json) {
		$("#insertProjectCode").attr("value", json[0]);
		$("#insertUnitName").attr("value", json[1]);
		$("#insertUnitNameCode").attr("value", json[2]);
		$("#insertProjectName").attr("value", json[3]);
		$("#insertParentName").attr("value", json[4]);
		$("#insertParentNameId").attr("value", json[5]);
		$("#insertProType").attr("value", json[6]);
		$("#designUnitName").attr("value", json[7]);
		$("#designUnitNameCode").attr("value", json[8]);
		$("#insertVoltageLevel").attr("value", json[9]);
		
		$("#fromspan").css("display","block");
		$("#tospan").css("display","block");
		
		$("#datepicker2").css("display","none");
		
		$("#from").css("display","none");
		
		$("#to").css("display","block");
		$("#insertProStatus").attr("value", json[10]);
		
		$("#fromspan").text(json[11]);
		
		$("#from").attr("value", json[12]);
		
		$("#tospan").text(json[12]);
		$("#to").attr("value", json[13]);
		
		$("#datepicker2").attr("value", json[11]);
		$("#projectId").attr("value", json[14]);
	    
		$("#insertProjectCode").attr("readOnly",true);
	});
}

function refreshProject() {
	$.getJSON('projectName.action?time=' + Math.random(), {
		tid : ''
	}, function(json) {
		projectNameComplete(json, "#projectName", "");
	});
	$.getJSON('projectParentName.action?time=' + Math.random(), {
		tid : ''
	}, function(json) {
		projectNameComplete(json, "#insertParentName", "");
	});
}