package com.nsc.dem.util.thumbnail;

import java.io.File;

import org.apache.log4j.Logger;

import com.nsc.base.thumbnail.ThumbnailInterface;
import com.nsc.base.util.ExtractImages;
import com.nsc.base.util.FileUtil;
import com.nsc.base.util.Word2PDF;

public class RTFThumbnails implements ThumbnailInterface {

	/**
	 * 先将RTF文件转换成PDF文件，在将PDF生成缩略图
	 * 返回缩略图本地路径
	 */
	public String makeThumbnil(String local) {
		String imagePath = "";
		String wordToPdf = "";
		try {
			wordToPdf = local.substring(0, local.lastIndexOf(".")) + ".pdf";
			Word2PDF tools = new Word2PDF(new File(local), new File(
					wordToPdf));
			//rtf转换成pdf文件
			tools.docToPdf();
            //生成缩略图
			imagePath = ExtractImages.toImages(wordToPdf);
		} catch (Exception e) {
			Logger.getLogger(ExtractImages.class).warn(local + "生成缩略图失败", e);
		}
		
		if (!wordToPdf.equals("")) {
			FileUtil.deleteFile(wordToPdf);
		}
		return imagePath;
	}
}
