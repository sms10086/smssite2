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
import com.smssite2.mgmt.staff.bean.StaffBean;
import com.smssite2.mgmt.staff.form.AdminListForm;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class StaffListRepOutAction extends Action{	private static final Log LOG = LogFactory.getLog(StaffListRepOutAction.class);
	IStaffService service = new StaffService();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		AdminListForm adminList=(AdminListForm)form;
		
		String EId =((Staff)request.getSession().getAttribute("staff")).getEId();
		
		PrintWriter pw = response.getWriter();
		ReportService reportService = new ReportService();
		
		try{
			adminList.setPageIndex(0);
			adminList.setPageSize(Integer.MAX_VALUE);
			List<StaffBean> admins=service.findStaffByEid(adminList,EId);
			response.setBufferSize(32);
			String topStr  = "用户帐号,用户名称,短信号码,短信署名,部门名称,用户角色,授权情况";
			
			response.setContentType("application/octet-stream");
			String filename = "staff.csv";
			filename=new String(filename.getBytes(),"utf-8");
			response.setHeader("Location",filename);
			response.setHeader("Cache-Control", "max-age=10000");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setCharacterEncoding("gb2312");
			reportService.setPw(pw);
			reportService.getReportInfo(topStr, getInfo(admins));
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
	private List getInfo(List<StaffBean> list) throws Exception {
		List info = null;
		List s = null;
		if (list == null) {
			return new ArrayList();
		}
		String privilegeState = null;
		info = new ArrayList();
		for (Object o : list) {
			StaffBean staff = (StaffBean) o;
			s = new ArrayList();
			s.add(this.getNullValue(staff.getUserId()));
			s.add(this.getNullValue(staff.getUserName()));
			s.add(this.getNullValue(staff.getSmsNum()));
			s.add(this.getNullValue(staff.getSmsSign()));
			s.add(this.getNullValue(staff.getTname()));
			s.add(this.getNullValue(staff.getUpost()));
			if("1".equals(staff.getIsadmin())){
				privilegeState = "管理员";
			}else{
				privilegeState = "普通授权";
			}
			s.add(this.getNullValue(privilegeState));
			info.add(s);
		}

		return info;
	}


	private int[] getStatus(List<StaffBean> list) {
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
