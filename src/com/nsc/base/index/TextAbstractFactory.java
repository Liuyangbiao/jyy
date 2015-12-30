package com.nsc.base.index;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.text.BadLocationException;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;

import com.nsc.base.conf.Configurater;
import com.nsc.base.util.FileUtil;
import com.nsc.dem.util.index.abstractor.Excel2007Abstractor;

public class TextAbstractFactory extends Factory<ITextAbstractor> {

	/**
	 * 从文件中提取文本
	 * 
	 * @param file
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws BadLocationException
	 * @throws InterruptedException 
	 */
	public StringBuffer getText(File file) throws ClassNotFoundException,
	InstantiationException, IllegalAccessException, IOException,
	BadLocationException, XmlException, OpenXML4JException, InterruptedException {
		ITextAbstractor textAbstractor;
		if (file.exists() && file.canRead()) {
			String suffix = file.getName();
			suffix = suffix.substring(suffix.lastIndexOf(".") + 1);
			textAbstractor = getAbstractor(suffix);
			//无内容提取工具
			if(textAbstractor == null)
				return null;
			textAbstractor.setFile(file);
			//启动线程
			textAbstractor.start();
			long time = 0;
			long maxTime = Integer.parseInt(Configurater.getInstance().getConfigValue("FileReadTimeOut"));
			while(true){
				if(textAbstractor.isFinish()){
					//textAbstractor.stop();
					return textAbstractor.getText();
				}else if(time >= maxTime){
					textAbstractor.stop();
					Logger.getLogger(TextAbstractFactory.class).info("提取内容失败！已经超时！");
					return null;
				}
				//每一秒检查一次
				Thread.sleep(1000);
				time += 1;
			}
		}
		return null;
		}

	/**
	 * 根据文件后缀取得文件抽取对象
	 * 
	 * 文件类型：PDF、Word、Excel、AutoCAD、Txt
	 * 
	 * @param fileType
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private ITextAbstractor getAbstractor(String suffixName)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {

		String metaType = FileUtil.getFileFormat("*." + suffixName)
				.toLowerCase();

		if (metaType.equals("txt"))
			return new TxtAbstractor();
		else
			return super.getImplement(metaType + "_Abstractor");
	}

	/**
	 * 读取Txt文件内容类
	 * 
	 * @author jiangcx
	 * 
	 */
	private class TxtAbstractor extends ITextAbstractor {

		public StringBuffer abstractText(File file) throws IOException {

			FileReader fr = new FileReader(file);

			StringBuffer contents = new StringBuffer("");

			char[] cbuf = new char[2048];

			int i;

			while ((i = fr.read(cbuf)) != -1) {
				contents.append(cbuf, 0, i);
			}

			return contents;
		}
	}
	
	
}
