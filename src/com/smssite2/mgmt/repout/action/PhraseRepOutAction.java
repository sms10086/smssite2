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
import com.note.bean.Phrase;
import com.note.bean.Staff;
import com.note.common.StringUtil;
import com.smssite2.mgmt.message.form.PhraseListForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;
import com.smssite2.mgmt.repout.service.ReportService;

public class PhraseRepOutAction extends Action{	private static final Log LOG = LogFactory.getLog(PhraseRepOutAction.class);
	IMessageService service= new MessageService();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		PhraseListForm phraseList=(PhraseListForm)form;
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		PrintWriter pw = response.getWriter();
		ReportService reportService = new ReportService();
		try{
			phraseList.setPageIndex(0);
			phraseList.setPageSize(Integer.MAX_VALUE);
			List<Phrase> list=service.findAllPhrase(EId,phraseList,staffId);
			response.setBufferSize(32);
			String topStr  = "短语内容,字数,类别,更新时间";
			
			response.setContentType("application/octet-stream");
			String filename = "phrae.csv";
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
	private List getInfo(List<Phrase> list) throws Exception {
		List info = null;
		List s = null;
		if (list == null) {
			return new ArrayList();
		}
		info = new ArrayList();
		for (Object o : list) {
			Phrase sizeNo = (Phrase) o;
			s = new ArrayList();
			if(!StringUtil.isEmpty(sizeNo.getPhraseContent()))
				sizeNo.setPhraseContent(sizeNo.getPhraseContent().replace("\r\n", " ").replace(",", "，"));
			s.add(this.getNullValue(sizeNo.getPhraseContent()));
			s.add(this.getNullValue(sizeNo.getPhraseContent().length()));
			s.add(this.getNullValue(sizeNo.getPhraseType()));
			if(sizeNo.getUpDateTime()==null)s.add("");
			else s.add(this.getNullValue((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(sizeNo.getUpDateTime())));
			info.add(s);
		}

		return info;
	}


	private int[] getStatus(List<Phrase> list) {
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
