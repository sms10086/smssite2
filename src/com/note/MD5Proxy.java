package com.note;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Proxy {	private static MessageDigest md = null;
	static {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	
	public static final String toHex(final byte[] hash) {
		char buf[] = new char[hash.length * 2];
		for (int i = 0, x = 0; i < hash.length; i++) {
			buf[x++] = HEX_CHARS[(hash[i] >>> 4) & 0xf];
			buf[x++] = HEX_CHARS[hash[i] & 0xf];
		}
		return new String(buf);
	}

	public static final String toHexTram(final byte[] hash) {
		char buf[] = new char[hash.length * 2];
		for (int i = 0, x = 0; i < hash.length; i++) {
			int low = (hash[i] >>> 4) & 0xf;
			if (low != 0) {
				buf[x++] = HEX_CHARS[(hash[i] >>> 4) & 0xf];
			}
			buf[x++] = HEX_CHARS[hash[i] & 0xf];
		}
		return new String(buf).trim();
	}

	public static byte[] toUnicodeBytes(String content) {
		byte[] b = new byte[content.length() * 2];
		for (int i = 0; i < content.length(); i++) {
			char ch = content.charAt(i);
			b[i * 2] = new Integer(ch & 0xff).byteValue();
			b[i * 2 + 1] = new Integer((ch >> 8) & 0xff).byteValue();
		}
		return b;
	}

	private static byte[] getMD5Bytes(String str) {
		try {
		    MessageDigest clone = (MessageDigest)md.clone();
			return clone.digest(toUnicodeBytes(str));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[16];
	}
 
	public static String getMd5Str(String str) {
		return toHexTram(getMD5Bytes(str));
	}

}
