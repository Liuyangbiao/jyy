package com.nsc.base.util;

import java.io.File;

import org.apache.log4j.Logger;

import ooo.connector.BootstrapSocketConnector;
import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XCloseable;

public class FileToPdf {

	private static XComponentContext createContext() throws Exception {
	    // get the remote office component context
	    // return Bootstrap.bootstrap();
		String oooExeFolder = "C:/Program Files/OpenOffice.org 3/program/";
	    return BootstrapSocketConnector.bootstrap(oooExeFolder);
	}
	
	private static XComponentLoader createLoader(XComponentContext context) throws Exception {
	    // get the remote office service manager
	    XMultiComponentFactory mcf = context.getServiceManager();
	    Object desktop = mcf.createInstanceWithContext("com.sun.star.frame.Desktop", context);
	    return (XComponentLoader) UnoRuntime.queryInterface(XComponentLoader.class, desktop);
	}
	
	//从Loader对象可以加载一篇文档
	private static Object loadDocument(XComponentLoader loader, String inputFilePath) throws Exception {
	    // Preparing properties for loading the document
	    PropertyValue[] propertyValues = new PropertyValue[1];
	    propertyValues[0] = new PropertyValue();
	    propertyValues[0].Name = "Hidden";
	    propertyValues[0].Value = new Boolean(true);
	    // Composing the URL by replacing all backslashs
	    File inputFile = new File(inputFilePath);
	    String inputUrl = "file:///" + inputFile.getAbsolutePath().replace('\\', '/');
	    return loader.loadComponentFromURL(inputUrl, "_blank", 0, propertyValues);
	}
	
	//接着自然就是文档转换了
	private static void convertDocument(Object doc, String outputFilePath, String convertType) throws Exception {
	    // Preparing properties for converting the document
	    PropertyValue[] propertyValues = new PropertyValue[2];
	    // Setting the flag for overwriting
	    propertyValues[0] = new PropertyValue();
	    propertyValues[0].Name = "Overwrite";
	    propertyValues[0].Value = new Boolean(true);
	    // Setting the filter name
	    propertyValues[1] = new PropertyValue();
	    propertyValues[1].Name = "FilterName";
	    propertyValues[1].Value = convertType;
	    // Composing the URL by replacing all backslashs
	    File outputFile = new File(outputFilePath);
	    String outputUrl = "file:///" + outputFile.getAbsolutePath().replace('\\', '/');
	    // Getting an object that will offer a simple way to store
	    // a document to a URL.
	    XStorable storable = (XStorable) UnoRuntime.queryInterface(XStorable.class, doc);
	    // Storing and converting the document
	    //storable.storeAsURL(outputUrl,　propertyValues);　
	    storable.storeToURL(outputUrl, propertyValues);
	}
	
	//最后还要关闭文档
	private static void closeDocument(Object doc) throws Exception {
	    // Closing the converted document. Use XCloseable.clsoe if the
	    // interface is supported, otherwise use XComponent.dispose
	    XCloseable closeable = (XCloseable) UnoRuntime.queryInterface(XCloseable.class, doc);
	    if (closeable != null) {
	     closeable.close(false);
	    } else {
	        XComponent component = (XComponent) UnoRuntime.queryInterface(XComponent.class, doc);
	        component.dispose();
	    }
	}
	
	/**
	 * 调用此方法执行文件转换
	 * @param inputFilePath  原始文件路径
	 * @param outputFilePath 转换后文件路径
	 * @param convertType    转换类型
	 */
	public static void wordToPdf(String inputFilePath,String outputFilePath,String convertType){
		try {
	        XComponentContext context = createContext();
	        XComponentLoader compLoader = createLoader(context);
	        Object doc = loadDocument(compLoader, inputFilePath);
	        convertDocument(doc, outputFilePath, convertType);
	        closeDocument(doc);
	    } catch (Exception e) {
	        Logger.getLogger(XComponentContext.class).error("word转pdf出错", e);
	    }
	}
	
	//最后便是将上面四个步骤串联起来
	public static void main(String args[]) {
	    String inputFilePath = "D:\\qq.doc";
	    String outputFilePath = "D:\\qq.pdf";
	    // the given type to convert to
	    String convertType = "writer_pdf_Export";
	    Logger logger = (Logger) Logger.getInstance(FileToPdf.class);
	    // String　convertType　=　"swriter:　MS　Word　97";
	    try {
	        XComponentContext context = createContext();
	        logger.info("connected to a running office ...");
	        XComponentLoader compLoader = createLoader(context);
	        logger.info("loader created ...");
	        Object doc = loadDocument(compLoader, inputFilePath);
	        logger.info("document loaded ...");
	        convertDocument(doc, outputFilePath, convertType);
	        logger.info("document converted ...");
	        closeDocument(doc);
	        logger.info("document closed ...");
	        System.exit(0);
	    } catch (Exception e) {
	        logger.warn(e.getMessage());
	        System.exit(1);
	    }
	}
}
