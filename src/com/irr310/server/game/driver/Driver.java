package com.irr310.server.game.driver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import sun.org.mozilla.javascript.Context;
import sun.org.mozilla.javascript.ContextFactory;
import sun.org.mozilla.javascript.ImporterTopLevel;
import sun.org.mozilla.javascript.Scriptable;

import com.irr310.server.js.SandboxContextFactory;
import com.irr310.server.js.SandboxShutter;
import com.irr310.server.js.objects.Player;

public class Driver {

	public Driver() throws IOException {

		ContextFactory.initGlobal(new SandboxContextFactory(
				new SandboxShutter() {

					List<Class<?>> allowedClasses;

					{
						allowedClasses = new ArrayList<Class<?>>();
						allowedClasses.add(Player.class);

					}
					
					private boolean isAllowedClass(Class<?> type) {
						return allowedClasses.contains(type);
					}

					@Override
					public boolean allowClassAccess(Class<?> type) {
						System.out.println("allowClassAccess");
						System.out.println(type);

						return isAllowedClass(type);
					}

					@Override
					public boolean allowFieldAccess(Class<?> type,
							Object instance, String fieldName) {
						System.out.println("allowFieldAccess");
						System.out.println(type);
						System.out.println(instance);
						System.out.println(fieldName);
						return isAllowedClass(type);
					}

					@Override
					public boolean allowMethodAccess(Class<?> type,
							Object instance, String methodName) {
						System.out.println("allowMethodAccess");
						System.out.println(type);
						System.out.println(instance);
						System.out.println(methodName);
						
						return isAllowedClass(type);
					}

					@Override
					public boolean allowStaticFieldAccess(Class<?> type,
							String fieldName) {
						System.out.println("allowStaticFieldAccess");
						System.out.println(type);
						System.out.println(fieldName);
						return false;
					}

					@Override
					public boolean allowStaticMethodAccess(Class<?> type,
							String methodName) {
						// TODO Auto-generated method stub
						return false;
					}
				}));

		// create and initialize Rhino Context
		Context cx = Context.enter();
		Scriptable prototype = cx.initStandardObjects();
		Scriptable topLevel = new ImporterTopLevel(cx);
		prototype.setParentScope(topLevel);
		Scriptable scope = cx.newObject(prototype);
		scope.setPrototype(prototype);

		// your scripts
		String script = readFileAsString("drivers/driver1.js");

		cx.evaluateString(scope, script, "<cmd>", 1, null);

	}

	private static String readFileAsString(String filePath)
			throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

	public static void main(String[] args) throws IOException {
		new Driver();
	}
}
