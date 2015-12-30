$(document).ready(function(){
    $.ajaxSetup({
        cache: false
    });
    // 初始化表格
    tableJqGrid();
    select();
    $("#from").mask("2999" + "-" + "99" + "-" + "99");
    $("#to").mask("2999" + "-" + "99" + "-" + "99");
    timer()
    // 模式窗体
    $('#insertShowDialog').dialog({
        width: 800,
        height: 850,
        autoOpen: false,// 打开模式窗体
        resizable: true,// 是否可以调整模式窗体的大小
        draggable: true,// 是否可以拖动模式窗体
        modal: true,// 启用模式窗体
        closeOnEscape: true,// 按下esc可以退出
        bgiframe: true,
        buttons: {
            "确定": function(){
                checkNotNullAll();
            },
            "取消 ": function(){
                $("#insertcodeerror").css("display", "none");
                $("#insertnameerror").css("display", "none");
                $(this).dialog("close");
            }
        }
    });
});

// 提交查询表单
function selectDictionary(){
    $("#rowedtable").clearGridData();
    var newurl = "getFileGrouping.action?" + $('#jsonForm').formSerialize();
    $("#rowedtable").jqGrid('setGridParam', {
        url: newurl
    }).trigger("reloadGrid");
}

// 下列拉表框
function select(){
	//分类编码
	$.getJSON("docTypeList.action", function(json) {
		var dd = "<option value=''>无</option>";
		for(var i = 0;i<json.length;i++){
			dd += "<option value=\"" + json[i].code + "\" >"
			 + json[i].code + "</option>";
		 }
		$("#docCode").html(dd);
	});
	var parentName = "<option value=''  >无</option>";
	$.getJSON("docTypeList.action", function(json) {
		for(var i = 0;i<json.length;i++){
			parentName += "<option value=\"" + json[i].code + "\" >"
			 + json[i].name + "</option>";
		 }
		$("#parentCode").html(parentName);
	});
	
	//文档分类
	var docClassSpan = $("#docClassspan").text();
	 $.getJSON('../system/dictionaryType.action',{
       paramValue:docClassSpan},
       function(json){
		 var docclassName = "<option value=''>无</option>";
		 for(var i = 0;i<json.length;i++){
			 docclassName += "<option value=\"" + json[i].id + "\" >"
			 + json[i].name + "</option>";
		 }
		 $("#docClass").html(docclassName);
    });
	 //专业
	 var zhuanyeSpan = $("#zhuanyeSpan").text();
	 $.getJSON('../system/dictionaryType.action',{
	       paramValue:zhuanyeSpan},
	       function(json){
			 var zhuanye = "<option value=''>无</option>";
			 for(var i = 0;i<json.length;i++){
				 zhuanye += "<option value=\"" + json[i].id + "\" >"
				 + json[i].name + "</option>";
			 }
			 $("#docZhuanye").html(zhuanye);
			 $("#insertspeciality").html(zhuanye);
	    });
	//保密
	 var baomispan = $("#baomispan").text();
	 $.getJSON('../system/dictionaryType.action',{
        paramValue:baomispan},
        function(json){
		 var baomi = "<option value=''>无</option>";
		 for(var i = 0;i<json.length;i++){
			 baomi += "<option value=\"" + json[i].id + "\" >"
			 + json[i].name + "</option>";
		 }
		 $("#docBaomi").html(baomi);
		 $("#insertdftSecurity").html(baomi)
     });
	 
}

