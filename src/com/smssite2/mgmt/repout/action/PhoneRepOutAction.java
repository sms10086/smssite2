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
import com.smssite2.mgmt.basicsetting.bean.Sift;
import com.smssite2.mgmt.message.bean.MessagePhoneBean;
import com.smssite2.mgmt.message.form.PhoneListForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;
import com.smssite2.mgmt.repout.service.ReportService;

public class PhoneRepOutAction extends Action{
	private static final Log LOG = LogFactory.getLog(BlackRepOutAction.class);
	IMessageService service=new MessageService();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		
		PrintWriter pw = response.getWriter();
		ReportService reportService = new ReportService();
		
		PhoneListForm phoneList=(PhoneListForm)form;
		String messageId=request.getParameter("messageId");
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String routeType= ((Account)request.getSession().getAttribute("account")).getRouteType();
		String content=request.getParameter("content");
		String flag=request.getParameter("flag");
		System.out.println(flag+"¡¡"+content+" "+routeType);
		try{
			phoneList.setPageIndex(0);
			phoneList.setPageSize(Integer.MAX_VALUE);
			List<MessagePhoneBean> list=service.findAllMessagePhone(phoneList,EId,routeType,content,messageId,flag);
			
			response.setBufferSize(32);
			String topStr  = "ÐÕÃû,ºÅÂë";
			
			response.setContentType("application/octet-stream");
			String filename = "sift.csv";
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
			return mapping.findForward("failure");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private List getInfo(List<MessagePhoneBean> list) throws Exception {
		List info = null;
		List s = null;
		if (list == null) {
			return new ArrayList();
		}
		info = new ArrayList();
		for (Object o : list) {
			MessagePhoneBean sizeNo = (MessagePhoneBean) o;
			s = new ArrayList();
			s.add(this.getNullValue(sizeNo.getName()));
			s.add(this.getNullValue(sizeNo.getPhone()));
			info.add(s);
		}

		return info;
	}


	private int[] getStatus(List<Sift> list) {
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
