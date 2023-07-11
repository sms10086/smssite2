package com.smssite2.mgmt.staff.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.http.HttpSession;
import com.note.NoteException;
import com.note.bean.Account;
import com.note.bean.Staff;

public class AdminLogin {	private static StaffService  staffService = new StaffService();
	public static String Login(String Eid,String username,String pwd,HttpSession session){
		String URL="";
		
		Calendar cale = Calendar.getInstance();
		SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd");
		String time=smd.format(cale.getTime());
	
			Account account;
			try {
				String feastname=staffService.findFeastName(Eid,time);
				account = staffService.findAccount(Eid);
				Staff staff=staffService.login(Eid,username,pwd);
				session.setAttribute("staff", staff);
				session.setAttribute("account", account);
				session.setAttribute("feastName", feastname);
				URL="/index.jsp";
			} catch (NoteException e) {
				e.printStackTrace();
				URL="/login.jsp";
			}
		return URL;
	}
}
