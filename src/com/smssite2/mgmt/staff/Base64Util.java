package com.smssite2.mgmt.staff;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;

public class Base64Util {	
	public static String encode(String source, String charset) {
		if(source == null)
			return null;
		try {
		byte[] data = source.getBytes(charset);
		return encode( data );
		}
		catch(Throwable thr) {
			thr.printStackTrace();
			return null;
		}
	}
	
	public static String encode(byte[] data) {
		try {
			return new String(Base64.encodeBase64(data));
		}
		catch(Throwable thr) {
			throw new RuntimeException(thr);
		}
	}
	
	public static String decode(String source, String charset) {
		if (source != null)
		try {
			return new String( Base64.decodeBase64( source.getBytes() ), charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] decode(String source) {
		if (source != null)
			try {
				return Base64.decodeBase64( source.getBytes() );
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
	}
	
}
