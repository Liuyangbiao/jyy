package com.nsc.dem.action.archives;

import java.util.List;

import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.DictionaryBean;
import com.nsc.dem.bean.archives.TDocType;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.service.archives.IarchivesService;
import com.nsc.dem.service.base.IService;
import com.nsc.dem.service.system.IdictionaryService;

/**
 * 档案管理类中的下拉列表框
 * @author ibm
 *
 */
public class DocTypeDicAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3851318031479777746L;

	// 文档的父节点
	private List<TDocType> listDoc;

	public List<TDocType> getListDoc() {
		return listDoc;
	}

	// 返回的list
	private List<List<DictionaryBean>> searchList;

	public List<List<DictionaryBean>> getSearchList() {
		return searchList;
	}

	// 格式分类
	List<TDictionary> formartList;

	public List<TDictionary> getFormartList() {
		return formartList;
	}

	public void setFormartList(List<TDictionary> formartList) {
		this.formartList = formartList;
	}

	public void setSearchList(List<List<DictionaryBean>> searchList) {
		this.searchList = searchList;
	}

	// 专业分类
	List<TDictionary> dictionaryList;

	// 保密
	List<TDictionary> dftSecurityList;

	List<TDictionary> docNameList;

	public List<TDictionary> getDocNameList() {
		return docNameList;
	}

	public void setDocNameList(List<TDictionary> docNameList) {
		this.docNameList = docNameList;
	}

	public List<TDictionary> getDftSecurityList() {
		return dftSecurityList;
	}

	public void setDftSecurityList(List<TDictionary> dftSecurityList) {
		this.dftSecurityList = dftSecurityList;
	}

	public List<TDictionary> getDictionaryList() {
		return dictionaryList;
	}

	public void setDictionaryList(List<TDictionary> dictionaryList) {
		this.dictionaryList = dictionaryList;
	}

	public void setListDoc(List<TDocType> listDoc) {
		this.listDoc = listDoc;
	}

	IarchivesService archivesService;

	public void setArchivesService(IarchivesService archivesService) {
		this.archivesService = archivesService;
	}

	IService baseService;

	public void setBaseService(IService baseService) {
		this.baseService = baseService;
	}

	// 取文档信息
	private TDocType tdocType = new TDocType();

	public TDocType getTdocType() {
		return tdocType;
	}

	public void setTdocType(TDocType tdocType) {
		this.tdocType = tdocType;
	}

	IdictionaryService dictionaryService;

	public void setDictionaryService(IdictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;

	}

	// 文件状态
	private List<TDictionary> fileStatusList;
	private List<TDictionary> proClassList;
	private List<TDictionary> proStatusList;
	// 单位类型
	private List<TDictionary> unitTypeList;
	private List<TDictionary> voltageLevelList;
	public List<TDictionary> getVoltageLevelList() {
		return voltageLevelList;
	}

	public void setVoltageLevelList(List<TDictionary> voltageLevelList) {
		this.voltageLevelList = voltageLevelList;
	}

	public List<TDictionary> getUnitTypeList() {
		return unitTypeList;
	}

	public void setUnitTypeList(List<TDictionary> unitTypeList) {
		this.unitTypeList = unitTypeList;
	}

	public List<TDictionary> getFileStatusList() {
		return fileStatusList;
	}

	public void setFileStatusList(List<TDictionary> fileStatusList) {
		this.fileStatusList = fileStatusList;
	}

	public List<TDictionary> getProClassList() {
		return proClassList;
	}

	public void setProClassList(List<TDictionary> proClassList) {
		this.proClassList = proClassList;
	}

	public List<TDictionary> getProStatusList() {
		return proStatusList;
	}

	public void setProStatusList(List<TDictionary> proStatusList) {
		this.proStatusList = proStatusList;
	}

	/**
	 * 获得文档信息
	 * @return
	 */
	public String getSearchTdocAction() {
		return SUCCESS;
	}

}
