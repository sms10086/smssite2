package com.smssite2.mgmt.privilege.dao.impl;

import java.util.ArrayList;
import java.util.List;
import com.note.bean.Role;
import com.note.common.DbBean;
import com.smssite2.mgmt.privilege.bean.MenuBean;
import com.smssite2.mgmt.privilege.dao.IPrivilegeDao;
import com.smssite2.mgmt.privilege.form.RoleListForm;

public class PrivilegeDao implements IPrivilegeDao{
	@SuppressWarnings("unchecked")
	public List<Role> queryRoleByEId(RoleListForm roleList, String EId) {
		StringBuffer sql=new StringBuffer();
		StringBuffer w=new StringBuffer();
		StringBuffer count= new StringBuffer();
		sql.append("select * from UserPost where EId=?");
		count.append("select count(*) from UserPost where EId=? ");
		List params= new ArrayList();
		params.add(EId);
		if(roleList.getRoleName()!=null&&!roleList.getRoleName().trim().equals("")){
			w.append(" and Uname like ? ");
			params.add("%"+roleList.getRoleName().trim()+"%");
		}
		if(w!=null&&w.length()>0){
			sql.append(w);
			count.append(w);
		}
		sql.append(" order by postId ");
		roleList.setTotalItems(DbBean.getInt(count.toString(), params.toArray()));
		roleList.adjustPageIndex();
		
		List<Role> list=DbBean.select(sql.toString(), params.toArray(), roleList.getStartIndex(), roleList.getPageSize(), Role.class);
		return list;
	}

	@SuppressWarnings("static-access")
	public Role findRoleByName(String roleName, String EId) {
		DbBean db =new DbBean();
		Role role=null;
		String sql="select * from UserPost where uname=? and EId=? ";
		Object[] params=new Object[]{roleName,EId};
	
			role=(Role)db.selectFirst(sql, params, Role.class);
		
		
		return role;
	}


	@SuppressWarnings({ "static-access", "unchecked" })
	public int deleteRole(String EId, String[] roleId) {
		DbBean db = new DbBean();
		String sql="";
		int b=0;
		for(String str:roleId){
			sql="delete from userPost where postId='"+str+"' and EId='"+EId+"'";
			try{
				b=db.executeUpdate(sql, null);
				b++;
			}catch(Exception e){
				db.rollback();
				return 0;
			}
		}
			return b;
		
	}
	public Role queryRoleByRoleId(String roleId,String EId) {
		String sql="select * from userPost where postId=? and Eid=?";
		return 	(Role)DbBean.selectFirst(sql, new Object[]{roleId,EId}, Role.class);
	}
	@SuppressWarnings("static-access")
	public int updateRole(String roleId, String uname, String umemu,String credit,String EId) {
		String sql="update userPost set uname=?,umemo=?,credit=? where postId=? and eid=?";
		DbBean db=new DbBean();
		try{
			int a=	db.executeUpdate(sql, new Object[]{uname,umemu,credit,roleId,EId});
			return a;
		}catch(Exception e){
			db.rollback();
			return 0;
		}
		
	}
	@SuppressWarnings("unchecked")
	public ArrayList<MenuBean> findALlMenu(String sql) {
		return 	DbBean.select(sql, null, 0, 10000, MenuBean.class);
	}
	@SuppressWarnings("unchecked")
	public String findRoleId(String EId){
		List<Role> list =DbBean.select("select * from UserPost where EId=? order by postId desc ", new Object[]{EId}, 0, 200, Role.class);
		if(list==null||list.size()==0)return "000";
		return list.get(0).getPostId();
	}
	@SuppressWarnings("static-access")
	public int saveRole(String roleId, String uname, String umemu,
			String credit, String EId) {
		String sql ="insert into UserPost(postid,uname,umemo,credit,eid) values(?,?,?,?,?)";
		Object[] params=new Object[]{roleId,uname,umemu,credit,EId};
		DbBean db=new DbBean();
		try{
		int a=db.executeUpdate(sql, params);
		return a;
		}catch(Exception e){
			db.rollback();
			return 0;
		}
	}
}
