package com.smssite2.mgmt.linkman.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.note.common.LogUtil;
import com.smssite2.mgmt.linkman.service.ILinkmanService;
import com.smssite2.mgmt.linkman.service.impl.LinkmanService;

public class AddLinkmanAction extends Action{
	ILinkmanService service = new LinkmanService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		String linkid=request.getParameter("linkid");
		String name=request.getParameter("name");
		String phone=request.getParameter("phone");
		String groupid=request.getParameter("groupid");
		String sex=request.getParameter("sex");
		String birthday=request.getParameter("birthday");
		String post=request.getParameter("post");
		String orgName=request.getParameter("orgName");
		String userMemo=request.getParameter("userMemo");
		String optionalContent=request.getParameter("optionalContent");
		String s="修改";
		try{
			if(linkid==null||linkid.trim().length()==0) s="添加";
			String message="";
			int a=service.addLinkman(eid,staffId,linkid,name,phone,groupid,sex,birthday,post,orgName,userMemo,optionalContent);
			if(a>0){
				message="成功"+s+"一个联系人！";
				LogUtil.writeLog("通讯录",s,message,staffId,eid);
			}else{
				message=s+"联系人失败！";
			}
			request.setAttribute("message", message);
	}catch(Exception e){
		request.setAttribute("message", e.getMessage());
	}
	return mapping.findForward("success");
}
}
