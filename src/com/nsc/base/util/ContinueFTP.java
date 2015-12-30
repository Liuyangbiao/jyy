package com.nsc.base.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;

import com.nsc.base.conf.Configurater;

/**
 * 支持断点续传的FTP实用类
 * 
 * @author
 * @version 0.1 实现基本断点上传下载
 * @version 0.2 实现上传下载进度汇报
 * @version 0.3 实现中文目录创建及中文文件创建，添加对于中文的支持
 */
public class ContinueFTP {
	public FTPClient ftpClient = new FTPClient();
	private static ContinueFTP ftp;
	private static ContinueFTP downLoadFtp;

	public ContinueFTP() {
		// 设置将过程中使用到的命令输出到控制台
		this.ftpClient.addProtocolCommandListener(new PrintCommandListener(
				new PrintWriter(System.out)));

	}

	/**
	 * 实例化 ContinueFTPEx
	 * 
	 * @param unitCode
	 *            单位编码
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static synchronized ContinueFTP getInstance()
			throws NumberFormatException, IOException {

		if (ftp == null) {
			ftp = new ContinueFTP();
		}

		Configurater config = Configurater.getInstance();

		ftp.connect(config.getConfigValue("HOSTNAME"), new Integer(config
				.getConfigValue("PORT")).intValue(), config
				.getConfigValue("USERNAME"), config.getConfigValue("PASSWORD"));

		return ftp;
	}



	public static synchronized ContinueFTP getDownLoadInstance(String hostName,
			int port, String userName, String passWord)
			throws NumberFormatException, IOException {

		if (downLoadFtp == null) {
			downLoadFtp = new ContinueFTP();
		}
		downLoadFtp.connect(hostName, port, userName, passWord);

		return downLoadFtp;
	}

	/**
	 * 连接到FTP服务器
	 * 
	 * @param hostname
	 *            主机名
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 是否连接成功
	 * @throws IOException
	 */
	public boolean connect(String hostname, int port, String username,
			String password) throws IOException {

		ftpClient.setControlEncoding("GBK");

		FTPClientConfig config = new FTPClientConfig(FTPClientConfig.SYST_NT);
		config.setServerLanguageCode("GBK");
		// ftpClient.configure(config);

		ftpClient.setDataTimeout(120000);

		ftpClient.connect(hostname, port);
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(username, password)) {
				return true;
			}
		}
		disconnect();
		return false;
	}

	/**
	 * 从FTP服务器上下载文件,支持断点续传，上传百分比汇报
	 * 
	 * @param remote
	 *            远程文件路径
	 * @param local
	 *            本地文件路径
	 * @return 上传的状态
	 * @throws IOException
	 */
	public DownloadStatus download(String remote, String local)
			throws IOException {
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		DownloadStatus result;

		// 检查远程文件是否存在
		FTPFile[] files = ftpClient.listFiles(remote);
		if (files.length != 1) {
			Logger.getLogger(ContinueFTP.class).warn("远程文件不存在");
			return DownloadStatus.Remote_File_Noexist;
		}

		long lRemoteSize = files[0].getSize();
		File f = new File(local);
		// 本地存在文件，进行断点下载
		if (f.exists()) {
			long localSize = f.length();
			// 判断本地文件大小是否大于远程文件大小
			if (localSize >= lRemoteSize) {
				Logger.getLogger(ContinueFTP.class).warn("本地文件大于远程文件，下载中止");
				return DownloadStatus.Local_Bigger_Remote;
			}

			Logger.getLogger(ContinueFTP.class).info("目标文件: "+f.getAbsolutePath());
			// 进行断点续传，并记录状态
			FileOutputStream out = new FileOutputStream(f, true);
			ftpClient.setRestartOffset(localSize);
			InputStream in = ftpClient.retrieveFileStream(remote);
			byte[] bytes = new byte[1024];
			long step = lRemoteSize < 100 ? 1 : lRemoteSize / 100;
			long process = localSize / step;
			int c;
			while ((c = in.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				localSize += c;
				long nowProcess = localSize / step;
				if (nowProcess > process) {
					process = nowProcess;
					if (process % 10 == 0)
						Logger.getLogger(ContinueFTP.class).info(
								"下载进度：" + process);
					// TODO 更新文件下载进度,值存放在process变量中
				}
			}
			in.close();
			out.close();
			boolean isDo = ftpClient.completePendingCommand();
			if (isDo) {
				result = DownloadStatus.Download_From_Break_Success;
			} else {
				result = DownloadStatus.Download_From_Break_Failed;
			}
		} else {
			OutputStream out = new FileOutputStream(f);
			InputStream in = ftpClient.retrieveFileStream(remote);
			byte[] bytes = new byte[1024];
			long step = lRemoteSize < 100 ? 1 : lRemoteSize / 100;
			long process = 0;
			long localSize = 0L;
			int c;
			while ((c = in.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				localSize += c;
				long nowProcess = localSize / step;
				if (nowProcess > process) {
					process = nowProcess;
					if (process % 10 == 0)
						Logger.getLogger(ContinueFTP.class).info(
								"下载进度：" + process);
					// TODO 更新文件下载进度,值存放在process变量中
				}
			}
			in.close();
			out.close();
			boolean upNewStatus = ftpClient.completePendingCommand();
			if (upNewStatus) {
				result = DownloadStatus.Download_New_Success;
			} else {
				result = DownloadStatus.Download_New_Failed;
			}
		}
		return result;
	}
	
	
	
	
	/**
	 * 从FTP服务器下载，获输出取流
	 */ 
	public InputStream download(String remote)
			throws IOException {
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		// 检查远程文件是否存在
		FTPFile[] files = ftpClient.listFiles(remote);
		if (files.length != 1) {
			Logger.getLogger(ContinueFTP.class).warn("远程文件不存在");
		}
		InputStream in = ftpClient.retrieveFileStream(remote);
//		ftpClient.completePendingCommand();
		return in;
	}
		
	

	
	
	/**
	 * 按目录进行下载
	 * @param remote 远程目录
	 * @param local  保存文件的本地地址
	 * @return
	 * @throws IOException 
	 */
	public DownloadStatus downloadByFolder(String remote, String local) throws IOException{
		if(StringUtils.isBlank(remote) || StringUtils.isBlank(local))
			return DownloadStatus.Remote_File_Noexist;
		remote = remote.replaceAll("\\\\", "/");
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setControlEncoding("GBK");
		DownloadStatus result = null;
		
		// 检查远程文件是否存在
		FTPFile[] files = ftpClient.listFiles(remote);
		for(FTPFile file : files){
			result = download(remote+"/"+file.getName(),local+File.separator+file.getName());
			//下载完成以后删除
			deleteFile(remote+"/"+file.getName());
		}
		return result;
	}
	
	
	/**
	 * 判断远程目录是否为空
	 * @param remote
	 * @return
	 * @throws IOException
	 */
	public boolean indexDirIsEmpty(String remote) throws IOException{
		FTPFile[] files = ftpClient.listFiles(remote);
		if(files == null){
			return true;
		}
		if(files.length > 0 )
			return false;
		return true;
	}
	/**
	 * 上传文件到FTP服务器，支持断点续传
	 * 
	 * @param local
	 *            本地文件名称，绝对路径
	 * @param remote
	 *            远程文件路径，使用/home/directory1/subdirectory/file.ext或是
	 *            http://www.guihua.org /subdirectory/file.ext
	 *            按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
	 * @return 上传结果
	 * @throws IOException
	 */
	public UploadStatus upload(String local, String remote) throws IOException {
		ftpClient.changeWorkingDirectory("/");
		// 设置PassiveMode传输
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制流的方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setControlEncoding("GBK");
		// ftpClient.setControlEncoding("utf-8");
		// ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);

		// FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
		// conf.setServerLanguageCode("zh");

		UploadStatus result;
		// 对远程目录的处理
		String remoteFileName = remote;
		remote = remote.replaceAll("\\\\", "/");

		if (remote.contains("/")) {
			remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
			// 创建服务器远程目录结构，创建失败直接返回
			if (CreateDirecroty(remote, ftpClient) == UploadStatus.Create_Directory_Fail) {
				return UploadStatus.Create_Directory_Fail;
			}
		}

		// 检查远程是否存在文件
		// FTPFile[] files = ftpClient.listFiles(new String(remoteFileName
		// .getBytes("GBK"), "iso-8859-1"));
		FTPFile[] files = ftpClient.listFiles(new String(remoteFileName));
		if (files.length == 1) {
			long remoteSize = files[0].getSize();
			File f = new File(local);
			long localSize = f.length();
			if (remoteSize == localSize) {
				return UploadStatus.File_Exits;
			} else if (remoteSize > localSize) {
				return UploadStatus.Remote_Bigger_Local;
			}

			// 尝试移动文件内读取指针,实现断点续传
			result = uploadFile(remoteFileName, f, ftpClient, remoteSize);

			// 如果断点续传没有成功，则删除服务器上文件，重新上传
			if (result == UploadStatus.Upload_From_Break_Failed) {
				if (!ftpClient.deleteFile(remoteFileName)) {
					return UploadStatus.Delete_Remote_Faild;
				}
				result = uploadFile(remoteFileName, f, ftpClient, 0);
			}
		} else {
			result = uploadFile(remoteFileName, new File(local), ftpClient, 0);
		}
		return result;
	}

	/**
	 * 删除远程文件
	 * 
	 * @param remoteFileName
	 * @return
	 * @throws IOException
	 */
	public DeleteFileStatus deleteFile(String remoteFileName)
			throws IOException {

		if (!ftpClient.deleteFile(remoteFileName)) {
			return DeleteFileStatus.Delete_Remote_Faild;
		}

		return DeleteFileStatus.Delete_Remote_Success;
	}

	/**
	 * 断开与远程服务器的连接
	 * 
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		if (ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}

	/**
	 * 递归创建远程服务器目录
	 * 
	 * @param remote
	 *            远程服务器文件绝对路径
	 * @param ftpClient
	 *            FTPClient 对象
	 * @return 目录创建是否成功
	 * @throws IOException
	 */
	public UploadStatus CreateDirecroty(String remote, FTPClient ftpClient)
			throws IOException {
		UploadStatus status = UploadStatus.Create_Directory_Success;
		String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
		// if (!directory.equalsIgnoreCase("/")
		// && !ftpClient.changeWorkingDirectory(new String(directory
		// .getBytes("GBK"), "iso-8859-1"))) {
		if (!directory.equalsIgnoreCase("/")
				&& !ftpClient.changeWorkingDirectory(directory)) {
			// 如果远程目录不存在，则递归创建远程服务器目录
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			while (true) {
				// String subDirectory = new String(remote.substring(start, end)
				// .getBytes("GBK"), "iso-8859-1");
				String subDirectory = new String(remote.substring(start, end));

				if (!ftpClient.changeWorkingDirectory(subDirectory)) {
					if (ftpClient.makeDirectory(subDirectory)) {
						ftpClient.changeWorkingDirectory(subDirectory);
					} else {
						Logger.getLogger(ContinueFTP.class).warn("创建目录失败");
						return UploadStatus.Create_Directory_Fail;
					}
				}

				start = end + 1;
				end = directory.indexOf("/", start);

				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		}
		return status;
	}

	/**
	 * 上传文件到服务器,新上传和断点续传
	 * 
	 * @param remoteFile
	 *            远程文件名，在上传之前已经将服务器工作目录做了改变
	 * @param localFile
	 *            本地文件 File句柄，绝对路径
	 * @param processStep
	 *            需要显示的处理进度步进值
	 * @param ftpClient
	 *            FTPClient 引用
	 * @return
	 * @throws IOException
	 */
	public UploadStatus uploadFile(String remoteFile, File localFile,
			FTPClient ftpClient, long remoteSize) throws IOException {
		UploadStatus status;
		// 显示进度的上传
		long step = localFile.length() < 100 ? 1 : (localFile.length() / 100);
		long process = 0;
		long localreadbytes = 0L;
		RandomAccessFile raf = new RandomAccessFile(localFile, "r");
		// OutputStream out = ftpClient.appendFileStream(new String(remoteFile
		// .getBytes("GBK"), "iso-8859-1"));
		OutputStream out = ftpClient.appendFileStream(remoteFile);
		// 断点续传
		if (remoteSize > 0) {
			ftpClient.setRestartOffset(remoteSize);
			process = remoteSize / step;
			raf.seek(remoteSize);
			localreadbytes = remoteSize;
		}
		byte[] bytes = new byte[1024];
		int c;
		while ((c = raf.read(bytes)) != -1) {
			out.write(bytes, 0, c);
			localreadbytes += c;
			if (localreadbytes / step != process) {
				process = localreadbytes / step;
				Logger.getLogger(ContinueFTP.class).info("上传进度:" + process);
				// TODO 汇报上传状态
			}
		}
		out.flush();
		raf.close();
		out.close();
		boolean result = ftpClient.completePendingCommand();
		if (remoteSize > 0) {
			status = result ? UploadStatus.Upload_From_Break_Success
					: UploadStatus.Upload_From_Break_Failed;
		} else {
			status = result ? UploadStatus.Upload_New_File_Success
					: UploadStatus.Upload_New_File_Failed;
		}
		return status;
	}

	public void rename(String remoteFile1, String remoteFile2)
			throws IOException {

		ftpClient.rename(remoteFile1, remoteFile2);

	}

	/**
	 * 将文件流输出到输出流
	 * 
	 * @param remoteFileName
	 * @param outs
	 * @throws IOException
	 */
	public boolean loadFile(String remoteFileName, OutputStream outs)
			throws IOException {
		boolean retFlag = ftpClient.retrieveFile(remoteFileName, outs);
		return retFlag;
	}

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		ContinueFTP ftp = new ContinueFTP();
		ftp.connect("192.168.3.123", 21, "edm", "edm");
		
		// 设置被动模式
		ftp.ftpClient.enterLocalPassiveMode();
		// 设置以二进制方式传输
		ftp.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		DownloadStatus result;

		// 检查远程文件是否存在
		FTPFile[] files = ftp.ftpClient.listFiles("/q08012905/");
		for(FTPFile file : files){
			
		}
		
		
		
	}
}
