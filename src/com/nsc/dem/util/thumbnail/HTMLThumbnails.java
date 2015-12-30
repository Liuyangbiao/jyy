package com.nsc.dem.util.thumbnail;

import java.awt.Color;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import com.nsc.base.thumbnail.ThumbnailInterface;
import com.nsc.base.util.GraphUtils;

public class HTMLThumbnails  implements ThumbnailInterface {

	//生成缩略图
	public String makeThumbnil(String local) {
		//缩略图本地路径
		String imagePath=local.substring(0,local.lastIndexOf("."))+".jpeg";
		Color c=new Color(255,255,255);
		String str;
		try {
			str = GraphUtils.getHtml(imagePath);
			str=Pattern.compile("<meta(.*?)>").matcher(str).replaceAll("");
			//生成缩略图
			GraphUtils.toImages(c,str,500,500,imagePath);
		} catch (Exception e) {
			Logger.getLogger(GraphUtils.class).warn(local + "生成缩略图失败", e);
		}
		return imagePath;
	}

}
