package reflect.asm;


import org.objectweb.asm.*;

/**
 * Created by yaozb on 15-4-9.
 */
public class AopClassAdapter extends ClassAdapter implements Opcodes {
    public AopClassAdapter(ClassVisitor classVisitor) {
        super(classVisitor);
    }

    @Override
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
           //更改类名，并使新类继承原有的类。
           super.visit(version, access, name + "_Tmp", signature, name, interfaces);
           //输出一个默认的构造方法
            MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "<init>",
                    "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, name, "<init>", "()V");
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();

    }

    @Override
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        if ("<init>".equals(name))
            return null;//放弃原有类中所有构造方法
        if (!name.equals("helloAop"))
            return null;// 只对halloAop方法执行代理
        MethodVisitor mv = super.visitMethod(access, name,
                desc, signature, exceptions);
        return new AopMethod(mv);
    }
}
class AopMethod extends MethodAdapter implements Opcodes {
    public AopMethod(MethodVisitor mv) {
        super(mv);
    }
    public void visitCode() {
        super.visitCode();
        this.visitMethodInsn(INVOKESTATIC, "reflect/asm/AopInterceptor", "beforeInvoke", "()V");
    }
    public void visitInsn(int opcode) {
        if (opcode == RETURN) {
            mv.visitMethodInsn(INVOKESTATIC, "reflect/asm/AopInterceptor", "afterInvoke", "()V");
        }
        super.visitInsn(opcode);
    }
}
