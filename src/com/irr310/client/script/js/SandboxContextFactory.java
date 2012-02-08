package com.irr310.client.script.js;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sun.org.mozilla.javascript.BaseFunction;
import sun.org.mozilla.javascript.ClassShutter;
import sun.org.mozilla.javascript.Context;
import sun.org.mozilla.javascript.ContextFactory;
import sun.org.mozilla.javascript.ImporterTopLevel;
import sun.org.mozilla.javascript.NativeJavaClass;
import sun.org.mozilla.javascript.NativeJavaObject;
import sun.org.mozilla.javascript.Scriptable;
import sun.org.mozilla.javascript.ScriptableObject;
import sun.org.mozilla.javascript.WrapFactory;

public class SandboxContextFactory extends ContextFactory {
    final SandboxShutter shutter;

    public SandboxContextFactory(SandboxShutter shutter) {
        this.shutter = shutter;
    }

    @Override
    protected Context makeContext() {
        Context cx = super.makeContext();
        cx.setWrapFactory(new SandboxWrapFactory());
        cx.setClassShutter(new ClassShutter() {
            private final Map<String, Boolean> nameToAccepted = new HashMap<String, Boolean>();

            @Override
            public boolean visibleToScripts(String name) {
                Boolean granted = this.nameToAccepted.get(name);

                if (granted != null) {
                    return granted.booleanValue();
                }

                Class<?> staticType;
                try {
                    staticType = Class.forName(name);
                } catch (Exception exc) {
                    this.nameToAccepted.put(name, Boolean.FALSE);
                    return false;
                }

                boolean grant = shutter.allowClassAccess(staticType);
                this.nameToAccepted.put(name, Boolean.valueOf(grant));
                return grant;
            }
        });
        return cx;
    }

    class SandboxWrapFactory extends WrapFactory {
        @Override
        public Scriptable wrapNewObject(Context cx, Scriptable scope, Object obj) {
            this.ensureReplacedClass(scope, obj, null);

            return super.wrapNewObject(cx, scope, obj);
        }

        /*public Object wrap(Context cx, Scriptable scope, Object obj, Class<?> staticType) {
            this.ensureReplacedClass(scope, obj, staticType);

            return super.wrap(cx, scope, obj, staticType);
        }*/
        //TODO: fix or verify that!

        @Override
        public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class<?> staticType) {
            final Class<?> type = this.ensureReplacedClass(scope, javaObject, staticType);

            return new NativeJavaObject(scope, javaObject, staticType) {
                private final Map<String, Boolean> instanceMethodToAllowed = new HashMap<String, Boolean>();

                @Override
                public Object get(String name, Scriptable scope) {
                    Object wrapped = super.get(name, scope);

                    if (wrapped instanceof BaseFunction) {
                        String id = type.getName() + "." + name;
                        Boolean allowed = this.instanceMethodToAllowed.get(id);

                        if (allowed == null) {
                            boolean allow = shutter.allowMethodAccess(type, javaObject, name);
                            this.instanceMethodToAllowed.put(id, allowed = Boolean.valueOf(allow));
                        }

                        if (!allowed.booleanValue()) {
                            return NOT_FOUND;
                        }
                    } else {
                        // NativeJavaObject + only boxed primitive types?
                        if (!shutter.allowFieldAccess(type, javaObject, name)) {
                            return NOT_FOUND;
                        }
                    }

                    return wrapped;
                }
            };
        }

        //

        private final Set<Class<?>> replacedClasses = new HashSet<Class<?>>();

        private Class<?> ensureReplacedClass(Scriptable scope, Object obj, Class<?> staticType) {
            // final Class< ? > type = (staticType == null && obj != null) ?
            // obj.getClass() : staticType;

            final Class<?> type = (obj != null) ? obj.getClass() : staticType;

            if (!type.isPrimitive() && !type.getName().startsWith("java.") && this.replacedClasses.add(type)) {
                this.replaceJavaNativeClass(type, scope);
            }

            return type;
        }

        private void replaceJavaNativeClass(final Class<?> type, Scriptable scope) {
            Scriptable topScope = scope;
            while (!(topScope instanceof ImporterTopLevel)) {
                topScope = topScope.getParentScope();
            }

            Object clazz = Context.jsToJava(ScriptableObject.getProperty(topScope, "Packages"), Object.class);
            Object holder = null;
            for (String part : type.getName().split("\\.")) {
                holder = clazz;
                clazz = ScriptableObject.getProperty((Scriptable) clazz, part);
            }
            NativeJavaClass nativeClass = (NativeJavaClass) clazz;

            nativeClass = new NativeJavaClass(topScope, type) {
                @Override
                public Object get(String name, Scriptable start) {
                    Object wrapped = super.get(name, start);

                    if (wrapped instanceof BaseFunction) {
                        if (!shutter.allowStaticMethodAccess(type, name)) {
                            return NOT_FOUND;
                        }
                    } else {
                        // NativeJavaObject + only boxed primitive types?
                        if (!shutter.allowStaticFieldAccess(type, name)) {
                            return NOT_FOUND;
                        }
                    }

                    return wrapped;
                }
            };

            ScriptableObject.putProperty((Scriptable) holder, type.getSimpleName(), nativeClass);
            ScriptableObject.putProperty(scope, type.getSimpleName(), nativeClass);
        }
    }
}
