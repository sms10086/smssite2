package com.smssite2.mgmt.staff.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SessionFilter implements Filter {	private static Log log = LogFactory.getLog(SessionFilter.class);
	public void destroy() {
		
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest)arg0;
			HttpServletResponse response = (HttpServletResponse)arg1;
			response.setHeader("Pragma","No-cache");
			response.setHeader("Cache-Control","no-cache");
			response.setDateHeader("Expires", 0);
			request.setCharacterEncoding("gb2312");
			response.setCharacterEncoding("gb2312");
			arg2.doFilter(request, response);
			response.setCharacterEncoding("gb2312");
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		finally {
			
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}
	
}
