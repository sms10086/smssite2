package com.smssite2.mgmt.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class WorkThread implements Runnable {	
	private static final Log LOG = LogFactory.getLog(WorkThread.class);
	
	private Thread thread;

	protected boolean work = false;
	
	public WorkThread() {
	}
	
	public synchronized boolean start() {
		if (thread == null) {
			thread = new Thread(this);
			work = true;
			thread.start();
			return true;
		}
		return false;
	}
	
	public final void sleep(long millis) {
		if (millis > 0 && isWork() )
		try {
			Thread.sleep(millis);
		}
		catch(Throwable thr) {
		}
	}
	
	public boolean isWork() {
		if (! work)
			return false;
		Thread _thread = thread;
		return _thread != null;
	}
	
	public final void run() {
		try {
			work();
		} catch(Throwable thr) {
			LOG.error(null, thr);
		}
	}
	
	public abstract void work() throws Exception;
	
	public boolean stop() {
		
		work = false;
		
		Thread _thread = thread;
		if (_thread != null) {
			try {
				_thread.interrupt();
				_thread.join();
			}
			catch(Throwable thr) {
				return false;
			}
		}
		return true;
	}
	
	public void join() {
		Thread _thread = thread;
		if (_thread == null)
		return;
		try {
		_thread.join();
		}catch(Throwable thr) {
		}
	}
}
