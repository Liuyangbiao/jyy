function searchMenu( list,searchelem,skArr,sfi ) 
{
	this.listMenu = list;
	this.elem  = searchelem;
	this.skArr = skArr;
	this.sfi   = sfi;  
}

searchMenu.prototype.enterpress = function ()
{
	if (window.event.keyCode == 13)
	{
		 this.searchword();
	}
}


searchMenu.prototype.searchword  = function ()
{
	var keyword = this.elem.value.toLowerCase();
	
	var wordArr = this.parseword( keyword );
	if ( 0 == wordArr.length )
	{
		alert('请输入要查找的关键字');
		return;
	}
	
	var op = 0;
	
	var fileIndex = new Array();

	for ( var i in wordArr )
	{
		var fIndex = this.findKeyword( wordArr[i] );
		if ( 0 == i )
		{
			fileIndex = fIndex;
			continue;
		}
		fileIndex = this.merge(fileIndex, fIndex, op );
	}
	window.top.searchWord = wordArr;
	
	this.listMenu.showArrItem( fileIndex );
	
}

searchMenu.prototype.findKeyword  = function ( word )
{
	var sfiIndex = null;

	sfiIndex = this.findWord( word )
	if ( null != sfiIndex )
	{
		return sfiIndex ;
	}
	
	var result = new Array();
	if ( word.charCodeAt(0) < 128 )
	{
		return result;
	}
	var chr = null;
	var charf = null;
	for( var i = 0; i < word.length; i++ )
	{
		chr = word.substring(i,i+1);
		charf = this.findWord( chr );
		if ( null == charf )
		{
			charf = new Array();
		}
		if ( 0 == i )
		{
			result = charf;
			continue;
		}

		result = this.merge( result,charf, 0 );
	}

    return result; 
}


// //0:AND,1:OR
searchMenu.prototype.merge = function ( arr1, arr2, op )
{
    var ret = new Array();
    if ( 0 == arr1.length )
    {
    	if ( 0 == op )
    	{
    		return ret;
    	}
    	else
    	{
    		return arr2;
    	}
    }
    
    for ( var i in arr1) 
	{
		if ( 1 == op )
		{
			ret[ret.length] = arr1[i]; 
		}
        for (j in arr2) 
		{
			if ( 0 == op )
			{
	            if (arr2[j] === arr1[i]) 
				{
					ret[ret.length] = arr2[j]; 
	            }
			}
			if ( 1 == op )
			{
	            if (arr2[j] != arr1[i]) 
				{
					ret[ret.length] = arr2[j]; 
	            }
			}
        }
    }
    return ret;
}

searchMenu.prototype.findchar  = function ( findchar )
{
	var result = new Array();
	for ( var i in this.skArr )
	{
		if (-1 == this.skArr[i].indexOf( findchar ) )
		{
			continue;
		}
		result[ result.length ] = this.sfi[i];
	}
	
	return result;
	
}

searchMenu.prototype.findWord  = function ( word )
{
	var skArr;
	var sfi;

	wordLen = word.length;
	if ( 1 == wordLen )
	{
		skArr = this.skArr[1];
		sfi   = this.sfi[1];
	}
	else if ( 2 == wordLen )
	{
		skArr = this.skArr[2];
		sfi   = this.sfi[2];
	}
	else if ( 3 == wordLen )
	{
		skArr = this.skArr[3];
		sfi   = this.sfi[3];
	}
	else if ( 4 == wordLen )
	{
		skArr = this.skArr[4];
		sfi   = this.sfi[4];
	}
	else 
	{
		skArr = this.skArr[5];
		sfi   = this.sfi[5];
	}
	
	return this.findWholeWord( word, skArr, sfi );
}

searchMenu.prototype.findWholeWord  = function ( word, skArr, sfi )
{
    var begin = 0;
    var end   = skArr.length - 1;
    var i = 0;
    
    while( begin <= end  )
    {
    	i = Math.floor( (begin+end)/2 );
    	if ( word == skArr[i] )
    	{
    		return sfi[i];
    		break;
    	}
    	if ( word >= skArr[i] )
    	{
    		begin = i + 1;
    	}
    	else 
    	{
    		end = i - 1;
    	}
    }
    return null; 
}


searchMenu.prototype.parseword  = function ( keyword )
{
	var ret = new Array();
	
	keyword = trim(keyword);
	
	keyword = keyword.split(' ');
	for ( var i in keyword )
	{
		var word = trim(keyword[i]);
		var wordlen = word.length;
		if ( 0 == wordlen )
		{
			continue;
		}

		var preStatus = 0;
		var prePosit  = 0;
		var j = 0;
		for ( j = 0; j < wordlen; j++  )
		{
			var chr = word.substring(j, j+1);
			var chrCode = word.charCodeAt(j);
			var curStatus = this.getCodeStatus(chrCode);
			if ( 0 == j )
			{
				preStatus = curStatus;
			}
			if ( curStatus == preStatus )
			{
				continue;
			}

			if ( j > prePosit )
			{
				ret[ret.length] = word.substring(prePosit, j);
				prePosit = j;
			}
			preStatus = curStatus;
		}
		

		if ( j > prePosit )
		{
			ret[ret.length] = word.substring(prePosit, j);
		}
	}
	
	return ret;

}

searchMenu.prototype.getCodeStatus  = function ( charCode )
{
	if ( charCode > 128 )
	{
		return 0; 
	}
	else if ( 48 <= charCode && 57 >= charCode  )
	{
		return 1; 
	}
	else
	{
		return 2;
	}
}

function trim (str, charlist) {
    var whitespace, l = 0, i = 0;
    str += '';
    
    if (!charlist) {
        whitespace = " \n\r\t\f\x0b\xa0\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u200b\u2028\u2029\u3000";
    } else {
        charlist += '';
        whitespace = charlist.replace(/([\[\]\(\)\.\?\/\*\{\}\+\$\^\:])/g, '\$1');
    }
    
    l = str.length;
    for (i = 0; i < l; i++) {
        if (whitespace.indexOf(str.charAt(i)) === -1) {
            str = str.substring(i);
            break;
        }
    }
    
    l = str.length;
    for (i = l - 1; i >= 0; i--) {
        if (whitespace.indexOf(str.charAt(i)) === -1) {
            str = str.substring(0, i + 1);
            break;
        }
    }
    
    return whitespace.indexOf(str.charAt(0)) === -1 ? str : '';
}
