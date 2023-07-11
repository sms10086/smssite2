package com.smssite2.mgmt.staff.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class ChangeLocksignAction extends Action{
	IStaffService service = new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String[] ids=request.getParameterValues("ids");
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String locksign=request.getParameter("locksign");
		String flag=request.getParameter("flag");
		String str="";
		String str1="";
		if(flag.equals("staff")){
			str="failure";str1="success";
		}else if(flag.equals("account")){
			str="failure1";str1="success1";
		}
		try{
			service.changeLocksign(ids,locksign,EId,flag);
		}catch(Exception e){
			e.printStackTrace();
			return mapping.findForward(str);
		}
		return mapping.findForward(str1);
	}
}
