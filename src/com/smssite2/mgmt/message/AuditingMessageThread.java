package com.smssite2.mgmt.message;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.note.common.DbBean;
import com.note.common.TimeUtil;
import com.smssite2.mgmt.message.service.IMessageService;
import com.smssite2.mgmt.message.service.impl.MessageService;

public class AuditingMessageThread extends Thread{	
	private static Log log = LogFactory.getLog(AuditingMessageThread.class);
	IMessageService service=new MessageService();
	@SuppressWarnings("unchecked")
	private List pids;
	private String eid;
	private String[] ids;
	 
	private int sum=0;
	private int totalNum;
	
	private int messageLength=60;
	@SuppressWarnings("unchecked")
	public AuditingMessageThread(List pids,String[] ids, String eid,int totalNum,int messageLength){
		this.pids=pids;
		this.eid=eid;
		this.ids=ids;
		this.totalNum=totalNum;
		this.messageLength=messageLength;
		this.start();
	}
	
	public void run(){
		log.info(">>>>>>>>>>>∫ÛÃ®∂Ã–≈…Û∫À "+eid);
		if(pids!=null){
			AduitingByPid();
		}else
		if(ids!=null){
			AduitingByMessageId();
		}
	}
	@SuppressWarnings("static-access")
	public void AduitingByMessageId(){
		if(ids!=null){
			DbBean db =new DbBean();
			Connection conn=null;
			Statement stm=null;
			Statement selstm=null;
			Statement del=null;
			Statement update=null;
			ResultSet rs =null;
			String[] contents=null;
			int total=0;
			int money=0;
			
			try{
				conn=db.getConnection();
				conn.setAutoCommit(false);
				del=conn.createStatement();
				stm=conn.createStatement();
				update=conn.createStatement();
				selstm=conn.createStatement(ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);
				for(int i=0;i<ids.length;i++){
					rs=selstm.executeQuery("select * from message_d where flag=1 and messageId='"+ids[i]+"' order by sendPRI desc");
					while(rs.next()){
						contents=service.getContents(rs.getString("content"), rs.getString("smssign"),messageLength);
						if(rs.getInt("isSchedule")==0){
							total++;
							if(contents==null){
								stm.addBatch("insert into Message_Send_d(messageId,phone,name,phoneType,content,mesType," +
										"sentCount,addTime,staffId,smsNum,smsSign,EId,sendPRI,routeType)" +
										"values('"+rs.getString("messageId")+"','"+rs.getString("phone")+"','"+rs.getString("name")+"',"
										+rs.getInt("phoneType")+", '"+rs.getString("content")+"','"+rs.getString("mesType")+"',"
										+1+",'"+rs.getString("addTime")+"','"+rs.getString("staffId")+"','"+rs.getString("smsNum")
										+"','"+rs.getString("smsSign")+"','"+rs.getString("EId")+"',"+rs.getInt("sendPRI")+",'"+rs.getString("routeType")+"')");
								sum++;
								money++;
							}else{
								for(int j=0;j<contents.length;j++){
									stm.addBatch("insert into Message_Send_d(messageId,phone,name,phoneType,content,mesType," +
											"sentCount,addTime,staffId,smsNum,smsSign,EId,sendPRI,routeType)" +
											"values('"+rs.getString("messageId")+"F"+j+"','"+rs.getString("phone")+"','"+rs.getString("name")+"',"
											+rs.getInt("phoneType")+", '"+contents[j].replace("'", "''")+"','"+rs.getString("mesType")+"',"
											+1+",'"+rs.getString("addTime")+"','"+rs.getString("staffId")+"','"+rs.getString("smsNum")
											+"','"+rs.getString("smsSign")+"','"+rs.getString("EId")+"',"+rs.getInt("sendPRI")+",'"+rs.getString("routeType")+"')");
								sum++;
								money++;
								}
							}
							del.addBatch("delete from message_d where id="+rs.getInt("id"));
							if(total%500==0){
								log.info(">>>>"+TimeUtil.now());
								stm.executeBatch();
								stm.clearBatch();
								del.executeBatch();
								del.clearBatch();
								update.executeQuery("update Sms_Basicinf set smsBalance=smsBalance-"+money+" where eid='"+eid+"'");
								conn.commit();
								money=0;
							}
							
						}else{
							 
							total++;
							if(contents==null){
								stm.addBatch("insert into Message_Schedule_d(messageId,phone,name,phoneType,content,mesType," +
									"sentCount,addTime,staffId,smsNum,smsSign,EId,sendPRI,routeType,needSendTime)" +
									"values('"+rs.getString("messageId")+"','"+rs.getString("phone")+"','"+rs.getString("name")+"',"
									+rs.getInt("phoneType")+", '"+rs.getString("content")+"','"+rs.getString("mesType")+"',"
									+1+",'"+rs.getString("addTime")+"','"+rs.getString("staffId")+"','"+rs.getString("smsNum")
									+"','"+rs.getString("smsSign")+"','"+rs.getString("EId")+"',"+rs.getInt("sendPRI")+",'"+rs.getString("routeType")+"','"+rs.getString("needSendTime")+"')");
								sum++;
								money++;
							}else{
								for(int j=0;j<contents.length;j++){
									stm.addBatch("insert into Message_Schedule_d(messageId,phone,name,phoneType,content,mesType," +
											"sentCount,addTime,staffId,smsNum,smsSign,EId,sendPRI,routeType,needSendTime)" +
											"values('"+rs.getString("messageId")+"F"+j+"','"+rs.getString("phone")+"','"+rs.getString("name")+"',"
											+rs.getInt("phoneType")+", '"+contents[j].replace("'", "''")+"','"+rs.getString("mesType")+"',"
											+1+",'"+rs.getString("addTime")+"','"+rs.getString("staffId")+"','"+rs.getString("smsNum")
											+"','"+rs.getString("smsSign")+"','"+rs.getString("EId")+"',"+rs.getInt("sendPRI")+",'"+rs.getString("routeType")+"','"+rs.getString("needSendTime")+"')");
									sum++;
									money++;
								}
							}
							del.addBatch("delete from message_d where id="+rs.getInt("id"));
							if(total%500==0){
								log.info(">>>>"+TimeUtil.now());
								stm.executeBatch();
								stm.clearBatch();
								del.executeBatch();
								del.clearBatch();
								update.executeQuery("update Sms_Basicinf set smsBalance=smsBalance-"+money+" where eid='"+eid+"'");
								conn.commit();
								money=0;
							}
						}
					}
					stm.executeBatch();
					del.executeBatch();
					update.executeQuery("update Sms_Basicinf set smsBalance=smsBalance-"+money+" where eid='"+eid+"'");
					conn.commit();
					money=0;
					sum++;
					
				}
				conn.setAutoCommit(true);
			}catch(Exception e){
				e.printStackTrace();
				try {
					conn.rollback();
				} catch (SQLException e1) {
				}
				
			}finally{
				db.close(rs);
				db.close(stm, del);
				db.close(selstm);
				db.close(conn);
			}
		}
	}
	@SuppressWarnings("static-access")
	public void AduitingByPid(){
		if(pids!=null){
			DbBean db =new DbBean();
			Connection conn=null;
			Statement stm=null;
			Statement del=null;
			Statement selstm=null;
			Statement update=null;
			ResultSet rs =null;
			String[] contents=null;
			int total=0;
			int money=0;
			
			try{
				conn=db.getConnection();
				conn.setAutoCommit(false);
				del=conn.createStatement();
				update=conn.createStatement();
				selstm=conn.createStatement(ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);
				stm=conn.createStatement();
				for(int i=0;i<pids.size();i++){
					rs=selstm.executeQuery("select * from message_d where flag=1 and pid="+pids.get(i)+" order by sendPRI desc");
					while(rs.next()){
						contents=service.getContents(rs.getString("content"), rs.getString("smssign"),messageLength);
						if(rs.getInt("isSchedule")==0){
							total++;
							if(contents==null){
								stm.addBatch("insert into Message_Send_d(messageId,phone,name,phoneType,content,mesType," +
										"sentCount,addTime,staffId,smsNum,smsSign,EId,sendPRI,routeType)" +
										"values('"+rs.getString("messageId")+"','"+rs.getString("phone")+"','"+rs.getString("name")+"',"
										+rs.getInt("phoneType")+", '"+rs.getString("content")+"','"+rs.getString("mesType")+"',"
										+1+",'"+rs.getString("addTime")+"','"+rs.getString("staffId")+"','"+rs.getString("smsNum")
										+"','"+rs.getString("smsSign")+"','"+rs.getString("EId")+"',"+rs.getInt("sendPRI")+",'"+rs.getString("routeType")+"')");
								sum++;
								money++;
							}else{
								for(int j=0;j<contents.length;j++){
									stm.addBatch("insert into Message_Send_d(messageId,phone,name,phoneType,content,mesType," +
											"sentCount,addTime,staffId,smsNum,smsSign,EId,sendPRI,routeType)" +
											"values('"+rs.getString("messageId")+"F"+j+"','"+rs.getString("phone")+"','"+rs.getString("name")+"',"
											+rs.getInt("phoneType")+",'"+contents[j].replace("'", "''")+"','"+rs.getString("mesType")+"',"
											+1+",'"+rs.getString("addTime")+"','"+rs.getString("staffId")+"','"+rs.getString("smsNum")
											+"','"+rs.getString("smsSign")+"','"+rs.getString("EId")+"',"+rs.getInt("sendPRI")+",'"+rs.getString("routeType")+"')");
								sum++;
								money++;
								}
							}
							del.addBatch("delete from message_d where id="+rs.getInt("id"));
							if(total%500==0){
								stm.executeBatch();
								del.executeBatch();
								update.executeQuery("update Sms_Basicinf set smsBalance=smsBalance-"+money+" where eid='"+eid+"'");
								conn.commit();
								money=0;
							}
							
						}else{
							total++;
							if(contents==null){
								stm.addBatch("insert into Message_Schedule_d(messageId,phone,name,phoneType,content,mesType," +
									"sentCount,addTime,staffId,smsNum,smsSign,EId,sendPRI,routeType,needSendTime)" +
									"values('"+rs.getString("messageId")+"','"+rs.getString("phone")+"','"+rs.getString("name")+"',"
									+rs.getInt("phoneType")+", '"+rs.getString("content")+"','"+rs.getString("mesType")+"',"
									+1+",'"+rs.getString("addTime")+"','"+rs.getString("staffId")+"','"+rs.getString("smsNum")
									+"','"+rs.getString("smsSign")+"','"+rs.getString("EId")+"',"+rs.getInt("sendPRI")+",'"+rs.getString("routeType")+"','"+rs.getString("needSendTime")+"')");
								sum++;
								money++;
							}else{
								for(int j=0;j<contents.length;j++){
									stm.addBatch("insert into Message_Schedule_d(messageId,phone,name,phoneType,content,mesType," +
											"sentCount,addTime,staffId,smsNum,smsSign,EId,sendPRI,routeType,needSendTime)" +
											"values('"+rs.getString("messageId")+"F"+j+"','"+rs.getString("phone")+"','"+rs.getString("name")+"',"
											+rs.getInt("phoneType")+", '"+contents[j].replace("'", "''")+"','"+rs.getString("mesType")+"',"
											+1+",'"+rs.getString("addTime")+"','"+rs.getString("staffId")+"','"+rs.getString("smsNum")
											+"','"+rs.getString("smsSign")+"','"+rs.getString("EId")+"',"+rs.getInt("sendPRI")+",'"+rs.getString("routeType")+"','"+rs.getString("needSendTime")+"')");
									sum++;
									money++;
								}
							}
							del.addBatch("delete from message_d where id="+rs.getInt("id"));
							if(total%500==0){
								stm.executeBatch();
								del.executeBatch();
								update.executeQuery("update Sms_Basicinf set smsBalance=smsBalance-"+money+" where eid='"+eid+"'");
								conn.commit();
								money=0;
							}
						}
						stm.executeBatch();
						del.executeBatch();
						update.executeQuery("update Sms_Basicinf set smsBalance=smsBalance-"+money+" where eid='"+eid+"'");
						conn.commit();
						money=0;
					}
					conn.commit();
					sum++;
					
				}
				conn.setAutoCommit(true);
			}catch(Exception e){
				e.printStackTrace();
				try {
					conn.rollback();
				} catch (SQLException e1) {
				}
				
			}finally{
				db.close(rs);
				db.close(stm, del);
				db.close(update,selstm );
				db.close(conn);
			}
		}
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	 

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}
}
