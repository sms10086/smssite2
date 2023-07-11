package com.smssite2.mgmt.privilege.form;

import com.note.common.page.BasicPagination;

public class RoleListForm extends BasicPagination{
	private String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
}
