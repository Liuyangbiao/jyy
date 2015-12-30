package com.nsc.base.index;


public enum FIELDEnum{
	
	path(new FileField("path", null, null, true, true,0f,false,3)),
	modified(new FileField("modified", null, null,true, true,0.1f,false,3)),
	contents(new FileField("contents", null, null,true, true,0.5f,true,3)),
	cpath(new FileField("cpath", null, null, true, false,0f,false,3)),
	format(new FileField("format", null, null,true, true,0.9f,true,1)),
	size(new FileField("size", null, null,true, true,0f,false,1));

	FIELDEnum(FileField f){
		this.f=f;
	}
	
	public final FileField f;
	
	/**
	 * 根据字符串取得枚举值
	 * @param name
	 * @return
	 */
	public static FIELDEnum getValue(String name){
		
		try{
			return valueOf(name);
		}catch(Exception ex){
			return null;
		}
	}
}
