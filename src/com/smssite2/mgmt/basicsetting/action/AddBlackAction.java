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

public class AddBlackAction extends Action {IBasicSettingService service = new BasicSettingService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String phraseid=request.getParameter("blackid");
			String type=request.getParameter("phone");
			String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
			String staffId=((Staff)request.getSession().getAttribute("staff")).getSysUserId();
			String s="�޸�";
			try{
				if(phraseid==null||phraseid.trim().equals("")) s="���";
				String message="";
				int a=service.addBlack(eid,phraseid,type,staffId);
				if(a>0){
					message="�ɹ���ӻ��޸�һ����¼��";
					LogUtil.writeLog("����������",s,message,staffId,eid);
				}else{
					message="��ӻ��޸ĺ�����ʧ�ܣ�";
				}
				request.setAttribute("message", message);
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
		}
		return mapping.findForward("success");
	}
}
