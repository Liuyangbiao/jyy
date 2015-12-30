package com.nsc.base.index;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.swing.text.BadLocationException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;

public interface IIndexWriter {

	/**
	 * 将文档增加到索引中
	 * 
	 * @param file
	 * @param params
	 * @param cPath
	 * @throws IOException
	 * @throws OpenXML4JException
	 * @throws XmlException
	 */
	public void addDocument(File file, File cPath,
			Map<Enum<?>, FileField> params) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, BadLocationException, XmlException,
			OpenXML4JException,InterruptedException;

	// 将文档增加到索引中 提示
	public void addKeyWords(DOCFIELDEnum keyword) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, BadLocationException, XmlException,
			OpenXML4JException, InvocationTargetException,
			NoSuchMethodException;

	/**
	 * 根据文档ID更新文档
	 * 
	 * @param file
	 * @param cPath
	 * @param params
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws BadLocationException
	 * @throws OpenXML4JException
	 * @throws XmlException
	 */
	public void updateDocument(File file, File cPath,
			Map<Enum<?>, FileField> params) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, BadLocationException, XmlException,
			OpenXML4JException,InterruptedException;

	/**
	 * 根据文档ID更新文档字段
	 * 
	 * @param params
	 */
	public void updateDocument(Map<Enum<?>, FileField> params);

	/**
	 * 根据标识符删除档案
	 * 
	 * @param id
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	public void deleteDocument(DOCFIELDEnum id) throws IllegalAccessException,
			InstantiationException, InvocationTargetException,
			NoSuchMethodException, CorruptIndexException, IOException;

	/**
	 * 初始化文件索引，提供索引文件夹和语法分析器
	 * 
	 * @param docDir
	 * @param analyzer
	 */
	public void initIndex(File docDir, Analyzer analyzer)
			throws CorruptIndexException, LockObtainFailedException,
			IOException;

	/**
	 * 初始化内存索引，提供索引文件夹和语法分析器
	 * 
	 * @param docDir
	 * @param analyzer
	 */
	public void initIndex(Analyzer analyzer) throws CorruptIndexException,
			LockObtainFailedException, IOException;
	
	/**
	 * 合并索引
	 * @param dics
	 */
	public void addIndex(Directory[] dics) throws CorruptIndexException, IOException;
	
	/**
	 * 得到目录
	 * @return
	 */
	public Directory getDirectory();

	/**
	 * 显性调用关闭程序，关闭所有索引
	 */
	public void closeWriter() throws IOException;
}
