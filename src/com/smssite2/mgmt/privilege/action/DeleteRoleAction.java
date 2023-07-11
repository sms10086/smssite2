package com.smssite2.mgmt.privilege.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.smssite2.mgmt.privilege.service.IPrivilegeService;
import com.smssite2.mgmt.privilege.service.impl.PrivileService;

public class DeleteRoleAction extends Action{
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] roleId=request.getParameterValues("ids");
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		IPrivilegeService service =new PrivileService();
		System.out.println(roleId.length);
		try{
			service.deleteRole(EId,roleId);
			request.setAttribute("message","成功删除"+roleId.length+"个角色！");
			
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
			e.printStackTrace();
			return mapping.findForward("failure");
		}
		
		return mapping.findForward("success");
	}
}
