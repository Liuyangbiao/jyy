package com.nsc.base.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 集合操作类
 * 
 * 此类用于对集合对象进行操作，包括查找、排序等。
 * 
 * @author bs-team
 *
 * @date Oct 19, 2010 10:31:55 AM
 * @version
 */
public class CollectionUtils {

	/**
	 * 将List 中的数组按照数组中某一个字段进行分类，放入hashmap中。
	 * 
	 * @param list
	 * @param index
	 *            数组中的字段序列号，从0开始
	 * @return
	 */
	public static HashMap<String, List<Object[]>> getUnitDataMap(
			List<Object[]> list, int index) {
		HashMap<String, List<Object[]>> dataMap = new HashMap<String, List<Object[]>>();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i)[index];
			String unit = obj.toString();

			if (dataMap.containsKey(unit)) {
				dataMap.get(unit).add((Object[]) list.get(i));
			} else {
				ArrayList<Object[]> rowdata = new ArrayList<Object[]>();
				rowdata.add((Object[]) list.get(i));
				dataMap.put(unit, rowdata);
			}
		}

		return dataMap;
	}

	/**
	 * 
	 * @param list
	 * @param findex
	 * @param sindex
	 * @return
	 */
	public static HashMap<String, HashMap<String, List<Object[]>>> getUnitDataHashMap(
			List<Object[]> list, int findex, int sindex) {

		HashMap<String, List<Object[]>> dataMap = new HashMap<String, List<Object[]>>();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i)[findex];
			String unit = obj.toString();

			if (dataMap.containsKey(unit)) {
				dataMap.get(unit).add((Object[]) list.get(i));
			} else {
				ArrayList<Object[]> rowdata = new ArrayList<Object[]>();
				rowdata.add((Object[]) list.get(i));
				dataMap.put(unit, rowdata);
			}
		}
		Iterator<String> keys = dataMap.keySet().iterator();

		HashMap<String, HashMap<String, List<Object[]>>> returnMap = new HashMap<String, HashMap<String, List<Object[]>>>();

		while (keys.hasNext()) {
			Object keyStr = keys.next();
			String company = String.valueOf(keyStr);// 这里定义的是站的名称
			List<Object[]> getDate = dataMap.get(company);
			HashMap<String, List<Object[]>> getMap = getUnitDataMap(getDate,
					sindex);

			returnMap.put(company, getMap);
		}

		return returnMap;

	}

	/**
	 * 将List 中的数组按照数组中某一个字段进行分类，放入hashmap中。
	 * 
	 * @param list
	 * @param indexnum
	 *            数组中的字段序列号，从0开始
	 * @return
	 */
	public static HashMap<String, List<Object[]>> getUnitDataMapList(
			List<Object[]> list, int[] indexnum) {
		HashMap<String, List<Object[]>> dataMap = new HashMap<String, List<Object[]>>();
		for (int i = 0; i < list.size(); i++) {
			StringBuffer returnStringBuffer = new StringBuffer();
			for (int ai = 0; ai < indexnum.length; ai++) {
				int index = indexnum[ai];
				Object obj = list.get(i)[index];
				String gunit = obj.toString();
				if (ai == 0) {
					returnStringBuffer.append(gunit);
				} else {
					returnStringBuffer.append("(" + gunit + ")");
				}

			}
			String unit = returnStringBuffer.toString();
			if (dataMap.containsKey(unit)) {
				dataMap.get(unit).add((Object[]) list.get(i));
			} else {
				ArrayList<Object[]> rowdata = new ArrayList<Object[]>();
				rowdata.add((Object[]) list.get(i));
				dataMap.put(unit, rowdata);
			}
		}

		return dataMap;
	}

	/**
	 * 取得list中属性名称为fieldName 值为value的对象
	 * 
	 * @param list
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public static Object getObjectByValue(List<?> list, String fieldName,
			Object value) {

		for (Object o : list) {
			Object value2 = BeanUtil.getFieldValue(fieldName, o);
			if (value.equals(value2))
				return o;
		}

		return null;
	}

	/**
	 * 取得list中，数组对象第index值为value的对象
	 * 
	 * @param list
	 * @param index
	 * @param value
	 * @return
	 */
	public static Object[] getObjectByValue(List<?> list, int index,
			Object value) {
		for (Object o : list) {
			Object[] os = (Object[]) o;
			if (value.equals(os[index]))
				return os;
		}
		return null;
	}

	/**
	 * 按照list中对象数组的第index对象进行分组
	 * 
	 * @param list
	 * @param index
	 */
	public static void groupListData(List<Object[]> list, int index) {

		String last = null;
		// sortListData(list,field);
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = list.get(i);
			if (!objs[index].equals(last)) {

				Object[] temp = objs.clone();

				for (int j = 0; j < temp.length; j++) {
					if (j != index) {
						temp[j] = null;
					}
				}

				list.add(i, temp);

				last = String.valueOf(objs[index]);
			}
		}
	}

	/**
	 * 按照list中对象的field属性进行分组，并返回组的个数
	 * 
	 * @param list
	 * @param field
	 * @param asc
	 * @return
	 */
	public static int groupListData(List<Object> list, String field, boolean asc) {
		Object last = null;
		int groupNum = 0;
		try {
			sortListData(list, field, asc);
			for (int i = 0; i < list.size(); i++) {
				Method method = Reflections.getGetterMethod(list.get(i)
						.getClass(), field);
				Object value = Reflections.invoke(method, list.get(i),
						new Object[] {});
				if (!value.equals(last)) {
					Object temp = list.get(i).getClass().newInstance();
					Method md = Reflections.getSetterMethod(temp.getClass(),
							field);
					Reflections.invoke(md, temp, new Object[] { value });
					list.add(i, temp);
					last = value;
					groupNum++;
				}
			}
		} catch (Exception ex) {
			throw new AppException(ex, "app.collection.group", null,
					new String[] {});
		}

		return groupNum;
	}

	/**
	 * 将list中对象按field属性进行排序
	 * 
	 * @param list
	 * @param field
	 * @param asc
	 *            为1的时候为升序，-1为降序
	 */
	public static void sortListData(List<Object> list, final String field,
			final boolean asc) {

		Collections.sort(list, new Comparator<Object>() {
			private int order = asc ? 1 : -1;

			public int compare(Object o1, Object o2) {
				try {
					Method method = Reflections.getGetterMethod(o1.getClass(),
							field);
					Object value1 = Reflections.invoke(method, o1,
							new Object[] {});

					method = Reflections.getGetterMethod(o2.getClass(), field);
					Object value2 = Reflections.invoke(method, o2,
							new Object[] {});

					if (value1 instanceof String) {
						return order
								* ((String) value1).compareTo((String) value2);
					} else if (value1 instanceof Long) {
						return order * ((Long) value1).compareTo((Long) value2);
					} else if (value1 instanceof Float) {
						return order
								* ((Float) value1).compareTo((Float) value2);
					} else {
						return 0;
					}
				} catch (Exception ex) {
					throw new AppException(ex, "app.collection.sort", null,
							new String[] {});
				}
			}
		});
	}
}
