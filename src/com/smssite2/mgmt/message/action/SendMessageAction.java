package com.smssite2.mgmt.message.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.NoteException;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.note.common.DbBean;
import com.note.common.StringUtil;
import com.note.common.TimeUtil;
import com.note.common.UUIDUtil;
import com.smssite2.mgmt.basicsetting.bean.Black;
import com.smssite2.mgmt.message.bean.SiftBean;
import com.smssite2.mgmt.message.dao.IMessageDao;
import com.smssite2.mgmt.message.dao.impl.MessageDao;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class SendMessageAction extends Action {
	private static Log log = LogFactory.getLog(SendMessageAction.class);
	IMessageService service = new MessageService();

	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String content = request.getParameter("content");
		String mesType = request.getParameter("mesType");
		String mesType2 = request.getParameter("mesType2");
		String userId = ((Staff) request.getSession().getAttribute("staff"))
				.getUserId();
		String smsNum = ((Account) request.getSession().getAttribute("account"))
				.getSmsNum();
		String smsSign = ((Account) request.getSession()
				.getAttribute("account")).getSmsSign();
		String isSign = ((Account) request.getSession().getAttribute("account"))
				.getIssmsSign();
		String EId = ((Staff) request.getSession().getAttribute("staff"))
				.getEId();
		int messageLength = ((Account) request.getSession().getAttribute(
				"account")).getMessageLength();
		if (messageLength == 0)
			messageLength = 60;
		String isSchedule = request.getParameter("isSchedule");
		String routeType = ((Account) request.getSession().getAttribute(
				"account")).getRouteType();
		String chkusevar = request.getParameter("Chkusevar");
		String hour = request.getParameter("selhour");
		String minute = request.getParameter("selminute");
		String year = request.getParameter("selyear");
		String month = request.getParameter("selmonth");
		String day = request.getParameter("selday");
		String needSendTime = null;
		if (isSign != null && isSign.equals("0")) {
			smsSign = "";
		}
		if (mesType2 != null && mesType2.trim().length() > 0)
			mesType = mesType2;

		if (isSchedule != null && isSchedule.equals("1")) {
			needSendTime = year + "-" + month + "-" + day + " " + hour + ":"
					+ minute;
		} else {
			isSchedule = "0";
			needSendTime = "";
		}
		HttpSession session = request.getSession();
		String taskID = (String) session.getAttribute("taskID");
		DbBean db = new DbBean();
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			IMessageDao dao = new MessageDao();
			if (content == null || content.trim().length() == 0)
				throw new NoteException("短信内容不能为空！");
			List<SiftBean> sifts = dao.findAllSift(EId);

			if (sifts != null && sifts.size() != 0) {
				for (int i = 0; i < sifts.size(); i++) {
					if (sifts.get(i).getSiftContent() != null
							&& content.indexOf(sifts.get(i).getSiftContent()) >= 0) {
						throw new NoteException("短信中含有非法字符"
								+ sifts.get(i).getSiftContent());
					}
				}
			}

			int totalPhone = DbBean.getInt(
					"select count(ID) from phones where taskID=? ",
					new Object[] { taskID });
			conn = db.getConnection();

			stm = conn.createStatement();
			rs = stm
					.executeQuery("select ID, phone,name,content,phonetype from phones  where taskID='"
							+ taskID + "'");
			if (!rs.next())
				throw new NoteException("无手机号码，未能生成短息！");

			List<Black> blacks = DbBean.select(
					"select blackphonenum from black where eid='" + EId + "'",
					null, 0, Integer.MAX_VALUE, Black.class);
			StringBuffer blackPhone = new StringBuffer();
			for (Black black : blacks) {
				if (black.getBlackPhoneNum() == null
						|| black.getBlackPhoneNum().trim().length() == 0)
					continue;
				if (black.getBlackPhoneNum().startsWith("86"))
					blackPhone.append(black.getBlackPhoneNum().substring(2))
							.append("|");
				else
					blackPhone.append(black.getBlackPhoneNum()).append("|");
			}
			String p = "";
			if (!StringUtil.isEmpty(blackPhone.toString()))
				do {
					p = rs.getString("phone");
					if (p == null || p.trim().length() == 0)
						continue;
					if (p.startsWith("86"))
						p = p.substring(2);
					if (blackPhone.toString().indexOf(p) >= 0) {
						DbBean.executeUpdate("delete from phones where ID="
								+ rs.getInt("ID") + " ", null);
					}
				} while (rs.next());

			while (!StringUtil.isEmpty(smsSign) && content.endsWith(smsSign)) {
				content = content.substring(0, (content.length() - smsSign
						.length()));

			}
			int phoneNum = DbBean.getInt(
					"select count(ID) from phones where taskID=? ",
					new Object[] { taskID });
			int sendCount = (content.length() % messageLength == 0) ? content
					.length()
					/ messageLength : content.length() / messageLength + 1;
			DbBean
					.executeUpdate(
							"insert into task(ID,content,mesType,splitCount,phoneNum,addTime,staffID,smsNum,smsSign,eid,routeType,scheduleTime,flag,Meslength) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
							new Object[] { taskID, content, mesType, sendCount,
									phoneNum, TimeUtil.now(), userId, smsNum,
									smsSign, EId, routeType,
									TimeUtil.formatTime(needSendTime), 0,
									messageLength });
			String message = "总共" + phoneNum + "批短信已提交到后台待生成!";
			if ((totalPhone - phoneNum) > 0)
				message = message + "另外有" + (totalPhone - phoneNum)
						+ "个号码在黑名单中或可选内容中含非法字符。";
			request.setAttribute("message", message);
			request.getSession().removeAttribute("phones");
			request.getSession().removeAttribute("phonesSet");
			request.getSession().removeAttribute("pagination");
		} catch (NoteException e) {
			log.error(e.getMessage());
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		} finally {
			session.removeAttribute("taskID");
			session.setAttribute("taskID", UUIDUtil.generate());
			db.close(rs);
			db.close(stm, conn);
		}
		return mapping.findForward("success");
	}
}
