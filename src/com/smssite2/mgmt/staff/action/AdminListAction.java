package com.smssite2.mgmt.staff.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.NoteException;
import com.note.bean.Staff;
import com.smssite2.mgmt.staff.bean.StaffBean;
import com.smssite2.mgmt.staff.form.AdminListForm;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class AdminListAction extends Action{	IStaffService service = new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		AdminListForm adminList=(AdminListForm)form;
	
		Staff staff=(Staff)request.getSession().getAttribute("staff");
		if(staff==null)throw new NoteException("sessino  yichang ");
		String EId =((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		String[] strs=credit.split("\\|");
		String privilege="";
		if(!EId.equals("000000")){
			if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("510")<0){
				request.setAttribute("message", "用户"+staffId+"没有[用户设置]的操作权限！");
				return mapping.findForward("err");
			}
			if(credit.trim().toLowerCase().equals("admin")){
				privilege="510:FAMD";
			}else{
				for(int i=1;i<strs.length;i++){
					if(strs[i].indexOf("510")>=0)privilege=strs[i];
				}
			}
			
			
		}else{
			if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("Acu")<0){
				request.setAttribute("message", "用户"+staffId+"没有[用户设置]的操作权限！");
				return mapping.findForward("err");
			}
			if(credit.trim().toLowerCase().equals("admin")){
				privilege="Acu:FAMD";
			}else{
				for(int i=1;i<strs.length;i++){
					if(strs[i].indexOf("Acu")>=0)privilege=strs[i];
				}
			}
		}
		
		List<StaffBean> admins=new ArrayList<StaffBean>();
		try{
			
			admins=service.findStaffByEid(adminList,EId);
			request.setAttribute("staffs", admins);
			request.setAttribute("privilege", privilege);
			request.setAttribute("pagination",adminList);
		}catch(Exception e){
			e.printStackTrace();
			return mapping.findForward("failure");
		}
		
		return mapping.findForward("success");
	}
}
