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

public class DeletePhraseAction extends Action {
	IBasicSettingService service = new BasicSettingService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] ids=request.getParameterValues("ids");
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getSysUserId();
		try{
			String message="";
			int a =service.delete(ids,eid,"phrase","phraseid");
			if(a>0){
				message=a+"Ìõ¶ÌÓïÒÑ³É¹¦É¾³ı£¡";
				LogUtil.writeLog("³£ÓÃ¶ÌÓïÉèÖÃ","É¾³ı",message,staffId,eid);
			}else{
				message="É¾³ı¶ÌÓïÊ§°Ü£¡";
			}
			request.setAttribute("message", message);
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
		}
		return mapping.findForward("success");
	}
}
