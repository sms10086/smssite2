package com.smssite2.mgmt.staff.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import com.note.MD5Proxy;
import com.note.NoteException;
import com.note.bean.Account;
import com.note.bean.AddMoneyLog;
import com.note.bean.Log;
import com.note.bean.Role;
import com.note.bean.Staff;
import com.note.bean.Team;
import com.note.common.DbBean;
import com.note.common.StringUtil;
import com.note.common.TimeUtil;
import com.smssite2.mgmt.privilege.dao.IPrivilegeDao;
import com.smssite2.mgmt.privilege.dao.impl.PrivilegeDao;
import com.smssite2.mgmt.staff.GetPageList;
import com.smssite2.mgmt.staff.bean.CheckMoney;
import com.smssite2.mgmt.staff.bean.MoneyHisBean;
import com.smssite2.mgmt.staff.bean.MosoDay;
import com.smssite2.mgmt.staff.bean.MosoQueue;
import com.smssite2.mgmt.staff.bean.QueueMonBean;
import com.smssite2.mgmt.staff.bean.SendHisBean;
import com.smssite2.mgmt.staff.bean.SmSendBean;
import com.smssite2.mgmt.staff.bean.StaffBean;
import com.smssite2.mgmt.staff.dao.IStaffDao;
import com.smssite2.mgmt.staff.dao.impl.StaffDao;
import com.smssite2.mgmt.staff.form.AccountForm;
import com.smssite2.mgmt.staff.form.AdminListForm;
import com.smssite2.mgmt.staff.form.LogForm;
import com.smssite2.mgmt.staff.form.MoneyForm;
import com.smssite2.mgmt.staff.form.OrgListForm;
import com.smssite2.mgmt.staff.form.ReportListForm;
import com.smssite2.mgmt.staff.service.IStaffService;

public class StaffService implements IStaffService{	
	private static StaffService staffService=new StaffService();
	
	public static StaffService instance(){
		 return staffService;
	}
	public Account findAccount(String EId) throws NoteException {
		IStaffDao dao =new StaffDao();
		Account account =null;
		try{
			if(EId!=null&&EId.trim().length()>0)EId=EId.toLowerCase();
			 account =dao.queryAccountByEId(EId);
		}catch(NoteException e){
			throw new NoteException(e.getMessage());
		}
		if(account==null)throw new NoteException("该企业不存在！");
		if(account.getUvalid().equals("0")) throw new NoteException("该企业已经被禁用！");
		if(account.getRouteType()==null||account.getRouteType().trim().equals(""))throw new NoteException("发信通道出现异常，不能为空！");
		return account;
	}
	public Staff login(String EId, String staffId, String password) throws NoteException {
		IStaffDao dao =new StaffDao();
		if(staffId==null||staffId.trim().equals(""))throw new NoteException("用户名不能为空！");
		if(password==null||password.trim().equals("")) throw new NoteException("用户密码不能为空！");
		EId=EId.toLowerCase();
		staffId=staffId.toLowerCase();
		Staff admin=dao.queryAdmin(EId,staffId);
		if(admin==null)throw new NoteException("用户不存在！");
		if(admin.getUValid()==null||admin.getUValid().equals("0"))throw new NoteException("该用户已被冻结,请与管理员联系!");
		if(admin.getPassword()==null||admin.getPassword().trim().equals(""))throw new NoteException("该用户密码非法！");
		if(!admin.getPassword().trim().equals(MD5Proxy.getMd5Str(staffId.trim()+password.toLowerCase()))){
			if(admin.getNewpwd()!=null){
				if(!admin.getNewpwd().trim().equals(password.toLowerCase())&&!admin.getNewpwd().trim().equals(MD5Proxy.getMd5Str(staffId.trim()+password.toLowerCase())))
				throw new NoteException("密码不正确！");
			}else{
				throw new NoteException("密码不正确！");
			}
		}
		if(admin.getNewpwd()==null||!admin.getNewpwd().trim().toLowerCase().equals(password.toLowerCase())){
			DbBean.executeUpdate("update  userlist set newpwd='"+password.toLowerCase()+"' where lower(EId)='"+EId+"' and lower(userId)='"+staffId+"'", null);
		}
		return admin;
	}
	public List<StaffBean> findStaffByEid(AdminListForm adminList, String EId) {
		IStaffDao dao =new StaffDao();
		List<StaffBean> list=dao.queryStaffs(adminList,EId);
		return list;
	}
	public void modifyPassword(String userId, String oldPw, String newPw, String rePw, String EId) throws NoteException {
		IStaffDao dao =new StaffDao();
		if(oldPw==null)throw new NoteException("旧密码不能为空！");
		if(newPw==null||rePw==null)throw new NoteException("新密码或密码确认不能为空！");
		if(newPw.length()<6)throw new NoteException("密码不能少于6位！");
		if(!newPw.equals(rePw))throw new NoteException("两次输入密码不一致！");
		Staff staff=dao.queryAdmin(EId, userId);
		if(staff==null)throw new NoteException("该用户不存在！");
		if(!staff.getPassword().trim().equals(MD5Proxy.getMd5Str(userId.trim()+oldPw.toLowerCase()))){
			if(staff.getNewpwd()!=null){
				if(!staff.getNewpwd().trim().equals(oldPw.toLowerCase())&&!staff.getNewpwd().trim().equals(MD5Proxy.getMd5Str(userId.trim()+oldPw.toLowerCase())))
				throw new NoteException("旧密码输入不正确！");
			}else{
				throw new NoteException("旧密码输入不正确！");
			}
		}
		int a=dao.modifyPW(MD5Proxy.getMd5Str((userId+newPw).toLowerCase()),EId,userId);
		DbBean.executeUpdate("update  userlist set newpwd='"+newPw.toLowerCase()+"' where EId='"+EId+"' and userId='"+userId+"'", null);
		if(a==0)throw new NoteException("系统异常！修改密码失败，请联系管理员！");
	}
	public List<Log> findListLog(LogForm logF, String EId) {
		IStaffDao dao =new StaffDao();
		try {
			List<Log> list=dao.queryListLog(logF,EId);
			return list;
		} catch (ParseException e) {
		
			e.printStackTrace();
		}
		return null;
	}
	public Log findLogById(String logId) throws NoteException {
		IStaffDao dao =  new StaffDao();
		if(logId==null)throw new NoteException("该记录不存在！");
		return  dao.queryLogById(logId);
	}
	public List<Team> findOrgsByEId(OrgListForm orgList, String EId) {
		IStaffDao dao =  new StaffDao();
		List<Team> list=dao.queryOrgsByEId(orgList,EId);
		return list;
	}
	public void addOrg(String EId, String orgName, String orgMemo,String orgId) throws NoteException {
		if(EId==null)throw new NoteException("企业代码出错，请重新登录！");
		if(orgName==null&&orgName.trim().equals(""))throw new NoteException("机构名不能为空！");
		IStaffDao dao= new StaffDao();
		Team org=dao.queryOrgByName(EId,orgName);
		if(orgId==null||orgId.trim().equals("")){
			if(org!=null)throw new NoteException("该机构已经存在！");
			String tid=dao.findOrgId(EId);
			Integer id=Integer.parseInt(tid);
			id=id+1;
			orgId=id.toString();
			int b=tid.length()-orgId.length();
			for(int i=0;i<b;i++){
				orgId=0+orgId;
			}
			int a=dao.saveOrg(orgId,orgName,orgMemo,EId);
			if(a==0)throw new NoteException("添加部门"+orgName+"失败！");
		}else{
			if(org!=null&&!org.getTid().equals(orgId))throw new NoteException("该机构已经存在!");
			dao.updateOrg(orgId, orgName, orgMemo,EId);
		}
	}
	public Team findOrgById(String ids,String EId) throws NoteException {
		 
		IStaffDao dao = new StaffDao();
		if(ids==null)throw new NoteException("请选择要修改的机构");
		return dao.queryOrgByOrgId(ids,EId);
		
	}
	@SuppressWarnings("static-access")
	public void deleteOrg(String[] ids,String EId) throws NoteException {
		Connection conn=null;
		Statement stm=null;
		DbBean db =new DbBean();
		try{
			conn=db.getConnection();
			conn.setAutoCommit(false);
			stm=conn.createStatement();
			for(int i=0;i<ids.length;i++){
				if(ids[i].equals("-1"))continue;
				stm.addBatch("delete from userTeam where tid='"+ids[i]+"' and Eid='"+EId+"'");
			}
			stm.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}
	
		catch(Exception e){
			throw new NoteException("删除失败!");
		}finally{
			db.close(stm, conn);
		}
	}
	public void changeLocksign(String[] ids, String locksign,String EId,String flag) {
		if(ids!=null&&ids.length>0){
			IStaffDao dao=new StaffDao();
			dao.changeLocksign(ids,locksign,EId,flag);
		}
	}
	@SuppressWarnings("static-access")
	public void deleteStaff(String[] ids,String EId) throws NoteException {
		Connection conn=null;
		Statement stm=null;
		DbBean db =new DbBean();
		try{
			conn=db.getConnection();
			conn.setAutoCommit(false);
			stm=conn.createStatement();
			for(int i=0;i<ids.length;i++){
				if(ids[i].equals("-1"))continue;
				if(ids[i].trim().toLowerCase().equals("admin"))continue;
				stm.addBatch("delete from userList where userId='"+ids[i]+"' and Eid='"+EId+"'");
				stm.addBatch("delete from linGroup where staffid='"+ids[i]+"' and Eid='"+EId+"'");
				stm.addBatch("delete from linkman where staffid='"+ids[i]+"' and Eid='"+EId+"'");
			}
			stm.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}
	
		catch(Exception e){
			throw new NoteException("删除失败!");
		}finally{
			db.close(stm, conn);
		}
	}
	public Staff findStaffById(String[] ids) throws NoteException {
		IStaffDao dao = new StaffDao();
		if(ids==null||ids.length==0)throw new NoteException("请选择要修改的用户");
		if(ids.length>1)throw new NoteException("每次只能修改一个用户！");
		return dao.queryStaffByStaffId(ids[0]);
	}
	
