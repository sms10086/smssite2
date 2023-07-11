package com.smssite2.mgmt.message.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Account;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class ChangeTurnAction extends Action{	private static Log log = LogFactory.getLog(ChangeTurnAction.class);
	IMessageService service=new MessageService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String routeType=((Account)request.getSession().getAttribute("account")).getRouteType();
		String EId=((Account)request.getSession().getAttribute("account")).getEId();
		String id = request.getParameter("ids");
		String flag=request.getParameter("flag");
		
		try{
			service.deletephones_load(request.getSession().getId());
			String  content=service.findAllHis(EId,routeType,id,flag);
			
			request.setAttribute("content", content);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		}
	
		return mapping.findForward("success");
	}
}

