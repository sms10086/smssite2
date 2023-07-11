package com.smssite2.mgmt.message.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.smssite2.mgmt.message.bean.SendHistory;
import com.smssite2.mgmt.message.form.SendHistoryForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class ListSendHistoryAction extends Action{
	IMessageService service=new MessageService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		SendHistoryForm hisList=(SendHistoryForm)form;
		String routeType=((Account)request.getSession().getAttribute("account")).getRouteType();
		String EId=((Account)request.getSession().getAttribute("account")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		 
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("110")<0){
			request.setAttribute("message", "用有"+staffId+"没有[短信发送历史记录]的操作权限！");
			return mapping.findForward("err");
		}
		String[] strs=credit.split("\\|");
		String privilege="";
		if(credit.trim().toLowerCase().equals("admin")){
			privilege="110:RDO";
		}else{
			for(int i=1;i<strs.length;i++){
				if(strs[i].indexOf("110")>=0)privilege=strs[i];
			}
		}
		request.setAttribute("privilege", privilege);
		
		Calendar cale = Calendar.getInstance();
		SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd");
		String time=smd.format(cale.getTime());
		if(hisList.getStateTime()==null||hisList.getStateTime().trim().length()==0)hisList.setStateTime(time);
		if(hisList.getEndTime()==null||hisList.getEndTime().trim().length()==0)hisList.setEndTime(time);
		try{
			List<SendHistory> list=service.findAllSendHis(hisList,EId,routeType,staffId);
			request.setAttribute("sendhistorys", list);
			request.setAttribute("pagination", hisList);
			request.setAttribute("status",hisList.getStatus());
		}catch(Exception e){
			return mapping.findForward("failure");
		}
	
		return mapping.findForward("success");
	}
}

