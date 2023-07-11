package com.smssite2.mgmt.message.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class AccountAction extends Action{	private static Log log = LogFactory.getLog(AccountAction.class);
	private IStaffService staffService = new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("611")<0){
			request.setAttribute("message", "用户"+staffId+"没有[短信帐户管理]的操作权限！");
			return mapping.findForward("err");
		}
		
		try{
			Account account=staffService.findAccount(eid);
			request.setAttribute("account", account);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return mapping.findForward("success");
	}
}
