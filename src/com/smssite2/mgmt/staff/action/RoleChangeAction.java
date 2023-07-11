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

public class RoleChangeAction extends Action{	IStaffService service = new StaffService();IStaffDao dao = new StaffDao();
	IPrivilegeService pservice = new PrivileService();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String flag=request.getParameter("flag");
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String userId=request.getParameter("txtUserid");
		String userName=request.getParameter("txtUsername");
		String password=request.getParameter("txtPwd");
		String uteam=request.getParameter("uteam");
		String upost= request.getParameter("upost");
		String umemo=request.getParameter("txtMemo");
		String smsNum=request.getParameter("txtSmsnum");
		String smsSign=request.getParameter("txtSmssign");
		String lockSign=request.getParameter("Chklocksign");
		String groupID = request.getParameter("groupID");
		
		Staff staff=new Staff();
		staff.setEId(EId);
		staff.setUserId(userId);
		staff.setUserName(userName);
		staff.setPassword(password);
		staff.setUmemo(umemo);
		staff.setUPost(upost);
		staff.setUTeam(uteam);
		staff.setSmsSign(smsSign);
		staff.setSmsNum(smsNum);
		staff.setUValid(lockSign);
		staff.setGroupID(groupID);
		try{
			ArrayList<MenuBean> list=pservice.findMemuByEId(EId);
			Map orgs=dao.findAllOrg(EId);
			Role role =service.findRoleByName(upost,EId);
			List<Role> roles=dao.findAllRole(EId);
			
			List<Group> groups = dao.findAllGroup(EId);
			if(groups==null||groups.size()==0)throw new Exception("请先添加分组");
			request.setAttribute("groups", groups);
			
			request.setAttribute("orgs", orgs);
			request.setAttribute("roles", roles);
			request.setAttribute("role", role);
			request.setAttribute("staff", staff);
			request.setAttribute("list", list);
		}catch(Exception e){
			e.printStackTrace();
			return mapping.findForward("failure");
		}
		if(flag.equals("add")){
			return mapping.findForward("success");
		}else if(flag.equals("modify")){
			return mapping.findForward("success1");
		}else{
			return mapping.findForward("failure");
		}
	}
}

