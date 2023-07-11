package com.note.common.extra;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SetMethods {	
	private static final Log LOG = LogFactory.getLog(SetMethods.class);
	
	private HashMap map = new HashMap();
	
	public void addMethod(String name, Method m) {
		ArrayList list = ( ArrayList ) map.get(name);
		if (list == null) {
			list = new ArrayList();
			map.put(name, list);
		}
		
		Class p = m.getParameterTypes()[0];
		for(int i = 0; i < list.size(); i ++) {
			Method m0 = (Method) list.get(i);
			Class p0 = m0.getParameterTypes()[0];
			if ( p0.isAssignableFrom(p) ) {
				list.add(i, m);
				break;
			}
		}
		list.add( m );
	}
	
	public void setValue(Object obj, String name, Object value) {
		ArrayList list = (ArrayList) map.get(name);
		if (list == null || list.size() == 0) return;
		if (value == null) {
			Method m = (Method) list.get(0);
			setObjectValue(obj, m, value);
			return;
		}
		Class p = value.getClass();
		for(int i = list.size()-1; i >=0; i --) {
			Method m0 = (Method) list.get(i);
			Class p0 = m0.getParameterTypes()[0];
			if ( p0.isAssignableFrom(p) ) {
				setObjectValue(obj, m0, value);
				return;
			}
		}
	}

	private void setObjectValue(Object obj, Method method, Object value) {
		try {
			method.invoke(obj, new Object[] { value } );
		}
		catch(Throwable thr) {
			LOG.error(method.getName(), thr);
		}
	}
}
