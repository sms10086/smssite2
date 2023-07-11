package com.smssite2.mgmt.staff.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.note.NoteException;
import com.note.bean.Account;
import com.note.bean.AddMoneyLog;
import com.note.bean.Log;
import com.note.bean.Role;
import com.note.bean.Staff;
import com.note.bean.Team;
import com.smssite2.mgmt.linkman.bean.Group;
import com.smssite2.mgmt.staff.bean.CheckMoney;
import com.smssite2.mgmt.staff.bean.MoneyHisBean;
import com.smssite2.mgmt.staff.bean.StaffBean;
import com.smssite2.mgmt.staff.form.AccountForm;
import com.smssite2.mgmt.staff.form.AdminListForm;
import com.smssite2.mgmt.staff.form.LogForm;
import com.smssite2.mgmt.staff.form.MoneyForm;
import com.smssite2.mgmt.staff.form.OrgListForm;
import com.smssite2.mgmt.staff.form.ReportListForm;

public interface IStaffDao {	Map findAllOrg(String EId);
	List<Role> findAllRole(String EId);
	
	Account queryAccountByEId(String id) throws NoteException;
	Staff queryAdmin(String EId, String staffId);
	List<StaffBean> queryStaffs(AdminListForm adminList, String id);
	int modifyPW(String md5Str, String id, String userId)throws NoteException;

	List<Log> queryListLog(LogForm logF, String id) throws ParseException;

	Log queryLogById(String logId);
	List<Team> queryOrgsByEId(OrgListForm orgList, String id);
	int saveOrg(String orgIdd, String orgName, String orgMemo, String EId);
	Team queryOrgByOrgId(String tid, String EId);
	Team queryOrgByName(String id, String tname);
	int updateOrg(String orgId, String orgName, String orgMemo, String EId);
	int deleteOrg(String[] ids, String EId);
	void changeLocksign(String[] ids, String locksign,String EId, String flag);
	int deleteStaff(String[] ids, String EId);

	Staff queryStaffByStaffId(String string);
	Staff findStaffByUserId(String userId, String id);

	void saveStaff(String id, String userName, String realName, String md5Str,
			String orgId, String roleId, String memo);

	List findAllPrivilegeByRoleId(String EId,String roleId);

	void addStaffGrant(String id, Integer staffId,String roleId);
	String findOrgId(String id);
	int saveOrUpdateStaff(String sql, Object[] params);
	Role findRoleByName(String upost, String id);
	List<Account> findAllAcount(StringBuffer sql, StringBuffer count, List list, AccountForm accF);
	Map findAllRoute();
	Account findAccountBySmsNum(String smsNum);
	int addAccount(String sql, Object[] array);
	int deleteAccount(String string, String[] ids);
	int resetPwd(String string, Object[] array);
	int addMoney(String sql, Object[] params);
	List<CheckMoney> findUnckecklistM(StringBuffer sql, StringBuffer count,
			MoneyForm moneyF);
	int execute(String string, Object[] params);
	AddMoneyLog findAddMoney(String sql, String[] strings);
	@SuppressWarnings("unchecked")
	ArrayList select(StringBuffer sql, StringBuffer count, List list,
			ReportListForm repF, Class clazz);
	String getString(String sql);
	void changeIsSmsSign(String[] ids, String locksign );

	List<Group> findAllGroup(String id);
}
