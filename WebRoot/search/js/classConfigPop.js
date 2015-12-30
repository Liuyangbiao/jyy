		  $(function(){  
			  $("#myForm").validate({
				   rules: {
				       treeName: {
							       required: true,
							       maxlength: 20
							       },
					   remark:{maxlength: 255},
				       menuId: "required"
				   },
				   messages: {
					   treeName: {
					            required: "树名称不能为空!",
						        maxlength:"树名称必须是10个汉字之内!"
						 	  },
					   remark:{maxlength:"备注必须在250个字符之内!"},
					   menuId:   "请先选择树节点!"
				   }
		       })
		  });

		  //判断名称是否重复
		  function nameExistence(name){
			  if(name!=""){
				  // 如果是更新不是新建  则首先不对名称进行检测  否则检查
				  if(f==""){
					  $.getJSON('existenceClassLevelConfig.action?time='+Math.random(),{ id:name}, function(json) {
		                     if(json.length>0){
		                    	 $("#existence").text(json[0].message);
		                     }else{
		                    	 $("#existence").text("");
		                     }
			       	});
				  }else{
					  // 虽然是更新,但是如果修改名称,名称还是不能重复的
	                    if(name!=tName){
	                    	 $.getJSON('existenceClassLevelConfig.action?time='+Math.random(),{ id:name}, function(json) {
	    	                     if(json.length>0){
	    	                    	 $("#existence").text(json[0].message);
	    	                     }else{
	    	                    	 $("#existence").text("");
	    	                     }
	    		       		});
	                    }else{
	                    	 $("#existence").text("");
	                    }
				  }
			  }
		  }
		 
         $(document).ready(function() {  
        	 $.getJSON('displayNodeDefClassLevelConfig.action?time='+Math.random(),{ id:pid }, function(json) {
  				menu(json,"listLeft");
  	        	}
  	        );
    	        //如果是更新才执行以下代码
    	        if(pid!=""){
    	        	$.getJSON('displayTreeDefClassLevelConfig.action?time='+Math.random(),{ id:pid }, function(json) {
    	  				menu(json,"listRight");
    	  	        	});
    	          }
     			
     			function menu(json,flag){
    				var s_root=document.getElementById(flag);   
    				s_root.options.length=0;  
    				var option = document.createElement("option"); 
    					for(var i in json){   
    				        var value=json[i].code;
    				        var text=json[i].name;  
    				        if(text==undefined){
    				        continue;
    				        } 
    				        var option = document.createElement("option"); 
    				            option.text=text;   
    				            option.value=value;   
    				            s_root.options[s_root.options.length] =option;
    				    }
     			}

     			
             //right move  
             $("#btnRight").click(function() {  
                 moveright();  
             });  
             //double click to move left  
             $("#listLeft").dblclick(function() {  
                 moveright();  
             });  
     
             //left move   
             $("#btnLeft").click(function() {  
                 moveleft();  
             });  
     
             //double click to move right  
             $("#listRight").dblclick(function() {  
                 moveleft();  
             });  

             //up move   
             $("#btnUp").click(function() {  
                 moveup();  
             }); 
             //down move   
             $("#btnDown").click(function() {  
                 movedown();  
             });  

             $("#upRight").click(function() {  
            	 upRight();  
             }); 
             $("#downRight").click(function() {  
            	 downRight();  
             }); 
             var s="      ";
             var m=0;
             function space(n){
            	var s="";
                for(var i=0;i<n;i++){
                    s=s+"      ";
                }
                return s;
             }
              // 右
             function objRight(obj){
            	 obj.attr("text",s+obj.text());
            	 var o=$(obj).next();
            	 if(o.text()==""){
                    return;
            	 }
            	 return objRight(o);
             }
             //左
             function objRight2(obj){
                 if(obj.text().indexOf("*")>6){
                	 obj.attr("text",obj.text().substr(6));
                 }
            	 var o=$(obj).next();
            	 if(o.text()==""){
                    return;
            	 }
            	 return objRight2(o);
             }
             //往右移
             function  upRight(){
                 var vSelected = $("#listRight option:selected");
                 var pre=$(vSelected).prev().text();
                 var next=$(vSelected).next().text();
            	 var t1=pre.indexOf("*");
            	 var t2=vSelected.text().indexOf("*");
            	 var t3=next.indexOf("*");
		          if(t1==t2 || t2<t1){
		        	  vSelected.attr("text",s+vSelected.text());
		        	  if(t2!=t3){
		        		  objRight($(vSelected).next());
		        	  }
		          }
             }
             //往左移
             function  downRight(){
            	 var vSelected = $("#listRight option:selected");
            	 var pre=$(vSelected).prev().text();
            	 var t1=pre.indexOf("*");
            	 var t2=vSelected.text().indexOf("*");
            	 if(t2-t1==6 || t2==t1 ||t1-t2>=6){
            		 objRight2(vSelected);
            	 }
             }
             
             function moveright() {  
                 //数据option选中的数据集合赋值给变量vSelect  
                 var vSelect = $("#listLeft option:selected");  
                 var value=vSelect.text();
                 if(m==0 && tName==""){
                	vSelect.attr("text","*"+value);
                    m=m+1;
                 }else{
                 	vSelect.attr("text",s+"*"+value);
                 }
                 vSelect.clone().appendTo("#listRight");  
                 vSelect.attr("text",value);
             //    vSelect.remove();  
             }  
             function moveleft() {  
                 var vSelect = $("#listRight option:selected");
                 var value=vSelect.text();
                 var nextObj=$(vSelect).next();
                 moveleftSpace(value,nextObj);
                 
                 if(value.indexOf("*")>3){  //树的根节点是不容许移走的
              //  	 vSelect.attr("text",value.substr(value.indexOf("*")+1)); 
              //     vSelect.clone().appendTo("#listLeft");  
                     vSelect.remove();  
                  }
              }  

             function moveleftSpace(value,nextObj){
                 var num1 = value.indexOf("*")
                 var num2=nextObj.text().indexOf("*");
                 var value2=nextObj.text();
                 if(num2-num1==6){
                	 nextObj.attr("text",value2.substr(6));
                	 moveleftSpace(value2,nextObj.next());
                 }else{
                     return;
                 }
             }
             //往上
			 function moveup(){
				var obj = $("#listRight option");
				var vSelected = $("#listRight option:selected");
				if($(obj[0]).val()!=vSelected.val()){  //先判断不是第一个
					var pre=$(vSelected).prev().text().indexOf("*"); 
					var cur=vSelected.text().indexOf("*");
					if(pre-cur<0){
						$(vSelected).prev().attr("text",s+$(vSelected).prev().text());
						vSelected.attr("text",vSelected.text().substr(6));
					}else if(pre-cur>0){
						$(vSelected).prev().attr("text",$(vSelected).prev().text().substr(6));
						vSelected.attr("text",s+vSelected.text());
					}
					$(vSelected).prev().insertAfter($(vSelected));
				}
			 }
			 //往下
			 function movedown(){
				var obj = $("#listRight option");
			 	var vSelected = $("#listRight option:selected");
			 	if($(obj[obj.length-1]).val()!=vSelected.val()){
			 		var s1=$(vSelected).next().text().indexOf("*");
                    var s2=vSelected.text().indexOf("*");
				 	if(s1>s2){
				 		vSelected.attr("text",s+vSelected.text());
				 		$(vSelected).next().attr("text",$(vSelected).next().text().substr(6));
				 	}else if(s1<s2){
				 		vSelected.attr("text",vSelected.text().substr(6));
				 		$(vSelected).next().attr("text",s+$(vSelected).next().text());
				 	}
				 	$(vSelected).next().insertBefore($(vSelected));
			 	}
			 }
         });  

	     $(document).ready(function() { 
	    	 $.ajaxSetup({ cache:false });
	    	    var options = { 
	    	        dataType:  'json',
	    	        beforeSubmit: function(){
	    	    	 var v=$("#existence").text();
	    	    		if(v==""){
	    	    			return true;
	    	    		}
	    	    		return false;
	                 },
	    	        type: 'post',
	    	        url: "saveClassLevelConfig.action?time="+Math.random(),
	    	        success:   processJson 
	    	    	 };
	    	    	 $('#myForm').ajaxForm(options); 
	    	});
	     function processJson(data) { 
	    	    alert("保存成功!"); 
	    	    //给父窗口返回参数,如果接到参数为1,则刷新父窗口
	    	    window.returnValue="1";
	    	    closeWin();
	    	}

	     //图片预览
	     function imgShow(){
	    		   var vSelect = $("#imageUrl option:selected");  
	               $("#imgId").attr("src","../"+vSelect.val());
	    	}

	     function sub(){
           var obj = document.getElementsByTagName("select")[2]  ;
           var selectOptions = obj.options;  
           var len=selectOptions.length;
           var menuId="";
           for(var i=0;i<len;i++){
                  var txt=selectOptions[i].text;
                  var val=selectOptions[i].value;
                  var num=parseInt((txt.indexOf("*"))/6);
                      val=num+val;
                  menuId=menuId+val+",";
           }
           $("#menuId").attr("value",menuId);
           return true;
         }

         function closeWin(){
        	 var ct=document.getElementById("remark");      
             ct.focus();
        	 window.close();
         }
