package com.smssite2.mgmt.staff;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.note.NoteConstants;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.smssite2.mgmt.message.sendMessageServlet;

public class PushServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(sendMessageServlet.class);
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		Staff staff=(Staff) req.getSession().getAttribute(NoteConstants.SESSION_STAFF);
		Account account=(Account)req.getSession().getAttribute("account");
		if (staff!=null)req.getSession().setAttribute(NoteConstants.SESSION_STAFF, staff);
		if(account!=null)req.getSession().setAttribute("account", account);
	}
}
