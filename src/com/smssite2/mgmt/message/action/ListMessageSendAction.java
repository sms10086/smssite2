package com.smssite2.mgmt.message.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.smssite2.mgmt.message.bean.MessageSendBean;
import com.smssite2.mgmt.message.form.MessageListForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class ListMessageSendAction extends Action{	IMessageService service=new MessageService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		MessageListForm msgList=(MessageListForm)form;
		String routeType=((Account)request.getSession().getAttribute("account")).getRouteType();
		String EId=((Account)request.getSession().getAttribute("account")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
	
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("112")<0){
			request.setAttribute("message", "用户"+staffId+"没有[短信发送队列查询]的操作权限！");
			return mapping.findForward("err");
		}
		String[] strs=credit.split("\\|");
		String privilege="";
		if(credit.trim().toLowerCase().equals("admin")){
			privilege="112:DO";
		}else{
			for(int i=1;i<strs.length;i++){
				if(strs[i].indexOf("112")>=0)privilege=strs[i];
			}
		}
		request.setAttribute("privilege", privilege);
		
		try{
			List<MessageSendBean> list=service.findAllMessageSend(msgList,EId,routeType,staffId);
			request.setAttribute("messages", list);
			request.setAttribute("pagination", msgList);
		}catch(Exception e){
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}
}

