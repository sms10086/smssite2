package com.smssite2.mgmt.repout.action;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import com.note.common.DbBean;
import com.note.common.StringUtil;
import com.smssite2.mgmt.message.form.SendHistoryForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;
import com.smssite2.mgmt.repout.service.ReportService;

public class SendHistoryRepOutAction extends Action{	private static final Log LOG = LogFactory.getLog(SendHistoryRepOutAction.class);
	IMessageService service=new MessageService();
	@SuppressWarnings({ "unchecked", "static-access" })
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		SendHistoryForm hisList=(SendHistoryForm)form;
		String EId=((Account)request.getSession().getAttribute("account")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		
		PrintWriter pw = response.getWriter();
		ReportService reportService = new ReportService();
		DbBean db=new DbBean();
		Connection conn =null;
		PreparedStatement stm=null;
		ResultSet rs=null;
		try{
			hisList.setPageIndex(0);
			hisList.setPageSize(Integer.MAX_VALUE);
			conn=db.getConnection();
			
			StringBuffer sql=new StringBuffer();
			StringBuffer w = new StringBuffer();
			sql.append("select his.staffId,his.id,his.messageId,his.routeType,his.phone,his.name,his.content," +
					"his.result as result,his.status,his.smsNum,his.smssign,his.sendTime,his.addTime,his.descript" +
					" from smsmt his  where result>-1 ");
			if(!staffId.trim().toLowerCase().equals("admin")){
				w.append(" and lower(his.staffid)='"+staffId.trim().toLowerCase()+"' ");
			
				 
			}
			if(hisList.getStateTime()!=null&&!hisList.getStateTime().trim().equals("")){
				w.append(" and his.sendTime>=?");
			}
			if(hisList.getEndTime()!=null&&!hisList.getEndTime().trim().equals("")){
				w.append(" and his.sendTime<?");
			}
			if(hisList.getPhone()!=null&&!hisList.getPhone().trim().equals("")){
				w.append(" and his.phone like ? ");
			}
			if(hisList.getContent()!=null&&!hisList.getContent().trim().equals("")){
				w.append(" and his.content like ? ");
			}
			 
			if(hisList.getStatus()!=null&&!hisList.getStatus().trim().equals("")){
				if(hisList.getStatus().equals("etc")){
					w.append(" and  his.status<>'0' and his.status<>'10004' ");
					 
				}else if(hisList.getStatus().equals("发出")){
					w.append(" and (his.status is null or his.status='10004') ");
					
				}else
					w.append(" and  his.status='0' ");
				 
			}
			w.append(" and his.EId='"+EId+"' and isdelete=0  ");
			if(!StringUtil.isEmpty(w.toString())){
				sql.append(w.toString());
			}
			stm=conn.prepareStatement(sql.toString());
			int i=0;
			if(hisList.getStateTime()!=null&&!hisList.getStateTime().trim().equals("")){
				stm.setTimestamp(++i, StringUtil.parseTimestamp(hisList.getStateTime(),0));
			}
			if(hisList.getEndTime()!=null&&!hisList.getEndTime().trim().equals("")){
				stm.setTimestamp(++i, StringUtil.parseTimestamp(hisList.getEndTime(),1));
			}
			if(hisList.getPhone()!=null&&!hisList.getPhone().trim().equals("")){
				stm.setString(++i, hisList.getPhone().trim()+"%");
			}
			if(hisList.getContent()!=null&&!hisList.getContent().trim().equals("")){
				stm.setString(++i, hisList.getContent().trim()+"%");
			}
			rs=stm.executeQuery();
		 
			response.setBufferSize(32);
			String topStr  = "短信编号,发送人,发出时间,手机号码,接收人,发出,报告,短信内容,子号,短信署名,条数,创建时间";
			
			response.setContentType("application/octet-stream");
			String filename = "sendhistory.csv";
			filename=new String(filename.getBytes(),"utf-8");
			response.setHeader("Location",filename);
			response.setHeader("Cache-Control", "max-age=10000");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setCharacterEncoding("gb2312");
			reportService.setPw(pw);
			reportService.getReportInfo(topStr, rs);
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			return mapping.findForward("fail");
		}finally{
			pw.close();
			db.close(stm, conn);
			db.close(rs);
		}
		return null;
	}

}
