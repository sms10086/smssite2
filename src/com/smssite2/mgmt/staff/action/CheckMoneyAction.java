package com.smssite2.mgmt.staff.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.note.common.LogUtil;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class CheckMoneyAction extends Action{
	IStaffService service = new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
			String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
			String id=request.getParameter("id");
			String eid=request.getParameter("eid");
			try{
				String str=service.checkMoney(id,eid,staffId);
				request.setAttribute("message", str);
			}catch(Exception e){
				e.printStackTrace();
				request.setAttribute("message", e.getMessage());
				return mapping.findForward("failure");
			}
			LogUtil.writeLog("≥‰÷µ…Û∫À","…Û∫À",id,staffId,"000000");
		return mapping.findForward("success");
	}
}
