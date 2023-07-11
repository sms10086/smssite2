package com.smssite2.mgmt.basicsetting.form;

import com.note.common.page.BasicPagination;

public class FeastListForm extends BasicPagination {
	private static final long serialVersionUID = 1L;
	private String feastName;
	private String EID;
	public String getFeastName() {
		return feastName;
	}
	public void setFeastName(String feastName) {
		this.feastName = feastName;
	}
	public String getEID() {
		return EID;
	}
	public void setEID(String eid) {
		EID = eid;
	}
	
}
