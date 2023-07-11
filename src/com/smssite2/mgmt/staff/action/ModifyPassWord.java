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

public class ModifyPassWord extends Action{	IStaffService service=new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String oldPw=request.getParameter("oldPassword");
		String newPw=request.getParameter("newPassword");
		String rePw=request.getParameter("rePassword");
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String userId=request.getParameter("userName");
		
		try{
			service.modifyPassword(userId,oldPw,newPw,rePw,EId);
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		}
		request.setAttribute("message", "ÐÞ¸ÄÃÜÂë³É¹¦!");
		return mapping.findForward("success");
	}
}

