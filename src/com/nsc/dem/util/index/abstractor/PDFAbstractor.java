package com.nsc.dem.util.index.abstractor;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.nsc.base.index.ITextAbstractor;

public class PDFAbstractor extends ITextAbstractor {

	public StringBuffer abstractText(File file) throws IOException {
		PDDocument pdfDoc=PDDocument.load(file);
		int pages=pdfDoc.getNumberOfPages();
		PDFTextStripper ts = new PDFTextStripper();
		if(pages>20){
			ts.setStartPage(7);
			ts.setEndPage(20);
		}else{
			ts.setStartPage(1);
			ts.setEndPage(pages);
		}
		StringWriter writer=new StringWriter();
		
		ts.writeText(pdfDoc, writer);
		
		pdfDoc.close();
		
		Logger.getLogger(PDFAbstractor.class).info(writer.getBuffer());
		
		return writer.getBuffer();
	}
}
