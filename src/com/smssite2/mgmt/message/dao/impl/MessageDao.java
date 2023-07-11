package com.smssite2.mgmt.message.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.note.bean.Message;
import com.note.bean.Phrase;
import com.note.bean.Staff;
import com.note.common.DbBean;
import com.smssite2.mgmt.basicsetting.bean.Feast;
import com.smssite2.mgmt.message.bean.MessageB;
import com.smssite2.mgmt.message.bean.MessageBean;
import com.smssite2.mgmt.message.bean.MessagePhoneBean;
import com.smssite2.mgmt.message.bean.MessageSendBean;
import com.smssite2.mgmt.message.bean.PhoneBean;
import com.smssite2.mgmt.message.bean.Receive;
import com.smssite2.mgmt.message.bean.SendHistory;
import com.smssite2.mgmt.message.bean.SiftBean;
import com.smssite2.mgmt.message.dao.IMessageDao;
import com.smssite2.mgmt.message.form.FeastListForm;
import com.smssite2.mgmt.message.form.MessageListForm;
import com.smssite2.mgmt.message.form.PhoneListForm;
import com.smssite2.mgmt.message.form.PhraseListForm;
import com.smssite2.mgmt.message.form.RiceiveListForm;
import com.smssite2.mgmt.message.form.SendHistoryForm;

