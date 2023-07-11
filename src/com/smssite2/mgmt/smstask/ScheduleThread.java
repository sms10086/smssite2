package com.smssite2.mgmt.smstask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.note.common.DbBean;
import com.note.common.StringUtil;
import com.note.common.TimeUtil;
import com.note.common.UUIDUtil;
import com.smssite2.mgmt.listener.WorkThread;
import com.smssite2.mgmt.message.ContentUtil;

public class ScheduleThread extends WorkThread{
	private static final Log LOG = LogFactory.getLog(ScheduleThread.class);
	private  boolean isbreak=false;
	private boolean isbig=false;
 
	public ScheduleThread(boolean isbig ){
		this.isbig=isbig;
	 
	}
	@Override
	public void work() throws Exception {
		 
		LOG.info(">>>>>>>>>>>>>>>>>>>ScheduleThread begin!isbig="+isbig);
		 
		 while(isWork()){
			 try{
					MTDate();
					sleep(1000);
				}catch(Exception e){
					LOG.error(e.getMessage(),e);
					sleep(1000);
				}
		 }
		 
			 LOG.info(">>>>>>>>>>>>>>>>>>>ScheduleThread end!isbig="+isbig);
		  
	}
	
	public void MTDate(){
		Connection conn=null;
		PreparedStatement prep = null;
		ResultSet taskRs = null;
		PreparedStatement phonePrep = null;
		 
		ResultSet phoneRs=null;
		PreparedStatement insertSmsMT = null;
		PreparedStatement insertSubmit = null;
		PreparedStatement updateSubmit=null;
		Statement taskDel = null;
		Statement phoneDel=null;
		DbBean db =new DbBean();
		try{
			conn=db.getConnection(true);
				if(isbig)
					prep=conn.prepareStatement("select * from task where flag=1 and scheduleTime<=?   and phoneNum>=20000 order by phonenum asc",ResultSet.TYPE_FORWARD_ONLY,
								ResultSet.CONCUR_READ_ONLY);
				else
					prep=conn.prepareStatement("select * from task where flag=1 and scheduleTime<=?   and phoneNum<20000 order by phonenum asc",ResultSet.TYPE_FORWARD_ONLY,
							ResultSet.CONCUR_READ_ONLY);
			 
			prep.setTimestamp(1, TimeUtil.now());
			taskRs=prep.executeQuery();
			phonePrep=conn.prepareStatement("select * from phones where taskID=? ",ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			taskDel = conn.createStatement();
			phoneDel=conn.createStatement();
			insertSmsMT = conn
				.prepareStatement("insert into SMSMT(ID,eid,routeType,staffID,phone,phoneType, name,result,content,smsNum,smssign,addTime,messageId, mesType,isdelete,priority) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			insertSubmit = conn
				.prepareStatement("insert into SMSMT_SUBMIT(ID,routeType,phonetype,PRIORITY ) values(?,?,?,?) ");
			updateSubmit=conn.prepareStatement("update Sms_Basicinf set smsBalance=smsBalance-? where eid=?");
			while(taskRs.next()){
				try{
					if(!isWork()) {
						break;
					}
					phonePrep.setString(1, taskRs.getString("ID"));
					phoneRs=phonePrep.executeQuery();
					isbreak=false;
					insert(phoneRs,taskRs,insertSmsMT,insertSubmit,conn,phoneDel,updateSubmit);
					if(isbreak){
						break;
					}
					taskDel.executeUpdate("delete from task where ID='"
							+ taskRs.getString("ID") + "'");
				}catch(Exception e){
					LOG.error(e.getMessage(), e);
				}
			}
			
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			 
		}finally{
			db.close(taskRs, prep);
			db.close(phoneRs, phonePrep);
			db.close(insertSmsMT, insertSubmit);
			db.close(updateSubmit,taskDel);
			db.close(phoneDel);
			db.close(conn);
		}
	}
	public int insert(ResultSet phoneRs, ResultSet taskRs,
			PreparedStatement insertSmsMT, PreparedStatement insertSubmit,
			Connection conn, Statement destDel,PreparedStatement updateSubmit) throws Exception{
		int total = 0;
		int sum=0;
		String ID = null;
		Timestamp time = TimeUtil.now();
	
		String sign="";
		String content="";
		int priority=ContentUtil.getSendPRI(2, taskRs.getInt("phoneNum"));
		String[] contents=null;
		 
			if(!StringUtil.isEmpty(taskRs.getString("smsSign"))){
				sign=taskRs.getString("smsSign").trim();
			}
			while (phoneRs.next()) {
				content = taskRs.getString("content");
				total+=taskRs.getInt("splitCount");
				if(!StringUtil.isEmpty(content)){
					content=content.replace("{姓名}",
							StringUtil.getString(phoneRs.getString("name")))
							.replace(
									"{手机号码}",
									StringUtil
											.getString(phoneRs.getString("phone")))
							.replace(
									"{可选短信内容}",
									StringUtil
											.getString(phoneRs.getString("content")));
				}
				contents=ContentUtil.getContents(content, sign, taskRs.getInt("Meslength"));
				for(String con:contents){
					ID = UUIDUtil.generate();
					insertSmsMT.setString(1, ID);
					insertSmsMT.setString(2, taskRs.getString("eid"));
					insertSmsMT.setString(3, taskRs.getString("routeType"));
					insertSmsMT.setString(4, taskRs.getString("staffID"));
					insertSmsMT.setString(5, phoneRs.getString("phone"));
					insertSmsMT.setInt(6, phoneRs.getInt("phoneType"));
					 
					insertSmsMT.setString(7, phoneRs.getString("name"));
					insertSmsMT.setInt(8, -1);
					insertSmsMT.setString(9, con);
					insertSmsMT.setString(10, StringUtil.getString(taskRs.getString("smsnum")));
					insertSmsMT.setString(11, sign);
					insertSmsMT.setTimestamp(12, TimeUtil.now());
					insertSmsMT.setString(13, taskRs.getString("ID"));
					insertSmsMT.setString(14, taskRs.getString("mesType"));
					insertSmsMT.setInt(15, 0);
					insertSmsMT.setInt(16, priority);
					insertSmsMT.addBatch();
	
					insertSubmit.setString(1, ID);
					insertSubmit.setInt(2, taskRs.getString("routeType").hashCode());
					if(phoneRs.getInt("phoneType")==0){
						insertSubmit.setInt(3, 0);
					}else{
						insertSubmit.setInt(3, 1);
					}
					insertSubmit.setInt(4, priority);
					insertSubmit.addBatch();
					sum++;
				}
				destDel.addBatch("delete from phones where ID="+phoneRs.getInt("ID"));
				if (total % 50 == 0) {
					insertSmsMT.executeBatch();
					insertSubmit.executeBatch();
					destDel.executeBatch();
					if((sum-total)!=0){
						updateSubmit.setInt(1, sum-total);
						updateSubmit.setString(2,taskRs.getString("eid"));
						updateSubmit.executeUpdate();
						total=0;
						sum=0;
					}
					LOG.info(">>>>scheduletime submit 50 tiao.>>>>"+System.currentTimeMillis());
					sleep(10);
				}
				if(!isWork()) {
					isbreak=true;
					break;
				}
			}
			insertSmsMT.executeBatch();
			insertSubmit.executeBatch();
			destDel.executeBatch();
			if((sum-total)!=0){
				updateSubmit.setInt(1, sum-total);
				updateSubmit.setString(2,taskRs.getString("eid"));
				updateSubmit.executeUpdate();
				total=0;
				sum=0;
			}
		return sum;
	}

}
