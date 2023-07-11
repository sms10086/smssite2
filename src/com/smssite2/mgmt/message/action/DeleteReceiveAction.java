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

public class DeleteReceiveAction extends Action{	private static Log log = LogFactory.getLog(DeleteReceiveAction.class);
	IMessageService service=new MessageService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		
		String[] ids=request.getParameterValues("ids");
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String routeType= ((Account)request.getSession().getAttribute("account")).getRouteType();
		try{
			String str=service.deleteReceive(ids,routeType,EId);
			request.setAttribute("message", str);
			LogUtil.writeLog("短信接收历史记录", "删除", str, ((Staff)request.getSession().getAttribute("staff")).getUserId(), EId);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			request.setAttribute("messagt", e.getMessage());
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}

}


