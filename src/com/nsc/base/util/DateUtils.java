package com.nsc.base.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.nsc.base.conf.ConstConfig;

/**
 * 日期工具类
 * 
 * 此类用于对日期进行一般操作，例如日期转字符串等。
 * 
 * @author bs-team
 * 
 * @date Oct 19, 2010 10:34:08 AM
 * @version
 */
public class DateUtils {

	/**
	 * 返回当前月所在旬的第一天或最后一天
	 * 
	 * @param isBegin
	 * @return 若isBegin为true，返回所在旬的第一天日期，若为FALSE则返回所在旬的最后一天日期
	 */
	public static Date getTenday(boolean isBegin) {
		Calendar cal = Calendar.getInstance();
		int beginDay;
		int endDay;
		// SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int dayofM = cal.get(Calendar.DAY_OF_MONTH);

		if (dayofM > 20) {
			beginDay = 21;
			endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		} else if (dayofM > 10) {
			beginDay = 11;
			endDay = 20;
		} else {
			beginDay = 1;
			endDay = 10;
		}

		if (isBegin) {
			cal.set(year, month, beginDay);
		} else {
			cal.set(year, month, endDay);
		}
		return cal.getTime();
	}

	/**
	 * 返回当前月当前旬的前（后）一个旬的第一天日期或最后一天日期
	 * 
	 * @param isBegin
	 *            true为第一天，FALSE为最后一天
	 * @param tenday
	 *            取值为1或-1
	 * @return 1为后一个旬日期，-1为前一个旬日期
	 */
	public static Date getAddTenday(boolean isBegin, int tenday) {
		Calendar cal = Calendar.getInstance();
		int beginDay;
		int endDay;

		Date beginDate = getDayAfterDays(getTenday(false), tenday);
		cal.setTime(beginDate);

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int dayofM = cal.get(Calendar.DAY_OF_MONTH);

		if (dayofM > 20) {
			beginDay = 21;
			endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		} else if (dayofM > 10) {
			beginDay = 11;
			endDay = 20;
		} else {
			beginDay = 1;
			endDay = 10;
		}

		if (isBegin) {
			cal.set(year, month, beginDay);
		} else {
			cal.set(year, month, endDay);
		}
		return cal.getTime();
	}

	/**
	 * 以date时间为基准，求解在days天之后的日期
	 * 
	 * @param date
	 *            基准时间
	 * @param days
	 *            相差天数
	 * @return date之后days天的日期
	 */
	public static Date getDayAfterDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	/**
	 * 取得该月的最后一天的日期
	 * 
	 * @param aDate
	 *            日期
	 * @return 该月最后一天的日期
	 */
	@SuppressWarnings("deprecation")
	public static final Date getEndMonthDate(Date aDate) {
		int date = 30;
		if (aDate == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(aDate);
		date = cal.getActualMaximum(Calendar.DATE);
		aDate.setDate(date);
		return aDate;
	}

	/**
	 * 将一个日期字符串dateStr按format的形式进行设置
	 * 
	 * @param dateStr
	 *            日期字符串
	 * @param format
	 *            日期的格式字符串
	 * @return format形式的日期
	 */
	public static Date StringToDate(String dateStr, String format) {
		if (dateStr == null || dateStr.length() == 0)
			return null;

		DateFormat dateFormat = new SimpleDateFormat(format);

		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException ex) {
			throw new AppException(ex, "app.string.todate", null,
					new String[] {});
		}
	}

	/**
	 * 将日期类实例按format的形式进行设置
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            日期的格式字符串
	 * @return 按format的形式设置的日期
	 */
	public static String DateToString(Date date, String format) {
		if (date == null)
			return "";
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * 取得当天日期
	 * 
	 * @param aDate
	 * @return 当天日期
	 */
	public static final String getDate(Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(ConstConfig.defaultDatePattern);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * 取得指定格式的当天日期
	 * 
	 * @param aMask
	 * @param aDate
	 * @return 当天日期
	 */
	public static final String getDate(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {
			return "";
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * 两个时间之间相差距离多少天
	 * 
	 * @param one
	 *            时间参数 1：
	 * @param two
	 *            时间参数 2：
	 * @return 相差天数
	 */
	public static long getDistanceDays(Date one, Date two){
		long days = 0;
		long time1 = one.getTime();
		long time2 = two.getTime();
		long diff;
		if (time1 < time2) {
			diff = time2 - time1;
		} else {
			diff = time1 - time2;
		}
		days = diff / (1000 * 60 * 60 * 24);
		return days;
	}

	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 * 
	 * @param str1
	 *            时间参数 1 格式：1990-01-01 12:00:00
	 * @param str2
	 *            时间参数 2 格式：2009-01-01 12:00:00
	 * @return long[] 返回值为：{天, 时, 分, 秒}
	 */
	public static long[] getDistanceTimes(String str1, String str2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch (ParseException e) {
			Logger.getLogger(DateUtils.class).warn("计算两个时间相差距离多少天多少小时多少分多少秒字符转换异常:",e);
		}
		long[] times = { day, hour, min, sec };
		return times;
	}

	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 * 
	 * @param str1
	 *            时间参数 1 格式：1990-01-01 12:00:00
	 * @param str2
	 *            时间参数 2 格式：2009-01-01 12:00:00
	 * @return String 返回值为：xx天xx小时xx分xx秒
	 */
	public static String getDistanceTime(String str1, String str2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch (ParseException e) {
			Logger.getLogger(DateUtils.class).warn("计算两个时间相差距离多少天多少小时多少分多少秒字符转换异常:",e);
		}
		return day + "天" + hour + "小时" + min + "分" + sec + "秒";
	}

	public static final String DateToString(Date aDate) {
		return DateToString(aDate, ConstConfig.defaultDatePattern);
	}

	public static Date StringToDate(String strDate) {
		Date aDate = null;

		if (strDate == null || "".equals(strDate)) {
			return aDate;
		}

		aDate = StringToDate(ConstConfig.defaultDatePattern, strDate);

		return aDate;
	}
}
