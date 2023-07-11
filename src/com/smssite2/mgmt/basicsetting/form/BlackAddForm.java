package com.smssite2.mgmt.basicsetting.form;

import java.sql.Timestamp;
import org.apache.struts.action.ActionForm;

public class BlackAddForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	private int blackId;
	private String blackPhoneNum;
	private int staffId;
	private String EID;
	private Timestamp addTime;
	
	public int getBlackId() {
		return blackId;
	}
	public void setBlackId(int blackId) {
		this.blackId = blackId;
	}
	public String getBlackPhoneNum() {
		return blackPhoneNum;
	}
	public void setBlackPhoneNum(String blackPhoneNum) {
		this.blackPhoneNum = blackPhoneNum;
	}
	public int getStaffId() {
		return staffId;
	}
	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}
	public String getEID() {
		return EID;
	}
	public void setEID(String eid) {
		EID = eid;
	}
	public Timestamp getAddTime() {
		return addTime;
	}
	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}
	
}
