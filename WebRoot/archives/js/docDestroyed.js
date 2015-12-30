$(document).ready(function(){
    buttonDisable();
    //整 面不缓存
    $.ajaxSetup({
        cache: false
    });
    $("#kaigongyear").mask("2999");
    //下拉列表框
    select();
    //模式窗体
    docInfoDialog();
    //JqGrid
    tableJqGrid();
    //自动联想功能
    function initAutoComplete(json, valId, pn){
        $(valId).autocomplete(json, {
        	max : 10, // 列表里的条目数
			minChars : 0, // 自动完成激活之前填入的最小字符,为0双击弹出列表
			matchCase : false,// 不区分大小写
			width : 285, // 提示的宽度，溢出隐藏
			scrollHeight : 300, // 提示的高度，溢出显示滚动条
			matchContains : true, // 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
			//autoFill : false, // 自动填充 ，就是在文本框中自动填充符合条件的项目
			mustMatch : true, // 自动匹配，mustMatch表示必须匹配条目，也就是在文本框里输入的内容，必须是data参数里的数据，如果不匹配，文本框就被清空
			formatItem : function(row, i, max, term) {
				var v = $(valId).val();
				return row.name;
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
                    $(valId + "Code").attr("value", row.id);
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
                $(valId + "Code").attr("value", "");
            }
            else {
                $(valId + "Code").attr("value", row.id);
            }
            
            $(valId + "Code").trigger("change");
        });
    }
    $.getJSON('../project/projectName.action?time=' + Math.random(), {
        id: ''
    }, function(json){
        initAutoComplete(json, "#projectName", "");
    });
    
    $("#proStatus").bind("change", function(){
    	//监听工程阶段的改变 
    	var val=$("#proStatus").attr("value");
        var projectType=$("#proClass").attr("value"); 
        var pn=$("#projectName").attr("value");
    	    $("#projectName").attr("value","");
    	    $("#projectNameCode").attr("value","");
        $("#projectName").unbind();
        if(projectType=="" || projectType==null){
            	$.getJSON('../search/projectNameBasic.action?time='+Math.random(),{ id:val }, function(json) {
		        	  initAutoComplete(json,"#projectName",pn);
		        	}
		          );  
        }else{
        		$.getJSON('../search/projectNameBasic.action?time='+Math.random(),{ id:val,tid:projectType }, function(json) {
		        	  initAutoComplete(json,"#projectName",pn);
		        	}
		          );  
        }
    });
    
    $("#proClass").bind("change", function(){
    	//监听工程分类的改变 
    	var val=$("#proClass").attr("value");
        var statussVal=$("#proStatus").attr("value"); 
        var pn=$("#projectName").attr("value");
    	    $("#projectName").attr("value","");
    	    $("#projectNameCode").attr("value","");
        $("#projectName").unbind();

        if(statussVal=="" || statussVal==null){
            	$.getJSON('../search/projectNameBasic.action?time='+Math.random(),{ tid:val }, function(json) {
		        	  initAutoComplete(json,"#projectName",pn);
		        	}
		          );  
        }else{
        		$.getJSON('../search/projectNameBasic.action?time='+Math.random(),{ tid:val,id:statussVal }, function(json) {
		        	  initAutoComplete(json,"#projectName",pn);
		        	}
		          );  
        }
	});
})
//下拉列表框中的值
function select(){
	//保密，工程状态，文档，工程分类，部件类型
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
	 
	 var projectTypespan = $("#projectTypespan").text();
	 $.getJSON("../system/dictionaryType.action",{
	    	paramValue:projectTypespan
	        	}, function(json){    
	        var ptype = "<option value=''>无</option>";
	        for (var i = 0; i < json.length; i++) {
	        	ptype += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
	        }
	        //填充下拉列表
	        $("#proClass").html(ptype);
	    });
	 var projectStatusspan = $("#projectStatusspan").text();
	 $.getJSON("../system/dictionaryType.action",{
	    	paramValue:projectStatusspan
	        	}, function(json){    
	        var pstatus = "<option value=''>无</option>";
	        for (var i = 0; i < json.length; i++) {
	        	pstatus += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
	        }
	        //填充下拉列表
	        $("#proStatus").html(pstatus);
	    });
	 $.getJSON("../system/dictionaryType.action",{
	    	paramValue:"部件类型"
	        	}, function(json){    
	        var bujian = "<option value=''>无</option>";
	        for (var i = 0; i < json.length; i++) {
	        	bujian += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
	        }
	        //填充下拉列表
	        $("#tDocType").html(bujian);
	    });
}

