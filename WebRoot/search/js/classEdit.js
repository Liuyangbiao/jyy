		  $(function(){  
			  $("#myForm").validate({
				   rules: {
				  	    typeName: {
								       required: true,
								       maxlength: 20
								       },
				  	    source : {
								       required: true,
								       maxlength: 254
									       },
						remark:{maxlength: 255},
		                params :{maxlength: 50}
				   },
				   messages: {
					   typeName: {
				            required: "分类名称不能为空!",
					        maxlength:"分类名称必须是10个汉字之内!"
					 	  },
					    source : {
					            required: "数据源不能为空!",
						        maxlength:"数据源必须是254个字符之内!"
						 	  },
						remark:{maxlength:"备注必须在250个字符之内!"},
						params:{maxlength:"参数代码必须在50个字符之内!"}
				   }
		       })
		  });

		  //判断名称是否重复
		  function nameExistence(name){
			  if(name!=""){
				  // 如果是更新不是新建  则首先不对名称进行检测  否则检查
				  if(f==""){
					  $.getJSON('existenceClassConfig.action?time='+Math.random(),{ id:name}, function(json) {
		                     if(json.length>0){
		                    	 $("#existence").text(json[0].message);
		                     }else{
		                    	 $("#existence").text("");
		                     }
			       	});
				  }else{
					  // 虽然是更新,但是如果修改名称,名称还是不能重复的
	                    if(name!=tName){
	                    	 $.getJSON('existenceClassConfig.action?time='+Math.random(),{ id:name}, function(json) {
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
		  

   $(function(){
        	function initAutoComplete(json,valId)
	        	{
	        		$(valId).autocomplete(json, {
	        		    max: 10, //列表里的条目数
	        		    minChars: 0, //自动完成激活之前填入的最小字符,为0双击弹出列表
	        		    matchCase:false,//不区分大小写
	        		    width: 155, //提示的宽度，溢出隐藏
	        		    scrollHeight: 300, //提示的高度，溢出显示滚动条
	        		    matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
	        		    autoFill: false, //自动填充 ，就是在文本框中自动填充符合条件的项目
	        		    mustMatch: true, //自动匹配，mustMatch表示必须匹配条目，也就是在文本框里输入的内容，必须是data参数里的数据，如果不匹配，文本框就被清空
	        		    formatItem: function(row, i, max, term) {
	        			var v = $(valId).val(); 
	        			
	        			return  row.name;
	        		      //  return i + '/' + max + ':' + "<I >" + row.name +"</I>"+ "<span style='display:none;'>"+ '"[' + row.id + ']' +"</span>";
	        		        //formatItem作用在于可以格式化列表中的条目，比如我们加了“I”，让列表里的字显示出了斜体
	        		        if(row.name.indexOf(v) == 0 || row.spell.indexOf(v)==0)
	        				{
	        					return  row.name + " (" + row.spell + ")";
	        				}
	        				else
	        					return false;
	        	    },
	        	    formatMatch: function(row, i, max) {
	        	  		 //return row.name + row.id;
	        	  		 return  row.name + " (" + row.spell + ")";
	        	        //formatMatch是配合formatItem使用，作用在于，由于使用了formatItem，所以条目中的内容有所改变，而我们要匹配的是原始的数据，所以用formatMatch做一个调整，使之匹配原始数据
	        	    },
	        	    formatResult: function(row) {
	        	    	
	            		 return row.name;
	        	        //formatResult是定义最终返回的数据，比如我们还是要返回原始数据，而不是formatItem过的数据
	        	    }
	        	    }).result(function(event, row, formatted) {
	        	    	    
	        	    	     if(row!=undefined || row!=null){
	        	    	    	 $("#dictionaryCode").attr("value", row.code);
	        	    	     }else{
	        	    	    	 $("#dictionaryCode").attr("value", "");
	        	    	     }
	        	    });
	            }

        	$.getJSON('queryDictionaryClassConfig.action?time='+Math.random(),{ id:''}, function(json) {
	        	  initAutoComplete(json,"#dictionaryName");
	        	}
	        );
        });
        
		function showUI(){
			 var source=$("#source").attr("value");
			 var reg=/:\w+/gi;
			 if(""==source){
                   alert("请选择或输入数据源!");
			 }else{
				 var str=source.match(reg);
				 if(str!=null){
					 document.getElementById("fset").innerHTML="";
					 for(var i=0;i<str.length;i++){
                         $("#dForm fieldset").append( "<label>" +"参数"+(i+1)+": "+"</label>" + "<input type='text' name='"+(i+1)+"' id='"+(i+1)+"' /><br/>" ); 
					 }
					 $( "#dialog-form" ).dialog( "open" );
				 }else{
					 //如果没有":",则直接弹出预览对话框
					 var table = document.getElementById("addRow");
			           var len=table.rows.length;
			           for(var i=len-1;i>0;i--){
			        	   table.deleteRow(i);
			           }
					 $(function() {
						    var flag=true;
							$.getJSON('previewClassConfig.action?time='+Math.random(),{ id:source }, function(json) {
								flag=false;
								if(0==json.length){
	                                alert("查询语句语法有误,或查询数据不存在!");
								}else{
									for(var i in json){   
										var newRow = table.insertRow(table.rows.length);
										var newTd1 = newRow.insertCell(0);newTd1.innerHTML=json[i].code;
										var newTd2 = newRow.insertCell(1);newTd2.innerHTML=json[i].name; 
						 	         }
									$( "#dialog-modal" ).dialog({
										maxHeight:300,
										width:300,
										modal: true
									  });
							    	}
							    });
						//    if(flag){alert("查询语句语法有误!");}
					 });

				 }

				 //参数对话框
				 $( "#dialog-form" ).dialog({
						autoOpen: false,
						height: 300,
						width: 350,
						modal: true,
						buttons: {
							"确定": function() {
								var bValid = true;
                                var len=$("#fset label");
                                //判断是否所有参数都已经填上
                                for(var i=0;i<len.length;i++){
                                	var v=$("#"+(i+1)).val()
                                      if(v==""){
                                    	  bValid=false;
                                      }
                                }
								if ( bValid ) {
									//替换,把params替换成对应的参数值
						             var rgExp=/:\w+/;
									 for(var i=0;i<len.length;i++){
		                                	var v=$("#"+(i+1)).val()
		                                    var f=source.replace(rgExp, v);
		                                    source=f;
		                                }
							//		$( "#source" ).attr("value",source);
									$( this ).dialog( "close" );

                                     //点确定直接弹出  预览对话框
									 var table = document.getElementById("addRow");
							           var len=table.rows.length;
							           for(var i=len-1;i>0;i--){
							        	   table.deleteRow(i);
							           }
									 $(function() {
										    var flag=true;
											$.getJSON('previewClassConfig.action?time='+Math.random(),{ id:source }, function(json) {
												flag=false;
												if(0==json.length){
					                                alert("查询数据不存在!");
												}else{
													for(var i in json){   
														var newRow = table.insertRow(table.rows.length);
														var newTd1 = newRow.insertCell(0);newTd1.innerHTML=json[i].code;
														var newTd2 = newRow.insertCell(1);newTd2.innerHTML=json[i].name; 
										 	         }
													$( "#dialog-modal" ).dialog({
														maxHeight:300,
														width:300,
														modal: true
													  });
											    	}
											    });
										//    if(flag){alert("查询语句语法有误!");}
									 });
								}else{
									alert("请输入参数!");
								}
							},
							"取消": function() {
								$( this ).dialog( "close" );
							}
						},
						close: function() {
						//	allFields.val( "" ).removeClass( "ui-state-error" );
						}
					});
			     
			 }
		}

		function test(){
	      $("#dialog-modal").dialog("close");
		}

		$(document).ready(function() { 
    	    $('#myForm').ajaxForm({ 
    	        dataType:  'json', 
    	        beforeSubmit: function(){
    	    	 var v=$("#existence").text();
    	    		if(v==""){
    	    			return true;
    	    		}
    	    		return false;
                 },
    	        type: 'post',
    	        success:   processJson 
    	    });
    	});
        function processJson(data) { 
    	    alert("保存成功!"); 
    	    //给父窗口返回参数,如果接到参数为1,则刷新父窗口
    	    window.returnValue="1";
    	    closeWin();
    	}
    	//分类定义 ajax Form 提交之前调用此方法
    	function commitBefore(){
           var tName=$("#typeName").val();
           if(tName.length==0){
               alert(tName);
               return false;
           }
           return false;
    	}
        function closeWin(){
        	 var ctrl=document.getElementById("source");      
             ctrl.focus(); 
            //关闭弹出窗口
            window.close();   
        }
        
         function result(){
	           var table = document.getElementById("addRow2");
	           var len=table.rows.length;
	           for(var i=len-1;i>0;i--){
	        	   table.deleteRow(i);
	           }
	           var code=$("#dictionaryCode").attr("value");
	        	 $(function() {
	     			$.getJSON('resultDictionaryClassConfig.action?time='+Math.random(),{ id:code }, function(json) {
				         for(var i in json){   
					        var auth= json[i].auth;
					        if(auth=="" || auth==null){
                                auth="无";
					        }
					        var remark=json[i].remark;
					        if(remark=="" || remark==null){
					        	remark="无";
					        }
							var newRow = table.insertRow(table.rows.length);
							var newTd1 = newRow.insertCell(0);newTd1.innerHTML='<input type="checkbox" name="boxName" value="'+json[i].code+'"/>';  
							var newTd2 = newRow.insertCell(1);newTd2.innerHTML=json[i].code;
							var newTd3 = newRow.insertCell(2);newTd3.innerHTML=json[i].name; 
							var newTd4 = newRow.insertCell(3);newTd4.innerHTML=auth;	
							var newTd5 = newRow.insertCell(4);newTd5.innerHTML=json[i].creator;
							var newTd6 = newRow.insertCell(5);newTd6.innerHTML=json[i].createDate;
							var newTd7 = newRow.insertCell(6);newTd7.innerHTML=remark;
		     	        	}
	     			    });
	        	 });
             }
         
           function topwin(){
        	 //参数对话框
			 $( "#dialog-form-dictionary" ).dialog({
						autoOpen: false,
						height: 520,
						width: 680,
						modal: false,
						buttons: {
							"确定": function() {
								   var sql="select t.code,t.name from t_dictionary  t";
								   var dc= document.getElementById("dictionaryCode").value;
						    	   var r=document.getElementsByName("boxName");
						    	   
						    	   var flag=true;
						    	   var flag2=true;
						    	   for(var i=0;i<r.length;i++){
							    	   if(r[i].checked){
							    		   flag2=false;
								    	   if(flag){
								    		   sql=sql + " where t.code = '"+r[i].value+"'"; 
								    		   flag=false;
								    	   }else{
								    		   sql=sql + " or t.code = '"+r[i].value+"'"; 
								    	   }
							    	   }
						    	   }

						    	   if(flag){
						    		   sql=sql+ " where t.parent_code = '"+ dc +"' ";
						    	   }else{
						    		   sql=sql+ " and t.parent_code = '"+ dc +"' ";
						    	   }
						    	   
						    	   if(r.length==0 && dc==""){
                                       sql="";
					    	 		  }
					    	 		  
						    	   if(r.length==0 && dc!=""){
                                       sql="select t.code,t.name from t_dictionary  t where t.parent_code = '"+ dc +"' ";
					    	  		 }	
						           document.getElementById("source").value=sql;
						           
						    	   $("#dialog-form-dictionary").dialog("close");
							},
							"取消": function() {
								$( this ).dialog( "close" );
							}
						},
						close: function() {
					 
						}
					});

				 $( "#dialog-form-dictionary" ).dialog( "open" );
   			   } 
