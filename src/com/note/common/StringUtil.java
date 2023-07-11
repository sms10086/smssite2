package com.note.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import com.note.MD5Proxy;

public class StringUtil {
	public static String[]  pattern(String source,String regEx){
		String[] ss=null;
		if(source==null||source.trim().length()==0)
		{
			ss=new String[0];
		}else{
			if(regEx==null)ss=new String[]{source};
			else{
				Pattern p=Pattern.compile(regEx);
				Matcher m=p.matcher(source);
				List<String> list=new ArrayList<String>();
				while(m.find()){
					list.add(m.group());
				}
				ss=new String[list.size()];
				for(int i=0;i<list.size();i++){
					ss[i]=list.get(i);
				}
			}
		}
		return ss;
	}
	public static boolean isEmpty(String source){
		 
		if(source==null||source.trim().length()==0)return true;
		else
			return false;
		
	}
	public static int parseInt(String value,int defaultValue) {
		if(isEmpty(value)) {
			return defaultValue;
		}
		value=value.trim();
		if(!StringUtils.isNumeric(value)){
			return defaultValue;
		}
		try {
			int v = Integer.parseInt(value);
			return v;
		} catch (Exception e) {
			return defaultValue;
		}
	}
	public static long parseLong(Object o,long defaultValue){
		if(o==null)return defaultValue;
		if(o instanceof Long){
			return Long.parseLong(String.valueOf(o));
		}else
		if(o instanceof Integer){
			return Long.parseLong(String.valueOf(o));
		}else
		if(o instanceof String){
			if(StringUtils.isNumeric(String.valueOf(o))){
				try{
					return Long.parseLong(String.valueOf(o));
				}catch(Exception e){
					return defaultValue;
				}
			}
			else return defaultValue;
		}
		return defaultValue;
	}
	public static String getTime(int day){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, day);
		SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd");
		String time=smd.format(cal.getTime());
		return time;
	}
	
	public static String parseTimestampToStr(Timestamp time,String format){
		String result = "";
		if(isEmpty(format))format="yyyy-MM-dd";
		if(time==null)return "";
		SimpleDateFormat sformat = new SimpleDateFormat(format);
		result = sformat.format(time);
		return result;
	}
	
	public static Date parseDate(String value,Date defaultValue) {
		if(value == null) {
			return defaultValue;
		}
		try {
			if(value.indexOf(":")>0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(value);
			return date;
			}else{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = sdf.parse(value);
				return date;
			}
		} catch (Exception e) {
			return defaultValue;
		}
	}
	public static Timestamp parseTimestamp(String value,int day){
		if(isEmpty(value)){
			return null;
		}
		try{
			value=value.trim();
			if(day==1){
				 return Timestamp.valueOf(value+" 24:00:00.0");
			}else
				return Timestamp.valueOf(value+" 00:00:00.0");
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static String getString(String source){
		if(isEmpty(source))return "";
		else return source.trim();
	}
	public static String getMD5Str(String username,String password){
		StringBuilder sb = new StringBuilder();
		sb.append(username.toLowerCase()).append(password.toLowerCase());
		if(sb.toString().length()!=0){
			return MD5Proxy.getMd5Str(sb.toString());
		}
		return null;
	}
	
	public static Integer parseInt(Object obj, int i) {
		if(obj==null) return i;
		 
		return parseInt(String.valueOf(obj),i);
	}
}
