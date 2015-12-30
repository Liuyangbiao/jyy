$(document).ready(function(){
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
    $.ajaxSetup({
        cache: false
    });
    // Jqgrid表格
    tableJqGrid();
    // 下拉列表框
    select();
    // 模式窗体
    $('#insertShowDialog').dialog({
        width: 600,
        height: 750,
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
                $("#insertcodeerrorValue").css("display", "none");
                $(this).dialog("close");
            }
        }
    });
});
// 提交查询表单
function selectDictionary(){
    $("#rowedtable").clearGridData();
    var newurl = "getDictionaryData.action?" + $('#jsonForm').formSerialize();
    $("#rowedtable").jqGrid('setGridParam', {
        url: newurl
    }).trigger("reloadGrid");
}

// 更新与插入的回调函数
function backCallInsert(data, status, xhr){
    if (data == "success") {
        alert("保存成功 ");
        $("#insertShowDialog").dialog('close');
        selectDictionary();
        select();
    }
    else {
        alert("保存失败");
    }
}

// 增加
function insertDictionary(){
    select();
    $("#updateCode").attr("value", "");
    $("#insertcode").attr("value", "");
    $("#insertcodeerror").css("display","none");
    $("#insertnameerror").css("display","none");
    $("#insertcodeerrorValue").css("display","none");
    $("#insertcode").attr("readOnly", false);
    // 将文本框 显示，并清空值
    $("#insertname").attr("value", "");
    $("#insertremark").attr("value", "");
    var selects = $('input[name="rolecheck"]');
    $(selects).attr('checked', false);
    rolefunction();
    $("#insertShowDialog").dialog('open');
}

