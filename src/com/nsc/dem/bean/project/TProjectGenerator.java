package com.nsc.dem.bean.project;

import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.impl.SessionImpl;

import com.nsc.base.hibernate.GeneratorFactory;
import com.nsc.dem.bean.system.TDictionary;
@SuppressWarnings("unchecked")
public class TProjectGenerator extends GeneratorFactory {

	/**
	 * 生成编码
	 * 
	 * @param session
	 *            (hibernate)
	 * @param obj
	 *            (hibernate实体)
	 * @return
	 * @throws Exception 
	 */
	@Override
	protected Object buildGeneratorCode(SessionImpl session, Object obj) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yy");
		TProject mainProject = null;
		TProject project = (TProject) obj;
		
		StringBuffer sb = null;
		if(project.getParentId()==null){
			sb = new StringBuffer("");
			sb.append(project.getVoltageLevel().replaceAll("\\D+", ""));
			
			String unitName = project.getTUnitByOwnerUnitId().getName();
			

			Query query = session.createQuery("from TDictionary as t where t.name='"+unitName+"' and t.parentCode='WSGS'");
			List list = query.list();
			if(list!=null&&list.size()!=0){
				TDictionary dic = (TDictionary)list.get(0);
				sb.append(dic.getCode().replaceAll("\\D+", ""));
			}else{
				throw new Exception("无法获取网省公司"+unitName+"的编码!");
			}
			sb.append("00");
			sb.append(formatter.format(project.getPreDesignYear()));
			
			// 工程项目流水号
			String serialNum = this.getSerialNumber( session, project, 4, "left","0", false);
			sb.append(serialNum);
			
		}else{
			mainProject = (TProject) session.get(TProject.class, project.getParentId());
			sb = new StringBuffer(mainProject.getCode());
			String num = this.getSerialNumber(session, obj, 2, "left","0", true);
			sb.append(num);
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
	 * @return
	 */
	private String getSerialNumber(SessionImpl session, Object obj,
			int length, String position,String fillChar, boolean isSingle) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		TProject project = (TProject) obj;
		
		String sql = "";
		String function = "LPAD";
		if(position.equalsIgnoreCase("right")){
			function = "RPAD";
		}
			
		if (!isSingle) {
			sql = "select case when tt.cn <= nvl(tt.serial,0) then "
				+ function
				+ "(nvl(tt.serial,0) + 1,"
				+ length
				+ ",'"
				+ fillChar
				+ "') else "
				+ function
				+ "(tt.cn,4,'0') end from (select count(*)+1 cn,max(to_number(substr(t.code,length(t.code)-3))) serial " +
											"from T_PROJECT t " +
										   "where TO_CHAR(T.PRE_DESIGN_YEAR,'yyyy')="
												+ formatter.format(project.getPreDesignYear())
												+ " AND T.PARENT_ID IS NULL" 
												+ " AND REGEXP_LIKE(substr(t.code, length(t.code) -3),'^[0-9]+$')) tt";
			
		} else {
			sql = "select case when tt.cn <= nvl(tt.serial,0) then "
				+ function
				+ "(nvl(tt.serial,0) + 1,"
				+ length
				+ ",'"
				+ fillChar
				+ "') else "
				+ function
				+ "(tt.cn,2,'0') end from (select count(*)+1 cn,max(to_number(substr(t.code,length(t.code)-1))) serial " +
											"from T_PROJECT t " +
										   "where T.PARENT_ID=" + project.getParentId()+""
											+ " AND REGEXP_LIKE(substr(t.code, length(t.code) -3),'^[0-9]+$')) tt";
				
		}

		String number = null;
		SQLQuery query = session.createSQLQuery(sql);
		List list = query.list();
		number = list.get(0).toString();
		return number;

	}
}
