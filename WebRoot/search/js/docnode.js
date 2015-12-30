var tempurl = "getNodeListInFormation.action";
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
		multiselect : true,// 
		viewrecords : true,// 
		multiselectWidth : '40',// 
		emptyrecords : '数据为空',// 7
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
    $("#preimge").attr("src", "getPreView.action?id=" + id);
    $('#dialog').dialog('open');
}

//点击打包下载时触 发的事件
/*function packAgeDownLoad(){
	$("#tdocname").attr("value","");
    var selectedIds = $("#rowedtable").jqGrid("getGridParam", "selarrrow");
    var len = selectedIds.length;
    if (len == 0) {
        alert("请选择打包下载的文件!");
    }
    else {
        $("#showdialog").dialog('open');
    }    
}
*/
$(function(){
    $('#dialog').dialog({
        width: 600,        
        autoOpen: false,//打开模式窗体
        resizable: false,
        draggable: false,//是否可以拖动模式窗体
        modal: true,//启用模式窗体
        closeOnEscape: true,
        //bgiframe: true,
        close: function(){
            $("#preimge").attr("src", "../images/circle_animation.gif");
        }
    });
  /*  $('#showdialog').dialog({
    	width: 300,   
    	height: 180,
        autoOpen: false,//打开模式窗体
        resizable: false,
        draggable: false,//是否可以拖动模式窗体
        modal: true,//启用模式窗体
        closeOnEscape: true,
        bgiframe: false,
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
                var url = "getPackAgeDownLoad.action?eachCheckbox=" + stringsplit + "&filename=" + tdocname;
                $("#packAgeDownLoad").attr("src", url);
                $("#showdialog").dialog('close');                
            }
        },
        close: function(){
            $("#tdocname").attr("val", "");
        }     
    });  */
    
    // Initialize the tree inside the <div>element.
    // The tree structure is read from the contained <ul> tag.
    $("#tree").dynatree({
        title: "Programming Sample",
        onLazyRead: function(node){
            //alert("a"+node.data.key);
            var id = node.data.key.id;
            var title = node.data.title;
            var options = getOptions(node, "");            
            $.getJSON("getNode.action", {
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
             tempstring = getOptions(node, tempstring);
             var array = tempstring.split(";");
             var projectId = "";
             for ( var i = 0; i < array.length; i++) {
 				var project =array[i].split("=");
 				for ( var j = 0; j < project.length; j++) {
 					if(project[j]=="project"){
 						projectId="project";
 					}
 				}
 			}
             if (node.data.isFolder == false ) {
           
             	 var newUrl = tempurl + "&options=" + tempstring; 
                  $("#rowedtable").clearGridData();

                  $("#rowedtable").setGridParam({
                      url: newUrl
                  }).trigger("reloadGrid");
             	if(projectId=="project"){           		
             		 $("#button9").attr("disabled", false);
                      $("#button9").click(function(){              
                          window.navigate("../archives/initTreeUpLoad.action?options=" + tempstring);
                      });
             	}
             }
             else {            
                 $("#button9").attr("disabled", true);
             }
         }
    });
    

    $.getJSON("getNode.action", {
        nodeId: id,
        sign: true 
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
