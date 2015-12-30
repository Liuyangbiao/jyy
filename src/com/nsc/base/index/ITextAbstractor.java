package com.nsc.base.index;

import java.io.File;

import org.directwebremoting.util.Logger;

public abstract class ITextAbstractor extends Thread{
	protected boolean finish = false;
	protected StringBuffer buffer;
	protected File file;
	protected abstract StringBuffer abstractText(File file) throws Exception;
	
	
	public void run() {
		try{
			//如果线程没有关闭
			this.buffer = abstractText(this.file);
		}catch (Exception e) {
			Logger.getLogger(ITextAbstractor.class).warn(e.getMessage());
			this.buffer = null;
			this.finish = true;
			return;
		}
		this.finish = true;
	}
	
	public void setFile(File file){
		this.file = file;
	}
	
	public StringBuffer getText() {
		return this.buffer;
	}

	public boolean isFinish() {
		return this.finish;
	}
	
}
