/**
 * 授权管理表格
 */
function authtableJqgrid(){
    var authurl = "selectProfileAll.action?" + $('#profileForm').formSerialize();
    $("#authrowedtable").clearGridData();
    $("#authrowedtable").jqGrid({
        url: authurl,
        datatype: "json",
        type: "POST",
        colNames: ['权限分类', '控制代码', '控件名称', '对象描述', '排序位置','创建日期', '操作'],
        colModel: [{
            id: 'authClass',
            name: 'authClass',
            index: 'authClass',
            align: 'left'
        }, {
            id: 'kongCode',
            name: 'kongCode',
            index: 'kongCode',
            align: 'left'
        }, {
            id: 'kongName',
            name: 'kongName',
            index: 'kongName',
            align: 'left'
        }, {
            id: 'object',
            name: 'object',
            index: 'object',
            align: 'center'
        }, {
            id: 'sortNo',
            name: 'sortNo',
            index: 'sortNo',
            align: 'left'
        },  {
            id: 'createDate',
            name: 'createDate',
            index: 'createDate',
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
        caption: "权限信息管理",
        forceFit: true,// 是否超出DIV
        multiselect: true,// 是否有全选
        viewrecords: true,// 是否显示多少行
        multiselectWidth: '40',// 调整选择的宽度
        emptyrecords: '数据为空',// 空记录时的提示信息
        width: '750',// 表格宽度
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


/**
 * 角色授权表格
 */
function roletableJqgrid(){
    var roleurl = "selectRoleProfile.action?" + $('#roleProfileName').formSerialize();
    $("#rolerowedtable").clearGridData();
    $("#rolerowedtable").jqGrid({
        url: roleurl,
        datatype: "json",
        type: "POST",
        colNames: ['角色名称', '控制对象', '授权操作', '授权人', '创建日期', '操作'],
        colModel: [{
            id: 'rolename',
            name: 'rolename',
            index: 'rolename',
            align: 'left'
        }, {
            id: 'roleobject',
            name: 'roleobject',
            index: 'roleobject',
            align: 'left'
        }, {
            id: 'rolehref',
            name: 'rolehref',
            index: 'rolehref',
            align: 'left'
        }, {
            id: 'roleUser',
            name: 'roleUser',
            index: 'roleUser',
            align: 'center'
        }, {
            id: 'roledate',
            name: 'roledate',
            index: 'roledate',
            align: 'center'
        }, {
            id: 'rolehref',
            name: 'rolehref',
            index: 'rolehref',
            align: 'center'
        }],
        height: 'auto',
        rowNum: 10,// 页数
        pager: "#roleprowed4",// 页码
        caption: "角色授权管理",
        forceFit: true,// 是否超出DIV
        multiselect: true,// 是否有全选
        viewrecords: true,// 是否显示多少行
        multiselectWidth: '40',// 调整选择的宽度
        emptyrecords: '数据为空',// 空记录时的提示信息
        width: '750',// 表格宽度
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


/**
 * 用户授权表格
 */
function usertableJqgrid(){
    var userurl = "selectUserProfileInfo.action?" + $('#userProfileForm').formSerialize();
    $("#userrowedtable").clearGridData();
    $("#userrowedtable").jqGrid({
        url: userurl,
        datatype: "json",
        type: "POST",
        colNames: ['用户名称', '控制对象', '授权操作', '授权人', '创建日期', '操作'],
        colModel: [{
            id: 'username',
            name: 'username',
            index: 'username',
            align: 'left'
        }, {
            id: 'userobject',
            name: 'userobject',
            index: 'userobject',
            align: 'left'
        }, {
            id: 'profileHref',
            name: 'profileHref',
            index: 'profileHref',
            align: 'left'
        }, {
            id: 'user',
            name: 'user',
            index: 'user',
            align: 'center'
        }, {
            id: 'userdate',
            name: 'userdate',
            index: 'userdate',
            align: 'center'
        }, {
            id: 'userhref',
            name: 'userhref',
            index: 'userhref',
            align: 'center'
        }],
        height: 'auto',
        rowNum: 10,// 页数
        pager: "#userprowed4",// 页码
        caption: "用户授权管理",
        forceFit: true,// 是否超出DIV
        multiselect: true,// 是否有全选
        viewrecords: true,// 是否显示多少行
        multiselectWidth: '40',// 调整选择的宽度
        emptyrecords: '数据为空',// 空记录时的提示信息
        width: '750',// 表格宽度
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

/**
 * 下拉列表框
 */
function select(){
    getTProfileByparentID();
    var authclass = $("#authClassSpan").text();
	 $.getJSON('../system/dictionaryType.action',{
      paramValue:authclass},
      function(json){
		 var authClass = "";
		 for(var i = 0;i<json.length;i++){
			 authClass += "<option value=\"" + json[i].id + "\" >"
			 + json[i].name + "</option>";
		 }
		 $("#authClass").html(authClass);
	        $("#roleAuthClass").html(authClass);
	        $("#userAuthClass").html(authClass);
	        $("#inserProClass").html(authClass);
	        $("#insertProfileType").html(authClass);
	        getTProfileByparentID();
	        authtableJqgrid();
	        roletableJqgrid();
	        roleselect();
   });
}
function getTProfileByparentID(){
	 var type="P01";
	    $.getJSON("getTProfileByparentId.action",{
			 parentId:type
		 },function(json){
	  	 if (json.length != 0) {
				 var parentId = "<option value='' >无</option>";
			 		for(var i =0;i<json.length;i++){
		 	           	parentId += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
		 	            $("#parentId").html(parentId);
			 	        }
			 		}
			 		 else {
			           var parentId = "<option value='' >无</option>";
			 	         $("#parentId").html(parentId);
			 	  }	
		   });
}
/**
 * 角色名称下拉列表框
 */
function roleselect(){
    $.getJSON("roleList.action", function(json){
        var role = "<option value=''  >无</option>";
        var insertRoleId = "";
        for (var i = 0; i < json.length; i++) {
            role += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
            insertRoleId += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
        }
        $("#roleId").html(role);
        $("#userRoleId").html(insertRoleId);
        $("#insertRoleId").html(insertRoleId);
        $("#insertuserRoleName").html(insertRoleId);
        var userRoleId = $("#insertuserRoleName").val();
        allUserselect(userRoleId);
        usertableJqgrid();
        var obj = $("#insertProfileType");
        

        getProfileType(obj);

    });
}

/**
 * 查询授权
 */
function selectProfile(){
    $("#authrowedtable").clearGridData();
    var newurl = "selectProfileAll.action?" + $('#profileForm').formSerialize();
    $("#authrowedtable").jqGrid('setGridParam', {
        url: newurl
    }).trigger("reloadGrid");
}

/**
 * 删除权限定义
 */
function deleteProfile(){
    var selectedIds = $("#authrowedtable").jqGrid("getGridParam", "selarrrow");
    var len = selectedIds.length;
    if (len == 0) {
        alert("请您选择删除的权限信息！");
    }
    else {
        var profileId = "";
        for (var i = 0; i < selectedIds.length; i++) {
            if (selectedIds[i] != undefined) {
                profileId += "," + selectedIds[i];
            }
        }
        var result = confirm("删除该权限，将删除与之相关的授权信息，您确定要否删除吗？");
        if (result == true) {
            $.getJSON("deleteProfile.action", {
                profileIdList: profileId.substr(1)
            }, function(json){
                if (json == "success") {
                    alert("删除成功!");
                    selectProfile();
                }
                else {
                    alert("删除失败!");
                }
            });
        }
    }
}

/**
 * 增加权限的模式窗体
 */
function insertProfileDialog(){
    // 模式窗体
    $('#insertProfile').dialog({
        width: 600,
        height: 550,
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

/**
 * 插入权限的事件
 * 
 * @param {Object}
 *            id 授权ID 已判断是调插入或更新的action
 */
function insertProfileshowDialog(id,type){
	$("#sortNo").attr("value","");
	$("#zhuimageUrl").attr("value","");
    $("#profileRemark").attr("value", "");
    $("#insertProfile").dialog('open');
    $("#insertProfileId").attr("value", "");
    $("#insertProfileName").attr("value", "");
    $("#profileUrl").attr("value", "");
    $("#profileRemark").attr("value", "");
    $("#updateProfileId").attr("value", "");
    $("#insertProfileIderror").css("display", "none");
    $("#insertProfileNameerror").css("display", "none");
    $("#insertProfileIderrorvalue").css("display", "none");
    $("profileUrlerror").css("display", "none");
	$("#remark3true").attr("checked", true);
	$("#sortNoerror").css("display", "none");
	$("#remark4true").attr("checked", true);
    id = id == null ? "" : id;
    if (id == "" || id == null) {
    	 $.getJSON("authClass.action", function(json){
    	        var authClass = "";
    	        for (var i = 0; i < json.length; i++) {
    	            authClass += "<option value=\"" + json[i].code + "\" >" + json[i].name + "</option>";
    	        }
    	       $("#inserProClass").html(authClass);
    	    });
    	 $("#imgId").css("display","none");
    	 $("#imageUrl").css("display","none");
    	 $("#zhuimageUrl").css("display","block");
 		$("#divurl").css("display","none");
 		$("#spandiv").css("display","none");
 		$("#divisParent").css("display","none");
 		$("#spanisParent").css("display","none");
    	$("#insertProfileId").attr("readOnly", false);
        $("#insertProfile").dialog('open');
    }
    else {

       if(type=="P02"){
			$("#divurl").css("display","block");
			$("#spandiv").css("display","block");
			$("#divisParent").css("display","block");
			$("#spanisParent").css("display","block");
			 $("#imageUrl").css("display","block");
			 $("#imgId").css("display","block");
			 $("#zhuimageUrl").css("display","none");
		}else{
    		$("#divurl").css("display","none");
			$("#spandiv").css("display","none");
    		$("#divisParent").css("display","none");
     		$("#spanisParent").css("display","none");
     		 $("#imageUrl").css("display","none");
     		$("#imgId").css("display","none");
     		$("#zhuimageUrl").css("display","block");
    	}
    	$("#insertProfileId").attr("readOnly", true);
    	  
        updateProfileSelect(id);
        $("#insertProfile").dialog('open');
    }
}

/**
 * 更新授权信息，将查询出来的结果，显示到页面上
 * 
 * @param {Object}
 *            id 授权ID
 */
function updateProfileSelect(id){
    $.getJSON("getProfileById.action", {
        id: id
    }, function(json){
        for (var i = 0; i < json.length; i++) {

            $("#insertProfileId").attr("value", json[i].id);
            $("#insertProfileName").attr("value", json[i].name);
            $("#profileUrl").attr("value", json[i].url);
            $("#profileRemark").attr("value", json[i].remark);
            $("#updateProfileId").attr("value", json[i].id);
           	$("#isParent").attr("value", json[i].isParent);
           	
           	$("#sortNo").attr("value",json[i].sortNo);
           	if(json[i].type=="P02"){
           		$("#imageUrl").attr("value",json[i].imageurl);
                var vSelect = $("#imageUrl option:selected");  
                $("#imgId").attr("src","../"+vSelect.val());	
           	}else {
           		$("#zhuimageUrl").attr("value",json[i].imageurl);
           	}
            $("#inserProClass").attr("value", json[i].type);
            
            if (json[i].shensuo=="1") {
    			$("#remark3true").attr("checked", true);
    		} else {
    			$("#remark3false").attr("checked", true);
    		}
            if (json[i].keyong=="1") {
    			$("#remark4true").attr("checked", true);
    		} else {
    			$("#remark4false").attr("checked", true);
    		}
        }
    });
}

/**
 * 插入权限的非空验证
 */
function checkPriFileNotNullAll(){
    var profileId = checkProfileID();
    var profileName = checkProfileName();
   var sortNo = checkSortNo();
    if (profileId && profileName && sortNo) {
        $('#insertProfileForm').ajaxSubmit({
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
 * 权限插入成功的回调函数
 * 
 * @param {Object}
 *            data json
 * @param {Object}
 *            status
 * @param {Object}
 *            xhr
 */
function profilebackCallInsert(data, status, xhr){
    if (data == "true") {
        alert("保存成功 ");
        selectProfile();
        
        $("#insertProfile").dialog('close');
    }
    else {
        alert("保存失败");
    }
}

/*
 * 
 * 非空验证权限ID
 */
function checkProfileID(){
    var id = $("#insertProfileId").val();
    var num = /\d{1,2}/;
    if (num.exec(id)) {
        if ($.getJSON("selectProfileId.action", {
            id: id
        }, function(json){
            if (json == 'true') {
                checkupdateId();
                return false;
            }
            else 
                if (json == 'false') {
                    $("#insertProfileIderror").css("display", "none");
                    $("#insertProfileIderrorvalue").css("display", "none");
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
        $("#insertProfileIderror").css("display", "block");
        return false;
    }
}

/**
 * 验证更新ID
 */
function checkupdateId(){
    var updateCode = $("#updateProfileId").val();
    if (updateCode.len != 0) {
        $("#insertProfileIderror").css("display", "none");
        $("#insertProfileIderrorvalue").css("display", "none");
    }
    if (updateCode == "") {
        $("#insertProfileIderrorvalue").css("display", "block");
    }
}

/**
 * 非空验证权限Name
 */
function checkProfileName(){
    if (jQuery.trim($("#insertProfileName").val()) != "") {
        $("#insertProfileNameerror").css("display", "none");
        return true;
    }
    else {
        $("#insertProfileNameerror").css("display", "block");
        return false;
    }
}

/**
 * 所属权限分类
 * 
 * @param {Object}
 *            obj
 */
function getisParentTProfileByType(type){
	var typeClass="";
    $.getJSON("getTProfileByType.action",{
    	parenttype:type
    }, function(json){
        for (var i = 0; i < json.length; i++) {
            typeClass += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
    
        }
        $("#isParent").html(typeClass);
    });
}

/**
 * 验证授权URL
 */
function checkProfileUrl(){
    if (jQuery.trim($("#profileUrl").val()) != "") {
        $("#profileUrlerror").css("display", "none");
        return true;
    }
    else {
        $("#profileUrlerror").css("display", "block");
        return false;
    }
}

/**
 * 查询角色授权
 */
function selectRoleProfile(){
    var newurl = "selectRoleProfile.action?" + $('#roleProfileName').formSerialize();
    $("#rolerowedtable").clearGridData();
    $("#rolerowedtable").jqGrid('setGridParam', {
        url: newurl
    }).trigger("reloadGrid");
}

/**
 * 删除角色授权
 */
function deleteRoleProfile(){
    var selectedIds = $("#rolerowedtable").jqGrid("getGridParam", "selarrrow");
    var len = selectedIds.length;
    if (len == 0) {
        alert("请您选择删除的角色信息！");
    }
    else {
        var profileId = "";
        for (var i = 0; i < selectedIds.length; i++) {
            if (selectedIds[i] != undefined) {
                profileId += ";" + selectedIds[i];
            }
        }
        var result = confirm("您确定要否删除吗？");
        if (result == true) {
            $.getJSON("deleteRoleProfile.action", {
                deleteRolefileList: profileId.substr(1)
            }, function(json){
                if (json == "success") {
                    alert("删除成功!");
                    selectRoleProfile();
                }
                else {
                    alert("删除失败!");
                }
            });
        }
    }
}

/**
 * 增加权限的模式窗体
 */
function insertRoeProfileShowDailog(){
    // 模式窗体
    $('#insertRoleProfile').dialog({
        width: 500,
        height: 350,
        autoOpen: false,// 打开模式窗体
        resizable: true,// 是否可以调整模式窗体的大小
        draggable: true,// 是否可以拖动模式窗体
        modal: true,// 启用模式窗体
        closeOnEscape: true,// 按下esc可以退出
        bgiframe: true
    });
}

function checkRoleProfileNotNullAll(){
	
    roleValue();
    $('#insertRoleProfileForm').ajaxSubmit({
        dataType: "json",
        beforeSubmit: function(){
        },
        type: "post",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success: rolebackCallInsert
    });
}

// 更新与插入的回调函数
function rolebackCallInsert(data, status, xhr){
    if (data == "success") {
        alert("保存成功 ");
        $("#insertRoleProfile").dialog('close');
        selectRoleProfile();
    }
    else {
        alert("保存失败");
    }
}

/**
 * 插入角色授权
 * 
 * @param {Object}
 *            roleId 角色ID
 * @param {Object}
 *            profileId 权限ID
 * @param {Object}
 *            type
 * @param {Object}
 *            roleName 角色名称
 * @param {Object}
 *            objName 控制对象
 * @param {Object}
 *            objtype 权限分类
 */
function insertRoleProfileshowDialog(roleId, profileId, type, roleName, objName, objtype){
    if ((profileId == "" || profileId == null) && (roleId == "" || roleId == null)) {
        $("#divrole").css("display", "block");
        $("#spanrole").css("display", "none");
        $("#temproleName").text("");
        $("#tempinsertProfileObject").text("");
        $("#tempinsertProfileType").text("");
        $("#tempspan").attr("value", "");
        $("#updateroleId").attr("value", "");
        $("#updateroleprofileId").attr("value", "");
        rolefunction($("#insertRoleId").val());
        $("#insertRoleProfile").dialog('open');
    }
    else {
        $("#updateroleId").attr("value", roleId);
        $("#updateroleprofileId").attr("value", profileId)
        $("#divrole").css("display", "none");
        $("#spanrole").css("display", "block");
        $("#temproleName").text(roleName);
        $("#tempinsertProfileObject").text(objName);
        $("#tempinsertProfileType").text(objtype);
        $("#btnroleSave").css("display", "block");
        rolefunction(roleId, profileId);
        $("#insertRoleProfile").dialog('open');
    }
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
    $.post("profileInfo.action", {
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

    $("#rolegrantPrivilege").val(allChkStr.substr(1));
    
}

/**
 * 更新控制类型 重新加载控制对象
 * 
 * @param {Object}
 *            obj 控制类型ID
 */
function getProfileType(obj){
    var type = $(obj).val();
    var roleId = $("#insertRoleId").val();

    $.getJSON("getTprofileByTypeandRoleId.action", {
        type: type,
        roleId: roleId
    }, function(json){
        if (json.length != 0) {
            $("#btnroleSave").css("display", "block");
            var role = "";
            for (var i = 0; i < json.length; i++) {
                role += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
            }
            $("#insertProfileObject").html(role);
        }
        else {
        
            $("#btnroleSave").css("display", "none");
            var role = "<option value=''  >无</option>";
            $("#insertProfileObject").html(role);
        }
    });
}

/**
 * 角色取消事件
 */
function roleEsc(){
    $("#insertRoleProfile").dialog('close');
}

/**
 * 删除用户授权
 */
function deleteUserProfile(){
    var selectedIds = $("#userrowedtable").jqGrid("getGridParam", "selarrrow");
    var len = selectedIds.length;
    if (len == 0) {
        alert("请您选择删除的角色信息！");
    }
    else {
        var userProfileId = "";
        for (var i = 0; i < selectedIds.length; i++) {
            if (selectedIds[i] != undefined) {
                userProfileId += ";" + selectedIds[i];
            }
        }
        var result = confirm("您确定要否删除吗？");
        if (result == true) {
            $.getJSON("deleteUserProfile.action", {
                deleteuserProfiles: userProfileId.substr(1)
            }, function(json){
                if (json == "success") {
                    alert("删除成功!");
                    selectUserProfile();
                }
                else {
                    alert("删除失败!");
                }
            });
        }
    }
}

/**
 * 更新角色ID时触发的事件
 * 
 * @param {Object}
 *            obj 角色ID
 */
function getRoleIdChange(obj){
    var objvalue = ($(obj).val());
    rolefunction(objvalue);
}

/**
 * 查询用户授权
 */
function selectUserProfile(){
    var userurl = "selectUserProfileInfo.action?" + $('#userProfileForm').formSerialize();
    $("#userrowedtable").clearGridData();
    $("#userrowedtable").jqGrid('setGridParam', {
        url: userurl
    }).trigger("reloadGrid");
}

/**
 * 增加权限的模式窗体
 */
function insertUserProfileShow(){
    // 模式窗体
    $('#insertUserRolefile').dialog({
        width: 500,
        height: 350,
        autoOpen: false,// 打开模式窗体
        resizable: true,// 是否可以调整模式窗体的大小
        draggable: true,// 是否可以拖动模式窗体
        modal: true,// 启用模式窗体
        closeOnEscape: true,// 按下esc可以退出
        bgiframe: true
    });
}

/**
 * 插入或更新用户授权
 * 
 * @param {Object}
 *            roleName 角色名称
 * @param {Object}
 *            uName 用户名称
 * @param {Object}
 *            pname 权限名称
 * @param {Object}
 *            uLoginid 用户ID
 * @param {Object}
 *            profileId 权限ID
 * @param {Object}
 *            roleid 角色ID
 */
function insertUserProfile(roleName, uName, pname, uLoginid, profileId, roleid){
    $("#grantPrivilege").attr("value", "");
    $("#btnuserSave").css("display", "block");
    if ((uLoginid == "" || uLoginid == null)) {
        $("#divspanuser").css("display", "none");
        $("#spanUserRolename").text("");
        $("#spanUserName").text("");
        $("#spanObject").text("");
        $("#updateUserId").attr("value", "");
        $("#updateUserProfileId").attr("value", "");
        $("#usertable").css("display", "block");
        userrolefunction($("#insertuserRoleName").val());
        $('#insertUserRolefile').dialog('open');
    }
    else {
        $("#divspanuser").css("display", "block");
        $("#spanUserRolename").text(roleName);
        $("#spanUserName").text(uName);
        $("#spanObject").text(pname);
        $("#updateUserId").attr("value", uLoginid);
        $("#updateUserProfileId").attr("value", profileId);
        $("#usertable").css("display", "none");
        userrolefunction(roleid, uLoginid, profileId);
        $('#insertUserRolefile').dialog('open');
    }
}

/**
 * 关闭用户授权模式窗体
 */
function btnuserProfileEsc(){
  
    $("#insertUserRolefile").dialog('close');
}

/**
 * 用户授权 操作权限
 * 
 * @param {Object}
 *            roleid 角色ID
 * @param {Object}
 *            uLoginid 用户ID
 * @param {Object}
 *            profileId 权限 ID
 */
function userrolefunction(roleid, uLoginid, profileId){
    uLoginid = uLoginid == null ? "" : uLoginid;
    roleid = roleid == null ? "" : roleid;
    profileId = profileId == null ? "" : profileId;
    $("#userroletable tr:not(:eq(0))").remove();
    // 找到要追加的表格
    var newtable = $("#userroletable");
    $.post("userProfileInfo.action", {
        roleId: roleid,
        userId: uLoginid,
        roleProfileId: profileId
    }, function(json){
        for (var i = 0; i < json.length; i++) {
            // 给表格添加一行
            var newtr = $("<tr id='usereachtr'  height='28'></tr>");
            $(newtr).attr("id", json[i].rolebean.roleid);
            // 给表格添加第一列
            var eacheck = $("<td ><input type='checkbox' name='usereachCheck' id='usereachCheck'></td>");
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
 * 更新角色ID时，重新加载所有用户
 * 
 * @param {Object}
 *            obj 角色ID
 */
function getUserRoleChange(obj){
    var objvalue = ($(obj).val());
    userrolefunction(objvalue);
    allUserselect(objvalue);
}

/**
 * 根据角色查询所有用户
 * 
 * @param {Object}
 *            obj 角色ID
 */
function allUserselect(obj){
    $.getJSON("selectAllUser.action", {
        userRoleId: obj
    }, function(json){
        if (json.length != 0) {
            var alluser = "";
            for (var i = 0; i < json.length; i++) {
                alluser += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
            }
            $("#btnuserSave").css("display", "block");
            $("#alluser").html(alluser);
            getTProfileByUserId($("#alluser").val());
        }
        else {
            var user = "<option value=''  >无</option>";
            $("#alluser").html(user);
            $("#btnuserSave").css("display", "none");
            var profile = "<option value=''  >无</option>";
            $("#userProfileObject").html(profile);
            
        }
    });
}

/**
 * 加载用户名称
 * 
 * @param {Object}
 *            userId 传来用户ID
 */
function getTProfileByUserId(userId){
    $("#grantPrivilege").attr("value", "");
    $.getJSON("getTProfileByUserId.action", {
        userId: userId
    }, function(json){
        if (json.length != 0) {
            var allprofile = "";
            for (var i = 0; i < json.length; i++) {
                allprofile += "<option value=\"" + json[i].id + "\" >" + json[i].name + "</option>";
            }
            $("#btnuserSave").css("display", "block");
            $("#userProfileObject").html(allprofile);
        }
        else {
            var profile = "<option value=''  >无</option>";
            $("#userProfileObject").html(profile);
            $("#btnuserSave").css("display", "none");
        }
    });
}

/**
 * 更改用户值，重新加载控制对象
 * 
 * @param {Object}
 *            userId 用户ID
 */
function getUserNameChange(userId){
    var userid = $(userId).val();
    getTProfileByUserId(userid);
}

/**
 * 插入用户权限的方法
 */
function saveUserProfile(){
    userRoleValue();
    $('#insertUserProfimeForm').ajaxSubmit({
        dataType: "json",
        beforeSubmit: function(){
        },
        type: "post",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success: userProfilebackCallInsert
    });
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
function userProfilebackCallInsert(data, status, xhr){
    if (data == "true") {
        alert("保存成功 ");
        $("#insertUserRolefile").dialog('close');
        
        selectUserProfile();
    }
    else {
        alert("保存失败");
    }
}

// 取操作权限的值
function userRoleValue(){
    var allChkStr = "";
    // 取出表格的行数
    $("#userroletable").find("tr:not(:eq(0))").each(function(){
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
    $("#grantPrivilege").attr("value", allChkStr.substr(1));
}

function getProClassChange(obj){
	var type=$(obj).val();
	 if(type=="P02"){
			getisParentTProfileByType("P01");
			$("#divurl").css("display","block");
			$("#spandiv").css("display","block");
			$("#divisParent").css("display","block");
			$("#spanisParent").css("display","block");
			$("#imageUrl").css("display","block");
			$("#imgId").css("display","block");
			 $("#zhuimageUrl").css("display","none");
			}else{
			$("#divurl").css("display","none");
			$("#spandiv").css("display","none");
			$("#divisParent").css("display","none");
			$("#spanisParent").css("display","none");
			$("#imageUrl").css("display","none");
			$("#imgId").css("display","none");
			 $("#zhuimageUrl").css("display","block");
		}
	 }

//图片预览
function imgShow(){
		   var vSelect = $("#imageUrl option:selected");  
          $("#imgId").attr("src","../"+vSelect.val());
	}

function checkSortNo(){
	var sortNo = $("#sortNo").val();
	 var num = /\d{1,2}/;
	    if (num.exec(sortNo)) {
	    	$("#sortNoerror").css("display", "none");
	          return true;	    
	    }else {
    	  $("#sortNoerror").css("display", "block");
	          return false;	    	
	    }
	
}