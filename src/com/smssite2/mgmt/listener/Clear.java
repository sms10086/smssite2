package com.smssite2.mgmt.listener;

import java.sql.Timestamp;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.note.common.DbBean;

public class Clear extends WorkThread{	private static Log log = LogFactory.getLog(Clear.class);
	private static Calendar now=null;
	private static int  hh=0;
	
	public void delete(Timestamp time){
		DbBean.executeUpdate("delete from phones where borndate<? and taskID is null ", new Object[]{time});
		DbBean.executeUpdate("delete from phones where taskID  not in (select ID from task )", null);
	}

	@Override
	public void work() throws Exception {
		log.info(">>>>>>>>>>>>>>>clear ");
		while(isWork()){
			now=Calendar.getInstance();
			hh=now.get(Calendar.HOUR_OF_DAY);
			 
			if(hh>23){
				 
					sleep((24-hh)*3600000);
				 
			}
			else if(hh<23){
				 
					sleep(3600000);
				 
			}
			if(hh==23){
				now.roll(Calendar.DAY_OF_YEAR, -1);
		 
				try {
					this.delete(new Timestamp(now.getTimeInMillis()));
					log.info(">>>>>>end clear");
					sleep(3600000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
