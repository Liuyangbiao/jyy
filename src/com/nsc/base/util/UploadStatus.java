package com.nsc.base.util;

public enum  UploadStatus {
	 Create_Directory_Fail,     //远程服务器相应目录创建失败   
	 Create_Directory_Success,   //远程服务器闯将目录成功   
	 Upload_New_File_Success,    //上传新文件成功   
	 Upload_New_File_Failed,     //上传新文件失败   
	 File_Exits,                 //文件已经存在   
	 Remote_Bigger_Local,        //远程文件大于本地文件   
	 Upload_From_Break_Success,  //断点续传成功   
	 Upload_From_Break_Failed,   //断点续传失败   
	 Delete_Remote_Faild;        //删除远程文件失败  
}
