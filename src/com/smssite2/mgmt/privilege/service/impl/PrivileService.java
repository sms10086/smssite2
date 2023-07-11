package com.smssite2.mgmt.privilege.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.note.NoteException;
import com.note.bean.Role;
import com.smssite2.mgmt.privilege.bean.MenuBean;
import com.smssite2.mgmt.privilege.dao.IPrivilegeDao;
import com.smssite2.mgmt.privilege.dao.impl.PrivilegeDao;
import com.smssite2.mgmt.privilege.form.RoleListForm;
import com.smssite2.mgmt.privilege.service.IPrivilegeService;

public class PrivileService implements IPrivilegeService {	public List<Role> findRoleByEId(RoleListForm roleList, String EId) {
		IPrivilegeDao dao = new PrivilegeDao();
		try{
		List<Role> list = dao.queryRoleByEId(roleList,EId);
		
		return list;
		}catch(Exception e){
			
		}
		return null;
	}

	public void addRole(String uname, String umemu, String credit,String EId,String flag,String roleId) throws  NoteException{
		if(flag==null)throw new NoteException("添加或修改角色出错！");
	
		IPrivilegeDao dao = new PrivilegeDao();
		if(uname==null||uname.trim().equals(""))throw new NoteException("角色名不能为空!");
		Role r=dao.findRoleByName(uname,EId);
		if(flag.equals("add")){
			if(r!=null)throw new NoteException("该角色已经存在!");
			String uid=dao.findRoleId(EId);
			Integer id=Integer.parseInt(uid);
			id=id+1;
			 roleId=id.toString();
			int b=uid.length()-roleId.length();
			for(int i=0;i<b;i++){
				roleId=0+roleId;
			}
			int a=dao.saveRole(roleId,uname,umemu,credit,EId);
			if(a<=0)throw new NoteException("添加角色失败！");
		}
		else if(flag.equals("modify")){
			if(r!=null&&!r.getPostId().equals(roleId))throw new NoteException("该角色已经存在!");
			int a=dao.updateRole(roleId,uname,umemu,credit,EId);
			if(a<=0)throw new NoteException("修改角色失败！");
		}
	}
	public void deleteRole(String EId, String[] roleId) throws NoteException {
		IPrivilegeDao dao = new PrivilegeDao();
		int a=dao.deleteRole(EId,roleId);
		if(a<=0)throw new NoteException("删除角色失败！");
	}
	public Role findRoleByRoleId(String roleId,String EId) throws NoteException {
		IPrivilegeDao dao = new PrivilegeDao();
		Role role=dao.queryRoleByRoleId(roleId,EId);
		if(role==null)throw new NoteException("该角色已被删除！");
		return role;
	}
	public ArrayList<MenuBean> findMemuByEId(String EId) throws NoteException {
		if(EId==null||EId.trim().equals(""))throw new NoteException("企业帐号不能为空");
		String sql ;
		if(EId.equals("000000")){
			sql="select * from defMenu_supervisor";
		}else{
			sql="select * from defMenu";
		}
		IPrivilegeDao dao = new PrivilegeDao();
		ArrayList<MenuBean> list=dao.findALlMenu(sql);
		return list;
	}

	

}
