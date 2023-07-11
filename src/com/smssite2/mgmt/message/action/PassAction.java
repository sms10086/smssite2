package com.smssite2.mgmt.message.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class PassAction extends Action{	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String[] receivers = request.getParameterValues("ids");
		String messageId = request.getParameter("messageId");
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		String Eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String routeType=((Account)request.getSession().getAttribute("account")).getRouteType();
		IMessageService service =new MessageService();
		if(messageId==null) return mapping.findForward("success");
		String[] messageids=messageId.split(";");
		
		try{
			String str=service.addReceive(receivers,messageids,staffId,Eid,routeType);
			request.setAttribute("message", str);
			request.setAttribute("messageids",messageId);
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("success");
		}
		return mapping.findForward("success");
	}
}
