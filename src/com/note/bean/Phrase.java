package com.note.bean;

import java.util.Date;

public class Phrase {
	private Integer phraseId;
	private String phraseContent;
	private String phraseType;
	private String StaffId;
	private String EId;
	private Date addTime;
	private Date upDateTime;
	public Integer getPhraseId() {
		return phraseId;
	}
	public void setPhraseId(Integer phraseId) {
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
	public String getStaffId() {
		return StaffId;
	}
	public void setStaffId(String staffId) {
		StaffId = staffId;
	}
	public String getEId() {
		return EId;
	}
	public void setEId(String id) {
		EId = id;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getUpDateTime() {
		return upDateTime;
	}
	public void setUpDateTime(Date upDateTime) {
		this.upDateTime = upDateTime;
	}
}
