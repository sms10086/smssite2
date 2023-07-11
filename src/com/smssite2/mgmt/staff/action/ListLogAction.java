package com.smssite2.mgmt.staff.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Log;
import com.note.bean.Staff;
import com.smssite2.mgmt.staff.form.LogForm;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class ListLogAction extends Action{
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		IStaffService service=new StaffService();
		LogForm logF=(LogForm)form;
		String EId= ((Staff)request.getSession().getAttribute("staff")).getEId();
		Calendar cale = Calendar.getInstance();
		SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd");
		String date=smd.format(cale.getTime());
		
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("E")<0){
			request.setAttribute("message", "用户"+staffId+"没有[操作日志管理]的操作权限！");
			return mapping.findForward("err");
		}
		
		
		
		if(logF.getStartTime()==null||logF.getStartTime().trim().equals("")){
			logF.setStartTime(date);
		}
		if(logF.getEndTime()==null||logF.getEndTime().trim().equals("")){
			logF.setEndTime(date);
		}
		try{
		List<Log> list =service.findListLog(logF,EId);
		request.setAttribute("logList",list);
		request.setAttribute("pagination", logF);
		return mapping.findForward("success");
		}catch(Exception e){
			e.printStackTrace();
			return mapping.findForward("failure");
		}
	}
}

