package com.smssite2.mgmt.message.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class ListStaffAction extends Action{
	IMessageService service=new MessageService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		
		String ids=request.getParameter("messageids");
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String routeType= ((Account)request.getSession().getAttribute("account")).getRouteType();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		try{
			List<Staff> list=service.findAllStaff(routeType,EId,staffId);
			request.setAttribute("staffs", list);
			request.setAttribute("ids",ids.substring(2));
		}catch(Exception e){
			request.setAttribute("messagt", e.getMessage());
		
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}

}


