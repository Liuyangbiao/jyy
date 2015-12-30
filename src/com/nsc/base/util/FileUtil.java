package com.nsc.base.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.util.StringUtils;

import com.nsc.base.jsf.util.FacesUtils;
import com.nsc.base.recource.ResourceLoader;

/**
 * 文件操作类
 * 
 * 此类用于文件的常用操作，比如拷贝、剪切等。
 * 
 * @author bs-team
 * 
 * @date Oct 19, 2010 10:34:53 AM
 * @version
 */
public class FileUtil {

	private static String temp = "";

	static {
		try {
			URL classUrl = FileUtil.class.getResource("");
			temp = classUrl.getPath();
			temp = temp.substring(0, temp.indexOf("classes") + 7);
		} catch (Exception e) {
			temp = "";
		}

	}

	/**
	 * 复制文件
	 * 
	 * @param oldFilePath
	 *            原文件路径
	 * @param newFilePath
	 *            新文件路径
	 */
	public static void copyFile(String oldFilePath, String newFilePath) {
		File oldFile = new File(oldFilePath);
		File newFile = new File(newFilePath);
		File folderPath = new File(newFilePath.substring(0, newFilePath.lastIndexOf("\\")));
		try {
			if (oldFile.exists()) {
				
				//不是目录
				if (!folderPath.isDirectory()) {
					folderPath.mkdirs();
				}
				
				if(newFile.exists()) {
					newFile.delete();
				}
				newFile.createNewFile();
				
				FileInputStream fis = new FileInputStream(oldFile);
				FileOutputStream out = new FileOutputStream(newFile);
				int length = 0;
				byte[] buffer = new byte[1444];
				while ((length = fis.read(buffer)) != -1) {
					out.write(buffer, 0, length);
				}
				fis.close();
				out.close();
			}
		} catch (IOException e) {
			Logger.getLogger(FileUtil.class).warn("文件复制异常:",e);
		}

	}

