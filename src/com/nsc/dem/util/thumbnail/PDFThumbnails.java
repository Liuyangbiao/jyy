package com.nsc.dem.util.thumbnail;

import org.apache.log4j.Logger;

import com.nsc.base.thumbnail.ThumbnailInterface;
import com.nsc.base.util.ExtractImages;

public class PDFThumbnails implements ThumbnailInterface{

	//生成缩略图
	public String makeThumbnil(String local) {
		//缩略图本地路径
		String imagePath="";
		try {
			imagePath = ExtractImages.toImages(local);
		} catch (Exception e) {
			Logger.getLogger(ExtractImages.class).warn(local + "生成缩略图失败", e);
		}
		return imagePath;
	}
}
