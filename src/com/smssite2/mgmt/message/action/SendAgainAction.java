package com.smssite2.mgmt.message.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.note.common.LogUtil;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class SendAgainAction extends Action{
	IMessageService service= new MessageService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String[] ids=request.getParameterValues("ids");
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String routeType= ((Account)request.getSession().getAttribute("account")).getRouteType();
		int num= ((Account)request.getSession().getAttribute("account")).getMessageLength();
		try{
			int l=service.sendMessageAgain(ids,EId,routeType,num);
			request.setAttribute("message", "重新发送了"+l+"批短信,请审核!");
			LogUtil.writeLog("短信中心", "重发", "重新发送了"+l+"批短信", ((Staff)request.getSession().getAttribute("staff")).getUserId(), EId);
			return mapping.findForward("success");
		}catch(Exception e){
			return mapping.findForward("failure");
		}
	}
}


