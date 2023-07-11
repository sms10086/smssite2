package com.smssite2.mgmt.linkman.action;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.smssite2.mgmt.linkman.dao.ILinkmanDao;
import com.smssite2.mgmt.linkman.dao.impl.LinkmanDao;

public class BeforeLoadAction extends Action{	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		String groupId=request.getParameter("groupId");
		ILinkmanDao dao =new LinkmanDao();
		try{
			Map group=dao.findGroup(staffId,eid);
			request.setAttribute("groupId", groupId);
			request.setAttribute("groups", group);
			request.setAttribute("flag", "add");
		}catch(Exception e){
		}
		return mapping.findForward("success");
	}
}
