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

public class DeleteOrganizationAction extends Action{
	IStaffService service=new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] ids=request.getParameterValues("ids");
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		int a=0;
		if(ids!=null&&ids.length!=0){
			if(ids[0].equals("-1"))a=ids.length-1;
			else a=ids.length;
		}
		try{
			service.deleteOrg(ids,EId);
			request.setAttribute("message","成功删除"+a+"个部门！");
			LogUtil.writeLog("用户管理","删除","成功删除"+a+"个部门！",((Staff)request.getSession().getAttribute("staff")).getUserId(),EId);
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}
}

