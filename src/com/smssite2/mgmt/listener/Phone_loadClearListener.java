package com.smssite2.mgmt.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.smssite2.mgmt.smstask.MTImmedisteThread;
import com.smssite2.mgmt.smstask.ScheduleThread;

public class Phone_loadClearListener implements ServletContextListener{	private Clear c =new Clear();
	private StatData stat=new StatData();
	 
	private MTImmedisteThread bigimmediste=new MTImmedisteThread(true );
	private MTImmedisteThread smallimmediste=new MTImmedisteThread(false );
	 
	private ScheduleThread bigschedule=new ScheduleThread(true );
	private ScheduleThread smallschedule=new ScheduleThread(false );
	public void contextInitialized(ServletContextEvent arg0) {
		try{
			c.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			stat.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		 
		try{
			bigimmediste.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			smallimmediste.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			bigschedule.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			smallschedule.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		 
	}

	@SuppressWarnings("deprecation")
	public void contextDestroyed(ServletContextEvent arg0) {
		try{
			c.stop();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			stat.stop();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			bigschedule.stop();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			smallschedule.stop();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			smallimmediste.stop();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			bigimmediste.stop();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
