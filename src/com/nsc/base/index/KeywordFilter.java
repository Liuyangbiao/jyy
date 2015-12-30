package com.nsc.base.index;

public class KeywordFilter {
	private static String firstError = "`~*)+,?）？-"; // 关键字中第一个特殊字符使搜索出错的特殊字符
	private static String allError = "!^({}:[]！（："; // 关键字中无论站位在哪里都出错的特殊字符
	private static String linkError = "&&"; // 关键字中两个字符合并使程序出错的字符串
	private static String filterFirst = null;

	/**
	 * 过滤"&&"特殊字符
	 * 
	 * @param keyword
	 * @return 过滤完“&&”的关键字
	 */
	private static String filterLink(String keyword) {
		return keyword.replaceAll(linkError, "");
	}

	/**
	 * 过滤字符!^({}:[]！（：
	 * 
	 * @param str
	 * @return 过滤完以上特殊字符的关键字
	 */
	private static String filterAll(String str) {
		String filterFirst = filterFirst(str);
		for (int i = 0; i < filterFirst.length(); i++) {
			for (int j = 0; j < allError.length(); j++) {
				if (filterFirst != null && !"".equals(filterFirst)) {
					if (filterFirst.length() >= (i + 1)) { // 当过滤行如特殊字符为
						// *`~*)+,?）？-*java!^({}:[]！（
						// 防止数组下标越界
						if (filterFirst.charAt(i) == allError.charAt(j)) {
							filterFirst = filterFirst
									.replaceAll("\\"
											+ String.valueOf(filterFirst
													.charAt(i)), "");
						}
					}
				} else
					return filterFirst;
			}
		}
		return filterFirst;
	}

	/**
	 * 递归方法，过滤第一个字符使搜索出错的关键字
	 * 
	 * @param filterLink
	 * @return 递归调用
	 */
	private static String filterFirst(String filterLink) {
		String str = null;
		if (filterLink != null && !"".equals(filterLink)) {
			for (int i = 0; i < firstError.length(); i++) {
				if (filterLink.charAt(0) == firstError.charAt(i)) {
					str = filterLink.substring(1, filterLink.length());
					break;
				}
			}
			filterFirst = filterLink;
		} else {
			for (int j = 0; j < firstError.length(); j++) {
				if (filterFirst.charAt(0) == firstError.charAt(j)) {
					return "";
				}
			}
			return filterFirst;
		}
		return filterFirst(str);
	}

	/**
	 * 过滤器总方法，提供给外部调用
	 * 
	 * @param queryStr
	 * @return 已经过滤的关键字
	 */
	public static String filterKeyword(String queryStr) {
		// 先过滤 &&，再过滤第一个字符为 `~*)+,?）？- 最后过滤所有!^({}:[]！（：的关键字
		return filterAll(filterFirst(filterLink(queryStr)));
	}
}
