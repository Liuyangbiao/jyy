
//自动补全
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
	    	    	 $(valId+"Code").attr("value", row.code);
	    	     }else{
	    	    	 $(valId+"Code").attr("value", "");
	    	     }
	    });
    }