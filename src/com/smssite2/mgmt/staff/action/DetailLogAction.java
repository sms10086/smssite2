package com.smssite2.mgmt.staff.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Log;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class DetailLogAction extends Action{	IStaffService service=new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String logId=request.getParameter("logId");
		try{
			Log log=service.findLogById(logId);
			request.setAttribute("log",log);
			return mapping.findForward("success");
			}catch(Exception e){
				e.printStackTrace();
				return mapping.findForward("failure");
			}
		}
	}
