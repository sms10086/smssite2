package com.smssite2.mgmt.staff.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class DeleteMoneyAction extends Action{	IStaffService service = new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String[] ids=request.getParameterValues("ids");
		
		try{
			service.deleteMoney(ids);
		}catch(Exception e){
			e.printStackTrace();
			return mapping.findForward("failure");
		}
		request.setAttribute("message", ids.length+"个充值请求已被删除");
		return mapping.findForward("success");
	}
}
