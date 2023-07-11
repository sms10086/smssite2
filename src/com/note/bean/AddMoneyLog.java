package com.note.bean;

import java.util.Date;

public class AddMoneyLog {
	private String moneyLogId;
	private String addTime;
	private String EId;
	private Float moneyCount;
	private Float price;
	private Long addNum;
	private String sraffId;
	private Integer isChecked;
	private String checkTime;
	private String addMome;
	private String director;
	
	public String getMoneyLogId() {
		return moneyLogId;
	}
	public void setMoneyLogId(String moneyLogId) {
		this.moneyLogId = moneyLogId;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getEId() {
		return EId;
	}
	public void setEId(String id) {
		EId = id;
	}
	public Float getMoneyCount() {
		return moneyCount;
	}
	public void setMoneyCount(Float moneyCount) {
		this.moneyCount = moneyCount;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Long getAddNum() {
		return addNum;
	}
	public void setAddNum(Long addNum) {
		this.addNum = addNum;
	}

	public String getSraffId() {
		return sraffId;
	}
	public void setSraffId(String sraffId) {
		this.sraffId = sraffId;
	}
	public Integer getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(Integer isChecked) {
		this.isChecked = isChecked;
	}
	
	public String getAddMome() {
		return addMome;
	}
	public void setAddMome(String addMome) {
		this.addMome = addMome;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
}
