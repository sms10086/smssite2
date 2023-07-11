package com.smssite2.mgmt.repout.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.note.common.StringUtil;
import com.smssite2.mgmt.message.bean.Receive;
import com.smssite2.mgmt.message.form.RiceiveListForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;
import com.smssite2.mgmt.repout.service.ReportService;

public class ReceiveRepOutAction extends Action{
	private static final Log LOG = LogFactory.getLog(SendHistoryRepOutAction.class);
	IMessageService service=new MessageService();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		RiceiveListForm riceF=(RiceiveListForm)form;
		String routeType=((Account)request.getSession().getAttribute("account")).getRouteType();
		String EId=((Account)request.getSession().getAttribute("account")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		PrintWriter pw = response.getWriter();
		ReportService reportService = new ReportService();
		 try{
			riceF.setPageIndex(0);
			riceF.setPageSize(Integer.MAX_VALUE);
			List<Receive> list=service.findAllRecive(riceF,EId,routeType,staffId);
			response.setBufferSize(32);
			String topStr  = "信编号,接收人,接收时间,手机号码,发信人,短信内容,子号";
			
			response.setContentType("application/octet-stream");
			String filename = "reseive.csv";
			filename=new String(filename.getBytes(),"utf-8");
			response.setHeader("Location",filename);
			response.setHeader("Cache-Control", "max-age=10000");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setCharacterEncoding("gb2312");
			reportService.setPw(pw);
			reportService.getReportInfo(topStr, getInfo(list));
			pw.flush();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return mapping.findForward("fail");
		}finally{
			pw.close();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private List getInfo(List<Receive> list) throws Exception {
		List info = null;
		List s = null;
		if (list == null) {
			return new ArrayList();
		}
		info = new ArrayList();
		for (Object o : list) {
			Receive sizeNo = (Receive) o;
			s = new ArrayList();
			s.add(this.getNullValue(sizeNo.getId()));
			s.add(this.getNullValue(sizeNo.getReceiver()));
			s.add(this.getNullValue(sizeNo.getReceiveTime()));
			s.add(this.getNullValue(sizeNo.getPhone()));
			s.add(this.getNullValue(sizeNo.getSender()));
			if(!StringUtil.isEmpty(sizeNo.getContent()))
				sizeNo.setContent(sizeNo.getContent().replace("\r\n", " ").replace(",", "，"));
			s.add(this.getNullValue(sizeNo.getContent()));
			s.add(this.getNullValue(sizeNo.getSmsNum()));
			info.add(s);
		}

		return info;
	}


	private int[] getStatus(List<Receive> list) {
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
