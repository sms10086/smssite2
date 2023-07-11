package com.smssite2.mgmt.message.form;

import org.apache.struts.upload.FormFile;
import com.note.common.page.BasicPagination;

public class LoadPhoneForm extends BasicPagination{	private static final long serialVersionUID = 1L;
	private FormFile phoneFile;
	private String name;
	public FormFile getPhoneFile() {
		return phoneFile;
	}
	public void setPhoneFile(FormFile phoneFile) {
		this.phoneFile = phoneFile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
