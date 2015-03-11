package effective.chapter02;

/**
 * Created by root on 15-3-10.
 */
public final class FinalClass {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void say(){
        System.out.println(name);
    }
}
