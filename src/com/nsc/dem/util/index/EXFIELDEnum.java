package com.nsc.dem.util.index;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.nsc.base.index.FileField;

public enum EXFIELDEnum {
	/** 公司号 */
	companyid(new FileField("companyid", null, null, true, true, 0.9f, false,1)),
	/** 公司名称 */
	company(new FileField("company", null, null, true, true, 0.9f, false,1)),
	/** 项目号 */
	projectid(new FileField("projectid", null, null, true, true, 0.9f, false,1)),
	/** 项目名称 */
	project(new FileField("project", null, null, true, true, 0.9f, false,1)),
	/** 阶段 */
	pharase(new FileField("pharase", null, null, true, true, 0.9f, false,1)),
	/** 工程类型 */
	proType(new FileField("proType", null, null, true, true, 0.9f, false,1)),
	/** 初设单位*/
	designer(new FileField("designer", null, null, true, true, 0.9f, false,1));

	/**
	 * 构造函数
	 * 
	 * @param f
	 */
	EXFIELDEnum(FileField f) {
		this.f = f;
	}

	/**
	 * 设置内容
	 * 
	 * @param v
	 */
	public void setValue(String v) {
		this.f.setContent(v);
	}

	private final FileField f;

	public FileField getF() throws IllegalAccessException,
			InstantiationException, InvocationTargetException,
			NoSuchMethodException {
		return (FileField) BeanUtils.cloneBean(this.f);
	}

	/**
	 * 根据字符串取得枚举值
	 * 
	 * @param name
	 * @return
	 */
	public static EXFIELDEnum getValue(String name) {

		try {
			return valueOf(name);
		} catch (Exception ex) {
			return null;
		}
	}
}
