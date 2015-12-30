$(document).ready(function(){
    buttonDisable();
    //整 面不缓存
    $.ajaxSetup({
        cache: false
    });
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
    $("#tree").dynatree({
        title: "Event samples",
        onQueryActivate: function(activate, node){
            logMsg("onQueryActivate(%o, %o)", activate, node);
            //return false;
        },
        onActivate: function(node){
        
            //必须点叶子节点才能调用下面代码
            $("#docTypeCode").attr("value", node.data.code);
        }
    });
    
    $("#from").mask("2999" + "-" + "99" + "-" + "99");
	$("#to").mask("2999" + "-" + "99" + "-" + "99");
    //模式窗体
    docInfoDialog();
    docMoveDialog();
    //时间控件
    timepicker();
    //下拉列表框
    select();
    //JqGrid
    tableJqGrid();
    //自动联想功能
    
    $.getJSON('../project/projectName.action?time=' + Math.random(), {
        id: ''
    }, function(json){
        projectNameComplete(json, "#projectName", "");
    });
    
    $.getJSON('../system/unitNameAction.action?time=' + Math.random(),
			function(json) {
				unitNameAutoComplete(json, "#unitName", "");
			});
})

//工程名称联想
function projectNameComplete(json, valId, pn){
    $(valId).autocomplete(json, {
        max: 10, //列表里的条目数
        minChars: 0, //自动完成激活之前填入的最小字符,为0双击弹出列表
        matchCase: false,//不区分大小写
        width: 285, //提示的宽度，溢出隐藏
        scrollHeight: 300, //提示的高度，溢出显示滚动条
        matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
       // autoFill: false, //自动填充 ，就是在文本框中自动填充符合条件的项目
        mustMatch: true, //自动匹配，mustMatch表示必须匹配条目，也就是在文本框里输入的内容，必须是data参数里的数据，如果不匹配，文本框就被清空
        formatItem: function(row, i, max, term){
            var v = $(valId).val();
            return row.name ;
            //  return i + '/' + max + ':' + "<I >" + row.name +"</I>"+ "<span style='display:none;'>"+ '"[' + row.id + ']' +"</span>";
            //formatItem作用在于可以格式化列表中的条目，比如我们加了“I”，让列表里的字显示出了斜体
            if (row.name.indexOf(v) == 0 || row.spell.indexOf(v) == 0) {
                return row.name ;
            }
            else 
                return false;
        },
        formatMatch: function(row, i, max){
            //return row.name + row.id;
            if (row.name == pn) {
                $(valId).attr("value", pn);
                $(valId + "Id").attr("value", row.id);
            }
            return row.name + " (" + row.spell + ")";
            //formatMatch是配合formatItem使用，作用在于，由于使用了formatItem，所以条目中的内容有所改变，而我们要匹配的是原始的数据，所以用formatMatch做一个调整，使之匹配原始数据
        },
        formatResult: function(row){
            return row.name;
            //formatResult是定义最终返回的数据，比如我们还是要返回原始数据，而不是formatItem过的数据
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

//时间控件
function timepicker(){
    //时间控件
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
	$("#timepicker").timepicker();
}

//下拉列表框中的值
function select(){
	var baomispan = $("#baomispan").text();
	 $.getJSON('../system/dictionaryType.action',{
        paramValue:baomispan},
        function(json){
		 var baomi = "<option value=''>无</option>";
		 for(var i = 0;i<json.length;i++){
			 baomi += "<option value=\"" + json[i].id + "\" >"
			 + json[i].name + "</option>";
		 }
		 $("#baomi").html(baomi)
     });
	 var docFormatspan = $("#docFormatspan").text();
	 $.getJSON('../system/dictionaryType.action',{
        paramValue:docFormatspan},
        function(json){
		 var fileFormat = "<option value=''>无</option>";
		 for(var i = 0;i<json.length;i++){
			 fileFormat += "<option value=\"" + json[i].id + "\" >"
			 + json[i].name + "</option>";
		 }
		 $("#fileFormat").html(fileFormat)
     });
	
	$.getJSON("docTypeList.action", function(json) {
		var dd = "<option value=''>无</option>";
		for(var i = 0;i<json.length;i++){
			dd += "<option value=\"" + json[i].code + "\" >"
			 + json[i].name + "</option>";
		 }
		$("#docClassName").html(dd);
	});
}

//table Jqrid
function tableJqGrid(){
    $("#rowedtable").clearGridData();
    var newurl = "selectDocList.action?" + $('#docDisposalForm').formSerialize();
    $("#rowedtable").jqGrid({
        url: newurl,
        datatype: "json",
        type: "POST",
        colNames: ['工程名称', '文件名称', '文档分类', '文件状态', '密级分类', '文件格式', '录入人', '录入时间', '操作','工程类型'],
        colModel: [{
            id: 'projectName',
            name: 'projectName',
            index: 'projectName',
            align: 'left'
        }, {
            id: 'fileName',
            name: 'fileName',
            index: 'fileName',
            align: 'left'
        }, {
            id: 'docClassName',
            name: 'docClassName',
            index: 'docClassName',
            align: 'center'
        }, {
            id: 'docStatus',
            name: 'docStatus',
            index: 'docStatus',
            align: "center"        
        }, {
            id: 'baomi',
            name: 'baomi',
            index: 'baomi',
            align: "center"        
        }, {
            id: 'fileFormat',
            name: 'fileFormat',
            index: 'fileFormat',
            align: 'center'
        }, {
            id: 'createName',
            name: 'createName',
            index: 'createName',
            align: 'center'
        }, {
            id: "editDate",
            name: 'editDate',
            index: 'editDate',
            align: 'center'
        }, {
            id: "update",
            name: 'update',
            index: 'update',
            align: 'center'
        }, {
            id: "proType",
            name: 'proType',
            index: 'proType',
            align: 'center',
            hidden:true
        }],
        rowNum: 10,// 页数
        pager: "#prowed4",// 页码
        caption: "档案归档",
        forceFit: true,// 是否超出DIV
        multiselect: true,// 是否有全选
        viewrecords: true,// 是否显示多少行
        multiselectWidth:'40',// 调整选择的宽度
        emptyrecords: '数据为空',// 空记录时的提示信息
        width:'850',//表格宽度 
        sortable:false,//是否支持排序,
        height: 'auto' ,  
        loadtext:"正在加载数据，请稍候……" ,
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

//单位名称联想
function unitNameAutoComplete(json, valId, pn) {
	$(valId).autocomplete(json, {
		max : 10, // 列表里的条目数
		minChars : 0, // 自动完成激活之前填入的最小字符,为0双击弹出列表
		matchCase : false,// 不区分大小写
		width : 285, // 提示的宽度，溢出隐藏
		scrollHeight : 300, // 提示的高度，溢出显示滚动条
		matchContains : true, // 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
		//autoFill : true, // 自动填充 ，就是在文本框中自动填充符合条件的项目
		mustMatch : true, // 自动匹配，mustMatch表示必须匹配条目，也就是在文本框里输入的内容，必须是data参数里的数据，如果不匹配，文本框就被清空
		formatItem : function(row, i, max, term) {
			var v = $(valId).val();
			return row.name ;
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


//点击查询时触发的事件
function selectDocDisposal(){
	
    buttonDisable();
    $("#rowedtable").clearGridData();
    var newurl = "selectDocList.action?" + $('#docDisposalForm').formSerialize();
    $("#rowedtable").jqGrid('setGridParam', {
        url: newurl
    }).trigger("reloadGrid");
}


//归档事件
function guiDangClick(){
    var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
    var len = selectedIds.length;
    if (len == 0) {
        alert("请您选择要归档的档案！");
    }
    else {
        var stringsplit = "";
        for (var i = 0; i < selectedIds.length; i++) {
            if (selectedIds[i] != undefined) {
                stringsplit += "," + selectedIds[i];
            }
        }
        $.getJSON("updateDocStatus.action?time=" + new Date().getTime(), {
            codes: stringsplit.substr(1)
        }, function(data){
            if (data == "success") {
                alert("归档成功!");
                selectDocDisposal();
            }
            else {
                alert("归档失败");
            }
        });
    }
}

//删除时触发的按钮
function deleteDocDetails(){
    var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
    var len = selectedIds.length;
    if (len == 0) {
        alert("请您选择删除的档案！");
    }
    else {
        var stringsplit = "";
        for (var i = 0; i < selectedIds.length; i++) {
            if (selectedIds[i] != undefined) {
                stringsplit += "," + selectedIds[i];
            }
        }
        $.getJSON("deleteDocInfo.action?time=" + new Date().getTime(), {
            codes: stringsplit.substr(1)
        }, function(data){
            if (data == "success") {
                alert("删除成功!");
                selectDocDisposal();
            }
            else {
                alert("删除失败");
            }
        });
    }
    
}

//按钮显示或者不显示
function buttonDisable(){
    var selects = $('input[name="fileStatus"]');    
    if (selects[0].checked == true) {    
        $("#delete").attr("disabled", false);
        $("#disposal").attr("disabled", false);        
        $("#move").attr("disabled", "true");        
    }
    if (selects[1].checked == true) {    
        $("#delete").attr("disabled", "true");
        $("#move").attr("disabled", false);
        $("#disposal").attr("disabled", "true");        
    }    
}

//迁移触发的事件
function moveDocDetails(){		
    $("#doccodes").attr("value", "");
    $("#docTypeCode").attr("value", "");
    var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
    var len = selectedIds.length;
    if (len == 0) {
        alert("请您选择迁移的档案！");
    }
    else {
        var stringsplit = "";        
        var proName="";
        for (var i = 0; i < selectedIds.length; i++) {
            if (selectedIds[i] != undefined) {
                stringsplit += "," + selectedIds[i];
               var colNameValue= $("#rowedtable").getCell(selectedIds[i],"projectName");
               var protype=$("#rowedtable").getCell(selectedIds[i],"proType");
               if(proName!="" && colNameValue!=proName){
            	   alert("选择的文档不在同一工程内，不能进行此操作！");
            	   return;
               }else {
            	   proName=colNameValue;
               }
            }
        }
        $("#doccodes").attr("value", stringsplit.substr(1));
        tree(protype);        
        $("#moveDocDialog").dialog('open');
    }
   
}

//迁移的模式窗体
function docMoveDialog(){
	// 模式窗体
    $('#moveDocDialog').dialog({
        width: 335,
        height: 200,
        autoOpen: false,// 打开模式窗体
        resizable: true,// 是否可以调整模式窗体的大小
        draggable: true,// 是否可以拖动模式窗体
        modal: true,// 启用模式窗体
        closeOnEscape: true,// 按下esc可以退出
        buttons: {
            "确定": function(){
                var doccodes = $("#doccodes").val();
                var doccodeType = $("#docTypeCode").val();                
                $.getJSON("moveDoc.action", {
                    doccodes: doccodes,
                    doccodeType: doccodeType
                }, function(data){
                    if (data == "success") {
                        alert("迁移成功!");
                        $("#moveDocDialog").dialog('close');
                        selectDocDisposal();
                    }
                    else {
                        alert("迁移失败");
                    }                    
                });
            },
            "取消": function(){
                $(this).dialog("close");
                ;
            }
        }
    });
}


function tree(protype){

	var	rootNode =$("#tree").dynatree("getRoot");
	
    var childList = rootNode.getChildren();
    for(var i =0;i<childList.length;i++){
    	var node = childList[i];
 
     	var tempProType=((node.data.code).substr(0,2));
   
    	if(tempProType==protype){
    
    		node.li.style.display="block";
    	}else {
    
    		node.li.style.display="none";
    	}
    }
}

//点击预览时触发的函数
function previewImage(id){
    $("#preimge").attr("src", "../search/getPreView.action?id=" + id);
    $('#dialog').dialog('open');
}

