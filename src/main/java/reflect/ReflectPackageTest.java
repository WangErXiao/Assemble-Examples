package reflect;

/**
 * Created by root on 15-3-28.
 */
public class ReflectPackageTest {
    public static void main(String[]args){
        String packageName=ReflectPackageTest.class.getPackage().getName();
        String packageVersion=ReflectPackageTest.class.getPackage().getImplementationVersion();
        System.out.println("package name:"+packageName+" package version:"+packageVersion);
    }
}
