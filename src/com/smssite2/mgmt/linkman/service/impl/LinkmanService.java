package com.smssite2.mgmt.linkman.service.impl;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.note.NoteException;
import com.note.common.DbBean;
import com.note.common.TimeUtil;
import com.smssite2.mgmt.basicsetting.dao.IBasicSettingDao;
import com.smssite2.mgmt.basicsetting.dao.impl.BasicSettingDao;
import com.smssite2.mgmt.linkman.bean.Group;
import com.smssite2.mgmt.linkman.bean.Linkman;
import com.smssite2.mgmt.linkman.dao.ILinkmanDao;
import com.smssite2.mgmt.linkman.dao.impl.LinkmanDao;
import com.smssite2.mgmt.linkman.form.LinkmanListForm;
import com.smssite2.mgmt.linkman.service.ILinkmanService;
import com.smssite2.mgmt.message.bean.PhoneBean;
import com.smssite2.mgmt.message.service.impl.LoadPhoneService;

public class LinkmanService implements ILinkmanService {
	
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(LinkmanService.class);

	@SuppressWarnings("unchecked")
	public List<Group> findAllGroup(String eid, String staffId) {
		String sql ="select groupid,groupname," +
				"isShare,staffid,replyContent from linGroup where eid='"+eid+"' ";
		if(!staffId.toUpperCase().equals("ADMIN"))sql=sql+" and staffId='"+staffId+"'";
		
		List list=new ArrayList();
		ILinkmanDao dao = new LinkmanDao();
		List<Group> arr=dao.findAllGroup(sql,list);
		if(arr!=null && arr.size()!=0){
			for(int i=0;i<arr.size();i++){
				Group group = arr.get(i);
				if(group.getIsShare()!=null&&"Y".equals(group.getIsShare().toUpperCase())){
					if(staffId.equals(group.getStaffId()))
						{
							group.setName(group.getGroupName()+"(共享)");
							arr.set(i, group);
						}
					else{
						arr.remove(i);
						i--;
					}
				}else{
					if(staffId.toUpperCase().equals("ADMIN")&&!group.getStaffId().trim().toUpperCase().equals("ADMIN")){
						group.setName(group.getGroupName()+"("+group.getStaffId()+"私有)");
					}else group.setName(group.getGroupName());
					
					arr.set(i, group);
				}
			}
		}
		return arr;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<Group> findAllGroupShare(String eid, String staffId) {
//oracle 写法
		String sql ="select groupid,groupname,groupname+'('+staffId+'共享)' as name," +
		"isShare,replyContent from linGroup where eid=? and staffId<>? and isShare='Y'";
		List list=new ArrayList();
		list.add(eid);
		list.add(staffId);
		ILinkmanDao dao = new LinkmanDao();
		List<Group> arr=dao.findAllGroup(sql,list);
		return arr;
	}
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public int addLinGroup(String eid, String groupid, String groupname,
			String staffId,String isShare,String replyContent) throws NoteException {
		IBasicSettingDao dao =new BasicSettingDao();
		String sql="";
		List list =new ArrayList();
		int s=DbBean.getInt("select count(*) from linGroup where groupname='"+groupname+"' and eid='"+eid+"' and staffId='"+staffId+"'", null);
		
		if(groupid==null||groupid.trim().equals("")){
			if(s>0) throw new NoteException("该组名已存在");
			String id=dao.getId("select max(groupID) from linGroup where EID=? ",new Object[]{eid});
			if(id != null){
				id = String.valueOf(Integer.parseInt(id) + 10001);
				id = id.substring(id.length()-4,id.length());
			}else id="0001";
			sql=" insert into linGroup(groupid,groupname,staffId,eid,isShare,replyContent) values(?,?,?,?,?,?)";
			list.add(id);list.add(groupname);list.add(staffId);list.add(eid);list.add(isShare);list.add(replyContent);
		}else {
			String userid=DbBean.getString("select staffid from linGroup where eid=? and groupid=?", new String[]{eid,groupid});
			if(!staffId.equals(userid))throw new NoteException("只能修改自己的群组属性！");
			sql="update linGroup set groupname=?,isShare=?,replyContent=? where eid=? and groupid=?";
			list.add(groupname);list.add(isShare);list.add(replyContent);list.add(eid);list.add(groupid);
			
		}
		int a=dao.excute(sql,list);
		return a;
	}
	@SuppressWarnings("unchecked")
	public int delete(String groupid, String eid, String table, String str) {
			
		String sql="delete from "+table+" where eid='"+eid+"' and "+str+"=? ";
		List list=new ArrayList();
		list.add(groupid);
		IBasicSettingDao dao =new BasicSettingDao();
		int a=dao.excute(sql,list);
		return a;
	}
	@SuppressWarnings("unchecked")
	public List<Linkman> findAllLinkman(LinkmanListForm manF, String eid,
			String groupid,String staffId,String flag) {
		StringBuffer sql =new StringBuffer();
		StringBuffer count=new StringBuffer();
		StringBuffer w =new StringBuffer();
		sql.append("select linkid,name,phone, birthday," +
				"eid,sex,post,orgName,userMemo,status,optionalContent,staffId,groupId,ismember from linkman where eid='"+eid+"' ");
		count.append("select count(*) from linkman where eid='"+eid+"' ");
		if(groupid!=null&&!groupid.equals("0")){
			if(groupid.equals("Null")){
				w.append(" and (groupid='Null' or groupId is null ) and staffid='"+staffId+"' ");
			}else{
				w.append(" and groupid='"+groupid+"' ");
			}
				
		}else if(!staffId.trim().toUpperCase().equals("ADMIN")){
			w.append(" and staffid='"+staffId+"' ");
		}
		if(manF.getName()!=null&&!manF.getName().trim().equals("")){
			w.append(" and name like '%"+manF.getName()+"%' ");
		}
		if(manF.getPhone()!=null&&!manF.getPhone().trim().equals("")){
			w.append(" and phone like '"+manF.getPhone()+"%' ");
		}
		if(manF.getBirthday()!=null&&!manF.getBirthday().trim().equals("")){
			w.append(" and birthday='"+manF.getBirthday()+"' ");
		}
		if(manF.getSex()!=null&&!manF.getSex().trim().equals("")){
			w.append(" and sex='"+manF.getSex().replace("'", "''")+"' ");
		}
		if(manF.getUserMemo()!=null&&!manF.getUserMemo().trim().equals("")){
			w.append(" and usermemo like '%"+manF.getUserMemo().replace("'", "''")+"%'");
		}
		sql.append(w);
		count.append(w);
		if(flag!=null&&flag.equals("repout")){
			manF.setPageSize(Integer.MAX_VALUE);
		}else{
			manF.setTotalItems(DbBean.getInt(count.toString(), null));
			manF.adjustPageIndex();
		}
		
	
		
		List<Linkman> lists= DbBean.select(sql.toString(), null, manF.getStartIndex(), manF.getPageSize(), Linkman.class);
		return lists;
	}
	@SuppressWarnings("unchecked")
	public int addLinkman(String eid, String staffId, String linkid, String name,
			String phone, String groupid, String sex, String birthday,
			String post, String orgName, String userMemo, String optionalContent) throws NoteException {
		IBasicSettingDao dao =new BasicSettingDao();
		LoadPhoneService lps=new LoadPhoneService();
		int phoneType=lps.IsPhone(phone.trim());
		if(phoneType<0)throw new NoteException("添加的号码格式不正确!");
		String sql="";
		List list =new ArrayList();
		if(linkid==null||linkid.trim().equals("")){
			sql="insert into linkman(name,phone,groupid,sex,birthday,post,orgName,userMemo,optionalContent,eid,staffId,status,phoneType)" +
				" values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			list.add(name);list.add(phone);list.add(groupid);list.add(sex);list.add(birthday);list.add(post);
			list.add(orgName);list.add(userMemo);list.add(optionalContent);list.add(eid);list.add(staffId);list.add("1");list.add(phoneType);
		}else {
			sql="update linkman set name=?,phone=?,groupid=?,sex=?,birthday=?,post=?,orgName=?,userMemo=?,optionalContent=?,phoneType=? where eid=? and linkid=? ";
			list.add(name);list.add(phone);list.add(groupid);list.add(sex);list.add(birthday);list.add(post);
			list.add(orgName);list.add(userMemo);list.add(optionalContent);list.add(phoneType);list.add(eid);list.add(linkid);
		}
		int a=dao.excute(sql,list);
		return a;
	}
	public Linkman findLinkman(String eid, String linkid) throws NoteException {
		ILinkmanDao dao =new LinkmanDao();
		String sql ="select linkid,name,phone, birthday," +
				"eid,sex,post,orgName,userMemo,status,optionalContent,staffId,groupId,ismember from linkman where eid=? and linkid=?";
		Linkman man=dao.findLinkman(sql,new Object[]{eid,linkid});
		if(man==null)
			throw new NoteException("该记录不存在或已被删除！");
		return man;
	}
	@SuppressWarnings("unchecked")
	public int change(String[] ids, String EId, String status,String[] phs) {
		DbBean db = new DbBean();
		Connection conn=null;
		Statement stm=null;
		int a=0;
		try{
			conn=db.getConnection();
			conn.setAutoCommit(false);
			stm=conn.createStatement();
			for(int i=1;i<ids.length;i++){
				stm.addBatch("update linkman set status='"+status+"' where eid='"+EId+"' and linkid='"+ids[i]+"'");
				a++;
			}
			for(int i=0;i<phs.length;i++){
				if(phs[i]!=null&&phs[i].trim().length()>10){
					if(status.equals("0")){
						stm.addBatch(" insert into black(blackPhoneNum,addTime,eid) values('"+phs[i]+"','"+TimeUtil.now()+"','"+EId+"') ");
					}else{
						stm.addBatch(" delete from black where eid='"+EId+"' and blackPhoneNum='"+phs[i]+"'");
					}
				}
			}
			stm.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}catch(Exception e){
			e.printStackTrace();
			db.rollback(conn);
			db.close(stm, conn);
			return 0;
		}finally{
			db.close(stm, conn);
			}
		return  a;
		}
	@SuppressWarnings("unchecked")
	public int deleteLinkman(String[] ids, String eid,String flag,String groupid,String staffId) {
		 
		DbBean db = new DbBean();
		Connection conn=null;
		Statement stm=null;
		int a=0;
		try{
			
			if(flag!=null&&!flag.trim().equals("all")){
				 
				for(int i=1;i<ids.length;i++){
				 
					if(staffId.toLowerCase().equals("admin")){
						int b=db.executeUpdate("delete from linkman where eid='"+eid+"' and linkid='"+ids[i]+"'", null);
						if(b>0)a++;
					}else {
						int b=db.executeUpdate("delete from linkman where eid='"+eid+"' and linkid='"+ids[i]+"' and staffid='"+staffId+"'", null);
						if(b>0)a++;
					}
					
				}
				 
			}else{
				conn=db.getConnection();
				conn.setAutoCommit(false);
				stm=conn.createStatement();
				if(groupid!=null&&!groupid.equals("0")){
					a=stm.executeUpdate(" delete from linkman where eid='"+eid+"' and groupid='"+groupid+"'");
				}else{
					if(!staffId.toLowerCase().equals("admin"))
						a=stm.executeUpdate(" delete from linkman where eid='"+eid+"' and staffid='"+staffId+"'");
					else a=stm.executeUpdate(" delete from linkman where eid='"+eid+"'");
				}
				conn.commit();
				conn.setAutoCommit(true);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			db.rollback(conn);
			db.close(stm, conn);
			return 0;
		}finally{
			db.close(stm, conn);
			}
		return  a;
	}
	public List<PhoneBean> findPhoneBean(String[] ids, String eid) {
		List<PhoneBean> list =new ArrayList<PhoneBean>();
		LoadPhoneService lps=new LoadPhoneService();
		ILinkmanDao dao = new LinkmanDao();
		for(int i=1;i<ids.length;i++){
			String sql=" select phone,name,optionalContent as content from linkman where eid='"+eid+"' and linkid='"+ids[i]+"' and status='1' ";
			PhoneBean phoneB= dao.findPhone(sql);
			if(phoneB==null||phoneB.getPhone().trim()==null)continue;
			int a=lps.IsPhone(phoneB.getPhone());
			if(a==-1)continue;
			phoneB.setPhoneType(a);
			list.add(phoneB);
		}
		return list;
	}
	public void changeGroups(String groupid, String eid, Object object) {
		DbBean.executeUpdate("update linkman set groupid='' where eid='"+eid+"' and groupid='"+groupid+"' ", null);
	}
}
