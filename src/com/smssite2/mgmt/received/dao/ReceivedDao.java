package com.smssite2.mgmt.received.dao;

import java.util.List;
import java.util.UUID;
import com.note.common.DbBean;

public class ReceivedDao {	
	private String DefaultEid = "daikin";
	private String defaultRouteType = "A";
	public boolean checkMO() {
		
		String sql = "select count(*) from received_d_wait";
		int count = DbBean.getInt(sql, null);
		if(count>0)return true;
		return false;
	}

	public String getGroupIDByPhone(String sender) {
		
		String sql = "select groupid from linkman where phone=?";
		String groupid = DbBean.getString(sql, new Object[]{sender});
		if(groupid==null)return null;
		return groupid;
	}

	public void updateReceived(String groupID, String id) {
		String sql ="update received_d set groupID=?,eid=? where id=?";
		DbBean.executeUpdate(sql, new String[]{groupID,DefaultEid,id});
	}

	public void deleteReceivedWait(String id) {
		String sql = "delete from received_d_wait where id=?";
		DbBean.executeUpdate(sql, new Object[]{id});
		
	}

	public void sendReplySms(String groupID, String sender) {
		String sql = "select replycontent from lingroup where groupid=?";
		String replycontent = DbBean.getString(sql, new Object[]{groupID});
		sendSms(sender,replycontent);
	}

	private void sendSms(String sender, String replycontent) {
		String sql = "insert into smsmt(id,eid,routetype,phone,phonetype,content) values(?,?,?,?,?,?)";
		String ID = UUID.randomUUID().toString();
		DbBean.executeUpdate(sql, new Object[]{ID,"daikin",defaultRouteType,sender,0,replycontent});
		String sql_wait = "insert into smsmt_submit(id,routetype,phonetype) values(?,?,?)";
		DbBean.executeUpdate(sql_wait, new Object[]{ID,defaultRouteType.hashCode(),0});
	}
	
}
