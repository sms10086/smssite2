package com.smssite2.mgmt.report.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.smssite2.mgmt.report.form.ReportBaseForm;
import com.smssite2.mgmt.report.service.IReportService;
import com.smssite2.mgmt.report.service.impl.ReportService;

public class SmsSendAction extends Action {	IReportService service = new ReportService();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ReportBaseForm form1=(ReportBaseForm)form;
		if(request.getSession().getAttribute("staff")==null)
		{
			request.setAttribute("message", "未知异常,请重新登陆或联系管理员!");
		}
		String EId =((Staff)request.getSession().getAttribute("staff")).getEId();
		String routeType=((Account)request.getSession().getAttribute("account")).getRouteType();
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("A")<0){
			request.setAttribute("message", "用户"+((Staff)request.getSession().getAttribute("staff")).getUserId()+"没有[短信统计报表汇集]的操作权限！");
			return mapping.findForward("err");
		}
		form1.setEid(EId);
		List list=new ArrayList();
		try{
			Calendar cale = Calendar.getInstance();
			SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd");
			String time=smd.format(cale.getTime());
			if(form1.getStartDate()==null||form1.getStartDate().trim().length()==0)form1.setStartDate(time);
			if(form1.getEndDate()==null||form1.getEndDate().trim().length()==0)form1.setEndDate(time);
			String type=form1.getType();
			
			request.setAttribute("startDate", form1.getStartDate());
			request.setAttribute("endDate", form1.getEndDate());
			request.setAttribute("indicators", form1.getIndicators());
			request.setAttribute("userName", form1.getUserName());
			request.setAttribute("receiver", form1.getReceiver());
			if(type==null||type.equals("smsend")){
				list = service.findSmsSendWithPagination(form1,routeType);
				request.setAttribute("type", type);
				request.setAttribute("p", form1);
				request.setAttribute("pgssmsend", list);
				return mapping.findForward("success");
			}else if(type.equals("evesend")){
				list = service.findEveSendWithPagination(form1,routeType);
				request.setAttribute("type", type);
				request.setAttribute("p", form1);
				request.setAttribute("pgsevesend", list);
				return mapping.findForward("success");
			}else if(type.equals("paragraph")){
				list = service.findParagraphWithPagination(form1,routeType);
				request.setAttribute("type", type);
				request.setAttribute("p", form1);
				request.setAttribute("pgsparagraph", list);
				return mapping.findForward("success");
			}/*else if(type.equals("groupsend")){
				list = service.findGroupsendWithPagination(form1,routeType);
				request.setAttribute("p", form1);
				request.setAttribute("pgsgroupsend", list);
			}else if(type.equals("classificationsend")){
				list = service.findClassificationSendWithPagination(form1,routeType);
				request.setAttribute("p", form1);
				request.setAttribute("pgsclassificationsend", list);
			}*/else if(type.equals("smsreceive")){
				list = service.findSmsReceiveWithPagination(form1,routeType);
				request.setAttribute("type", type);
				request.setAttribute("p", form1);
				request.setAttribute("pgssmsreceive", list);
				return mapping.findForward("success");
			}else{
				return mapping.findForward("failure");
			}
		}catch(Exception e){
			
			return mapping.findForward("failure");
		}
		
	}
}
