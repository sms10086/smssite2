package com.smssite2.mgmt.message.bean;


public class SumBalanceBean {
	private String balance;
	private int sum;
	private String messageId;
	private String isSchedule;
	public int getIsSchedule() {
		if(isSchedule==null)isSchedule="0";
		if(isSchedule!=null&&isSchedule.indexOf(".")>0)isSchedule=isSchedule.substring(0,isSchedule.indexOf("."));
		return Integer.parseInt(isSchedule);
	}
	public void setIsSchedule(String isSchedule) {
		this.isSchedule = isSchedule;
	}
	public int getBalance() {
		if(balance==null)balance="0";
		if(balance!=null&&balance.indexOf(".")>0)balance=balance.substring(0,balance.indexOf("."));
		return Integer.parseInt(balance);
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
}
