package reflect.asm;

import aj.org.objectweb.asm.ClassWriter;
import aj.org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by yaozb on 15-4-9.
 */
public class TestBean {
    public void helloAop(){
            System.out.println("Hello Aop");
    }
  /*  public void helloAopASM(){
        ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "halloAop", "()V", null, null);
        mv.visitCode();
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "reflect/asm/AopInterceptor", "beforeInvoke", "()V");
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("Hello Aop");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "org/more/test/asm/AopInterceptor", "afterInvoke", "()V");
        mv.visitInsn(Opcodes.RETURN);
        //mv.visitMaxs(0, 0);
        mv.visitEnd();

    }*/
}
