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
			 topStr  = "������,��������,�ɹ�����,ʧ������,�ƶ�����,�ƶ��ɹ�,�ƶ�ʧ��,��ͨ����,��ͨ�ɹ�,��ͨʧ��,��������,���ųɹ�,����ʧ��";
		 else if(type.equals("evesend"))
			 topStr  = "����,��������,�ɹ�����,ʧ������,�ƶ�����,�ƶ��ɹ�,�ƶ�ʧ��,��ͨ����,��ͨ�ɹ�,��ͨʧ��,��������,���ųɹ�,����ʧ��";
		 else if(type.equals("paragraph"))
			 topStr  = "�����,��������,�ɹ�����,ʧ������";
		 else if(type.equals("smsreceive"))
			 topStr  = "������,����ʱ��,�ɹ�����,�ƶ��ɹ�,��ͨ�ɹ�,���ųɹ�";
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
			 title  = "���ŷ���ͳ�Ʊ���";
		 else if(type.equals("evesend"))
			 title = "ÿ�շ�������ͳ��";
		 else if(type.equals("paragraph"))
			 title = "����η���ͳ��";
		 else if(type.equals("smsreceive"))
			 title = "���Ž���ͳ��";
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
