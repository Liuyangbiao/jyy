package com.nsc.base.index;

import org.apache.lucene.analysis.Analyzer;

public class AnalyzerFactory extends Factory<Analyzer> {
	
	static String ANALYZER_CONFIG="words_Analyzer";
	
	public static AnalyzerFactory getInstance(){
		return new AnalyzerFactory();
	}
	
	public Analyzer getAnalyzer() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		return (Analyzer)super.getImplement(ANALYZER_CONFIG);
	}
}
