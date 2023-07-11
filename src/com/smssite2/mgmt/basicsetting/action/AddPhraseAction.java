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

public class AddPhraseAction extends Action {
	IBasicSettingService service = new BasicSettingService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String phraseid=request.getParameter("phraseid");
			String type=request.getParameter("type");
			String content=request.getParameter("content");
			String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
			String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
			String s="修改";
			if(content!=null&&content.length()>0)content=content.replace("\r\n", "");
			try{
				if(phraseid==null||phraseid.trim().equals("")) s="添加";
				String message="";
				int a=service.addPhrase(eid,phraseid,type,content,staffId);
				if(a>0){
					message="成功添加或修改一条短语！";
					LogUtil.writeLog("常用短语设置",s,message,staffId,eid);
				}else{
					message="添加或修改短语失败！";
				}
				request.setAttribute("message", message);
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
		}
		return mapping.findForward("success");
	}
}
