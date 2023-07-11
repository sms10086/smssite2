package com.smssite2.mgmt.message.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.NoteException;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.note.common.LogUtil;
import com.smssite2.mgmt.message.form.MessageListForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class MessageSendAction extends Action{
	IMessageService service=new MessageService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		MessageListForm msgList=(MessageListForm)form;
		String[] messageIds=request.getParameterValues("ids");
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String userId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		HttpSession session =request.getSession();
		String routeType= ((Account)session.getAttribute("account")).getRouteType();
		int messageLength=((Account)request.getSession().getAttribute("account")).getMessageLength();
		
		int accountPRI=((Account)session.getAttribute("account")).getAccountPRI();
		String flag=request.getParameter("flag");
		try{
			String message=service.MessageSend(messageIds,routeType,EId,accountPRI,flag,userId,msgList,session,messageLength);
			request.setAttribute("message", message);
			LogUtil.writeLog("∂Ã–≈…Û∫À", "…Û∫À", message, userId, EId);
			return mapping.findForward("success");
		}catch(NoteException e){
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		}catch(Exception e1){
			request.setAttribute("message", e1.getMessage());
			return mapping.findForward("failure");
		}
		
	}

}

