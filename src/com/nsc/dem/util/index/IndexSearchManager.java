package com.nsc.dem.util.index;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.nsc.base.conf.Configurater;

/**
 * IndexSearchManager维护类
 * 
 * 单例模式
 */
public class IndexSearchManager {

	private Map<File, IndexSearcher> indexSearches;
	private static IndexSearchManager indexSearchManager;
	private Boolean reloading = false;

	/**
	 * 获取对象
	 * 
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static IndexSearchManager getInstance() throws URISyntaxException,
			IOException {
		if (indexSearchManager == null) {
			indexSearchManager = new IndexSearchManager();
		}
		return indexSearchManager;
	}

	/**
	 * 构造方法 初始化所有的检索文件
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	private IndexSearchManager() throws URISyntaxException, IOException {
		String unitCode = Configurater.getInstance().getConfigValue("unitCode");
		indexSearches = new HashMap<File, IndexSearcher>();

		// 加载word
		reloadSingleFile(FileDirUtils.getWordFile());

		// 单位ID，如果是国家电网系统有五个单位ID
		if (StringUtils.isNotBlank(unitCode)) {
			String[] unitIds = unitCode.split(",");

			// 国网系统中有多个ID
			for (String unitId : unitIds) {
				List<File> writeFiles = FileDirUtils.getAllLoadDirs("doc_write_Dir", unitId);
				// 同步本地的索引库,成功后初始化
				for (File file : writeFiles) {
					File readFile = FileDirUtils.getReadFileByWriteFile(file);
					if (initReadFolder(file, readFile)) {
						reloadSingleFile(readFile);
					}
				}
			}
		}
	}

	/**
	 * 加载单个文件
	 */
	public boolean reloadSingleFile(File file) {
		if (file == null)
			return false;
		if (file.list().length <= 0) {
			Logger.getLogger(IndexSearchManager.class).info(
					"检索库目录为空，不能加载：" + file.getPath());
			return false;
		}
		// 如果已经加载，不再初始化
		if (indexSearches.get(file) == null) {
			try {
				IndexSearcher searcher = new IndexSearcher(IndexReader
						.open(FSDirectory.open(file)));
				indexSearches.put(file, searcher);
				Logger.getLogger(IndexSearchManager.class).info(
						"加载成功：" + file.getPath());
				return true;
			} catch (CorruptIndexException e) {
				Logger.getLogger(IndexSearchManager.class).warn(
						file.getPath() + e);
			} catch (IOException e) {
				Logger.getLogger(IndexSearchManager.class).warn(
						"检索库不能打开" + file.getPath() + e);
			}
		}
		return false;
	}

	/**
	 * 索引库同步
	 */
	public boolean initReadFolder(File writeFolder, File readFolder)
			throws URISyntaxException, IOException {
		//如果已经加载，则不能同步
		if (indexSearches.get(readFolder) != null) {
			return false;
		}
		if (!readFolder.exists()) {
			readFolder.mkdirs();
		}
		//如果第一次使用时write不为空，read为空时，加载
		//write的上一次修改时间大于read的上一次时间时，加载
		if ((writeFolder.lastModified() > readFolder.lastModified() && 
				 writeFolder.list().length > 0) || (writeFolder.listFiles().length > 0 &&
						 readFolder.listFiles().length == 0)) {

			File[] files = readFolder.listFiles();

			for (File file : files) {
				if (file.isDirectory())
					FileUtils.deleteDirectory(file);
				else
					file.delete();
			}

			FSDirectory srcDic = FSDirectory.open(writeFolder);
			FSDirectory destDic = FSDirectory.open(readFolder);

			Directory.copy(srcDic, destDic, true);

			srcDic.close();
			destDic.close();

			Logger.getLogger(IndexSearchManager.class).info(
					"检索文件同步完成:" + writeFolder);
		}

		return readFolder.list().length > 0;
	}

	/**
	 * 释放单个索引
	 * 
	 * @param docDir
	 * @return
	 */
	public boolean releaseSearch(File file) {
		IndexSearcher indexSearcher = indexSearches.get(file);
		//如果没有使用，直接返回true
		if(null == indexSearcher){
			return true;
		}

		boolean released = false;
		synchronized (reloading) {
			reloading = true;
		}
		
		try {
			indexSearcher.getIndexReader().close();
			Logger.getLogger(IndexSearchManager.class).info(
					"释放成功" + file.getPath());
			indexSearches.remove(file);
		} catch (IOException e) {
			Logger.getLogger(IndexSearchManager.class).info(
					"释放失败" + file.getPath());
		}
		
		
		synchronized (reloading) {
			reloading = false;
		}
		return released;
	}

	/**
	 * 释放所有文件
	 */
	public void releaseAllSearch() {
		for (File file : indexSearches.keySet()) {
			try {
				indexSearches.get(file).getIndexReader().close();
				Logger.getLogger(IndexSearchManager.class).info(
						"释放成功" + file.getPath());
			} catch (IOException e) {
				Logger.getLogger(IndexSearchManager.class).info(
						"释放失败" + file.getPath() + e);
			}
		}
		indexSearchManager = null;
		indexSearches = null;
	}

	/**
	 * 取得索引对象组
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public IndexSearcher[] getIndexSearcher(String unitId)
			throws URISyntaxException, IOException {
		synchronized (reloading) {
			if (reloading) {
				Logger.getLogger(IndexSearchManager.class).info("目录库正在使用");
				return null;
			}
		}
		
		
		String systemType = Configurater.getInstance().getConfigValue("system_type");
		//如果是省公司用户在国网登录，应该加载同步检索库
		if(StringUtils.isNotBlank(unitId) && unitId.trim().length() == 8
				&& "1".equals(systemType.trim())){
			String synWrite = FileDirUtils.getRealPathByUnitId("doc_write_Dir", unitId, "syn");
			File synFile = new File(synWrite);
			if(!synFile.exists()){
				synFile.mkdirs();
			}else{
				File synReadFile = FileDirUtils.getReadFileByWriteFile(synFile);
				if(this.initReadFolder(synFile, synReadFile)){
					reloadSingleFile(synReadFile);
					Logger.getLogger(IndexSearchManager.class).info(unitId + "省公司用户登录国网," +
							"同步检索库加载成功");
				}
			}
		}
		
		List<File> readFile = new ArrayList<File>();
		if(StringUtils.isNotBlank(unitId)){
			readFile.addAll(FileDirUtils.getAllLoadDirs("doc_read_Dir",unitId));
		}
		
		//获取word
		File wordFile = FileDirUtils.getWordFile();
		readFile.add(wordFile);
		
		List<IndexSearcher> lists = new ArrayList<IndexSearcher>();
		for (File file : readFile) {
			if (null != indexSearches.get(file)) {
				lists.add(indexSearches.get(file));
			}
		}
		
		// lists to array
		IndexSearcher[] indexs = new IndexSearcher[lists.size()];
		int i = 0;
		for (IndexSearcher searcher : lists) {
			indexs[i++] = searcher;
		}
		Logger.getLogger(IndexSearchManager.class).info(
				"共获取" + lists.size() + "个IndexSearch对象");
		return indexs;
	}

}
