package com.nsc.dem.util.thumbnail;

import java.io.File;

import org.apache.log4j.Logger;

import com.nsc.base.conf.Configurater;
import com.nsc.base.thumbnail.ThumbnailInterface;
import com.nsc.base.util.DesUtil;
import com.nsc.base.util.ExtractImages;
import com.nsc.base.util.ImageToJpg;

public class IMAGEThumbnails implements ThumbnailInterface{

    //生成缩略图
	public String makeThumbnil(String local) {
		//缩略图本地路径
		String imagePath="";
		try {
			imagePath = DesUtil.getFilePath(local)
					+ Configurater.getInstance()
							.getConfigValue("miniature") + File.separator
					+ DesUtil.getFileName(local);
            //生成缩略图
			ImageToJpg.saveImageAsJpg(local, imagePath, 500, 500);
		} catch (Exception e) {
			Logger.getLogger(ExtractImages.class).warn(local + "生成缩略图失败", e);
		}
		return imagePath;
	}

}
