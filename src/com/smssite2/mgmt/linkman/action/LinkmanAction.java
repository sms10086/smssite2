package com.smssite2.mgmt.linkman.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;

public class LinkmanAction extends Action{
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("210")<0){
			request.setAttribute("message", "�û�"+staffId+"û��[ͨѶ¼]�Ĳ���Ȩ�ޣ�");
			return mapping.findForward("err");
		}
		return mapping.findForward("success");
	}
}
