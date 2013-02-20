/**
 * 
 */
package com.arishiplay.mobile.android.framework.reflect;

import java.lang.reflect.InvocationTargetException;

/**
 * @author airshiplay
 * @Create Date 2013-2-14
 * @version 1.0
 * @since 1.0
 */
public class Method {
	/**
	 * @param obj
	 * @param methodName
	 * @param parameters
	 * @return
	 * @since 1.7
	 * @hide 1.7
	 */
	public static Object invoke(Object obj, String methodName,
			Object... parameters) {
		try {
			@SuppressWarnings("rawtypes")
			Class[] parameterTypes = null;
			if (parameters != null) {
				parameterTypes = new Class[parameters.length];
				for (int i = 0; i < parameters.length; i++) {
					parameterTypes[i] = parameters.getClass();
				}
			}
			java.lang.reflect.Method method = obj.getClass().getMethod(
					methodName, parameterTypes);
			return method.invoke(obj, parameters);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
