package com.smssite2.mgmt.basicsetting.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.smssite2.mgmt.linkman.service.ILinkmanService;
import com.smssite2.mgmt.linkman.service.impl.LinkmanService;

public class DetailSiftAction extends Action {
	ILinkmanService linkmanService = new LinkmanService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		try{
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return mapping.findForward("");
	}
}
