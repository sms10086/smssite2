package com.smssite2.mgmt.message.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Staff;
import com.smssite2.mgmt.basicsetting.bean.Feast;
import com.smssite2.mgmt.message.form.FeastListForm;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class ChoseFeastAction extends Action {	private static Log log = LogFactory.getLog(ChoseFeastAction.class);
	IMessageService service= new MessageService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		FeastListForm feastList=(FeastListForm)form;
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("420")<0){
			request.setAttribute("message", "用户"+staffId+"没有[节日列表]的操作权限！");
			return mapping.findForward("err");
		}
		String[] strs=credit.split("\\|");
		String privilege="";
		if(credit.trim().toLowerCase().equals("admin")){
			privilege="420:FAMDLO";
		}else{
			for(int i=1;i<strs.length;i++){
				if(strs[i].indexOf("420")>=0)privilege=strs[i];
			}
		}
		request.setAttribute("privilege", privilege);
		
		try{
			List<Feast> list=service.findAllFeast(EId,feastList);
			request.setAttribute("feasts", list);
			request.setAttribute("pagination",feastList);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return mapping.findForward("success");
	}
}

