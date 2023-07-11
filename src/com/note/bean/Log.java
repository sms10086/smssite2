package com.note.bean;

import java.sql.Timestamp;
import java.util.Date;

public class Log {
	private String EId;
	private String logId;
	private String logTime;
	private String menuItem;
	private String actionType;
	private String actionLog;
	private String staffId;

	
	public String getEId() {
		return EId;
	}
	public void setEId(String id) {
		EId = id;
	}
	
	public String getMenuItem() {
		return menuItem;
	}
	public void setMenuItem(String menuItem) {
		this.menuItem = menuItem;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getActionLog() {
		return actionLog;
	}
	public void setActionLog(String actionLog) {
		this.actionLog = actionLog;
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getLogTime() {
		return logTime;
	}
	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}
	
}
