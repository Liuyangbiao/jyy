package com.nsc.dem.util.index;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.nsc.base.conf.Configurater;
import com.nsc.base.index.AnalyzerFactory;
import com.nsc.base.index.DOCFIELDEnum;
import com.nsc.base.index.FileField;
import com.nsc.base.index.IIndexWriter;
import com.nsc.base.index.IndexFactory;
import com.nsc.base.recource.ResourceLoader;

public class KeyWordsIndex {
	public static Map<Enum<?>, FileField> setArchivesIndex(String conditions) throws Exception{
		Map<Enum<?>, FileField> params = new HashMap<Enum<?>, FileField>();
		DOCFIELDEnum keyword = DOCFIELDEnum.keyword;
		keyword.setValue(conditions);
		params.put(keyword, keyword.getF());
		return params;
	}
	
	public static Set<String> indexingArchive(Map<String, Map<Enum<?>, FileField>> files,String folder,boolean update) throws Exception{
		String docDir = Configurater.getInstance().getConfigValue("doc_word_Dir");
		// 检索文件存放目录
		URL docUrl = ResourceLoader.getDefaultClassLoader().getResource(docDir);
		docDir = docUrl.toURI().getPath();
		String indexDir = docDir;
		if (folder != null && folder.length() > 0)
			indexDir = docDir + File.separator + folder;
		boolean reload = false;
		IIndexWriter writer = null;
		File iFolder = new File(indexDir);
		Set<String> falses = new java.util.HashSet<String>();
		try {
			writer = IndexFactory.getInstance().getIndexWriter(iFolder,
					AnalyzerFactory.getInstance().getAnalyzer());

			File contents = new File(iFolder.getParentFile(), "contentKeyWords");

			if (!contents.exists())
				contents.mkdirs();

			Set<String> keys = files.keySet();

			for (String str : keys) {
				//Map<Enum<?>, FileField> params = files.get(str);

				try {
					if (update) {
						//writer.updateDocument(str, contents, params);
					} else {
						//writer.addDocumentKeyWords(str, contents, params);
					}

					reload = true;
				} catch (Exception ex) {
					falses.add(str);
				}
			}
		} finally {
			// 关闭索引文件
			if (writer != null)
				IndexFactory.getInstance().close(iFolder);
			
			//是否实时加载检索文件
			String realTime = Configurater.getInstance().getConfigValue(
					"realtime_Reaload");
			if (reload && "true".equals(realTime)){
				//SearchFactory.getInstance().reloadAllSearcher();
			}
			for (String str : falses) {
				files.remove(str);
			}
		}
		
		return falses;
	}
	public static void main(String[] args) throws Exception {
		String conditions="北京天安门";
		Map<String, Map<Enum<?>, FileField>> files = new HashMap<String, Map<Enum<?>, FileField>>();
		files.put(conditions, setArchivesIndex(conditions));
		indexingArchive(files,null,false);
	}
}
