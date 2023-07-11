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
import com.note.bean.Staff;
import com.smssite2.mgmt.repout.service.ReportService;
import com.smssite2.mgmt.staff.form.LogForm;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class LogRepOutAction extends Action{
	private static final Log LOG = LogFactory.getLog(BlackRepOutAction.class);
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		IStaffService service=new StaffService();
		LogForm logF=(LogForm)form;
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		
		PrintWriter pw = response.getWriter();
		ReportService reportService = new ReportService();
		try{
			logF.setPageIndex(0);
			logF.setPageSize(Integer.MAX_VALUE);
			List<com.note.bean.Log> list =service.findListLog(logF,eid);
			response.setBufferSize(32);
			String topStr  = "日期时间,查看,功能,操作类型,用户";
			
			response.setContentType("application/octet-stream");
			String filename = "log.csv";
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
	private List getInfo(List<com.note.bean.Log> list) throws Exception {
		List info = null;
		List s = null;
		if (list == null) {
			return new ArrayList();
		}
		info = new ArrayList();
		for (Object o : list) {
			com.note.bean.Log sizeNo = (com.note.bean.Log) o;
			s = new ArrayList();
			s.add(this.getNullValue(sizeNo.getLogTime()));
			s.add("查看");
			s.add(this.getNullValue(sizeNo.getMenuItem()));
			s.add(this.getNullValue(sizeNo.getActionType()));
			s.add(this.getNullValue(sizeNo.getStaffId()));
			info.add(s);
		}

		return info;
	}
	private int[] getStatus(List<com.note.bean.Log> list) {
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
