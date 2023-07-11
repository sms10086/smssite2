package com.note.common;

import java.sql.Timestamp;
import com.note.NoteException;

public class LogUtil {	
	@SuppressWarnings("static-access")
	public static  void writeLog(String menuItem,String actionType,String actionLog,String staffId,String EId)throws NoteException{
		
		if (staffId == null) throw new NoteException("staffId is null");
		if (EId == null) throw new NoteException("companId is null");
		if (actionType == null) throw new NoteException("log message is null");
		DbBean db = new DbBean();
		try{
			String sql="insert into smslog(logTime,menuItem,actionType,actionLog,staffId,EId) values(?,?,?,?,?,?)";
			Object[] params=new Object[]{(new Timestamp(System.currentTimeMillis())).toString(),menuItem,actionType,actionLog,staffId,EId};
			db.executeUpdate(sql, params);
		}
		catch(Exception e){
			db.rollback();
			throw new NoteException(e.getMessage(),e);
		}
	}
}
