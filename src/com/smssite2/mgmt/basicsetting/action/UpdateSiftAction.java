package com.smssite2.mgmt.basicsetting.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.smssite2.mgmt.basicsetting.service.IBasicSettingService;
import com.smssite2.mgmt.basicsetting.service.impl.BasicSettingService;

public class UpdateSiftAction extends Action {
	IBasicSettingService basicSettingService = new BasicSettingService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try{
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return mapping.findForward("");
	}
}
