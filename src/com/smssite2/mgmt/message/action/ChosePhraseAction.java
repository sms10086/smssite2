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
import com.smssite2.mgmt.message.form.PhraseListForm;
import com.note.bean.Phrase;
import com.note.bean.Staff;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class ChosePhraseAction extends Action{	private static Log log = LogFactory.getLog(ChosePhraseAction.class);
	IMessageService service= new MessageService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		PhraseListForm phraseList=(PhraseListForm)form;
		String EId=((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("410")<0){
			request.setAttribute("message", "用户"+staffId+"没有[短语列表]的操作权限！");
			return mapping.findForward("err");
		}
		String[] strs=credit.split("\\|");
		String privilege="";
		if(credit.trim().toLowerCase().equals("admin")){
			privilege="410:FAMDLO";
		}else{
			for(int i=1;i<strs.length;i++){
				if(strs[i].indexOf("410")>=0)privilege=strs[i];
			}
		}
		request.setAttribute("privilege", privilege);
		
		try{
			List<Phrase> list=service.findAllPhrase(EId,phraseList,staffId);
			request.setAttribute("phrases", list);
			request.setAttribute("pagination",phraseList);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return mapping.findForward("success");
	}
}
