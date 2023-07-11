package com.smssite2.mgmt.linkman.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.note.common.LogUtil;
import com.smssite2.mgmt.linkman.service.ILinkmanService;
import com.smssite2.mgmt.linkman.service.impl.LinkmanService;

public class AddGroupAction extends Action{
	ILinkmanService service = new LinkmanService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		String groupid=request.getParameter("groupid");
		String groupname=request.getParameter("groupname");
		String replyContent = request.getParameter("replycontent");
		String isShare=request.getParameter("isShare");
		String s="�޸�";
		try{
			if(groupid==null||groupid.trim().equals("")) s="���";
			String message="";
			int a=service.addLinGroup(eid,groupid,groupname,staffId,isShare,replyContent);
			if(a>0){
				message="�ɹ���ӻ��޸�һ����ϵ��Ⱥ�����ã�";
				LogUtil.writeLog("ͨѶ¼",s,message,staffId,eid);
			}else{
				message="��ӻ��޸���ϵ��Ⱥ��ʧ�ܣ�";
			}
			request.setAttribute("message", message);
	}catch(Exception e){
		request.setAttribute("message", e.getMessage());
	}
	return mapping.findForward("success");
}
}
