package com.smssite2.mgmt.message.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.smssite2.mgmt.message.bean.PhoneBean;
import com.smssite2.mgmt.message.form.LoadPhoneForm;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.note.common.DbBean;
import com.note.common.TimeUtil;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class ResendAction extends Action{
	IMessageService service=new MessageService();
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception  {
		LoadPhoneForm phoneForm=(LoadPhoneForm)form;
		String[] ids=request.getParameterValues("ids");
		String eid =((Staff)request.getSession().getAttribute("staff")).getEId();
		String sessionid=request.getSession().getId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		String routeType= ((Account)request.getSession().getAttribute("account")).getRouteType();
		String taskID=(String) request.getSession().getAttribute("taskID");
		try{
			 List<PhoneBean> list=service.findPhone(ids,routeType,eid);
			 service.deletephones_load(sessionid);
			 for(PhoneBean pb:list){
					DbBean.executeUpdate("insert into phones(ID,phone,name,content,phonetype,sessionid,eid,staffID,bornDate,taskID)" +
							" values(seq_phone.nextval,?,?,?,?,?,?,?,?,?)",new Object[]{pb.getPhone(),pb.getName(),pb.getContent(),pb.getPhoneType(),sessionid,eid,staffId,TimeUtil.now(),taskID});
				}
			 	List<PhoneBean> pageList=service.findPhones_load(sessionid, eid, staffId, phoneForm,taskID);
				request.getSession().setAttribute("phones", pageList);
				request.getSession().setAttribute("pagination", phoneForm);
		}catch(Exception e){
			request.setAttribute("messagt", e.getMessage());
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}
}
