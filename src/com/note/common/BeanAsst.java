package com.note.common;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.note.common.extra.GetMethod;
import com.note.common.extra.MethodUtils;
import com.note.common.extra.SetMethods;

public class BeanAsst {	
	private static final Log LOG = LogFactory.getLog(BeanAsst.class);
	
	public static void copyProperties(Object target, Object source) {
		if (target == null || source == null)
			return;
		GetMethod[] origins = MethodUtils.getGetMethods(source.getClass());
		SetMethods sets = MethodUtils.getSetMethods(target.getClass());
		for(int i = 0; i < origins.length; i ++) {
			Object value = origins[i].getValue(source);
			sets.setValue(target, origins[i].getMethodName(), value);
		}
	}
	
	public static void copyPropertiesWithoutNull(Object target, Object source) {
		if (target == null || source == null)
			return;
		GetMethod[] origins = MethodUtils.getGetMethods(source.getClass());
		SetMethods sets = MethodUtils.getSetMethods(target.getClass());
		for(int i = 0; i < origins.length; i ++) {
			Object value = origins[i].getValue(source);
			if(value!=null){
				if(value instanceof String){
					String v = (String)value;
					if(!v.equals("")){
						sets.setValue(target, origins[i].getMethodName(), value);
					}
				}else{
					sets.setValue(target, origins[i].getMethodName(), value);
				}
			}
		}
	}

	
	public static boolean formToBean(HttpServletRequest req, Object bean) {
		try {
			Enumeration en = req.getParameterNames();
			HashMap hm = new HashMap();
			while ( en.hasMoreElements() ) {
				String name = (String) en.nextElement();
				String[] values = getStrings(req, name);
				if (values != null) {
					if (values.length == 1)
						hm.put(name, values[0]);
					else
					hm.put(name, values);
				}
			}
			BeanUtils.populate(bean, hm);
			return true;
		}
		catch(Throwable thr) {
		}
		return false;
	}
	
	public static String[] getStrings(HttpServletRequest req, String name) {
		String[] values = req.getParameterValues(name);
		if (values == null || values.length == 0)
			return null;
		ArrayList list = new ArrayList();
		for(int i = 0; i < values.length; i ++) {
			String s = reformString(values[i], null);
			if (s != null) list.add( s );
		}
		if (list.size() == 0)
			return null;
		return (String[])list.toArray(new String[list.size()]);
	}
	
	private static String reformString(String value, String def) {
		if (value == null) return def;
		value = value.trim();
		if (value.length() == 0)
			return def;
		return value;
	}
}
