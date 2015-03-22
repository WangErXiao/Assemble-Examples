package annotation;

/**
 * Created by root on 15-3-22.
 */
@MyAnnotation(name="dog-wangerxiao")
public class Dog {
    private String type;
    private String age;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dog(String type, String age) {
        this.type = type;
        this.age = age;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
}
