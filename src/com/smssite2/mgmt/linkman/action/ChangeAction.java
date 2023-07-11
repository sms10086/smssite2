package com.smssite2.mgmt.linkman.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.note.common.LogUtil;
import com.smssite2.mgmt.linkman.service.ILinkmanService;
import com.smssite2.mgmt.linkman.service.impl.LinkmanService;

public class ChangeAction extends Action{
	ILinkmanService service =new LinkmanService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String linkids=request.getParameter("linkids");
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		String status=request.getParameter("status");
		String phone=request.getParameter("phone1");
		String groupid=request.getParameter("groupid");
		String[] phs=phone.split(";");
		String[] ids= linkids.split(";");
		String message="";
		try{
			int a=service.change(ids,EId,status,phs);
			if(status.equals("0"))message="成功将"+a+"个号码改为拒收状态";
			else message="成功将"+a+"个号码改为接收状态";
			request.setAttribute("groupid", groupid);
			request.setAttribute("message", message);
			LogUtil.writeLog("通讯录","修改状态","改变 status="+status,staffId,EId);
		}catch(Exception e){
			request.setAttribute("groupid", groupid);
			request.setAttribute("message", e.getMessage());
		}
		return mapping.findForward("success");
	}
}
