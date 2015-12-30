package com.nsc.base.util;

import java.io.File;
import java.util.Date;
import java.net.ConnectException;

import org.apache.log4j.Logger;

import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.artofsolving.jodconverter.DocumentConverter;

public class Word2PDF {

	private File inputFile;// 需要转换的文件
	private File outputFile;// 输出的文件

	public Word2PDF(File inputFile, File outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}

	public void docToPdf() {
		Date start = new Date();
		// connect to an OpenOffice.org instance running on port 8100
		OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
		try {
			connection.connect();

			// convert
			DocumentConverter converter = new OpenOfficeDocumentConverter(
					connection);
			converter.convert(inputFile, outputFile);
		} catch (ConnectException cex) {
			Logger.getLogger(Word2PDF.class).warn("连接OpenOffice.org发生异常:",cex);
		} finally {
			// close the connection
			if (connection != null) {
				connection.disconnect();
				connection = null;
			}
		}
		long l = (start.getTime() - new Date().getTime());
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		Logger.getLogger(Word2PDF.class).info("生成" + outputFile.getName() + "耗费：" + min + "分" + s
				+ "秒");
	}
	
	public static void main(String[] args) {
		//转换word2007也是没问题的
	    File f1=new File("D:\\11.docx");
	    File f2=new File("D:\\22.pdf");
		Word2PDF w2p=new Word2PDF(f1,f2);
		w2p.docToPdf();
	}
}
