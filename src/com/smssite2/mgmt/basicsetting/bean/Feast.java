package com.smssite2.mgmt.basicsetting.bean;

import java.io.Serializable;

public class Feast implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int feastId;
	private String feastName;
	private String feastStyle;
	private String worldDate;
	private String chinaDate;
	private String feastContent;
	private String EID;
	
	public int getFeastId() {
		return feastId;
	}
	public void setFeastId(int feastId) {
		this.feastId = feastId;
	}
	public String getFeastName() {
		return feastName;
	}
	public void setFeastName(String feastName) {
		this.feastName = feastName;
	}
	public String getFeastStyle() {
		return feastStyle;
	}
	public void setFeastStyle(String feastStyle) {
		this.feastStyle = feastStyle;
	}
	public String getWorldDate() {
		return worldDate;
	}
	public void setWorldDate(String worldDate) {
		this.worldDate = worldDate;
	}
	public String getChinaDate() {
		return chinaDate;
	}
	public void setChinaDate(String chinaDate) {
		this.chinaDate = chinaDate;
	}
	public String getFeastContent() {
		return feastContent;
	}
	public void setFeastContent(String feastContent) {
		this.feastContent = feastContent;
	}
	public String getEID() {
		return EID;
	}
	public void setEID(String eid) {
		EID = eid;
	}
	
}
