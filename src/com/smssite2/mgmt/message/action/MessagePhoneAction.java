package com.smssite2.mgmt.message.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.NoteException;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.note.common.DbBean;
import com.smssite2.mgmt.message.bean.MessagePhoneBean;
import com.smssite2.mgmt.message.form.PhoneListForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class MessagePhoneAction extends Action{
	IMessageService service=new MessageService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		PhoneListForm phoneList=(PhoneListForm)form;
		String messageId=request.getParameter("messageId");
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String routeType= ((Account)request.getSession().getAttribute("account")).getRouteType();
		String content=request.getParameter("content");
		String flag=request.getParameter("flag");
		if(content==null||content.trim().length()==0){
content=DbBean.getString("select content from task where ID='"+messageId+"' ", null);
		}
		try{
			List<MessagePhoneBean> list=service.findAllMessagePhone(phoneList,EId,routeType,content,messageId,flag);
			if(request.getAttribute("message")==null){
				if(list==null||list.size()==0){
					if(flag!=null&&flag.equals("1"))throw new NoteException("该批短信不存在或正在发送");
					else throw new NoteException("该批短信不存在或已审核!");
				}
			}
			request.setAttribute("phones", list);
		}
		catch(Exception e){
			request.setAttribute("message", e.getMessage());
		}
		request.setAttribute("messageId", messageId);
		request.setAttribute("content", content);
		request.setAttribute("flag", flag);
		request.setAttribute("pagination", phoneList);
		return mapping.findForward("success");
	}
}

