package com.smssite2.mgmt.message;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.note.common.DbBean;
import com.note.common.TimeUtil;
import com.smssite2.mgmt.message.bean.SiftBean;
import com.smssite2.mgmt.message.service.impl.MessageIdCheck;

public class sendMessageThread extends Thread{	private static Log log = LogFactory.getLog(sendMessageThread.class);
	
	private  int num=60;
	
	private String sessionid;
	private List<SiftBean> sifts=new ArrayList<SiftBean>();
	private String blackPhone;
	private   int success=0;
	private int total=0;
	private String chkusevar;
	private String content;
	private String mesType;
	
	private String userId;
	private String smsNum;
	private String smsSign;
	private String EId;
	private String isSchedule;
	private String routeType;
	private String needSendTime;
	private long pid;
	private int state;
	public void del(){
		 
		DbBean.executeUpdate("delete from phones_load where sessionid='"+sessionid+"' and state="+state+"", null);
	}
	public sendMessageThread(String content, String mesType,
			 String userId, String smsNum, String smsSign,
			String EId, String isSchedule, String routeType,
			String needSendTime, String sessionid,String chkusevar,String blackPhone,List<SiftBean> sifts,int state,int messageLength){
		this.sessionid=sessionid;
		this.blackPhone=blackPhone;
		this.chkusevar=chkusevar;
		this.content=content+"";
		this.mesType=mesType;
		this.userId=userId;
		this.smsNum=smsNum;
		this.smsSign=smsSign+"";
		this.EId=EId;
		this.isSchedule=isSchedule;
		this.routeType=routeType;
		this.needSendTime=needSendTime;
		this.sifts=sifts;
		this.success=0;
		this.pid=MessageIdCheck.getPId();
		this.state=state;
		this.num=messageLength;
		this.start();
	}
	public void run(){
		log.info(">>>>>>>>>>>后台短信生成线程 "+userId+" "+EId);
		this.sendMessage();
		this.del();
	}
	public void sendMessage(){
		DbBean db= new DbBean();
		Connection conn = null;
		Statement stm=null;
		Statement stm2=null;
		Statement totalStm=null;
		ResultSet rs=null;
		ResultSet totalRs=null;
		int sendCount=0;
		if(smsSign!=null&&smsSign.trim().length()>0){
			num=num-smsSign.length();
		}
		if(chkusevar!=null&&chkusevar.equals("1")){
			String content1=content;
			try{
				conn = db.getConnection();
				conn.setAutoCommit(false);
				stm=conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
				stm2=conn.createStatement();
				totalStm=conn.createStatement();
				totalRs=totalStm.executeQuery("select count(*) from (select distinct phone,name,content,phonetype from phones_load where sessionid='"+sessionid+"' and state="+state+" ) ");
				if(totalRs.next())
				total=totalRs.getInt(1);
				rs=stm.executeQuery("select distinct phone,name,content,phonetype from phones_load where sessionid='"+sessionid+"' and state="+state+" ");
				log.debug(TimeUtil.now());
				String phone="";
				String name="";
				String co="";
				int pt=0;
					while(rs.next()){
						phone=rs.getString("phone");
						name=rs.getString("name");
						co=rs.getString("content");
						pt=rs.getInt("phonetype");
						 
							if(phone==null||phone.trim().length()==0)continue;
							if(phone.startsWith("86"))phone=phone.substring(2);
							 phone=phone.trim();
							 content=content.replace("{手机号码}", rs.getString("phone"));
							 if(name==null||name.trim().length()==0)name="";
							 content=content.replace("{姓名}", name);
							 if(co==null||co.trim().equals(""))co="";
							 content=content.replace("{可选短信内容}", co);
							 String messageId=getMessageId(EId,userId);
							 messageId=messageId+"|"+success;
							 
							sendCount= (content.length()%num==0)?content.length()/num:content.length()/num+1;
							if(content!=null&&content.trim().length()>0)
								content=content.replace("'", "''");
							success++;
						 
							stm2.addBatch("insert into Message_d(messageId,content,sentCount,staffId,smsNum,smsSign,EId,routeType,mesType,addTime,isSchedule,needSendTime,phone,name,phoneType,pid) " +
										"values('"+messageId+"','"+content+"',"+sendCount+",'"+userId+"','"+smsNum+"','"+smsSign+"'," +
												"'"+EId+"','"+routeType+"','"+mesType+"','"+TimeUtil.now()+"',"+isSchedule+",'"+needSendTime+"','"+phone.trim()+"','"+name+"',"+pt+","+pid+")");
							 
							content=content1;
								if(success%100==0){
									try{
										stm2.executeBatch();
										stm2.clearBatch();
									}catch(SQLException e){
										log.error(e.getMessage(),e);
										stm2.clearBatch();
									}
								}
								if(success%1000==0){
									log.debug(TimeUtil.now());
								}
						}
						try{
							stm2.executeBatch();
							stm2.clearBatch();
						}catch(SQLException e){
							log.error(e.getMessage(),e);
							stm2.clearBatch();
						}
					conn.commit();
					success++;
					conn.setAutoCommit(true);
					}catch(Exception e){
						log.error(e.getMessage(),e);
						try {
							conn.rollback();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
				 }finally{
					try{
						if(rs!=null)rs.close();
						if(totalRs!=null)totalRs.close();
						if(stm!=null)stm.close();
						if(totalStm!=null)totalStm.close();
						if(stm2!=null)stm2.close();
						if(conn!=null)conn.close();
					}catch(Exception e){
						
					}
				}
				 
		}else{
			String messageId=this.getMessageId(EId, userId);
			 
			sendCount=(content.length()%num==0)?content.length()/num:content.length()/num+1;
			content=content.replace("'", "''");
			
			String phone="";
			String name="";
			int pt=0;
				try{
					conn = db.getConnection();
					conn.setAutoCommit(false);
					stm=conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
					totalStm=conn.createStatement();
					totalRs=totalStm.executeQuery("select count(*) from (select distinct phone,name,content,phonetype from phones_load where sessionid='"+sessionid+"' and state="+state+" ) ");
					if(totalRs.next())
					total=totalRs.getInt(1);
					rs=stm.executeQuery("select distinct phone,name,content,phonetype from phones_load where sessionid='"+sessionid+"' and state="+state+" ");
					 
					stm2=conn.createStatement();
					
					log.debug(TimeUtil.now());
					while(rs.next()){
						phone=rs.getString("phone");
						name=rs.getString("name");
						pt=rs.getInt("phonetype");
						 
						if(phone==null||phone.trim().length()==0)continue;
						if(phone.startsWith("86"))phone=phone.substring(2);
						phone=phone.trim();
						success++;
							 
						stm2.addBatch("insert into Message_d(messageId,content,sentCount,staffId,smsNum,smsSign,EId,routeType,mesType,addTime,isSchedule,needSendTime,phone,name,phoneType,pid) " +
								"values('"+messageId+"','"+content+"',"+sendCount+",'"+userId+"','"+smsNum+"','"+smsSign+"'," +
										"'"+EId+"','"+routeType+"','"+mesType+"','"+TimeUtil.now()+"',"+isSchedule+",'"+needSendTime+"','"+phone+"','"+name+"',"+pt+","+pid+")");
						 
						if(success%100==0){
							try{
								stm2.executeBatch();
								stm2.clearBatch();
							}catch(Exception e){
								log.error(e.getMessage(),e);
								success=success-100;
								stm2.clearBatch();
							}
						}
						if(success%1000==0){
							log.debug(TimeUtil.now());
						}
					}
					try{
						stm2.executeBatch();
						log.debug(TimeUtil.now());
						stm2.clearBatch();
					}catch(Exception e){
						log.error(e.getMessage(),e);
						success=success-(success%100);
						stm2.clearBatch();
					}
					conn.commit();
					success++;
					conn.setAutoCommit(true);
				}catch(Exception e){
					log.error(e.getMessage(),e);
					try {
						conn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
				}finally{
					try{
						if(rs!=null)rs.close();
						if(stm2!=null)stm2.close();
						if(totalRs!=null)totalRs.close();
						if(totalStm!=null)totalStm.close();
						if(stm!=null)stm.close();
						if(conn!=null)conn.close();
					}catch(Exception e){
					}
				}
		}
	}
	public String getMessageId(String EId,String staffId){
		String messageId=EId+"|"+staffId+"P"+System.currentTimeMillis();
		return messageId;
	}


	public List<SiftBean> getSifts() {
		return sifts;
	}

	public void setSifts(List<SiftBean> sifts) {
		this.sifts = sifts;
	}

	public String getBlackPhone() {
		return blackPhone;
	}

	public void setBlackPhone(String blackPhone) {
		this.blackPhone = blackPhone;
	}

	public  int getSuccess() {
		return success;
	}

	public  void setSuccess(int success) {
		this.success = success;
	}

	public String getChkusevar() {
		return chkusevar;
	}

	public void setChkusevar(String chkusevar) {
		this.chkusevar = chkusevar;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMesType() {
		return mesType;
	}

	public void setMesType(String mesType) {
		this.mesType = mesType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSmsNum() {
		return smsNum;
	}

	public void setSmsNum(String smsNum) {
		this.smsNum = smsNum;
	}

	public String getSmsSign() {
		return smsSign;
	}

	public void setSmsSign(String smsSign) {
		this.smsSign = smsSign;
	}

	public String getEId() {
		return EId;
	}

	public void setEId(String id) {
		EId = id;
	}

	public String getIsSchedule() {
		return isSchedule;
	}

	public void setIsSchedule(String isSchedule) {
		this.isSchedule = isSchedule;
	}

	public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}

	public String getNeedSendTime() {
		return needSendTime;
	}

	public void setNeedSendTime(String needSendTime) {
		this.needSendTime = needSendTime;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public   int getNum() {
		return num;
	}
	public   void setNum(int num) {
		this.num = num;
	}
}
