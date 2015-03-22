package classloader;

/**
 * Created by root on 15-3-22.
 */
public class TestClassForName {
    public static void main(String[]axx){
        try {
            Class<Main>clazz= (Class<Main>) Class.forName("classloader.Main");
            System.out.println("xxxxxxxxx");
            Main main=clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
