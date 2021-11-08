package com.example;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

public class PerformMonitorTransformer implements ClassFileTransformer {

    private static final Set<String> classNameSets = new HashSet<>();

    static {
        // 直接定位
        classNameSets.add("com.example.HelloService");
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            String currentClassName = className.replaceAll("/", ".");
            if (!classNameSets.contains(currentClassName)) {
                return null;
            }
            System.out.println("transform: [" + currentClassName + "]");
            CtClass ctClass = ClassPool.getDefault().get(currentClassName);
            CtBehavior[] methods = ctClass.getDeclaredBehaviors();
            for (CtBehavior method : methods) {
                // 增强方法
                enhanceMethod(method);
            }
            return ctClass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void enhanceMethod(CtBehavior method) throws Exception {
        if (method.isEmpty()) {
            return;
        }
        String methodName = method.getName();
        if ("main".equalsIgnoreCase(methodName)) {
            return;
        }
        if (null == method.getAnnotation(Monitor.class)) {
            return;
        }
        final StringBuilder source = new StringBuilder();
        source.append("{")
                // 前置增强，增加时间戳
                .append("long start = System.currentTimeMillis();\n")
                // 保留原有的代码逻辑
                .append("$_ = $proceed($$);\n")
                // 后置增强
                .append("System.out.println(\"cost：[" + methodName + "] \" + (System.currentTimeMillis() - start) + \"ms\");\n")
                .append("}");

        ExprEditor editor = new ExprEditor() {
            @Override
            public void edit(MethodCall methodCall) throws CannotCompileException {
                methodCall.replace(source.toString());
            }
        };
        method.instrument(editor);

    }
}
