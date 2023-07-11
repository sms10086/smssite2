package com.smssite2.mgmt.basicsetting.form;

import java.sql.Timestamp;
import org.apache.struts.action.ActionForm;

public class PhraseAddForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	private int phraseId;
	private String phraseContent;
	private String phraseType;
	private int staffId;
	private String EID;
	private Timestamp addTime;
	private Timestamp updateTime;
	
	public int getPhraseId() {
		return phraseId;
	}
	public void setPhraseId(int phraseId) {
		this.phraseId = phraseId;
	}
	public String getPhraseContent() {
		return phraseContent;
	}
	public void setPhraseContent(String phraseContent) {
		this.phraseContent = phraseContent;
	}
	public String getPhraseType() {
		return phraseType;
	}
	public void setPhraseType(String phraseType) {
		this.phraseType = phraseType;
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
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
}
