package com.smssite2.mgmt.basicsetting.form;

import java.sql.Timestamp;
import org.apache.struts.action.ActionForm;

public class FeastAddForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	private int feastId;
	private String feastName;
	private String feastStyle;
	private Timestamp worldDate;
	private Timestamp chinaDate;
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
	public Timestamp getWorldDate() {
		return worldDate;
	}
	public void setWorldDate(Timestamp worldDate) {
		this.worldDate = worldDate;
	}
	public Timestamp getChinaDate() {
		return chinaDate;
	}
	public void setChinaDate(Timestamp chinaDate) {
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
