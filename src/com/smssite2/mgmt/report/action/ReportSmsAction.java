package com.smssite2.mgmt.report.action;

import java.util.ArrayList;
import java.util.List;
import com.note.NoteException;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.smssite2.mgmt.report.bean.EveSendBean;
import com.smssite2.mgmt.report.bean.ExcelBasicAction;
import com.smssite2.mgmt.report.bean.ParagraphBean;
import com.smssite2.mgmt.report.bean.SmsReceiveBean;
import com.smssite2.mgmt.report.bean.SmsSendBean;
import com.smssite2.mgmt.report.form.ReportBaseForm;
import com.smssite2.mgmt.report.service.IReportService;
import com.smssite2.mgmt.report.service.impl.ReportService;

public class ReportSmsAction extends ExcelBasicAction {	IReportService service = new ReportService();
	 
	 
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<String> getColumnNames() {
		 ReportBaseForm form1=(ReportBaseForm)form;
		 String type=form1.getType();
		 ArrayList columnNames=new ArrayList();
		 String topStr=null;
		 if(type==null||type.equals("smsend"))
			 topStr  = "发送人,发送总数,成功总数,失败总数,移动总数,移动成功,移动失败,联通总数,联通成功,联通失败,电信总数,电信成功,电信失败";
		 else if(type.equals("evesend"))
			 topStr  = "日期,发送总数,成功总数,失败总数,移动总数,移动成功,移动失败,联通总数,联通成功,联通失败,电信总数,电信成功,电信失败";
		 else if(type.equals("paragraph"))
			 topStr  = "号码段,发送总数,成功总数,失败总数";
		 else if(type.equals("smsreceive"))
			 topStr  = "接收人,接收时间,成功总数,移动成功,联通成功,电信成功";
		 String[] names=topStr.split(",");
		 for(String str:names){
			 columnNames.add(str);
		 }
		return columnNames;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDatas() {
		 ReportBaseForm form1=(ReportBaseForm)form;
		 String type=form1.getType();
		ArrayList<Object[]> dates=new ArrayList<Object[]>();
		List list=null;
		form1.setPageIndex(0);
		form1.setPageSize(Integer.MAX_VALUE);
		String routeType=((Account)session.getAttribute("account")).getRouteType();
		String EId =((Staff)session.getAttribute("staff")).getEId();form1.setEid(EId);
		if(type==null||type.equals("smsend")){
			try {
				list = service.findSmsSendWithPagination(form1,routeType);
				System.out.println(list.size());
			} catch (NoteException e) {
				e.printStackTrace();
			}
				dates= (ArrayList<Object[]>) getInfoSmsSend(list);
				System.out.println(dates.size());
		}else if(type.equals("evesend")){
			try {
				list = service.findEveSendWithPagination(form1,routeType);
			} catch (NoteException e) {
				 
				e.printStackTrace();
			}
			dates= (ArrayList<Object[]>) getInfoEveSend(list);
		}else if(type.equals("paragraph")){
			try {
				list = service.findParagraphWithPagination(form1,routeType);
			} catch (NoteException e) {
				 
				e.printStackTrace();
			}
			dates= (ArrayList<Object[]>) getInfoParagraph(list);
		}else if(type.equals("smsreceive")){
			try {
				list = service.findSmsReceiveWithPagination(form1,routeType);
			} catch (NoteException e) {
			 
				e.printStackTrace();
			}
			dates= (ArrayList<Object[]>) getInfoSmsReceive(list);
		}
		return dates;
	}

	@Override
	public String getFileName() {
		 ReportBaseForm form1=(ReportBaseForm)form;
		 String type=form1.getType();
		String filename="";
		 if(type==null||type.equals("smsend"))
			 filename  = "smsend.xls";
		 else if(type.equals("evesend"))
			 filename = "evesend.xls";
		 else if(type.equals("paragraph"))
			 filename = "paragraph.xls";
		 else if(type.equals("smsreceive"))
			 filename = "smsreceive.xls";
		return filename;
	}

	@Override
	public String getTitle() {
		 ReportBaseForm form1=(ReportBaseForm)form;
		 String type=form1.getType();
		String title="";
		 if(type==null||type.equals("smsend"))
			 title  = "短信发送统计报表";
		 else if(type.equals("evesend"))
			 title = "每日发送流量统计";
		 else if(type.equals("paragraph"))
			 title = "号码段发送统计";
		 else if(type.equals("smsreceive"))
			 title = "短信接收统计";
		return title;
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getInfoSmsSend(List list)  {
		List info = null;
		if (list == null)
			return info;
		info = new ArrayList();
		for (Object o : list) {
			SmsSendBean bean = (SmsSendBean) o;
			List s = new ArrayList();
			s.add(this.getNullValue(bean.getStaffId()));
			s.add(this.getNullValue(bean.getTotal()));
			s.add(this.getNullValue(bean.getTotalok()+bean.getTotalnull()));
			s.add(this.getNullValue(bean.getTotalfail()));
			s.add(this.getNullValue(bean.getTotalmobile()));
			s.add(this.getNullValue(bean.getSucTotalMobile()+bean.getNulltotalmobile()));
			s.add(this.getNullValue(bean.getFaiTotalMobile()));
			s.add(this.getNullValue(bean.getTotalunicom()));
			s.add(this.getNullValue(bean.getSucTotalUnicom()+bean.getNulltotalUnicom()));
			s.add(this.getNullValue(bean.getFaiTotalUnicom()));
			s.add(this.getNullValue(bean.getTotalTelecom()));
			s.add(this.getNullValue(bean.getSucTotalTelecom()+bean.getNulltotalTelecom()));
			s.add(this.getNullValue(bean.getFaiTotalTelecom()));
			 
			info.add(s.toArray());
		}

		return info;
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getInfoEveSend(List list) {
		List info = null;
		if (list == null)
			return info;
		info = new ArrayList();
		for (Object o : list) {
			EveSendBean bean = (EveSendBean) o;
			List s = new ArrayList();
			s.add(this.getNullValue(bean.getSendDate()));
			s.add(this.getNullValue(bean.getTotal()));
			s.add(this.getNullValue(bean.getTotalok()+bean.getTotalnull()));
			s.add(this.getNullValue(bean.getTotalfail()));
			s.add(this.getNullValue(bean.getTotalmobile()));
			s.add(this.getNullValue(bean.getSucTotalMobile()+bean.getNulltotalmobile()));
			s.add(this.getNullValue(bean.getFaiTotalMobile()));
			s.add(this.getNullValue(bean.getTotalunicom()));
			s.add(this.getNullValue(bean.getSucTotalUnicom()+bean.getNulltotalUnicom()));
			s.add(this.getNullValue(bean.getFaiTotalUnicom()));
			s.add(this.getNullValue(bean.getTotalTelecom()));
			s.add(this.getNullValue(bean.getSucTotalTelecom()+bean.getNulltotalTelecom()));
			s.add(this.getNullValue(bean.getFaiTotalTelecom()));
			info.add(s.toArray());
		}

		return info;
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getInfoParagraph(List list)   {
		List info = null;
		if (list == null)
			return info;
		info = new ArrayList();
		for (Object o : list) {
			ParagraphBean bean = (ParagraphBean) o;
			List s = new ArrayList();
			s.add(this.getNullValue(bean.getPhone3()));
			s.add(this.getNullValue(bean.getTotal()));
			s.add(this.getNullValue(bean.getSucTotal()));
			s.add(this.getNullValue(bean.getFaiTotal()));
			info.add(s.toArray());
		}

		return info;
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getInfoSmsReceive(List list)   {
		List info = null;
		if (list == null)
			return info;
		info = new ArrayList();
		for (Object o : list) {
			SmsReceiveBean bean = (SmsReceiveBean) o;
			List s = new ArrayList();
			s.add(this.getNullValue(bean.getReceiver()));
			s.add(this.getNullValue(bean.getBorndate()));
			s.add(this.getNullValue(bean.getTotal()));
			s.add(this.getNullValue(bean.getTotalmobile()));
			s.add(this.getNullValue(bean.getTotalunicom()));
			s.add(this.getNullValue(bean.getTotaltelecom()));
			info.add(s.toArray());
		}
		return info;
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
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
	
	 

	
	
}
