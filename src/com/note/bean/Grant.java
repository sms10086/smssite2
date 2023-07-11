package com.note.bean;


public class Grant {
	private String EId;
	private Integer roleId;
	private Integer privilegeId;
	public String getEId() {
		return EId;
	}
	public void setEId(String id) {
		EId = id;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Integer getPrivilegeId() {
		return privilegeId;
	}
	public void setPrivilegeId(Integer privilegeId) {
		this.privilegeId = privilegeId;
	}
	
}
