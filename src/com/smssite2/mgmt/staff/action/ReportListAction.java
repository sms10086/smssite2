package com.smssite2.mgmt.staff.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.smssite2.mgmt.staff.bean.MoneyHisBean;
import com.smssite2.mgmt.staff.bean.MosoDay;
import com.smssite2.mgmt.staff.bean.MosoQueue;
import com.smssite2.mgmt.staff.bean.QueueMonBean;
import com.smssite2.mgmt.staff.bean.SendHisBean;
import com.smssite2.mgmt.staff.bean.SmSendBean;
import com.smssite2.mgmt.staff.form.ReportListForm;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class ReportListAction extends Action{
	IStaffService service = new StaffService();
	@SuppressWarnings("static-access")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("Ac")<0){
			request.setAttribute("message", "用户"+staffId+"没有[平台管理报表汇集]的操作权限！");
			return mapping.findForward("err");
		}
		
		ReportListForm repF=(ReportListForm)form;
		String type=request.getParameter("type");
		
		Calendar cale = Calendar.getInstance();
		Calendar cals = Calendar.getInstance();
		SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd");
		String end=smd.format(cale.getTime());
		String start=smd.format(cals.getTime());

		if(repF.getStart()==null||repF.getStart().trim().equals(""))repF.setStart(start);
		if(repF.getEnd()==null||repF.getEnd().trim().equals(""))repF.setEnd(end);
		
		try{
			if(type==null||type.equals("moneyhis")){
				List<MoneyHisBean> list=service.findAllMoneyHis(repF);
				request.setAttribute("moneyhis", list);
			}else
			if(type.equals("smsend")){
				List<SmSendBean> list=service.findAllSmSend(repF);
				request.setAttribute("smsends", list);
			}else
			if(type.equals("sendhis")){
				List<SendHisBean> list=service.findAllSendHis(repF);
				request.setAttribute("sendhis", list);
				request.setAttribute("stuate", repF.getStuate());
			}else
			if(type.equals("routetype")){
				List<QueueMonBean> list=service.findAllRouteType(repF);
				request.setAttribute("routeType", list);
			}else
			if(type.equals("mosoqueue")){
				List<MosoQueue> list=service.findMosoQueue(repF);
				request.setAttribute("mosoqueue", list);
			}else
			if(type.equals("mosoday")){
				List<MosoDay> list=service.findMosoDay(repF);
				request.setAttribute("mosoday",list);
			}
		}catch(Exception e){
			e.printStackTrace();
			return mapping.findForward("success");
		}
		request.setAttribute("p", repF);
		request.setAttribute("type", type);
		return  mapping.findForward("success");
	}
}