// Gqrid表格
function tableJqGrid(){
    $("#rowedtable").clearGridData();
    var newurl = "getFileGrouping.action?" + $('#jsonForm').formSerialize();
    $("#rowedtable").jqGrid({
        url: newurl,
        datatype: "json",
        type: "POST",
        colNames: ['分类编码', '分类名称', '默认密级', '权限控制', '创建者', '专业', '创建时间', '操作'],
        colModel: [{
            id: 'code',
            name: 'code',
            index: 'code',
            align: 'center'
        }, {
            id: 'name',
            name: 'name',
            index: 'name',
            align: 'left'
        }, {
            id: 'dftSecurity',
            name: 'dftSecurity',
            index: 'dftSecurity',
            align: 'center'
        }, {
            id: 'privilege',
            name: 'privilege',
            index: 'privilege',
            align: 'center'
        }, {
            id: 'username',
            name: 'username',
            index: 'username',
            align: "center"
        
        }, {
            id: 'speciality',
            name: 'speciality',
            index: 'speciality',
            align: 'center'
        }, {
            id: 'createdate',
            name: 'createdate',
            index: 'createdate',
            align: 'center'
        
        }, {
            id: "href",
            name: 'href',
            index: 'href',
            align: 'center'
        }],
        rowNum: 10,// 页数
        pager: "#prowed4",// 页码
        caption: "档案分类信息",// 标题
        forceFit: true,// 是否超出DIV
        multiselect: true,// 是否有全选
        viewrecords: true,// 是否显示多少行
        multiselectWidth: '40',// 调整选择的宽度
        emptyrecords: '数据为空',// 空记录时的提示信息
        width: '750',// 表格宽度
        sortable: false,// 是否支持排序,
        loadtext: "正在加载数据，请稍候……",
        height: 'auto',
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

// 插入的方法
function insertFunction(){
    select();
    checkupdateCode();
    
    $("#insertcode").attr("value", "");
    $("#insertcode").attr("readOnly", false);
    $("#updateCode").attr("value", "");
    $("#insertcodeerror").css("display", "none");
    $("#insertcodeerrorvalue").css("display", "none");
    $("#insertname").attr("value", "");
    $("#remark").attr("value", "");
    $("#parentCode").attr("value", "");
    var selects = $('input[name="rolecheck"]');
    $(selects).attr('checked', false);
    rolefunction();
    $("#insertShowDialog").dialog('open');
}

// 删除文档信息
function deleteInfo(){
    var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
    var len = selectedIds.length;
    if (len == 0) {
        alert("请您选择删除的文档分类！");
    }
    else {
        var button = window.confirm("是否要删除所选文档分类?");
        if (button) {
            var stringsplit = "";
            for (var i = 0; i < selectedIds.length; i++) {
                if (selectedIds[i] != undefined) {
                    stringsplit += "," + selectedIds[i];
                }
            }
            $.getJSON("getDeleteFileGrouping.action?time=" + new Date().getTime(), {
                codes: stringsplit.substr(1)
            }, function(data){
                if (data == "success") {
                    alert("删除成功!");
                    var strs = stringsplit.split(",");
                    for (var i = 0; i < strs.length; i++) {
                        if (strs[i] != undefined) {
                            var codeSel = document.getElementById("docCode");
                            for (var k = 0; k < codeSel.options.length; k++) {
                                if (codeSel.options[k].value == strs[i] && codeSel.options[k].value != '') {
                                    codeSel.options.remove(k);
                                }
                            }
                        }
                    }
                    selectDictionary();
                }else if(data=="true"){
                	alert("该父分类存在子分类，请先处理子分类！");
                	selectDictionary();
                }
                else {
                    alert("删除失败");
                }
            });
        }
    }
}

// 操作权限
function rolefunction(id){
    id = id == null ? "" : id;
    $("#roletable tr:not(:eq(0))").remove();
    // 找到要追加的表格
    var newtable = $("#roletable");
    $.post("getRoleProiorityAction.action", {
        id: id
    
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

// 删除授 权信息
function deleteroleinfo(){
    // 取出第一列所有在值
    $("#roletable").find("tr:not(:eq(0))").each(function(){
        var row = $(this);
        $(this).find("td:eq(0)").children().each(function(){
            if ($(this).attr("checked") == true) {
                row.find("td").children()[2].checked = false;
                row.find("td").children()[3].checked = false;
                row.find("td").children()[4].checked = false;
                $(this).attr("checked", false);
            }
        })
    })
}

// 更新的方法
function updateFile(id){

    $("#insertcode").attr("readOnly", true);
    rolefunction(id);
    $("#insertcodeerror").css("display", "none");
    $("#insertcodeerrorvalue").css("display", "none");
    $("#insertcodeerrorvalue").css("display", "none");
    
    
    
    if (id == "" || id == null) {
        $("#insertShowDialog").dialog('open');
    }
    else {
        updateSelect(id);
        $("#insertShowDialog").dialog('open');
    }
}


function updateSelect(id){
    $.getJSON("getDocTypeByCode.action", {
        id: id
    }, function(json){
        for (var i = 0; i < json.length; i++) {
            $("#insertcode").attr("value", json[i].code);
            $("#insertname").attr("value", json[i].name);
            $("#remark").attr("value", json[i].remark);
            $("#insertdftSecurity").attr("value", json[i].baomi);
            $("#insertspeciality").attr("value", json[i].zhuanye);
            $("#updateCode").attr("value", json[i].code);
            $("#parentCode").attr("value", json[i].pcode);
            
            if (json[i].comFloat == "0") {
                $("#insertcomFlagfalse").attr("checked", true);
            }
            else {
            
                $("#insertcomFlagtrue").attr("checked", true);
            }
        }
    });
}

// 非空验证全部
function checkNotNullAll(){
    var codefunction = codetext();
    var namefunction = nametext();
    if (codefunction && namefunction) {
        roleValue();
        $('#myform').ajaxSubmit({
            dataType: "json",
            beforeSubmit: function(){
            },
            type: "post",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            success: backCallInsert
        });
    }
}

function checkupdateCode(){
    var updateCode = $("#updateCode").val();
    if (updateCode.len != 0) {
        $("#insertcodeerror").css("display", "none");
        $("#insertcodeerrorvalue").css("display", "none");
    }
    if (updateCode == "") {
        $("#insertcodeerror").css("display", "block");
        $("#insertcodeerrorvalue").css("display", "none");
    }
}

function backCallInsert(data, status, xhr){
    if (data == "true") {
        alert("保存成功 ");
        $("#insertShowDialog").dialog('close');
        selectDictionary();
        select();
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

// 非空验证分类编码
function codetext(){
    var parentCode = $("#parentCode").val();
    var code = $("#insertcode").val();
    var updateCode = $("#updateCode").val();
    var reg = /^[A-Z]{2}[0-9]{2}[A-Z]{2}$/;
    var reg1 = /^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$/;
    var reg2 = /^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{2}$/;
    if (reg.exec(code) || reg1.exec(code) || reg2.exec(code)) {
        if ($.getJSON("selectDocTypeCode.action", {
            seleceCode: code
        }, function(json){
            if (json == 'true') {
                checkupdateCode();
                return false;
            }
            else 
                if (json == 'false') {
                    $("#insertcodeerror").css("display", "none");
                    $("#insertcodeerrorvalue").css("display", "none");
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
        $("#insertcodeerrorvalue").css("display", "block");
        return false;
    }
}

// 非空验证分类名称
function nametext(){
    if (jQuery.trim($("#insertname").val()) != "") {
        $("#insertnameerror").css("display", "none");
        return true;
    }
    else {
        $("#insertnameerror").css("display", "block");
        return false;
    }
}

// 分类编码的规则
function codeRule(obj){
    var parentCode = $(obj).val();
    var code = $("#insertcode").val();
    if (parentCode == "") {
        $("#insertcode").attr("value", "");
    }
    else {
        $("#insertcode").attr("value", parentCode);
    }
}


// 时间控件
function timer(){
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
