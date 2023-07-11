package com.note.common.extra;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class MethodUtils {	
	private static HashMap<Class<?>, GetMethod[]> getMethods = new HashMap<Class<?>, GetMethod[]>();
	
	private static HashMap<Class<?>, SetMethods> setMethods = new HashMap<Class<?>, SetMethods>();
	
	public static GetMethod[] getGetMethods(Class<?> clazz) {
		synchronized( getMethods ) {
			GetMethod[] ms = (GetMethod[]) getMethods.get(clazz);
			if (ms != null)
				return ms;
			Method[] arr = clazz.getMethods();
			ArrayList<GetMethod> list = new ArrayList<GetMethod>(arr.length);
			for(int i = 0; i < arr.length; i ++) {
				Method m = arr[i];
				if (m.getName().startsWith("get")
						&& m.getParameterTypes().length == 0) {
					Class returnType = m.getReturnType();
					if (returnType != null) {
						String methodName = m.getName().substring(3);
						list.add( new GetMethod(methodName, m) );
					}
				}
			}
			ms = (GetMethod[])list.toArray(new GetMethod[list.size()]);
			getMethods.put(clazz, ms);
			return ms;
		}
	}
	
	public static SetMethods getSetMethods(Class<?> clazz) {
		synchronized( getMethods ) {
			SetMethods sets = (SetMethods) setMethods.get(clazz);
			if (sets != null)
				return sets;
			Method[] arr = clazz.getMethods();
			sets = new SetMethods();
			for(int i = 0; i < arr.length; i ++) {
				Method m = arr[i];
				if (m.getName().startsWith("set")
						&& m.getParameterTypes().length == 1) {
					String name = m.getName().substring(3);
					sets.addMethod( name, m );
				}
			}
			setMethods.put(clazz, sets);
			return sets;
		}
	}
}
