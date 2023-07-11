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
import com.note.bean.Team;
import com.smssite2.mgmt.repout.service.ReportService;
import com.smssite2.mgmt.staff.form.OrgListForm;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class OrgListRepOutAction extends Action{	private static final Log LOG = LogFactory.getLog(OrgListRepOutAction.class);
	IStaffService service = new StaffService();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		OrgListForm orgList=(OrgListForm)form;
		
		String EId =((Staff)request.getSession().getAttribute("staff")).getEId();
		
		PrintWriter pw = response.getWriter();
		ReportService reportService = new ReportService();
		
		try{
			orgList.setPageIndex(0);
			orgList.setPageSize(Integer.MAX_VALUE);
			List<Team> list=service.findOrgsByEId(orgList,EId);
			response.setBufferSize(32);
			String topStr  = "部门名称,说明";
			
			response.setContentType("application/octet-stream");
			String filename = "organization.csv";
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
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private List getInfo(List<Team> list) throws Exception {
		List info = null;
		List s = null;
		if (list == null) {
			return new ArrayList();
		}
		info = new ArrayList();
		for (Object o : list) {
			Team org = (Team) o;
			s = new ArrayList();
			s.add(this.getNullValue(org.getTname()));
			s.add(this.getNullValue(org.getTmemo()));
			info.add(s);
		}

		return info;
	}


	private int[] getStatus(List<Team> list) {
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
