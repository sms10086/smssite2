package com.smssite2.mgmt.message.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.note.common.DbBean;

public class MessageIdCheck {
	@SuppressWarnings("unchecked")
	private static Map map = new HashMap();
	private static List list = new ArrayList();
	private static String f = "T";

	@SuppressWarnings("unchecked")
	public static synchronized List check(List pids) {
		if (map != null && map.size() > 0) {
			for (int i = 0; i < pids.size(); i++) {
				if (map.get(pids.get(i)) != null) {
					return null;
				}
			}
		}
		for (int i = 0; i < pids.size(); i++) {
			if (map.get(pids.get(i)) != null)
				continue;
			map.put(pids.get(i), pids.get(i));
		}
		return pids;
	}

	@SuppressWarnings("unchecked")
	public static synchronized void removePids(List pids) {
		if (pids != null && pids.size() > 0) {
			if (map != null && map.size() > 0) {
				for (int i = 0; i < pids.size(); i++) {
					map.remove(pids.get(i));
				}
			}
		}
	}

	public static synchronized long getPId() {
		long pid = DbBean
				.getLong("select pid_sequence.nextval from dual", null);
		return pid;
	}

	public static synchronized boolean isuse(String flag) {
		if (f.equals(flag))
			return true;
		else {
			f = flag;
			return false;
		}
	}

	public static synchronized void notuse() {
		f = "T";
	}
}