// 操作权限
function rolefunction(id){
    id = id == null ? "" : id;
    $("#roletable tr:not(:eq(0))").remove();
    // 找到要追加的表格
    var newtable = $("#roletable");
    $.post("getRoleProioityAction.action", {
        code: id
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

// 下列拉表框
function select(){
    $.getJSON("getSelectDictionary.action", function(json){
        var diccode = "<option value='0'  >无</option>";
        var parentcode = "<option value='kong'  >无</option>";
        for (var i = 0; i < json.length; i++) {
            diccode += "<option value=\"" + json[i].code + "\" >" + json[i].code + "(" + json[i].name + ")" + "</option>";
        }
        for (var i = 0; i < json.length; i++) {
            parentcode += "<option value=\"" + json[i].code + "\" >" + json[i].code + "(" + json[i].name + ")" + "</option>";
        }
        $("#diccode").html(diccode);
        $("#parentCode").html(parentcode);
    });
}

// JqGrid表格
function tableJqGrid(){
    var newurl = "getDictionaryData.action?" + $('#jsonForm').formSerialize();
    $("#rowedtable").clearGridData();
    $("#rowedtable").jqGrid({
        url: newurl,
        datatype: "json",
        type: "POST",
        colNames: ['字典编码', '字典名称', '权限控制', '创建人', '创建时间', '备注', '操作'],
        colModel: [{
            id: 'code',
            name: 'code',
            index: 'code',
            align: 'left'
        }, {
            id: 'name',
            name: 'name',
            index: 'name',
            align: 'left'
        }, {
            id: 'authControl',
            name: 'authControl',
            index: 'authControl',
            align: 'left'
        }, {
            id: 'username',
            name: 'username',
            index: 'username',
            align: 'center'
        }, {
            id: 'createDate',
            name: 'createDate',
            index: 'createDate',
            align: 'center'
        }, {
            id: 'remark',
            name: 'remark',
            index: 'remark',
            align: 'left'
        }, {
            id: "operate",
            name: 'operate',
            index: 'operate',
            align: 'center'
        }],
        rowNum: 10,
        width: '750',
        multiselectWidth: '40',// 调整选择的宽度
        multiselect: true,
        loadtext: '正在加载数据，请稍等...',// 获得数据时的提示信息
        emptyrecords: '数据为空',// 空记录时的提示信息
        pager: "#prowed4",
        caption: "数据字典信息",
        // autowidth: true,
        height: 'auto',
        viewrecords: true,
        forceFit: true,
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

// 更新事件
function updateDictionary(id){
    $("#insertremark").attr("value", "");
    rolefunction(id);
    $("#insertcode").attr("readOnly", true);
    $("#insertcodeerror").css("display","none");
    $("#insertnameerror").css("display","none");
    $("#insertcodeerrorValue").css("display","none");
    if (id == "" || id == null) {
        $("#insertShowDialog").dialog('open');
    }
    else {
        updateSelect(id);
        $("#insertShowDialog").dialog('open');
    }
}

function updateSelect(id){
    $.getJSON("getDictionaryByCode.action", {
        code: id
    }, function(json){
        for (var i = 0; i < json.length; i++) {
            $("#insertcode").attr("value", json[i].code);
            $("#insertname").attr("value", json[i].dicName);
            $("#updateCode").attr("value", json[i].code);
            $("#insertremark").attr("value", json[i].remark);
            if(json[i].pCode==null){
        		$("#parentCode").attr("value", "kong");
        	}else {
        		$("#parentCode").attr("value", json[i].pCode);	
        	}
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

// 取操作权限的值
function roleValue(){
    var allChkStr = "";
    // 取出表格的行数
    $("#roletable").find("tr:not(:eq(0))").each(function(){
        var roleid = $(this).attr("id");
        var chkStr ="";
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

// 非空验证全部
function checkNotNullAll(){
    var code = codetext();
    
    var name = nametext();
    if (code && name) {
        roleValue();
        
        $('#myform').ajaxSubmit({
            dataType: "json",
            beforeSubmit: function(){// alert("aa"); return true;}
            },
            type: "post",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            success: backCallInsert
        });
    }
}

// 非空验证字典编码
function codetext(){
    var insertcode = $("#insertcode").val();
    if (jQuery.trim(insertcode) != "") {
    
        if ($.getJSON("selectDictionaryCode.action", {
            checkCode: insertcode
        }, function(json){
            if (json == 'true') {
                checkupdateCode();
                return false;
            }
            else 
                if (json == 'false') {
                    $("#insertcodeerror").css("display", "none");
                    $("#insertcodeerrorValue").css("display", "none");
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
        $("#insertcodeerror").css("display", "block");
        return false;
    }
}

function checkupdateCode(){

    var updateCode = $("#updateCode").val();
    if (updateCode.len != 0) {
        $("#insertcodeerror").css("display", "none");
        $("#insertcodeerrorValue").css("display", "none");
    }
    if (updateCode == "") {
        $("#insertcodeerrorValue").css("display", "block");
    }
}

// 非空验证字典名称
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

// 删除文档信息
function deleteInfo(){

    var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
    var len = selectedIds.length;
    if (len == 0) {
        alert("请您选择删除的字典信息吗！");
    }
    else {
        var button = window.confirm("是否要删除所选字典项?");
        if (button) {
            var stringsplit = "";
            for (var i = 0; i < selectedIds.length; i++) {
                if (selectedIds[i] != undefined) {
                    stringsplit += "," + selectedIds[i];
                }
            }
            
            $.getJSON("getDeleteDictionary.action", {
                codes: stringsplit.substr(1)
            }, function(data){
                if (data == "success") {
                    alert("删除成功!");
                    var strs = stringsplit.split(",");
                    for (var i = 0; i < strs.length; i++) {
                        if (strs[i] != undefined) {
                            var codeSel = document.getElementById("diccode");
                            for (var k = 0; k < codeSel.options.length; k++) {
                                if (codeSel.options[k].value == strs[i]) {
                                    codeSel.options.remove(k);
                                }
                            }
                        }
                    }
                    selectDictionary();
                }
                else {
                    alert("删除失败");
                }
            });
        }
        else {
            return;
        }
    }
}
