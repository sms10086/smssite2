package com.smssite2.mgmt.privilege.service;

import java.util.ArrayList;
import java.util.List;
import com.note.NoteException;
import com.note.bean.Role;
import com.smssite2.mgmt.privilege.bean.MenuBean;
import com.smssite2.mgmt.privilege.form.RoleListForm;

public interface IPrivilegeService {


	List<Role> findRoleByEId(RoleListForm roleList, String id);



	void deleteRole(String id, String[] roleId) throws NoteException;
	

	ArrayList<MenuBean> findMemuByEId(String EId) throws NoteException;
	void addRole(String uname, String umemu, String credit,String EId, String flag, String roleId) throws NoteException;
	Role findRoleByRoleId(String roleId, String EId)throws NoteException;

	
	

}
