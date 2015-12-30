package com.nsc.dem.service.system;

import java.util.List;

import com.nsc.dem.bean.system.TRoleTree;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.service.base.IService;

public interface IsortSearchesService extends IService{
	
	public List<Object[]> querySortList(Object[] obj,int firstResult,int maxResults,TableBean table) throws Exception;
	public List<Object[]> queryTreeDefList(Object[] obj,int firstResult,int maxResults,TableBean table,String m) throws Exception;
	public List<TRoleTree> delRoleTree(String treeId);
}
