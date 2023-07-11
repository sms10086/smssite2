package com.note.common;

import java.sql.Timestamp;

public class TimeUtil {	public static Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}
	public static Timestamp formatTime(String time){
		if(time.trim()==null||"".equals(time.trim()))return null;
		return Timestamp.valueOf(time+":00.000");
	}
}
