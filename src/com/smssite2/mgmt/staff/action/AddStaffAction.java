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

public class AddStaffAction extends Action{
	IStaffService service = new StaffService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		String flag=request.getParameter("flag");
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String userId=request.getParameter("txtUserid");
		String userName=request.getParameter("txtUsername");
		String password=request.getParameter("txtPwd");
		String uteam=request.getParameter("uteam");
		String upost= request.getParameter("upost");
		String umemo=request.getParameter("txtMemo");
		String smsNum=request.getParameter("txtSmsnum");
		String smsSign=request.getParameter("txtSmssign");
		String isAdmin=request.getParameter("chkadmin");
		String lockSign=request.getParameter("Chklocksign");
		String groupID = request.getParameter("groupID");
		
		String[] opBox=request.getParameterValues("opBox");
		String credit="";
		if(opBox!=null){
			for(int i=0;i<opBox.length;i++){
				credit=credit+opBox[i];
			}
		}
	
		if(isAdmin!=null&&isAdmin.equals("1"))credit="admin";
		try{
		service.addStaff(EId,userId,userName,password,uteam,upost,umemo,smsNum,smsSign,isAdmin,credit,lockSign,groupID,flag);
		
		if(flag.equals("add")){
			LogUtil.writeLog("用户设置","添加","添加用户：用户名称="+userName+" 用户账号="+userId,((Staff)request.getSession().getAttribute("staff")).getUserId(),EId);
			request.setAttribute("message", "添加用户成功");
		}
		else {
			LogUtil.writeLog("用户设置","修改","修改用户：用户名称="+userName+" 用户账号="+userId,((Staff)request.getSession().getAttribute("staff")).getUserId(),EId);
			request.setAttribute("message", "修改用户"+userId+" 成功");
		}
		return mapping.findForward("success");
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		}
		
	}
}
