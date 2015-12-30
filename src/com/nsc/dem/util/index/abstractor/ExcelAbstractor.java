package com.nsc.dem.util.index.abstractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.nsc.base.index.ITextAbstractor;

public class ExcelAbstractor extends ITextAbstractor {

	public StringBuffer abstractText(File file) throws IOException {
		InputStream inputStream = null;
		
		inputStream = new FileInputStream(file);
		HSSFWorkbook wb = new HSSFWorkbook(inputStream);
		ExcelExtractor extractor = new ExcelExtractor(wb);
		
		extractor.setIncludeBlankCells(false);
		extractor.setIncludeHeadersFooters(false);
		extractor.setFormulasNotResults(true);
		extractor.setIncludeCellComments(true);
		extractor.setIncludeSheetNames(false);
		
		String tempStr=extractor.getText();
		
		if(tempStr.trim().endsWith("...")){
			return new StringBuffer(tempStr.substring(0,tempStr.length()-3));
		}

		return new StringBuffer(tempStr);
	}
}
