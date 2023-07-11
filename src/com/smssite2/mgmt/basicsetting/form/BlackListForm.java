package com.smssite2.mgmt.basicsetting.form;

import com.note.common.page.BasicPagination;

public class BlackListForm extends BasicPagination { 
	private static final long serialVersionUID = 1L;
	private String blackPhoneNum;
	private String siftContent;
	public String getSiftContent() {
		return siftContent;
	}
	public void setSiftContent(String siftContent) {
		this.siftContent = siftContent;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getBlackPhoneNum() {
		return blackPhoneNum;
	}
	public void setBlackPhoneNum(String blackPhoneNum) {
		this.blackPhoneNum = blackPhoneNum;
	}
	
}
