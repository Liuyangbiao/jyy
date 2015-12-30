package com.nsc.base.index;

import java.io.IOException;
import java.net.URISyntaxException;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.LockObtainFailedException;

import com.nsc.dem.util.index.IndexSearchManager;

public class SearchFactory extends Factory<IIndexSearcher> {

	private static final String INDEX_SEARCHER = "index_Searcher";
	private static SearchFactory searchFactory;

	
	/**
	 * 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws LockObtainFailedException 
	 * @throws CorruptIndexException 
	 * 
	 */
	public static SearchFactory getInstance() throws IllegalAccessException {
		if (searchFactory == null) {
			searchFactory = new SearchFactory();
		}
		return searchFactory;
	}
	
	private SearchFactory(){
	}

	/**
	 * 取得索引对象
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws LockObtainFailedException 
	 * @throws CorruptIndexException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * 
	 */
	public IIndexSearcher getIndexSearcher(Analyzer analyzer, String unitId) throws CorruptIndexException, LockObtainFailedException, IOException, URISyntaxException, ClassNotFoundException, InstantiationException, IllegalAccessException{

		IIndexSearcher indexSearcher;
		indexSearcher = super.getImplement(INDEX_SEARCHER);
		if(null != indexSearcher){
			IndexSearcher[] indexSearches = IndexSearchManager.getInstance().getIndexSearcher(unitId);
			if(indexSearches != null && indexSearches.length > 0){
				indexSearcher.initIndex(indexSearches,analyzer);
				return indexSearcher;
			}else{
				return null;
			}
		}
		return null;
	}
}