public class MessageDao implements IMessageDao{
	@SuppressWarnings("unchecked")
	public List<MessageB> queryAllMessage(List list, StringBuffer sql,
			StringBuffer count, MessageListForm msgList) {
		msgList.setTotalItems(DbBean.getInt(count.toString(),null));
		msgList.adjustPageIndex();
		List<MessageB>lists=DbBean.select(sql.toString(),list.toArray(), 0, msgList.getPageSize(), MessageB.class);
		return lists;
	}
	@SuppressWarnings("unchecked")
	public List<MessageBean> queryAllMessage1(List list, StringBuffer sql,
			StringBuffer count, MessageListForm msgList) {
		msgList.setTotalItems(DbBean.getInt(count.toString(),list.toArray()));
		msgList.adjustPageIndex();
	return 	DbBean.select(sql.toString(), list.toArray(), 0, msgList.getPageSize(), MessageBean.class);
	}
	@SuppressWarnings("unchecked")
	public List<MessagePhoneBean> findAllMsgPhone(StringBuffer sql,
			StringBuffer count,  PhoneListForm phoneList) {
		phoneList.setTotalItems(DbBean.getInt(count.toString(), null));
		phoneList.adjustPageIndex();
		return DbBean.select(sql.toString(),null, phoneList.getStartIndex(), phoneList.getPageSize(), MessagePhoneBean.class);
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	public int deleteMessage(StringBuffer sql, List list) {
		DbBean db=new DbBean();
		try{
		int a=	db.executeUpdate(sql.toString(), list.toArray());
		return a;
		}catch(Exception e){
			db.rollback();
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Message> findAllMessageByMessageId(String EId, String messageId,StringBuffer sql) {
	return	DbBean.select(sql.toString(), new Object[]{messageId,EId}, 0, Integer.MAX_VALUE	, Message.class);
	
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	public int savaAndDeleteMessage(StringBuffer sql2, List params) {
		DbBean db=new DbBean();
			try{
				int a=	db.executeUpdate(sql2.toString(), params.toArray());
			return a;
			}catch(Exception e){
				db.rollback();
				e.printStackTrace();
				return 0;
			}
	}

	@SuppressWarnings("unchecked")
	public String[] findAllMessageIdByEId(String routeType, String EId) {
		DbBean  db =new DbBean();
		String sql="select DISTINCT messageId from message_d where EId=?";
		Connection conn =null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List list=new ArrayList();
		try{
			conn = db.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, EId);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("messageId"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			DbBean.close(rs, ps);
			db.close();
		}
		return  (String[]) list.toArray(new String[0]);
	}

	@SuppressWarnings("unchecked")
	public List<MessageSendBean> findAllMEssageSend(StringBuffer sql, StringBuffer count,
			MessageListForm msgList) {
		msgList.setTotalItems(DbBean.getInt(count.toString(), null));
		msgList.adjustPageIndex();
		return DbBean.select(sql.toString(), null, 0, msgList.getPageSize(), MessageSendBean.class);
	}

	@SuppressWarnings("unchecked")
	public List<SendHistory> findAllSendHistory(StringBuffer sql,
			StringBuffer count, List list, SendHistoryForm hisList) {
		hisList.setTotalItems(DbBean.getInt(count.toString(), list.toArray()));
		hisList.adjustPageIndex();
		return DbBean.select(sql.toString(), list.toArray(), 0, hisList.getPageSize(), SendHistory.class);
	}

	public SendHistory findSendHistoryById(String sql, String id) {
		return (SendHistory) DbBean.selectFirst(sql, new Object[]{id}, SendHistory.class);
	}
	@SuppressWarnings("unchecked")
	public List<SiftBean> findAllSift(String EId){
		String sql="select distinct siftContent from sift where EId=? or EId='000000'";
		return DbBean.select(sql, new Object[]{EId}, 0, Integer.MAX_VALUE, SiftBean.class);
	}
	public int saveOrUpdateMessage(String sql, Object[] array) {
		try{
			int a=DbBean.executeUpdate(sql, array);
			return a;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	@SuppressWarnings("unchecked")
	public List<Phrase> findAllPhrase(String sql, String count, Object[] params, PhraseListForm phraseList) {
		phraseList.setTotalItems(DbBean.getInt(count, params));
		phraseList.adjustPageIndex();
		List<Phrase> list =DbBean.select(sql, params, phraseList.getStartIndex(), phraseList.getPageSize(), Phrase.class);
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Feast> findAllFeast(String sql, String count,
			Object[] params, FeastListForm feastList) {
		feastList.setTotalItems(DbBean.getInt(count, params));
		feastList.adjustPageIndex();
		List<Feast> list=DbBean.select(sql, params, feastList.getStartIndex(), feastList.getPageSize(), Feast.class);
		return list;
	}
	@SuppressWarnings("unchecked")
	public List<Receive> findNewReceive(RiceiveListForm riceF, String sql,
			String count, Object[] params) {
		riceF.setTotalItems(DbBean.getInt(count, params));
		riceF.adjustPageIndex();
		
		return DbBean.select(sql, params, riceF.getStartIndex(), riceF.getPageSize(), Receive.class);
	}
	@SuppressWarnings("unchecked")
	public List<Receive> findReceiveList(RiceiveListForm riceF, String sql,
			String count, Object[] params) {
		riceF.setTotalItems(DbBean.getInt(count, params));
		riceF.adjustPageIndex();
		
		return DbBean.select(sql, params,0, riceF.getPageSize(), Receive.class);
	}
	@SuppressWarnings("static-access")
	public int deleteReceive(StringBuffer sql, Object[] ids) {
		DbBean db=new DbBean();
		try{
			int a=db.executeUpdate(sql.toString(), ids);
		return a;
		}catch(Exception e){
			db.rollback();
			e.printStackTrace();
			return 0;
		}
		
	}
	@SuppressWarnings("unchecked")
	public List<PhoneBean> findPhone(StringBuffer sql, String[] ids) {
		
		return DbBean.select(sql.toString(), ids, 0, Integer.MAX_VALUE, PhoneBean.class);
	}
	public String findContent(String sql, Object[] param) {
		try{
			String content=DbBean.getString(sql, param);
			
			return content;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public List<Staff> findAllStaff(String sql, Object[] params) {
		
		return DbBean.select(sql, params, 0, Integer.MAX_VALUE, Staff.class);
	}
	public Receive findReceiveById(String sql, String id) {
	
		return (Receive) DbBean.selectFirst(sql, new Object[]{id}, Receive.class);
	}
	@SuppressWarnings("static-access")
	public int addReceive(String string, Object[] array) {
		DbBean db=new DbBean();
		try{
			int a=db.executeUpdate(string, array);
		return a;
		}catch(Exception e){
			db.rollback();
			e.printStackTrace();
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	public List<PhoneBean> findlinkman(StringBuffer sql, List list) {
		
		return DbBean.select(sql.toString(), list.toArray(), 0, Integer.MAX_VALUE, PhoneBean.class);
	}
}
