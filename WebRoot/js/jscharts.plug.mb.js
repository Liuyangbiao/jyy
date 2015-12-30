
/*
 * JSCharts MBString Path, by iLwave [16th Oct, 2009]
 *
 * page charset UTF-8, then include this script (below) with JSChart.
 *
 * demo:
 *
 *  var myChart = new JSChart('graph', 'line');
 * 		myChart.patchMbString();
 *
 *		//native code below.
 *
 */

JSChart.prototype.patchMbString = function() {
	
	if (this.bMbStringPathed) return false; //避免重复patch
	
	var fOriginalDraw = this.draw;
	var _this = this;
	
	var fGetVmlStrokeText = function() {

		var sVML = "<v:textbox id=\"{11}\" inset=\"0pt,0pt,0pt,0pt\" style=\"position:absolute; left:{1}px; top:{2}px; font-size:{3}px; font-weight:normal; width:auto; text-spacing:2px; font-family:{7}; color:{8}; margin:0px;\" rotation={10}>{0}</v:textbox>";
		
		for (var i=0; i<12; i++) sVML = sVML.replace("{" + i + "}", arguments[i]);
		
		return sVML;
		
	};
	
	this.fontFamily = "Tahoma"; // default font family
	
	this.setFontFamily = function(sFontFamily) {
		
		this.fontFamily = sFontFamily;
		
	};
	
	this.jg.ep=function(dI,text,cU,cV,fontsize,eO,eP,eQ,eR,color,dw,eS,id)
	{
		var nMbTextCount = text.replace(/[^\u4e00-\u9fa5]/ig, "").length;
		
		cU -= nMbTextCount * fontsize * 0.6; // 修复中文字符的双倍宽度导致的偏移
		
		if(typeof dI==='undefined'||typeof text==='undefined'||typeof cU==='undefined'||typeof cV==='undefined') return false;

		var gq = this.eN(fontsize,eO,eP,eQ,eR,color,dw,eS,id);

		set_textRenderContext(dI);
		
		if(!this.dL() && check_textRenderContext(dI)) {
			
			dI.font = (gq.fontsize+1) + "px " + _this.fontFamily;
			dI.fillStyle = gq.color;
			dI.fillText(text,cU-2,cV+10,gq.eP);
			
		}
		
		if(this.dL() && check_strokeTextCapability()) document.getElementById(this.z).getElementsByTagName('div')[0].innerHTML += fGetVmlStrokeText(text,cU,cV,gq.fontsize+2,gq.eO,gq.eP,gq.eQ,_this.fontFamily,gq.color,gq.dw*100,gq.eS,gq.id);
	};
	
	this.bMbStringPathed = true;

};