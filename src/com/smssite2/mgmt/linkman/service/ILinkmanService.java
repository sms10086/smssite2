package com.smssite2.mgmt.linkman.service;

import java.util.List;
import com.note.NoteException;
import com.smssite2.mgmt.linkman.bean.Group;
import com.smssite2.mgmt.linkman.bean.Linkman;
import com.smssite2.mgmt.linkman.form.LinkmanListForm;
import com.smssite2.mgmt.message.bean.PhoneBean;

public interface ILinkmanService {

	List<Group> findAllGroup(String eid, String staffId);
	List<Group> findAllGroupShare(String eid, String staffId);
	int addLinGroup(String eid, String groupid, String groupname, String staffId, String isShare,String replyContent) throws NoteException;
	int delete(String groupid, String eid, String table, String string2);
	 
	List<Linkman> findAllLinkman(LinkmanListForm manF, String eid,
			String groupid, String staffId, String flag);
	int addLinkman(String eid, String staffId, String flag, String name,
			String phone, String groupid, String sex, String birthday,
			String post, String orgName, String userMemo, String optionalContent) throws NoteException;
	
	Linkman findLinkman(String eid, String linkid) throws NoteException;
	int change(String[] ids, String EId, String status, String[] phs);
	int deleteLinkman(String[] ids, String eid, String flag, String groupid, String staffid);
	List<PhoneBean> findPhoneBean(String[] ids, String eid);
	void changeGroups(String groupid, String eid, Object object);
}
