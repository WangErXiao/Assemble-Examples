package effective.chapter02;

/**
 * Created by root on 15-3-10.
 */
public class TestFianlClass {
    final FinalClass finalClass=new FinalClass();
    public static void main(String[]args){
        FinalClass finalClass=new FinalClass();
        finalClass.setName("xx");
        finalClass.say();
        TestFianlClass testFianlClass=new TestFianlClass();
        testFianlClass.finalClass.setName("xx");
        testFianlClass.finalClass.say();
        testFianlClass.finalClass.setName("xxxx");
        testFianlClass.finalClass.say();
    }
}
