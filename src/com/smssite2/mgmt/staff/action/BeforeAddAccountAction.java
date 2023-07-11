package com.smssite2.mgmt.staff.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.smssite2.mgmt.staff.dao.IStaffDao;
import com.smssite2.mgmt.staff.dao.impl.StaffDao;

public class BeforeAddAccountAction extends Action{	
	IStaffDao dao = new StaffDao();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String username=((Staff)request.getSession().getAttribute("staff")).getUserName();
		Calendar cale = Calendar.getInstance();
		SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd");
		String date=smd.format(cale.getTime());
		try{
			Map orgs=dao.findAllOrg(EId);
			Map route=dao.findAllRoute();
			request.setAttribute("orgs", orgs);
			request.setAttribute("routes", route);
			request.setAttribute("regDate", date);
			request.setAttribute("regDirector", username);
		}catch(Exception e){
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}
}

