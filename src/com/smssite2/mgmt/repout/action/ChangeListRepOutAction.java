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
import com.note.bean.Task;
import com.note.common.StringUtil;
import com.smssite2.mgmt.message.form.MessageListForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;
import com.smssite2.mgmt.repout.service.ReportService;

public class ChangeListRepOutAction extends Action{	private static final Log LOG = LogFactory
	.getLog(ChangeListRepOutAction.class);
	IMessageService service=new MessageService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		
		MessageListForm msgList=(MessageListForm)form;
		String routeType=((Account)request.getSession().getAttribute("account")).getRouteType();
		String EId=((Account)request.getSession().getAttribute("account")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		PrintWriter pw = response.getWriter();
		ReportService reportService = new ReportService();
		try{
			msgList.setPageIndex(0);
			msgList.setPageSize(Integer.MAX_VALUE);
			List<Task> list=service.findAllMessageOut(msgList,EId,routeType,staffId);
			response.setBufferSize(32);
			String topStr  = "短信编号,发送人,定时设置,分割条数,短信号码,短信署名短信类型,短信内容,手机号,姓名,替换内容";
			
			response.setContentType("application/octet-stream");
			String filename = "changelist.csv";
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
	private List getInfo(List<Task> list) throws Exception {
		List info = null;
		List s = null;
		if (list == null) {
			return new ArrayList();
		}
		info = new ArrayList();
		for (Object o : list) {
			Task sizeNo = (Task) o;
			s = new ArrayList();
			s.add(this.getNullValue(sizeNo.getID()));
			s.add(this.getNullValue(sizeNo.getStaffId()));
			if(sizeNo.getScheduleTime()==null){
				s.add("即时发出");
			}else{
				s.add(sizeNo.getScheduleTime()+"发出");
			}
			s.add(this.getNullValue(sizeNo.getSplitCount()));
			s.add(this.getNullValue(sizeNo.getSmsNum()));
			s.add(this.getNullValue(sizeNo.getSmsSign()));
			s.add(this.getNullValue(sizeNo.getMesType()));
			if(!StringUtil.isEmpty(sizeNo.getContent()))
					sizeNo.setContent(sizeNo.getContent().replace("\r\n", " ").replace(",", "，"));
			s.add(this.getNullValue(sizeNo.getPhone()));
			s.add(this.getNullValue(sizeNo.getName()));
			s.add(this.getNullValue(sizeNo.getPcontent()));
			info.add(s);
		}

		return info;
	}

	@SuppressWarnings("unchecked")
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
