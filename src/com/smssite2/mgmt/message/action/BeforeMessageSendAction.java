package com.smssite2.mgmt.message.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.common.DbBean;
import com.note.common.UUIDUtil;
import com.smssite2.mgmt.message.bean.PhoneBean;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class BeforeMessageSendAction extends Action {	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		try{
			HttpSession session=request.getSession();
			if(session.getAttribute("taskID")==null){
				session.setAttribute("taskID", UUIDUtil.generate());
			}
			 
		}catch(Exception e){
			
		}
		return mapping.findForward("success");
	}
}
