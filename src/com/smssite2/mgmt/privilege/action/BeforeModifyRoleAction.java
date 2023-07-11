package com.smssite2.mgmt.privilege.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.smssite2.mgmt.privilege.bean.MenuBean;
import com.note.bean.Role;
import com.note.bean.Staff;
import com.smssite2.mgmt.privilege.service.IPrivilegeService;
import com.smssite2.mgmt.privilege.service.impl.PrivileService;

public class BeforeModifyRoleAction extends Action{
	IPrivilegeService service = new PrivileService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String roleId=request.getParameter("ids");
		String EId = ((Staff)request.getSession().getAttribute("staff")).getEId();
		try{
			Role role=service.findRoleByRoleId(roleId,EId);
			List<MenuBean> list=service.findMemuByEId(EId);
			request.setAttribute("role", role);
			request.setAttribute("list", list);
			request.setAttribute("flag", "modify");
			return mapping.findForward("success");
		}catch(Exception e){
				request.setAttribute("message", e.getMessage());
				e.printStackTrace();
				return mapping.findForward("failure");
		}
		}
		
	}


