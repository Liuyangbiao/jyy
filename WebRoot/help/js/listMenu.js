
function listMenu(listname, titles, files, isSearch) 
{
	var listElem = document.getElementById(listname);
	if ( null == listElem )
	{
		return;
	}
	this.listframe = listElem;
	this.titles    = titles;
	this.files     = files;
	this.isSearch  = isSearch;
	this.arrinfo   = new Array();
}

listMenu.prototype.addItem = function (titleIndex)
{
	var i= 0;
	for ( i= 0; i<this.arrinfo.length; i++)
	{
		if ( titleIndex == this.arrinfo[i] )
		{
			return -1;
		}
	}
	this.arrinfo[i] = titleIndex;

   	var id = '';
   	var name = this.titles[ titleIndex ];;
   	var opt = new Option(name, id);
   	opt.value = titleIndex;
   	this.listframe.options.add(opt);
   	opt.selected = false;	
   	
   	return titleIndex;

	
}

listMenu.prototype.removeItem = function ( )
{
    if (this.listframe.selectedIndex < 0) return -1;
    
    var selindex = this.listframe.selectedIndex;
    
    var titleIndex = this.listframe[selindex].value;
    
	this.listframe.remove(this.listframe.selectedIndex); 	
 
    this.arrinfo.splice(selindex,1);
    
    return titleIndex;
    
}

listMenu.prototype.enterpress = function ()
{
	if (window.event.keyCode == 13)
	{
		 this.showHref();
	}
     
}

listMenu.prototype.gotokey = function (keyword)
{
	if (window.event.keyCode == 13)
	{
		 this.showHref();
		 return;
	}
    if (keyword.length == 0) return;
    keyword = keyword.toLowerCase();
    for (var i= 0; i<this.arrinfo.length; i++)
    {
      var title = this.titles[this.arrinfo[i]].toLowerCase();
      if (title.indexOf(keyword) == 0)
      {
        this.listframe[i].selected = true;
        return;
      } 
    }
    return;
	
}

listMenu.prototype.showArrItem = function (indexArr)
{
	this.listframe.length=0;
	this.arrinfo.length=0;
	
	for( i in indexArr )
	{

     	var id = '';
     	var name = this.titles[ indexArr[i] ];;
     	var opt = new Option(name, id);
     	opt.value = indexArr[i];
     	this.listframe.options.add(opt);
     	opt.selected = false;	
     	this.arrinfo[i] = indexArr[i];
	}
	
	return;
}

listMenu.prototype.showHref = function ()
{
    if (this.listframe.selectedIndex < 0) return;
    var item = this.listframe[this.listframe.selectedIndex];
    
    var fileindex = item.value;

    window.top.isSearch = this.isSearch;
    
    showHelpfileOnFrame( fileindex, this.files );
}  


////////////bookmark
function bookmarkMenu(listMenu, appFlag) 
{
	this.listMenu = listMenu;
	this.appFlag  = 'cookie'+appFlag;
}

bookmarkMenu.prototype.addBookmark  = function ()
{
	if ( !window.top )
	{
		return;
	}
	var curtitle = window.top.curtitleindex;
	if ( null == curtitle )
	{
		return;
	}
	
	if ( -1 != this.listMenu.addItem( curtitle ) )
	{
		var bookmarkindex = GetCookie(this.appFlag);
		if ( null == bookmarkindex )
		{
			bookmarkindex = curtitle;
		}
		else
		{
			bookmarkindex = bookmarkindex+'-'+ curtitle;
		}
		
		SetCookie(this.appFlag, bookmarkindex,1000);
	}
}

bookmarkMenu.prototype.delBookmark  = function ()
{
	var delIndex = this.listMenu.removeItem( );
	if ( -1 == delIndex )  
	{
		return;
	}

	var bookmarkindex = GetCookie(this.appFlag);
	if ( null == bookmarkindex )
	{
		return;
	}
	var bookmarkArr = bookmarkindex.split("-");

	for ( var i = 0; i< bookmarkArr.length; i++ )
	{
		if( bookmarkArr[i] == delIndex)
		{
			bookmarkArr.splice(i,1);
		}
	}
	
	bookmarkindex = bookmarkArr.join("-");
	SetCookie(this.appFlag, bookmarkindex,1000);
	return;	
}


bookmarkMenu.prototype.initBookmark  = function ()
{
	var bookmarkindex = GetCookie(this.appFlag);
	if ( null == bookmarkindex )
	{
		return;
	}
	var bookmarkArr = bookmarkindex.split("-");
	this.listMenu.showArrItem(bookmarkArr);
	
	bookmarkindex = bookmarkArr.join("-");
	SetCookie(this.appFlag, bookmarkindex, 1000);
	
	return;	
}



function SetCookie(name,value,expires,path,domain,secure)
{
var expDays = expires*24*60*60*1000;
var expDate = new Date();
expDate.setTime(expDate.getTime()+expDays);
var expString = ((expires==null) ? "" : (";expires="+expDate.toGMTString()))
var pathString = ((path==null) ? "" : (";path="+path))
var domainString = ((domain==null) ? "" : (";domain="+domain))
var secureString = ((secure==true) ? ";secure" : "" )
document.cookie = name + "=" + escape(value) + expString + pathString + domainString + secureString;
} 

function GetCookie(name)
{
var result = null;
var myCookie = document.cookie + ";";
var searchName = name + "=";
var startOfCookie = myCookie.indexOf(searchName);
var endOfCookie;
if (startOfCookie != -1)
{
startOfCookie += searchName.length;
endOfCookie = myCookie.indexOf(";",startOfCookie);
result = unescape(myCookie.substring(startOfCookie, endOfCookie));
}
return result;
} 