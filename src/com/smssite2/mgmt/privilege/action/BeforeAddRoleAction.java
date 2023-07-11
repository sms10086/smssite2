package com.smssite2.mgmt.privilege.action;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Account;
import com.smssite2.mgmt.privilege.bean.MenuBean;
import com.smssite2.mgmt.privilege.service.IPrivilegeService;
import com.smssite2.mgmt.privilege.service.impl.PrivileService;

public class BeforeAddRoleAction extends Action{
	IPrivilegeService service = new PrivileService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	String EId=((Account)request.getSession().getAttribute("account")).getEId();
		try{
			ArrayList<MenuBean> list=service.findMemuByEId(EId);
			request.setAttribute("list", list);
			request.setAttribute("flag", "add");
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
			e.printStackTrace();
			return mapping.findForward("failure");
		}
		
		return mapping.findForward("success");
	}
	
}

