package com.note.common;

import java.util.UUID;

public class PKID{
	public static String generate(){
		UUID uuid=UUID.randomUUID();
		String[] uus=uuid.toString().split("-");
		String str="";
		for(int i=0;i<uus.length;i++){
			str+=uus[i];
		}
		return str;
	}
}