//table Jqrid
function tableJqGrid(){
    $("#rowedtable").clearGridData();
    var newurl = "selectDocDestoryed.action?" + $('#docDestroyForm').formSerialize();    
    $("#rowedtable").jqGrid({
        url: newurl,
        datatype: "json",
        type: "POST",
        colNames: ['工程名称', '文件名称', '文档分类', '文件状态', '密级分类', '文件格式', '录入人', '录入时间', '操作'],
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
            align: 'left'
        }, {
            id: 'docStatus',
            name: 'docStatus',
            index: 'docStatus',
            align: 'center'        
        }, {
            id: 'baomi',
            name: 'baomi',
            index: 'baomi',
            align: 'center'
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
            align: 'center',
            index: 'update'
        }],
        rowNum: 10,// 页数
        pager: "#prowed4",// 页码
        caption: "文件销毁列表",// 标题
        forceFit: true,// 是否超出DIV
        multiselect: true,// 是否有全选
        viewrecords: true,// 是否显示多少行
        multiselectWidth:'40',// 调整选择的宽度
        emptyrecords: '数据为空',// 空记录时的提示信息
        width:'750',//表格宽度 
        sortable:false,//是否支持排序,
        loadtext:"正在加载数据，请稍候……",
        height: 'auto' ,  
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

//点击查询时触发的事件
function selectDocDestroy(){
    buttonDisable();
    $("#rowedtable").clearGridData();
    var newurl = "selectDocDestoryed.action?" + $('#docDestroyForm').formSerialize();
    $("#rowedtable").jqGrid('setGridParam', {
        url: newurl
    }).trigger("reloadGrid");
}

//点击销毁触发的事件
function updateDocDestory(){
    var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
    var len = selectedIds.length;
    if (len == 0) {
        alert("请您选择要销毁的档案！");
    }
    else {
        var stringsplit = "";
        for (var i = 0; i < selectedIds.length; i++) {
            if (selectedIds[i] != undefined) {
                stringsplit += "," + selectedIds[i];
            }
        }
        $.getJSON("updateDocDestroy.action?time=" + new Date().getTime(), {
            codes: stringsplit.substr(1)
        }, function(data){
            if (data == "success") {
                alert("销毁成功!");
                selectDocDestroy();
            }
            else {
                alert("销毁失败");
            }            
        });
    }
}

//点击恢复时触发的事件
function comeBackDocDestory(){
    var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
    var len = selectedIds.length;
    if (len == 0) {
        alert("请您选择恢复的档案！");
    }
    else {
        var stringsplit = "";
        for (var i = 0; i < selectedIds.length; i++) {
            if (selectedIds[i] != undefined) {
                stringsplit += "," + selectedIds[i];
            }
        }
        $.getJSON("comBackDocDestory.action?time=" + new Date().getTime(), {
            codes: stringsplit.substr(1)
        }, function(data){
        
            if (data == "success") {
                alert("恢复成功!");
                selectDocDestroy();
            }else {
                alert("恢复失败");
            }
        });
    }
}

//按钮显示或者不显示
function buttonDisable(){
    var selects = $('input[name="fileStatus"]');
    var a = $("#comeBack");    
    if (selects[0].checked == true) {
        $("#comeBack").attr("disabled", "true");
        $("#destory").attr("disabled", false);        
        $("#clear").attr("disabled", "true");        
    }
    if (selects[1].checked == true) {    
        $("#destory").attr("disabled", "true");
        $("#comeBack").attr("disabled", false);
        $("#clear").attr("disabled", false);
    }    
}

//点击清空时触发的事件
function clearDocDestory(){
    var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
    var len = selectedIds.length;
    if (len == 0) {
        alert("请您选择清空的档案！");
    }
    else {
        var stringsplit = "";
        for (var i = 0; i < selectedIds.length; i++) {
            if (selectedIds[i] != undefined) {
                stringsplit += "," + selectedIds[i];
            }
        }
        $.getJSON("clearDocDestory.action?time=" + new Date().getTime(), {
            codes: stringsplit.substr(1)
        }, function(data){        
            if (data == "success") {
                alert("清空成功!");
                selectDocDestroy();
            }else {
                alert("清空失败");
            }
        });
    }    
}
