package com.smssite2.mgmt.linkman.bean;

import java.io.Serializable;

public class Linkman implements Serializable {	private static final long serialVersionUID = 1L;
	
	private String linkId;
	private String name;
	private String phone;
	private String birthday;
	private String sex;
	private String post;
	private String orgName;
	private String userMemo;
	private String groupId;
	private String staffId;
	private String EID;
	private String status;
	private String optionalContent;
	private String ismember;
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getUserMemo() {
		return userMemo;
	}
	public void setUserMemo(String userMemo) {
		this.userMemo = userMemo;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public String getEID() {
		return EID;
	}
	public void setEID(String eid) {
		EID = eid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOptionalContent() {
		return optionalContent;
	}
	public void setOptionalContent(String optionalContent) {
		this.optionalContent = optionalContent;
	}
	public String getIsmember() {
		return ismember;
	}
	public void setIsmember(String ismember) {
		this.ismember = ismember;
	}
	
		
}
