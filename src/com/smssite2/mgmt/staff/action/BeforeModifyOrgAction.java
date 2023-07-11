package com.smssite2.mgmt.staff.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.note.bean.Team;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class BeforeModifyOrgAction extends Action{	IStaffService service = new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String ids=request.getParameter("ids");
		String EId = ((Staff)request.getSession().getAttribute("staff")).getEId();
		try{
			Team org=service.findOrgById(ids,EId);
			request.setAttribute("org", org);
		}catch(Exception e){
			
		}
		return mapping.findForward("success");
	}
}

