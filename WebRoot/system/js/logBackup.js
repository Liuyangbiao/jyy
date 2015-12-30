$(function(){
	// 整 面不缓存
	$.ajaxSetup( {
		cache : false
	});
	
	tableJqGrid();
	
	timer();
	select();
	
	$( "#slider" ).slider({
		value: 15,
		min: 1,
		max: 29,
		step: 1,
		slide: function( event, ui ) {
			$( "#backupValue" ).html(ui.value );
			$( "#backupAmount" ).val(ui.value );
		}
	});
	$("#backupValue" ).html($( "#slider" ).slider( "value" ) );
	$("#backupAmount" ).val($( "#slider" ).slider( "value" ) );
	
});

// 时间控件
function timer() {
	$("#timeFrom").mask("2999" + "-" + "12" + "-" + "39");
	$("#timeTo").mask("2999" + "-" + "12" + "-" + "39");
	$.datepicker.setDefaults( $.datepicker.regional[ "zh-CN" ] );
	var dates = $("#timeFrom, #timeTo").datepicker({
		changeMonth : true,
		numberOfMonths : 1, // 显示多少个月
		onSelect : function(selectedDate) {
			var option = this.id == "timeFrom" ? "minDate"
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

// 下拉列表框 值
function select() {
	//日志分类
	var url = "../archives/getSearchTdoc.action";
	// $.getJSON(url, function(json) {
	// var backupScope = "";
	// for ( var i = 0; i < json[12].length; i++) {
	// backupScope += "<option value=\"" + json[12][i].code + "\" >"
	// + json[12][i].name + "</option>";
	// }
	// $("#autoScope").html(backupScope);
	// $("#handScope").html(backupScope);
	//		
	// status();
	//	});
	
	
	 $.getJSON('../system/dictionaryType.action',{
        paramValue:'日志分类'},
        function(json){
        	var backupScope="";
		 for(var i = 0;i<json.length;i++){
			 backupScope += "<option value=\"" + json[i].id + "\" >"
			 + json[i].name + "</option>";
		 }
		 $("#autoScope").html(backupScope);
		 $("#handScope").html(backupScope);
		 status();
     });
}

//自动备份状态（启用或停止）
function status(){
	var url = "statusBackup.action";
	$.getJSON(url, function(json) {
		if(json[0]=='success'){
			$("#backupImage").attr("src","../images/onCorrect.png");
			$("#status").attr("checked",true);
			
			$("option[value='"+json[1]+"']").attr("selected",true);
			$("input[value='"+json[2]+"']").attr("checked",true);
			
			//备份范围
			$("#backupWay").html("/"+json[5]);
			$("#backupWaya").html("/"+json[5]);
			
			changeValue(json[2]);
			$('#slider').slider('option', 'value', json[3]);
			$("#backupValue" ).html($( "#slider" ).slider( "value" ) );
			$("#backupAmount" ).val($( "#slider" ).slider( "value" ) );
			
			//选中备份范围
			if(json[4]!=null &&json[4].length>0){
				for(var i=0;i<json[4].length;i++){
					$("#autoScope").find("option").each(function(){
						if($(this).val()==json[4][i]){
							$(this).attr("selected",true);						
						}
					});
				}
			}
		}else{
			$("#backupImage").attr("src","../images/onError.png");
			$("#status").attr("checked",false);	
			//备份范围
			$("#backupWay").html("/"+json[1]);
			$("#backupWaya").html("/"+json[1]);
		}
		
		
		
	});
}

function changeValue(value){
	if(value=='d'){//选中天
		$('#slider').slider('option', 'max', 29);
		$('#slider').slider('option', 'min', 1);
		$('#slider').slider('option', 'value', 15);
	}else{
		$('#slider').slider('option', 'max', 10);
		$('#slider').slider('option', 'min', 1);
		$('#slider').slider('option', 'value', 5);
	}
	$("#backupValue" ).html($( "#slider" ).slider( "value" ) );
	$("#backupAmount" ).val($( "#slider" ).slider( "value" ) );
}


// table Jqrid
function tableJqGrid() {
	var newurl = "getLogListBackup.action?time" + new Date().getTime();
	$("#rowedtable").clearGridData();
	$("#rowedtable").jqGrid(
		{
			url : newurl,
			datatype : "json",
			type : "POST",
			colNames : [ '备份时间','备份文件', '操作人'],
			colModel : [ {
				id : 'backupDate',
				name : 'backupDate',
				index : 'backupDate',
				align : 'center',
				width:'80'
			}, {
				id : 'backupFile',
				name : 'backupFile',
				index : 'backupFile',
				align : 'left'
				
			}, {
				id : 'backupMan',
				name : 'backupMan',
				index : 'backupMan',
				align : 'center',
				width:'60'
			}],
			rowNum : 10,// 页数
			pager : "#prowed4",// 页码
			caption : "日志备份信息列表",
			forceFit : true,// 是否超出DIV
//			multiselect : true,// 是否有全选
			viewrecords : true,// 是否显示多少行
			multiselectWidth : '40',// 调整选择的宽度
			emptyrecords : '数据为空',// 空记录时的提示信息
			width : '900',// 表格宽度
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

function handBackup(){
	var timeFrom=$("#timeFrom").val();
	var timeTo=$("#timeTo").val();
	if(timeFrom==''||timeTo==''){
		alert("请选择备份时间！");
		return false;
	}
	//判断日期合法性
    var regex = new RegExp("^(?:(?:([0-9]{4}(-|\/)(?:(?:0?[1,3-9]|1[0-2])(-|\/)(?:29|30)|((?:0?[13578]|1[02])(-|\/)31)))|([0-9]{4}(-|\/)(?:0?[1-9]|1[0-2])(-|\/)(?:0?[1-9]|1\\d|2[0-8]))|(((?:(\\d\\d(?:0[48]|[2468][048]|[13579][26]))|(?:0[48]00|[2468][048]00|[13579][26]00))(-|\/)0?2(-|\/)29))))$");
    if(timeFrom!=''){
    	if (!regex.test(timeFrom)) { 
			$("#timeError").html("请输入正确的起始日期！"); 
			$("#timeFrom").val("");
			return false; 
		}
    }if(timeTo!=''){
    	if (!regex.test(timeTo)) { 
			$("#timeError").html("请输入正确的结束日期！"); 
			$("#timeTo").val("");
			return false; 
		}
    }
	
    $("#timeError").html("");
	var newurl = "handBackup.action?"+ $('#handForm').formSerialize();
	$.getJSON(newurl, function(json){
		if(json=='error'){
			alert("请选择备份范围！");
		}else{
			alert("备份成功！");
			//重新加载grid
			$("#rowedtable").clearGridData();
			var newurl = "getLogListBackup.action?time" + new Date().getTime();
			$("#rowedtable").jqGrid('setGridParam', {
				url : newurl
			}).trigger("reloadGrid");
		}
	});
	
	
}


function autoBackup(){
	var newurl = "autoBackup.action?"+ $('#autoForm').formSerialize();
	$.getJSON(newurl, function(json){
		if(json=='error'){
			alert("请选择备份范围！");
		}else{
			alert("设置成功！");
			if(json=='true'){
				$("#backupImage").attr("src","../images/onCorrect.png");
			}else{
				$("#backupImage").attr("src","../images/onError.png");
			}
		}
	});

}