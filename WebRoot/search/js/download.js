
	function packAgeDownLoad(){
		//组织下载参数
		var stringsplit="";
		$("input[name='checkDownloadVals']").each(function(){ 
			if(this.checked){
				stringsplit += ":" + this.value;
			}
		});
		if(stringsplit!=""){				
			$('#services').dialog("open");
			$("#showDownListId").empty();
			var url = "getDownLoadAddress.action?time="+(new Date()).valueOf();
            var params = {   
                eachCheckboxVals:stringsplit,  
                pa:"test"   
               };  
            jQuery.post(url,params,callbackFun,'json'); 
		}else{
			alert("请先选择需打包下载的文件！");
		}
	}

	var isLocal;
	var serverName;
	var trStr ="";

	//下载地址回调函数
	function callbackFun(data)   
	{   
		$("#displayMessage").html(data.loginLocationInfo[1]);

		//用户在外地登录
		if(data.loginLocationInfo[2] == 'N'){
			isLocal = true;
			serverName = data.loginLocationInfo[3];
			var buttons= [{
		            text:serverName+"下载",
		            click:function(){
		        	    $("#downType").val(data.loginLocationInfo[0]);
						$(this).dialog("close");
						$("#showdialog").dialog("open");
		   	      }},
			   	     {text:"本地下载", 
		             click:function(){
						$("#downType").attr("value","isLocal"); 
					    $(this).dialog("close");
					    $("#showdialog").dialog("open");
		             }},
		          {
		             text:"取消",
		             click:function(){
		     	     $(this).dialog("close");
		          }
		     }];
			downLoadDialog();
			$("#services").dialog("option", "buttons", buttons);
			$("#services").dialog('open');
		} else{//本地登录
			downLoadDialog(serverName);
			$("#services").dialog('open');
		}
		trStr ="";
		$("#showDownListId").empty();
		
		if(data.allDocsObj.length > 0){
			for(var index=0; index<data.allDocsObj.length; index++){
				eachDocfun(data.allDocsObj[index],index);		
			}
		}
		var tableStr = "";
		var tableStart = "" + "<table style='font-size:12px;' >";
		var tableEnd   = "</table>" + "";	
		tableStr = tableStart + trStr + tableEnd;
		$("#showDownListId").append(tableStr);   
	}

	//下载地址
	function downLoadDialog(downNameIP){
		 $('#services').dialog({
                width: 600,
                height: 340,
                autoOpen: false,//打开模式窗体
                resizable: false,//是否可以调整模式窗体的大小
                draggable: false,//是否可以拖动模式窗体
                modal: true,//启用模式窗体
                closeOnEscape: true,//按下esc可以退出
                bgiframe: true,
                buttons:[{
		                text:"本地下载", 
		                click:function(){
    		     	      $("#downType").attr("value","isLocal");  
					      $(this).dialog("close");
					      $("#showdialog").dialog("open");
		                }
		               },
		          {
    		            text:"取消",
    		            click:function(){
    		     	    $(this).dialog("close");
		           }
		        }]                    
            });
         }
	function eachDocfun(downInfo,index){
		var downAddr = downInfo.downAddress;
		var trStart  = "<tr height='20px'>";
		var trEnd    = "</tr>";
		var	tdStr = "<td>&nbsp;</td>";
		var iii = 0;
		for ( var i = 0; i < downInfo.downAddress.length; i++ ) { //一个文件的每个下载地址
             var code = downInfo.downAddress[i][0]; // 省公司代码
             if(code != undefined && code != null && code != "" ){
	              var name = downInfo.downAddress[i][1]; // 省公司名字
	              var firstTd = "";
	              if(i==0){ //默认选择第一个下载地址   
	              	firstTd =  "<td width='180px'>"+downInfo.name+"."+downInfo.suffix+"</td><td>&nbsp;<input type='hidden' name='checkVal' value='checkVal'/></td>"+
					  		   "<td>"+ 
					  		   "<input type='radio' checked='checked' name='downFileList["+index+"].code' value='"+code+"'/>"+
					  		   "<input type='hidden' name='downFileList["+index+"].name' value='"+downInfo.name+"'/>"+
					  		   "<input type='hidden' name='downFileList["+index+"].path' value='"+downInfo.path+"'/>"+
					  		   "<input type='hidden' name='downFileList["+index+"].docid' value='"+downInfo.docid+"'/>"+
					  		   "<input type='hidden' name='downFileList["+index+"].suffix' value='"+downInfo.suffix+"'/>"+
					  		   "</td>"+
					  		   "<td width='100px'>"+name+"</td><td>&nbsp;</td>";
					 tdStr = firstTd; 
					 firstTd = "";
					 continue;  
	              }
				  tdStr += (
				  		    "<td>"+ 
				  		    "<input type='radio' name='downFileList["+index+"].code' value='"+code+"'/>"+  
				  		    "</td>"+
					  		"<td width='100px'>"+name+"</td><td>&nbsp;</td>"
				  		    );
			 }
        }
        trStr += (trStart + tdStr + trEnd);
	}
	
	$(function(){        
	    $("#onLineSeeDIV").hide();   
		$('#showdialog').dialog({
            width: 400,
            height: 180,
            autoOpen: false,//打开模式窗体
            resizable: false,//是否可以调整模式窗体的大小
            draggable: false,//是否可以拖动模式窗体
            modal: true,//启用模式窗体
            closeOnEscape: true,//按下esc可以退出
            bgiframe: true,
            buttons: {
                     "下载": function(){
                     var tdocname = $("#tdocname").val();
					 $('#filename').attr("value",tdocname);
					 $('#dowloadFormId').submit();
					 $(this).dialog("close");
                   },
                  "取消": function(){
                	  $(this).dialog("close");
                  }
                },
            close: function(){
            	$("#tdocname").attr("value","");
            }
        });
	})
	
	function divShow(){
    	 $("#onLineSeeDIV").hide(); 
		 $("#mainDIV").show();
		 document.ols.location.href="about:blank";
     }

	function onLineSeeOrDown(action,target,docid,projectid,companyid,
			                              filename,path,suffix,isOnLine){
		$("#onLineSeeForm").attr("action",action);
		$("#onLineSeeForm").attr("target",target);
		$("#docid").attr("value",docid);
		$("#projectId").attr("value",projectid);
		$("#code").attr("value",companyid);
		$("#name").attr("value",filename);
		$("#path").attr("value",path);
		$("#suffix").attr("value",suffix);
		$("#isonline").attr("value",isOnLine);
		$("#onLineSeeForm").submit();
      }

	
	//点击在线查看时触发的函数
    function onlineDown(id){
    	var value = $("#"+id).val();
		var allValue = value.split("<>");
		
		var docid = allValue[0];
		var projectid = allValue[1];
		var companyid = allValue[2];
		var name = allValue[3];
		var path = encodeURI(allValue[4]);
		var suffix = allValue[5];
		
		var action = "onLineSee.jsp";
		var target = "ols";
		var isOnLine = "false";
		
		onLineSeeOrDown(action,target,docid,projectid,companyid,
                name,path,suffix,isOnLine)
		
   		$("#onLineSeeDIV").show(); 
   		$("#mainDIV").hide();
    }

    //本地下载
    function download(id){  
    	var value = $("#"+id).val();
		var allValue = value.split("<>");

		var docid = allValue[0];
		var projectid = allValue[1];
		var companyid = allValue[2];
		var name = allValue[3];
		var path = allValue[4];
		var suffix = allValue[5];
		
		var action = "getOnLineSee.action";
		var target = "downLoadFrame";
		var isOnLine = "true";
		onLineSeeOrDown(action,target,docid,projectid,companyid,
                name,path,suffix,isOnLine)
    }