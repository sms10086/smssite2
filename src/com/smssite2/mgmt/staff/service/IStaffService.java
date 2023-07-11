package com.smssite2.mgmt.staff.service;

import java.util.List;
import com.note.NoteException;
import com.note.bean.Account;
import com.note.bean.Log;
import com.note.bean.Role;
import com.note.bean.Staff;
import com.note.bean.Team;
import com.smssite2.mgmt.staff.bean.CheckMoney;
import com.smssite2.mgmt.staff.bean.MoneyHisBean;
import com.smssite2.mgmt.staff.bean.MosoDay;
import com.smssite2.mgmt.staff.bean.MosoQueue;
import com.smssite2.mgmt.staff.bean.QueueMonBean;
import com.smssite2.mgmt.staff.bean.SendHisBean;
import com.smssite2.mgmt.staff.bean.SmSendBean;
import com.smssite2.mgmt.staff.bean.StaffBean;
import com.smssite2.mgmt.staff.form.AccountForm;
import com.smssite2.mgmt.staff.form.AdminListForm;
import com.smssite2.mgmt.staff.form.LogForm;
import com.smssite2.mgmt.staff.form.MoneyForm;
import com.smssite2.mgmt.staff.form.OrgListForm;
import com.smssite2.mgmt.staff.form.ReportListForm;

public interface IStaffService {	Account findAccount(String EId) throws NoteException;
	Staff login(String id, String userName, String password) throws NoteException;
	List<StaffBean> findStaffByEid(AdminListForm adminList, String EId);
	void modifyPassword(String userId, String oldPw, String newPw, String rePw, String id)throws NoteException;
	List<Log> findListLog(LogForm logF, String id);
	Log findLogById(String logId) throws NoteException;
	List<Team> findOrgsByEId(OrgListForm orgList, String EId);
	void addOrg(String id, String orgName, String orgMemo, String orgId) throws NoteException;
	Team findOrgById(String ids, String EId) throws NoteException;
	void deleteOrg(String[] ids, String EId) throws NoteException;
	void changeLocksign(String[] ids, String locksign,String EId, String flag)throws NoteException;
	void deleteStaff(String[] ids,String EId)throws NoteException;
	void addStaff(String id, String userId, String userName, String password,
			String uteam, String upost, String umemo, String smsNum,
			String smsSign, String isAdmin, String credit, String lockSign,String groupID, String flag) throws NoteException;
	Staff findStaffById(String userId, String EId) throws NoteException;
	Role findRoleByName(String upost, String EId);
	List<Account> findListAccount(AccountForm accF, String id);
	String addAccount(String eid, String smsCompanyName, String regTeam,
			String regSales, String regDirector, String regDate,
			String regAddress, String regContact, String regMobiles,
			String regPhones, String regEmail, String smsNum, String smsnumlen,
			String routeType, String smsSign, String smsBalance, String regMemo, String messageLength) throws NoteException;
	String modifyAccount(String eid, String smsCompanyName, String regTeam,
			String regSales, String regDirector, String regDate,
			String regAddress, String regContact, String regMobiles,
			String regPhones, String regEmail, String smsNum, String smsnumlen,
			String routeType, String smsSign, String smsBalance, String regMemo, String messageLength);
	void deleteAccount(String[] ids) throws NoteException;
	String resetPwd(String[] ids) throws NoteException;
	String addMoney(String eid, String addNum, String price, String moneyCount,
			String addMome, String staffId) throws NoteException;
	List<CheckMoney> findUncheckListM(MoneyForm moeyF, String id);
	void deleteMoney(String[] ids) throws NoteException;
	String checkMoney(String id, String eid,String staffId) throws NoteException;
	List<MoneyHisBean> findAllMoneyHis(ReportListForm repF);
	List<SmSendBean> findAllSmSend(ReportListForm repF);
	List<SendHisBean> findAllSendHis(ReportListForm repF);
	List<QueueMonBean> findAllRouteType(ReportListForm repF);
	List<MosoQueue> findMosoQueue(ReportListForm repF);
	List<MosoDay> findMosoDay(ReportListForm repF);
	String findFeastName(String id, String time);
	void changeIsSmsSign(String[] ids, String locksign );
	
}
