package com.smssite2.mgmt.staff.form;

import com.note.common.page.BasicPagination;

public class AccountForm extends BasicPagination{
	private String eid;
	private String smsCompanyname;
	private String sregSales;
	private String routeType;
	private String order;
	public String getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = eid;
	}
	public String getSmsCompanyname() {
		return smsCompanyname;
	}
	public void setSmsCompanyname(String smsCompanyname) {
		this.smsCompanyname = smsCompanyname;
	}
	public String getSregSales() {
		return sregSales;
	}
	public void setSregSales(String sregSales) {
		this.sregSales = sregSales;
	}
	public String getRouteType() {
		return routeType;
	}
	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
}
