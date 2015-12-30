        function imgDisplay(mId){
			 if(mId != null){
				 $("#img1").hide(); 
				 $("#img2").hide();
	             if(mId==""){
	                 $("#img2").show();
	             }else{
	            	 $.getJSON('../system/imgDisplayShortcut.action?time='+Math.random(),{ id:mId }, function(json) {
		            		 if(json.length>0){
		            			 $("#img1").show();
		                     }else{
		                    	 $("#img2").show();
		                     }
	 		        	});
	                }
             }else{
             	$("#img1").hide(); 
			 	$("#img2").hide();
             }
        }
			
		function topwin(id){
			var re=window.showModalDialog("../system/popShortcut.jsp?id="+id,"","dialogWidth:500px;dialogHeight:380px;dialogTop:80px;scroll:no;status:no");
			if(re==1){
				 parent.parent.parent.topFrame.location.reload();
		    }
		} 
		
