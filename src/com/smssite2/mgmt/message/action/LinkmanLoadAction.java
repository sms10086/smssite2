package com.smssite2.mgmt.message.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.smssite2.mgmt.message.bean.PhoneBean;
import com.smssite2.mgmt.message.form.LoadPhoneForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class LinkmanLoadAction extends Action{	IMessageService service =new MessageService();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		LoadPhoneForm phoneForm=(LoadPhoneForm)form;
		
		String flag=request.getParameter("flag");
		String[] ids=request.getParameterValues("ids");
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		String groupid=request.getParameter("groupid");
		HttpSession session =request.getSession();
		String sessionId=session.getId();
		String taskID=(String) session.getAttribute("taskID");
		int total=0;
		try{
			total=service.findLinkman(ids, eid, groupid, flag, staffId,sessionId,taskID);
			session.removeAttribute("phones");
			List<PhoneBean> pageList=service.findPhones_load(sessionId, eid, staffId, phoneForm,taskID);
			request.getSession().setAttribute("phones", pageList);
			request.getSession().setAttribute("pagination", phoneForm);
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
		}
		request.setAttribute("message", "成功导入"+total+"条记录");
		return mapping.findForward("success");
	}
	
}
