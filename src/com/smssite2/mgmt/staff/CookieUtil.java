package com.smssite2.mgmt.staff;

import java.util.ArrayList;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.note.MD5Proxy;
import com.note.NoteException;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.note.common.StringUtil;
import com.smssite2.mgmt.message.sendMessageThread;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class CookieUtil {	
	private static Log log = LogFactory.getLog(CookieUtil.class);
    private final static String domainName = "091110";
    private final static int cookieMaxAge = 60 * 60 * 24 * 7 * 3 ;
    private final static String webKey = "key";
 
    public static void saveCookie(String eid, String username,String password,String path, HttpServletResponse response){
        try {
        	
            String cookieValueWithMD5=MD5Proxy.getMd5Str(username.toUpperCase()+password.toLowerCase()+webKey);
            String cookieValue=eid+":"+username+":"+cookieValueWithMD5;
			String cookieValue64=Base64Util.encode(cookieValue.getBytes("GB18030"));
			Cookie cookie=new Cookie(domainName,cookieValue64);
			cookie.setMaxAge(cookieMaxAge);
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
    }
    
    public static void clearCookie(String path,HttpServletResponse response){
    	 try {
	    	Cookie cookie=new Cookie(domainName,null);
	    	cookie.setMaxAge(0);
	    	cookie.setPath("/");
	    	response.addCookie(cookie);
    	 }catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
    }
    public static boolean cookieLogin(HttpServletRequest request,HttpServletResponse response){
    	boolean result=false;
    	try{
			Cookie cookies[] = request.getCookies();
			String cookieValue64 = null;
	         if(cookies!=null){
	                for(int i=0;i<cookies.length;i++){
	                       if (domainName.equals(cookies[i].getName())) {
	                              cookieValue64 = cookies[i].getValue();
	                              break;
	                       }
	                }
	         }
	         if(cookieValue64!=null){
	        	 String cookieValueAfterDecode=Base64Util.decode(cookieValue64,"GB18030");
	        	 String[] cookieValues=cookieValueAfterDecode.split(":");
	        	 if(cookieValues.length==3){
	        		 String eid=cookieValues[0];
	        		 String username=cookieValues[1];
	        		 String MD5Value=cookieValues[2];
	        		 StaffService  service=StaffService.instance();
	        		 try {
						Staff staff=service.findStaffById(username, eid);
						if(MD5Value.equals(MD5Proxy.getMd5Str(staff.getUserId().toUpperCase()+staff.getNewpwd().toLowerCase()+webKey))){
							Account account=service.findAccount(eid);
							String feastname=service.findFeastName(eid,StringUtil.getTime(0));
							if(account.getIssmsSign()!=null&&!account.getIssmsSign().equals("0")){
								if(staff.getSmsSign()!=null&&staff.getSmsSign().length()>0){
									account.setSmsSign("/"+staff.getSmsSign().trim());
								}
								else if(account.getSmsSign()!=null&&account.getSmsSign().trim().length()>0){
									account.setSmsSign("/"+account.getSmsSign().trim());
								}
								else account.setSmsSign("");
							}else{
								account.setSmsSign("");
							}
							request.getSession().setAttribute("staff", staff);
							request.getSession().setAttribute("account", account);
							request.getSession().setAttribute("feastName", feastname);
							request.getSession().setAttribute("sendThread", new ArrayList<sendMessageThread>());
							saveCookie(eid,username,staff.getNewpwd(),"/",response);
							result=true;
						}
					} catch (NoteException e) {
						log.error(e.getMessage(),e);
					}
	        	 }
	         }
    	}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
    	return result;
    }
}
