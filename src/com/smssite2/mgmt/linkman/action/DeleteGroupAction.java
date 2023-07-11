package com.smssite2.mgmt.linkman.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.NoteException;
import com.note.bean.Staff;
import com.note.common.DbBean;
import com.note.common.LogUtil;
import com.smssite2.mgmt.linkman.service.ILinkmanService;
import com.smssite2.mgmt.linkman.service.impl.LinkmanService;

public class DeleteGroupAction extends Action{
	ILinkmanService service = new LinkmanService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String groupid=request.getParameter("groupid");
		String eid=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		try{
			String userid=DbBean.getString("select staffid from linGroup where eid=? and groupid=?", new String[]{eid,groupid});
			if(!staffId.equals(userid))throw new NoteException("只能删除自己的群组！");
			if(eid.equals("yiqibt")){
				String groupname=DbBean.getString("select groupname from linGroup where eid=? and groupid=?", new String[]{eid,groupid});
				if(groupname!=null&&groupname.trim().endsWith("天气预报")){
					throw new NoteException("天气预报群组为固定群组，请勿删除！");
				}
			}
			String message="";
			int a =service.delete(groupid,eid,"linGroup","groupid");
			service.changeGroups(groupid,eid, null);
			if(a>0){
				message="一个联系人群组已成功删除！";
				LogUtil.writeLog("通讯录","删除",message,staffId,eid);
			}else{
				message="删除联系人群组失败！";
			}
			request.setAttribute("message", message);
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
		}
		return mapping.findForward("success");
	}

}

