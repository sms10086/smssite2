package com.smssite2.mgmt.repout.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import com.smssite2.mgmt.basicsetting.bean.Feast;
import com.smssite2.mgmt.message.form.FeastListForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;
import com.smssite2.mgmt.repout.service.ReportService;

public class FeastRepOutAction extends Action{
	private static final Log LOG = LogFactory.getLog(FeastRepOutAction.class);
	IMessageService service= new MessageService();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		
		FeastListForm feastList=(FeastListForm)form;
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		PrintWriter pw = response.getWriter();
		ReportService reportService = new ReportService();
		try{
			feastList.setPageIndex(1);
			feastList.setPageSize(Integer.MAX_VALUE);
			List<Feast> list=service.findAllFeast(EId,feastList);
			response.setBufferSize(32);
			String topStr  = "节日,节日类型,公历日期,农历日期,节日祝辞";
			response.setContentType("application/octet-stream");
			String filename = "Feast.csv";
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
	private List getInfo(List<Feast> list) throws Exception {
		List info = null;
		List s = null;
		if (list == null) {
			return new ArrayList();
		}
		info = new ArrayList();
		for (Object o : list) {
			Feast sizeNo = (Feast) o;
			s = new ArrayList();
			s.add(this.getNullValue(sizeNo.getFeastName()));
			s.add(this.getNullValue(sizeNo.getFeastStyle()));
			if(sizeNo.getWorldDate()==null){
				s.add("");
			}else{
				s.add(this.getNullValue(sizeNo.getWorldDate()));
			}
			if(sizeNo.getChinaDate()==null){
				s.add("");
			}else{
				s.add(this.getNullValue(sizeNo.getChinaDate()));
			}
			if(!StringUtil.isEmpty(sizeNo.getFeastContent()))
				sizeNo.setFeastContent(sizeNo.getFeastContent().replace("\r\n", " ").replace(",", "，"));
			s.add(this.getNullValue(sizeNo.getFeastContent()));
			info.add(s);
		}

		return info;
	}


	private int[] getStatus(List<Feast> list) {
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

