var tempurl = "../search/getNodeListInFormation.action";
$(document).ready(function(){
    $.ajaxSetup({
        cache: false
    });
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
    	rowNum : 10,// 页数
		pager : "#prowed4",// 页码
	    caption: "定位列表",
		forceFit : true,// 是否超出DIV
		multiselect : true,// 是否有全选
		viewrecords : true,// 是否显示多少行
		multiselectWidth : '40',// 调整选择的宽度
		emptyrecords : '数据为空',// 空记录时的提示信息
		width : '760',// 表格宽度
		sortable : false,// 是否支持排序,
		loadtext : "正在加载数据，请稍候……",
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
});

//点击预览时触发的函数
function previewImage(id){
    $("#preimge").attr("src", "../search/getPreView.action?id=" + id);
    $('#dialog').dialog('open');
}

//点击打包下载时触 发的事件
function packAgeDownLoad(){
    var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
    var len = selectedIds.length;
    if (len == 0) {
        alert("请选择打包下载的文件!");
    }
    else {
        $("#showdialog").dialog('open');
    }    
}

$(function(){
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
                        stringsplit += "," + selectedIds[i];
                    }
                }
                tdocname=escape(encodeURIComponent(tdocname)); 
                var url = "../search/getPackAgeDownLoad.action?eachCheckbox=" + stringsplit + "&filename=" + tdocname;
                $("#packAgeDownLoad").attr("src", url);
                $("#showdialog").dialog('close');                
            }
        },
        close: function(){
            $("#tdocname").attr("val", "");
        }        
    });
    
    // Initialize the tree inside the <div>element.
    // The tree structure is read from the contained <ul> tag.
    $("#tree").dynatree({
        title: "Programming Sample",
        onLazyRead: function(node){
            //alert("a"+node.data.key);
            var id = node.data.key.id;
            var title = node.data.title;
            var options = getOptions(node, "");            
            $.getJSON("../search/getNode.action", {
                nodeId: id,
                nodeName: title,
                sign: false,
                options: options
            }, function(json){
                node.addChild(json);
                node.setLazyNodeStatus(DTNodeStatus_Ok);
            });
        },
        onActivate: function(node){
            var tempstring = "";
            if (node.data.isFolder == false) {
                tempstring = getOptions(node, tempstring);
                $("#button9").attr("disabled", false);
                var newUrl = tempurl + "?options=" + tempstring; 
                $("#rowedtable").clearGridData();              
                $("#rowedtable").setGridParam({
                    url: newUrl
                }).trigger("reloadGrid");
                
            }
        }
    });
    
    //初始化调用
    $.getJSON("../search/getNode.action", {
        nodeId: id,
        sign: true //true：查本级节点 false：查子节点
    }, function(json){
        $("#tree").dynatree("getRoot").addChild(json);
    });
    
});
//取得该节点及父节点的信息
function getOptions(node, tempstring){
    var position = node.data.title;
    //("#position").text(position);
    tempstring = node.data.key.parm + "=" + node.data.key.value;    
    while (node.parent != null) {
        node = node.parent;
        if (node.data.title) {
            tempstring += ";" + node.data.key.parm + "=" + node.data.key.value;
            position = node.data.title + " -> " + position;
            $("#position").text(position);
        }
    }
    return tempstring;
}
