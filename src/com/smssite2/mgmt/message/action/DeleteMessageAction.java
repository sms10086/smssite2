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
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class DeleteMessageAction extends Action{	private static Log log = LogFactory.getLog(DeleteMessageAction.class);
	IMessageService service=new MessageService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		
		String[] ids=request.getParameterValues("ids");
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String routeType= ((Account)request.getSession().getAttribute("account")).getRouteType();
		String messageId=request.getParameter("messageId");
		String content =request.getParameter("content");
		String flag=request.getParameter("flag");
		
		try{
			String message=service.deleteMessageById(ids,routeType,flag,EId,messageId);
			request.setAttribute("messageId",messageId);
			request.setAttribute("content", content);
			request.setAttribute("flag", flag);
			request.setAttribute("message", message);
			if(flag.equals("1")){
				LogUtil.writeLog("定时短信","删除",message,((Staff)request.getSession().getAttribute("staff")).getUserId(),EId);
			}else{
				LogUtil.writeLog("短信审核列表","删除",message,((Staff)request.getSession().getAttribute("staff")).getUserId(),EId);
			}
		}catch(Exception e){
			log.error(e.getMessage(), e);
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}

}
