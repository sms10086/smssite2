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

public class sendMessageServlet extends HttpServlet{	private static Log log = LogFactory.getLog(sendMessageServlet.class);
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		log.debug(">>>>>>>>>>>>*******************************");
		resp.setContentType("text/html;charset=gb2312");
		List<sendMessageThread> list= (List<sendMessageThread>) req.getSession().getAttribute("sendThread");
		PrintWriter out = resp.getWriter();
		StringBuffer str=new StringBuffer();
		int m=0;
		int n=0;
		if(list!=null&&list.size()>0){
		 n = list.size();
			for(int i=0;i<list.size();i++){
				sendMessageThread thread=list.get(i);
				if(thread!=null){
					int succ=thread.getSuccess();
					int total=thread.getTotal();
					if(total==0)continue;
					int a=succ*100/(total+1);
					
					
					
					
					if(a<100){
						str.append("总共有"+total+"条短信待生成，已经生成"+succ+"条短信，生成比例"+a+"% <br>");
					}else{
						m++;
						list.remove(i);
						i--;
					}
					
				}
				
			}
		}
		if(m==n){
			out.print("T");
		}else if(m!=0&&m<n){
			out.print("S");
		}else{
			out.print(str.toString());
		}
		out.flush();
		out.close();
	}
}
