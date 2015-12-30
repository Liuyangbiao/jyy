package com.nsc.dem.util.index;

import java.io.File;
import java.util.Date;


/**
 * 保存索引上一次修改时间
 *   用于上传索引时使用
 */
public class StoreLastModifyTime {
	private static long  local;//read下local
	private static long  syn;//read下syn
	private static long  localContent;//content下local
	private static long  synContent;//content下syn
	private static StoreLastModifyTime instance ;
	private StoreLastModifyTime(){};
	
	public static synchronized StoreLastModifyTime createInstance(){
		if(instance == null)
			instance = new StoreLastModifyTime();
		return instance;
	}
	
	public  long getLocal() {
		return local;
	}

	public  void setLocal(long local) {
		StoreLastModifyTime.local = local;
	}

	public  long getSyn() {
		return syn;
	}

	public  void setSyn(long syn) {
		StoreLastModifyTime.syn = syn;
	}

	public long getLocalContent() {
		return localContent;
	}

	public void setLocalContent(long localContent) {
		StoreLastModifyTime.localContent = localContent;
	}

	public long getSynContent() {
		return synContent;
	}

	public void setSynContent(long synContent) {
		StoreLastModifyTime.synContent = synContent;
	}
}


