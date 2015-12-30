package com.nsc.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ToUnicode  
{     
  // 将字符串转unicode  
  public static String convert(String s) {  
    String unicode = "";  
    char[] charAry = new char[s.length()];  
    for(int i=0; i<charAry.length; i++) {  
      charAry[i] = (char)s.charAt(i);  
      if(Character.isLetter(charAry[i])&&(charAry[i]>255))  
           unicode+="/u" + Integer.toString(charAry[i], 16);  
      else  
           unicode+=charAry[i];  
    }  
    return unicode;  
  }  
//读文件   
  public static String readFile(String filePath) throws IOException, FileNotFoundException {   
              String result = null;   
              File file = new File(filePath);   
              if (file.exists()) {   
                           FileInputStream fis = new FileInputStream(file);   
            byte[] b = new byte[fis.available()];   
            //从输入流中读取b.length长的字节并将其存储在缓冲区数组 b 中。  
            fis.read(b);   
            //使用指定的字符集解码指定的字节数组  
            result = new String(b, "utf-8");   
            fis.close();   
        }   
        return convert(result);   
    }   
}   