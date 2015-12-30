package com.nsc.dem.util.index.abstractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;

import com.nsc.base.index.ITextAbstractor;

public class WordAbstractor extends ITextAbstractor {

	public StringBuffer abstractText(File file) throws IOException {
		StringBuffer content = new StringBuffer("");// 文档内容

		HWPFDocument doc = new HWPFDocument(new FileInputStream(file));
		Range range = doc.getRange();
		int paragraphCount = range.numParagraphs();// 段落
		for (int i = 0; i < paragraphCount; i++) {// 遍历段落读取数据
			Paragraph pp = range.getParagraph(i);
			content.append(pp.text().replaceAll("", "")
									.replaceAll("", "")
									.replaceAll("", "")
									.replaceAll("", "")
									.replaceAll("", "")
									.replaceAll("", ""));
		}
		return content;
	}
}
