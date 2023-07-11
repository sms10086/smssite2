package com.smssite2.mgmt.linkman.action;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.smssite2.mgmt.linkman.bean.Linkman;
import com.note.bean.Staff;
import com.smssite2.mgmt.linkman.dao.ILinkmanDao;
import com.smssite2.mgmt.linkman.dao.impl.LinkmanDao;
import com.smssite2.mgmt.linkman.service.ILinkmanService;
import com.smssite2.mgmt.linkman.service.impl.LinkmanService;

public class BeforeModifyLinkmanAction extends Action{	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		String linkid=request.getParameter("id");
		ILinkmanDao dao =new LinkmanDao();
		ILinkmanService service =new LinkmanService();
		try{
			Linkman man =service.findLinkman(eid,linkid);
			Map group=dao.findGroup(staffId,eid);
			request.setAttribute("groups", group);
			request.setAttribute("flag", "add");
			request.setAttribute("linkman", man);
			request.setAttribute("linkid", linkid);
			request.setAttribute("groupid", man.getGroupId());
			request.setAttribute("sex", man.getSex());
		}catch(Exception e){
			request.setAttribute("message",e.getMessage());
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}
}
