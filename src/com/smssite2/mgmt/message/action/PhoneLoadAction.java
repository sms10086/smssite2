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
import com.note.common.UUIDUtil;
import com.smssite2.mgmt.message.bean.PhoneBean;
import com.smssite2.mgmt.message.form.LoadPhoneForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.LoadPhoneService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class PhoneLoadAction extends Action{	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		LoadPhoneForm phoneForm=(LoadPhoneForm)form;
		LoadPhoneService  service=new LoadPhoneService();
		String newfn=request.getParameter("newfn");
		String spit=request.getParameter("spit");
		HttpSession session =request.getSession();
		String eid=((Staff)session.getAttribute("staff")).getEId();
		String userid=((Staff)session.getAttribute("staff")).getUserId();
		String sessionId=session.getId();
		IMessageService mservice=new MessageService();
		try{
			
			String taskID=(String) session.getAttribute("taskID");
			if(taskID==null){
				taskID=UUIDUtil.generate();
				session.setAttribute("taskID", taskID);
			}
		 
			String  message=service.LoadPhones(newfn, spit, sessionId, eid, userid,taskID);
			
			session.removeAttribute("phones");
			List<PhoneBean> pageList=mservice.findPhones_load( sessionId, eid, userid,phoneForm,taskID);
			
			request.getSession().setAttribute("phones", pageList);
			request.getSession().setAttribute("pagination", phoneForm);
			request.setAttribute("message", message);
			return mapping.findForward("success");
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		}
		
	}
}
