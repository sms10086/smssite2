package com.smssite2.mgmt.staff.action;

import java.io.PrintWriter;
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
import com.note.bean.Staff;
import com.smssite2.mgmt.repout.service.ReportService;
import com.smssite2.mgmt.staff.bean.MoneyHisBean;
import com.smssite2.mgmt.staff.bean.MosoDay;
import com.smssite2.mgmt.staff.bean.MosoQueue;
import com.smssite2.mgmt.staff.bean.QueueMonBean;
import com.smssite2.mgmt.staff.bean.SendHisBean;
import com.smssite2.mgmt.staff.bean.SmSendBean;
import com.smssite2.mgmt.staff.form.ReportListForm;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class ReportListOutAction extends Action{
	IStaffService service = new StaffService();
	@SuppressWarnings("static-access")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		PrintWriter pw = response.getWriter();
		
		ReportService reportService = new ReportService();
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
				response.setBufferSize(32);
				String topStr  = "下单日期,企业代码,客户名称,数量,单价,金额,制单人,说明,审核人,审核时间,当前状态,业务员,开户人,开户日期";
				response.setContentType("application/octet-stream");
				String filename = "moneyhis.csv";
				filename=new String(filename.getBytes(),"utf-8");
				response.setHeader("Location",filename);
				response.setHeader("Cache-Control", "max-age=10000");
				response.setHeader("Content-Disposition", "attachment; filename=" + filename);
				response.setCharacterEncoding("gb2312");
				reportService.setPw(pw);
				reportService.getReportInfo(topStr, getInfoMoneyhis(list));
				pw.flush();
			}else
			if(type.equals("smsend")){
				List<SmSendBean> list=service.findAllSmSend(repF);
				response.setBufferSize(32);
				String topStr  = "账号,客户名称,发出数量,成功数量,失败数量,未返回报告,移动总数,移动成功,移动失败,移动无报告,联通总数,联通成功,联通失败,联通无报告,电信总数,电信成功,电信失败,电信无报告,当前余额,开户日期,业务员";
				response.setContentType("application/octet-stream");
				String filename = "smsend.csv";
				filename=new String(filename.getBytes(),"utf-8");
				response.setHeader("Location",filename);
				response.setHeader("Cache-Control", "max-age=10000");
				response.setHeader("Content-Disposition", "attachment; filename=" + filename);
				response.setCharacterEncoding("gb2312");
				reportService.setPw(pw);
				reportService.getReportInfo(topStr, getInfoSmsend(list));
				pw.flush();
			}else
			if(type.equals("sendhis")){
				List<SendHisBean> list=service.findAllSendHis(repF);
				response.setBufferSize(32);
				String topStr  = "企业代码,用户账号,发送时间,手机号码,接收人,状态,错误代码,短信内容,子号,署名";
				response.setContentType("application/octet-stream");
				String filename = "sendhis.csv";
				filename=new String(filename.getBytes(),"utf-8");
				response.setHeader("Location",filename);
				response.setHeader("Cache-Control", "max-age=10000");
				response.setHeader("Content-Disposition", "attachment; filename=" + filename);
				response.setCharacterEncoding("gb2312");
				reportService.setPw(pw);
				reportService.getReportInfo(topStr, getInfoSendhis(list));
				pw.flush();
			}else
			if(type.equals("routetype")){
				List<QueueMonBean> list=service.findAllRouteType(repF);
				response.setBufferSize(32);
				String topStr  = "监视项目,数量,移动,联通,电信,说明";
				response.setContentType("application/octet-stream");
				String filename = "routetype.csv";
				filename=new String(filename.getBytes(),"utf-8");
				response.setHeader("Location",filename);
				response.setHeader("Cache-Control", "max-age=10000");
				response.setHeader("Content-Disposition", "attachment; filename=" + filename);
				response.setCharacterEncoding("gb2312");
				reportService.setPw(pw);
				reportService.getReportInfo(topStr, getInfoRoutetype(list));
				pw.flush();
			}else
			if(type.equals("mosoqueue")){
				List<MosoQueue> list=service.findMosoQueue(repF);
				response.setBufferSize(32);
				String topStr  = "通道,号码数量,创建时间,企业代码,客户名称,发送人,短信内容,短信号码,短信署名";
				response.setContentType("application/octet-stream");
				String filename = "mosoqueue.csv";
				filename=new String(filename.getBytes(),"utf-8");
				response.setHeader("Location",filename);
				response.setHeader("Cache-Control", "max-age=10000");
				response.setHeader("Content-Disposition", "attachment; filename=" + filename);
				response.setCharacterEncoding("gb2312");
				reportService.setPw(pw);
				reportService.getReportInfo(topStr, getInfoMosoqueue(list));
				pw.flush();
			}/*else
			if(type.equals("mosoday")){
				List<MosoDay> list=service.findMosoDay(repF);
				request.setAttribute("mosoday",list);
			}*/
		}catch(Exception e){
			
		}
		return  null;
	}
	@SuppressWarnings("unchecked")
	private List getInfoMosoqueue(List<MosoQueue> list) {
		List info = null;
		if (list == null)
			return info;
		info = new ArrayList();
		for (Object o : list) {
			MosoQueue bean = (MosoQueue) o;
			List s = new ArrayList();
			s.add(this.getNullValue(bean.getRouteType()));
			s.add(this.getNullValue(bean.getTotal()));
			s.add(this.getNullValue(bean.getAddTime()));
			s.add(this.getNullValue(bean.getEid()));
			s.add(this.getNullValue(bean.getName()));
			s.add(this.getNullValue(bean.getSender()));
			s.add(this.getNullValue(bean.getContent()));
			s.add(this.getNullValue(bean.getSmsNum()));
			s.add(this.getNullValue(bean.getSmsSign()));
			info.add(s);
		}

		return info;

	}
	@SuppressWarnings("unchecked")
	private List getInfoRoutetype(List<QueueMonBean> list) {
		List info = null;
		if (list == null)
			return info;
		info = new ArrayList();
		for (Object o : list) {
			QueueMonBean bean = (QueueMonBean) o;
			List s = new ArrayList();
			s.add(this.getNullValue(bean.getDay()));
			s.add(this.getNullValue(bean.getTotal()));
			s.add(this.getNullValue(bean.getYdTotal()));
			s.add(this.getNullValue(bean.getLtTotal()));
			s.add(this.getNullValue(bean.getDxTotal()));
			s.add(this.getNullValue(bean.getNote()));
			info.add(s);
		}

		return info;
	}
	@SuppressWarnings("unchecked")
	private List getInfoSendhis(List<SendHisBean> list) {
		List info = null;
		if (list == null)
			return info;
		info = new ArrayList();
		for (Object o : list) {
			SendHisBean bean = (SendHisBean) o;
			List s = new ArrayList();
			s.add(this.getNullValue(bean.getEid()));
			s.add(this.getNullValue(bean.getStaffId()));
			s.add(this.getNullValue(bean.getDay()));
			s.add(this.getNullValue(bean.getPhone()));
			s.add(this.getNullValue(bean.getName()));
			s.add(this.getNullValue(bean.getState()));
			s.add(this.getNullValue(bean.getErr()));
			s.add(this.getNullValue(bean.getContent()));
			s.add(this.getNullValue(bean.getSmsNum()));
			s.add(this.getNullValue(bean.getSmsSign()));
			info.add(s);
		}

		return info;
	}
	@SuppressWarnings("unchecked")
	private List getInfoSmsend(List<SmSendBean> list) {
		List info = null;
		if (list == null)
			return info;
		info = new ArrayList();
		for (Object o : list) {
			SmSendBean bean = (SmSendBean) o;
			List s = new ArrayList();
			s.add(this.getNullValue(bean.getEid()));
			s.add(this.getNullValue(bean.getName()));
			s.add(this.getNullValue(bean.getTotal()));
			s.add(this.getNullValue(bean.getTotalOk()));
			s.add(this.getNullValue(bean.getTotalFail()));
			s.add(this.getNullValue(bean.getTotalNull()));
			s.add(this.getNullValue(bean.getYdTotal()));
			s.add(this.getNullValue(bean.getYdOk()));
			s.add(this.getNullValue(bean.getYdFail()));
			s.add(this.getNullValue(bean.getYdNull()));
			s.add(this.getNullValue(bean.getLtTotal()));
			s.add(this.getNullValue(bean.getLtOk()));
			s.add(this.getNullValue(bean.getLtFail()));
			s.add(this.getNullValue(bean.getLtNull()));
			s.add(this.getNullValue(bean.getDxTotal()));
			s.add(this.getNullValue(bean.getDxOk()));
			s.add(this.getNullValue(bean.getDxFail()));
			s.add(this.getNullValue(bean.getDxNull()));
			s.add(this.getNullValue(bean.getBalance()));
			s.add(this.getNullValue(bean.getDay()));
			s.add(this.getNullValue(bean.getStaff()));
			info.add(s);
		}

		return info;
	}
	@SuppressWarnings("unchecked")
	private List getInfoMoneyhis(List<MoneyHisBean> list) {
		List info = null;
		if (list == null)
			return info;
		info = new ArrayList();
		for (Object o : list) {
			MoneyHisBean bean = (MoneyHisBean) o;
			List s = new ArrayList();
			s.add(this.getNullValue(bean.getAddTime()));
			s.add(this.getNullValue(bean.getEid()));
			s.add(this.getNullValue(bean.getSmsCompanyname()));
			s.add(this.getNullValue(bean.getAddNum()));
			s.add(this.getNullValue(bean.getPrice()));
			s.add(this.getNullValue(bean.getMoneyCount()));
			s.add(this.getNullValue(bean.getStaffId()));
			s.add(this.getNullValue(bean.getAddMemo()));
			s.add(this.getNullValue(bean.getDirector()));
			s.add(this.getNullValue(bean.getCheckTime()));
			s.add(this.getNullValue(bean.getState()));
			s.add(this.getNullValue(bean.getRegSales()));
			s.add(this.getNullValue(bean.getRegDirector()));
			s.add(this.getNullValue(bean.getRegDate()));
			info.add(s);
		}

		return info;
	}
	private int[] getStatus(List list) {
		int[] status = null;
		if (list != null) {
			status = new int[list.size()];
			for (int i = 0; i < status.length; i++) {
				status[i] = -1;
			}
		}
		return status;
	}

	private Object getNullValue(Object o) {
		return o != null ? o : "";
	}
	
}
