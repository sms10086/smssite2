package com.note.bean;

import java.util.ArrayList;

public class Privilege {
	private String EId;
	private Integer privilegeId;
	private String name;
	private String url;
	private Integer parentId;
	private Integer nodeType;
	private Integer ordinal;
	private String note;
	private String credit;
	
	private ArrayList<Privilege> childList = new ArrayList<Privilege>();
	private String checked = "";

	public String getEId() {
		return EId;
	}
	public void setEId(String id) {
		EId = id;
	}
	public Integer getPrivilegeId() {
		return privilegeId;
	}
	public void setPrivilegeId(Integer privilegeId) {
		this.privilegeId = privilegeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getNodeType() {
		return nodeType;
	}
	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}
	public Integer getOrdinal() {
		return ordinal;
	}
	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public ArrayList<Privilege> getChildList() {
		return childList;
	}
	public void setChildList(ArrayList<Privilege> childList) {
		this.childList = childList;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	
}
