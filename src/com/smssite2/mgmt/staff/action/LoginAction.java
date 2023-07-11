package com.smssite2.mgmt.staff.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.note.common.LogUtil;
import com.note.common.TimeUtil;
import com.smssite2.mgmt.message.sendMessageThread;
import com.smssite2.mgmt.staff.form.LoginForm;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class LoginAction extends Action{
private IStaffService staffService = new StaffService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		
		LoginForm loginForm = (LoginForm) form;
		String EId = loginForm.getEId();
		String staffId = loginForm.getStaffId();
		String password = loginForm.getPassword();
		String checkCode = loginForm.getCheckCode();
		HttpSession session = request.getSession();
		try {
			if(checkCode==null||checkCode.trim().equals("")){
				throw new Exception("验证码不能为空");
				
			}
			if(request.getSession().getAttribute("verifyCode")!=null&&!request.getSession().getAttribute("verifyCode").equals(checkCode.trim())){
				throw new Exception("验证码不正确");
				
			}
			Calendar cale = Calendar.getInstance();
			SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd");
			String time=smd.format(cale.getTime());
			String feastname=staffService.findFeastName(EId,time);
		
				Account account=staffService.findAccount(EId);
				
				Staff staff=staffService.login(EId,staffId,password);
				
				if(account.getIssmsSign()!=null&&!account.getIssmsSign().equals("0")){
					if(staff.getSmsSign()!=null&&staff.getSmsSign().length()>0){
						if(EId.equals("taobaoll")){
							account.setSmsSign("【"+staff.getSmsSign().trim()+"】");
						}
						else
							account.setSmsSign("/"+staff.getSmsSign().trim());
					}
					else if(account.getSmsSign()!=null&&account.getSmsSign().trim().length()>0){
						if(EId.equals("taobaoll")){
							account.setSmsSign("【"+staff.getSmsSign().trim()+"】");
						}
						else
						account.setSmsSign("/"+account.getSmsSign().trim());
					}
					else account.setSmsSign("");
				}else{
					account.setSmsSign("");
				}
				
				session.setAttribute("staff", staff);
				session.setAttribute("account", account);
				session.setAttribute("feastName", feastname);
				 
				request.getSession().setAttribute("sendThread", new ArrayList<sendMessageThread>());
			LogUtil.writeLog("用户登陆","登陆","登陆时间:"+TimeUtil.now()+" IP:"+request.getRemoteAddr(),staffId,EId);
			return mapping.findForward("success");
		} catch(Exception e){
			request.setAttribute("EId", EId);
			request.setAttribute("staffId", staffId);
			request.setAttribute("checkCode", checkCode);
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		}
	}
	
}
