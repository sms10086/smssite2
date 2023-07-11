package com.smssite2.mgmt.basicsetting.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.note.common.LogUtil;
import com.smssite2.mgmt.basicsetting.service.IBasicSettingService;
import com.smssite2.mgmt.basicsetting.service.impl.BasicSettingService;

public class DeleteFeastAction extends Action {
	IBasicSettingService service = new BasicSettingService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String[] ids=request.getParameterValues("ids");
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getSysUserId();
		try{
			String message="";
			int a =service.delete(ids,eid,"feast","feastid");
			if(a>0){
				message=a+"个节日已成功删除！";
				LogUtil.writeLog("节日设置","删除",message,staffId,eid);
			}else{
				message="删除节日失败！";
			}
			request.setAttribute("message", message);
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
		}
		return mapping.findForward("success");
	}

}
