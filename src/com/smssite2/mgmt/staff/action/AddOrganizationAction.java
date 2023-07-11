package com.smssite2.mgmt.staff.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.note.common.LogUtil;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class AddOrganizationAction extends Action{
	IStaffService service = new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String orgName=request.getParameter("orgName");
		String orgMemo=request.getParameter("orgMemo");
		String orgId=request.getParameter("orgId");
		
		try{
			service.addOrg(EId,orgName,orgMemo,orgId);
			request.setAttribute("message", "添加或修改部门"+orgName+"成功");
			if(orgId==null){
				LogUtil.writeLog("部门设置","添加","成功添加部门:"+orgName,((Staff)request.getSession().getAttribute("staff")).getUserId(),EId);
			}else{
				LogUtil.writeLog("部门设置","修改","成功修改部门:"+orgName,((Staff)request.getSession().getAttribute("staff")).getUserId(),EId);
			}
			}catch(Exception e){
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}
}

