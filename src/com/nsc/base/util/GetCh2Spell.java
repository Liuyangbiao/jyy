package com.nsc.base.util;

public class GetCh2Spell {

	private static String _FromEncode_="GBK";
	private static String _ToEncode_="GBK";
	
	/*
	*compareTo(StringanotherString)按字典顺序比较两个字符串。该比较基于字符串中各个字符的Unicode值。将此
	*String对象表示的字符序列与参数字符串所表示的字符序列进行比较。如果按字典顺序此String
	*对象在参数字符串之前，则比较结果为一个负整数。如果按字典顺序此String
	*对象位于参数字符串之后，则比较结果为一个正整数。如果这两个字符串相等，则结果为0；compareTo只有在方法equals(Object)
	*返回true时才返回0。这是字典排序的定义。
	*如果这两个字符串不同，则要么它们在某个索引处具有不同的字符，该索引对二者均为有效索引，要么它们的长度不同，或者同时具备上述两种情况。
	*如果它们在一个或多个索引位置上具有不同的字符，假设k是这类索引的最小值；则按照<运算符确定的那个字符串在位置k
	*上具有较小的值，其字典顺序在其他字符串之前。这种情况下，compareTo返回这两个字符串在位置k处的两个不同的char值，即值：
	*this.charAt(k)-anotherString.charAt(k)
	*如果它们没有不同的索引位置，则较短字符串在字典顺序上位于较长字符串的前面。这种情况下，compareTo返回这两个字符串长度的不同，即值：
	*this.length()-anotherString.length()
	*
	*compareTo("你好","你好");结果：0compareTo("你","你好");结果：－1
	*compareTo("你好","你");结果：1以上，由于对应位置的值都相等，所有返回的是长度差。
	*
	*compareTo("你好1","你好2");结果：－1compareTo("你好1","你好9");结果：－8以上返回值是不同位置索引的
	*差～，即：字符编码的差额
	*/
	
	/**
	*功能：返回两个字符的字典顺序比较结果
	*/
	public static int compare(String str1, String str2)
    {
        int result = 0;
        String m_s1 = null;
        String m_s2 = null;
        try
        {
            m_s1 = new String(str1.getBytes(_FromEncode_), _ToEncode_);
            m_s2 = new String(str2.getBytes(_FromEncode_), _ToEncode_);
        }
        catch(Exception e)
        {
            return str1.compareTo(str2);
        }
        result = chineseCompareTo(m_s1, m_s2);
        return result;
    }
	@SuppressWarnings("all")
    public static int getCharCode(String s)
    {
        if(s == null && s.equals(""))
            return -1;
        byte b[] = s.getBytes();
        int value = 0;
        for(int i = 0; i < b.length && i <= 2; i++)
            value = value * 100 + b[i];

        return value;
    }

    public static int chineseCompareTo(String s1, String s2)
    {
        int len1 = s1.length();
        int len2 = s2.length();
        int n = Math.min(len1, len2);
        for(int i = 0; i < n; i++)
        {
            int s1_code = getCharCode(s1.charAt(i) + "");
            int s2_code = getCharCode(s2.charAt(i) + "");
            if(s1_code * s2_code < 0)
                return Math.min(s1_code, s2_code);
            if(s1_code != s2_code)
                return s1_code - s2_code;
        }

        return len1 - len2;
    }

    /**
     * 返回字符串的首字母
     * @param res
     * @return
     */
    public static String getBeginCharacter(String res)
    {
        String a = res;
        String result = "";
        for(int i = 0; i < a.length(); i++)
        {
            String current = a.substring(i, i + 1);
            if(compare(current, "\u554A") < 0)
                result = result + current;
            else
            if(compare(current, "\u554A") >= 0 && compare(current, "\u5EA7") <= 0)
                if(compare(current, "\u531D") >= 0)
                    result = result + "z";
                else
                if(compare(current, "\u538B") >= 0)
                    result = result + "y";
                else
                if(compare(current, "\u6614") >= 0)
                    result = result + "x";
                else
                if(compare(current, "\u6316") >= 0)
                    result = result + "w";
                else
                if(compare(current, "\u584C") >= 0)
                    result = result + "t";
                else
                if(compare(current, "\u6492") >= 0)
                    result = result + "s";
                else
                if(compare(current, "\u7136") >= 0)
                    result = result + "r";
                else
                if(compare(current, "\u671F") >= 0)
                    result = result + "q";
                else
                if(compare(current, "\u556A") >= 0)
                    result = result + "p";
                else
                if(compare(current, "\u54E6") >= 0)
                    result = result + "o";
                else
                if(compare(current, "\u62FF") >= 0)
                    result = result + "n";
                else
                if(compare(current, "\u5988") >= 0)
                    result = result + "m";
                else
                if(compare(current, "\u5783") >= 0)
                    result = result + "l";
                else
                if(compare(current, "\u5580") >= 0)
                    result = result + "k";
                else
                if(compare(current, "\u51FB") > 0)
                    result = result + "j";
                else
                if(compare(current, "\u54C8") >= 0)
                    result = result + "h";
                else
                if(compare(current, "\u5676") >= 0)
                    result = result + "g";
                else
                if(compare(current, "\u53D1") >= 0)
                    result = result + "f";
                else
                if(compare(current, "\u86FE") >= 0)
                    result = result + "e";
                else
                if(compare(current, "\u642D") >= 0)
                    result = result + "d";
                else
                if(compare(current, "\u64E6") >= 0)
                    result = result + "c";
                else
                if(compare(current, "\u82AD") >= 0)
                    result = result + "b";
                else
                if(compare(current, "\u554A") >= 0)
                    result = result + "a";
        }

        return result;
    }

    /**
     * 得到一个汉字的首字符
     * @param str
     * @return
     */
    public static String getFirstStr(String str)
    {
        char a = str.charAt(0);
        char aa[] = {
            a
        };
        String sss = new String(aa);
        if(Character.isDigit(aa[0]))
            sss = "data";
        else
        if(a >= 'a' && a <= 'z' || a >= 'A' && a <= 'Z')
            sss = "character";
        else
            sss = getBeginCharacter(sss);
        return sss;
    }
}
