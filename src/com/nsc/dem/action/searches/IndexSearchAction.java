package com.nsc.dem.action.searches;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.store.RAMDirectory;

import com.nsc.base.index.AnalyzerFactory;
import com.nsc.base.index.DOCFIELDEnum;
import com.nsc.base.index.FileField;
import com.nsc.base.index.IIndexWriter;
import com.nsc.base.index.IndexFactory;
import com.nsc.base.util.GetCh2Spell;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.action.bean.WholeSearchDoc;
import com.nsc.dem.service.searches.IsearchesService;
import com.nsc.dem.util.index.RAMDirectoryStore;

public class IndexSearchAction extends BaseAction {

	private String conditions;
	private boolean highLighter = true;

	private IsearchesService searchesService;
	private List<WholeSearchDoc> docList;
	private List<Map<String, Object>> list;
	private int totals;
	private int rows;
	private int records;
	private int page = 1;
	private String menuId;
	private String test;

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getTotals() {
		return totals;
	}

	public List<WholeSearchDoc> getDocList() {
		return docList;
	}

	public void setSearchesService(IsearchesService searchesService) {
		this.searchesService = searchesService;
	}

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions)
			throws UnsupportedEncodingException {
		this.conditions = conditions;
	}

	public boolean isHighLighter() {
		return !highLighter;
	}

	/**
	 * 设置是否高亮
	 * 
	 * @param highLighter
	 *            选中时struts2调用此方法，但传入false.
	 */
	public void setHighLighter(boolean highLighter) {
		this.highLighter = highLighter;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2860400512716915880L;

	/**
	 * 通过全文检索获取文件列表
	 * 
	 * @return
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws URISyntaxException
	 * @throws ParseException
	 * @throws InvalidTokenOffsetsException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public String doSESearch() throws Exception {

		TableBean tb = new TableBean();
		tb.setPage(page);
		tb.setRecords(rows);

		Map<Enum<?>, Object> params = new HashMap<Enum<?>, Object>();

		Map<Enum<?>, Object> filters = new HashMap<Enum<?>, Object>();
		
		filters.put(DOCFIELDEnum.status, "01");

		
		String unitId = super.getLoginUser().getTUnit().getProxyCode();
		
		docList = searchesService.searchArchive(conditions, params, filters,
				tb, !this.highLighter, unitId);
		if (null != docList && docList.size() > 0) {
			this.doTest();
		}
		this.records = tb.getTotal();
		this.totals = records % rows == 0 ? records / rows : records / rows+1 ;
		
		return this.totals == 0 ? "failse" : SUCCESS;
	}

	public String suggest() throws Exception {
		list = new ArrayList<Map<String, Object>>();
		Map<Enum<?>, Object> params = new HashMap<Enum<?>, Object>();
		Map<Enum<?>, Object> filters = new HashMap<Enum<?>, Object>();
		String ids = getRequest().getParameter("ids");
		
		List<String> keyList = searchesService.searchSuggest(ids, params,
				filters, !this.highLighter,null);
		if (keyList == null) {
			return SUCCESS;
		}
		for (int i = 0; i < keyList.size(); i++) {
			String content = keyList.get(i);

			// 必须以检索文字开始，且长度大于检索文件
			if (content.startsWith(ids) && ids.length() < content.length()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", content);
				map.put("name", ids + "<b>" + content.substring(ids.length())
						+ "<b>");
				map.put("spell", GetCh2Spell.getBeginCharacter(content));
				list.add(map);
			}
		}
		return SUCCESS;
	}

	public void doTest() throws Exception {
		Map<Enum<?>, FileField> params = new HashMap<Enum<?>, FileField>();
		DOCFIELDEnum keyword = DOCFIELDEnum.keyword;
		keyword.setValue(conditions.replaceAll(" ", ""));
		params.put(keyword, keyword.getF());
		IIndexWriter writer = null;
		try {
			writer = IndexFactory.getInstance().getIndexWriter(
					AnalyzerFactory.getInstance().getAnalyzer());

			writer.addKeyWords(keyword);

			RAMDirectoryStore.addRAM((RAMDirectory) writer.getDirectory());
		} finally {
			// 关闭索引文件
			if (writer != null)
				writer.closeWriter();
		}
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public int getRecords() {
		return records;
	}
	
	

}
