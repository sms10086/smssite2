package com.note.common.extra;

import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetMethod {	
	
	private static final Log LOG = LogFactory.getLog(GetMethod.class);
	
	private String methodName;
	
	private Method method;
	
	
	public GetMethod(String name, Method method) {
		this.method = method;
		this.methodName = name;
	}
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getValue(Object source) {
		try {
			return method.invoke(source, new Object[0]);
		}
		catch(Throwable thr) {
			LOG.error(methodName, thr);
			return null;
		}
	}
}
