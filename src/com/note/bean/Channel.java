package com.note.bean;


public class Channel {
	private Integer routeId;
	private String routeName;
	private String routeUserId;
	private String routeStyle;
	private String routeStatue;
	private String routeAddress;
	private Long smsBalance;
	private String routNum;
	private String routeSign;
	private String routeType;
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public String getRouteUserId() {
		return routeUserId;
	}
	public void setRouteUserId(String routeUserId) {
		this.routeUserId = routeUserId;
	}
	
	
	public String getRouteStyle() {
		return routeStyle;
	}
	public void setRouteStyle(String routeStyle) {
		this.routeStyle = routeStyle;
	}

	public Integer getRouteId() {
		return routeId;
	}
	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}
	public String getRouteStatue() {
		return routeStatue;
	}
	public void setRouteStatue(String routeStatue) {
		this.routeStatue = routeStatue;
	}
	public String getRouteType() {
		return routeType;
	}
	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}
	public String getRouteAddress() {
		return routeAddress;
	}
	public void setRouteAddress(String routeAddress) {
		this.routeAddress = routeAddress;
	}
	public Long getSmsBalance() {
		return smsBalance;
	}
	public void setSmsBalance(Long smsBalance) {
		this.smsBalance = smsBalance;
	}
	public String getRoutNum() {
		return routNum;
	}
	public void setRoutNum(String routNum) {
		this.routNum = routNum;
	}
	public String getRouteSign() {
		return routeSign;
	}
	public void setRouteSign(String routeSign) {
		this.routeSign = routeSign;
	}
}
