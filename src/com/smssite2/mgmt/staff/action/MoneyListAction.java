package com.smssite2.mgmt.staff.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.smssite2.mgmt.staff.bean.CheckMoney;
import com.smssite2.mgmt.staff.form.MoneyForm;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class MoneyListAction extends Action{	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		IStaffService service=new StaffService();
		MoneyForm moeyF=(MoneyForm)form;
		String EId= ((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("Ac2")<0){
			request.setAttribute("message", "用户"+staffId+"没有[充值审核]的操作权限！");
			return mapping.findForward("err");
		}
		String[] strs=credit.split("\\|");
		String privilege="";
		if(credit.trim().toLowerCase().equals("admin")){
			privilege="Ac2:DS";
		}else{
			for(int i=1;i<strs.length;i++){
				if(strs[i].indexOf("Ac2")>=0)privilege=strs[i];
			}
		}
		request.setAttribute("privilege", privilege);
		
		try{
			List<CheckMoney> list =service.findUncheckListM(moeyF,EId);
			request.setAttribute("moneys",list);
			request.setAttribute("pagination", moeyF);
			return mapping.findForward("success");
		}catch(Exception e){
			e.printStackTrace();
			return mapping.findForward("failure");
		}
	}
}


