package com.nsc.dem.util.index;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import com.nsc.base.conf.Configurater;
import com.nsc.base.index.DOCFIELDEnum;
import com.nsc.base.index.FIELDEnum;
import com.nsc.base.index.FileField;
import com.nsc.base.index.IIndexSearcher;
import com.nsc.base.index.KeywordFilter;
import com.nsc.base.recource.ResourceLoader;

public class IndexSearcherWrap implements IIndexSearcher {

	private MultiSearcher searcher;
	private Analyzer analyzer;
	private int hitsSize = 0;


	
	/**
	 * 初始化索引
	 * @throws IOException 
	 */
	public boolean initIndex(IndexSearcher[] indexSearchers, Analyzer analyzer) throws IOException {

		this.searcher = new MultiSearcher(indexSearchers);
		this.analyzer = analyzer;
		return true;
	}

	/**
	 * 索引重新加载
	 */
	public boolean reloadIndex(IndexSearcher[] indexSearchers) throws CorruptIndexException,
			LockObtainFailedException, IOException {
		this.searcher = new MultiSearcher(indexSearchers);
		return true;
	}

	
	
	
	/**
	 * 得到结果数量
	 */
	public int getHitsSize() {
		return this.hitsSize;
	}

	/**
	 * 档案全文检索
	 * @throws  
	 */
	public List<Map<Enum<?>, Object>> searchDocument(Map<?, Object> params,
			Map<?, Object> filters, int hitsPerPage, int page,
			boolean highLighter) throws ParseException, IOException,
			InvalidTokenOffsetsException, IllegalAccessException,
			InstantiationException, InvocationTargetException,
			NoSuchMethodException{

		List<Map<Enum<?>, Object>> docList = new ArrayList<Map<Enum<?>, Object>>();

		// searcher.setSimilarity(similarity);

		BooleanQuery bQuery = new BooleanQuery(); // 组合查询

		Set<?> keys = params.keySet();

		for (Object param : keys) {

			String qStr = (String) params.get(param);

			if (qStr == null || qStr.length() == 0)
				continue;

			try {

				// 去掉特殊字符
				qStr = KeywordFilter.filterKeyword(qStr);

				Query preQuery = new QueryParser(Version.LUCENE_30, " ",
						analyzer).parse(qStr);
				qStr = preQuery.toString().replaceAll("\"", "").replaceAll(":",
						"");
			} catch (ParseException ex) {
				Logger.getLogger(IndexSearcherWrap.class).warn(ex);
				continue;
			}

			if (qStr.length() == 0) {
				continue;
			} else if (param.equals(TYPE.whole)) {
				doWholeQuery(bQuery, qStr);
			} else if (param instanceof DOCFIELDEnum
					|| param instanceof EXFIELDEnum
					|| param instanceof FIELDEnum) {

				Enum<?> e = (Enum<?>) param;

				FileField f = DOCFIELDEnum.getValue(e.name()) != null ? DOCFIELDEnum
						.getValue(e.name()).getF()
						: EXFIELDEnum.getValue(e.name()) != null ? EXFIELDEnum
								.getValue(e.name()).getF() : FIELDEnum
								.getValue(e.name()).f;

				Query fieldQuery = new QueryParser(Version.LUCENE_30, e.name(),
						analyzer).parse(qStr);

				bQuery.add(fieldQuery, f.getOccurInt() == 3 ? Occur.SHOULD : f
						.getOccurInt() == 2 ? Occur.MUST_NOT : Occur.MUST);

				Logger.getLogger(IndexSearcherWrap.class).info(
						"搜索词汇: " + fieldQuery.toString());
			} else if (param instanceof String) {
				Query fieldQuery = new QueryParser(Version.LUCENE_30, param
						.toString(), analyzer).parse(qStr);

				bQuery.add(fieldQuery, Occur.SHOULD);

				Logger.getLogger(IndexSearcherWrap.class).info(
						"搜索词汇: " + fieldQuery.toString());
			} else {
				Logger.getLogger(IndexSearcherWrap.class).info(
						"无法判断该条件的类型： " + param.getClass().getName()
								+ " : 搜索词汇: " + qStr);
			}
		}

		if (bQuery.clauses().size() == 0)
			return docList;

		Logger.getLogger(IndexSearcherWrap.class).info(
				"全部搜索词汇: " + bQuery.toString());

		// Collect enough docs to show 5 pages
		TopScoreDocCollector collector = TopScoreDocCollector.create(page
				* hitsPerPage, false);

		BooleanQuery bfQuery = new BooleanQuery(); // 组合查询
		for (Object filter : filters.keySet()) {

			String qStr = (String) filters.get(filter);

			if (filter instanceof DOCFIELDEnum || filter instanceof EXFIELDEnum
					|| filter instanceof FIELDEnum) {

				Enum<?> e = (Enum<?>) filter;

				Term term = new Term(e.name(), qStr);
				Query query = new TermQuery(term);

				bfQuery.add(query, Occur.MUST);

				Logger.getLogger(IndexSearcherWrap.class).info(
						"过滤词汇: " + query.toString());
			} else if (filter instanceof String) {

				Term term = new Term(filter.toString(), qStr);
				Query query = new TermQuery(term);

				bfQuery.add(query, Occur.MUST);

				Logger.getLogger(IndexSearcherWrap.class).info(
						"过滤词汇: " + query.toString());
			} else {
				Logger.getLogger(IndexSearcherWrap.class).info(
						"无法判断该条件的类型： " + filter.getClass().getName()
								+ " : 过滤词汇: " + qStr);
			}
		}

		QueryWrapperFilter filter = new QueryWrapperFilter(bfQuery);

		Logger.getLogger(IndexSearcherWrap.class).info(
				"全部过滤词汇: " + bfQuery.toString());

		if (bfQuery.clauses().size() != 0)
			searcher.search(bQuery, filter, collector);
		else
			// searcher.search(new TermQuery(new Term("cpath","con_59065.txt")),
			// collector);
			searcher.search(bQuery, collector);

		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		hitsSize = collector.getTotalHits();
		Logger.getLogger(IndexSearcherWrap.class).info(
				hitsSize + " total matching documents");

		if (hitsSize == 0)
			return docList;

		int start = (page - 1) * hitsPerPage;
		int end = Math.min(hits.length, start + hitsPerPage);
		// if (hits.length >= start) {
		// // end = Math.max(hits.length, start + hitsPerPage);
		// } else {

		// }

		// 用这个进行高亮显示，默认是<b>..</b>
		// 用这个指定<read>..</read>
		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
				"<b><font color='red'>", "</font></b>");
		QueryScorer queryScorer = new QueryScorer(bQuery);
		// 构造高亮
		// 指定高亮的格式
		// 指定查询评分
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter,
				queryScorer);
		// 这个一般等于你要返回的，高亮的数据长度
		// 如果太小，则只有数据的开始部分被解析并高亮，且返回的数据也少
		// 太大，有时太浪费了。
		highlighter
				.setTextFragmenter(new SimpleSpanFragmenter(queryScorer, 200));
		for (int i = start; i < end; i++) {

			Document doc = searcher.doc(hits[i].doc);

			Logger.getLogger(IndexSearcherWrap.class).info(
					doc.get("title") + " 匹配分数: " + hits[i].score);

			Map<Enum<?>, Object> fieldMap = new HashMap<Enum<?>, Object>();

			List<Fieldable> fieldList = doc.getFields();

			for (Fieldable field : fieldList) {
				String name = field.name();
				String content = doc.get(name);

				Enum<?> filedEnum = FIELDEnum.getValue(name);

				if (filedEnum != null) {
					FIELDEnum fileF = (FIELDEnum) filedEnum;

					// 提取内容,如果内容存放路径不为空，则从另存文件中提取内容
					if (fileF.equals(FIELDEnum.cpath)) {

						String cFile = doc.get(FIELDEnum.cpath.name());
						
						String contentDir = Configurater.getInstance().getConfigValue("doc_content_Dir");
						URL contentUrl = ResourceLoader.getDefaultClassLoader().getResource(contentDir);
						try {
							contentDir = contentUrl.toURI().getPath();
						} catch (URISyntaxException e) {
							Logger.getLogger(IndexSearcherWrap.class).warn(e.getMessage());
						}
						File file = new File(contentDir,File.separator+cFile);

						if (file.exists()) {
							FileReader ir = new FileReader(file);

							StringBuffer sb = new StringBuffer();

							int r = -1;
							char[] buf = new char[1024];
							while ((r = ir.read(buf)) != -1)
								sb.append(buf, 0, r);

							ir.close();

							content = sb.toString();
						} else {
							content = "";
						}

						filedEnum = FIELDEnum.contents;
						fileF = FIELDEnum.contents;
					}

					if (highLighter && fileF.f.isHighLighter()
							&& content != null && content.trim().length() > 0) {
						content = highLigher(highlighter, hits[i].doc, content,
								200).toString();
					} else if (fileF.equals(FIELDEnum.contents)) {
						content = content.substring(0, Math.min(content
								.length(), 200));
					}
				} else {
					filedEnum = DOCFIELDEnum.getValue(name);

					if (filedEnum != null) {
						DOCFIELDEnum docF = (DOCFIELDEnum) filedEnum;

						if (highLighter && docF.getF().isHighLighter()) {
							content = highLigher(highlighter, hits[i].doc,
									content, 200).toString();
						} else if (docF.equals(DOCFIELDEnum.title)) {
							content = content.substring(0, Math.min(content
									.length(), 200));
						}
					} else {
						filedEnum = EXFIELDEnum.getValue(name);

						if (filedEnum != null) {
							EXFIELDEnum docF = (EXFIELDEnum) filedEnum;

							if (highLighter && docF.getF().isHighLighter()) {
								content = highLigher(highlighter, hits[i].doc,
										content, 200).toString();
							} else {
								content = content.substring(0, Math.min(content
										.length(), 200));
							}
						}
					}
				}

				fieldMap.put(filedEnum, content);
			}

			docList.add(fieldMap);
		}

		this.searcher.close();

		return docList;
	}

	/**
	 * 高亮处理
	 * 
	 * @param highlighter
	 * @param doc
	 * @param contents
	 * @param limit
	 * @return
	 * @throws IOException
	 * @throws InvalidTokenOffsetsException
	 */
	@SuppressWarnings("all")
	private StringBuffer highLigher(Highlighter highlighter, int doc,
			String contents, int limit) throws IOException,
			InvalidTokenOffsetsException {

		if (contents != null) {
			StringBuffer sb = new StringBuffer();
			// TermPositionVector tpv = (TermPositionVector) searcher
			// .getIndexReader().getTermFreqVector(doc,
			// FIELDEnum.contents.name());

			TokenStream tokenStream;
			// if (tpv != null)
			// tokenStream = TokenSources.getTokenStream(tpv,true);
			// else
			tokenStream = this.analyzer.tokenStream(FIELDEnum.contents.name(),
					new StringReader(contents));

			String str = highlighter.getBestFragment(tokenStream, contents);

			sb// .append("<li><li>")
					.append(str);
			// .append("<br/>");

			int length = Math.min(limit, contents.length());

			return str == null ? new StringBuffer(contents.substring(0, length))
					: sb;
		}

		return null;
	}

	/**
	 * 全字段查询
	 * 
	 * @param bQuery
	 * @param qStr
	 * @throws ParseException
	 */
	private void doWholeQuery(BooleanQuery bQuery, String qStr)
			throws ParseException {

		// 遍历基础字段
		for (FIELDEnum key : FIELDEnum.values()) {

			Query fieldQuery = new QueryParser(Version.LUCENE_30, key.name(),
					analyzer).parse(qStr);

			bQuery.add(fieldQuery, Occur.SHOULD);

			Logger.getLogger(IndexSearcherWrap.class).info(
					"搜索词汇: " + fieldQuery.toString());
		}

		// 遍历档案字段
		for (DOCFIELDEnum key : DOCFIELDEnum.values()) {

			Query fieldQuery = new QueryParser(Version.LUCENE_30, key.name(),
					analyzer).parse(qStr);

			bQuery.add(fieldQuery, Occur.SHOULD);

			Logger.getLogger(IndexSearcherWrap.class).info(
					"搜索词汇: " + fieldQuery.toString());
		}

		// 遍历扩展字段
		for (EXFIELDEnum key : EXFIELDEnum.values()) {
			Query fieldQuery = new QueryParser(Version.LUCENE_30, key.name(),
					analyzer).parse(qStr);

			bQuery.add(fieldQuery, Occur.SHOULD);

			Logger.getLogger(IndexSearcherWrap.class).info(
					"搜索词汇: " + fieldQuery.toString());
		}
	}

	/**
	 * 释放检索
	 * @throws IOException 
	 */
	public boolean releaseIndex() {
		try {
			this.searcher.close();
			return true;
		} catch (IOException e) {
			Logger.getLogger(IndexSearcherWrap.class).info(
					"释放检索失败: " + e);
		}
		return false;
	}

	public List<String> searchDocument(Map<?, Object> params,
			Map<?, Object> filters, boolean highLighter) throws ParseException,
			IOException, InvalidTokenOffsetsException, IllegalAccessException,
			InstantiationException, InvocationTargetException,
			NoSuchMethodException {

		List<String> docList = new ArrayList<String>();
		BooleanQuery bQuery = new BooleanQuery(); // 组合查询
		Set<?> keys = params.keySet();
		for (Object param : keys) {
			String qStr = (String) params.get(param);
			if (qStr == null || qStr.length() == 0)
				continue;
			try {
				// 去掉特殊字符
				qStr = KeywordFilter.filterKeyword(qStr);
				Query preQuery = new QueryParser(Version.LUCENE_30, " ",
						analyzer).parse(qStr);
				qStr = preQuery.toString().replaceAll("\"", "").replaceAll(":",
						"");
			} catch (ParseException ex) {
				Logger.getLogger(IndexSearcherWrap.class).warn(ex);
				continue;
			}

			if (qStr.length() == 0) {
				continue;
			} else if (param.equals(TYPE.whole)) {
				doWholeQuery(bQuery, qStr);
			} else if (param instanceof DOCFIELDEnum
					|| param instanceof EXFIELDEnum
					|| param instanceof FIELDEnum) {
				Enum<?> e = (Enum<?>) param;
				FileField f = DOCFIELDEnum.getValue(e.name()) != null ? DOCFIELDEnum
						.getValue(e.name()).getF()
						: EXFIELDEnum.getValue(e.name()) != null ? EXFIELDEnum
								.getValue(e.name()).getF() : FIELDEnum
								.getValue(e.name()).f;

				Query fieldQuery = new QueryParser(Version.LUCENE_30, e.name(),
						analyzer).parse(qStr);
				bQuery.add(fieldQuery, f.getOccurInt() == 3 ? Occur.SHOULD : f
						.getOccurInt() == 2 ? Occur.MUST_NOT : Occur.MUST);
				Logger.getLogger(IndexSearcherWrap.class).info(
						"搜索词汇: " + fieldQuery.toString());
			} else if (param instanceof String) {
				Query fieldQuery = new QueryParser(Version.LUCENE_30, param
						.toString(), analyzer).parse(qStr);

				bQuery.add(fieldQuery, Occur.SHOULD);
				Logger.getLogger(IndexSearcherWrap.class).info(
						"搜索词汇: " + fieldQuery.toString());
			} else {
				Logger.getLogger(IndexSearcherWrap.class).info(
						"无法判断该条件的类型： " + param.getClass().getName()
								+ " : 搜索词汇: " + qStr);
			}
		}

		if (bQuery.clauses().size() == 0)
			return docList;
		Logger.getLogger(IndexSearcherWrap.class).info(
				"全部搜索词汇: " + bQuery.toString());
		TopScoreDocCollector collector = TopScoreDocCollector
				.create(200, false);
		BooleanQuery bfQuery = new BooleanQuery(); // 组合查询
		QueryWrapperFilter filter = new QueryWrapperFilter(bfQuery);
		Logger.getLogger(IndexSearcherWrap.class).info(
				"全部过滤词汇: " + bfQuery.toString());
		if (bfQuery.clauses().size() != 0)
			searcher.search(bQuery, filter, collector);
		else
			searcher.search(bQuery, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		hitsSize = collector.getTotalHits();
		Logger.getLogger(IndexSearcherWrap.class).info(
				hitsSize + " total matching documents");
		if (hitsSize == 0)
			return docList;
		// 查询结果
		for (int i = 0; i < hitsSize; i++) {
			Document doc = searcher.doc(hits[i].doc);
			Logger.getLogger(IndexSearcherWrap.class).info(
					(doc.get("title") == null ? doc.get("keyword") : doc.get("title"))
					+ " 匹配分数: " + hits[i].score);
			List<Fieldable> fieldList = doc.getFields();
			String content = "";
			for (Fieldable field : fieldList) {
				content = doc.get(field.name());
			}
			docList.add(content);
		}
		this.searcher.close();
		return docList;
	}

	public boolean reloadIndex(File srcDir) throws CorruptIndexException,
			LockObtainFailedException, IOException {
		// TODO Auto-generated method stub
		return false;
	}
}
