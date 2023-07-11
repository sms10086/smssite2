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
import com.smssite2.mgmt.basicsetting.bean.Sift;
import com.smssite2.mgmt.basicsetting.form.BlackListForm;
import com.smssite2.mgmt.basicsetting.service.IBasicSettingService;
import com.smssite2.mgmt.basicsetting.service.impl.BasicSettingService;
import com.smssite2.mgmt.repout.service.ReportService;

	public class SiftRepOutAction extends Action{		
		private static final Log LOG = LogFactory.getLog(BlackRepOutAction.class);
		IBasicSettingService service = new BasicSettingService();
		@SuppressWarnings("unchecked")
		public ActionForward execute(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception  {
			
			PrintWriter pw = response.getWriter();
			ReportService reportService = new ReportService();
			
			BlackListForm siftF=(BlackListForm)form;
			String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
			try{
				siftF.setPageIndex(0);
				siftF.setPageSize(Integer.MAX_VALUE);
				List<Sift> list=service.findAllSift(eid,siftF);
				response.setBufferSize(32);
				String topStr  = "词语,添加单位,更新时间";
				
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
				return mapping.findForward("fail");
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		private List getInfo(List<Sift> list) throws Exception {
			List info = null;
			List s = null;
			if (list == null) {
				return new ArrayList();
			}
			info = new ArrayList();
			for (Object o : list) {
				Sift sizeNo = (Sift) o;
				s = new ArrayList();
				if(!StringUtil.isEmpty(sizeNo.getSiftContent()))
					sizeNo.setSiftContent(sizeNo.getSiftContent().replace("\r\n", " ").replace(",", "，"));
				s.add(this.getNullValue(sizeNo.getSiftContent()));
				s.add(this.getNullValue(sizeNo.getEID()));
				s.add(this.getNullValue((new SimpleDateFormat("yyyy-MM-dd")).format(sizeNo.getAddTime())));
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
