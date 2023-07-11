package com.smssite2.mgmt.staff.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class ChangeIsSmsSignAction extends Action{	IStaffService service = new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String[] ids=request.getParameterValues("ids");
		String locksign=request.getParameter("locksign");
		 
		try{
			service.changeIsSmsSign(ids,locksign);
		}catch(Exception e){
			e.printStackTrace();
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}
}
