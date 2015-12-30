package com.nsc.dem.util.index.abstractor;

import java.io.File;
import java.io.IOException;

import javax.swing.text.BadLocationException;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;

import com.nsc.base.index.ITextAbstractor;

public class Word2007Abstractor extends ITextAbstractor {

	public StringBuffer abstractText(File file) throws IOException,
			BadLocationException, XmlException, OpenXML4JException {
		
		OPCPackage opcPackage = POIXMLDocument.openPackage(file
				.getAbsolutePath());
		POIXMLTextExtractor ex = new XWPFWordExtractor(opcPackage);

		return new StringBuffer(ex.getText());
	}

}
