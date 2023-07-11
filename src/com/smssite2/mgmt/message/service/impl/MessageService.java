package com.smssite2.mgmt.message.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.note.NoteException;
import com.note.bean.Account;
import com.note.bean.Phrase;
import com.note.bean.Staff;
import com.note.bean.Task;
import com.note.common.DbBean;
import com.note.common.StringUtil;
import com.note.common.TimeUtil;
import com.note.common.UUIDUtil;
import com.smssite2.mgmt.basicsetting.bean.Feast;
import com.smssite2.mgmt.message.bean.MessageBean;
import com.smssite2.mgmt.message.bean.MessagePhoneBean;
import com.smssite2.mgmt.message.bean.MessageSendBean;
import com.smssite2.mgmt.message.bean.PhoneBean;
import com.smssite2.mgmt.message.bean.Receive;
import com.smssite2.mgmt.message.bean.SendHistory;
import com.smssite2.mgmt.message.dao.IMessageDao;
import com.smssite2.mgmt.message.dao.impl.MessageDao;
import com.smssite2.mgmt.message.form.FeastListForm;
import com.smssite2.mgmt.message.form.LoadPhoneForm;
import com.smssite2.mgmt.message.form.MessageListForm;
import com.smssite2.mgmt.message.form.PhoneListForm;
import com.smssite2.mgmt.message.form.PhraseListForm;
import com.smssite2.mgmt.message.form.RiceiveListForm;
import com.smssite2.mgmt.message.form.SendHistoryForm;
import com.smssite2.mgmt.message.service.IMessageService;

public class MessageService implements IMessageService {	private static Log log = LogFactory.getLog(MessageService.class);

	@SuppressWarnings("unchecked")
	public List<Task> findAllMessageOut(MessageListForm msgList, String eid,
			String routeType, String staffId) {
		StringBuffer sql = new StringBuffer();
		StringBuffer w = new StringBuffer();
		StringBuffer count = new StringBuffer();
		sql
				.append("select t.*,p.phone,p.name,p.content as pcontent  from task t,phones p where t.eid=? and t.flag=0 ");
		List list = new ArrayList();
		list.add(eid);
		if (msgList.getContent() != null
				&& msgList.getContent().trim().length() > 0) {
			w.append(" and t.content like ? ");
			list.add("%" + msgList.getContent().trim() + "%");
		}
		if (msgList.getUserName() != null
				&& !msgList.getUserName().trim().equals("")) {
			w.append(" and t.staffId=? ");
			list.add(msgList.getUserName().trim());
		}
		if (msgList.getMesType() != null
				&& msgList.getMesType().trim().length() > 0) {
			w.append(" and t.mesType=? ");
			list.add(msgList.getMesType().trim());
		}
		if (!staffId.trim().toLowerCase().equals("admin")) {
			sql.append(" and t.staffid=? ");
			list.add(staffId);
		}

		w.append(" and t.id=p.taskid order by t.addTime");
		if (w != null && w.length() > 0) {
			sql.append(w);
		}
		List<Task> lists = DbBean.select(sql.toString(), list
				.toArray(new Object[list.size()]), 0, Integer.MAX_VALUE,
				Task.class);

		return lists;
	}

	@SuppressWarnings( { "unchecked", "static-access" })
	public List<Task> findAllMessage(MessageListForm msgList, String EId,
			String routeType, String staffId) {
		StringBuffer sql = new StringBuffer();
		StringBuffer w = new StringBuffer();
		StringBuffer count = new StringBuffer();
		count.append("select count(ID) from task where eid=? and flag=0 ");
		sql.append("select * from task where eid=? and flag=0 ");
		List list = new ArrayList();
		list.add(EId);
		if (msgList.getContent() != null
				&& msgList.getContent().trim().length() > 0) {
			w.append(" and content like ? ");
			list.add("%" + msgList.getContent().trim() + "%");
		}
		if (msgList.getUserName() != null
				&& !msgList.getUserName().trim().equals("")) {
			w.append(" and staffId=? ");
			list.add(msgList.getUserName().trim());
		}
		if (msgList.getMesType() != null
				&& msgList.getMesType().trim().length() > 0) {
			w.append(" and mesType=? ");
			list.add(msgList.getMesType().trim());
		}
		if (!staffId.trim().toLowerCase().equals("admin")) {
			sql.append(" and staffid=? ");
			count.append(" and staffid=? ");
			list.add(staffId);
		}
		if (w != null && w.length() > 0) {
			sql.append(w);
			count.append(w);
		}
		sql.append(" order by addTime");
		msgList.setTotalItems(DbBean.getInt(count.toString(), list
				.toArray(new Object[list.size()])));
		msgList.adjustPageIndex();
		List<Task> lists = DbBean.select(sql.toString(), list
				.toArray(new Object[list.size()]), msgList.getStartIndex(),
				msgList.getPageSize(), Task.class);

		return lists;
	}

	@SuppressWarnings("unchecked")
	public List<MessagePhoneBean> findAllMessagePhone(PhoneListForm phoneList,
			String EId, String routeType, String content, String messageId,
			String flag) {
		StringBuffer sql = new StringBuffer();
		StringBuffer w = new StringBuffer();
		StringBuffer count = new StringBuffer();
		sql.append("select id,name,phone,content from phones  where taskId='"
				+ messageId + "' ");
		count.append("select count(*) from phones where taskId='" + messageId
				+ "' ");
		if (phoneList.getPhone() != null
				&& !phoneList.getPhone().trim().equals("")) {
			w.append(" and phone like '"
					+ phoneList.getPhone().trim().replace("'", "''") + "%'");
		}
		if (w != null && w.length() > 0) {
			sql.append(w);
			count.append(w);
		}
		sql.append(" order by id");
		IMessageDao dao = new MessageDao();
		return dao.findAllMsgPhone(sql, count, phoneList);
	}

