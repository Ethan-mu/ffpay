package org.xxpay.common.util.excel.util;

import java.util.regex.Pattern;

public class MyStringUtils {
	
	public static boolean isNotBlank(CharSequence str){
		if(!str.equals("") && str!=null)
			return true;
		return false;
	}
	public static boolean isBlank(CharSequence str){
		if(str.equals("") || str==null)
			return true;
		return false;
	}
	
	public static boolean isDecimalPointOfManyZero(String str) {    
		if(isBlank(str))
			return false;
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*[.]?[0]+$");    
		return pattern.matcher(str).matches();    
	}
	
   public static boolean isNumeric(CharSequence cs)
    {
        if(isBlank(cs))
            return false;
        int sz = cs.length();
        for(int i = 0; i < sz; i++)
            if(!Character.isDigit(cs.charAt(i)))
                return false;

        return true;
    }
   public static boolean isENum(CharSequence cs)
   {
	   if(isBlank(cs))
	       return false;
	   String regx = "^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$";//科学计数法正则表达式
	   Pattern pattern = Pattern.compile(regx);
	   return pattern.matcher(cs).matches();
   }
   public static boolean isDouble(CharSequence cs) {    
	   if(isBlank(cs))
	       return false;
	    Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");    
	    return pattern.matcher(cs).matches();    
	 } 
}
