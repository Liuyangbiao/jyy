package com.nsc.base.index;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.store.LockObtainFailedException;

public interface IIndexSearcher {

	public static enum TYPE {
		whole, field
	};

	/**
	 * 搜索文档
	 * 
	 * @param params
	 */
	public List<Map<Enum<?>, Object>> searchDocument(Map<?, Object> params,
			Map<?, Object> filters, int hitsPerPage, int page,
			boolean highLighter) throws ParseException, IOException,
			InvalidTokenOffsetsException, IllegalAccessException,
			InstantiationException, InvocationTargetException,
			NoSuchMethodException;

	public List<String> searchDocument(Map<?, Object> params,
			Map<?, Object> filters,boolean highLighter) throws ParseException, IOException,
			InvalidTokenOffsetsException, IllegalAccessException,
			InstantiationException, InvocationTargetException,
			NoSuchMethodException;
	public int getHitsSize();

	/**
	 * 初始化索引，提供索引文件夹和语法分析器
	 * 
	 * @param docDir
	 * @param analyzer
	 */
	public boolean initIndex(IndexSearcher[] indexSearchers, Analyzer analyzer)
			throws CorruptIndexException, LockObtainFailedException,
			IOException;

	/**
	 * 对检索重新加载
	 * @param srcDir
	 * @return
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
	public boolean reloadIndex(File srcDir) throws CorruptIndexException,
			LockObtainFailedException, IOException;

	/**
	 * 释放检索对象
	 * @return
	 */
	public boolean releaseIndex();
	
}
