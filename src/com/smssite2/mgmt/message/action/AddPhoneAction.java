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
import com.note.common.TimeUtil;
import com.smssite2.mgmt.message.bean.PhoneBean;
import com.smssite2.mgmt.message.form.LoadPhoneForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.LoadPhoneService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class AddPhoneAction extends Action{	private static Log log = LogFactory.getLog(AddPhoneAction.class);
	LoadPhoneService  service=new LoadPhoneService();
	IMessageService mservice=new MessageService();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		LoadPhoneForm phoneForm=(LoadPhoneForm)form;
		String phone=request.getParameter("txtphone");
		String content =request.getParameter("content");
		String mesType =request.getParameter("mesType");
		String mesType2=request.getParameter("mesType2");
		String isSchedule =request.getParameter("isSchedule");
		String chkusevar=request.getParameter("Chkusevar");
		String hour=request.getParameter("selhour");
		String minute=request.getParameter("selminute");
		String year=request.getParameter("selyear");
		String month=request.getParameter("selmonth");
		String day=request.getParameter("selday");
		String needSendTime=null;
		
		if(isSchedule!=null&&isSchedule.equals("1")){
			needSendTime=year+"-"+month+"-"+day+" "+hour+":"+minute;
		}
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
		request.setAttribute("mesType2", mesType2);
		try{
		HttpSession session=request.getSession();
		String eid=((Staff)session.getAttribute("staff")).getEId();
		String userid=((Staff)session.getAttribute("staff")).getUserId();
		String sessionId=session.getId();
		String taskID =(String) session.getAttribute("taskID");
		int phoneType=service.IsPhone(phone);
		if(phoneType==-1) {
			request.setAttribute("message", "手机号码不正确！");
			return mapping.findForward("success");
		}
		PhoneBean pb=new PhoneBean();
		pb.setPhone(phone);
		pb.setPhoneType(phoneType);
		DbBean.executeUpdate("insert into phones(phone,name,content,phonetype,sessionid,eid,staffID,bornDate,taskID) values(?,?,?,?,?,?,?,?,?)",
				new Object[]{phone,"","",phoneType,sessionId,eid,userid,TimeUtil.now(),taskID});
		List<PhoneBean> pageList=mservice.findPhones_load( sessionId, eid, userid,phoneForm,taskID);
		request.getSession().setAttribute("phones", pageList);
		request.getSession().setAttribute("pagination", phoneForm);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return mapping.findForward("success");
	}
}
