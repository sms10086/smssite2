package com.smssite2.mgmt.basicsetting.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.smssite2.mgmt.basicsetting.bean.Sift;
import com.smssite2.mgmt.basicsetting.form.BlackListForm;
import com.smssite2.mgmt.basicsetting.service.IBasicSettingService;
import com.smssite2.mgmt.basicsetting.service.impl.BasicSettingService;

public class SiftListAction extends Action{
IBasicSettingService service = new BasicSettingService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		BlackListForm siftF=(BlackListForm)form;
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		String[] strs=credit.split("\\|");
		String privilege="";
		if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("440")<0){
			request.setAttribute("message", "用户"+staffId+"没有[过滤词设置]的操作权限！");
			return mapping.findForward("err");
		}
		if(credit.trim().toLowerCase().equals("admin")){
			privilege="440:FAMDLO";
		}else{
			for(int i=1;i<strs.length;i++){
				if(strs[i].indexOf("440")>=0)privilege=strs[i];
			}
		}
		
		try{
			List<Sift> list=service.findAllSift(eid,siftF);
			request.setAttribute("feasts", list);
			request.setAttribute("pagination",siftF);
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
			request.setAttribute("privilege", privilege);
			return mapping.findForward("failure");
		}
		request.setAttribute("privilege", privilege);
		return mapping.findForward("success");
	}
}

