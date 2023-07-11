package com.smssite2.mgmt.report.service;

import java.util.List;
import com.note.NoteException;
import com.smssite2.mgmt.report.bean.ClassificationSendBean;
import com.smssite2.mgmt.report.bean.EveSendBean;
import com.smssite2.mgmt.report.bean.GroupSendBean;
import com.smssite2.mgmt.report.bean.SmsReceiveBean;
import com.smssite2.mgmt.report.bean.SmsSendBean;
import com.smssite2.mgmt.report.bean.ParagraphBean;
import com.smssite2.mgmt.report.form.ReportBaseForm;

public interface IReportService {	public List<SmsSendBean> findSmsSendWithPagination(ReportBaseForm form, String routeType)throws  NoteException;
	public List<ParagraphBean> findParagraphWithPagination(ReportBaseForm form, String routeType)throws  NoteException;
	public List<EveSendBean> findEveSendWithPagination(ReportBaseForm form1, String routeType)throws  NoteException;
	public List<GroupSendBean> findGroupsendWithPagination(ReportBaseForm form1, String routeType) throws  NoteException;
	public List<ClassificationSendBean> findClassificationSendWithPagination(ReportBaseForm form1, String routeType) throws NoteException;
	public List<SmsReceiveBean> findSmsReceiveWithPagination(ReportBaseForm form1, String routeType) throws NoteException;
}
