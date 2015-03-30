package reflect.javassist;

import javassist.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by root on 15-3-30.
 */
public class JavassitDemo {
    public static void main(String[]args) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ClassPool classPool=ClassPool.getDefault();
        CtClass ctClass=classPool.makeClass("com.yao.Dog");
        StringBuffer body=null;
        CtField ctField=new CtField(classPool.get("java.lang.String"),"name",ctClass);
        ctField.setModifiers(Modifier.PRIVATE);
        ctClass.addMethod(CtNewMethod.setter("setName",ctField));
        ctClass.addMethod(CtNewMethod.getter("getName", ctField));
        ctClass.addField(ctField, CtField.Initializer.constant("dahuang"));
        CtConstructor ctConstructor=new CtConstructor(new CtClass[]{}, ctClass);
        body=new StringBuffer();
        body.append("{\n name=\"me da\";\n}");
        ctConstructor.setBody(body.toString());
        ctClass.addConstructor(ctConstructor);

        CtMethod ctMethod=new CtMethod(CtClass.voidType,"execute",new CtClass[]{},ctClass);
        ctMethod.setModifiers(Modifier.PUBLIC);
        body=new StringBuffer();
        body.append("{\n System.out.println(name);");
        body.append("\n System.out.println(\"execute ok\");");
        body.append("\n return ;");
        body.append("\n}");
        ctMethod.setBody(body.toString());
        ctClass.addMethod(ctMethod);

        Class<?> c=ctClass.toClass();
        Object o=c.newInstance();
        Method method=o.getClass().getMethod("execute", new Class[]{});
        //调用字节码生成类的execute方法
        method.invoke(o, new Object[]{});
    }
}
