package com.smssite2.mgmt.message.action;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.note.NoteException;
import com.note.bean.Account;
import com.note.bean.CityBean;
import com.note.bean.Staff;
import com.note.common.DbBean;
import com.note.common.LogUtil;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class SendWeatherAction extends Action{
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Account account=(Account) request.getSession().getAttribute("account");
		String staffId=((Staff)request.getSession().getAttribute("staff")).getUserId();
		IMessageService service=new MessageService();
		ArrayList<CityBean> citys=(ArrayList<CityBean>) request.getSession().getAttribute("citys");
		try{
			if(!account.getEId().equals("yiqibt")){
				throw new NoteException("����Ȩʹ�øù���");
			}
			if(!staffId.trim().toLowerCase().equals("admin")){
				throw new NoteException("����Ԥ��ֻ����admin�û�����");
			}
			ArrayList<CityBean> cityList=new ArrayList();
			cityList=DbBean.select("select name,spell,code from city where stauts=0 order by code", null, 0, Integer.MAX_VALUE, CityBean.class);
			HashMap map=new HashMap();
			for(CityBean city:cityList){
				map.put(city.getName()+"����Ԥ��", request.getParameter(city.getCode()));
			}
			service.sendWeather(map,account,staffId);
			request.setAttribute("list", citys);
			request.setAttribute("message"	,"��������Ԥ���ɹ�");
			LogUtil.writeLog("����Ԥ��","����","��������Ԥ���ɹ�",staffId,account.getEId());
		}catch(Exception e){
			 
			request.setAttribute("list", citys);
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}
}
