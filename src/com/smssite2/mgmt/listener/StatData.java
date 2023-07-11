package com.smssite2.mgmt.listener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.note.common.DbBean;

public class StatData extends WorkThread{	private static Log log = LogFactory.getLog(StatData.class);
	private static Calendar now=null;
	private String today;
	private String yestoday;
	private String TDBY;
	private static int  hh=0;
	
	@Override
	public void work() throws Exception {
		while(isWork()){

			now=Calendar.getInstance();
			hh=now.get(Calendar.HOUR_OF_DAY);
			SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd");
			today =smd.format(now.getTime());
			now.add(Calendar.DAY_OF_MONTH, -1);
			yestoday=smd.format(now.getTime());
			now.add(Calendar.DAY_OF_MONTH, -1);
			TDBY=smd.format(now.getTime());
				if(hh!=23){
					 
						log.debug(">>>>>>StatData in sleep");
						sleep(3600000);
					 
				}else{
					try{
						SmsSend();
						log.info(">>>>>SmsSend succ");
					}catch(Exception e){
						log.error(e.getMessage(),e);
					}
					try{
						Paragraph();
						log.info(">>>>>Paragraph succ");
					}catch(Exception e){
						log.error(e.getMessage(),e);
					}
					try{
						SmsReceive();
						log.info(">>>>>SmsReceive succ");
					}catch(Exception e){
						log.error(e.getMessage(),e);
					}
					
					
					 
						log.debug(">>>>>sleeping");
						sleep(3600000);
					 
				}
			
		}
		
	}
	private String getSmsSendSql(String today){
		StringBuilder sb=new StringBuilder();
		sb.append("select a.eid,a.staffid,total ,totalok,totalfail,totalnull,totalmobile,sucTotalMobile,faiTotalMobile,nulltotalmobile,totalUnicom,sucTotalUnicom,faiTotalUnicom,nulltotalUnicom,totalTelecom,sucTotalTelecom,faiTotalTelecom,nulltotalTelecom from");
		sb.append("(select eid,staffid,count(id) as total from smsmt   where   convert(varchar,sendTime,120) like '"+today+"%' and result>-1 group by eid,staffid) a  ");
		sb.append("left join");
		sb.append(" (select eid,staffid,count(id) as totalok from smsmt   where  convert(varchar,sendTime,120) like '"+today+"%' and status='0' group by eid,staffid) b ");
		sb.append(" on a.eid=b.eid and a.staffid=b.staffid ");
		sb.append("  left join ");
		sb.append("(select eid,staffid,count(id) as totalfail from smsmt   where  convert(varchar,sendTime,120) like '"+today+"%' and status<>'0' and status<>'10004' group by eid,staffid) c ");
		sb.append(" on a.eid=c.eid and a.staffid=c.staffid ");
		sb.append(" left join ");
		sb.append("  (select eid,staffid,count(id) as totalnull from smsmt   where  convert(varchar,sendTime,120) like '"+today+"%' and (status is null or status='10004') and result>-1 group by eid,staffid) d ");
		sb.append("  on a.eid=d.eid and a.staffid=d.staffid ");
		sb.append("  left join ");
		sb.append(" (select eid,staffid,count(id) as totalmobile from smsmt   where  convert(varchar,sendTime,120) like '"+today+"%' and phonetype=0 and result>-1 group by eid,staffid) e ");
		sb.append(" on a.eid=e.eid and a.staffid=e.staffid ");
		sb.append(" left join ");
		sb.append(" (select eid,staffid,count(id) as sucTotalMobile from smsmt   where  convert(varchar,sendTime,120) like '"+today+"%' and phonetype=0 and status='0' group by eid,staffid) f ");
		sb.append(" on a.eid=f.eid and a.staffid=f.staffid ");
		sb.append(" left join ");
		sb.append(" (select eid,staffid,count(id) as faiTotalMobile from smsmt   where  convert(varchar,sendTime,120) like '"+today+"%' and phonetype=0 and status<>'0' and status<>'10004' group by eid,staffid) g on a.eid=g.eid and a.staffid=g.staffid");
		sb.append("   left join ");
		sb.append("  (select eid,staffid,count(id) as nulltotalmobile from smsmt   where  convert(varchar,sendTime,120) like '"+today+"%' and phonetype=0 and (status is null or status='10004') and result>-1 group by eid,staffid) h ");
		sb.append(" on a.eid=h.eid and a.staffid=h.staffid ");
		sb.append("   left join ");
		sb.append("   (select eid,staffid,count(id) as totalUnicom from smsmt   where  convert(varchar,sendTime,120) like '"+today+"%' and phonetype=1 and result>-1 group by eid,staffid) o ");
		sb.append("   on a.eid=o.eid and a.staffid=o.staffid ");
		sb.append("   left join ");
		sb.append("  (select eid,staffid,count(id) as sucTotalUnicom from smsmt   where  convert(varchar,sendTime,120) like '"+today+"%' and phonetype=1 and status='0' group by eid,staffid) i ");
		sb.append("  on a.eid=i.eid and a.staffid=i.staffid ");
		sb.append("left join ");
		sb.append(" (select eid,staffid,count(id) as faiTotalUnicom from smsmt   where  convert(varchar,sendTime,120) like '"+today+"%' and phonetype=1 and status<>'0' and status<>'10004' group by eid,staffid) j ");
		sb.append(" on a.eid=j.eid and a.staffid=j.staffid ");
		sb.append("  left join ");
		sb.append(" (select eid,staffid,count(id) as nulltotalUnicom from smsmt   where  convert(varchar,sendTime,120) like '"+today+"%' and phonetype=1 and (status is null or status='10004') and result>-1 group by eid,staffid) k ");
		sb.append(" on a.eid=k.eid and a.staffid=k.staffid ");
		sb.append("  left join ");
		sb.append("(select eid,staffid,count(id) as totalTelecom from smsmt   where  convert(varchar,sendTime,120) like '"+today+"%' and phonetype=2 and result>-1 group by eid,staffid) p ");
		sb.append("on a.eid=p.eid and a.staffid=p.staffid ");
		sb.append("   left join ");
		sb.append("  (select eid,staffid,count(id) as sucTotalTelecom from smsmt   where  convert(varchar,sendTime,120) like '"+today+"%' and phonetype=2 and status='0' group by eid,staffid) l ");
		sb.append("  on a.eid=l.eid and a.staffid=l.staffid ");
		sb.append("  left join ");
		sb.append(" (select eid,staffid,count(id) as faiTotalTelecom from smsmt   where  convert(varchar,sendTime,120) like '"+today+"%' and phonetype=2 and status<>'0' and status<>'10004' group by eid,staffid) m ");
		sb.append(" on a.eid=m.eid and a.staffid=m.staffid ");
		sb.append("   left join ");
		sb.append("  (select eid,staffid,count(id) as nulltotalTelecom from smsmt   where  convert(varchar,sendTime,120) like '"+today+"%' and phonetype=2 and (status is null or status='10004') and result>-1 group by eid,staffid) n ");
		sb.append("  on a.eid=n.eid and a.staffid=n.staffid");
		return sb.toString();
	}
	@SuppressWarnings("static-access")
	public void SmsSend(){
			DbBean db=new DbBean();
			Connection conn=null;
			Statement stm=null;
			Statement insert=null;
			Statement update=null;
			
			ResultSet rs=null;
			
			try{
				conn=db.getConnection();
				conn.setAutoCommit(false);
				stm=conn.createStatement();
				insert=conn.createStatement();
				update=conn.createStatement();
				rs=stm.executeQuery(getSmsSendSql(today));
				while(rs.next()){
					insert.addBatch("insert into stat_smssend(senddate,eid,staffid ,total ,totalok,totalfail,totalnull,totalmobile,sucTotalMobile," +
							"faiTotalMobile,nulltotalmobile,totalUnicom,sucTotalUnicom,faiTotalUnicom,nulltotalUnicom,totalTelecom," +
							"sucTotalTelecom,faiTotalTelecom,nulltotalTelecom) values('"+today+"','"+rs.getString("eid")+"','"+rs.getString("staffid")+"'," +
							""+rs.getInt("total")+","+rs.getInt("totalok")+","+rs.getInt("totalfail")+","+rs.getInt("totalnull")+","+rs.getInt("totalmobile")+","+rs.getInt("sucTotalMobile")+"," +
									""+rs.getInt("faiTotalMobile")+","+rs.getInt("nulltotalmobile")+","+rs.getInt("totalUnicom")+","+rs.getInt("sucTotalUnicom")+","
									+rs.getInt("faiTotalUnicom")+","+rs.getInt("nulltotalUnicom")+","+rs.getInt("totalTelecom")+","+rs.getInt("sucTotalTelecom")+","+rs.getInt("faiTotalTelecom")+","+rs.getInt("nulltotalTelecom")+")");
				}
				insert.executeBatch();
				insert.clearBatch();
				conn.commit();
				rs=stm.executeQuery(getSmsSendSql(yestoday));
				while(rs.next()){
					update.addBatch("update stat_smssend set total="+rs.getInt("total")+",totalok="+rs.getInt("totalok")+",totalfail="+rs.getInt("totalfail")+",totalnull="+rs.getInt("totalnull")
							+",totalmobile="+rs.getInt("totalmobile")+",sucTotalMobile="+rs.getInt("sucTotalMobile")+",faiTotalMobile="+rs.getInt("faiTotalMobile")+",nulltotalmobile="+rs.getInt("nulltotalmobile")+",totalUnicom="+rs.getInt("totalUnicom")+"," +
									"sucTotalUnicom="+rs.getInt("sucTotalUnicom")+",faiTotalUnicom="+rs.getInt("faiTotalUnicom")+",nulltotalUnicom="+rs.getInt("nulltotalUnicom")+",totalTelecom="
									+rs.getInt("totalTelecom")+",sucTotalTelecom="+rs.getInt("sucTotalTelecom")+",faiTotalTelecom="+rs.getInt("faiTotalTelecom")+",nulltotalTelecom="+rs.getInt("nulltotalTelecom")
									+" where eid='"+rs.getString("eid")+"' and staffid='"+rs.getString("staffid")+"' and senddate='"+yestoday+"'");
				}
				update.executeBatch();
				update.clearBatch();
				conn.commit();
				rs=stm.executeQuery(getSmsSendSql(TDBY));
				while(rs.next()){
					update.addBatch("update stat_smssend set total="+rs.getInt("total")+",totalok="+rs.getInt("totalok")+",totalfail="+rs.getInt("totalfail")+",totalnull="+rs.getInt("totalnull")
							+",totalmobile="+rs.getInt("totalmobile")+",sucTotalMobile="+rs.getInt("sucTotalMobile")+",faiTotalMobile="+rs.getInt("faiTotalMobile")+",nulltotalmobile="+rs.getInt("nulltotalmobile")+",totalUnicom="+rs.getInt("totalUnicom")+"," +
									"sucTotalUnicom="+rs.getInt("sucTotalUnicom")+",faiTotalUnicom="+rs.getInt("faiTotalUnicom")+",nulltotalUnicom="+rs.getInt("nulltotalUnicom")+",totalTelecom="
									+rs.getInt("totalTelecom")+",sucTotalTelecom="+rs.getInt("sucTotalTelecom")+",faiTotalTelecom="+rs.getInt("faiTotalTelecom")+",nulltotalTelecom="+rs.getInt("nulltotalTelecom")
									+" where eid='"+rs.getString("eid")+"' and staffid='"+rs.getString("staffid")+"' and senddate='"+TDBY+"'");
				}
				update.executeBatch();
				update.clearBatch();
				conn.commit();
				conn.setAutoCommit(true);
			}catch(Exception e){
				log.error(e.getMessage(), e);
				try {
					conn.rollback();
				} catch (SQLException e1) {
					log.error(e1.getMessage(), e1);
				}
			}finally{
				db.close(rs);
				db.close(stm,update);
				db.close(insert);
				db.close(conn);
			}
	}
	private String getParagraph(String today){
		StringBuilder sb=new StringBuilder();
		sb.append("select a.eid,a.phone3,total,suc,fail,totalnull from ")
		  .append(" (select eid,substring(phone,1,3) as phone3,count(id) as total from smsmt  where  convert(varchar,sendTime,120) like '"+today+"%' and result>-1 group by eid,substring(phone,1,3)) a ")
		  .append(" left join ")
		  .append(" (select eid,substring(phone,1,3) as phone3,count(id) as suc from smsmt where  convert(varchar,sendTime,120) like '"+today+"%' and status='0'   group by eid,substring(phone,1,3)) b  ")
		  .append(" on a.eid=b.eid and a.phone3=b.phone3 ")
		  .append(" left join ")
		  .append(" (select eid,substring(phone,1,3) as phone3,count(id) as fail from smsmt where  convert(varchar,sendTime,120) like '"+today+"%' and status<>'0' and status<>'10004'   group by eid,substring(phone,1,3)) c   ")
		  .append(" on a.eid=c.eid and a.phone3=c.phone3 ")
		  .append(" left join ")
		  .append(" (select eid,substring(phone,1,3) as phone3,count(id) as totalnull from smsmt where  convert(varchar,sendTime,120) like '"+today+"%' and (status is null or status='10004') and result>-1 group by eid,substring(phone,1,3)) d  ")
		  .append(" on a.eid=d.eid and a.phone3=d.phone3 ");
		return sb.toString();
	}
	@SuppressWarnings("static-access")
	public void Paragraph(){
		DbBean db=new DbBean();
		Connection conn=null;
		Statement stm=null;
		Statement insert=null;
		Statement update=null;
		
		ResultSet rs=null;
		
		try{
			conn=db.getConnection();
			conn.setAutoCommit(false);
			stm=conn.createStatement();
			insert=conn.createStatement();
			update=conn.createStatement();
			rs=stm.executeQuery(getParagraph(today));
			while(rs.next()){
				insert.addBatch("insert into stat_phone(phone3,senddate,eid,total ,suc,fail,totalnull) values('"+rs.getString("phone3")+"','"+today+"','"+rs.getString("eid")+"'," +
						""+rs.getInt("total")+","+rs.getInt("suc")+","+rs.getInt("fail")+","+rs.getInt("totalnull")+")");
			}
			insert.executeBatch();
			insert.clearBatch();
			conn.commit();
			rs=stm.executeQuery(getParagraph(yestoday));
			while(rs.next()){
				update.addBatch("update stat_phone set total="+rs.getInt("total")+",suc="+rs.getInt("suc")+",fail="+rs.getInt("fail")+",totalnull="+rs.getInt("totalnull")
								+" where eid='"+rs.getString("eid")+"'   and senddate='"+yestoday+"' and phone3='"+rs.getString("phone3")+"'");
			}
			update.executeBatch();
			update.clearBatch();
			conn.commit();
			rs=stm.executeQuery(getParagraph(TDBY));
			while(rs.next()){
				update.addBatch("update stat_phone set total="+rs.getInt("total")+",suc="+rs.getInt("suc")+",fail="+rs.getInt("fail")+",totalnull="+rs.getInt("totalnull")
						+" where eid='"+rs.getString("eid")+"'   and senddate='"+TDBY+"' and phone3='"+rs.getString("phone3")+"' ");
			}
			update.executeBatch();
			update.clearBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error(e1.getMessage(), e1);
			}
		}finally{
			db.close(rs);
			db.close(stm,update);
			db.close(insert);
			db.close(conn);
		}
	}
	private String getReceiveSql(String today){
		StringBuilder sb=new StringBuilder();
		sb.append("select a.eid,a.receiver ,total,totalmobile,totalunicom,totaltelecom from ")
		.append(" (select eid,receiver,count(id) as total from received_d where receivetime>'"+today+" 00:00' and receivetime<'"+today+" 24:00' group by eid,receiver) a ")
		.append(" left join ")
		.append(" (select eid,receiver,count(id) as totalmobile from received_d where receivetime>'"+today+" 00:00' and receivetime<'"+today+" 24:00' and phonetype=0 group by eid,receiver) b ")
		.append("on a.eid=b.eid and a.receiver=b.receiver ")
		.append("left join ")
		.append("(select eid,receiver,count(id) as totalunicom from received_d where receivetime>'"+today+" 00:00' and receivetime<'"+today+" 24:00' and phonetype=1 group by eid,receiver) c ")
		.append(" on a.eid=c.eid and a.receiver=c.receiver ")
		.append("left join  ")
		.append("(select eid,receiver,count(id) as totaltelecom from received_d where receivetime>'"+today+" 00:00' and receivetime<'"+today+" 24:00' and phonetype=2 group by eid,receiver) d ")
		.append(" on a.eid=d.eid and a.receiver=d.receiver");
		return sb.toString();
	}

	@SuppressWarnings("static-access")
	public void SmsReceive(){
		DbBean db=new DbBean();
		Connection conn=null;
		Statement stm=null;
		Statement insert=null;
		Statement update=null;
		
		ResultSet rs=null;
		
		try{
			conn=db.getConnection();
			conn.setAutoCommit(false);
			stm=conn.createStatement();
			insert=conn.createStatement();
			update=conn.createStatement();
			rs=stm.executeQuery(getReceiveSql(today));
			while(rs.next()){
				insert.addBatch("insert into stat_receive(borndate,eid,receiver,total ,totalmobile,totalunicom,totaltelecom) values('"+today+"','"+rs.getString("eid")+"','"+rs.getString("receiver")+"'," +
						""+rs.getInt("total")+","+rs.getInt("totalmobile")+","+rs.getInt("totalunicom")+","+rs.getInt("totaltelecom")+")");
			}
			insert.executeBatch();
			insert.clearBatch();
			conn.commit();
			rs=stm.executeQuery(getReceiveSql(yestoday));
			while(rs.next()){
				update.addBatch("update stat_receive set total="+rs.getInt("total")+",totalmobile="+rs.getInt("totalmobile")+",totalunicom="+rs.getInt("totalunicom")+",totaltelecom="+rs.getInt("totaltelecom")
								+" where eid='"+rs.getString("eid")+"'  and receiver='"+rs.getString("receiver")+"' and borndate='"+yestoday+"'");
			}
			update.executeBatch();
			update.clearBatch();
			conn.commit();
			rs=stm.executeQuery(getReceiveSql(TDBY));
			while(rs.next()){
				update.addBatch("update stat_receive set total="+rs.getInt("total")+",totalmobile="+rs.getInt("totalmobile")+",totalunicom="+rs.getInt("totalunicom")+",totaltelecom="+rs.getInt("totaltelecom")
						+" where eid='"+rs.getString("eid")+"' and receiver='"+rs.getString("receiver")+"'  and borndate='"+TDBY+"'");
			}
			update.executeBatch();
			update.clearBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error(e1.getMessage(), e1);
			}
		}finally{
			db.close(rs);
			db.close(stm,update);
			db.close(insert);
			db.close(conn);
		}

	}

	private static String getSmsSendAdminSql(String today){
		StringBuilder sb=new StringBuilder();
		sb.append("select a.eid, total ,totalok,totalfail,totalnull,totalmobile  ,sucTotalMobile  ,faiTotalMobile  ,nulltotalmobile  ,totalUnicom  ,sucTotalUnicom  ,faiTotalUnicom  ,nulltotalUnicom  ,totalTelecom  ,sucTotalTelecom  ,faiTotalTelecom  ,nulltotalTelecom   from ");
		sb.append("(select eid, count(id) as total from sendadmin_history   where sendtime like '"+today+"%'  and result is not null group by eid) a  ");
		sb.append("left join");
		sb.append(" (select eid,count(id) as totalok from sendadmin_history   where sendtime like '"+today+"%'  and status='0' group by eid) b ");
		sb.append(" on a.eid=b.eid  ");
		sb.append("  left join ");
		sb.append("(select eid,count(id) as totalfail from sendadmin_history   where sendtime like '"+today+"%'  and status<>'0' and status<>'10004' group by eid) c ");
		sb.append(" on a.eid=c.eid  ");
		sb.append(" left join ");
		sb.append("  (select eid,count(id) as totalnull from sendadmin_history   where sendtime like '"+today+"%' and (status is null or status='10004')  and result is not null group by eid) d ");
		sb.append("  on a.eid=d.eid ");
		sb.append("  left join ");
		sb.append(" (select eid,count(id) as totalmobile from sendadmin_history   where sendtime like '"+today+"%' and phonetype=0  and result is not null group by eid) e ");
		sb.append(" on a.eid=e.eid ");
		sb.append(" left join ");
		sb.append(" (select eid,count(id) as sucTotalMobile from sendadmin_history   where sendtime like '"+today+"%'  and phonetype=0 and status='0' group by eid) f ");
		sb.append(" on a.eid=f.eid ");
		sb.append(" left join ");
		sb.append(" (select eid,count(id) as faiTotalMobile from sendadmin_history   where sendtime like '"+today+"%'  and phonetype=0 and status<>'0' and status<>'10004' group by eid) g on a.eid=g.eid  ");
		sb.append("   left join ");
		sb.append("  (select eid,count(id) as nulltotalmobile from sendadmin_history   where sendtime like '"+today+"%' and phonetype=0 and (status is null or status='10004')  and result is not null group by eid) h ");
		sb.append(" on a.eid=h.eid  ");
		sb.append("   left join ");
		sb.append("   (select eid,count(id) as totalUnicom from sendadmin_history   where sendtime like '"+today+"%' and phonetype=1  and result is not null group by eid) o ");
		sb.append("   on a.eid=o.eid   ");
		sb.append("   left join ");
		sb.append("  (select eid,count(id) as sucTotalUnicom from sendadmin_history   where sendtime like '"+today+"%'  and phonetype=1 and status='0' group by eid) i ");
		sb.append("  on a.eid=i.eid   ");
		sb.append("left join ");
		sb.append(" (select eid,count(id) as faiTotalUnicom from sendadmin_history   where sendtime like '"+today+"%'  and phonetype=1 and status<>'0' and status<>'10004' group by eid) j ");
		sb.append(" on a.eid=j.eid   ");
		sb.append("  left join ");
		sb.append(" (select eid,count(id) as nulltotalUnicom from sendadmin_history   where sendtime like '"+today+"%' and phonetype=1 and (status is null or status='10004')  and result is not null group by eid) k ");
		sb.append(" on a.eid=k.eid   ");
		sb.append("  left join ");
		sb.append("(select eid,count(id) as totalTelecom from sendadmin_history   where sendtime like '"+today+"%' and phonetype=2  and result is not null group by eid) p ");
		sb.append("on a.eid=p.eid   ");
		sb.append("   left join ");
		sb.append("  (select eid,count(id) as sucTotalTelecom from sendadmin_history   where sendtime like '"+today+"%'  and phonetype=2 and status='0' group by eid) l ");
		sb.append("  on a.eid=l.eid   ");
		sb.append("  left join ");
		sb.append(" (select eid,count(id) as faiTotalTelecom from sendadmin_history   where sendtime like '"+today+"%'  and phonetype=2 and status<>'0' and status<>'10004' group by eid) m ");
		sb.append(" on a.eid=m.eid  ");
		sb.append("   left join ");
		sb.append("  (select eid,count(id) as nulltotalTelecom from sendadmin_history   where sendtime like '"+today+"%' and phonetype=2 and (status is null or status='10004')   and result is not null group by eid) n ");
		sb.append("  on a.eid=n.eid  ");
		return sb.toString();
	}
	
	private void SmsSendAdmin(){
		DbBean db=new DbBean();
		Connection conn=null;
		Statement stm=null;
		Statement insert=null;
		Statement update=null;
		
		ResultSet rs=null;
		
		try{
			conn=db.getConnection();
			conn.setAutoCommit(false);
			stm=conn.createStatement();
			insert=conn.createStatement();
			update=conn.createStatement();
			rs=stm.executeQuery(getSmsSendAdminSql(today));
			while(rs.next()){
				insert.addBatch("insert into stat_smssendadmin(senddate,eid,staffid ,total ,totalok,totalfail,totalnull,totalmobile,sucTotalMobile," +
						"faiTotalMobile,nulltotalmobile,totalUnicom,sucTotalUnicom,faiTotalUnicom,nulltotalUnicom,totalTelecom," +
						"sucTotalTelecom,faiTotalTelecom,nulltotalTelecom) values('"+today+"','"+rs.getString("eid")+"','admin'," +
						""+rs.getInt("total")+","+rs.getInt("totalok")+","+rs.getInt("totalfail")+","+rs.getInt("totalnull")+","+rs.getInt("totalmobile")+","+rs.getInt("sucTotalMobile")+"," +
								""+rs.getInt("faiTotalMobile")+","+rs.getInt("nulltotalmobile")+","+rs.getInt("totalUnicom")+","+rs.getInt("sucTotalUnicom")+","
								+rs.getInt("faiTotalUnicom")+","+rs.getInt("nulltotalUnicom")+","+rs.getInt("totalTelecom")+","+rs.getInt("sucTotalTelecom")+","+rs.getInt("faiTotalTelecom")+","+rs.getInt("nulltotalTelecom")+")");
			}
			insert.executeBatch();
			insert.clearBatch();
			conn.commit();
			rs=stm.executeQuery(getSmsSendAdminSql(yestoday));
			while(rs.next()){
				update.addBatch("update stat_smssendadmin set total="+rs.getInt("total")+",totalok="+rs.getInt("totalok")+",totalfail="+rs.getInt("totalfail")+",totalnull="+rs.getInt("totalnull")
						+",totalmobile="+rs.getInt("totalmobile")+",sucTotalMobile="+rs.getInt("sucTotalMobile")+",faiTotalMobile="+rs.getInt("faiTotalMobile")+",nulltotalmobile="+rs.getInt("nulltotalmobile")+",totalUnicom="+rs.getInt("totalUnicom")+"," +
								"sucTotalUnicom="+rs.getInt("sucTotalUnicom")+",faiTotalUnicom="+rs.getInt("faiTotalUnicom")+",nulltotalUnicom="+rs.getInt("nulltotalUnicom")+",totalTelecom="
								+rs.getInt("totalTelecom")+",sucTotalTelecom="+rs.getInt("sucTotalTelecom")+",faiTotalTelecom="+rs.getInt("faiTotalTelecom")+",nulltotalTelecom="+rs.getInt("nulltotalTelecom")
								+" where eid='"+rs.getString("eid")+"'   and senddate='"+yestoday+"'");
			}
			update.executeBatch();
			update.clearBatch();
			conn.commit();
			rs=stm.executeQuery(getSmsSendAdminSql(TDBY));
			while(rs.next()){
				update.addBatch("update stat_smssendadmin set total="+rs.getInt("total")+",totalok="+rs.getInt("totalok")+",totalfail="+rs.getInt("totalfail")+",totalnull="+rs.getInt("totalnull")
						+",totalmobile="+rs.getInt("totalmobile")+",sucTotalMobile="+rs.getInt("sucTotalMobile")+",faiTotalMobile="+rs.getInt("faiTotalMobile")+",nulltotalmobile="+rs.getInt("nulltotalmobile")+",totalUnicom="+rs.getInt("totalUnicom")+"," +
								"sucTotalUnicom="+rs.getInt("sucTotalUnicom")+",faiTotalUnicom="+rs.getInt("faiTotalUnicom")+",nulltotalUnicom="+rs.getInt("nulltotalUnicom")+",totalTelecom="
								+rs.getInt("totalTelecom")+",sucTotalTelecom="+rs.getInt("sucTotalTelecom")+",faiTotalTelecom="+rs.getInt("faiTotalTelecom")+",nulltotalTelecom="+rs.getInt("nulltotalTelecom")
								+" where eid='"+rs.getString("eid")+"'  and senddate='"+TDBY+"'");
			}
			update.executeBatch();
			update.clearBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error(e1.getMessage(), e1);
			}
		}finally{
			db.close(rs);
			db.close(stm,update);
			db.close(insert);
			db.close(conn);
		}
	}
	
}
