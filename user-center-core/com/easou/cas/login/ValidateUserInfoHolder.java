package com.easou.cas.login;

public class ValidateUserInfoHolder {
	private static final ThreadLocal threadLocal = new ThreadLocal();

	public static void clear() {
		threadLocal.set(null);
	}

	public static Object getUinfo() {
		return threadLocal.get();
	}

	public static void setUinfo(Object obj) {
		threadLocal.set(obj);
	}

}
