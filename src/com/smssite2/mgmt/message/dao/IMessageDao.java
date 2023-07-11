package com.smssite2.mgmt.message.dao;

import java.util.List;
import com.smssite2.mgmt.basicsetting.bean.Feast;
import com.smssite2.mgmt.message.bean.PhoneBean;
import com.note.bean.Message;
import com.note.bean.Phrase;
import com.note.bean.Staff;
import com.smssite2.mgmt.message.bean.MessageB;
import com.smssite2.mgmt.message.bean.MessageBean;
import com.smssite2.mgmt.message.bean.MessagePhoneBean;
import com.smssite2.mgmt.message.bean.MessageSendBean;
import com.smssite2.mgmt.message.bean.Receive;
import com.smssite2.mgmt.message.bean.SendHistory;
import com.smssite2.mgmt.message.bean.SiftBean;
import com.smssite2.mgmt.message.form.FeastListForm;
import com.smssite2.mgmt.message.form.MessageListForm;
import com.smssite2.mgmt.message.form.PhoneListForm;
import com.smssite2.mgmt.message.form.PhraseListForm;
import com.smssite2.mgmt.message.form.RiceiveListForm;
import com.smssite2.mgmt.message.form.SendHistoryForm;

public interface IMessageDao {	@SuppressWarnings("unchecked")
	List<MessageB> queryAllMessage(List list, StringBuffer sql, StringBuffer count,
			MessageListForm msgList);
	List<MessagePhoneBean> findAllMsgPhone(StringBuffer sql, StringBuffer count,
			PhoneListForm phoneList);
	@SuppressWarnings("unchecked")
	int deleteMessage(StringBuffer sql, List list);
	List<Message> findAllMessageByMessageId(String id, String string, StringBuffer sql);
	@SuppressWarnings("unchecked")
	int savaAndDeleteMessage(StringBuffer sql2, List params);
	String[] findAllMessageIdByEId(String routeType, String EId);
	List<MessageSendBean> findAllMEssageSend(StringBuffer sql, StringBuffer count,
			MessageListForm msgList );
	@SuppressWarnings("unchecked")
	List<SendHistory> findAllSendHistory(StringBuffer sql, StringBuffer count,
			List list, SendHistoryForm hisList);
	SendHistory findSendHistoryById(String sql, String id);
	public List<SiftBean> findAllSift(String EId);
	int saveOrUpdateMessage(String sql, Object[] array);
	List<Phrase> findAllPhrase(String sql, String count, Object[] params, PhraseListForm phraseList);
	List<Feast> findAllFeast(String string, String string2, Object[] array,
			FeastListForm feastList);
	@SuppressWarnings("unchecked")
	List<MessageBean> queryAllMessage1(List list, StringBuffer sql,
			StringBuffer count, MessageListForm msgList);
	List<Receive> findNewReceive(RiceiveListForm riceF, String string,
			String string2, Object[] array);
	List<Receive> findReceiveList(RiceiveListForm riceF, String sql,
			String count, Object[] params);
	int deleteReceive(StringBuffer sql, Object[] objects);
	List<PhoneBean> findPhone(StringBuffer sql, String[] ids);
	String findContent(String sql, Object[] param);
	List<Staff> findAllStaff(String sql, Object[] params);
	Receive findReceiveById(String sql, String id);
	int addReceive(String string, Object[] array);
	@SuppressWarnings("unchecked")
	List<PhoneBean> findlinkman(StringBuffer sql, List list);
}
