package org.mydemo;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class SkywalkingTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        // 只拦截SkywalkingTest类
        if(!"org/mydemo/SkywalkingTest".equals(className)) {
            // 返回null, 代表没有修改此字节码
            return null;
        }
        // 获取Javassist Class池
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath(new ClassClassPath(className.getClass()));
        try {
            // 获取Class池中的SkywalkingTest CtClass对象
            CtClass ctClass = cp.getCtClass(className.replace("/", "."));
            // 找到对应的main方法
            CtMethod method = ctClass.getDeclaredMethod("main");
            // 增加本地变量 - long类型的beginTime
            method.addLocalVariable("beginTime", CtClass.longType);
            // 在main方法之前增加代码: long beginTime = System.currentTimeMillis();
            method.insertBefore("long beginTime = System.currentTimeMillis();");
            // 在main方法之后打印出耗时
            method.insertAfter("System.out.print(\"总共耗时:\");");
            method.insertAfter("System.out.println(System.currentTimeMillis() - beginTime);");
            // 返回修改过后的字节码数据
            return ctClass.toBytecode();


        } catch (NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }
        // 返回null, 代表没有修改此字节码
        return null;
    }
}
