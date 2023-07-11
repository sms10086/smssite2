package com.note.bean;

import java.util.Date;

public class Sift {
	private String EId;
	private Integer siftId;
	private String siftContent;
	private Integer staffId;
	private Date addTime;
	public String getEId() {
		return EId;
	}
	public void setEId(String id) {
		EId = id;
	}
	public Integer getSiftId() {
		return siftId;
	}
	public void setSiftId(Integer siftId) {
		this.siftId = siftId;
	}
	public String getSiftContent() {
		return siftContent;
	}
	public void setSiftContent(String siftContent) {
		this.siftContent = siftContent;
	}
	public Integer getStaffId() {
		return staffId;
	}
	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}
