package com.smssite2.mgmt.message.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.note.common.DbBean;
import com.smssite2.mgmt.message.bean.PhoneBean;
import com.smssite2.mgmt.message.form.LoadPhoneForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class DeletePhoneAction extends Action{	private static Log log = LogFactory.getLog(DeletePhoneAction.class);
	IMessageService mservice=new MessageService();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		LoadPhoneForm phoneForm=(LoadPhoneForm)form;
		
		String content =request.getParameter("content");
		String mesType =request.getParameter("mesType");
		String isSchedule =request.getParameter("isSchedule");
		String chkusevar=request.getParameter("Chkusevar");
		String hour=request.getParameter("selhour");
		String minute=request.getParameter("selminute");
		String year=request.getParameter("selyear");
		String month=request.getParameter("selmonth");
		String day=request.getParameter("selday");
		String needSendTime=null;
		
		if(isSchedule!=null&&isSchedule.equals("1")){
			needSendTime=year+"/"+month+"/"+day+" "+hour+":"+minute;
		}
		HttpSession session=request.getSession();
		
		String[] ids=request.getParameterValues("smsBox");
		String taskID=(String)session.getAttribute("taskID");
		try{
				if(ids!=null||ids.length!=0){
					for(int i=0;i<ids.length;i++){
						if(ids[i].equals("-1"))continue;
						DbBean.executeUpdate("delete from phones  where id='"+ids[i]+"' and sessionid='"+session.getId()+"' and taskID='"+taskID+"' ", null);
					}
				}
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		
		List<PhoneBean> pageList=mservice.findPhones_load( session.getId(), ((Staff)session.getAttribute("staff")).getEId(), ((Staff)session.getAttribute("staff")).getUserId(),phoneForm,taskID);
		request.getSession().setAttribute("phones", pageList);
		request.getSession().setAttribute("pagination", phoneForm);
		
		request.setAttribute("content", content);
		request.setAttribute("chkusevar", chkusevar);
		request.setAttribute("needSendTime",needSendTime);
		request.setAttribute("isSchedule", isSchedule);
		request.setAttribute("selyear", year);
		request.setAttribute("selmonth", month);
		request.setAttribute("selday", day);
		request.setAttribute("selhour", hour);
		request.setAttribute("selminute", minute);
		request.setAttribute("mesType", mesType);
		return mapping.findForward("success");
	}
}
