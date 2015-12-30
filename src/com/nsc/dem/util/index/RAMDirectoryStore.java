package com.nsc.dem.util.index;

import java.util.Stack;

import org.apache.lucene.store.RAMDirectory;

/**
 * ÄÚ´æË÷Òý¶ÑÕ»´æ´¢Àà
 * @author Administrator
 *
 */
public class RAMDirectoryStore {
	private static Stack<RAMDirectory> RAMs=new Stack<RAMDirectory>();
	
	public synchronized static void addRAM(RAMDirectory ram){
		RAMs.add(ram);
	}
	
	public synchronized static RAMDirectory getRAM(){
		return RAMs.isEmpty()?null:RAMs.pop();
	}
}
