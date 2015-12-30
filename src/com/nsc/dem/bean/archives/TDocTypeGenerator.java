package com.nsc.dem.bean.archives;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.impl.SessionImpl;

import com.nsc.base.hibernate.GeneratorFactory;
@SuppressWarnings("unchecked")
public class TDocTypeGenerator extends GeneratorFactory {
	
	/**
	 * 生成编码
	 * 
	 * @param session
	 *            (hibernate)
	 * @param obj
	 *            (hibernate实体)
	 * @return
	 */
	@Override
	protected Object buildGeneratorCode(SessionImpl session, Object obj) {

		TDocType docType = (TDocType) obj;
		StringBuffer sb = new StringBuffer("");
		String projectType = null;
		String fileType = null;
		
		String code = docType.getCode();
		if(code!=null&&!code.equals("")){
			String[] strs = code.split("_");
			
			try {
				projectType = strs[0];
			} catch (Exception e) {
				projectType = "WS";
			}
			
			try {
				fileType = strs[1];
			} catch (Exception e) {
				fileType = "QT";
			}
		}else{
			
		}
		sb.append(projectType);
		sb.append("14");
		sb.append(fileType);
		if(docType.getParentCode()==null||docType.getParentCode().equals("")){
			return sb;
		}
		
		int length = sb.length();
		int offset = 1;
		String serialNum = this.getSerialNumber(session, obj, 4, "0",offset);
		sb.append(serialNum);
		
		while(this.isExists(session, sb.toString())){
			offset++;
			sb = new StringBuffer(sb.substring(0, length));
			serialNum = this.getSerialNumber(session, obj, 4, "0",offset);
			sb.append(serialNum);
		}
		
		return sb;
	}
	
	/**
	 * 生成流水号
	 * 
	 * @param session
	 *            (hibernate)
	 * @param obj
	 *            (hibernate实体)
	 * @param length
	 *            流水号长度
	 * @param fillChar
	 *            长度不够时的填充字符
	 * @param offset
	 *            序列增加值
	 * @return
	 */
	private String getSerialNumber(SessionImpl session, Object obj,
			int length, String fillChar,int offset) {
		TDocType docType = (TDocType) obj;
		String parentCode = docType.getParentCode();
		if(parentCode==null||parentCode.equals("")){
			return "";
		}
		
		String function = "LPAD";
		String sql = "SELECT "+function+"(COUNT(*)+"+offset+"," + length + ",'" + fillChar
					+ "') FROM T_Doc_Type T WHERE T.PARENT_CODE='"+docType.getParentCode()+"'";
		

		String number = null;
		SQLQuery query = session.createSQLQuery(sql);
		List list = query.list();
		number = list.get(0).toString();
		return number;

	}
	private boolean isExists(SessionImpl session,String code){
		String sql = "SELECT 1 FROM T_DOC_TYPE T WHERE CODE='"+code+"'";
		SQLQuery query = session.createSQLQuery(sql);
		List list = query.list();
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
}