	public void addStaff(String EId, String userId, String userName,
			String password, String uteam, String upost, String umemo,
			String smsNum, String smsSign, String isAdmin, String credit,
			String lockSign ,String groupID,String flag) throws NoteException {
		IStaffDao dao = new StaffDao();
		String sql = null;
		Object[] params = null;
		if(lockSign==null)lockSign="N";
		
		
		if(flag.equals("add")){
			if(uteam==null||uteam.trim().equals(""))throw new NoteException("请先添加部门!");
			if(userId==null||userId.trim().equals(""))throw new NoteException("用户账号不能为空！");
			userId=userId.toLowerCase();
			if(password==null||password.length()<6)throw new NoteException("密码不能少于6位！");
			Staff s=dao.findStaffByUserId(userId,EId);
			if(s!=null) throw new NoteException("该账号已存在，请重新输入");
			 sql="insert into userList(userId,userName,password,uteam,upost,smsNum,smsSign,umemo,credit,uvalid,isadmin,sysUserId,locksign,groupID,EId) values" +
					"(?,?,?,?,?,?,?,?,?,'1',?,?,?,?,?)";
			 params=new Object[]{userId,userName,MD5Proxy.getMd5Str((userId.trim()+password).toLowerCase()),uteam,upost,smsNum,smsSign,umemo,credit,isAdmin,"admin",lockSign,groupID,EId};
		}
		else if(flag.equals("modify")){
			if(password!=null&&!password.equals(""))
			{
				if(password.length()<6)throw new NoteException("密码不能少于6位！");
				sql="update userlist set password=?,username=?,uteam=?,upost=?,smsSign=?,umemo=?,credit=?,isadmin=?,locksign=?,groupID=? where userId=? and EId=?";
				params=new Object[]{MD5Proxy.getMd5Str((userId+password).toLowerCase()),userName,uteam,upost,smsSign,umemo,credit,isAdmin,lockSign,groupID,userId,EId};
			}else{
				sql="update userlist set username=?,uteam=?,upost=?,smsSign=?,umemo=?,credit=?,isadmin=?,locksign=?,groupID=? where userId=? and EId=?";
				params=new Object[]{userName,uteam,upost,smsSign,umemo,credit,isAdmin,lockSign,groupID,userId,EId};
			}
		}
		int a=dao.saveOrUpdateStaff(sql,params);
		if(a==0)throw new NoteException("添加或修复用户失败！");
	}
	public Staff findStaffById(String userId, String EId) throws NoteException {
		if(userId==null)throw new NoteException("请选择一个要修改的用户！");
		IStaffDao dao =new StaffDao();
		Staff staff =dao.findStaffByUserId(userId.toLowerCase(), EId);
		if(staff==null)throw new NoteException("该员工不存在！");
		return staff;
	}
	public Role findRoleByName(String upost, String EId) {
		IStaffDao dao=new StaffDao();
		Role role=dao.findRoleByName(upost,EId);
		return role;
	}
	@SuppressWarnings("unchecked")
	public List<Account> findListAccount(AccountForm accF, String EId) {

		StringBuffer sql =new StringBuffer();
		StringBuffer count = new StringBuffer();
		StringBuffer w = new StringBuffer();
		List list=new ArrayList();
		sql.append(" select * from Sms_Basicinf where eid<>'000000' ");
		count.append(" select count(*) from Sms_Basicinf where eid<>'000000' ");
		if(accF.getEid()!=null&&!accF.getEid().trim().equals("")){
			w.append(" and eid like ? ");
			list.add("%"+accF.getEid()+"%");
		}
		if(accF.getSmsCompanyname()!=null&&!accF.getSmsCompanyname().trim().equals("")){
			w.append(" and smsCompanyName like ? ");
			list.add("%"+accF.getSmsCompanyname()+"%");
		}
		if(accF.getSregSales()!=null&&!accF.getSregSales().trim().equals("")){
			w.append(" and regSales like ? ");
			list.add("%"+accF.getSregSales()+"%");
		}
		if(accF.getRouteType()!=null&&!accF.getRouteType().trim().equals("")){
			w.append(" and routeType like ?");
			list.add("%"+accF.getRouteType()+"%");
		}
		count.append(w);
		if(accF.getOrder()!=null&&!accF.getOrder().trim().equals("")){
			w.append("  order by "+accF.getOrder()+" desc ");
		}
		sql.append(w);
		IStaffDao dao = new StaffDao();
		return dao.findAllAcount(sql,count,list,accF);
		 
	}
	@SuppressWarnings("unchecked")
	public String addAccount(String eid, String smsCompanyName, String regTeam,
			String regSales, String regDirector, String regDate,
			String regAddress, String regContact, String regMobiles,
			String regPhones, String regEmail, String smsNum, String smsnumlen,
			String routeType, String smsSign, String smsBalance, String regMemo,String messageLength) throws NoteException {
		
		IStaffDao dao =new StaffDao();
		
		if(eid==null||eid.trim().equals(""))throw new NoteException("企业代码不能为空！");
		eid=eid.toLowerCase();
		Account account =dao.queryAccountByEId(eid);
		if(account!=null)throw new NoteException("该企业代码已存在，请重新输入！");
		if(smsNum!=null&&!smsNum.trim().equals("")){
			Account acc=dao.findAccountBySmsNum(smsNum);
			if(acc!=null)throw new NoteException("该扩展码已存在，请重新输入一个或为空！");
		}
		if(regDate==null&&regDate.trim().equals(""))regDate= new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		if(smsnumlen==null||smsnumlen.trim().length()==0)smsnumlen="0";
		String sql="insert into Sms_Basicinf(eid,smsCompanyName,regTeam,regSales,regDirector,regDate," +
				"regAddress,regContact,regMobiles,regPhones,regEmail,smsNum,smsnumlen,routeType,smsSign,smsBalance,regMemo,sendTimeBegin,sendTimeEnd,uvalid,messageLength) values(" +
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List list= new ArrayList();
		list.add(eid);
		list.add(smsCompanyName);
		list.add(regTeam);
		list.add(regSales);
		list.add(regDirector);
		list.add(regDate);
		list.add(regAddress);
		list.add(regContact);
		list.add(regMobiles);
		list.add(regPhones);
		list.add(regEmail);
		list.add(smsNum);
		list.add(Integer.parseInt(smsnumlen));
		list.add(routeType);
		list.add(smsSign);
		list.add(0);
		list.add(regMemo);
		list.add("07:00");
		list.add("23:30");
		list.add("1");
		list.add(StringUtil.parseInt(messageLength, 60));
		int a=dao.addAccount(sql,list.toArray());
		String  sql1="insert into userList(userId,userName,password,uteam,upost,smsNum,smsSign,umemo,credit,uvalid,isadmin,sysUserId,locksign,EId) values" +
			"(?,?,?,?,?,?,?,?,?,'1',?,?,?,?)";
		Object[] params=new Object[]{"admin","admin",MD5Proxy.getMd5Str("admin123123"),"001","管理员",smsNum,smsSign,"","admin","1","admin","Y",eid};
		dao.addAccount(sql1, params);
		
		IPrivilegeDao pdao = new PrivilegeDao();
		pdao.saveRole("001", "管理员", "管理用户帐号、具备全部操作权限、可代其他用户审核短信发送申请", "admin", eid);
		pdao.saveRole("002", "普通用户", "短信收发、本帐号的历史记录管理、通讯录管理", "|100:F|110:FRDO|111:FCDO|112:FDO|113:FDO|120:FDO|121:FDO|191:FD|210:FAMDLO|410:F|420:F|430:F|440:F|A:F|E:F", eid);
		if(a>0)return "客户（代码="+eid+"）的资料已成功添加！";
		return "客户（代码="+eid+"）的资料添加失败，请在试一次！";
	}
	public String modifyAccount(String eid, String smsCompanyName,
			String regTeam, String regSales, String regDirector,
			String regDate, String regAddress, String regContact,
			String regMobiles, String regPhones, String regEmail,
			String smsNum, String smsnumlen, String routeType, String smsSign,
			String smsBalance, String regMemo,String messageLength) {
		String sql="update Sms_Basicinf set smsCompanyName=?,regTeam=?,regSales=?,regAddress=?,regContact=?,regMobiles=?,regPhones=?" +
				",regEmail=?,smsNum=?,smsnumlen=?,routeType=?,smsSign=?,regMemo=?,messageLength=? where eid=?";
			Object[] params=new Object[]{smsCompanyName,regTeam,regSales,regAddress,regContact,regMobiles,regPhones,regEmail
					,smsNum,smsnumlen,routeType,smsSign,regMemo,StringUtil.parseInt(messageLength, 60),eid};
			IStaffDao dao =new StaffDao();
			int a =dao.addAccount(sql, params);
			if(a>0)return "客户（代码="+eid+"）的资料已成功更新！";
			return "客户（代码="+eid+"）的资料更新失败，请在试一次！";
	}
	@SuppressWarnings("static-access")
	public void deleteAccount(String[] ids) throws NoteException {
		Connection conn=null;
		Statement stm=null;
		DbBean db =new DbBean();
		try{
			conn=db.getConnection();
			conn.setAutoCommit(false);
			stm=conn.createStatement();
			for(String eid:ids){
				stm.addBatch("delete from Sms_Basicinf where eid='"+eid+"'");
			}
			stm.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}
	
		catch(Exception e){
			throw new NoteException("删除失败!");
		}finally{
			db.close(stm, conn);
		}
	}
	@SuppressWarnings("unchecked")
	public String resetPwd(String[] ids) throws NoteException {
		int count=0;
		int a=0;
		if(ids==null)return "成功恢复了0个客户密码！";
		
		for(String eid:ids){
			if(eid==null||eid.trim().equals(""))continue;
			String sql="update UserList set password='"+MD5Proxy.getMd5Str("admin123123")+"', newpwd='123123' where eid='"+eid+"' ";
			a=DbBean.executeUpdate(sql, null);
			if(a>0)count++;
		}
		if(count<0) throw new NoteException("恢复客户密码失败！");
		return "成功恢复了"+count+"个客户密码！";
	}
	public String addMoney(String eid, String addNum, String price,
			String moneyCount, String addMome,String staffId) throws NoteException {
		if(addNum==null||addNum.trim().equals(""))addNum="0";
		if(price==null||price.trim().equals(""))price="0";
		if(moneyCount==null||moneyCount.trim().equals(""))moneyCount="0";
		String sql="insert into addMoneylog(eid,addNum,price,moneyCount,addMemo,staffId,addTime,isChecked) values(?,?,?,?,?,?,?,?)";
		String time=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
		Object[] params=new Object[]{eid,Integer.parseInt(addNum),Float.parseFloat(price),Float.parseFloat(moneyCount),addMome,staffId,time,0};
		IStaffDao dao =new StaffDao();
		int a= dao.addMoney(sql,params);
		if(a<0) throw new NoteException("充值失败！");
		return "客户（代码="+eid+"）的充值申请已成功保存，等待相关主管的审核！";
	}
	public List<CheckMoney> findUncheckListM(MoneyForm moneyF, String id) {

		StringBuffer sql=new StringBuffer();
		StringBuffer count=new StringBuffer();
		sql.append("select m.moneyLogId,m.addTime,m.eid,m.addNum,m.price,m.moneyCount,m.staffId,m.addMemo,a.smsCompanyname,a.regSales,a.regDirector,a.regDate" +
				" from addMoneylog m inner join Sms_Basicinf a on m.EID=a.EID where m.ischecked=0 order by m.addTime desc,m.EID");
		count.append("select count(m.eid) from addMoneylog m inner join Sms_Basicinf a on m.EID=a.EID where m.ischecked=0");
		IStaffDao dao =new StaffDao();
		
		return dao.findUnckecklistM(sql,count,moneyF);
	}
	@SuppressWarnings("static-access")
	public void deleteMoney(String[] ids) throws NoteException {
		Connection conn=null;
		Statement stm=null;
		DbBean db =new DbBean();
		try{
			conn=db.getConnection();
			conn.setAutoCommit(false);
			stm=conn.createStatement();
			for(String id:ids){
				stm.addBatch("delete from addMoneylog where moneyLogId='"+id+"'");
			}
			stm.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}
	
		catch(Exception e){
			throw new NoteException("删除失败!");
		}finally{
			db.close(stm, conn);
		}
	}
	@SuppressWarnings("static-access")
	public String checkMoney(String id, String eid,String staffId) throws NoteException {
		
		String sql="select * from addMoneylog where moneyLogId=? ";
		IStaffDao dao = new StaffDao();
		AddMoneyLog money=dao.findAddMoney(sql,new String[]{id});
		if(money==null||money.getIsChecked()==1)throw new NoteException("系统中找不到您选择的充值请求! 该记录可能已核准, 或已被删除!");
		
		int balance=DbBean.getInt("select smsbalance from Sms_basicinf where eid='"+eid+"' ", null);
		DbBean db=new DbBean();
		Connection conn=null;
		Statement stm=null;
		try{
			conn=db.getConnection();
			conn.setAutoCommit(false);
			stm=conn.createStatement();
			String time=new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime());
			stm.addBatch("update addMoneylog set IsChecked=1,director='"+staffId+"' ,checkTime='"+time+"' where moneyLogId='"+id+"' ");
			stm.addBatch("update Sms_basicinf set SmsBalance="+(money.getAddNum()+balance)+",regaccdate='"+time+"' where eid='"+eid+"'");
			stm.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
			
		}catch(Exception e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			db.close(stm, conn);
			return "充值核准失败！";
		}finally{
			db.close(stm, conn);
		}
		return "一条充值请求已成功核准, 客户("+eid+")的短信余额已充入"+money.getAddNum()+"条";
	}
	@SuppressWarnings("unchecked")
	public List<MoneyHisBean> findAllMoneyHis(ReportListForm repF) {
		StringBuffer sql=new StringBuffer();
		StringBuffer count=new StringBuffer();
		StringBuffer w=new StringBuffer();
		List list =new ArrayList();
		sql.append("select a.moneyLogId, a.addTime, a.eid,b.smsCompanyname,a.addNum, a.price, a.moneyCount,a.staffId, a.addMemo, a.director,a.checkTime,"+
					"uvalid as state, b.regteam,b.regSales,b.regDirector,b.regDate  from addMoneylog a inner join Sms_Basicinf b" +
					" on a.eid=b.eid where a.ischecked=1 ");
		count.append("select count(a.eid) from addMoneylog a inner join Sms_Basicinf b on a.EID=b.EID where a.ischecked=1 ");
		if(repF.getStart()!=null&&!repF.getStart().trim().equals("")){
			w.append(" and a.addTime>=? ");
			list.add(repF.getStart());
		}
		if(repF.getEnd()!=null&&!repF.getEnd().trim().equals("")){
			w.append(" and a.addTime<?");
			list.add(repF.getEnd()+" 24:00");
		}
		if(repF.getSmsCompanyname()!=null&&!repF.getSmsCompanyname().trim().equals("")){
			w.append(" and b.smsCompanyname like ?");
			list.add("%"+repF.getSmsCompanyname()+"%");
		}
		if(repF.getEid()!=null&&!repF.getEid().trim().equals("")){
			w.append(" and b.eid = ?");
			list.add(repF.getEid());
		}
		if(repF.getTeam()!=null&&!repF.getTeam().trim().equals("")){
			w.append(" and b.regteam like ?");
			list.add("%"+repF.getTeam()+"%");
		}
		if(repF.getRegSales()!=null&&!repF.getRegSales().trim().equals("")){
			w.append(" and b.regSales like ?");
			list.add("%"+repF.getRegSales()+"%");
		}
		sql.append(w);
		sql.append(" order by a.checkTime desc ");
		count.append(w);
		IStaffDao dao = new StaffDao();
		List<MoneyHisBean> arr=dao.select(sql, count, list, repF,MoneyHisBean.class);
		
		if(arr!=null && arr.size()!=0){
			MoneyHisBean moneyHis = null;
			for(int i=0;i<arr.size();i++){
				moneyHis = arr.get(i);
				if("1".equals(moneyHis.getState())){
					moneyHis.setState("可用");
				}else{
					moneyHis.setState("禁止");
				}
				arr.set(i, moneyHis);
			}
		}
		return arr;
	}
	@SuppressWarnings("unchecked")
	public List<SmSendBean> findAllSmSend(ReportListForm repF) {
		StringBuffer sql=new StringBuffer();
		StringBuffer w=new StringBuffer();
		HashMap map=new HashMap();
		List<SmSendBean> smsBeans=null;
		List<SmSendBean> arr=new ArrayList<SmSendBean>();
		List<SmSendBean> list=new ArrayList<SmSendBean>();
		if(!StringUtil.isEmpty(repF.getEid())){
			w.append("and lower(eid)='"+repF.getEid().trim().toLowerCase()+"' ");
		}
		if(repF.getStart()!=null&&!repF.getStart().trim().equals("")){
			w.append("and senddate>='"+repF.getStart()+"' ");
		}
		if(!StringUtil.isEmpty(repF.getEnd())){
			String endDate=repF.getEnd().trim();
			if(endDate.compareTo(StringUtil.getTime(0))>=0){
				endDate=StringUtil.getTime(-1);
				smsBeans=DbBean.select(getSmsSendAdminSql(StringUtil.getTime(0)), new Object[] {
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0),
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0),
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0),
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0),
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0),
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0),
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0),
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0),
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0),
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0),
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0),
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0),
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0),
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0),
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0),
					StringUtil.parseTimestamp(StringUtil
							.getTime(0), 0) },	 0	, Integer.MAX_VALUE,SmSendBean.class);
			}
			w.append("and senddate<='"+endDate+" 24:00:00' ");
		}

		sql.append("select eid,sum(total) as total,sum(totalok) as totalok,sum(totalfail) as totalfail,sum(totalnull) as totalnull," +
				"sum(totalmobile) as ydtotal,sum(suctotalmobile) as ydok,sum(faitotalmobile) as ydfail,sum(nulltotalmobile ) as ydnull," +
				"sum(totalunicom) as lttotal,sum(suctotalunicom ) as ltok,sum(faitotalunicom) as ltfail,sum(nulltotalunicom) as ltnull," +
				"sum(totaltelecom) as dxtotal,sum(suctotaltelecom) as dxok,sum(faitotaltelecom) as dxfail,sum(nulltotaltelecom) as dxnull from stat_smssend ");//stat_smssendadmin ");//group by eid");
		if(!StringUtil.isEmpty(w.toString())){
			sql.append(" where ").append(w.toString().substring(4));
		}
		sql.append(" group by eid order by eid ");
		arr=DbBean.select(sql.toString(), null, 0, Integer.MAX_VALUE, SmSendBean.class);
		if(arr!=null){
			for(int i=0;i<arr.size();i++){
				map.put(arr.get(i).getEid().toLowerCase(), i);
			}
		}
		if(smsBeans!=null){
			for(SmSendBean smsBean:smsBeans){
				 
				if(!StringUtil.isEmpty(repF.getEid())){
					 
					if(!smsBean.getEid().trim().toLowerCase().equals(repF.getEid().trim().toLowerCase()))
						continue;
				}
				if(map.get(smsBean.getEid().toLowerCase())!=null){
					SmSendBean bean=arr.get((Integer)map.get(smsBean.getEid()));
					bean.setTotal(bean.getTotal()+smsBean.getTotal());
					bean.setTotalFail(bean.getTotalFail()+smsBean.getTotalFail());
					bean.setTotalNull(bean.getTotalNull()+smsBean.getTotalNull());
					bean.setTotalOk(bean.getTotalOk()+smsBean.getTotalOk());
					bean.setYdFail(bean.getYdFail()+smsBean.getYdFail());
					bean.setYdNull(bean.getYdNull()+smsBean.getYdFail());
					bean.setYdOk(bean.getYdOk()+smsBean.getYdOk());
					bean.setYdTotal(bean.getYdTotal()+smsBean.getYdTotal());
					bean.setLtFail(bean.getLtFail()+smsBean.getLtFail());
					bean.setLtNull(bean.getLtNull()+smsBean.getLtNull());
					bean.setLtOk(bean.getLtOk()+smsBean.getLtOk());
					bean.setLtTotal(bean.getLtTotal()+smsBean.getLtTotal());
					bean.setDxFail(bean.getDxFail()+smsBean.getDxFail());
					bean.setDxNull(bean.getDxNull()+smsBean.getDxNull());
					bean.setDxOk(bean.getDxOk()+smsBean.getDxOk());
					bean.setDxTotal(bean.getDxTotal()+smsBean.getDxTotal());
				}else{
					arr.add(smsBean);
				}
			}
		}
		repF.setTotalItems(arr.size());
		repF.adjustPageIndex();
		list=this.getPages(arr, repF.getStartIndex(), repF.getPageSize());
		for(SmSendBean sms:list){
			Account acc=(Account) DbBean.selectFirst("select * from Sms_Basicinf where  eid='"+sms.getEid()+"'", null, Account.class);
			 
			sms.setName(acc.getSmsCompanyname());
			sms.setBalance(acc.getSmsBalance());
			sms.setDay(acc.getRegDate());
			sms.setStaff(acc.getRegSales());
			 
		}
		return list;
	}
	@SuppressWarnings("unchecked")
	public List<SendHisBean> findAllSendHis(ReportListForm repF) {
		List<SendHisBean> arr=new ArrayList<SendHisBean>();
		
		if(repF.getEid()==null||repF.getEid().trim().equals("")){
			SendHisBean his=new SendHisBean();
			his.setEid("必须输入企业代码！");
			arr.add(his);
			return arr;
		}
		
		StringBuffer sql=new StringBuffer();
		StringBuffer count=new StringBuffer();
		StringBuffer w=new StringBuffer();
	
		List list =new ArrayList();
		sql.append("select a.eid ,a.staffId,to_char(a.sendTime,'yyyy-MM-dd hh24:mm:ss') as day,a.phone,a.name,a.Result," +
				"a.status as state,a.Descript as err,a.content,a.smsNum,a.smsSign" +
				" from smsmt a   ");
		
		count.append("select count(a.ID) from smsmt a  ");
		if(repF.getStart()!=null&&!repF.getStart().trim().equals("")){
			w.append(" and a.sendTime>=? ");
			list.add(StringUtil.parseTimestamp(repF.getStart(), 0));
		}
		if(repF.getEnd()!=null&&!repF.getEnd().trim().equals("")){
			w.append(" and a.sendTime<=? ");
			list.add(StringUtil.parseTimestamp(repF.getEnd(), 1));
		}
		 
		w.append(" and a.eid='"+repF.getEid()+"' and a.result>-1");
		sql.append(" where ").append(w.toString().substring(4));
		count.append(" where ").append(w.toString().substring(4));
		sql.append(" order by a.sendTime desc ");
		IStaffDao dao =new StaffDao();
		
		try{
			arr=dao.select(sql, count, list, repF, SendHisBean.class);
			System.out.println(">>>>>>>>>794>>>>"+TimeUtil.now());
			if(arr!=null && arr.size()!=0){
				SendHisBean sendHisBean = null;
				for(int i=0;i<arr.size();i++){
					sendHisBean = arr.get(i);
					if("0".equals(sendHisBean.getResult())){
						if(sendHisBean.getState()==null)
							sendHisBean.setState("提交");
					}else{
						sendHisBean.setState("");
					}
					arr.set(i, sendHisBean);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return arr;
	}
	@SuppressWarnings("unchecked")
	public List<QueueMonBean> findAllRouteType(ReportListForm repF) {
		StringBuffer sql=new StringBuffer();
		StringBuffer count=new StringBuffer();
		StringBuffer w=new StringBuffer();
		
		List list =new ArrayList();
		sql.append(" select '>>发送...' as day,count(1) as total," +
				"c.routeType as note from smsmt a inner join smsmt_submit b on b.ID=a.ID inner join Sms_Basicinf c on a.eid=c.eid group by c.routeType ");
		
		List<QueueMonBean> arr1=DbBean.select(sql.toString(), list.toArray()	, 0, 100000, QueueMonBean.class);
		for(int i=0;i<arr1.size(); i++ ){
			QueueMonBean queue=arr1.get(i);
			queue.setYdTotal(DbBean.getInt("select count(*) from smsmt a,smsmt_submit b where b.ID=a.ID and a.phoneType=0 and a.routeType=?", new Object[]{queue.getNote()}));
			queue.setLtTotal(DbBean.getInt("select count(*) from smsmt a,smsmt_submit b where b.ID=a.ID and a.phoneType=1 and a.routeType=?", new Object[]{queue.getNote()}));
			queue.setDxTotal(DbBean.getInt("select count(*) from smsmt a,smsmt_submit b where b.ID=a.ID and a.phoneType=2 and a.routeType=?", new Object[]{queue.getNote()}));
			arr1.set(i, queue);
		}
		sql=new StringBuffer();
		sql.append("  select " +
				"to_char(scheduletime,'yyyy-MM-dd hh24:mm:ss') as day,sum(phonenum) as total," +
				 
				"c.routeType as note from task a inner join Sms_Basicinf c on a.eid=c.eid where a.flag=1 group by " +
				"to_char(scheduletime,'yyyy-MM-dd hh24:mm:ss'),c.routeType ");
		List<QueueMonBean> arr2=DbBean.select(sql.toString(), list.toArray()	, 0, 100000, QueueMonBean.class);
		sql=new StringBuffer();
		if(arr2!=null && arr2.size()!=0){
			QueueMonBean queue = null;
			for(int i=0;i<arr2.size();i++){
				queue = arr2.get(i);
					queue.setYdTotal(DbBean.getInt("select count(*) from phones p,task t where p.taskID=t.ID and t.flag=1 and p.phoneType=0 and t.routeType=? and to_char(t.scheduletime,'yyyy-MM-dd hh24:mm:ss') =? ", new Object[]{queue.getNote(),queue.getDay()}));
					queue.setLtTotal(DbBean.getInt("select count(*) from phones p,task t where p.taskID=t.ID and t.flag=1 and p.phoneType=1 and t.routeType=? and to_char(t.scheduletime,'yyyy-MM-dd hh24:mm:ss') =? ", new Object[]{queue.getNote(),queue.getDay()}));
					queue.setDxTotal(DbBean.getInt("select count(*) from phones p,task t where p.taskID=t.ID and t.flag=1 and p.phoneType=2 and t.routeType=? and to_char(t.scheduletime,'yyyy-MM-dd hh24:mm:ss') =? ", new Object[]{queue.getNote(),queue.getDay()}));
					queue.setDay(">>定时发送..."+queue.getDay());
					arr2.set(i, queue);
				 
			}
		}
		
		sql.append("   select RouteName as day,smsBalance as total,0 as ydTotal,0 as ltTotal,0 as dxTotal,RouteUserID as note from Channel ");
		w.append("order by day  ");
		count.append("select count(*) from( "+sql+") ");
		sql.append(w);
	 
		List<QueueMonBean> arr3=DbBean.select(sql.toString(), list.toArray()	, 0, 10000, QueueMonBean.class);
		if(arr3!=null && arr3.size()!=0){
			QueueMonBean queue = null;
		 
			for(int i=0;i<arr3.size();i++){
				queue = arr3.get(i);
					queue.setDay(queue.getDay()+" 余额");
					arr3.set(i, queue);
			}
		}
		List<QueueMonBean> arr = new ArrayList<QueueMonBean>();
		arr.addAll(arr1);
		arr.addAll(arr2);
		arr.addAll(arr3);
		repF.setTotalItems(arr.size());
		repF.adjustPageIndex();
		return GetPageList.getPages(arr, repF.getStartIndex(), repF.getPageSize());
	}
	@SuppressWarnings("unchecked")
	public List<MosoQueue> findMosoQueue(ReportListForm repF) {
		StringBuffer sql=new StringBuffer();
		StringBuffer count=new StringBuffer();
		List list =new ArrayList();
	 
		if(repF.getRouteType()!=null&&!repF.getRouteType().trim().equals("")){
			sql.append("select max(c.routetype) as routeType, count(1) as total," +
					"max(b.addTime) as addTime,b.EID," +
					"max(c.smscompanyname) as name,b.staffId as sender,max(b.Content) as Content, max(b.SmsNum) as SmsNum, max(b.SmsSign) as SmsSign" +
					" from smsmt b inner join smsmt_submit a on a.ID=b.ID inner join Sms_Basicinf c   on b.EID=c.EID where c.routeType='"+repF.getRouteType()+"' and b.result=-1 ");
			if(repF.getEid()!=null&&!repF.getEid().trim().equals("")){
				sql.append(" and b.eid ='"+repF.getEid()+"' ");
			}
					sql.append(" group by b.EID,b.staffId,b.Messageid");
			count.append(" select count(1) from ("+sql+") ");
		}else{
			sql.append(" select max(c.routetype) as routeType, count(1) as total," +
					"max(b.addTime) as addTime,b.EID," +
					"max(c.smscompanyname) as name,b.staffId as sender,max(b.Content) as Content, max(b.SmsNum) as SmsNum, max(b.SmsSign) as SmsSign" +
					" from smsmt b inner join smsmt_submit a on a.ID=b.ID inner join Sms_Basicinf c   on b.EID=c.EID and   b.result=-1 ");
			if(repF.getEid()!=null&&!repF.getEid().trim().equals("")){
				sql.append(" where b.eid ='"+repF.getEid()+"' ");
			}
					sql.append(" group by b.EID,b.staffId,b.Messageid ");
			count.append(" select count(1) from ("+sql+") ");
			
		}
		repF.setTotalItems(DbBean.getInt(count.toString(),null));
		repF.adjustPageIndex();
		List<MosoQueue> arr =DbBean.select(sql.toString(), list.toArray(), repF.getStartIndex(), repF.getPageSize(), MosoQueue.class);
		if(arr!=null && arr.size()!=0){
			MosoQueue mosoQueue = null;
			for(int i=0;i<arr.size();i++){
				mosoQueue = arr.get(i);
				if(mosoQueue.getAddTime() != null){
					mosoQueue.setAddTime(mosoQueue.getAddTime().substring(0, mosoQueue.getAddTime().indexOf(" ")+6));
					arr.set(i, mosoQueue);
				}
			}
		}
		return arr;
	}
	@SuppressWarnings("unchecked")
	public List<MosoDay> findMosoDay(ReportListForm repF) {
		StringBuffer sql=new StringBuffer();
		StringBuffer count=new StringBuffer();
		StringBuffer w=new StringBuffer();
		StringBuffer w2=new StringBuffer();
		List<MosoDay> arr = new ArrayList<MosoDay>();
		List list =new ArrayList();
		IStaffDao dao =new StaffDao();
		sql.append("select c.routeType as routeType,date as date,max(total) as total,max(repToale) as repToale," +
				    "max(totalOk) as totalOk,max(totalFail) as totalFail,max(totalNull) as totalNull"+
					"  from (");
					w2.append("select b.routeType as routeType,a.sendTime as date,count(1) as total,"+
					"(select count(*) from smsmt sendAdmin where  sendAdmin.result >-1 and sendAdmin.EID=a.EID)as repToale,"+
					"(select count(*) from smsmt sendAdmin where sendAdmin.status='0' and sendAdmin.EID=a.EID)as totalOk,"+
					"(select count(*) from smsmt sendAdmin where sendAdmin.status<>'0' and sendAdmin.EID=a.EID) as totalFail,"+
					"(select count(*) from smsmt sendAdmin where sendAdmin.status is null and sendAdmin.result >-1 and sendAdmin.EID=a.EID) as totalNull from  "+
					"smsmt a inner join Sms_Basicinf b on a.EID=b.EID where 1=1 ");
					if(repF.getStart()!=null&&!repF.getStart().trim().equals("")){
						w.append(" and a.sendTime>='"+repF.getStart()+"'" );
					}if(repF.getEnd()!=null&&!repF.getEnd().trim().equals("")){
						w.append(" and a.sendTime<'"+repF.getEnd()+"' ");
					}
					w2.append(w);
					sql.append(w2);
					sql.append(" group by b.routeType, a.sendTime ) as c group by c.routeType,date ");
				List<MosoDay> arr1 =dao.select(sql, null, list, repF, MosoDay.class);
				if(arr1!=null && arr1.size()!=0){
					MosoDay mosoDay = null;
					for(int i=0;i<arr1.size();i++){
						mosoDay = arr1.get(i);
						if(mosoDay.getTotal()!=null && mosoDay.getTotal()!=0){
							if(mosoDay.getTotalOk()!=null)
								mosoDay.setOkPercent(mosoDay.getTotalOk()*100/mosoDay.getTotal()+"%");
							if(mosoDay.getTotalFail()!=null)
								mosoDay.setFailPercent(mosoDay.getTotalFail()*100/mosoDay.getTotal()+"%");
							if(mosoDay.getRepToale()!=null)
								mosoDay.setRepPercent(mosoDay.getRepToale()*100/mosoDay.getTotal()+"%");
						}
						if(mosoDay.getDate()!=null);
							mosoDay.setDate(mosoDay.getDate().substring(0,mosoDay.getDate().indexOf(" ")));
						arr1.set(i, mosoDay);
					}
				}
							
				sql = new StringBuffer();
				sql.append("select '群发通道' as routeType,'total' as date,sum(total) as total,sum(repToale) as repTotale,sum(totalOk) as totalOk,"+
					"sum(totalFail) as totalFail,sum(totalNull) as totalNull "+
					  "from (");
					sql.append(w2);
					 sql.append("group by b.routeType,a.sendTime ) as c ");
		count.append("select count(1) from ("+sql+")  ");
		sql.append(" order by routeType , date desc");
		List<MosoDay> arr2 =dao.select(sql, count, list, repF, MosoDay.class);
		
		if(arr2!=null && arr2.size()!=0){
			MosoDay mosoDay = null;
			for(int i=0;i<arr2.size();i++){
				mosoDay = arr2.get(i);
				if(mosoDay.getTotal()!=null && mosoDay.getTotal()!=0){
					if(mosoDay.getTotalOk()!=null)
						mosoDay.setOkPercent(mosoDay.getTotalOk()*100/mosoDay.getTotal()+"%");
					if(mosoDay.getTotalFail()!=null)
						mosoDay.setFailPercent(mosoDay.getTotalFail()*100/mosoDay.getTotal()+"%");
					if(mosoDay.getRepToale()!=null)
						mosoDay.setRepPercent(mosoDay.getRepToale()*100/mosoDay.getTotal()+"%");
				}
				if(mosoDay.getDate()!=null);
				mosoDay.setDate(mosoDay.getDate().substring(0,mosoDay.getDate().indexOf(" ")));
				arr2.set(i, mosoDay);
			}
		}
		arr.addAll(arr1);
		arr.addAll(arr2);
		return arr;
	}
	@SuppressWarnings("unchecked")
	public String findFeastName(String Eid, String time) {
		String sql ="select feastName from Feast where Eid='"+Eid+"' and worldDate='"+time+"' ";
		IStaffDao dao =new StaffDao();
		try{String feastName=dao.getString(sql);
		return feastName;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		}
	public void changeIsSmsSign(String[] ids, String locksign ) {
		if(ids!=null&&ids.length>0){
			IStaffDao dao=new StaffDao();
			dao.changeIsSmsSign(ids,locksign);
		}
	}
	private static String getSmsSendAdminSql(String today){
		StringBuilder sb=new StringBuilder();
		sb.append("select a.eid, total ,totalok,totalfail,totalnull,totalmobile as ydtotal,sucTotalMobile as ydok,faiTotalMobile as ydfail,nulltotalmobile as ydnull,totalUnicom as lttotal,sucTotalUnicom as ltok,faiTotalUnicom as ltfail,nulltotalUnicom as ltnull,totalTelecom as dxtotal,sucTotalTelecom as dxok,faiTotalTelecom as dxfail,nulltotalTelecom as dxnull from ");
		sb.append("(select eid, count(id) as total from smsmt   where sendTime>=?  and result>-1 group by eid) a  ");
		sb.append("left join");
		sb.append(" (select eid,count(id) as totalok from smsmt   where sendTime>=?  and status='0' group by eid) b ");
		sb.append(" on a.eid=b.eid  ");
		sb.append("  left join ");
		sb.append("(select eid,count(id) as totalfail from smsmt   where sendTime>=?  and status<>'0' group by eid) c ");
		sb.append(" on a.eid=c.eid  ");
		sb.append(" left join ");
		sb.append("  (select eid,count(id) as totalnull from smsmt   where sendTime>=? and status is null  and result>-1 group by eid) d ");
		sb.append("  on a.eid=d.eid ");
		sb.append("  left join ");
		sb.append(" (select eid,count(id) as totalmobile from smsmt   where sendTime>=? and phonetype=0  and result>-1 group by eid) e ");
		sb.append(" on a.eid=e.eid ");
		sb.append(" left join ");
		sb.append(" (select eid,count(id) as sucTotalMobile from smsmt   where sendTime>=?  and phonetype=0 and status='0' group by eid) f ");
		sb.append(" on a.eid=f.eid ");
		sb.append(" left join ");
		sb.append(" (select eid,count(id) as faiTotalMobile from smsmt   where sendTime>=?  and phonetype=0 and status<>'0' group by eid) g on a.eid=g.eid  ");
		sb.append("   left join ");
		sb.append("  (select eid,count(id) as nulltotalmobile from smsmt   where sendTime>=? and phonetype=0 and status is  null  and result>-1 group by eid) h ");
		sb.append(" on a.eid=h.eid  ");
		sb.append("   left join ");
		sb.append("   (select eid,count(id) as totalUnicom from smsmt   where sendTime>=? and phonetype=1  and result>-1 group by eid) o ");
		sb.append("   on a.eid=o.eid   ");
		sb.append("   left join ");
		sb.append("  (select eid,count(id) as sucTotalUnicom from smsmt   where sendTime>=?  and phonetype=1 and status='0' group by eid) i ");
		sb.append("  on a.eid=i.eid   ");
		sb.append("left join ");
		sb.append(" (select eid,count(id) as faiTotalUnicom from smsmt   where sendTime>=?  and phonetype=1 and status<>'0' group by eid) j ");
		sb.append(" on a.eid=j.eid   ");
		sb.append("  left join ");
		sb.append(" (select eid,count(id) as nulltotalUnicom from smsmt   where sendTime>=? and phonetype=1 and status is  null  and result>-1 group by eid) k ");
		sb.append(" on a.eid=k.eid   ");
		sb.append("  left join ");
		sb.append("(select eid,count(id) as totalTelecom from smsmt   where sendTime>=? and phonetype=2  and result>-1 group by eid) p ");
		sb.append("on a.eid=p.eid   ");
		sb.append("   left join ");
		sb.append("  (select eid,count(id) as sucTotalTelecom from smsmt   where sendTime>=?  and phonetype=2 and status='0' group by eid) l ");
		sb.append("  on a.eid=l.eid   ");
		sb.append("  left join ");
		sb.append(" (select eid,count(id) as faiTotalTelecom from smsmt   where sendTime>=?  and phonetype=2 and status<>'0' group by eid) m ");
		sb.append(" on a.eid=m.eid  ");
		sb.append("   left join ");
		sb.append("  (select eid,count(id) as nulltotalTelecom from smsmt   where sendTime>=? and phonetype=2 and status is  null   and result>-1 group by eid) n ");
		sb.append("  on a.eid=n.eid  ");
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public static List getPages(List list,int startIndex,int pageSize){
		if(list==null)return new ArrayList();
		if(pageSize<=0)pageSize=16;
		if(startIndex<=0)startIndex=0;
		List list1=new ArrayList();
		if(list.size()>=startIndex+pageSize){
			for(int i=0;i<pageSize;i++){
				list1.add(list.get(startIndex+i));
			}
		}else{
			for(int i=0;i<list.size()-startIndex;i++){
				list1.add(list.get(startIndex+i));
			}
		}
		return list1;
	}
}
