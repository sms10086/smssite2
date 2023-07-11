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
import com.smssite2.mgmt.message.bean.Receive;
import com.smssite2.mgmt.message.form.RiceiveListForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class NewReciveAction extends Action{	IMessageService service=new MessageService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		RiceiveListForm riceF=(RiceiveListForm)form;
		String routeType=((Account)request.getSession().getAttribute("account")).getRouteType();
		String EId=((Account)request.getSession().getAttribute("account")).getEId();
		String staffid=((Staff)request.getSession().getAttribute("staff")).getUserId();
		
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("121")<0){
			request.setAttribute("message", "�û�"+staffid+"û��[���յ����ֻ�����]�Ĳ���Ȩ�ޣ�");
			return mapping.findForward("err");
		}
		String[] strs=credit.split("\\|");
		String privilege="";
		if(credit.trim().toLowerCase().equals("admin")){
			privilege="121:DO";
		}else{
			for(int i=1;i<strs.length;i++){
				if(strs[i].indexOf("121")>=0)privilege=strs[i];
			}
		}
		request.setAttribute("privilege", privilege);
		
		try{
			List<Receive> list=service.findNewRecive(riceF,EId,routeType,staffid);
			request.setAttribute("rcveives", list);
			request.setAttribute("pagination", riceF);
		}catch(Exception e){
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}
}


