package com.smssite2.mgmt.staff.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.note.bean.Team;
import com.smssite2.mgmt.staff.form.OrgListForm;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class OrgListAction extends Action{
	IStaffService service = new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		OrgListForm orgList=(OrgListForm)form;
		String	EId= ((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")){
			request.setAttribute("message", "用户"+staffId+"没有[部门设置]的操作权限！");
			return mapping.findForward("err");
		}
		
		List<Team> orgs =new ArrayList<Team>();
		try{
			orgs=service.findOrgsByEId(orgList,EId);
			request.setAttribute("orgs", orgs);
			request.setAttribute("pagination",orgList);
		}catch(Exception e){
			e.printStackTrace();
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}
}
