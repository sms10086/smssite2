package com.smssite2.mgmt.basicsetting.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.note.common.LogUtil;
import com.smssite2.mgmt.basicsetting.service.IBasicSettingService;
import com.smssite2.mgmt.basicsetting.service.impl.BasicSettingService;

public class AddFeastAction extends Action {
	IBasicSettingService service = new BasicSettingService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String feastid=request.getParameter("feastid");
		String type=request.getParameter("type");
		String content=request.getParameter("content");
		String fname=request.getParameter("fname");
		String worldDate=request.getParameter("worldDate");
		String chinadate=request.getParameter("chinadate");
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getSysUserId();
		String s="修改";
		if(content!=null&&content.length()>0)content=content.replace("\r\n", "");
		try{
			if(feastid==null||feastid.trim().equals("")) s="添加";
			String message="";
			int a=service.addFeast(eid,feastid,type,content,fname,worldDate,chinadate);
			if(a>0){
				message="成功添加或修改一个节日！";
				LogUtil.writeLog("节日设置",s,message,staffId,eid);
			}else{
				message="添加或修改节日失败！";
			}
			request.setAttribute("message", message);
	}catch(Exception e){
		request.setAttribute("message", e.getMessage());
	}
	return mapping.findForward("success");
	}
}