	/**
	 * sourceFile复制到newFile
	 * 
	 * @param sourceFile
	 *            原文件名称
	 * @param newFile
	 *            新文件名称
	 */
	public static void newFile(File sourceFile, File newFile) {

		try {
			FileInputStream fis = new FileInputStream(sourceFile);
			FileOutputStream out = new FileOutputStream(newFile);
			int bytes = 0;
			int bytesum = 0;
			int byteread = 0;
			byte[] buffer = new byte[1444];
			while ((bytes = fis.read(buffer)) != -1) {
				bytesum += byteread;
				out.write(buffer, 0, bytes);
			}
			fis.close();
			out.close();
		} catch (Exception e) {
			Logger.getLogger(FileUtil.class).warn("文件复制异常:",e);
		}

	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            文件绝对路径
	 */
	public static void deleteFile(String filePathAndName) {
		try {
			File delFile = new File(filePathAndName);
			if (delFile.exists())
				delFile.delete();
		} catch (Exception e) {
			Logger.getLogger(FileUtil.class).warn("删除文件异常:",e);
		}
	}

	/**
	 * 移动文件并将原文件删除
	 * 
	 * @param oldFilePath
	 *            文件原路径
	 * @param newFilePath
	 *            文件新路径
	 */
	public static void moveFile(String oldFilePath, String newFilePath) {
		copyFile(oldFilePath, newFilePath);
		deleteFile(oldFilePath);
	}

	/**
	 * 移动多个文件
	 * 
	 * @param filePaths
	 */
	public static void moveFiles(String filePaths) {
		String basePath;
		String[] filePath;
		String tempFilePath;
		String newFilePath;
		basePath = FacesUtils.getBasePath();
		filePath = filePaths.split(";");
		for (int i = 0; i < filePath.length; i++) {
			tempFilePath = StringUtils.replace(basePath + "temp\\file\\"
					+ filePath[i], "/", "\\");
			newFilePath = StringUtils.replace(basePath + "source\\file\\"
					+ filePath[i], "/", "\\");
			FileUtil.moveFile(tempFilePath, newFilePath);
		}
	}

	/**
	 * 移动文件
	 * 
	 * @param lineCode
	 * @param filePath
	 */
	public static void moveFileTo(String lineCode, String filePath) {
		String basePath;
		String tempFilePath;
		String newFilePath;
		basePath = FacesUtils.getBasePath();
		tempFilePath = StringUtils.replace(
				basePath + "temp\\file\\" + filePath, "/", "\\");
		newFilePath = StringUtils.replace(basePath + "source\\file\\"
				+ lineCode + "\\" + filePath, "/", "\\");

		FileUtil.moveFile(tempFilePath, newFilePath);
	}

	/**
	 * 将文件下载到客户端浏览器
	 * 
	 * @param fileName
	 * @param filePath
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void download(String fileName, String filePath,
			String contentType, boolean inline, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		OutputStream outp = null;
		FileInputStream br = null;

		int len = 0;
		try {
			File source = new File(filePath);
			br = new FileInputStream(source);
			response.reset();
			outp = response.getOutputStream();
			response.setContentType(contentType);
			response.setContentLength((int) source.length());

			String header = (inline ? "inline" : "attachment") + ";filename="
					+ new String(fileName.getBytes(), "ISO8859-1");

			response.addHeader("Content-Disposition", header);

			byte[] buf = new byte[1024];
			while ((len = br.read(buf)) != -1) {
				outp.write(buf, 0, len);
			}

			outp.flush();

		} finally {
			if (br != null)
				br.close();
		}
	}

	/**
	 * 获得文件格式
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		String suffix = FilenameUtils.getExtension(fileName);
		
		Properties p=PropertyReader.getProperties("fileformat.properties");
		return p.getProperty(suffix.toLowerCase(), "UNKNOWN");
	}

	/**
	 * 将文件打包到指定输入流
	 * 
	 * @param files
	 *            被打包的文件
	 * @param out
	 *            被指定输出流
	 * @throws IOException
	 * @throws Exception
	 */
	public static void getPackAgeDownLoad(File[] files, HttpServletResponse response)
			throws IOException {
		
		ServletOutputStream out=response.getOutputStream();
		ZipOutputStream zipout = new ZipOutputStream(out);

		zipout.setLevel(1);

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.canRead()) {
				try {
					String filename = new String(file.getName().getBytes(),
							"GBK");
					zipout.putNextEntry(new ZipEntry(filename));
				} catch (IOException e) {
					Logger.getLogger(FileUtil.class).warn("文件打包时异常:",e);
				}
				BufferedInputStream fr = new BufferedInputStream(
						new FileInputStream(file));
				int b;
				while ((b = fr.read()) != -1)
					zipout.write(b);
				fr.close();
				zipout.closeEntry();
			}
			if (file.exists()) {
				file.delete();
			}
		}

		zipout.finish();
		zipout.flush();
//		out.flush();

	}

	// 获取工程classes目录
	public static String getClassesPath() {
		return temp;
	}

	// 获取工程web-inf目录
	public static String getWebInfPath() {
		return temp.substring(0, temp.lastIndexOf("/"));
	}

	// 获取工程根目录
	public static String getWebRootPath() {
		return getWebInfPath().substring(0, getWebInfPath().lastIndexOf("/"));
	}

	/**
	 * 将文件大小转换成通俗形式
	 * 
	 * @param fileSize
	 *            文件大小
	 * @return
	 */
	public String getHumanSize(long fileSize) {
		// 设置位数
		DecimalFormat df = new DecimalFormat("#.##");
		String[] units = new String[] { "字节", "KB", "MB", "GB" };
		int i = 0;
		double size = fileSize;
		while (size > 102) {
			size = size / 1024;
			i++;
		}
		return (df.format(size)) + units[i];
	}

	/**
	 * 得到系统文件相对路径 path 文件名
	 * 
	 * @throws URISyntaxException
	 */
	public static String relPath(String path) throws URISyntaxException {
		URL docUrl = ResourceLoader.getDefaultClassLoader().getResource(path);
		String docDir = docUrl.toURI().getPath();
		return docDir;
	}
	/**
	 * 
	 * @param str
	 * @return 清除掉所有特殊字符
	 * @throws PatternSyntaxException
	 */
	public static String folderPathFilter(String str) throws PatternSyntaxException {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[:*?\"<>|/\\\\]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		
		return m.replaceAll("_").trim();
	}
}
