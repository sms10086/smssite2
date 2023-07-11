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

public class AddRoleAction extends Action{	IPrivilegeService service = new PrivileService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String uname=request.getParameter("uname");
		String umemu=request.getParameter("umemu");
		String chkadmin=request.getParameter("chkadmin");
		String EId =((Staff)request.getSession().getAttribute("staff")).getEId();
		String[] opBox=request.getParameterValues("opBox");
		String roleId=request.getParameter("txtUserid");
		String flag=request.getParameter("flag");
		String credit="";
		if(opBox!=null){
			for(int i=0;i<opBox.length;i++){
				credit=credit+opBox[i];
			}
		}
		if(chkadmin!=null&&chkadmin.equals("1"))credit="admin";
	
		try{
			service.addRole(uname,umemu,credit,EId,flag,roleId);
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
			e.printStackTrace();
			return mapping.findForward("failure");
		}
		request.setAttribute("flag", flag);
		request.setAttribute("message", "你已成功添加或修改一个角色。名称："+uname);
		return mapping.findForward("success");
	}
	
}

