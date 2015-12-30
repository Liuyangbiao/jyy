package com.nsc.base.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtil {


    /**  
     * 
     * 
     *  @param  p1 分子
     *  @param  p2 分母
     *  @return 
      */ 
      public static String percent( double p1,  double p2){
        String str;
        double p3 =  p1 / p2;
        NumberFormat nf  =  NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(0);
        str  =  nf.format(p3);
        return  str;
      }

	/**
	 * 字符串转为Double
	 * @param obj
	 * @return Double 对象
	 */
  	public static double notNullDouble(String obj) {
		double re = 0d;
		if (obj != null && !"".equals(obj)) {
			re = Double.parseDouble(obj);
		}
		return re;
	}
  	
  	/**
  	 * 取得百分比
  	 * @param val1
  	 * @param val2
  	 * @return 经过格式化后的百分比值
  	 */
	public float getPercentValue(float val1,float val2){
		float f = 0f;
		if(val2>0){
			f = (val1 / val2) * 100;
			f = toFloat(formatValue(f));
		}
		return f;
	}
	/**
	 * 把String型转换成Float型
	 * @param str
	 * @return float值
	 */
	public float toFloat(String str) {
		float f = 0f;

		if (str != null && !"".equals(str.trim())) {
			f = Float.parseFloat(str);
		}

		return f;
	}

	/**
	 * 把对象转换成Float型
	 * @param obj
	 * @return float值
	 */
	public float notNullFloat(Object obj) {
		float f = 0f;
		if (obj == null) {
			f = 0f;
		} else {
			f = (Float) obj;
		}

		return f;
	}

	/**
	 * 把对象按0.00格式化格式化
	 * @param obj 被格式化对象
	 * @return 字符串
	 */
	public static String formatValue(Object obj){
		return formatValue(obj,"0.00");
	}
	
	/**
	 * 把对象按指定格式进行格式化
	 * @param obj 被格式化对象
	 * @param pattern 格式化样式
	 * @return 字符串
	 */
	public static String formatValue(Object obj,String pattern){
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(obj);
	}
	
	/**
	 * 把对象格式化成无小数点的字符串
	 * @param obj
	 * @return
	 */
	public static String formatToIntStr(Object obj){
		return formatValue(obj,"0");
	}
	
}
