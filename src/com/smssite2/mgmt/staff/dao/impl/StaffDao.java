package com.smssite2.mgmt.staff.dao.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.note.NoteException;
import com.note.bean.Account;
import com.note.bean.AddMoneyLog;
import com.note.bean.Channel;
import com.note.bean.Grant;
import com.note.bean.Log;
import com.note.bean.Role;
import com.note.bean.Staff;
import com.note.bean.Team;
import com.note.common.DbBean;
import com.note.common.TimeUtil;
import com.smssite2.mgmt.linkman.bean.Group;
import com.smssite2.mgmt.staff.bean.CheckMoney;
import com.smssite2.mgmt.staff.bean.StaffBean;
import com.smssite2.mgmt.staff.dao.IStaffDao;
import com.smssite2.mgmt.staff.form.AccountForm;
import com.smssite2.mgmt.staff.form.AdminListForm;
import com.smssite2.mgmt.staff.form.LogForm;
import com.smssite2.mgmt.staff.form.MoneyForm;
import com.smssite2.mgmt.staff.form.OrgListForm;
import com.smssite2.mgmt.staff.form.ReportListForm;

public class StaffDao implements IStaffDao{	private static final Logger LOG= Logger.getLogger(StaffDao.class);
	@SuppressWarnings("unchecked")
	public Map findAllOrg(String EId){
		String sql="select * from UserTeam where EId=?";
		List<Team> list=DbBean.select(sql, new Object[]{EId}, 0, Integer.MAX_VALUE, Team.class);
		Map map =new HashMap();
		for(Team org:list){
		map.put(org.getTid(), org.getTname());
		}
		return map;
	}
	@SuppressWarnings("unchecked")
	public Map findAllRoute(){
		String sql="select * from Channel";
		List<Channel> list=DbBean.select(sql,null,0,10000,Channel.class);
		Map map =new HashMap();
		for(Channel c:list){
			map.put(c.getRouteType(), c.getRouteName());
		}
		return map;
	}
	@SuppressWarnings("unchecked")
	public List<Role> findAllRole(String EId){
		String sql="select * from userPost where EId=?";
		List<Role> list=DbBean.select(sql, new Object[]{EId}, 0, Integer.MAX_VALUE, Role.class);
		return list;
		
	}
	@SuppressWarnings("static-access")
	public Account queryAccountByEId(String EId) throws NoteException {
		DbBean db=new DbBean();
		String sql="select * from Sms_Basicinf  where lower(EId)='"+EId+"'";
		 
		try{
			Account account =(Account)db.selectFirst(sql, null, Account.class);
			return account;
		}catch(Exception e){
			e.printStackTrace();
			throw new NoteException("数据库连接超时,或网络故障!");
		}
		
	}
	@SuppressWarnings("static-access")
	public Staff queryAdmin(String EId, String staffId) {
		DbBean  db =new DbBean();
		String sql="select * from UserList where lower(userId)=? and lower(Eid)=? ";
		Object[] params=new Object[]{staffId,EId};
		Staff staff=null;
		try {
			 staff=(Staff) db.selectFirst(sql, params, Staff.class);
		} catch (Exception e) {
			e.printStackTrace();
		}/*finally{
			db.close();
		}*/
		return staff;
	}
	public void AddUserBlack(){
		
	}
	@SuppressWarnings({ "unchecked", "static-access" })
	public List<StaffBean> queryStaffs(AdminListForm adminList, String EId) {
		DbBean  db =new DbBean();
		List<StaffBean> list=new ArrayList<StaffBean>();
		List params=new ArrayList();
		params.add(EId);
		StringBuffer sb=new StringBuffer();
		StringBuffer w=new StringBuffer();
		StringBuffer count =new StringBuffer();
		sb.append("select staff.userId, staff.userName,staff.upost,org.tname,staff.uvalid,staff.smsNum ,staff.smsSign,staff.Isadmin " +
				" from userList staff LEFT  JOIN  userTeam org ON (staff.Uteam=org.tID and staff.EId=org.EId) where   " +
				"   staff.EId=?  and lower(staff.userId)<>'admin' ");
		
		count.append("select count(staff.userId) from userlist staff,userTeam org where  staff.Uteam=org.tID" +
				" and staff.EId=org.EId and staff.EId=? and lower(staff.userId)<>'admin' ");
		if(adminList.getRealName()!=null&&!adminList.getRealName().trim().equals("")){
			w.append(" and staff.userName like ? ");
			params.add("%"+adminList.getRealName().trim()+"%");
		}
		if(adminList.getUserName()!=null&&!adminList.getUserName().trim().equals("")){
			w.append(" and staff.userId like ? ");
			params.add("%"+adminList.getUserName().trim()+"%");
		}
		if(adminList.getRoleName()!=null&&!adminList.getRoleName().trim().equals("")){
			w.append(" and staff.upost like ? ");
			params.add("%"+adminList.getRoleName().trim()+"%");
		}
		if(adminList.getOrgName()!=null&&!adminList.getOrgName().trim().equals("")){
			w.append(" and org.tname like ? ");
			params.add("%"+adminList.getOrgName().trim()+"%");
		}
		if(w!=null&&w.length()>0){
			sb.append(w);
			count.append(w);
		}
		sb.append(" order by staff.userId ");
		adminList.setTotalItems(DbBean.getInt(count.toString(), params.toArray()));
		adminList.adjustPageIndex();
		
		list=db.select(sb.toString(), params.toArray(), adminList.getStartIndex(), adminList.getPageSize(), StaffBean.class);
		if(list==null||list.size()==0)return null;
		return list;
	}
	@SuppressWarnings("static-access")
	public int modifyPW(String newPw, String EId, String userId)
			throws NoteException {
		DbBean db = new DbBean();
		String sql="update  userlist set password=? where EId=? and userId=?";
		Object[] params = new Object[]{newPw,EId,userId};
		try{
			int a=db.executeUpdate(sql, params);
			return a;
		}catch(Exception e){
			db.rollback();
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Log> queryListLog(LogForm logF, String EId) throws ParseException {
		StringBuffer  sql= new StringBuffer();
		StringBuffer  w=new StringBuffer();
		StringBuffer count=new StringBuffer();
		List list =new ArrayList();
		sql.append("select * from smslog log where  log.EId=? ");
		count.append("select count(*) from smslog log where log.eid=? ");
		list.add(EId);
		if(logF.getStartTime()!=null&&!logF.getStartTime().trim().equals("")){
			w.append(" and log.logTime>=? ");
			list.add(logF.getStartTime());
		}
		if(logF.getEndTime()!=null&&!logF.getEndTime().equals("")){
			w.append(" and log.logTime<?");
			list.add(logF.getEndTime()+" 24:00");
		}
		if(logF.getStaffId()!=null&&!logF.getStaffId().trim().equals("")){
			w.append(" and log.staffId like ?");
			list.add("%"+logF.getStaffId()+"%");
		}
		if(logF.getActionType()!=null&&!logF.getActionType().trim().equals("")){
			w.append(" and log.actionType like ?");
			list.add("%"+logF.getActionType().trim()+"%");
		}
		if(w.length()>0){
			sql.append(w);
			count.append(w);
		}
		sql.append(" order by log.logTime desc");
		logF.setTotalItems(DbBean.getInt(count.toString(), list.toArray()));
		logF.adjustPageIndex();
		List<Log> list1=DbBean.select(sql.toString(), list.toArray(), logF.getStartIndex(), logF.getPageSize(), Log.class);
		return list1;
	}

	public Log queryLogById(String logId) {
		String sql="select * from smslog  where  logId=? ";
	 return (Log)DbBean.selectFirst(sql, new Object[]{logId}, Log.class);
	}
	@SuppressWarnings("unchecked")
	public List<Team> queryOrgsByEId(OrgListForm orgList, String EId) {
		StringBuffer sql=new StringBuffer();
		StringBuffer w= new StringBuffer();
		StringBuffer count=new StringBuffer();
		sql.append(" select * from UserTeam where EId=?");
		count.append("select count(tid) from UserTeam where EId=? ");
		List params=new ArrayList();
		params.add(EId);
		if(orgList.getOrgName()!=null&&!orgList.getOrgName().trim().equals("")){
			w.append(" and Tname like ? ");
			params.add("%"+orgList.getOrgName().trim()+"%");
		}
		if(w!=null&&w.length()>0){
		sql.append(w);
		count.append(w);
		}
		sql.append(" order by tid");
		orgList.setTotalItems(DbBean.getInt(count.toString(), params.toArray()));
		orgList.adjustPageIndex();
		
		List<Team> list=DbBean.select(sql.toString(), params.toArray(), orgList.getStartIndex(),orgList.getPageSize(), Team.class);
		return list;
	}
	@SuppressWarnings("static-access")
	public int saveOrg(String orgId, String orgName, String orgMemo, String EId) {
		String sql="insert into userTeam(tid,tname,tmemo,eid) values(?,?,?,?)";
		DbBean db=new DbBean();
		try{
		 int a=db.executeUpdate(sql, new Object[]{orgId,orgName,orgMemo,EId});
		 return a;
		}catch(Exception e){
			db.rollback();
			e.printStackTrace();
			return 0;
		}
	}
	public Team queryOrgByOrgId(String tid,String EId) {
		String sql="select * from userTeam where tid=? and eid=?";
		Team org=(Team) DbBean.selectFirst(sql, new Object[]{tid,EId}, Team.class);
		return org;
	}
	public Team queryOrgByName(String EId, String tname) {
		String sql="select * from UserTeam where EId=? and tname=?";
		Team org=(Team) DbBean.selectFirst(sql, new Object[]{EId,tname}, Team.class);
		return org;
	}
	@SuppressWarnings("static-access")
	public int  updateOrg(String orgId, String orgName, String orgMemo,String EId) {
		String sql="update userTeam set tname=?,tmemo=? where tid=? and eid=?";
		DbBean db =new DbBean();
		try{
			int a =db.executeUpdate(sql, new Object[]{orgName,orgMemo,orgId,EId});
			return a;
		}catch(Exception e){
			db.rollback();
			e.printStackTrace();
			return 0;
		}
	}
	@SuppressWarnings({ "static-access", "unchecked" })
	public int  deleteOrg(String[] ids,String EId) {
		StringBuffer sql=new StringBuffer();
		List params=new ArrayList();
		for(int i=0;i<ids.length;i++){
			if(ids[i].equals("-1"))continue;
			sql.append(" delete from userTeam where tid=? and Eid=?;");
			params.add(ids[i]);
			params.add(EId);
		}
		DbBean db= new DbBean();
		try{
			int a=db.executeUpdate(sql.toString(), params.toArray());
			return a;
		}catch(Exception e){
			db.rollback();
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	public void changeLocksign(String[] ids, String locksign,String EId,String flag) {
		String sql="";
		
		if(flag.equals("staff")){
			for(int i=0;i<ids.length;i++){
				sql="update userList set uvalid='"+locksign+"' where userId='"+ids[i]+"' and Eid='"+EId+"'";
				DbBean.executeUpdate(sql,null);
			}
		 }else if(flag.equals("account")){
			 for(String eid:ids){
				 if(eid!=null&&eid.trim().length()>0){
				  sql="update Sms_Basicinf set uvalid='"+locksign+"' where  Eid='"+eid+"'";
				  DbBean.executeUpdate(sql,null);
				 }
			 }
		 }
		
	}
	@SuppressWarnings({ "static-access", "unchecked" })
	public int deleteStaff(String[] ids,String EId) {
		StringBuffer sql=new StringBuffer();
		List params=new ArrayList();
		for(int i=0;i<ids.length;i++){
			if(ids[i].equals("-1"))continue;
			sql.append(" delete from userList where userId=? and Eid=? ;");
			params.add(ids[i]);
			params.add(EId);
		}
		DbBean db=new DbBean();
		try{
		int a=db.executeUpdate(sql.toString(), params.toArray());
		return a;
		}catch(Exception e){
			db.rollback();
			e.printStackTrace();
			return 0;
		}
	}

	public Staff queryStaffByStaffId(String staffId) {
		String sql="select * from Staff where staffId=?";
		return (Staff)DbBean.selectFirst(sql, new Object[]{staffId}, Staff.class);
	}
	public Staff findStaffByUserId(String userId, String EId) {
		String sql="select * from userList where lower(userId)=? and lower(EId)=?";
		 
		return (Staff)DbBean.selectFirst(sql, new Object[]{userId.toLowerCase(),EId.toLowerCase()}, Staff.class);
	}
	@SuppressWarnings("static-access")
	public void saveStaff(String EId, String userName, String realName,
			String password, String orgId, String roleId, String memo) {
		String sql="insert into Staff(EId,userName,realName,password,orgId,roleId,memo,locksign)" +
				" values(?,?,?,?,?,?,?,?)";
		Object[] params=new Object[]{EId,userName,realName,password,Integer.parseInt(orgId),Integer.parseInt(roleId),memo,1};
		DbBean db = new DbBean();
		try{
		db.executeUpdate(sql, params);
		
		}catch(Exception e){
			db.rollback();
		}
	}
	@SuppressWarnings("unchecked")
	public List findAllPrivilegeByRoleId(String EId,String roleId){
		
		String sql="select * from Grant where EId=? and roleId=?";
		List<Grant> grants=	DbBean.select(sql, new Object[]{EId,Integer.parseInt(roleId)}, 0, Integer.MAX_VALUE, Grant.class);
		List list=new ArrayList();
		for(Grant g:grants){
			list.add(g.getPrivilegeId());
		}
		return list;
	}
	@SuppressWarnings({ "static-access", "unchecked" })
	public void addStaffGrant(String EId, Integer staffId,String roleId) {
		List list=this.findAllPrivilegeByRoleId(EId,roleId);
		StringBuffer sql=new StringBuffer();
		List params=new ArrayList();
		for(Object o:list){
			sql.append(" insert into StaffGrant(EId,staffId,privilegeId) values(?,?,?);");
			params.add(EId);
			params.add(staffId);
			params.add(o);
		}
		DbBean db =new DbBean();
		try{
		db.executeUpdate(sql.toString(), params.toArray());
		}catch(Exception e){
			db.rollback();
			e.printStackTrace();
		}
	}
	public String findOrgId(String EId) {
		Team t=(Team) DbBean.selectFirst("select * from userTeam where eid=? order by tid desc", new Object[]{EId}, Team.class);
		if(t==null)return "000";
		return t.getTid();
	}
	@SuppressWarnings("static-access")
	public int  saveOrUpdateStaff(String sql, Object[] params) {
		DbBean db= new DbBean();
		try{
			int a=db.executeUpdate(sql, params);
		return a;
		}catch(Exception e){
			db.rollback();
			e.printStackTrace();
			return 0;
		}
		
	}
	public Role findRoleByName(String upost, String id) {
		String sql="select * from userPost where uname=? and EId=?";
		return	(Role)DbBean.selectFirst(sql, new Object[]{upost,id}, Role.class);
		 
	}
	@SuppressWarnings("unchecked")
	public List<Account> findAllAcount(StringBuffer sql, StringBuffer count,
			List list, AccountForm accF) {
		try{
			accF.setTotalItems(DbBean.getInt(count.toString(), list.toArray()));
			accF.adjustPageIndex();
			return DbBean.select(sql.toString(), list.toArray(), accF.getStartIndex(), accF.getPageSize(), Account.class);
				}catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}
	public Account findAccountBySmsNum(String smsNum) {
		String sql ="select * from Sms_Basicinf  where smsNum=?";
		
		return (Account) DbBean.selectFirst(sql, new Object[]{smsNum}, Account.class);
	}
	@SuppressWarnings("static-access")
	public int addAccount(String sql, Object[] array) {
		DbBean db= new DbBean();
		try{
			int a=db.executeUpdate(sql, array);
			 
		return a;
		}catch(Exception e){
			db.rollback();
			e.printStackTrace();
			return 0;
		}
		
	}
	@SuppressWarnings("static-access")
	public int deleteAccount(String sql, String[] ids) {
		DbBean db= new DbBean();
		try{
			int a=db.executeUpdate(sql, ids);
		return a;
		}catch(Exception e){
			db.rollback();
			e.printStackTrace();
			return 0;
		}
	}
	@SuppressWarnings("static-access")
	public int resetPwd(String sql, Object[] array) {
		DbBean db= new DbBean();
		try{
			int a=db.executeUpdate(sql, array);
		return a;
		}catch(Exception e){
			db.rollback();
			e.printStackTrace();
			return 0;
		}
	}
	@SuppressWarnings("static-access")
	public int addMoney(String sql, Object[] params) {
		DbBean db= new DbBean();
		try{
			int a=db.executeUpdate(sql, params);
		return a;
		}catch(Exception e){
			db.rollback();
			e.printStackTrace();
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	public List<CheckMoney> findUnckecklistM(StringBuffer sql,
			StringBuffer count, MoneyForm moneyF) {
		moneyF.setTotalItems(DbBean.getInt(count.toString(), null));
		moneyF.adjustPageIndex();
		return DbBean.select(sql.toString(), null, moneyF.getStartIndex(), moneyF.getPageSize(), CheckMoney.class);
	}
	@SuppressWarnings("static-access")
	public int execute(String sql, Object[] ids) {
		DbBean db= new DbBean();
		try{
			int a=db.executeUpdate(sql, ids);
		return a;
		}catch(Exception e){
			db.rollback();
			e.printStackTrace();
			return 0;
		}
	}
	public AddMoneyLog findAddMoney(String sql, String[] params) {
		return (AddMoneyLog) DbBean.selectFirst(sql, params, AddMoneyLog.class);
	}
	@SuppressWarnings("unchecked")
	public ArrayList select(StringBuffer sql, StringBuffer count, List list,
			ReportListForm repF, Class clazz) {
		if(count!=null){
			repF.setTotalItems(DbBean.getInt(count.toString(), list.toArray()));
			repF.adjustPageIndex();
		}
		return DbBean.select(sql.toString(), list.toArray(), repF.getStartIndex(), repF.getPageSize(), clazz);
	}
	@SuppressWarnings("unchecked")
	public String getString(String sql) {
		return DbBean.getString(sql,null);
	}
	public void changeIsSmsSign(String[] ids, String locksign ) {
		for(String eid:ids){
			 if(eid!=null&&eid.trim().length()>0){
			 String sql="update Sms_Basicinf set issmssign='"+locksign+"' where  Eid='"+eid+"'";
			  DbBean.executeUpdate(sql,null);
			 }
		 }
		
	}
	public List<Group> findAllGroup(String eid) {
		List<Group> groups = null;
		try{
			String sql = "select * from Lingroup where eid=?";
			List params = new ArrayList();
			params.add(eid);
			groups = DbBean.select(sql, params.toArray(new Object[params.size()]), 0, Integer.MAX_VALUE, Group.class);
			
		}catch(Exception e){
			LOG.error(e.getMessage(),e);
		}
		return groups;
	}
}
