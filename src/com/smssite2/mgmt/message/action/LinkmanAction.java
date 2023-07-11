package com.smssite2.mgmt.message.action;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.smssite2.mgmt.linkman.bean.Linkman;
import com.smssite2.mgmt.linkman.dao.ILinkmanDao;
import com.smssite2.mgmt.linkman.dao.impl.LinkmanDao;
import com.smssite2.mgmt.linkman.form.LinkmanListForm;
import com.smssite2.mgmt.linkman.service.ILinkmanService;
import com.smssite2.mgmt.linkman.service.impl.LinkmanService;

public class LinkmanAction extends Action{	private static Log log = LogFactory.getLog(LinkmanAction.class);
	ILinkmanService service = new LinkmanService();
	ILinkmanDao dao =new LinkmanDao();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		LinkmanListForm manF=(LinkmanListForm)form;
		String groupid=request.getParameter("groupid");
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		try{
			List<Linkman> list=service.findAllLinkman(manF,eid,groupid,staffId,"find");
			Map group=dao.findGroup(staffId,eid);
			request.setAttribute("groups", group);
			request.setAttribute("mans", list);
			request.setAttribute("pagination", manF);
			request.setAttribute("groupid", groupid);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			return mapping.findForward("failuer");
		}
		String message = (String)request.getAttribute("message");
		request.setAttribute("message", message);
		return mapping.findForward("success");
	}
}

