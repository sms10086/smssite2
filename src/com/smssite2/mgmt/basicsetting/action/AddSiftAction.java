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

public class AddSiftAction extends Action {
	IBasicSettingService service = new BasicSettingService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String siftid=request.getParameter("siftid");
			String content=request.getParameter("content");
			String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
			String staffId=((Staff)request.getSession().getAttribute("staff")).getSysUserId();
			String s="�޸�";
			try{
				if(siftid==null||siftid.trim().equals("")) s="���";
				String message="";
				int a=service.addsift(eid,siftid,content,staffId);
				if(a>0){
					message="�ɹ���ӻ��޸�һ����¼��";
					LogUtil.writeLog("���˴�����",s,message,staffId,eid);
				}else{
					message="��ӻ��޸Ĺ��˴�ʧ�ܣ�";
				}
				request.setAttribute("message", message);
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
		}
		return mapping.findForward("success");
	}
}
