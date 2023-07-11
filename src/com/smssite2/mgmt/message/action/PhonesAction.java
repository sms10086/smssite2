package com.smssite2.mgmt.message.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.smssite2.mgmt.message.bean.PhoneBean;
import com.smssite2.mgmt.message.form.LoadPhoneForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class PhonesAction extends Action{
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		IMessageService mservice=new MessageService();
		LoadPhoneForm phoneForm=(LoadPhoneForm)form;
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
		String eid =((Staff)request.getSession().getAttribute("staff")).getEId();
		String sessionid=request.getSession().getId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		String taskID=(String) request.getSession().getAttribute("taskID");
		if(isSchedule!=null&&isSchedule.equals("1")){
			needSendTime=year+"-"+month+"-"+day+" "+hour+":"+minute;
		}
		List<PhoneBean> pageList=mservice.findPhones_load(sessionid, eid, staffId, phoneForm,taskID);
		request.getSession().setAttribute("phones", pageList);
		request.getSession().setAttribute("pagination", phoneForm);
		request.setAttribute("mesType", mesType);
		request.setAttribute("content", content);
		request.setAttribute("chkusevar", chkusevar);
		request.setAttribute("needSendTime",needSendTime);
		request.setAttribute("isSchedule", isSchedule);
		request.setAttribute("selyear", year);
		request.setAttribute("selmonth", month);
		request.setAttribute("selday", day);
		request.setAttribute("selhour", hour);
		request.setAttribute("selminute", minute);
		return mapping.findForward("success");
	}
}
