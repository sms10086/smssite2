package com.smssite2.mgmt.patch;

import java.io.File;
import java.io.IOException;

public class FormatResult {	public File file;
	public int err = 0;
	public int total = 0;
	public int real = 0;
	public boolean flag = false;
	
	public FormatResult() throws IOException {
		file = File.createTempFile("imp", null);
	}
}
