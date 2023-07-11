package com.smssite2.mgmt.message;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuditingMessageServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(sendMessageServlet.class);
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		log.info(">>>>>>>>>>>in AuditingMessageServlet");
		resp.setContentType("text/html;charset=gb2312");
		List<AuditingMessageThread> list= (List<AuditingMessageThread>) req.getSession().getAttribute("auditThread ");
		log.info(">>>>>>>>>"+list);
		PrintWriter out = resp.getWriter();
		StringBuffer str=new StringBuffer();
		int m=0;
		int n=0;
		if(list!=null&&list.size()>0){
		 n = list.size();
			for(int i=0;i<list.size();i++){
				AuditingMessageThread thread=list.get(i);
				if(thread!=null){
					int succ=thread.getSum();
					int total=thread.getTotalNum();
					if(total==0)continue;
					int a=succ*100/(total+1);
					 
					if(a<100){
						str.append("总共有"+total+"条短信已审核，已经插入队列"+succ+"条短信，插入比例"+a+"% <br>");
						 
					}else{
						m++;
						list.remove(i);
						i--;
					}
				}
				
			}
		}
		if(m==n){
			out.print("t");
		}else if(m!=0&&m<n){
			out.print("s");
		}else{
			out.print(str.toString());
		}
		out.flush();
		out.close();
		 
	}
}
