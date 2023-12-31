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
import com.note.bean.Staff;
import com.note.common.LogUtil;
import com.smssite2.mgmt.message.form.MessageListForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class DeleteMessageByMessageIdAction extends Action{	private static Log log = LogFactory.getLog(DeleteMessageByMessageIdAction.class);
	IMessageService service=new MessageService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		MessageListForm msgList=(MessageListForm)form;
		String[] ids=request.getParameterValues("ids");
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		String routeType= ((Account)request.getSession().getAttribute("account")).getRouteType();
		String flag=request.getParameter("flag");
		try{
			String message=service.deleteMessageByMessageId(ids,routeType,flag,EId,msgList,staffId);
			request.setAttribute("message", message);
			LogUtil.writeLog("��������б�","ɾ��",message,((Staff)request.getSession().getAttribute("staff")).getUserId(),EId);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}

}

