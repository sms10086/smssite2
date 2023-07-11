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

public class DeleteStaffAction extends Action{
	
	IStaffService service = new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String[] ids=request.getParameterValues("ids");
		String EId= ((Staff)request.getSession().getAttribute("staff")).getEId();
		int a=0;
		if(ids!=null&&ids.length!=0){
			if(ids[0].equals("-1"))a=ids.length-1;
			else a=ids.length;
		}
		try{
			service.deleteStaff(ids,EId);
		}catch(Exception e){
			e.printStackTrace();
			return mapping.findForward("failure");
		}
		request.setAttribute("message", "删除"+a+"个用户成功");
		LogUtil.writeLog("用户管理","删除","成功删除"+a+"个用户！",((Staff)request.getSession().getAttribute("staff")).getUserId(),EId);
		return mapping.findForward("success");
	}
}
