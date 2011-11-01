package com.irr310.client.script.js;

public interface SandboxShutter {
	public boolean allowClassAccess(Class<?> type);

	public boolean allowFieldAccess(Class<?> type, Object instance,
			String fieldName);

	public boolean allowMethodAccess(Class<?> type, Object instance,
			String methodName);

	public boolean allowStaticFieldAccess(Class<?> type, String fieldName);

	public boolean allowStaticMethodAccess(Class<?> type, String methodName);
}
