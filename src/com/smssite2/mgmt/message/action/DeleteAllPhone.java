package com.smssite2.mgmt.message.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.common.DbBean;

public class DeleteAllPhone extends Action{	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String content =request.getParameter("content");
		String mesType =request.getParameter("mesType");
		
		String isSchedule =request.getParameter("isSchedule");
		String chkusevar=request.getParameter("Chkusevar");
		String hour=request.getParameter("selhour");
		String minute=request.getParameter("selminute");
		String year=request.getParameter("selyear");
		String month=request.getParameter("selmonth");
		String day=request.getParameter("selday");
		String needSendTime=null;
		
		if(isSchedule!=null&&isSchedule.equals("1")){
			needSendTime=year+"/"+month+"/"+day+" "+hour+":"+minute;
		}
		HttpSession session=request.getSession();
		String taskID=(String) session.getAttribute("taskID");
		DbBean.executeUpdate("delete from phones where sessionid='"+session.getId()+"' and taskID='"+taskID+"'", null);
		session.getAttribute("phones");
		session.removeAttribute("phones");
		session.removeAttribute("pagination");
		request.setAttribute("mesType", mesType);
		request.setAttribute("content", content);
		request.setAttribute("chkusevar", chkusevar);
		request.setAttribute("needSendTime",needSendTime);
		request.setAttribute("selyear", year);
		request.setAttribute("selmonth", month);
		request.setAttribute("selday", day);
		request.setAttribute("selhour", hour);
		request.setAttribute("selminute", minute);
		request.setAttribute("isSchedule", isSchedule);
		return mapping.findForward("success");
	}
}
