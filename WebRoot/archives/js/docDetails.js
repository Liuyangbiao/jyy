//显示模式窗体
function showDocDetails(id){
    $.getJSON("../archives/getProjectDetailsAction.action", {
        id: id
    }, function(json){
        var docDetails = json[0][0];
        var proDetials = json[0][1];
           
        // 文件信息
        var docname = docDetails[0];// 名称
        var docformat = docDetails[1];// 格式
        var suffix = docDetails[7];// 后缀
        var baomi = docDetails[8];// 保密
        var docStatus = docDetails[9];// 状态
        var docCreateDate = docDetails[4];// 创建时间
        var docCreateor = docDetails[6];// 创建者
        var docfileSize = docDetails[2];// 大小
        var docVersion = docDetails[3];// 版本号
        // 给文件信息的DIV添加详细信息
        $("li[id='docName']").text("文件名称：" + docname);
        $("li[id='docFormat']").text("文件格式：" + docformat);
        $("li[id='suffix']").text("文件后缀：" + suffix);
        $("li[id='baomiinfo']").text("文件密级：" + baomi);
        $("li[id='docStatus']").text("文件状态：" + docStatus);
        $("li[id='docCreateDate']").text("上传时间：" + docCreateDate);
        $("li[id='docCreate']").text("上传者：" + docCreateor);
        $("li[id='docFileSize']").text("文件大小：" + docfileSize);        
        // 档案信息
        var zongHeFile = docDetails[10];// 文档类型分类
        var docClass = docDetails[5];// 档案分类
        var sheJiName = docDetails[12];// 设计单位
        var zhuanye = docDetails[11];// 专业
        var docVersion = docDetails[3];// 档案版本
        // 添加档案信息
        $("li[id='zongHeFile']").text("文档类型分类：" + zongHeFile);
        $("li[id='docClass']").text("档案分类：" + docClass);
        // $("#sheJiName").text("设计单位："+sheJiName);
        $("li[id='zhuanye']").text("专业：" + zhuanye);
        $("li[id='docVersion']").text("档案版本：" + docVersion);        
        // 工程信息
        var proCode = proDetials[0];// 工程编码
        var proName = proDetials[1];// 名称
        var proOpenYear = proDetials[3];// 开工年份
        var ownerCode = proDetials[4];// 业主单位
        var wangSheng = proDetials[8];// 网省公司
        var projectType = proDetials[6];// 工程分类
        var projectStatus = proDetials[7];// 工程阶段
        var proVoltagelevel = proDetials[2];// 电压等级
        var designCode = proDetials[9];// 初设单位
        var proParent = proDetials[5];// 所属工程        
        // 添加工程信息
        $("li[id='proCode']").text("工程编码：" + proCode);
        $("li[id='proName']").text("工程名称：" + proName);        
        $("li[id='ownerCode']").text("业主单位：" + ownerCode);
        $("li[id='wangSheng']").text("网省公司：" + wangSheng);
        $("li[id='projectType']").text("工程分类：" + projectType);
        $("li[id='projectStatus']").text("工程阶段：" + projectStatus);
        $("li[id='proVoltagelevel']").text("电压等级：" + proVoltagelevel);
        $("li[id='designCode']").text("初设单位：" + designCode);
        $("li[id='proOpenYear']").text("开工年份：" + proOpenYear);        
        if(proParent!=null&&proParent!=''){
        	$("li[id='proParent']").text("所属工程：" + proParent);
        }else{
        	$("li[id='proParent']").text("");
        }
        if(json[0][2]!=null){
        
        var preDesignDetails = json[0][2];   
      
        //初设档案信息
        var unitName =json[0][2][0]==null?'':trim(json[0][2][0]);
       
        var createDate = json[0][2][1]==null?'':json[0][2][1];
        var ajtm = json[0][2][2]==null?'':json[0][2][2];
        var andh = json[0][2][3]==null?'':json[0][2][3];
        var flbh = json[0][2][4]==null?'':json[0][2][4];
   
        var jcrm = json[0][2][5]==null?'':json[0][2][5];
        var jnyh = json[0][2][6]==null?'':json[0][2][6];
        var ljrm = json[0][2][7]==null?'':json[0][2][7];
        var pzrm = json[0][2][8]==null?'':json[0][2][8];
        var shrm = json[0][2][9]==null?'':json[0][2][9];
        var sjjd = json[0][2][10]==null?'':json[0][2][10];
        var sjrm = json[0][2][11]==null?'':json[0][2][11];
        var tzmc = json[0][2][12]==null?'':json[0][2][12];
        var tzzs = json[0][2][13]==null?'':json[0][2][13];
        var xhrm = json[0][2][14]==null?'':json[0][2][14];
        // 添加初设信息
        //$("#unitName").html("得到:"+unitName);
        $("li[id='unitName']").text("编制单位：" + unitName);
        $("li[id='createDate']").text("编制日期：" + createDate);
        $("#ajtm").text("案卷提名：" + ajtm);
        $("#andh").text("案卷档号：" + andh);
        $("#flbh").text("分类号：" + flbh);
        $("#jcrm").text("检查人：" + jcrm);
        $("#jnyh").text("卷内页号：" + jnyh);
        $("#ljrm").text("立卷人：" + ljrm);
        $("#pzrm").text("批准人：" + pzrm);
        $("#shrm").text("审核人：" + shrm);
        $("#sjjd").text("设计阶段：" + sjjd);
        $("#sjrm").text("设计人：" + sjrm);
        $("#tzmc").text("图纸名称：" + tzmc);
        $("#tzzs").text("图纸张数：" + tzzs);
        $("#xhrm").text("校核人：" + xhrm);
        }
        $("#docInfo").dialog('open');
        
    });    
}

// 档案信息的模式窗体
function docInfoDialog(){
    // 模式窗体
    $('#docInfo').dialog({
        width: 600,
        height: 940,
        autoOpen: false,// 打开模式窗体
        resizable: true,// 是否可以调整模式窗体的大小
        draggable: true,// 是否可以拖动模式窗体
        modal: true,// 启用模式窗体
        closeOnEscape: true,// 按下esc可以退出
        bgiframe: true,
        close: function(){
           $("#docInfo ul li").empty();
        }
    });
}

function trim(str)
{
	return str.replace(/(^\s*)(\s*$)/g,'');
}
