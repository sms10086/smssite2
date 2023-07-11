package com.smssite2.mgmt.privilege.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Role;
import com.note.bean.Staff;
import com.smssite2.mgmt.privilege.form.RoleListForm;
import com.smssite2.mgmt.privilege.service.IPrivilegeService;
import com.smssite2.mgmt.privilege.service.impl.PrivileService;

public class ListRoleAction extends Action{
	IPrivilegeService service = new PrivileService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		RoleListForm roleList=(RoleListForm)form;
		
		String EId =((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")){
			request.setAttribute("message", "用户"+staffId+"没有[角色设置]的操作权限！");
			return mapping.findForward("err");
		}
		
		List<Role> roles=new ArrayList<Role>();
		try{
			roles=service.findRoleByEId(roleList,EId);
		
		}catch(Exception e){
			e.printStackTrace();
			return mapping.findForward("failure");
		}
		request.setAttribute("roles", roles);
		request.setAttribute("pagination",roleList);
		return mapping.findForward("success");
	}
}
