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
import com.note.common.StringUtil;
import com.smssite2.mgmt.linkman.bean.Linkman;
import com.smssite2.mgmt.linkman.form.LinkmanListForm;
import com.smssite2.mgmt.linkman.service.ILinkmanService;
import com.smssite2.mgmt.linkman.service.impl.LinkmanService;
import com.smssite2.mgmt.repout.service.ReportService;

public class LinkmanRepOutAction extends Action{	private static final Log LOG = LogFactory.getLog(LinkmanRepOutAction.class);
	ILinkmanService service = new LinkmanService();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		LinkmanListForm manF=(LinkmanListForm)form;
		String groupid=request.getParameter("groupid");
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		PrintWriter pw = response.getWriter();
		ReportService reportService = new ReportService();
		System.out.println(groupid+" *");
		try{
			manF.setPageIndex(0);
			manF.setPageSize(Integer.MAX_VALUE);
			List<Linkman> list=service.findAllLinkman(manF,eid,groupid,staffId,"repout");
			System.out.println(list.size());
			response.setBufferSize(32);
			String topStr  = "姓名,电话,状态,生日,性别,职位,单位,个人说明,可选短信内容";
			
			response.setContentType("application/octet-stream");
			String filename = "linkman.csv";
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
	private List getInfo(List<Linkman> list) throws Exception {
		List info = null;
		List s = null;
		if (list == null) {
			return new ArrayList();
		}
		info = new ArrayList();
		for (Object o : list) {
			Linkman sizeNo = (Linkman) o;
			s = new ArrayList();
			s.add(this.getNullValue(sizeNo.getName()));
			s.add(this.getNullValue(sizeNo.getPhone()));
			s.add(this.getNullValue(sizeNo.getStatus()));
			s.add(this.getNullValue(sizeNo.getBirthday()));
			s.add(this.getNullValue(sizeNo.getSex()));
			s.add(this.getNullValue(sizeNo.getPost()));
			s.add(this.getNullValue(sizeNo.getOrgName()));
			s.add(this.getNullValue(sizeNo.getUserMemo()));
			if(!StringUtil.isEmpty(sizeNo.getOptionalContent()))
				sizeNo.setOptionalContent(sizeNo.getOptionalContent().replace("\r\n", " ").replace(",", "，"));
			s.add(this.getNullValue(sizeNo.getOptionalContent()));
			info.add(s);
		}

		return info;
	}


	private int[] getStatus(List<Linkman> list) {
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
