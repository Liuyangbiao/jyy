package com.nsc.dem.util.index;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.swing.text.BadLocationException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;

import com.nsc.base.index.DOCFIELDEnum;
import com.nsc.base.index.FIELDEnum;
import com.nsc.base.index.FileField;
import com.nsc.base.index.IIndexWriter;
import com.nsc.base.util.AppException;

public class IndexWriterWrap implements IIndexWriter {

	IndexWriter writer;
	File docDir;
	Analyzer analyzer;

	/**
	 * 初始化Index，提供索引位置和语法分析器
	 * 
	 * @param docDir
	 * @param analyzer
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
	public void initIndex(File docDir, Analyzer analyzer)
			throws CorruptIndexException, LockObtainFailedException,
			IOException {

		// 不存在就创建
		if (!docDir.exists()) {
			docDir.mkdirs();
		}

		if (!docDir.canRead() || !docDir.canWrite()) {
			throw new IOException("目录: " + docDir.getAbsolutePath()
					+ "不可写或不可读，请检查该路径！");
		}

		File segFile = new File(docDir, "segments.gen");

		Logger.getLogger(IndexWriterWrap.class).info(
				"索引文件夹下拥有索引文件？" + segFile.exists());
		
		Directory fdic=FSDirectory.open(docDir);

		writer = new IndexWriter(fdic, analyzer, !segFile
				.exists(), IndexWriter.MaxFieldLength.LIMITED);

		this.docDir = docDir;
		this.analyzer = analyzer;
	}

	public void initIndex(Analyzer analyzer) throws CorruptIndexException,
			LockObtainFailedException, IOException {
		RAMDirectory ramdir=new RAMDirectory();
		writer=new IndexWriter(ramdir, analyzer,true,IndexWriter.MaxFieldLength.LIMITED);
		this.analyzer = analyzer;
	}

	public void closeWriter() throws IOException {
		writer.optimize();
		writer.close();
	}

	/**
	 * 增加文档索引
	 * 
	 * @param file
	 *            源文件
	 * @param cPath
	 *            内容存储路径
	 * @param params
	 *            索引参数
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws BadLocationException
	 * @throws OpenXML4JException
	 * @throws XmlException
	 * @throws InterruptedException 
	 */
	public void addDocument(File file, File cPath,
			Map<Enum<?>, FileField> params) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, BadLocationException, XmlException,
			OpenXML4JException, InterruptedException {
		indexDocs(file, cPath, params);
	}
	
	////将文档增加到索引中     提示
	public void addKeyWords(DOCFIELDEnum keyword) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, BadLocationException, XmlException,
			OpenXML4JException, InvocationTargetException, NoSuchMethodException {
		
		Logger.getLogger(IndexWriterWrap.class).info("建立检索词提示索引: ");
		
		try {
			Document document = DocumentWraper.wrapKeyWords(keyword);

			writer.addDocument(document);
		}
		catch (FileNotFoundException fnfe) {
			Logger.getLogger(IndexWriterWrap.class).warn("文件没有发现异常:",
					fnfe);
		} catch (AppException ex) {
			Logger.getLogger(IndexWriterWrap.class).warn(ex);
		}
	}

	/**
	 * 更新文档索引
	 * @throws InterruptedException 
	 */
	public void updateDocument(File file, File cPath,
			Map<Enum<?>, FileField> params) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, BadLocationException, XmlException,
			OpenXML4JException, InterruptedException {

		Document doc = DocumentWraper.wrapDocument(file, params, cPath);

		Field field = doc.getField(DOCFIELDEnum.docid.name());

		String value = field.stringValue();
		Term term = new Term(DOCFIELDEnum.docid.name(), value);

		writer.updateDocument(term, doc);
	}

	public void deleteDocument(DOCFIELDEnum id) throws IllegalAccessException,
			InstantiationException, InvocationTargetException,
			NoSuchMethodException, CorruptIndexException, IOException {

		Term term = new Term(id.name(), id.getF().getContent());

		IndexSearcher searcher = new IndexSearcher(writer.getReader().clone(true));

		TopDocs docs = searcher.search(new TermQuery(term), 1);
		if (docs.totalHits == 1) {
			Document document = searcher.doc(docs.scoreDocs[0].doc);
			String cpath = document.getField(FIELDEnum.cpath.name())==null?"null.txt":
						   document.getField(FIELDEnum.cpath.name()).stringValue();
			File file = new File(docDir.getParentFile(), "content"
					+ File.separator + cpath);

			file.delete();
		}
		
		searcher.close();
		
		writer.deleteDocuments(term);

		Logger.getLogger(IndexWriterWrap.class).info(
				"删除文档索引: " + id.getF().getContent());
	}

	/**
	 * 对文档创建索引
	 * 
	 * @param file
	 * @param cPath
	 *            文件内容存放路径
	 * @param params
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws BadLocationException
	 * @throws InterruptedException 
	 */
	private void indexDocs(File file, File cPath, Map<Enum<?>, FileField> params)
			throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, BadLocationException, XmlException,
			OpenXML4JException, InterruptedException {
		// do not try to index files that cannot be read
		if (file.canRead()) {
			if (file.isDirectory()) {
				String[] files = file.list();
				// an IO error could occur
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						indexDocs(new File(file, files[i]), cPath, params);
					}
				}
			} else {

				Logger.getLogger(IndexWriterWrap.class).info(
						"建立索引: " + file.getName());

				try {
					Document document = DocumentWraper.wrapDocument(file,
							params, cPath);

					writer.addDocument(document);
				}
				// at least on windows, some temporary files raise this
				// exception with an "access denied" message
				// checking if the file can be read doesn't help
				catch (FileNotFoundException fnfe) {
					Logger.getLogger(IndexWriterWrap.class).warn("文件没有发现异常:",
							fnfe);
				} catch (AppException ex) {
					Logger.getLogger(IndexWriterWrap.class).warn(ex);
				}
			}
		}
	}
	
	public void updateDocument(Map<Enum<?>, FileField> params) {
		// TODO 需要完善文档参数更新
		
	}

	/**
	 * 得到目录
	 */
	public Directory getDirectory() {
		if(this.writer!=null)
		return this.writer.getDirectory();
		
		return null;
	}

	public void addIndex(Directory[] dics) throws CorruptIndexException, IOException {
		this.writer.addIndexesNoOptimize(dics);
	}
}

