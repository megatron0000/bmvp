package xyz.baudelaplace.bmvp.framework.utility;

public class ReflectionUtil {
	/**
	 * Designed to work both with anonymous and non-anonymous classes
	 * 
	 * @param o
	 * @return
	 */
	public static Class<?> getClass(Object o) {
		if (!o.getClass().isAnonymousClass())
			return o.getClass();

		Class<?> superClass = o.getClass().getSuperclass();
		Class<?>[] superInterface = o.getClass().getInterfaces();
		
		return superClass != java.lang.Object.class ? superClass
				: superInterface.length > 0 ? superInterface[0] : java.lang.Object.class;

	}
}
