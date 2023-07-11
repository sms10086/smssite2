package com.smssite2.mgmt.basicsetting.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.smssite2.mgmt.basicsetting.bean.Black;
import com.note.bean.Staff;
import com.smssite2.mgmt.basicsetting.form.BlackListForm;
import com.smssite2.mgmt.basicsetting.service.IBasicSettingService;
import com.smssite2.mgmt.basicsetting.service.impl.BasicSettingService;

public class BlackListAction extends Action{
IBasicSettingService service = new BasicSettingService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		BlackListForm blackF=(BlackListForm)form;
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("430")<0){
			request.setAttribute("message", "用户"+staffId+"没有[黑名单设置]的操作权限！");
			return mapping.findForward("err");
		}
		String[] strs=credit.split("\\|");
		String privilege="";
		if(credit.trim().toLowerCase().equals("admin")){
			privilege="430:FAMDLO";
		}else{
			for(int i=1;i<strs.length;i++){
				if(strs[i].indexOf("430")>=0)privilege=strs[i];
			}
		}
		request.setAttribute("privilege", privilege);
		try{
			List<Black> list=service.findAllBlack(eid,blackF);
			request.setAttribute("feasts", list);
			request.setAttribute("pagination",blackF);
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}
}
