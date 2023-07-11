package com.smssite2.mgmt.message;

import com.note.common.StringUtil;

public class ContentUtil {
	public static String[] getContents(String content,String smssign,int num){
		 
		if(content==null||content.trim().length()==0)return new String[]{StringUtil.getString(smssign)};
		if(smssign==null||smssign.trim().length()==0||smssign.toLowerCase().equals("null")){
			String[] contents=new String[(content.length()%num==0)?content.length()/num:content.length()/num+1];
			for(int i=0;i<((content.length()%num==0)?content.length()/num:content.length()/num+1);i++){
				if((i+1)*num>content.length()){
					contents[i]=content.substring(i*num);
				}else{
					contents[i]=content.substring(i*num, (i+1)*num);
				}
			}
			return contents;
		}else{
			num=num-smssign.length();
			String[] contents=new String[(content.length()%num==0)?content.length()/num:content.length()/num+1];
			for(int i=0;i<((content.length()%num==0)?content.length()/num:content.length()/num+1);i++){
				if((i+1)*num>content.length()){
					contents[i]=content.substring(i*num)+smssign;
				}else{
					contents[i]=content.substring(i*num, (i+1)*num)+smssign;
				}
			}
			return contents;
		}
	}
	public static int getSendPRI(int accountPRI,int count){
		if(count<1000){
			return accountPRI+18;
		}else
		if(count<4000){
			return accountPRI+14;
		}else
		if(count<10000){
			return accountPRI+10;
		}else
		if(count>10000&&count<25000){
			return accountPRI+8;
		}else
		if(count>25000&&count<50000){
			return accountPRI+6;
		}else
		if(count>50000&&count<100000){
			return accountPRI+4;
		}else
		if(count>100000&&count<500000){
			return accountPRI+2;
		}else{
			return accountPRI+1;
		}
	}
}
