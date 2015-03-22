package proxy;

import java.lang.reflect.Proxy;
/**
 * Created by root on 15-3-22.
 */
public class AnimalFactory {
    private Animal a;
    public AnimalFactory(Animal a) {
        this.a = a;
    }
    public Animal getAnimalProxy(){
        return (Animal)Proxy.newProxyInstance(AnimalFactory.class.getClassLoader(), a.getClass().getInterfaces(), new AniamlProxy(a));
    }
}
