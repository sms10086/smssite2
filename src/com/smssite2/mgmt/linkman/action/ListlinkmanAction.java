package com.smssite2.mgmt.linkman.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.smssite2.mgmt.linkman.bean.Linkman;
import com.note.bean.Staff;
import com.smssite2.mgmt.linkman.form.LinkmanListForm;
import com.smssite2.mgmt.linkman.service.ILinkmanService;
import com.smssite2.mgmt.linkman.service.impl.LinkmanService;

public class ListlinkmanAction extends Action{	
	ILinkmanService service = new LinkmanService();

	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		LinkmanListForm manF=(LinkmanListForm)form;
		String groupid=request.getParameter("groupid");
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		String[] strs=credit.split("\\|");
		String privilege="";
		if(credit.trim().toLowerCase().equals("admin")){
			privilege="210:AMDLO";
		}else{
			for(int i=1;i<strs.length;i++){
				if(strs[i].indexOf("210")>=0)privilege=strs[i];
			}
		}
		request.setAttribute("privilege", privilege);
		try{
			List<Linkman> list=service.findAllLinkman(manF,eid,groupid,staffId,"find");
			request.setAttribute("mans", list);
			request.setAttribute("pagination", manF);
			request.setAttribute("groupid", groupid);
		}catch(Exception e){
			return mapping.findForward("failuer");
		}
		return mapping.findForward("success");
	}
}
