package com.note.bean;

import java.util.Date;

public class Feast {
	private Integer feastId;
	private String feastName;
	private String feastStyle;
	private String worldDate;
	private String chinaDate;
	private String feastContent;
	private String EId;
	public Integer getFeastId() {
		return feastId;
	}
	public void setFeastId(Integer feastId) {
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
	public String getEId() {
		return EId;
	}
	public void setEId(String id) {
		EId = id;
	}
}
