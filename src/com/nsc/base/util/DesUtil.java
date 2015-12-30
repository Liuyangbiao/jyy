package com.nsc.base.util;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;

public class DesUtil{
	
	//得到项目web-info目录
	public static String getAppPath(){
    	String path =""; 
		try {
			path = DesUtil.class.getResource("").toURI().getPath();
		} catch (Exception e) {
			Logger.getLogger(DesUtil.class).warn("得到项目web-info目录发生异常:",e);
		}
    	path=path.substring(0,path.indexOf("classes"));
    	
		return path;
	}
	
	/**
	 * 得到文件名前的路径
	 * @param filePath
	 * @return
	 */
	public static String getFilePath(String filePath){
		String temp[] = filePath.replaceAll("\\\\","/").split("/");
		String fileName = "";
		if(temp.length > 1){
		    fileName = temp[temp.length - 1];
		}
		int i = filePath.indexOf(fileName);
		filePath = filePath.substring(0, i);

		return filePath;
	}
	
	/**
	 * 得到文件名前的路径
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath){
		String temp[] = filePath.replaceAll("\\\\","/").split("/");
		String fileName = "";
		if(temp.length > 1){
		    fileName = temp[temp.length - 1];
		}
		return fileName;
	}	

	/**
	 * 把成生的一对密钥保存到DesKey.xml文件中
	 */
	public static void saveDesKey() {
		try {
			SecureRandom sr = new SecureRandom();
			// 为我们选择的DES算法生成一个KeyGenerator对象
			KeyGenerator kg = KeyGenerator.getInstance("DES");
			kg.init(sr);
	    	String path = getAppPath();
			FileOutputStream fos = new FileOutputStream(path+"DesKey.xml");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			// 生成密钥
			Key key = kg.generateKey();
			oos.writeObject(key);
			oos.close();
		} catch (Exception e) {
			Logger.getLogger(DesUtil.class).warn("把成生的一对密钥保存到DesKey.xml文件中发生异常:",e);
		}
	}

	/**
	 * 获得DES加密的密钥。在交易处理的过程中应该定时更 换密钥。需要JCE的支持，如果jdk版本低于1.4，则需要
	 * 安装jce-1_2_2才能正常使用。
	 * 
	 * @return Key 返回对称密钥
	 */
	public static Key getKey() {
		Key kp = null;
		try {
			String path = getAppPath();
 			String fileName = path+"DesKey.xml";
			InputStream is = new FileInputStream(fileName);
			ObjectInputStream oos = new ObjectInputStream(is);
			kp = (Key) oos.readObject();
			oos.close();
		} catch (Exception e) {
			Logger.getLogger(DesUtil.class).warn("获得DES加密的密钥异常:",e);
		}
		return kp;
	}

	// 文件采用DES算法加密文件
	/**
	 * 文件file进行加密并保存目标文件destFile中
	 * 
	 * @param file
	 *            要加密的文件 如c:/test/srcFile.txt
	 * @param destFile
	 *            加密后存放的文件名 如c:/加密后文件.txt
	 */
	public static void encrypt(String file, String destFile) throws Exception {
		File targetFile=new File(destFile);
		File encryptFolder=targetFile.getParentFile();
		
		if(!encryptFolder.isDirectory())
			encryptFolder.mkdirs();
		
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, getKey());
		InputStream is = new FileInputStream(file);
		OutputStream out = new FileOutputStream(destFile);
		CipherInputStream cis = new CipherInputStream(is, cipher);
		byte[] buffer = new byte[1024];
		int r;
		while ((r = cis.read(buffer)) > 0) {
			out.write(buffer, 0, r);
		}
		cis.close();
		is.close();
		out.flush();
		out.close();
	}

	// 　文件采用DES算法解密文件
	/**
	 * 文件file进行加密并保存目标文件destFile中
	 * 
	 * @param file
	 *            已加密的文件 如c:/加密后文件.txt
	 * @param destFile
	 *            解密后存放的文件名 如c:/ test/解密后文件.txt
	 */
	public static void decrypt(String file, String dest) throws Exception {
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, getKey());
		InputStream is = new FileInputStream(file);
		OutputStream out = new FileOutputStream(dest);
		CipherOutputStream cos = new CipherOutputStream(out, cipher);
		byte[] buffer = new byte[1024];
		int r;
		while ((r = is.read(buffer)) >= 0) {
			cos.write(buffer, 0, r);
		}
		cos.flush();
		cos.close();
		out.flush();
		out.close();
		is.close();
	}

}
