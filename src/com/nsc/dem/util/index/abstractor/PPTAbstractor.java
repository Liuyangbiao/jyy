package com.nsc.dem.util.index.abstractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;

import com.nsc.base.index.ITextAbstractor;

public class PPTAbstractor extends ITextAbstractor {

	public StringBuffer abstractText(File file) throws IOException {
		StringBuffer content = new StringBuffer("");

		SlideShow ss = new SlideShow(new HSLFSlideShow(
				new FileInputStream(file)));// is

		// 为文件的InputStream，建立SlideShow
		Slide[] slides = ss.getSlides();// 获得每一张幻灯片
		for (int i = 0; i < slides.length; i++) {
			TextRun[] t = slides[i].getTextRuns();// 为了取得幻灯片的文字内容，建立TextRun
			for (int j = 0; j < t.length; j++) {
				content.append(t[j].getText());// 这里会将文字内容加到content中去
			}
		}

		return content;
	}
}
