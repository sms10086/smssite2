package com.note.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class ConfigUtil {	private static HashMap<String, String> confMaps = new HashMap<String, String>();
	private static File config = new File("conf/config.properties");

	public static String getProperties(String name) {
		if (name != null && confMaps.get(name) != null) {
			return confMaps.get(name);
		} else {
			Properties p = new Properties();
			InputStream input = null;
			try {
				input = new FileInputStream(config);
				p.load(input);
				String ss = null;
				ss = p.getProperty(name);
				if (ss == null) {
					confMaps.put(name, ss);
					return null;
				} else {
					confMaps.put(name, ss);
					return ss;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (input != null)
						input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		return null;
	}

}
