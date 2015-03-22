package proxy;

/**
 * Created by root on 15-3-22.
 */
public class TestProxy {


    public static void main(String[]args){
        Animal animal=new Cat();
        AnimalFactory animalFactory=new AnimalFactory(animal);
        animal=animalFactory.getAnimalProxy();
        System.out.println(animal.getClass().getName());
        animal.say("xxx");
    }
}
