
function highlightWord(node,word) {
    if (node.hasChildNodes) {
		var hi_cn;
		for (hi_cn=0;hi_cn<node.childNodes.length;hi_cn++) { 
			if ( highlightWord(node.childNodes[hi_cn],word) ) break;
		}
	}
	
	if (node.nodeType == 3) {
		tempNodeVal = node.nodeValue.toLowerCase(); 
		tempWordVal = word.toLowerCase(); 
		if (tempNodeVal.indexOf(tempWordVal) != -1) { 
			pn = node.parentNode; 
			if (pn.className != "searchword") { 
				nv = node.nodeValue; 
				ni = tempNodeVal.indexOf(tempWordVal); 
				before = document.createTextNode(nv.substr(0,ni)); 
				docWordVal = nv.substr(ni,word.length); 
				after = document.createTextNode(nv.substr(ni+word.length))
				hiwordtext = document.createTextNode(docWordVal); 
				hiword = document.createElement("span"); 
				hiword.className = "highlight"; 
				hiword.appendChild(hiwordtext); 
				pn.insertBefore(before,node); 
				pn.insertBefore(hiword,node); 
				pn.insertBefore(after,node); 
				pn.removeChild(node); 
			}
		}
		return true;
	}
	return false;
}

function highlight() 
{
	if ( !window.top.isSearch )
	{
		return;
	}
	if ( !window.top.searchWord )
	{
		return;
	}
	
	for ( var i in window.top.searchWord )
	{
		highlightWord(document.getElementsByTagName("body")[0], window.top.searchWord[i]);
		//highlightKey(window.top.searchWord[i]);
	}
	window.top.isSearch = false;
	return;
}

function getEvent(){
       if(document.all)    return window.event;           
       var func=getEvent.caller;               
       while(func!=null){       
           var arg0=func.arguments[0];   
           if(arg0){   
               if((arg0.constructor==Event || arg0.constructor ==MouseEvent)   
                   || (typeof(arg0)=="object" && arg0.preventDefault && arg0.stopPropagation)){       
                   return arg0;   
               }   
           }   
           func=func.caller;   
       }   
      return null;   
}

function getFrameByName(fname, pflist)
{
	if (!pflist) 
	{
		pflist = window.parent.parent;
	}
	var flist = pflist.frames;
	if ( !flist )
	{
		return null;
	}

	if ( 0 == flist.length )
 	{
 		return null;
 	}
 	
 	for ( var i = 0; i < flist.length; i++ )
 	{
 		if ( !flist[i].name )
 		{
 			continue;
 		}
 		
 		if ( flist[i].name == fname  )
 		{
 			return flist[i];
 		}
 		
 		var rf = getFrameByName(fname, flist[i]);
 		if ( rf ) 
 		{
 			return rf;
 		}
 	}
 	
 	return null;  
}

function setCurTitleIndex( titleIndex)
{
	window.top.curtitleindex = titleIndex;	
}

function GetPageName(url)    
{    
	var tmp= new Array();
	tmp=url.split("/");
	var pp = tmp[tmp.length-1];
	tmp=pp.split("?");
	return tmp[0];    
}  

function localFrame(titleIndex)
{
	if (parent != self)
	{
		if ( 'index.htm' == GetPageName(parent.location.href) )
		{
			return;
		}
	}
	var indexfile = "../index.htm?ti="+titleIndex;
	location.href = indexfile;
}

function GetUrlParms(an)    
{
    var tophref=window.top.location.href;
    var queryArr=tophref.split("?");
    if ( !queryArr[1] )
    {
    	return null;
    }
    var query = queryArr[1];
    var pairs=query.split("&");
    for(var i=0;i<pairs.length;i++)   
    {   
        var pos=pairs[i].indexOf('=');
        if(pos==-1)   continue;
        var argname=pairs[i].substring(0,pos);
        if ( argname != an )
        {
        	continue;
        }
        return pairs[i].substring(pos+1);        
    }
    return null;
}

function contextkey()
{
	var evt = getEvent();
	if ( ! evt.ctrlKey  
	|| !( 37 == evt.keyCode || 38 == evt.keyCode || 39 == evt.keyCode || 40 == evt.keyCode ))
	{
		return true;
	}

	var hf = getFrameByName('helpMenu', window.parent);
	if ( !hf )
	{
		return true;
	}
	if ( !hf.rootNode  )
	{
		return true;
	}
	
	var treemenuNode = hf.rootNode;
	treemenuNode.moveMenu(evt.keyCode, false);
	treemenuNode.moveMenu(13, true);
	
	return false;
	
}

function setContentPage()
{
	var hcf = getFrameByName('helpContent', window.parent.parent);
	if ( !hcf )
	{
		return;
	}
	var titleIndex = 0;
	if ( window.top.curtitleindex )
	{
		titleIndex = window.top.curtitleindex;
	}
	else
	{
		var ti = GetUrlParms('ti');
		if ( null != ti )
		{
			titleIndex = parseInt(ti);
		}
	}
	if ( titleIndex < 0 || titleIndex >= fileArr.length )
	{
		titleIndex = 0;
	}
	
	hcf.location = '../docfile/'+fileArr[titleIndex];
	
	return;
}

function forceTreeShow(treeid)
{
	var hf = getFrameByName('helpMenu', window.parent);
	if ( !hf )
	{
		return;
	}
	if ( !hf.rootNode  )
	{
		return;
	}
	
	hf.rootNode.forceShow(treeid);
}

function showHelpfileOnFrame(fileindex, files)
{
	var hcf = getFrameByName('helpContent', window.parent.parent);
	if ( !hcf )
	{
		return;
	}
    var hreffile  = "../docfile/" + files[fileindex];
    hcf.location = hreffile;
}

function printdiv(printpage)
{
	var divelem = document.getElementById(printpage);
	if ( null == divelem )
	{
		return;
	}
	var headstr = "<html><head><title></title></head><body>";
	var footstr = "</body></html>";
	var newstr  = divelem.innerHTML;
	var oldstr  = document.body.innerHTML;
	document.body.innerHTML = headstr+newstr+footstr;
	window.print(); 
	document.body.innerHTML = oldstr;
	return ;
}