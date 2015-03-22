package proxy;

/**
 * Created by root on 15-3-22.
 */
public class Cat implements Animal {
    @Override
    public void say(String content) {
        System.out.println("cat:"+content);
    }
}
