package com.note.bean;


public class StaffGrant {
	private String EId;
	private Integer privilegeId;
	private Integer staffId;
	private String credit;
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
	public Integer getStaffId() {
		return staffId;
	}
	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	
}
