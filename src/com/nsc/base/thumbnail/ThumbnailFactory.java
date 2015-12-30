package com.nsc.base.thumbnail;

import com.nsc.base.conf.Configurater;
import com.nsc.base.index.Factory;
import com.nsc.base.util.FileUtil;

public class ThumbnailFactory extends Factory<ThumbnailInterface> {
	
    private static ThumbnailFactory thumbnail=null;
    
	public static ThumbnailFactory getInstance(){
		if(thumbnail==null){
			thumbnail=new ThumbnailFactory();
		}
		return thumbnail;
	}
	/**
	 * 根据文件后缀取得文件抽取对象
	 * 文件类型：PDF、Word
	 * @param fileType
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public  ThumbnailInterface getAbstractor(String fileName) 
	       throws ClassNotFoundException, InstantiationException, 
	       IllegalAccessException{
		
		//先判断是否是图片文件
		String fileSuffix = fileName.substring(fileName.lastIndexOf(".")+1);
		String mimeType = Configurater.getInstance().getConfigValue("mime",
				fileSuffix.toLowerCase());
		if (mimeType!=null && mimeType.indexOf("image")!=-1) {
			return super.getImplement("image_Thumbnails");
		}
		
		String metaType = FileUtil.getFileFormat(fileName)
				.toLowerCase();
		if(metaType==null || "".equals(metaType))
			return null;
		
		return super.getImplement(metaType + "_Thumbnails");
	}
}
