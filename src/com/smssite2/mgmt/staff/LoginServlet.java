package com.smssite2.mgmt.staff;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.note.MD5Proxy;
import com.note.NoteException;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.note.common.LogUtil;
import com.note.common.StringUtil;
import com.note.common.TimeUtil;
import com.smssite2.mgmt.message.sendMessageServlet;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class LoginServlet extends HttpServlet{	private static StaffService  staffService = new StaffService();
	private static Log log = LogFactory.getLog(sendMessageServlet.class);
	private static String key="bW9tZW50ZWs";
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doPost(req, resp);
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp){
		String eid=req.getParameter("eid");
		String username=req.getParameter("username");
		String pwd=req.getParameter("pwd");
		String callid=req.getParameter("callid");
		String sig=req.getParameter("sig");
	 
		Account account=null;
		Staff staff=null;
	    int result=-1;
		try {
			PrintWriter out = resp.getWriter();
			try{
				account = staffService.findAccount(eid);
			}catch(NoteException e){
				e.printStackTrace();
			}
			try{
				staff=staffService.findStaffById(username, eid);
			}catch(NoteException e){
				e.printStackTrace();
			}
			if(account==null)result=1;  
			else if(staff==null)result=2; 
			else if(callid==null||!callid.equals(StringUtil.parseTimestampToStr(TimeUtil.now(), "yyyyMMdd")))
				result=4;
			else if(sig==null||!sig.equals(MD5Proxy.getMd5Str(eid.toLowerCase()+username.toLowerCase()+callid+key)))
				result=5;
			else if(pwd!=null&&!MD5Proxy.getMd5Str(staff.getNewpwd()).equals(pwd))
				result=3;
			else{
				String feastname=staffService.findFeastName(eid,StringUtil.getTime(0));
				req.getSession().setAttribute("staff", staff);
				req.getSession().setAttribute("account", account);
				req.getSession().setAttribute("feastName", feastname);
				LogUtil.writeLog("用户登陆","登陆","登陆时间:"+TimeUtil.now()+" IP:"+req.getRemoteAddr(),username,eid);
				result=0;
				System.out.println(req.getSession().getId()+">>>>>>>>>>>>>>>>"+req.getRequestedSessionId());
			}
				out.print(result+"|"+req.getSession().getId());
				req.getRequestDispatcher("index.jsp").forward(req, resp);
				out.flush();
				
			out.close();
		
		} catch (Exception e) {
			e.printStackTrace();
			try {
				req.setAttribute("message", e.getMessage());
				req.setAttribute("EId", eid);
				req.setAttribute("staffId", username);
				
			} catch (Exception e1) {
				
			}
		}
	}
	
}
