package com.smssite2.mgmt.linkman.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.smssite2.mgmt.message.service.impl.LoadPhoneService;

public class LoadPhoneAction extends Action{
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		LoadPhoneService  service=new LoadPhoneService();
		String newfn=request.getParameter("newfn");
		String groupid=request.getParameter("groupid");
		String spit=request.getParameter("spit");
		String Eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffid=((Staff)request.getSession().getAttribute("staff")).getUserId();
		try{
			String message=service.LoadLinkman(newfn, spit,groupid,Eid,staffid);
			request.setAttribute("message", message);
			return mapping.findForward("success");
		}catch(Exception e){
			 
			 request.setAttribute("message", e.getMessage());
			 return mapping.findForward("success");
		}
	}
}
