package com.smssite2.mgmt.linkman.bean;

import java.io.Serializable;

public class Group implements Serializable{	private static final long serialVersionUID = 1L;
	
	private String groupId;
	private String groupName;
	private String replyContent;
	private String name;
	private String groupMemo;
	private String staffId;
	private String EID;
	private String isShare;
	
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupMemo() {
		return groupMemo;
	}
	public void setGroupMemo(String groupMemo) {
		this.groupMemo = groupMemo;
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
	
	public String getIsShare() {
		return isShare;
	}
	public void setIsShare(String isShare) {
		this.isShare = isShare;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	
	
}
