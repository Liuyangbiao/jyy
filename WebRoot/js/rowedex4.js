jQuery("#rowed4").jqGrid({
	//路径
   	url:'server.php?q=2',
	//传入的类型
	datatype: "json",
	//表格的列名
   	colNames:['Inv No','Date', 'Client', 'Amount','Tax','Total','Notes'],
	//列的名称   索引  列宽  对齐方式  是否可以编辑  
   	colModel:[
   		{name:'id',index:'id', width:55},
   		{name:'invdate',index:'invdate', width:90, editable:true},
   		{name:'name',index:'name', width:100,editable:true},
   		{name:'amount',index:'amount', width:80, align:"right",editable:true},
   		{name:'tax',index:'tax', width:80, align:"right",editable:true},		
   		{name:'total',index:'total', width:80,align:"right",editable:true},		
   		{name:'note',index:'note', width:150, sortable:false,editable:true}		
   	],
	//一页显示多少行
   	rowNum:10,
	//
	rowList:[10,20,30],
	//页码
   	pager: '#prowed4',
	//根据什么排序
   	sortname: 'id',
    viewrecords: true,
	//默认顺序
    sortorder: "desc",
	//编辑的文件类型
	editurl: "server.php",
	caption: "Full control"
});
jQuery("#ed4").click( function() {
	jQuery("#rowed4").jqGrid('editRow',"13");
	//更改当前表格，为不可能编辑
	this.disabled = 'true';
	jQuery("#sved4").attr("disabled",false);
});
jQuery("#sved4").click( function() {
	jQuery("#rowed4").jqGrid('saveRow',"13", checksave);
	//更改当前表格的样式,为不可保存
	jQuery("#sved4").attr("disabled",true);
	jQuery("#ed4").attr("disabled",false);
});
//保存的方法
function checksave(result) {
	if (result.responseText=="") {alert("Update is missing!"); return false;}
	return true;
}