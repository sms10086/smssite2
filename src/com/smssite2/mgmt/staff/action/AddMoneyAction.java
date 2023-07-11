package com.smssite2.mgmt.staff.action;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.note.common.LogUtil;
import com.smssite2.mgmt.staff.dao.IStaffDao;
import com.smssite2.mgmt.staff.dao.impl.StaffDao;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class AddMoneyAction extends Action{
	IStaffService service = new StaffService();
	IStaffDao dao=new StaffDao();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String eid =request.getParameter("EId");
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		String addNum =request.getParameter("addNum");
		String price =request.getParameter("price");
		String moneyCount=request.getParameter("moneyCount");
		String addMome=request.getParameter("addMome");
		Map orgs=new HashMap();
		Map route=new HashMap();
		Account account=null;
		try{
			orgs=dao.findAllOrg("000000");
			route=dao.findAllRoute();
			account=service.findAccount(eid);
			String str=service.addMoney(eid,addNum,price,moneyCount,addMome,staffId);
			
			request.setAttribute("message", str);
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		}
		request.setAttribute("orgs", orgs);
		request.setAttribute("routes", route);
		request.setAttribute("account", account);
		LogUtil.writeLog("客户充值","申请","客户："+eid+"充值金额："+moneyCount+"短信条数："+addNum,staffId,"000000");
		return mapping.findForward("success");
	}
}
