package com.nsc.dem.service.system;

public interface IsystemSet {
	
	
    /**
     * 系统设置--测试连接   调用FTP通用模块进行测试连接
	 * @param  参数为系统设置实体类
     */
	public void testConnect();
	

	
    /**
     * 系统设置--保存   调用通用模块的读写工具进行写配置文件
	 * @param  参数为系统设置实体类
     */
	public void writeSystemSet();
	
	
}
