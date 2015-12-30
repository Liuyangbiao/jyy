package com.nsc.dem.action.searches;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nsc.base.util.DateUtils;
import com.nsc.base.util.GetCh2Spell;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.RowBean;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.profile.TRole;
import com.nsc.dem.bean.system.TNodeDef;
import com.nsc.dem.bean.system.TRoleTree;
import com.nsc.dem.bean.system.TRoleTreeId;
import com.nsc.dem.bean.system.TTreeDef;
import com.nsc.dem.service.system.IsortSearchesService;

public class ClassLevelConfigAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private int t = 0;
	private List<Map<String, Object>> list;
	private List<TTreeDef> listTreeDef = new ArrayList<TTreeDef>();
	IsortSearchesService sortSearchesService;
	private String remark;
	private String imageUrl;
	private String treeName;
	private String menuId;
	private String treeId;
	private String typeLevelName;
	private String roleNameCode;

	public void setSortSearchesService(IsortSearchesService sortSearchesService) {
		this.sortSearchesService = sortSearchesService;
	}

	/**
	 * 查询所有角色名称
	 */
	public String role() {
		TRole tr = new TRole();
		// ID为选中分类树的ID
		String id = getRequest().getParameter("id");
		// 查询被选择的分类树 已经分配好了的角色
		List<TRoleTree> listRoleTree = sortSearchesService.delRoleTree(id);

		List<Object> tList = sortSearchesService.EntityQuery(tr);
		list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < tList.size(); i++) {
			TRole tRole = (TRole) tList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			boolean f = false;
			boolean isUsable = true;
			// 如果f=true 说明是已经分配好了的角色
			// 如果isUsable=true 说明该角色是可用的
			for (TRoleTree roleTree : listRoleTree) {
				String tname = roleTree.getId().getTRole().getName();
				if (tname.equals(tRole.getName())) {
					f = true;
					if (!(roleTree.getIsUsable())) {
						isUsable = false;
					}
				}
			}
			map.put("code", tRole.getId());
			map.put("name", tRole.getName());
			map.put("spell", GetCh2Spell.getBeginCharacter(tRole.getName()));
			if (f) {
				map.put("existence", "1");
			} else {
				map.put("existence", "0");
			}
			if (isUsable) {
				map.put("isUsable", "1");
			} else {
				map.put("isUsable", "0");
			}
			list.add(map);
		}
		return "list";
	}

	/**
	 * 查询
	 */
	public String show() {

		return "del";
	}

	/**
	 * 对分类等级配置表做删除操作
	 */
	public String del() {
		String ids = getRequest().getParameter("id");
		String[] sIds = ids.split(",");
		for (int i = 0; i < sIds.length; i++) {
			TTreeDef tnd = (TTreeDef) sortSearchesService.EntityQuery(
					TTreeDef.class, Long.parseLong(sIds[i]));
			List<TRoleTree> list = sortSearchesService.delRoleTree(tnd.getId()
					.toString());
			for (TRoleTree roleTree : list) {
				sortSearchesService.delEntity(roleTree);
			}
			this.delRecursive(tnd);
			sortSearchesService.delEntity(tnd);
		}
		return "del";
	}

	// 递归调用 对分类等级配置表做删除操作
	public void delRecursive(TTreeDef tnd) {
		TTreeDef td = new TTreeDef();
		td.setParentId(tnd.getId());
		List<Object> tdList = sortSearchesService.EntityQuery(td);
		if (tdList.size() > 0) {
			for (int i = 0; i < tdList.size(); i++) {
				TTreeDef ttd = (TTreeDef) tdList.get(i);
				sortSearchesService.delEntity(ttd);
				this.delRecursive(ttd);
			}
		} else {
			return;
		}

	}

	/**
	 * 对分类等级配置表某记录更新操作
	 */
	public String edit() {
		String id = getRequest().getParameter("id");
		TTreeDef tTreeDef = (TTreeDef) sortSearchesService.EntityQuery(
				TTreeDef.class, Long.parseLong(id));
		getRequest().setAttribute("tTreeDef", tTreeDef);
		getRequest().setAttribute("treeId", tTreeDef.getId());
		return "edit";
	}

	/**
	 * 判断输入的名字是否存在!
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public String existence() {
		String name = getRequest().getParameter("id");
		list = new ArrayList<Map<String, Object>>();
		if (!("".equals(name) || name == null)) {
			TTreeDef tTreeDef = new TTreeDef();
			List<Object> tTreeDefList = sortSearchesService
					.EntityQuery(tTreeDef);
			for (Object ttd : tTreeDefList) {
				TTreeDef td = (TTreeDef) ttd;
				String treeName = td.getName();
				if (!("".equals(treeName) || treeName == null)) {
					if (treeName.equals(name)) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("message", "该名称已存在,请重新输入!");
						list.add(map);
					}
				}
			}
		}
		return "list";
	}

	/**
	 * 查询文档等级定义表的内容,用于listBox左边显示 如果 listBox右边已经有了的 则不在左边显示
	 */
	public String displayNodeDef() {

		TNodeDef nd = new TNodeDef();
		List<Object> ndList = sortSearchesService.EntityQuery(nd);

		list = new ArrayList<Map<String, Object>>();
		Iterator<Object> it = ndList.iterator();
		while (it.hasNext()) {
			TNodeDef tNodeDef = (TNodeDef) it.next();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", tNodeDef.getId());
			map.put("name", tNodeDef.getName());
			list.add(map);
		}
		return "list";
	}

	// 递归 调用
	public void utilTreeDisplay(TTreeDef td) {
		TTreeDef tTreeDef = new TTreeDef();
		tTreeDef.setParentId(td.getId());
		List<Object> tdList = sortSearchesService.EntityQuery(tTreeDef);
		if (tdList.size() > 0) {
			for (int i = 0; i < tdList.size(); i++) {
				TTreeDef tree = (TTreeDef) tdList.get(i);
				listTreeDef.add(tree);
				this.utilTreeDisplay(tree);
			}
		} else {
			return;
		}
	}

	/**
	 * 查询文档等级配置表下 某已棵树的所有节点
	 */
	public String displayTreeDef() {
		String id = getRequest().getParameter("id");
		TTreeDef td = (TTreeDef) sortSearchesService.EntityQuery(
				TTreeDef.class, Long.parseLong(id));
		list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", td.getTNodeDef().getId());
		map.put("name", "*" + td.getTNodeDef().getName());
		list.add(map);
		this.utilTree(td);

		return "list";
	}

	// 用来输出树节点前面的空格
	public String space(int t) {
		String s = "";
		for (int i = 0; i < t; i++) {
			s = s + "      ";
		}
		return s;
	}

	// 递归 调用
	public void utilTree(TTreeDef td) {
		TTreeDef tTreeDef = new TTreeDef();
		tTreeDef.setParentId(td.getId());
		List<Object> tdList = sortSearchesService.EntityQuery(tTreeDef);
		if (tdList.size() > 0) {
			TTreeDef tree;
			for (int i = 0; i < tdList.size(); i++) {
				t = 0;
				tree = (TTreeDef) tdList.get(i);
				spaceName(tree);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", tree.getTNodeDef().getId());
				map.put("name", space(t) + "*" + tree.getTNodeDef().getName());
				list.add(map);
				this.utilTree(tree);
			}
		} else {
			return;
		}
	}

	// 根据t的值来判断是树的第几层 节点
	public void spaceName(TTreeDef tree) {
		long parentId = tree.getParentId();
		if (parentId != 0) {
			t = t + 1;
			TTreeDef tTreeDef = (TTreeDef) sortSearchesService.EntityQuery(
					TTreeDef.class, parentId);
			this.spaceName(tTreeDef);
		}
	}

	/**
	 * 分类等级配置----保存
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public String save() throws UnsupportedEncodingException {

		List<TRoleTree> tRoleTree = new ArrayList<TRoleTree>();
		List<TRoleTree> tRoleTreeTemp = new ArrayList<TRoleTree>();
		Map<String, String> hm = new HashMap<String, String>();
		// 如果是编辑，则先把原始的数据删除
		if (!(treeId == null || "".equals(treeId))) {
			TTreeDef tree = (TTreeDef) sortSearchesService.EntityQuery(
					TTreeDef.class, Long.parseLong(treeId));

			tRoleTree = sortSearchesService.delRoleTree(treeId);

			delRecursive(tree);
		}
		String[] menuIds = menuId.split(",");
		long id = 0;
		for (int i = 0; i < menuIds.length; i++) {
			TTreeDef td = new TTreeDef();
			String mid = menuIds[i].substring(1);
			int num = Integer.parseInt(menuIds[i].substring(0, 1));
			if (i == 0) {
				td.setName(treeName);
				td.setImageUrl(imageUrl);
				td.setParentId(0l);
				td.setRemark(remark);
				td.setCreateDate(new Date());
				td.setTUser(getLoginUser());
				TNodeDef tNodeDef = (TNodeDef) sortSearchesService.EntityQuery(
						TNodeDef.class, Long.parseLong(mid));
				td.setTNodeDef(tNodeDef);
			} else {
				id = Long.parseLong(hm.get((num - 1) + ""));
				TTreeDef tTreeDef = (TTreeDef) sortSearchesService.EntityQuery(
						TTreeDef.class, id);
				td.setParentId(tTreeDef.getId());
				td.setCreateDate(new Date());
				td.setTUser(getLoginUser());
				TNodeDef tNodeDef = (TNodeDef) sortSearchesService.EntityQuery(
						TNodeDef.class, Long.parseLong(mid));
				td.setTNodeDef(tNodeDef);
			}
			if (td.getParentId() == 0 && treeId.length() != 0) {
				td.setId(Long.valueOf(treeId));
				sortSearchesService.updateEntity(td);
			} else {
				sortSearchesService.insertEntity(td);
			}
			if (treeId == null || treeId.length() == 0) {
				// 保存---测试画面时先不保存
				if (tRoleTree.size() > 0 && i == 0) {
					TRoleTree trt = tRoleTree.get(0);
					trt.getId().setTTreeDef(td);
					tRoleTreeTemp.add(trt);
				}
			}

			hm.put("" + num, td.getId() + "");
		}
		if (treeId == null || treeId.length() == 0) {
			// 重新保存角色
			if (tRoleTreeTemp.size() > 0) {
				for (TRoleTree t : tRoleTreeTemp) {
					sortSearchesService.insertEntity(t);
				}
			}
		}

		return "list";
	}

	/**
	 * 分类等级配置----保存 角色映射
	 * 
	 * @return
	 */
	public String saveRoleTree() {
		// 树id
		String treeId = getRequest().getParameter("id");
		// 角色ID
		String bn = getRequest().getParameter("bn");
		// 是否可用
		String bf = getRequest().getParameter("bf");
		String[] roleids = bn.split(",");
		String[] bflags = bf.split(",");
		// 删除选中的树下的所有 记录
		List<TRoleTree> list = sortSearchesService.delRoleTree(treeId);
		for (TRoleTree roleTree : list) {
			sortSearchesService.delEntity(roleTree);
		}
		if (!"".equals(bn)) {
			// 循环添加
			for (String roleid : roleids) {
				boolean bl = false; // 是否可用
				TRoleTree tRoleTree = new TRoleTree();
				tRoleTree.setCreateDate(new Date());
				tRoleTree.setTUser(getLoginUser());
				for (String bflag : bflags) {
					if (roleid.equals(bflag)) {
						bl = true;
					}
				}
				tRoleTree.setIsUsable(bl);
				TTreeDef tTreeDef = (TTreeDef) sortSearchesService.EntityQuery(
						TTreeDef.class, Long.parseLong(treeId));
				TRole tRole = (TRole) sortSearchesService.EntityQuery(
						TRole.class, roleid);
				TRoleTreeId tRoleTreeId = new TRoleTreeId();
				tRoleTreeId.setTRole(tRole);
				tRoleTreeId.setTTreeDef(tTreeDef);
				tRoleTree.setId(tRoleTreeId);
				tRoleTree.setTreeIdOrder("1"); // 暂时默认
				sortSearchesService.insertEntity(tRoleTree);
			}
		}
		return "list";
	}

	/**
	 * 分类等级配置主页面，分页显示
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String display() throws Exception {
		int firstResult = (page - 1) * rows;
		int maxResults = page * rows;
		typeLevelName = java.net.URLDecoder.decode(getRequest().getParameter(
				"levelName"), "UTF-8");// 分类列表名称
		roleNameCode = getRequest().getParameter("roleCode"); // 角色名称 Code
		List<Object[]> list = new ArrayList<Object[]>();
		if (roleNameCode == null || "".equals(roleNameCode)) {
			list = sortSearchesService.queryTreeDefList(new Object[] { "%"
					+ typeLevelName + "%" }, firstResult, maxResults,
					tablebean, "treeDefSearch");
		} else {
			list = sortSearchesService.queryTreeDefList(new Object[] {
					"%" + typeLevelName + "%", "%" + roleNameCode + "%" },
					firstResult, maxResults, tablebean, "treeDefRoleSearch");
		}

		List rowsList = new ArrayList();
		if (list != null) {
			for (Object[] objs : list) {
				TTreeDef tn = (TTreeDef) objs[0];
				RowBean rowbean = new RowBean();
				rowbean
						.setCell(new Object[] {
								tn.getName(),
								tn.getRemark(),
								tn.getTNodeDef().getName(),
								tn.getTUser().getName(),
								DateUtils.DateToString(tn.getCreateDate()),
								"<a href='#' id='dialog_link' onclick='edit2("
										+ tn.getId()
										+ ")'>更新</a>|<a href='#' id='dialog_link' onclick='roleAssign("
										+ tn.getId() + ")'>角色分配</a> " });
				rowbean.setId(tn.getId() + "");
				rowsList.add(rowbean);
			}
		}

		tablebean.setRows(rowsList);
		tablebean.setPage(this.page);
		int records = tablebean.getRecords();
		tablebean.setTotal(records % rows == 0 ? records / rows : records
				/ rows + 1);
		return "tab";
	}

	// 默认的页数
	private int page;

	public void setPage(int page) {
		this.page = page;
	}

	private int rows;

	public void setRows(int rows) {
		this.rows = rows;
	}

	TableBean tablebean = new TableBean();

	public TableBean getTablebean() {
		return tablebean;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public String getTreeName() {
		return treeName;
	}

	public void setTreeName(String treeName) {
		this.treeName = treeName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getTreeId() {
		return treeId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public String getTypeLevelName() {
		return typeLevelName;
	}

	public void setTypeLevelName(String typeLevelName) {
		this.typeLevelName = typeLevelName;
	}

	public String getRoleNameCode() {
		return roleNameCode;
	}

	public void setRoleNameCode(String roleNameCode) {
		this.roleNameCode = roleNameCode;
	}

}
