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
import com.note.bean.Role;
import com.note.bean.Staff;
import com.smssite2.mgmt.privilege.form.RoleListForm;
import com.smssite2.mgmt.privilege.service.IPrivilegeService;
import com.smssite2.mgmt.privilege.service.impl.PrivileService;
import com.smssite2.mgmt.repout.service.ReportService;

public class RoleListRepOutAction extends Action{	private static final Log LOG = LogFactory.getLog(RoleListRepOutAction.class);
	IPrivilegeService service = new PrivileService();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		RoleListForm roleList=(RoleListForm)form;
		
		String EId =((Staff)request.getSession().getAttribute("staff")).getEId();
		
		PrintWriter pw = response.getWriter();
		ReportService reportService = new ReportService();
		try{
			roleList.setPageIndex(0);
			roleList.setPageSize(Integer.MAX_VALUE);
			List<Role> roles=service.findRoleByEId(roleList,EId);
			
			response.setBufferSize(32);
			String topStr  = "角色名称,角色说明,授权情况";
			
			response.setContentType("application/octet-stream");
			String filename = "role.csv";
			filename=new String(filename.getBytes(),"utf-8");
			response.setHeader("Location",filename);
			response.setHeader("Cache-Control", "max-age=10000");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setCharacterEncoding("gb2312");
			reportService.setPw(pw);
			reportService.getReportInfo(topStr, getInfo(roles));
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
	private List getInfo(List<Role> list) throws Exception {
		List info = null;
		List s = null;
		if (list == null) {
			return new ArrayList();
		}
		info = new ArrayList();
		String privilegeSituation = null;
		for (Object o : list) {
			Role role = (Role) o;
			s = new ArrayList();
			s.add(this.getNullValue(role.getUname()));
			s.add(this.getNullValue(role.getUmemo()));
			if("admin".equals(role.getCredit())){
				privilegeSituation = "管理员";
			}else{
				privilegeSituation = "普通授权";
			}
			s.add(this.getNullValue(privilegeSituation));
			info.add(s);
		}

		return info;
	}


	private int[] getStatus(List<Role> list) {
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
