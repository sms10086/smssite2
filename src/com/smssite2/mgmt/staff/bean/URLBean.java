package com.smssite2.mgmt.staff.bean;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class URLBean {
	public String getURL(){
		String URL=null;
		 BufferedInputStream in = null;
			try {
				  in = new BufferedInputStream( new FileInputStream( "conf/URL.ini"));
				  BufferedReader br = new BufferedReader( new InputStreamReader(in));
				  String s;
			         while((s = br.readLine()) != null){
			        	 if(s.startsWith("URL"))URL=s.substring(4);
			         }
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return URL;
		}
	}
