package com.smssite2.mgmt.message.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.smssite2.mgmt.message.service.impl.MessageService;
import com.note.bean.Staff;
import com.smssite2.mgmt.message.service.IMessageService;

public class ModifyAccountAction extends Action{ 
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String name=request.getParameter("txtadmin");
		String phone=request.getParameter("txtphone");
		String email=request.getParameter("txtemail");
		String smsBalance=request.getParameter("smsBalance");
		IMessageService service =new MessageService();
		try{
			 service.modifyAccount(eid,name,phone,email,smsBalance);
		}catch(Exception e){
		}
		return mapping.findForward("success");
	}
}
