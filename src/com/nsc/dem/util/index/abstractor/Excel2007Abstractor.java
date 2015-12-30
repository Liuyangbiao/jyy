package com.nsc.dem.util.index.abstractor;

import java.io.File;
import java.io.IOException;

import javax.swing.text.BadLocationException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.xmlbeans.XmlException;

import com.nsc.base.index.ITextAbstractor;

public class Excel2007Abstractor extends ITextAbstractor {

	public StringBuffer abstractText(File file) throws IOException,
			BadLocationException, XmlException, OpenXML4JException {
		XSSFWorkbook xwb = new XSSFWorkbook(file.getAbsolutePath());
		
		XSSFExcelExtractor extractor=new XSSFExcelExtractor(xwb);
		
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
	
	/**
	 * // 循环工作表Sheet
		for (int numSheet = 0; numSheet < xwb.getNumberOfSheets(); numSheet++) {
			XSSFSheet xSheet = xwb.getSheetAt(numSheet);
			if (xSheet == null) {
				continue;
			}

			// 循环行Row
			for (int rowNum = 0; rowNum <= xSheet.getLastRowNum(); rowNum++) {
				XSSFRow xRow = xSheet.getRow(rowNum);
				if (xRow == null) {
					continue;
				}

				// 循环列Cell
				for (int cellNum = 0; cellNum <= xRow.getLastCellNum(); cellNum++) {
					XSSFCell xCell = xRow.getCell(cellNum);
					if (xCell == null) {
						continue;
					}

					if (xCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
						content.append(xCell.getBooleanCellValue());
					} else if (xCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
						content.append(xCell.getNumericCellValue());
					} else {
						content.append(xCell.getStringCellValue());
					}
				}
			}
		}

		return content;
	 */
}
