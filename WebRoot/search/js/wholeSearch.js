var prevalue;

$(function() {
	// 根据鼠标点击地方来确认是否隐藏弹出DIV
	document.onmousedown = function(event) {
		var targ
		if (!e)
			var e = window.event
		if (e.target)
			targ = e.target
		else if (e.srcElement)
			targ = e.srcElement
		if (targ.nodeType == 3) // defeat Safari bug
			targ = targ.parentNode
		var tname
		tname = targ.tagName;
		if (tname != "FONT") {
			document.getElementById('helper').style.visibility = 'hidden';
		} else {
		}
	};
	
	// 添加键盘事件 按上下可以控制选中结果
	document.onkeydown = function(e) {
		var keycode;
		try {
			keycode = event.keyCode;
		} catch (err) {
			keycode = e.keyCode;
		}
		if (keycode == 40) {
			// 按了向下的键
			choosetr(nowid + 1);
		} else if (keycode == 38) {
			// 按了向上的键
			choosetr(nowid - 1);
		} else if (keycode == 32) {
			selecttr();
		}
	};
	
	checkvalue();
	
	$("conditions").bind("blur",function(){
		prevalue="";
	});
});

// 根据输入的内容 准备数据
function checkvalue() {
	var ids = document.getElementById('conditions').value;
	
	if (prevalue!=undefined && prevalue != ids) {
		$.getJSON('../search/suggestArchive.action?time=' + Math.random(), {
			ids : ids
		}, function(json) {
			getnameresponse(json);
		});
	}
	prevalue = ids;
	setTimeout(checkvalue, 300);
}

// div移动到正确的位置
function getabsposition(obj) {
	var r = {
		left : obj.offsetLeft,
		top : obj.offsetTop
	};
	r.left = obj.offsetLeft;
	r.top = obj.offsetTop;
	if (obj.offsetParent) {
		var tmp = getabsposition(obj.offsetParent);
		r.left += tmp.left;
		r.top += tmp.top;
	}
	return r;
}
// 设定弹出DIV的大小
function gettable(obj) {
	var pos = getabsposition(obj);
	pos.top += obj.offsetHeight;
	document.getElementById('helper').style.top = pos.top + "px";
	document.getElementById('helper').style.left = pos.left + "px";
	document.getElementById('helper').style.width = obj.offsetWidth + "px";
	document.getElementById('helper').style.visibility = '';
}

// 往DIV里添加内容
function getnameresponse(result) {
	var info = result;
	var html = '<table width="100%">';
	for ( var i = 0; i < info.length; i++) {
		html += '<tr id="tr' + (i + 1) + '" onmouseover="choosetr(' + (i + 1)
				+ ');" onclick="selecttr();">' + '<td colspan="2"><font>'
				+ info[i].name + '</font></td></tr>';
	}

	html += '<td align="left"><font color="red">按空格键选择</font></td>' + '<td align="right"><a href="#" onclick="document.getElementById(\'helper\').style.visibility=\'hidden\';">' + '关闭</a></td></tr></table>';

	document.getElementById('helper').innerHTML = html;
	if (info.length > 0) {
		gettable(document.getElementById('conditions'));
	} else {
		document.getElementById('helper').style.visibility = 'hidden';
	}
}

var nowid = 0;
// 选中结果 包括鼠标和键盘事件
function selecttr() {
	try {
		if (document.getElementById('helper').style.visibility == 'hidden')
			return;
		document.getElementById('conditions').value = document
				.getElementById('tr' + nowid).cells[0].childNodes[0].innerText;
		setTimeout(function() {
			document.getElementById('conditions').value = trim(document
					.getElementById('conditions').value);
		}, 50);
		falg = false;
		nowid = 0;
		var eles = document.getElementsByName('trname');
		for ( var i = 0; i < eles.length; i++) {
			eles[i].className = '';
		}
		document.getElementById('helper').style.visibility = 'hidden';
	} catch (e) {
	}
}

// 选中某个结果
function choosetr(nextid) {
	var len = 0;
	do {
		try {
			var obj = document.getElementById('tr' + (len + 1));
			if (obj)
				len++;
			else
				break;
		} catch (e) {
			break;
		}
	} while (1);
	if (nextid > len)
		nextid = 1;
	if (nextid < 1)
		nextid = len;
	if (nowid >= 1 && nowid <= len) {
		document.getElementById('tr' + nowid).className = '';
	}
	document.getElementById('tr' + nextid).className = 'gac_b';
	nowid = nextid;
}

// 去掉字符串空格
function trim(s) {
	try {
		return s.replace(/^\s+|\s+$/g, "");
	} catch (e) {
		return s;
	}
}