package com.smssite2.mgmt.staff.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class DeleteAccountAction extends Action{	IStaffService service = new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String[] ids=request.getParameterValues("ids");
		
		try{
			service.deleteAccount(ids);
		}catch(Exception e){
			e.printStackTrace();
			return mapping.findForward("failure");
		}
		request.setAttribute("message", "删除"+ids.length+"个用户成功");
		return mapping.findForward("success");
	}
}
