package com.smssite2.mgmt.basicsetting.form;

import java.sql.Timestamp;
import org.apache.struts.action.ActionForm;

public class SiftAddForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	private int siftId;
	private String EID;
	private String siftContent;
	private int staffId;
	private Timestamp addTime;
	public int getSiftId() {
		return siftId;
	}
	public void setSiftId(int siftId) {
		this.siftId = siftId;
	}
	public String getEID() {
		return EID;
	}
	public void setEID(String eid) {
		EID = eid;
	}
	public String getSiftContent() {
		return siftContent;
	}
	public void setSiftContent(String siftContent) {
		this.siftContent = siftContent;
	}
	public int getStaffId() {
		return staffId;
	}
	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}
	public Timestamp getAddTime() {
		return addTime;
	}
	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}
	
}