	@SuppressWarnings("unchecked")
	public String deleteMessageById(String[] ids, String routeType,
			String flag, String eid, String taskID) throws NoteException {
		if (ids != null && ids.length > 0) {

			int sum = 0;
			if (flag.equals("1")) {
				DbBean db = new DbBean();
				Connection conn = null;
				Statement stm = null;
				ResultSet rs = null;
				int succ = 0;
				int err = 0;
				try {
					int splitCount = DbBean.getInt(
							"select splitCount from task where ID=?",
							new Object[] { taskID });
					int sentC = 0;
					int a = 0;
					conn = db.getConnection();
					conn.setAutoCommit(false);
					stm = conn.createStatement();
					for (int i = 0; i < ids.length; i++) {
						if (ids[i] == null || ids[i].trim().equals(""))
							continue;
						a = stm.executeUpdate("delete from phones where id='"
								+ ids[i] + "'");
						if (a > 0) {
							stm
									.executeUpdate("update Sms_Basicinf set smsBalance=smsBalance+"
											+ splitCount
											+ " where eid='"
											+ eid
											+ "'");
							stm
									.executeUpdate("update task set phonenum=phonenum-1 where ID='"
											+ taskID + "'");
							succ++;
						} else {
							err++;
						}
					}
					conn.commit();
					conn.setAutoCommit(true);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (rs != null)
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					if (stm != null)
						try {
							stm.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					if (conn != null)
						try {
							conn.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				}

				if (succ > 0 && err > 0)
					return "成功删除" + succ + "条待发短信,另有" + err + "条短信已经发出或正在发送!";
				else if (succ > 0 && err == 0)
					return "成功删除" + succ + "条待发短信!";
				else
					return "删除失败,所选的" + err + "条短信已经发出或正在发送!";
			} else {
				int a = 0;
				Task task = (Task) DbBean.selectFirst(
						"select flag from task where ID=? ",
						new Object[] { taskID }, Task.class);
				if (task == null || task.getFlag() == 1)
					return "你本次删除的短信不存在，或已被审核。删除失败!";
				for (int i = 0; i < ids.length; i++) {
					if (ids[i] == null || ids[i].trim().equals(""))
						continue;
					String sql = "delete from phones where id=" + ids[i];
					a = DbBean.executeUpdate(sql, null);
					if (a > 0)
						sum++;
				}
				DbBean.executeUpdate(
						"update task set phoneNum=phoneNum-? where ID=? ",
						new Object[] { sum, taskID });
				if (sum <= 0)
					throw new NoteException("未知错误,删除失败!请联系管理员");
				else
					return "成功删除" + sum + "个号码!";
			}
		} else
			return "所选号码不存在,或已发出!";
	}

	public int getSendPRI(int accountPRI, int count) {
		if (count < 1000) {
			return accountPRI + 18;
		} else if (count < 4000) {
			return accountPRI + 14;
		} else if (count < 10000) {
			return accountPRI + 10;
		} else if (count > 10000 && count < 25000) {
			return accountPRI + 8;
		} else if (count > 25000 && count < 50000) {
			return accountPRI + 6;
		} else if (count > 50000 && count < 100000) {
			return accountPRI + 4;
		} else if (count > 100000 && count < 500000) {
			return accountPRI + 2;
		} else {
			return accountPRI + 1;
		}
	}

	public String getMessageId(String EId, String staffId) {
		String messageId = EId + "|" + staffId + "P"
				+ System.currentTimeMillis();
		return messageId;
	}

	@SuppressWarnings("unchecked")
	public String MessageSend(String[] messageIds, String routeType,
			String EId, int accountPRI, String flag, String userId,
			MessageListForm msgList, HttpSession session, int messageLength)
			throws NoteException {
		int smsBalance = DbBean.getInt(
				"select smsBalance from Sms_Basicinf where eid=? ",
				new Object[] { EId });
		if (smsBalance == 0)
			throw new NoteException("因帐户余额为0,您不能审核通过短信!");
		List<String> pids = new ArrayList();
		DbBean db = new DbBean();
		Connection conn = null;
		Statement stm = null;
		Statement stm2 = null;
		ResultSet rs = null;
		int total = 0;
		int totalnum = 0;
		log.info(">>>>>>>>>>" + TimeUtil.now() + "审核开始  " + flag + "  " + EId);
		if (flag.equals("1")) {
			try {
				conn = db.getConnection();
				conn.setAutoCommit(false);
				stm = conn.createStatement();
				stm2 = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY);
				StringBuffer fee = new StringBuffer();
				StringBuffer count = new StringBuffer();

				StringBuffer w = new StringBuffer();

				if (msgList.getUserName() != null
						&& msgList.getUserName().trim().length() > 0) {
					w.append(" and staffid='"
							+ msgList.getUserName().replace("'", "''") + "'");
				}
				if (msgList.getContent() != null
						&& msgList.getContent().trim().length() > 0) {
					w.append(" and content like '"
							+ msgList.getContent().replace("'", "''") + "%'");
				}
				if (msgList.getMesType() != null
						&& msgList.getMesType().trim().length() > 0) {
					w.append(" and mesType='"
							+ msgList.getMesType().replace("'", "''") + "'");
				}
				if (!userId.trim().toLowerCase().equals("admin")) {
					w.append(" and staffid='" + userId + "'");
				}
				w.append(" and flag='0' ");
				rs = stm2.executeQuery("select ID from task where eid='" + EId
						+ "' " + w.toString());
				while (rs.next()) {
					pids.add(rs.getString(1));
				}
				if (pids == null || pids.size() == 0)
					throw new NoteException("你本次审核的短信不存在，或已被审核。审核失败!");
				pids = MessageIdCheck.check(pids);
				if (pids == null || pids.size() == 0)
					throw new NoteException("你本次审核的短信包含别人正在处理的短信，请稍候再审核！");

				count.append("select sum(phoneNum)  from task where eid='"
						+ EId + "' ");
				count.append(w);
				rs = stm2.executeQuery(count.toString());
				if (rs.next()) {
					totalnum = rs.getInt(1);
				}
				fee
						.append("select sum(phoneNum*splitcount) from task where eid='"
								+ EId + "' ");
				fee.append(w);
				rs = stm2.executeQuery(fee.toString());
				if (rs.next()) {
					total = rs.getInt(1);
				}

				if (total > smsBalance)
					throw new NoteException("因帐户余额不足, 您本次申请的短信发送任务总计"
							+ totalnum + "条(实际" + total + "条)不能通过审核！");
				if (total == 0)
					throw new NoteException("你本次审核的短信不存在，或已被审核。审核失败!");
				stm.addBatch("update task set flag=1 where eid='" + EId + "' "
						+ w.toString());
				stm.addBatch("update Sms_Basicinf set smsBalance=smsBalance-"
						+ total + " where eid='" + EId + "'");
				stm.executeBatch();
				conn.commit();
				conn.setAutoCommit(true);
				log.info(">>>>>>>>>>" + TimeUtil.now() + "审核结束。eid：" + EId
						+ " 总数：" + total);

			} catch (SQLException e) {
				try {
					conn.rollback();
					total = 0;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();

			} finally {
				MessageIdCheck.removePids(pids);
				try {
					if (rs != null)
						rs.close();
					if (stm != null)
						stm.close();
					if (conn != null)
						conn.close();
				} catch (Exception e) {

				}
			}
			return "您已成功审核短信总计" + total + "条";
		} else if (messageIds != null && messageIds.length > 0) {

			try {
				conn = db.getConnection();
				conn.setAutoCommit(false);
				stm = conn.createStatement();
				stm2 = conn.createStatement();

				for (String ids : messageIds) {
					rs = stm2.executeQuery("select ID from task where   ID='"
							+ ids + "'  and flag=0 ");
					if (rs.next()) {
						pids.add(ids);
					}
				}
				if (pids == null || pids.size() == 0)
					throw new NoteException("你本次审核的短信不存在，或已被审核。审核失败!");
				pids = MessageIdCheck.check(pids);
				if (pids == null || pids.size() == 0)
					throw new NoteException("你本次审核的短信包含别人正在处理的短信，请稍候再审核！");

				for (String ids : pids) {
					if (StringUtil.isEmpty(ids))
						continue;
					rs = stm2
							.executeQuery("select phoneNum from task where   ID='"
									+ ids + "'");
					if (rs.next()) {
						totalnum += rs.getInt(1);
					}
				}
				for (String ids : pids) {
					if (StringUtil.isEmpty(ids))
						continue;
					rs = stm2
							.executeQuery("select sum(phoneNum*splitCount) from task where ID='"
									+ ids + "'");
					if (rs.next()) {
						total += rs.getInt(1);
					}
				}
				if (total > smsBalance)
					throw new NoteException("因帐户余额不足, 您本次申请的短信发送任务总计"
							+ totalnum + "条(实际" + total + "条)不能通过审核！");
				if (total == 0)
					throw new NoteException("你本次审核的短信不存在，或已被审核。审核失败!");

				for (String ids : pids) {
					if (StringUtil.isEmpty(ids))
						continue;
					stm.addBatch("update task set flag=1 where ID='" + ids
							+ "'");
				}
				stm.addBatch("update Sms_Basicinf set smsBalance=smsBalance-"
						+ total + " where eid='" + EId + "'");
				stm.executeBatch();
				conn.commit();
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			} finally {
				MessageIdCheck.removePids(pids);
				try {
					if (rs != null)
						rs.close();
					if (stm != null)
						stm.close();
					if (stm2 != null)
						stm2.close();

					if (conn != null)
						conn.close();
				} catch (Exception e) {

				}
			}
			log.info(">>>>>>>>>>" + TimeUtil.now() + "审核结束。eid：" + EId + " 总数："
					+ total);
			if (total <= 0)
				throw new NoteException("审核失败,所选短信不存在,或已删除!");
			else
				return "您已成功审核短信总计" + total + "条!";
		}
		return null;
	}

	@SuppressWarnings( { "static-access", "unchecked" })
	public String deleteMessageByMessageId(String[] ids, String routetype,
			String flag, String EId, MessageListForm msgList, String userId) {
		DbBean db = new DbBean();
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		int count = 0;
		List<String> pids = new ArrayList();
		if (flag.equals("1")) {
			try {
				conn = db.getConnection();
				conn.setAutoCommit(false);
				stm = conn.createStatement();
				StringBuffer w = new StringBuffer();
				if (msgList.getUserName() != null
						&& msgList.getUserName().trim().length() > 0) {
					w.append(" and staffid='" + msgList.getUserName() + "'");
				}
				if (msgList.getContent() != null
						&& msgList.getContent().trim().length() > 0) {
					w.append(" and content like '"
							+ msgList.getContent().replace("'", "''") + "%'");
				}
				if (msgList.getMesType() != null
						&& msgList.getMesType().trim().length() > 0) {
					w.append(" and mesType='"
							+ msgList.getMesType().replace("'", "''") + "'");
				}
				if (!userId.trim().toLowerCase().equals("admin")) {
					w.append(" and staffid='" + userId + "'");
				}
				rs = stm.executeQuery("select ID from task where eid='" + EId
						+ "' " + w.toString());
				while (rs.next()) {
					pids.add(rs.getString(1));
				}
				if (pids == null || pids.size() == 0)
					return "你本次删除的短信不存在，或已被审核。删除失败!";
				pids = MessageIdCheck.check(pids);
				if (pids == null || pids.size() == 0)
					return "你本次删除的短信包含别人正在处理的短信，请稍候再删除！";
				count = stm.executeUpdate("delete from task where EId='" + EId
						+ "' " + w.toString());
				conn.commit();
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				db.rollback(conn);
			} finally {
				MessageIdCheck.removePids(pids);
				db.close(rs);
				db.close(stm, conn);
			}
			if (count > 0)
				return "成功删除全部满足条件的待审核短信!共" + count + "批";
			return "该短信已删除或已审核!";
		} else {
			try {
				conn = db.getConnection();
				conn.setAutoCommit(false);
				stm = conn.createStatement();

				for (String id : ids) {
					rs = stm.executeQuery("select ID from Task where   ID='"
							+ id + "' ");
					if (rs.next()) {
						pids.add(id);
					}
				}

				if (pids == null || pids.size() == 0)
					return "你本次删除的短信不存在，或已被审核。删除失败!";
				pids = MessageIdCheck.check(pids);
				if (pids == null || pids.size() == 0)
					return "你本次删除的短信包含别人正在处理的短信，请稍候再删除！";

				for (String id : pids) {

					stm.addBatch("delete from task where ID='" + id + "' ");

					count++;
				}
				stm.executeBatch();

				conn.commit();
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				db.rollback(conn);
			} finally {
				MessageIdCheck.removePids(pids);
				db.close(rs);
				db.close(stm, conn);
			}
			if (count > 0)
				return "成功删除所选的" + count + "批待审核短信!";// 共"+total+"条";
			return "该短信已删除或已审核!";
		}
	}

	@SuppressWarnings( { "unchecked", "static-access" })
	public String deleteMessageByMessageId(String[] ids, String routeType,
			String flag, String EId) {

		DbBean db = new DbBean();
		Connection conn = null;
		Statement stm = null;
		int count = 0;

		if (flag.equals("1")) {
			try {
				conn = db.getConnection();
				conn.setAutoCommit(false);
				stm = conn.createStatement();
				count = stm.executeUpdate("delete from Message_d where EId='"
						+ EId + "'");

				conn.commit();
				conn.setAutoCommit(true);
			} catch (Exception e) {
				db.rollback(conn);
			} finally {
				db.close(stm, conn);
			}
			if (count > 0)
				return "成功删除全部待审核短信!";
			return "该短信已删除或已审核!";
		} else {
			try {
				conn = db.getConnection();
				conn.setAutoCommit(false);
				stm = conn.createStatement();
				for (int i = 0; i < ids.length; i++) {
					if (ids[i] == null || ids[i].trim().length() == 0)
						continue;
					stm.addBatch("delete from Message_d where messageId='"
							+ ids[i] + "' and EId='" + EId + "' ");

					count++;
				}
				stm.executeBatch();
				conn.commit();
				conn.setAutoCommit(true);
			} catch (Exception e) {
				db.rollback(conn);
			} finally {
				db.close(stm, conn);
			}
			if (count > 0)
				return "成功删除所选的" + count + "批待审核短信!";
			return "该短信已删除或已审核!";
		}
	}

	@SuppressWarnings("unchecked")
	public List<MessageSendBean> findAllMessageSend(MessageListForm msgList,
			String EId, String routeType, String staffId) {
		StringBuffer sql = new StringBuffer();
		StringBuffer w = new StringBuffer();
		StringBuffer count = new StringBuffer();

		sql
				.append("select mt.ID,mt.staffId,mt.addtime,mt.phone,mt.content from smsmt mt   where   mt.eid='"
						+ EId + "' and result=-1  ");
		count
				.append("select count(mt.ID) from smsmt mt  where   mt.eid='"
						+ EId + "' and result=-1 ");
		if (!staffId.trim().toLowerCase().equals("admin")) {
			sql.append(" and staffid='" + staffId + "' ");
			count.append(" and staffid='" + staffId + "' ");

		}

		if (msgList.getPhone() != null && !msgList.getPhone().trim().equals("")) {
			w.append(" and phone like '" + msgList.getPhone().trim() + "%'");

		}
		if (msgList.getUserName() != null
				&& !msgList.getUserName().trim().equals("")) {
			w.append(" and staffId= '" + msgList.getUserName().trim() + "' ");

		}
		if (msgList.getContent() != null
				&& !msgList.getContent().trim().equals("")) {
			w.append(" and  content like '%"
					+ msgList.getContent().replace("'", "''") + "%' ");

		}
		if (w != null && w.length() > 0) {
			sql.append(w);
			count.append(w);
		}
		msgList.setTotalItems(DbBean.getInt(count.toString(), null));
		msgList.adjustPageIndex();
		return DbBean.select(sql.toString(), null, msgList.getStartIndex(),
				msgList.getPageSize(), MessageSendBean.class);

	}

	@SuppressWarnings("unchecked")
	public String deleteMessageSend(String[] ids, String routeType, String EId) {
		return "删除失败,所选的短信已经发出或正在发送!";
	}

	@SuppressWarnings( { "unchecked", "static-access" })
	public List<MessageBean> findAllSchedule(MessageListForm msgList,
			String EId, String routeType, String staffId) {
		StringBuffer sql = new StringBuffer();
		StringBuffer w = new StringBuffer();
		StringBuffer count = new StringBuffer();
		sql
				.append("select ID as messageID,staffId, scheduleTime,content, smsNum, smsSign,splitcount as SentCount,phoneNum as total from task mt where eid= '"
						+ EId + "' and flag=1 and scheduleTime is not null ");
		count.append("select count(ID) from task where eid='" + EId
				+ "' and flag=1 and scheduleTime is not null ");

		if (!staffId.trim().toLowerCase().equals("admin")) {
			sql.append(" and mt.staffid='" + staffId + "' ");
			count.append(" and mt.staffid='" + staffId + "' ");
		}
		if (msgList.getContent() != null
				&& !msgList.getContent().trim().equals("")) {
			w.append(" and mt.content like '%"
					+ msgList.getContent().replace("'", "''") + "%' ");
		}
		if (msgList.getUserName() != null
				&& !msgList.getUserName().trim().equals("")) {
			w.append(" and mt.staffId like '%" + msgList.getUserName() + "%' ");
		}
		if (w != null && w.length() > 0) {
			sql.append(w);
			count.append(w);
		}
		sql.append(" order by mt.scheduleTime");
		msgList.setTotalItems(DbBean.getInt(count.toString(), null));
		msgList.adjustPageIndex();
		List<MessageBean> lists = DbBean.select(sql.toString(), null, msgList
				.getStartIndex(), msgList.getPageSize(), MessageBean.class);
		return lists;
	}

	public List<MessageBean> findAllScheduleOut(MessageListForm msgList,
			String EId, String routeType, String staffId) {
		StringBuffer sql = new StringBuffer();
		StringBuffer w = new StringBuffer();
		StringBuffer count = new StringBuffer();
		sql
				.append("select mt.ID as messageID,mt.staffId, mt.scheduleTime,mt.content, mt.smsNum, mt.smsSign,splitcount as SentCount,p.phone,p.name,p.content as pcontent  from task mt,phones p where mt.eid= '"
						+ EId + "' and flag=1 and scheduleTime is not null ");

		if (!staffId.trim().toLowerCase().equals("admin")) {
			sql.append(" and mt.staffid='" + staffId + "' ");
			count.append(" and mt.staffid='" + staffId + "' ");
		}
		if (msgList.getContent() != null
				&& !msgList.getContent().trim().equals("")) {
			w.append(" and mt.content like '%"
					+ msgList.getContent().replace("'", "''") + "%' ");
		}
		if (msgList.getUserName() != null
				&& !msgList.getUserName().trim().equals("")) {
			w.append(" and mt.staffId like '%" + msgList.getUserName() + "%' ");
		}
		if (w != null && w.length() > 0) {
			sql.append(w);
		}
		sql.append(" and p.taskID=mt.Id order by mt.scheduleTime");
		List<MessageBean> lists = DbBean.select(sql.toString(), null, 0,
				Integer.MAX_VALUE, MessageBean.class);

		return lists;
	}

	@SuppressWarnings("unchecked")
	public String deleteSchedule(String[] ids, String routeType, String EId,
			String flag, MessageListForm msgList) {
		int smsbalance = DbBean.getInt(
				"select smsBalance from Sms_Basicinf where eid='" + EId + "' ",
				null);
		int a = 0;
		int succ = 0;
		int err = 0;
		DbBean db = new DbBean();
		Connection conn = null;
		Statement stm = null;
		Statement stm1 = null;
		Statement stm2 = null;
		ResultSet rs = null;
		try {
			conn = db.getConnection();
			conn.setAutoCommit(false);
			stm = conn.createStatement();
			stm1 = conn.createStatement();
			stm2 = conn.createStatement();
			if (flag.equals("1")) {
				StringBuilder sql = new StringBuilder();
				sql.append(" select * from task where  EId='" + EId
						+ "' and flag=1 and scheduleTime is not null ");
				if (!StringUtil.isEmpty(msgList.getUserName())) {
					sql.append(" and staffId='"
							+ msgList.getUserName().trim().replace("'", "''")
							+ "'");
				}
				if (!StringUtil.isEmpty(msgList.getContent())) {
					sql.append(" and content like '"
							+ msgList.getContent().trim().replace("'", "''")
							+ "%' ");
				}
				rs = stm.executeQuery(sql.toString());
				while (rs.next()) {
					Timestamp schuedle = rs.getTimestamp("scheduleTime");
					if ((schuedle.getTime() - System.currentTimeMillis()) < 300000) {
						err++;
					} else {
						a = stm1.executeUpdate(" delete from task where  id='"
								+ rs.getString("ID") + "'");
						if (a > 0)
							stm2
									.executeUpdate("update Sms_Basicinf set smsBalance=smsBalance+"
											+ rs.getInt("splitCount")
											* rs.getInt("phoneNum")
											+ " where eid='" + EId + "'");
						succ++;
					}
				}
			} else {
				for (String msgid : ids) {
					rs = stm.executeQuery("select * from task where id='"
							+ msgid + "'");
					if (rs.next()) {
						Timestamp schuedle = rs.getTimestamp("scheduleTime");
						if ((schuedle.getTime() - System.currentTimeMillis()) < 300000) {
							err++;
						} else {
							a = stm1
									.executeUpdate(" delete from task where  id='"
											+ rs.getString("ID") + "'");
							if (a > 0)
								stm2
										.executeUpdate("update Sms_Basicinf set smsBalance=smsBalance+"
												+ rs.getInt("splitCount")
												* rs.getInt("phoneNum")
												+ " where eid='" + EId + "'");
							succ++;
						}
					}
				}
			}
			conn.commit();
			conn.setAutoCommit(true);
			if (succ > 0 && err > 0)
				return "成功删除" + succ + "批定时短信,另有" + err + "批短信已经发出或正在发送!";
			else if (succ > 0 && err == 0)
				return "成功删除" + succ + "批定时短信!";
			else
				return "删除失败,所选的" + err + "批定时短信已经发出或正在发送!";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (stm != null)
				try {
					stm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (stm1 != null)
				try {
					stm1.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (stm2 != null)
				try {
					stm2.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

	}

	@SuppressWarnings("unchecked")
	public List<SendHistory> findAllSendHis(SendHistoryForm hisList,
			String EId, String routeType, String staffId) {
		StringBuffer sql = new StringBuffer();
		StringBuffer w = new StringBuffer();
		StringBuffer count = new StringBuffer();
		List list = new ArrayList();
		List values = new ArrayList();
		if (EId.equals("000000")) {
			return null;
		} else {
			sql
					.append("select his.staffId,his.id,his.messageId,his.routeType,his.phone,his.name,his.content,"
							+ "his.result as result,his.status,his.smsNum,his.smssign,his.sendTime,his.addTime,his.descript"
							+ " from smsmt his ");
			count.append("select count(*) from smsmt his      ");
			if (!staffId.trim().toLowerCase().equals("admin")) {
				w.append(" and lower(his.staffid)=? ");
				values.add(staffId.toLowerCase());
			}

			if (hisList.getStateTime() != null
					&& !hisList.getStateTime().trim().equals("")) {
				w.append(" and his.sendTime>=? ");
				values
						.add(StringUtil.parseTimestamp(hisList.getStateTime(),
								0));
			}
			if (hisList.getEndTime() != null
					&& !hisList.getEndTime().trim().equals("")) {
				w.append(" and his.sendTime<? ");
				values.add(StringUtil.parseTimestamp(hisList.getEndTime(), 1));
			}
			if (hisList.getPhone() != null
					&& !hisList.getPhone().trim().equals("")) {
				w.append(" and his.phone like ? ");
				values.add(hisList.getPhone().trim() + "%");
			}
			if (hisList.getContent() != null
					&& !hisList.getContent().trim().equals("")) {
				w.append(" and his.content like ? ");
				values.add(hisList.getContent().trim() + "%");
			}
			if (hisList.getStatus() != null
					&& !hisList.getStatus().trim().equals("")) {
				if (hisList.getStatus().equals("etc")) {
					w.append(" and   his.status<>'0' and his.status<>'10004' ");
				} else if (hisList.getStatus().equals("发出")) {
					w
							.append(" and (his.status is null or his.status='10004') ");

				} else
					w.append(" and his.status='0' ");
			}
			w.append(" and his.EId='" + EId
					+ "' and his.result>-1 and his.isdelete=0  ");
			if (!StringUtil.isEmpty(w.toString())) {
				sql.append(" where ").append(w.toString().substring(4));
				count.append(" where ").append(w.toString().substring(4));
			}
			 
			hisList.setTotalItems(DbBean.getInt(count.toString(), values
					.toArray(new Object[values.size()])));
			 
					 
			hisList.adjustPageIndex();

			sql.append(" order by his.sendTime desc ");
			List<SendHistory> sendHistories = DbBean.select(sql.toString(),
					values.toArray(new Object[values.size()]), hisList
							.getStartIndex(), hisList.getPageSize(),
					SendHistory.class); 
		 
			if (sendHistories != null && sendHistories.size() != 0) {
				SendHistory sendHistory = null;
				for (int i = 0; i < sendHistories.size(); i++) {
					sendHistory = sendHistories.get(i);
					if (sendHistory.getName() == null)
						sendHistory.setName("");
					if (0==sendHistory.getResult()) {
						sendHistory.setResultDesc("成功");
					} else {
						sendHistory.setResultDesc("已发送");
					}
					if (sendHistory.getStatus() == null
							|| sendHistory.getStatus().trim().length() == 0
							|| sendHistory.getStatus().equals("10004")) {
						sendHistory.setStatus("发出");
					} else if (sendHistory.getStatus().equals("0")) {
						sendHistory.setStatus("收到");
					} else
						sendHistory.setStatus("etc");

					sendHistories.set(i, sendHistory);
				}
			}
			return sendHistories;
		}

	}

	@SuppressWarnings("unchecked")
	public int sendMessageAgain(String[] ids, String EId, String routeType,
			int length) throws NoteException {
		IMessageDao dao = new MessageDao();
		DbBean db = new DbBean();
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement phonePrep = null;
		int count = 0;
		if (ids != null && ids.length > 0) {
			try {
				conn = db.getConnection();
				conn.setAutoCommit(false);
				String sql2 = "insert into task(Id,content,mesType,splitCount,"
						+ "staffId,smsNum,smsSign,EId,routeType,addTime,flag,Meslength) values(?,?,?,?,?,?,?,?,?,?,?,?)";
				String sql3 = "insert into phones(phone,name,phoneType,eid,staffId,borndate,taskID) values(?,?,?,?,?,?,?)";
				ps = conn.prepareStatement(sql2);
				phonePrep = conn
						.prepareStatement("insert into phones(phone,name,phoneType,eid,staffId,borndate,taskID) values(?,?,?,?,?,?,?)");
				for (int i = 0; i < ids.length; i++) {
					if (ids[i].equals("-1"))
						continue;
					String sql = "select  his.staffId,his.routeType,his.phone,his.name,his.content,"
							+ "his.smsNum,his.smssign,eid,staffId,phoneType from smsmt his where id=?";
					SendHistory sendHis = dao.findSendHistoryById(sql, ids[i]);
					String ID = UUIDUtil.generate();

					ps.setString(1, ID);
					ps.setString(2, sendHis.getContent());
					ps.setString(3, "普通");
					ps.setInt(4, 1);
					ps.setString(5, sendHis.getStaffId());
					ps.setString(6, sendHis.getSmsNum());
					ps.setString(7, sendHis.getSmsSign());
					ps.setString(8, sendHis.getEId());
					ps.setString(9, sendHis.getRouteType());
					ps.setTimestamp(10, TimeUtil.now());
					ps.setInt(11, 0);
					ps.setInt(12, length);
					ps.addBatch();
					phonePrep.setString(1, sendHis.getPhone());
					phonePrep.setString(2, sendHis.getName());
					phonePrep.setInt(3, sendHis.getPhoneType());
					phonePrep.setString(4, sendHis.getEId());
					phonePrep.setString(5, sendHis.getStaffId());
					phonePrep.setTimestamp(6, TimeUtil.now());
					phonePrep.setString(7, ID);
					phonePrep.addBatch();
					count++;
				}
				ps.executeBatch();
				phonePrep.executeBatch();
				conn.commit();
				conn.setAutoCommit(true);
			} catch (Exception e) {
				e.printStackTrace();
				try {
					conn.rollback();
				} catch (SQLException e1) {

					e1.printStackTrace();
				}
			} finally {
				try {
					if (ps != null)
						ps.close();
					if (conn != null)
						conn.close();
				} catch (Exception e) {

				}
			}

		}
		return count;
	}

	@SuppressWarnings("unchecked")
	public String sendMessage(String content, String mesType,
			String checkNeedChk, String userId, String smsNum, String smsSign,
			String EId, String isSchedule, String routeType,
			String needSendTime, List<PhoneBean> list, String chkusevar)
			throws NoteException {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Phrase> findAllPhrase(String EId, PhraseListForm phraseList,
			String staffId) {
		IMessageDao dao = new MessageDao();
		StringBuffer sql = new StringBuffer();
		StringBuffer count = new StringBuffer();
		StringBuffer w = new StringBuffer();
		sql.append("select * from Phrase where EId=? ");
		count.append("select count(*) from Phrase where EId=? ");
		List params = new ArrayList();
		params.add(EId);
		if (!staffId.toLowerCase().equals("admin")) {
			w.append(" and staffid=? ");
			params.add(staffId);
		}
		if (phraseList.getPhraseContent() != null
				&& !phraseList.getPhraseContent().trim().equals("")) {
			w.append(" and phraseContent like ? ");
			params.add("%" + phraseList.getPhraseContent().replace("'", "''")
					+ "%");
		}
		if (phraseList.getPhraseType() != null
				&& !phraseList.getPhraseType().trim().equals("")) {
			w.append(" and phraseType like ?");
			params.add("%" + phraseList.getPhraseType() + "%");
		}
		if (w.length() > 0) {
			sql.append(w);
			count.append(w);
		}
		sql.append(" order by updateTime");
		List<Phrase> list = dao.findAllPhrase(sql.toString(), count.toString(),
				params.toArray(), phraseList);
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Feast> findAllFeast(String EId, FeastListForm feastList) {
		IMessageDao dao = new MessageDao();
		StringBuffer sql = new StringBuffer();
		StringBuffer count = new StringBuffer();
		@SuppressWarnings("unused")
		StringBuffer w = new StringBuffer();
		sql.append("select * from Feast where EId=?");
		count.append("select count(*) from Feast where EId=?");
		List params = new ArrayList();
		params.add(EId);
		sql.append(" order by worldDate");
		List<Feast> list = dao.findAllFeast(sql.toString(), count.toString(),
				params.toArray(), feastList);
		return list;
	}

	@SuppressWarnings( { "unchecked", "static-access" })
	public int deleteHistory(String[] ids, String routeType, String EId) {

		DbBean db = new DbBean();
		Connection conn = null;
		Statement stm = null;
		int count = 0;
		try {
			conn = db.getConnection();
			conn.setAutoCommit(false);
			stm = conn.createStatement();
			if (ids != null && ids.length > 0) {
				if ("-1".equals(ids[0])) {
					for (int i = 1; i < ids.length; i++) {
						stm.addBatch("update smsmt set isdelete=1 where Id='"
								+ ids[i] + "' and EId='" + EId + "' ");
						count++;
					}
				} else {
					for (int i = 0; i < ids.length; i++) {
						stm.addBatch("update smsmt set isdelete=1 where Id='"
								+ ids[i] + "' and EId='" + EId + "' ");
						count++;
					}
				}
				stm.executeBatch();
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			db.rollback(conn);
		} finally {
			db.close(stm, conn);
		}
		return count;
	}

	public String findAllHis(String EId, String routeType, String id,
			String flag) {
		String sql = "";
		if (flag.equals("0")) {
			sql = "select content from smsmt where Id=? and EId=?";
		} else {
			sql = "select content from Received_d where id=? and EId=?";
		}
		Object[] param = new Object[] { id, EId };
		IMessageDao dao = new MessageDao();
		return dao.findContent(sql, param);
	}

	@SuppressWarnings( { "unchecked", "deprecation" })
	public List<Receive> findNewRecive(RiceiveListForm riceF, String EId,
			String routeType, String staffId) {
		StringBuffer sql = new StringBuffer();
		StringBuffer count = new StringBuffer();
		StringBuffer w = new StringBuffer();
		sql
				.append("select * from Received_d where Eid=? and (isRead='0' or isRead is null) and receiveTime >=? and receiveTime<? ");
		count
				.append("select count(*) from Received_d where Eid=? and (isRead='0' or isRead is null) and receiveTime >=? and receiveTime<? ");
		List list = new ArrayList();
		list.add(EId);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		list.add(sdf.format(ts.getTime()));
		ts.setDate(ts.getDate() + 1);
		list.add(sdf.format(ts.getTime()));
		if (!staffId.trim().toLowerCase().equals("admin")) {
			sql.append(" and receiver=? ");
			count.append(" and receiver=? ");
			list.add(staffId);
		}

		if (riceF.getContent() != null && !riceF.getContent().trim().equals("")) {
			w.append(" and content like ? ");
			list.add("%" + riceF.getContent().trim().replace("'", "''") + "%");
		}
		if (riceF.getReceicer() != null
				&& !riceF.getReceicer().trim().equals("")) {
			w.append(" and receiver like ? ");
			list.add("%" + riceF.getReceicer().trim() + "%");
		}
		if (riceF.getPhone() != null && !riceF.getPhone().trim().equals("")) {
			w.append(" and phone like ? ");
			list.add(riceF.getPhone() + "%");
		}
		if (riceF.getSender() != null && !riceF.getSender().trim().equals("")) {
			w.append(" and sender like ? ");
			list.add("%" + riceF.getSender() + "%");
		}
		if (w.length() > 0) {
			sql.append(w);
			count.append(w);
		}
		sql.append(" order by receiveTime DESC");
		IMessageDao dao = new MessageDao();
		List<Receive> res = dao.findNewReceive(riceF, sql.toString(), count
				.toString(), list.toArray());
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<Receive> findAllRecive(RiceiveListForm riceF, String EId,
			String routeType, String staffId) {
		StringBuffer sql = new StringBuffer();
		StringBuffer count = new StringBuffer();
		StringBuffer w = new StringBuffer();
		sql.append("select * from Received_d where EId=? ");
		count.append("select count(*) from Received_d where eid=? ");
		List list = new ArrayList();
		list.add(EId);
		if (!staffId.trim().toLowerCase().equals("admin")) {
			sql.append(" and receiver=? ");
			count.append(" and receiver=? ");
			list.add(staffId);
		}
		if (riceF.getStart() != null && !riceF.getStart().trim().equals("")) {
			w.append(" and receiveTime>=? ");
			list.add(riceF.getStart());
		}
		if (riceF.getEnd() != null && !riceF.getEnd().trim().equals("")) {
			w.append(" and receiveTime<='" + riceF.getEnd() + " 24:00:00' ");
		}
		if (riceF.getContent() != null && !riceF.getContent().trim().equals("")) {
			w.append(" and content like ? ");
			list.add("%" + riceF.getContent().trim().replace("'", "''") + "%");
		}
		if (riceF.getReceicer() != null
				&& !riceF.getReceicer().trim().equals("")) {
			w.append(" and receiver like ? ");
			list.add("%" + riceF.getReceicer().trim() + "%");
		}
		if (riceF.getPhone() != null && !riceF.getPhone().trim().equals("")) {
			w.append(" and phone like ? ");
			list.add(riceF.getPhone() + "%");
		}
		if (riceF.getSender() != null && !riceF.getSender().trim().equals("")) {
			w.append(" and sender like ? ");
			list.add("%" + riceF.getSender() + "%");
		}
		if (w.length() > 0) {
			sql.append(w);
			count.append(w);
		}

		riceF.setTotalItems(DbBean.getInt(count.toString(), list.toArray()));
		riceF.adjustPageIndex();
		sql.append(" order by receiveTime DESC");
		return DbBean.select(sql.toString(), list.toArray(), riceF
				.getStartIndex(), riceF.getPageSize(), Receive.class);
	}

	@SuppressWarnings( { "unchecked", "static-access" })
	public String deleteReceive(String[] ids, String routeType, String id)
			throws NoteException {

		int i = 0;
		DbBean db = new DbBean();
		Connection conn = null;
		Statement stm = null;
		try {
			conn = db.getConnection();
			conn.setAutoCommit(false);
			stm = conn.createStatement();
			if (ids != null) {
				for (String str : ids) {
					stm.addBatch("delete from Received_d where EId='" + id
							+ "' and id='" + str + "'  ");
					i++;
				}
				stm.executeBatch();
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			db.rollback(conn);
			throw new NoteException("删除短信失败！");
		} finally {
			db.close(stm, conn);
		}
		return "成功删除" + i + "条短信！";
	}

	public List<PhoneBean> findPhone(String[] ids, String routeType, String id) {

		List<PhoneBean> list = new ArrayList<PhoneBean>();
		if (ids != null) {
			for (String str : ids) {
				String sql = "select phone,sender as name,phoneType from Received_d where EId='"
						+ id + "' and id=? ";
				PhoneBean pb = (PhoneBean) DbBean.selectFirst(sql,
						new Object[] { str }, PhoneBean.class);
				if (pb == null)
					continue;
				list.add(pb);
			}
		}
		return list;
	}

	public List<Staff> findAllStaff(String routeType, String EId, String staffId) {
		String sql = "select * from UserList where eid=? and userId<>?";
		IMessageDao dao = new MessageDao();
		Object[] params = new Object[] { EId, staffId };
		List<Staff> list = dao.findAllStaff(sql, params);
		return list;
	}

	@SuppressWarnings("unchecked")
	public String addReceive(String[] receivers, String[] messageId,
			String staffId, String Eid, String routeType) {
		IMessageDao dao = new MessageDao();
		int i = 0;
		List params = new ArrayList();

		for (String id : messageId) {
			String sql = "select * from Received_d where Eid='" + Eid
					+ "' and id=?";
			Receive rec = dao.findReceiveById(sql, id);
			for (String receive : receivers) {
				String sql1 = "insert into Received_d(id,receiver,phone,sender,content,EId,routeType,phoneType,smsNum,smsSign,receiveTime) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?)";
				String msgid = System.currentTimeMillis() + "|" + (i++) + "-"
						+ id;
				params.add(msgid);
				params.add(receive);
				params.add(rec.getPhone());
				params.add(rec.getSender());
				if (rec.getContent() == null)
					rec.setContent("");
				params.add("(" + staffId + "传阅)" + rec.getContent());
				params.add(Eid);
				params.add(routeType);
				params.add(rec.getPhoneType());
				params.add(rec.getSmsNum());
				params.add(rec.getSmsSign());
				params.add(TimeUtil.now().toString().substring(0, 19));
				int a = dao.addReceive(sql1, params.toArray());
				params.clear();
				if (a <= 0)
					i--;
			}
		}
		if (i > 0)
			return "您已成功向指定人员传阅了总计" + i + "条短信记录!";
		return "您未能传阅任何一条记录!";
	}

	@SuppressWarnings("unchecked")
	public List<PhoneBean> findLinkman(String[] ids, String eid,
			String groupid, String flag, String staffId) {
		StringBuffer sql = new StringBuffer();
		List<PhoneBean> phones = new ArrayList<PhoneBean>();
		LoadPhoneService lps = new LoadPhoneService();
		if (flag.equals("all")) {
			sql
					.append("select phone,name,optionalContent as content,phoneType from linkman where eid='"
							+ eid + "' and status='1' ");
			if (groupid != null && !groupid.equals("0")) {
				if (groupid.equals("Null")) {
					sql.append(" and (groupid is null or groupid='Null') ");
				} else {
					sql.append(" and groupid='" + groupid + "' ");
				}

			}
			DbBean db = new DbBean();
			Connection conn = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				conn = db.getConnection();
				pstm = conn.prepareStatement(sql.toString());
				rs = pstm.executeQuery();
				while (rs.next()) {
					if (lps.IsPhone(rs.getString("phone")) != -1) {
						PhoneBean p = new PhoneBean();
						p.setPhone(rs.getString(1));
						p.setName(rs.getString(2));
						p.setContent(rs.getString(3));
						p.setPhoneType(rs.getInt(4));
						phones.add(p);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if (pstm != null)
					try {
						pstm.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}

		} else {

			for (int i = 0; i < ids.length; i++) {
				if (ids[i].equals("-1"))
					continue;
				String sql1 = " select phone,name,optionalContent as content,phoneType from linkman where linkid='"
						+ ids[i] + "' and status='1' ";
				PhoneBean p = (PhoneBean) DbBean.selectFirst(sql1, null,
						PhoneBean.class);
				if (p == null)
					continue;
				if (lps.IsPhone(p.getPhone()) == -1)
					continue;
				phones.add(p);
			}
		}
		return phones;
	}

	@SuppressWarnings("unchecked")
	public int findLinkman(String[] ids, String eid, String groupid,
			String flag, String staffId, String sessionid,String taskID) {
		StringBuffer sql = new StringBuffer();
		int total = 0;
		LoadPhoneService lps = new LoadPhoneService();

		if (flag.equals("all")) {
			sql
					.append("select phone,name,optionalContent as content,phoneType from linkman where eid='"
							+ eid + "' and status='1' ");
			if (groupid != null && !groupid.equals("0")) {
				if (groupid.equals("Null")) {
					sql.append(" and (groupid is null or groupid='Null') ");
				} else {
					sql.append(" and groupid='" + groupid + "' ");
				}

			} else if (!staffId.trim().toLowerCase().endsWith("admin")) {
				sql.append(" and lower(staffid)='" + staffId.toLowerCase()
						+ "'");
			}
			DbBean db = new DbBean();
			Connection conn = null;
			PreparedStatement pstm = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				conn = db.getConnection();
				conn.setAutoCommit(false);
				pstm = conn.prepareStatement(sql.toString());
				ps = conn
						.prepareStatement("insert into phones(phone,name,content,phonetype,sessionid,eid,staffID,borndate,taskID) values(?,?,?,?,'"
								+ sessionid
								+ "','"
								+ eid
								+ "','"
								+ staffId
								+ "',?,?)");
				rs = pstm.executeQuery();
				while (rs.next()) {
					if (lps.IsPhone(rs.getString("phone")) != -1) {
						ps.setString(1, rs.getString(1));
						ps.setString(2, rs.getString(2));
						ps.setString(3, rs.getString(3));
						ps.setInt(4, rs.getInt(4));
						ps.setTimestamp(5, TimeUtil.now());
						ps.setString(6, taskID);
						ps.addBatch();
						total++;
						if (total % 100 == 0) {
								ps.executeBatch();
								ps.clearBatch();
						}
					}
				}
				try {
					ps.executeBatch();
					ps.clearBatch();
				} catch (Exception e) {
					conn.rollback();
					log.error(e.getMessage(), e);
				}
				conn.commit();
				conn.setAutoCommit(true);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if (pstm != null)
					try {
						pstm.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if (ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}

		} else {

			for (int i = 0; i < ids.length; i++) {
				if (ids[i].equals("-1"))
					continue;
				String sql1 = " select phone,name,optionalContent as content,phoneType from linkman where linkid='"
						+ ids[i] + "' and status='1' ";
				PhoneBean p = (PhoneBean) DbBean.selectFirst(sql1, null,
						PhoneBean.class);
				if (p == null)
					continue;
				if (lps.IsPhone(p.getPhone()) == -1)
					continue;
				DbBean
						.executeUpdate(
								"insert into phones(phone,name,content,phonetype,sessionid,eid,staffId,borndate,taskID) "
										+ "values(?,?,?,?,'"
										+ sessionid
										+ "','"
										+ eid
										+ "','"
										+ staffId + "',?,?)", new Object[] {
										p.getPhone(),
										StringUtil.getString(p.getName()),
										StringUtil.getString(p.getContent()),
										p.getPhoneType(), TimeUtil.now(),taskID });
				total++;
			}
		}
		return total;
	}

	public void modifyAccount(String eid, String name, String phone,
			String email,String smsbalance) {
		String sql = "update sms_basicinf set adminname=?,adminphone=?,adminemail=?,smsBalance=? where eid=?";
		Object[] params = new Object[] { name, phone, email,StringUtil.parseInt(smsbalance, 0), eid };
		IMessageDao dao = new MessageDao();
		dao.saveOrUpdateMessage(sql, params);
	}

	@SuppressWarnings("static-access")
	public List<PhoneBean> findPhones_load(String sessionId, String eid,
			String userid, LoadPhoneForm phoneForm,String taskID) {
		String sql = "select id as rowid,phone,name,content,phoneType from phones where sessionid='"
				+ sessionId + "' and taskID='"+taskID+"' ";
		String count = "select count(phone) from phones where sessionid='"
				+ sessionId + "' and taskID ='"+taskID+"' ";
		phoneForm.setTotalItems(DbBean.getInt(count, null));
		phoneForm.adjustPageIndex();
		int total = phoneForm.getPageSize();
		DbBean db = new DbBean();
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		List<PhoneBean> list = new ArrayList<PhoneBean>();
		int count1 = 0;
		try {
			conn = db.getConnection();
			stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = stm.executeQuery(sql);
			if (!rs.absolute(phoneForm.getStartIndex() + 1)) {
				return list;
			}
			do {
				PhoneBean pb = new PhoneBean();
				pb.setRowid(rs.getString(1));
				pb.setPhone(rs.getString(2));
				pb.setName(rs.getString(3));
				pb.setContent(rs.getString(4));
				pb.setPhoneType(rs.getInt(5));

				list.add(pb);
				count1++;
			} while ((count1 < total || total <= 0) && rs.next());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close(rs, stm, conn);
		}
		return list;
	}

	public void deletephones_load(String sessionid) {
		DbBean.executeUpdate("delete from phones where sessionid='" + sessionid
				+ "' and taskID is null ", null);
	}

	public String[] getContents(String content, String smssign, int num) {

		if (content == null || content.trim().length() == 0)
			return null;
		if (smssign == null || smssign.trim().length() == 0
				|| smssign.toLowerCase().equals("null")) {
			String[] contents = new String[(content.length() % num == 0) ? content
					.length()
					/ num
					: content.length() / num + 1];
			for (int i = 0; i < ((content.length() % num == 0) ? content
					.length()
					/ num : content.length() / num + 1); i++) {
				if ((i + 1) * num > content.length()) {
					contents[i] = content.substring(i * num);
				} else {
					contents[i] = content.substring(i * num, (i + 1) * num);
				}
			}
			return contents;
		} else {
			num = num - smssign.length();
			String[] contents = new String[(content.length() % num == 0) ? content
					.length()
					/ num
					: content.length() / num + 1];
			for (int i = 0; i < ((content.length() % num == 0) ? content
					.length()
					/ num : content.length() / num + 1); i++) {
				if ((i + 1) * num > content.length()) {
					contents[i] = content.substring(i * num) + smssign;
				} else {
					contents[i] = content.substring(i * num, (i + 1) * num)
							+ smssign;
				}
			}
			return contents;
		}

	}

	@SuppressWarnings( { "unchecked", "static-access" })
	public void sendWeather(Map weathers, Account account, String staffId)
			throws NoteException {
		String flag = "F";
		if (MessageIdCheck.isuse(flag)) {
			throw new NoteException("天气预报正在发送中");
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat smd1 = new SimpleDateFormat("yyyy-MM-dd");
		String time = smd.format(cal.getTime());
		String date = smd1.format(cal.getTime());
		int total = DbBean.getInt(
				"select count(*) from weather_sendflag where senddate='" + date
						+ "'", null);
		if (total > 0) {
			MessageIdCheck.notuse();
			throw new NoteException("今天已经发送过天气预报！");
		}
		DbBean db = new DbBean();
		Connection conn = null;
		Statement stm = null;
		PreparedStatement prep = null;
		PreparedStatement subP = null;
		Statement insert = null;
		Statement money = null;
		ResultSet rs = null;
		int sum = 0;
		try {
			conn = db.getConnection();
			conn.setAutoCommit(false);
			stm = conn.createStatement();

			money = conn.createStatement();
			insert = conn.createStatement();
			prep = conn
					.prepareStatement("insert into SMSMT(ID,eid,routeType,staffID,phone,phoneType, name,result,content,smsNum,smssign,addTime,messageId, mesType,isdelete,priority) "
							+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			subP = conn
					.prepareStatement("insert into SMSMT_SUBMIT(ID,PRIORITY,routeType,phoneType ) values(?,?,?,? ) ");
			Set<String> set = weathers.keySet();
			for (String key : set) {
				String weathercontent = (String) weathers.get(key);

				String[] contrents = getContents(weathers.get(key).toString(),
						account.getSmsSign(), account.getMessageLength());
				rs = stm
						.executeQuery("select distinct phone,name,phoneType from linkman man,linGroup gp where man.groupid=gp.groupid and man.eid=gp.eid and gp.eid='"
								+ account.getEId()
								+ "' and gp.groupname='"
								+ key + "'");

				while (rs.next()) {
					if (contrents == null || contrents.length == 0)
						continue;

					for (int i = 0; i < contrents.length; i++) {
						String ID = UUIDUtil.generate();
						ID = UUIDUtil.generate();
						prep.setString(1, ID);
						prep.setString(2, account.getEId());
						prep.setString(3, account.getRouteType());
						prep.setString(4, staffId);
						prep.setString(5, rs.getString("phone"));
						prep.setInt(6, rs.getInt("phoneType"));
						prep.setString(7, rs.getString("name"));
						prep.setInt(8, -1);
						prep.setString(9, contrents[i]);
						prep.setString(10, account.getSmsNum());
						prep.setString(11, account.getSmsSign());
						prep.setTimestamp(12, TimeUtil.now());
						prep.setString(13, "");
						prep.setString(14, "天气预报");
						prep.setInt(15, 0);
						prep.setInt(16, 20);
						prep.addBatch();
						subP.setString(1, ID);
						subP.setInt(2, 20);
						subP.setInt(3,account.getRouteType().hashCode());
						if(rs.getInt("phoneType")==0){
							subP.setInt(4, 0);
						}else{
							subP.setInt(4, 1);
						}
						
						subP.addBatch();
						sum++;
						if (sum % 100 == 0) {
							prep.executeBatch();
							subP.executeBatch();
							subP.clearBatch();
							prep.clearBatch();
						}
					}
				}
			}
			prep.executeBatch();
			subP.executeBatch();
			subP.clearBatch();
			prep.clearBatch();
			money.execute("update Sms_Basicinf set smsBalance=smsBalance-"
					+ sum + " where eid='" + account.getEId() + "' ");
			insert
					.execute("insert into weather_sendflag(senddate,borndate,eid,staffid) values('"
							+ date
							+ "','"
							+ time
							+ "','"
							+ account.getEId()
							+ "','" + staffId + "')");
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			MessageIdCheck.notuse();
			db.close(rs);
			db.close(stm);
			db.close(prep, subP);
			db.close(money, insert);
			db.close(conn);
		}
	}
}
