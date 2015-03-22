package annotation;

import java.lang.annotation.Annotation;

/**
 * Created by root on 15-3-22.
 */
public class TestAnnotation {
    public static void main(String[]args){
        Dog dog=new Dog("xxx","2222");
        if(dog.getClass().getAnnotations().length>0){
          Annotation[]annotations= dog.getClass().getAnnotations();
          for (Annotation annotation:annotations){
              if(annotation.annotationType().equals(MyAnnotation.class)){
                   MyAnnotation myAnnotation=(MyAnnotation)annotation;
                   dog.setName(myAnnotation.name());
              }
          }
        }
        System.out.println(dog.getName());
    }
}
