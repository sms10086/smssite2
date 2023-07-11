package com.smssite2.mgmt.staff.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Role;
import com.note.bean.Staff;
import com.smssite2.mgmt.linkman.bean.Group;
import com.smssite2.mgmt.privilege.bean.MenuBean;
import com.smssite2.mgmt.privilege.service.IPrivilegeService;
import com.smssite2.mgmt.privilege.service.impl.PrivileService;
import com.smssite2.mgmt.staff.dao.IStaffDao;
import com.smssite2.mgmt.staff.dao.impl.StaffDao;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class BeforeModifyStaffAction extends Action{
	IPrivilegeService service = new PrivileService();
	IStaffService sService=new StaffService();
	IStaffDao dao = new StaffDao();;
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String userId=request.getParameter("ids");
		try{
			ArrayList<MenuBean> list=service.findMemuByEId(EId);
			Map orgs=dao.findAllOrg(EId);
			List<Role> roles=dao.findAllRole(EId);
			Staff staff=sService.findStaffById(userId,EId);
			
			List<Group> groups = dao.findAllGroup(EId);
			if(groups==null||groups.size()==0)throw new Exception("请先添加分组");
			request.setAttribute("groups", groups);

			request.setAttribute("staff", staff);
			request.setAttribute("orgs", orgs);
			request.setAttribute("roles", roles);
			request.setAttribute("list", list);
			request.setAttribute("flag", "add");
			
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
			e.printStackTrace();
		}
		return mapping.findForward("success");
	}
}

