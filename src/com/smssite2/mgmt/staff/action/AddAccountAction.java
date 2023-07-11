package com.smssite2.mgmt.staff.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.note.common.LogUtil;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class AddAccountAction extends Action{
	IStaffService service = new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String eid=request.getParameter("EId");
		String smsCompanyName=request.getParameter("smsCompanyname");
		String regTeam=request.getParameter("regTeam");
		String regSales=request.getParameter("regSales");
		String regDirector=request.getParameter("regDirector");
		String regDate=request.getParameter("regDate");
		String regAddress=request.getParameter("regAddress");
		String regContact=request.getParameter("regContact");
		String regMobiles=request.getParameter("regMobiles");
		String regPhones=request.getParameter("regPhones");
		String regEmail=request.getParameter("regEmail");
		String smsNum=request.getParameter("smsNum");
		String smsnumlen=request.getParameter("smsnumlen");
		String routeType=request.getParameter("routeType");
		String smsSign=request.getParameter("smsSign");
		String smsBalance=request.getParameter("smsBalance");
		String regMemo=request.getParameter("regMemo");
		String messageLength=request.getParameter("messageLength");
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		try{
		String str=service.addAccount(eid,smsCompanyName,regTeam,regSales,regDirector,regDate,regAddress,regContact,regMobiles
					,regPhones,regEmail,smsNum,smsnumlen,routeType,smsSign,smsBalance,regMemo,messageLength);
			request.setAttribute("message", str);
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		}
		LogUtil.writeLog("客户设置","添加","添加新客户 企业代码："+eid,staffId,"000000");
		return mapping.findForward("success");
	}
}
