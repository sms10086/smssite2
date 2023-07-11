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
import com.smssite2.mgmt.message.bean.Receive;
import com.smssite2.mgmt.message.form.RiceiveListForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class ReceiveListAction extends Action{
	IMessageService service=new MessageService();
	@SuppressWarnings("static-access")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		RiceiveListForm riceF=(RiceiveListForm)form;
		String routeType=((Account)request.getSession().getAttribute("account")).getRouteType();
		String EId=((Account)request.getSession().getAttribute("account")).getEId();
		String staffid=((Staff)request.getSession().getAttribute("staff")).getUserId();
	
		Calendar cale = Calendar.getInstance();
		Calendar cals = Calendar.getInstance();
		cals.add(cals.MONTH, -1);
		SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd");
		String end=smd.format(cale.getTime());
		String start=smd.format(cals.getTime());
		if(riceF.getStart()==null)riceF.setStart(start);
		if(riceF.getEnd()==null)riceF.setEnd(end);
	
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("120")<0){
			request.setAttribute("message", "用户"+staffid+"没有[短信接收历史记录]的操作权限！");
			return mapping.findForward("err");
		}
		String[] strs=credit.split("\\|");
		String privilege="";
		if(credit.trim().toLowerCase().equals("admin")){
			privilege="120:DO";
		}else{
			for(int i=1;i<strs.length;i++){
				if(strs[i].indexOf("120")>=0)privilege=strs[i];
			}
		}
		request.setAttribute("privilege", privilege);
		
		try{
			List<Receive> list=service.findAllRecive(riceF,EId,routeType,staffid);
			request.setAttribute("rcveives", list);
			request.setAttribute("pagination", riceF);
		}catch(Exception e){
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}
}


