package com.nsc.dem.action.searches;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import com.nsc.base.conf.Configurater;
import com.nsc.base.util.ContinueFTP;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.service.base.IService;
import com.nsc.dem.util.log.Logger;

/**
 * 预览action
 * @author ibm
 *
 */
public class PreViewAction extends BaseAction {
	private static final long serialVersionUID = 5304532811867020924L;
	// ID
	private String id;

	public void setId(String id) {
		this.id = id;
	}

	IService baseService;

	public void setBaseService(IService baseService) {
		this.baseService = baseService;

	}

	//预览
	public void getPreView() throws IOException {
		Logger logger=super.logger.getLogger(PreViewAction.class);
		TDoc tdoc = (TDoc) baseService.EntityQuery(TDoc.class, id);
		if (tdoc != null) {

			String filePath = tdoc.getPreviewPath();
			if (filePath != null) {
				ServletOutputStream outs = super.getResponse()
						.getOutputStream();
				// 调用下载的FileUtil.download(fileName, filePath, request,
				// response);
				// String fileName = tdoc.getName();
				// 从配置文件中读取主机名、用户名、密码、端口号
				Configurater config = Configurater.getInstance();
				String hostname = config.getConfigValue("HOSTNAME");
				String username = config.getConfigValue("USERNAME");
				int port = Integer.parseInt(config.getConfigValue("PORT"));
				String password = config.getConfigValue("PASSWORD");
				ContinueFTP ftp = ContinueFTP.getDownLoadInstance(hostname, port, username, password);
				// 判断ftp是否连接成功，如果连接成功，则调用输出流的方法
				if (ftp != null) {
					ftp.loadFile(filePath, outs);
				}
				outs.flush();
			}
			logger.info("文件路径：" + filePath);
		}
	}
}
