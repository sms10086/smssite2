package com.smssite2.mgmt.staff.action;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.smssite2.mgmt.staff.dao.IStaffDao;
import com.smssite2.mgmt.staff.dao.impl.StaffDao;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class BeforeModifyAccountAction extends Action{

	IStaffService sService=new StaffService();
	IStaffDao dao = new StaffDao();;
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String eid=request.getParameter("ids");
		try{
			
			Map orgs=dao.findAllOrg(EId);
			Map route=dao.findAllRoute();
			Account account=sService.findAccount(eid);
			request.setAttribute("account", account);
			request.setAttribute("orgs", orgs);
			request.setAttribute("routes", route);
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
			e.printStackTrace();
		}
		return mapping.findForward("success");
	}
}
