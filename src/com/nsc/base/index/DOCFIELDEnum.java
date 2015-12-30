package com.nsc.base.index;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

public enum DOCFIELDEnum {
	/** 文档号 */
	docid(new FileField("docid", null, null, true, true, 1f, false,1)),
	/** 标题 */
	title(new FileField("title", null, null, true, true, 0.9f, true,3)),
	/** 主题 */
	subject(new FileField("subject", null, null, true, true, 0.8f, false,3)),
	/** 文档类别 */
	doctype(new FileField("doctype", null, null, true, true, 0.7f, false,1)),
	/** 关键词 */
	keyword(new FileField("keyword", null, null, true, true, 0.6f, false,3)),
	/** 作者 */
	author(new FileField("author", null, null, true, true, 0.9f, false,1)),
	/** 来源 */
	resource(new FileField("resource", null, null, true, true, 0.9f, false,1)),
	/** 版本 */
	version(new FileField("version", null, null, true, true, 0.9f, false,1)),
	/** 创建时间 */
	cdate(new FileField("cdate", null, null, true, true, 0.5f, false,3)),
	/** 档案状态 */
	status(new FileField("status", null, null,true, true,0.9f,false,1)),
	/**路径*/
	url(new FileField("url", null, null, true, true, 0.1f, false, 1)),
	/**后缀*/
	suffix(new FileField("suffix", null, null, true, true, 0.1f, false, 1));
	
	
	
	/**
	 * 构造函数
	 * 
	 * @param f
	 */
	DOCFIELDEnum(FileField f) {
		Logger.getLogger(DOCFIELDEnum.class).info(f.getName() + " " + f.hashCode());
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
	public static DOCFIELDEnum getValue(String name) {

		try {
			return valueOf(name);
		} catch (Exception ex) {
			return null;
		}
	}
}
