package com.nsc.dem.util.index;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.nsc.base.conf.Configurater;
import com.nsc.base.recource.ResourceLoader;

public class FileDirUtils {
	
	
	/**
	 * 获取content\read\write下的目录所有需要加载的目录
	 * 
	 * 
	 * @param dir: global文件中的：doc_read_Dir|doc_write_Dir|doc_content_Dir
	 * @param unitId:用户所属单位ID
	 */
	public static List<File> getAllLoadDirs(String dir, String code) throws URISyntaxException{
		List<String> readFiles = new ArrayList<String>(); 
		String readDir = Configurater.getInstance().getConfigValue(dir);
		// 检索文件存放根目录
		URL readUrl = ResourceLoader.getDefaultClassLoader().getResource(readDir);
		readDir = readUrl.toURI().getPath();
		/*
		 * 如果是国网用户，需要加载本地索引库和同步索引库和24个省公司本地索引库，共加载26个文件
		 *  区域以z开头，省以q开头
		 */
		//08是开发人员调试时使用
		if(code != null && code.trim().length()==4 || code.equals("08")){
			//获取read目录下所有文件
			List<String> fileList = showAllFiles(new File(readDir));
			String pattern = "z+.*local$";
			Pattern pp = Pattern.compile(pattern);
			Matcher m = null;
			//获取所有省公司本地词库目录
			for(String str : fileList){
				m = pp.matcher(str);
				if(m.find()){
					readFiles.add(str);
				}	
			}
			
			//获取本地索引库和同步索引库
			readFiles.add(0,readDir+"/local");
			readFiles.add(1,readDir+"/syn");
			
		 } else if( code != null && code.length() == 6){ 
			File file = new File(readDir+"/z"+code);
			if(!file.exists()){
				file.mkdirs();
			}
			List<String> fileList = showAllFiles(file);
			String pattern = "z+.*q+.*local$";
			Pattern p = Pattern.compile(pattern);
			Matcher m = null;
			//获取下属所有省公司本地词库目录
			for(String str : fileList){
				m = p.matcher(str);
				if(m.find()){
					readFiles.add(str);
				}	
			}
			//获取本地索引库和同步索引库
			readFiles.add(0,readDir+"/z"+code+"/local");
			readFiles.add(1,readDir+"/z"+code+"/syn"); 
			
		}else if(code != null && code.length() == 8){
			File file = new File(readDir+"/z"+code.substring(0,6)+"/q"+code);
			if(!file.exists()){
				file.mkdirs();
			}
			//省公司
			List<String> fileList = showAllFiles(file);
			String pattern = "z+.*q+"+code+".*(local|syn)$";
			Pattern p = Pattern.compile(pattern);
			Matcher m = null;
			for(String str : fileList){
				m = p.matcher(str);
				if(m.find()){
					readFiles.add(str);
				}	
			}
			
		}
		//to File
		List<File> dirs = new ArrayList<File>();
		for(String path : readFiles){
			File file = new File(path);
			if(!file.exists())
				file.mkdirs();
			dirs.add(file);
		}
		return dirs;
	}
	
	
	//获取word目录
	public static File getWordFile() throws URISyntaxException{
		String wordDir = Configurater.getInstance().getConfigValue("doc_word_Dir");
		URL wordUrl = ResourceLoader.getDefaultClassLoader().getResource(wordDir);
		wordDir = wordUrl.toURI().getPath();
		File file = new File(wordDir);
		if(! file.exists()){
			file.mkdirs();
		}
		return file;
	}
	
	
	
	/**
	 * 根据code获取该code对应的检索目录相对位置
	 * 
	 * @param dir  gobal.properties文件中的doc_read_Dir 等
	 * @param code 单位ID
	 * @param folder 文件夹 local或者syn
	 * @return 例如：z080118/q08011801/local
	 * @throws URISyntaxException
	 */
	public static String getDirByUnitId(String dir, String code, String folder) throws URISyntaxException{
		if(code != null && code.trim().length()==4 || code.equals("08")){
			return "/"+folder;
		 } else if( code != null && code.length() == 6){ 		
			return "/z"+code+"/"+folder;
		}else if(code != null && code.length() == 8){
			//省公司
			String zone = code.substring(0,6);
			return "/z"+zone+"/q"+code+"/"+folder;
		}
		
		return null;
	}
	
	
	
	/**
	 * 根据code获取该code对应的检索目录绝对位置
	 * 
	 * @param dir  gobal.properties文件中的doc_read_Dir 等
	 * @param code 单位ID
	 * @param folder 文件夹 local或者syn
	 * @return 例如：c:/seroot/wirte/z080118/q08011801/local
	 * @throws URISyntaxException 
	 * @throws URISyntaxException
	 */
	public static String getRealPathByUnitId(String dir, String code, String folder) throws URISyntaxException{
		String fileDir = Configurater.getInstance().getConfigValue(dir);
		URL docUrl = ResourceLoader.getDefaultClassLoader().getResource(fileDir);
		fileDir = docUrl.toURI().getPath();
		//获取相对部分路径
		String relative = getDirByUnitId(dir, code, folder);	
		return fileDir +  relative;
	}
	
	
	public static String getClassPath() throws URISyntaxException{
		String readDir = Configurater.getInstance().getConfigValue("doc_content_Dir");
		// 检索文件存放根目录
		URL readUrl = ResourceLoader.getDefaultClassLoader().getResource(readDir);
		
		String path = readUrl.toURI().getPath();
		
		return path.substring(0,path.lastIndexOf(readDir));
	}
	
	
	/**
	 * 递归得到一个目录下的所有文件及文件夹
	 * @param dir
	 * @return
	 * @throws Exception
	 */
	private  static List<String> showAllFiles(File dir) {
		  File[] fs = dir.listFiles();
		  List<String> fileNames = new ArrayList<String>();
		  for(int i=0; i<fs.length; i++){
			  fileNames.add(fs[i].getAbsolutePath());
			  if(fs[i].isDirectory()){
		          List<String> list = showAllFiles(fs[i]);
		          for(String str : list){
		        	  fileNames.add(str);
		          }
		      }
		}
		  return fileNames;
	}
	
	/**
	 * 根据write找到read
	 * @param writeDir
	 * @return
	 */
	public static File getReadFileByWriteFile(File writeDir){
		String path = writeDir.getAbsolutePath();
		String regex = File.separator + "seroot" + File.separator+ "dic" + File.separator + "write";
		String replacement = File.separator + "seroot"+ File.separator + "dic" + File.separator + "read";
		path = path.replace(regex, replacement);
		File file = new File(path);
		if(! file.exists()){
			file.mkdirs();
		}
		return file;
	}
}
