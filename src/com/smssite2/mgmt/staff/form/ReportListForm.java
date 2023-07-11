package com.smssite2.mgmt.staff.form;

import com.note.common.page.BasicPagination;

public class ReportListForm extends BasicPagination{
	private String start;
	private String end;
	private String smsCompanyname;
	private String team;
	private String regSales;
	private String stuate;
	private String routeType;
	private String eid;
	public String getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = eid;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getSmsCompanyname() {
		return smsCompanyname;
	}
	public void setSmsCompanyname(String smsCompanyname) {
		this.smsCompanyname = smsCompanyname;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public String getRegSales() {
		return regSales;
	}
	public void setRegSales(String regSales) {
		this.regSales = regSales;
	}
	public String getStuate() {
		return stuate;
	}
	public void setStuate(String stuate) {
		this.stuate = stuate;
	}
	public String getRouteType() {
		return routeType;
	}
	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}
}
