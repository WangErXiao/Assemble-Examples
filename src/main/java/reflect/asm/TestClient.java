package reflect.asm;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by yaozb on 15-4-9.
 * 参照 site http://my.oschina.net/u/1166271/blog/162796
 */
public class TestClient {
    public static void main(String[]args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Method mdDefineClass = java.lang.ClassLoader.class.getDeclaredMethod("defineClass",
                String.class, byte[].class, int.class, int.class);
        mdDefineClass.setAccessible(true);

        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("reflect/asm/TestBean.class");

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassAdapter classAdapter = new AopClassAdapter(classWriter);

        ClassReader classReader = new ClassReader(is);
        classReader.accept(classAdapter, ClassReader.SKIP_DEBUG);

        byte[] code = classWriter.toByteArray();
        ClassLoader loader=Thread.currentThread().getContextClassLoader();
        mdDefineClass.invoke(loader,"reflect.asm.TestBean_Tmp",code,0,code.length);
        Class<?> c = Class.forName("reflect.asm.TestBean_Tmp");
        TestBean testBean = (TestBean)c.newInstance();
        testBean.helloAop();

    }
}
