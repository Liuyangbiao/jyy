package com.nsc.dem.bean.system;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.impl.SessionImpl;

import com.nsc.base.hibernate.GeneratorFactory;

public class TUnitGenerator extends GeneratorFactory {

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
		// SimpleDateFormat formatter = new SimpleDateFormat("yy");
		TUnit unit = (TUnit) obj;
		StringBuffer sb = new StringBuffer("");
		String type = unit.getType();

		if (type.equalsIgnoreCase("C01")) {
			sb.append(unit.getCode());
		} else {
			// 设计单位
			if ("C02".equalsIgnoreCase(type)) {
				sb.append("CD");
				// 监理单位
			} else if ("C03".equalsIgnoreCase(type)) {
				sb.append("CS");
				// 建设单位
			} else if ("C04".equalsIgnoreCase(type)) {
				sb.append("CC");
				// 评审单位
			} else if ("C05".equalsIgnoreCase(type)) {
				sb.append("CA");
			} else {
				return null;
			}
			String serialNum = this.getSerialNumber(session, obj, 4, "left",
					"0", type);
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
	 * @return
	 */
	private String getSerialNumber(SessionImpl session, Object obj, int length,
			String position, String fillChar, String type) {
//		TUnit unit = (TUnit) obj;
		String function = "LPAD";
		if (position.equalsIgnoreCase("right")) {
			function = "RPAD";
		}
		String sql = "select case when tt.cn <= nvl(tt.serial,0) then "
				+ function
				+ "(nvl(tt.serial,0) + 1,"
				+ length
				+ ",'"
				+ fillChar
				+ "') else "
				+ function
				+ "(tt.cn,4,'0') end from (select count(*)+1 cn,max(to_number(substr(t.code, 3))) serial from t_unit t where t.type = '"
				+ type + "') tt";

		String number = null;
		SQLQuery query = session.createSQLQuery(sql);
		List<?> list = query.list();
		number = list.get(0).toString();
		return number;

	}
}
