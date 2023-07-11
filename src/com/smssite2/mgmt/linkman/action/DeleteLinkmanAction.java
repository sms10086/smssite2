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

public class DeleteLinkmanAction extends Action{	ILinkmanService service = new LinkmanService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String linkids=request.getParameter("linkids");
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		String[] ids =linkids.split(";");
		String flag=request.getParameter("flag");
		String groupid=request.getParameter("groupid");
		Staff staff=(Staff) request.getSession().getAttribute("staff");
		try{
			String message="";
			int a =service.deleteLinkman(ids,eid,flag,groupid,staff.getUserId());
			if(a>0){
				if(flag!=null&&!flag.trim().equals("all")){
					message="�ѳɹ�ɾ��"+a+"����ϵ�ˣ�";
				}else{
					if(groupid!=null&&!groupid.trim().equals("0"))message="�ɹ�ɾ����Ⱥ���е�������ϵ��";
					else message="ɾ��"+a+"����ϵ�˳ɹ�!";
				}
				LogUtil.writeLog("ͨѶ¼","ɾ��",message,staffId,eid);
			}else{
				message="ɾ����ϵ��ʧ�ܣ�";
			}
			request.setAttribute("message", message);
			request.setAttribute("groupid", groupid);
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
		}
		return mapping.findForward("success");
	}

}

