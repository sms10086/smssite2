package com.smssite2.mgmt.staff.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.bean.Account;
import com.note.bean.Staff;
import com.note.common.LogUtil;
import com.smssite2.mgmt.staff.form.AccountForm;
import com.smssite2.mgmt.staff.service.IStaffService;
import com.smssite2.mgmt.staff.service.impl.StaffService;

public class AccountListAction extends Action{
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		IStaffService service=new StaffService();
		AccountForm accF=(AccountForm)form;
		if(accF.getOrder()==null)accF.setOrder("regDate");
		String EId= ((Staff)request.getSession().getAttribute("staff")).getEId();
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		
		String credit=((Staff)request.getSession().getAttribute("staff")).getCredit();
		if(!credit.trim().toLowerCase().equals("admin")&&credit.indexOf("Ac1")<0){
			request.setAttribute("message", "�û�"+staffId+"û��[�ͻ�����]�Ĳ���Ȩ�ޣ�");
			return mapping.findForward("err");
		}
		String[] strs=credit.split("\\|");
		String privilege="";
		if(credit.trim().toLowerCase().equals("admin")){
			privilege="Ac1:AMDS";
		}else{
			for(int i=1;i<strs.length;i++){
				if(strs[i].indexOf("Ac1")>=0)privilege=strs[i];
			}
		}
		request.setAttribute("privilege", privilege);
		
		Map map=new HashMap();
		map.put("eid", "��ҵ����");
		map.put("regTeam", "����");
		map.put("regSales", "ҵ��Ա");
		map.put("regDate", "��������");
		map.put("regaccdate", "��ֵ����");
		try{
			List<Account> list =service.findListAccount(accF,EId);
			request.setAttribute("accList",list);
			request.setAttribute("pagination", accF);
			request.setAttribute("map", map);
			request.setAttribute("order", accF.getOrder());
			LogUtil.writeLog("�ͻ�����","��ѯ","��ѯ��������ҵ����="+accF.getEid()+" �ͻ�����="+accF.getSmsCompanyname()+" ҵ��Ա="+accF.getSregSales()+" ͨ��="+accF.getRouteType()+" ����="+accF.getOrder(),staffId,"000000");
			return mapping.findForward("success");
		}catch(Exception e){
			e.printStackTrace();
			return mapping.findForward("failure");
		}
	}
}

