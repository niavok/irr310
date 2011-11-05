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
import sun.org.mozilla.javascript.RhinoException;
import sun.org.mozilla.javascript.Scriptable;

import com.irr310.client.GameClient;
import com.irr310.client.script.js.SandboxContextFactory;
import com.irr310.client.script.js.SandboxShutter;
import com.irr310.common.world.Player;

public class ScriptContext {

    private Scriptable scope;
    private Context cx;

    private static boolean isInit = false;

    public ScriptContext() {

        init();

        cx = Context.enter();

        Scriptable prototype = cx.initStandardObjects();
        Scriptable topLevel = new ImporterTopLevel(cx);
        prototype.setParentScope(topLevel);
        scope = cx.newObject(prototype);
        scope.setPrototype(prototype);

        // load init scripts
        loadScriptFile("drivers/constants.js");
        loadScriptFile("drivers/imports.js");
        loadScriptFile("drivers/init.js");
        
        // Init player
        
        Player localPlayer = GameClient.getInstance().localPlayer;
        if(localPlayer != null) {
            loadScriptString("core.me = new Player("+localPlayer.getId()+");");
        }

        
        // Load player scripts
        loadScriptFile("drivers/driver1.js");
        
    }

    
    private boolean loadScriptString(String script) {
        try {
            // Exec script
            cx.evaluateString(scope, script, "custom command", 1, null);
        } catch (RhinoException e) {
            printError(e);
            return false;

        }
        return true;
    }
    
    private boolean loadScriptFile(String path) {
        try {
            String script = readFileAsString(path);
            // Load script
            cx.evaluateString(scope, script, path, 1, null);
        } catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (RhinoException e) {
            printError(e);
            return false;

        }
        return true;
    }

    private void printError(RhinoException e) {
        System.err.println("JS error in file: " + e.sourceName());
        System.err.println("line " + e.lineNumber() + ", colomn " + e.columnNumber() + ":");
        System.err.println(e.lineSource());
        String arrow = "^";
        for (int i = 0; i < e.columnNumber(); i++) {
            arrow = "-" + arrow;
        }
        System.err.println(arrow);
        System.err.println("Error: " + e.details());
        System.err.println("Stacktrace:");
        System.err.println(e.getScriptStackTrace());
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
        try {
            Object fObj = scope.get(functionName, scope);
            if (!(fObj instanceof Function)) {
                System.out.println("f is undefined or not a function.");
            } else {
                Function f = (Function) fObj;
                Object result = f.call(cx, scope, scope, functionArgs);
                String report = functionName + "('my args') = " + Context.toString(result);
            }

        } catch (RhinoException e) {
            printError(e);

        }

        // TODO: add cache
    }

    public void close() {
        System.out.println("close");
        Context.exit();
    }

    private void init() {
        if (isInit) {
            return;
        }
        isInit = true;
        
        ContextFactory.initGlobal(new SandboxContextFactory(new SandboxShutter() {

            List<Class<?>> allowedClasses;

            {
                allowedClasses = new ArrayList<Class<?>>();
                allowedClasses.add(com.irr310.client.script.js.objects.Core.class);
                allowedClasses.add(com.irr310.client.script.js.objects.Player.class);

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
                return false;
                //return isAllowedClass(type);
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

    }

}
