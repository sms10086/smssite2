package com.note.common;


public class UUIDUtil {	
	private static UUIDHexGenerator gen = new UUIDHexGenerator();
	
	public static String generate() {
		return gen.generate();
	}
}
