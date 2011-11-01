package com.irr310.client.script;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sun.org.mozilla.javascript.Context;
import sun.org.mozilla.javascript.ContextFactory;
import sun.org.mozilla.javascript.Function;
import sun.org.mozilla.javascript.ImporterTopLevel;
import sun.org.mozilla.javascript.Scriptable;

import com.irr310.client.script.js.SandboxContextFactory;
import com.irr310.client.script.js.SandboxShutter;
import com.irr310.client.script.js.objects.Player;

public class ScriptContext {

    private Scriptable scope;
    private Context cx;

    public ScriptContext() {

        ContextFactory.initGlobal(new SandboxContextFactory(new SandboxShutter() {

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
            public boolean allowFieldAccess(Class<?> type, Object instance, String fieldName) {
                System.out.println("allowFieldAccess");
                System.out.println(type);
                System.out.println(instance);
                System.out.println(fieldName);
                return isAllowedClass(type);
            }

            @Override
            public boolean allowMethodAccess(Class<?> type, Object instance, String methodName) {
                System.out.println("allowMethodAccess");
                System.out.println(type);
                System.out.println(instance);
                System.out.println(methodName);

                return isAllowedClass(type);
            }

            @Override
            public boolean allowStaticFieldAccess(Class<?> type, String fieldName) {
                System.out.println("allowStaticFieldAccess");
                System.out.println(type);
                System.out.println(fieldName);
                return false;
            }

            @Override
            public boolean allowStaticMethodAccess(Class<?> type, String methodName) {
                // TODO Auto-generated method stub
                return false;
            }
        }));

        cx = Context.enter();
        Scriptable prototype = cx.initStandardObjects();
        Scriptable topLevel = new ImporterTopLevel(cx);
        prototype.setParentScope(topLevel);
        scope = cx.newObject(prototype);
        scope.setPrototype(prototype);

        // your scripts
        String script;
        try {
            script = readFileAsString("drivers/driver1.js");

            // Load script
            cx.evaluateString(scope, script, "<cmd>", 1, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String readFileAsString(String filePath) throws java.io.IOException {
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

    public void callFunction(String functionName, Object[] functionArgs) {

        Object fObj = scope.get(functionName, scope);
        if (!(fObj instanceof Function)) {
            System.out.println("f is undefined or not a function.");
        } else {
            Function f = (Function) fObj;
            Object result = f.call(cx, scope, scope, functionArgs);
            String report = functionName +"('my args') = " + Context.toString(result);
            System.out.println(report);
        }

        // TODO: add cache
    }

}
