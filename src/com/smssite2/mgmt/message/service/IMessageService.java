package com.smssite2.mgmt.message.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import com.note.NoteException;
import com.note.bean.Account;
import com.note.bean.Phrase;
import com.note.bean.Staff;
import com.note.bean.Task;
import com.smssite2.mgmt.basicsetting.bean.Feast;
import com.smssite2.mgmt.message.bean.MessageBean;
import com.smssite2.mgmt.message.bean.MessagePhoneBean;
import com.smssite2.mgmt.message.bean.MessageSendBean;
import com.smssite2.mgmt.message.bean.PhoneBean;
import com.smssite2.mgmt.message.bean.Receive;
import com.smssite2.mgmt.message.bean.SendHistory;
import com.smssite2.mgmt.message.form.FeastListForm;
import com.smssite2.mgmt.message.form.LoadPhoneForm;
import com.smssite2.mgmt.message.form.MessageListForm;
import com.smssite2.mgmt.message.form.PhoneListForm;
import com.smssite2.mgmt.message.form.PhraseListForm;
import com.smssite2.mgmt.message.form.RiceiveListForm;
import com.smssite2.mgmt.message.form.SendHistoryForm;

public interface IMessageService {	List<Task> findAllMessage(MessageListForm msgList, String EId,
			String routeType, String staffId);

	List<MessagePhoneBean> findAllMessagePhone(PhoneListForm phoneList,
			String id, String routeType, String content, String messageId,
			String flag);

	String deleteMessageById(String[] ids, String routeType, String flag,
			String eid, String taskID) throws NoteException;

	String MessageSend(String[] messageIds, String routeType, String id,
			int accountPRI, String flag, String userId,
			MessageListForm msgList, HttpSession session, int messageLength)
			throws NoteException;


	String deleteMessageByMessageId(String[] ids, String routeType,
			String flag, String id, MessageListForm msgList, String userId);

	List<MessageSendBean> findAllMessageSend(MessageListForm msgList,
			String id, String routeType, String staffId);

	String deleteMessageSend(String[] ids, String routeType, String EId);

	List<MessageBean> findAllSchedule(MessageListForm msgList, String id,
			String routeType, String staffId);

	String deleteSchedule(String[] ids, String routeType, String EId,
			String flag, MessageListForm msgList);

	List<SendHistory> findAllSendHis(SendHistoryForm hisList, String id,
			String routeType, String staffId);

	int sendMessageAgain(String[] ids, String EId, String routeType, int length)
			throws NoteException;

	String sendMessage(String content, String mesType, String checkNeedChk,
			String userId, String smsNum, String smsSign, String id,
			String isSchedule, String routeType, String needSendTime,
			List<PhoneBean> list, String chkusevar) throws NoteException;

	List<Phrase> findAllPhrase(String EId, PhraseListForm phraseList,
			String staffId);

	List<Feast> findAllFeast(String id, FeastListForm feastList);

	int deleteHistory(String[] ids, String routeType, String id);

	String findAllHis(String EId, String routeType, String id, String flag);

	List<Receive> findNewRecive(RiceiveListForm riceF, String id,
			String routeType, String staffid);

	List<Receive> findAllRecive(RiceiveListForm riceF, String EId,
			String routeType, String staffid);

	String deleteReceive(String[] ids, String routeType, String id)
			throws NoteException;

	List<PhoneBean> findPhone(String[] ids, String routeType, String id);

	List<Staff> findAllStaff(String routeType, String EId, String staffId);

	String addReceive(String[] receivers, String[] messageId, String staffId,
			String Eid, String routeType);

	List<PhoneBean> findLinkman(String[] ids, String eid, String groupid,
			String flag, String staffId);

	void modifyAccount(String eid, String name, String phone, String email, String smsBalance);

	List<Task> findAllMessageOut(MessageListForm msgList, String id,
			String routeType, String staffId);

	List<PhoneBean> findPhones_load(String sessionId, String eid,
			String userid, LoadPhoneForm phoneForm, String taskID);

	int findLinkman(String[] ids, String eid, String groupid, String flag,
			String staffId, String sessionId, String taskID);

	void deletephones_load(String sessionid);

	String[] getContents(String content, String smssign, int num);

	@SuppressWarnings("unchecked")
	void sendWeather(Map map, Account account, String staffId)
			throws NoteException;

	List<MessageBean> findAllScheduleOut(MessageListForm msgList, String id,
			String routeType, String staffId);

}
